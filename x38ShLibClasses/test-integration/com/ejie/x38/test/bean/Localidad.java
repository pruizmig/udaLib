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
package com.ejie.x38.test.bean;


import java.math.BigDecimal;

/**
*  * Localidad generated by UDA 1.0, 29-jul-2011 9:08:11.
* @author UDA
 */

public class Localidad  implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
         private BigDecimal code;
         private Comarca comarca;
         private String descEs;
         private String descEu;
         private String css;

/** Method 'Localidad'.
*
*/
    public Localidad() {
    }

    public Localidad(BigDecimal code) {
    	 this.code = code;
    }
    
    /** Method 'Localidad'.
    * @param  code BigDecimal
    * @param  descEs String
    * @param  descEu String
    * @param  css String
    * @param  comarca Comarca
    */
   public Localidad(BigDecimal code, String descEs, String descEu, String css, Comarca comarca) {	

           this.code = code;
           this.comarca = comarca;
           this.descEs = descEs;
           this.descEu = descEu;
           this.css = css;
    }

    /**
     * Method 'getCode'.
     *
     * @return BigDecimal
     */
	 
    
    public BigDecimal getCode() {
       return this.code;
     }

    /**
     * Method 'setCode'.
     *
     * @param code BigDecimal
     */
    
    public void setCode(BigDecimal code) {
      this.code = code;
    }
    	
    /**
     * Method 'getComarca'.
     *
     * @return Comarca
     */
    
    public Comarca getComarca() {
       return this.comarca;
     }

    /**
     * Method 'setComarca'.
     *
     * @param comarca Comarca
     */
    
    public void setComarca(Comarca comarca) {
      this.comarca = comarca;
    }
    	
    /**
     * Method 'getDescEs'.
     *
     * @return String
     */
	 
    
    public String getDescEs() {
       return this.descEs;
     }

    /**
     * Method 'setDescEs'.
     *
     * @param descEs String
     */
    
    public void setDescEs(String descEs) {
      this.descEs = descEs;
    }
    	
    /**
     * Method 'getDescEu'.
     *
     * @return String
     */
	 
    
    public String getDescEu() {
       return this.descEu;
     }

    /**
     * Method 'setDescEu'.
     *
     * @param descEu String
     */
    
    public void setDescEu(String descEu) {
      this.descEu = descEu;
    }
    	
    /**
     * Method 'getCss'.
     *
     * @return String
     */
	 
    
    public String getCss() {
       return this.css;
     }

    /**
     * Method 'setCss'.
     *
     * @param css String
     */
    
    public void setCss(String css) {
      this.css = css;
    }
    	

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
				result.append(" [ code: ").append(this.code).append(" ]");
				result.append(", [ descEs: ").append(this.descEs).append(" ]");
				result.append(", [ descEu: ").append(this.descEu).append(" ]");
				result.append(", [ css: ").append(this.css).append(" ]");
	result.append("}");
    return result.toString();
     }


}

