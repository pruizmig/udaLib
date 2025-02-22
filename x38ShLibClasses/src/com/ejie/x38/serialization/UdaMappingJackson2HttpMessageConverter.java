/*
 * Copyright 2011 E.J.I.E., S.A.
 *
 * Licencia con arreglo a la EUPL, Versión 1.1 exclusivamente (la «Licencia»);
 * Solo podrá usarse esta obra si se respeta la Licencia.
 * Puede obtenerse una copia de la Licencia en
 *
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito,
 * el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL»,
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones
 * que establece la Licencia.
 */
package com.ejie.x38.serialization;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.ejie.x38.util.StackTraceManager;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * Sobreescritura de MappingJacksonHttpMessageConverter que permite leer y
 * escribir objetos JSON mediante el ObjectMapper de Jackson.
 * 
 * Gestiona el uso del ObjectMapper por defecto de Jackson o el personalizado
 * por UDA de acuerdo a las propiedades almacenadas en el thread mediante el
 * Filtro de UDA.
 * 
 * El ObjectMapper personalizado de UDA permite:
 * 
 * - Determinar las propiedades a serializar a partir de la informacion
 * almacenada en la propiedad RUP del thread.
 * 
 * - Deserializar multiples entidades enviadas en un unico objeto JSON.
 * 
 * @author UDA
 * 
 */
public class UdaMappingJackson2HttpMessageConverter extends
		MappingJackson2HttpMessageConverter {

	protected final Logger logger = LoggerFactory
			.getLogger(UdaMappingJackson2HttpMessageConverter.class);

	/**
	 * ObjectMapper personalizado de UDA.
	 */
	private ObjectMapper udaObjectMapper = new ObjectMapper();

	/**
	 * UdaModule utilizado para configurar el ObjectMapper personalizado.
	 */
	private UdaModule udaModule;
	
	/**
	 * Inicializa los componentes necesarios una vez instanciada la clase. En
	 * concreto configura el ObjectMapper personalizado mediante el UdaModule.
	 */
	@PostConstruct
	public void initialize() {
		if (udaModule != null) {
			udaObjectMapper.registerModule(udaModule);
		
			// Se realiza la configuracion del serializador
			if (udaModule.getSerializationFeature()!=null){
				for (SerializationFeature feature : udaModule.getSerializationFeature().keySet()) {
					if (udaModule.getSerializationFeature().get(feature)){
						udaObjectMapper.enable(feature);
						this.getObjectMapper().enable(feature);
					
					}else{
						udaObjectMapper.disable(feature);
						this.getObjectMapper().disable(feature);
					}
				}
			}
			
			// Se realiza la configuracion del deserializador
			if (udaModule.getDeserializationFeature()!=null){
				for (DeserializationFeature feature : udaModule.getDeserializationFeature().keySet()) {
					if (this.udaModule.getDeserializationFeature().get(feature)){
						udaObjectMapper.enable(feature);
						this.getObjectMapper().enable(feature);
						
					}else{
						udaObjectMapper.disable(feature);
						this.getObjectMapper().disable(feature);
					}
				}
			}
			
			// Se realiza la configuracion de las inclusion
			if (udaModule.getSerializationInclusions()!=null){
				for (Include inclusion : udaModule.getSerializationInclusions()) {
					udaObjectMapper.setSerializationInclusion(inclusion);
					this.getObjectMapper().setSerializationInclusion(inclusion);
				}
			}
		}
		
	}

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		if (!ThreadSafeCache.getMap().isEmpty() && ThreadSafeCache.getMap().keySet().contains("RUP")) {
			this.writeInternal(object, outputMessage);
		} else {
			super.writeInternal(object, type, outputMessage);
		}
		
	}
	
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator jsonGenerator = udaObjectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
		try {
			if (!ThreadSafeCache.getMap().isEmpty() && ThreadSafeCache.getMap().keySet().contains("RUP")) {
				logger.info("UDA's Serialization Mechanism is being triggered.");
				udaObjectMapper.writeValue(jsonGenerator, object);
			} else {
				logger.info("Spring's Default Object Mapper searialization is being triggered.");
				super.writeInternal(object, outputMessage);
			}
		} catch (Exception ex) {
			logger.error(StackTraceManager.getStackTrace(ex));
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		try {
			if (!ThreadSafeCache.getMap().isEmpty()
					&& ThreadSafeCache.getMap().keySet()
							.contains("RUP_MULTI_ENTITY")) {
				logger.info("UDA's MultiBean deserialization Mechanism is being triggered.");
				return this.udaObjectMapper.readValue(inputMessage.getBody(),
						clazz);
			} else {
				logger.info("Spring's Default Object Mapper deserialization is being triggered.");
				return super.readInternal(clazz, inputMessage);
			}
		} catch (Exception ex) {
			logger.error(StackTraceManager.getStackTrace(ex));
			throw new HttpMessageNotReadableException(
					"Could not deserialize JSON: " + ex.getMessage(), ex);
		}
	}

	private JsonEncoding getEncoding(MediaType contentType) {
		if (contentType != null && contentType.getCharset() != null) {
			Charset charset = contentType.getCharset();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}

	public void setUdaObjectMapper(ObjectMapper udaObjectMapper) {
		this.udaObjectMapper = udaObjectMapper;
	}

	public ObjectMapper getUdaObjectMapper() {
		return this.udaObjectMapper;
	}

	@Deprecated
	public void setJacksonJsonObjectMapper(ObjectMapper jacksonJsonObjectMapper) {
		this.udaObjectMapper = jacksonJsonObjectMapper;
	}

	public void setUdaModule(UdaModule udaModule) {
		this.udaModule = udaModule;
	}
}