package com.tuempresa.facturacion.validadores;

import javax.validation.*;
import javax.ws.rs.client.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;

import com.tuempresa.facturacion.anotaciones.*;

public class ValidadorISBN implements ConstraintValidator<ISBN, Object>{
	
	private static Log log = LogFactory.getLog(ValidadorISBN.class);
	
	private static org.apache.commons.validator.routines.ISBNValidator
	validador = new org.apache.commons.validator.routines.ISBNValidator();
	
	public void initialize(ISBN isbn) {
		
	}
	
	public boolean isValid(Object valor, ConstraintValidatorContext contexto) {
		if(Is.empty(valor)) return true;
		if(!validador.isValid(valor.toString())) return false;
		return existeISBN(valor);
	}
	
	private boolean existeISBN(Object isbn) {
		try {
			String respuesta = ClientBuilder.newClient()
					.target("http://openlibrary.org")
					.path("/api/books")
					.queryParam("jscmd", "data")
					.queryParam("format", "json")
					.queryParam("bibkeys", "ISBN" + isbn)
					.request()
					.get(String.class);
			return !respuesta.equals("{}");
					
		}
		catch(Exception ex) {
			log.warn("Imposible conectar a openlibrary.org" +
					"para validar el ISBN. Validación fallida", ex);
			return false;
		}
	}

}
