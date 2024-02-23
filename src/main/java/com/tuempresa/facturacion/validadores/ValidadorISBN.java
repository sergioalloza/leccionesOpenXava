package com.tuempresa.facturacion.validadores;

import javax.validation.*;

import org.openxava.util.*;

import com.tuempresa.facturacion.anotaciones.*;

public class ValidadorISBN implements ConstraintValidator<ISBN, Object>{
	
	private static org.apache.commons.validator.routines.ISBNValidator
	validador = new org.apache.commons.validator.routines.ISBNValidator();
	
	public void initialize(ISBN isbn) {
		
	}
	
	public boolean isValid(Object valor, ConstraintValidatorContext contexto) {
		if(Is.empty(valor)) return true;
		return validador.isValid(valor.toString());
	}

}
