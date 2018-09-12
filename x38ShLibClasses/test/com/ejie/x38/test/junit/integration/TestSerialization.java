package com.ejie.x38.test.junit.integration;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ejie.x38.IframeXHREmulationFilter;
import com.ejie.x38.UdaFilter;
import com.ejie.x38.serialization.UdaMappingJackson2HttpMessageConverter;
import com.ejie.x38.test.common.model.Coche;
import com.ejie.x38.test.common.model.Empleado;
import com.ejie.x38.test.common.model.Marca;
import com.ejie.x38.test.control.SerializationController;
import com.ejie.x38.util.DateTimeManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebAppConfiguration
@ContextConfiguration(classes = { TestConfig.class })

@RunWith(SpringJUnit4ClassRunner.class)
public class TestSerialization {

	@Resource
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private IframeXHREmulationFilter iframeXHREmulationFilter;

	@Autowired
	private UdaFilter udaFilter;

	@Autowired
	private SerializationController serializationController;

	@Autowired
	private UdaMappingJackson2HttpMessageConverter udaMappingJackson2HttpMessageConverter;

	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(serializationController).addFilter(udaFilter, "/*")
				.addFilter(iframeXHREmulationFilter, "/*").build();
		this.objectMapper = udaMappingJackson2HttpMessageConverter.getObjectMapper();
	}

	/**
	 * @param object
	 * @return
	 * @throws JsonProcessingException
	 */
	private String serialize(Object object) throws JsonProcessingException {
		return this.objectMapper.writeValueAsString(object);
	}

	@Test
	public void test() {
		try {
			mockMvc.perform(get("/serialization/test")).andExpect(status().is(200))
					.andExpect(content().string("{\"respuesta\":\"ok\"}"));
		} catch (Exception e) {
			fail("Exception al realizar el test de conexión GET con el controller de prueba de serialización [/serialization/test]");
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void serialize() {
		Locale localeEs = new Locale("es");
		Locale localeEu = new Locale("eu");

		Empleado eneko = new Empleado("Eneko");
		Empleado laura = new Empleado("Laura");
		Marca foo = new Marca("Foo", null, Arrays.asList(eneko, laura));
		Coche crx5 = new Coche("CRX-5", foo);
		crx5.setCoste(BigDecimal.valueOf(9123000.0123456789));

		String strFechaConstruccion = "02/06/1995 13:45:11";
		String strFecNacEneko = "01/01/1981";
		String strFecNacLaura = "12/12/1992";
		SimpleDateFormat sdtf = DateTimeManager.getTimestampFormat(localeEs);
		SimpleDateFormat sdf = DateTimeManager.getDateTimeFormat(localeEs);

		try {
			crx5.setFechaConstruccion(new Timestamp(sdtf.parse(strFechaConstruccion).getTime()));
			eneko.setFechaNacimiento(new Date(sdf.parse(strFecNacEneko).getTime()));
			laura.setFechaNacimiento(new Date(sdf.parse(strFecNacLaura).getTime()));
		} catch (ParseException e) {
			fail("ParseException inicializando el objeto para el caso de prueba");
		}

		String jsonResEs = "";
		String jsonResEu = "";
		try {
			String modelo = crx5.getModelo();
			crx5.setModelo(modelo + "_ES");
			jsonResEs = this.serialize(crx5);
			crx5.setModelo(modelo + "_EU");
			jsonResEu = this.serialize(crx5);
		} catch (JsonProcessingException e1) {
			fail("Exception serializando el objeto que se va a comprobar en la prueba");
		}

		String jsonReqEs = "{\"modelo\":\"CRX-5_ES\",\"marca\":{\"nombre\":\"Foo\",\"empleados\":[{\"nombre\":\"Eneko\",\"fechaNacimiento\":\"01/01/1981\"},{\"nombre\":\"Laura\",\"fechaNacimiento\":\"12/12/1992\"}]},\"fechaConstruccion\":\"02/06/1995 13:45:11\",\"coste\":\"9.123.000,01234568\"}";
		String jsonReqEu = "{\"modelo\":\"CRX-5_EU\",\"marca\":{\"nombre\":\"Foo\",\"empleados\":[{\"nombre\":\"Eneko\",\"fechaNacimiento\":\"1981/01/01\"},{\"nombre\":\"Laura\",\"fechaNacimiento\":\"1992/12/12\"}]},\"fechaConstruccion\":\"1995/06/02 13:45:11\",\"coste\":\"9.123.000,01234568\"}";

		try {

			mockMvc.perform(post("/serialization/serialize")

					.contentType(MediaType.APPLICATION_JSON)

					.accept(MediaType.ALL)

					.locale(localeEs)

					.content(jsonReqEs))

					.andExpect(status().is(200))

					.andExpect(content().string(jsonResEs));
		} catch (Exception e) {
			fail("Exception al realizar la petición POST con el controller de prueba de serialización en castellano [/serialization/serialize]");
		}

		try {
			mockMvc.perform(post("/serialization/serialize")

					.contentType(MediaType.APPLICATION_JSON)

					.accept(MediaType.ALL)

					.locale(localeEu)

					.content(jsonReqEu))

					.andExpect(status().is(200))

					.andExpect(content().string(jsonResEu));
		} catch (Exception e) {
			fail("Exception al realizar la petición POST con el controller de prueba de serialización en euskera [/serialization/serialize]");
		}
	}

}
