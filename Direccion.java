package com.tuempresa.facturacion.modelo;

import javax.persistence.*;

import lombok.*;

@Embeddable @Getter @Setter

public class Direccion {
	
	@Column(length=30)
	String viaPublica;
	
	@Column(length=5)
	int codigoPostal;
	
	@Column(length=20)
	String municipio;
	
	@Column(length=30)
	String provincia;

}
