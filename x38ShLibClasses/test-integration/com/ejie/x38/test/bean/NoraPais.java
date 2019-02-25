/*
* Copyright 2012 E.J.I.E., S.A.
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

package com.ejie.x38.test.bean;

import javax.validation.groups.Default;

import org.hibernate.validator.constraints.NotEmpty;



/**
 *  * NoraPais generated by UDA, 16-ene-2012 13:17:19.
 * @author UDA
 */

public class NoraPais  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    //Clave compuesta
			@NotEmpty(message="validacion.required",groups={Default.class})
            private String id;
			@NotEmpty(message="validacion.required")
            private String dsO;

	/** Method 'NoraPais'.
	*
	*/
    public NoraPais() {
    }
   /** Method 'NoraPais'.
   * @param id Long
   * @param dsO String
   */
   public NoraPais(String id, String dsO) {

           this.id = id;		
           this.dsO = dsO;		
    }

	/**
     * Method 'getId'.
     *
     * @return Long
     */
	
	
	public String getId() {
		return this.id;
	}
	
	/**
	 * Method 'setId'.
	 *
	 * @param id Long
	 * @return
	 */
	
	public void setId(String id) {
		this.id = id;
	}
	/**
     * Method 'getDsO'.
     *
     * @return String
     */
	
	
	public String getDsO() {
		return this.dsO;
	}
	
	/**
	 * Method 'setDsO'.
	 *
	 * @param dsO String
	 * @return
	 */
	
	public void setDsO(String dsO) {
		this.dsO = dsO;
	}
	/**
     * Method 'getDsE'.
     *
     * @return String
     */
	
	

	/**
	 * Intended only for logging and debugging.
	 * 
	 * Here, the contents of every main field are placed into the result.
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getName()).append(" Object { " ); 
		//Clave compuesta

		result.append("[ id: ").append(this.id).append(" ]");
		result.append(", [ dsO: ").append(this.dsO).append(" ]");
		result.append("}");
		return result.toString();
	}


}

