package com.tuempresa.facturacion.modelo;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import com.tuempresa.facturacion.anotaciones.*;

import lombok.*;

@Entity @Getter @Setter

public class Producto {

	@Id
	@Column(length=9)
	int numero;
	
	@Column(length=50) @Required
	String descripcion;
	
	@ManyToOne
	(fetch=FetchType.LAZY,
	optional=true)
	@DescriptionsList
	Categoria categoria;
	
	@Money
	BigDecimal precio;
	
	@Files
	@Column(length=32)
	String fotos;
	
	@TextArea
	String observaciones;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@DescriptionsList
	Autor autor;
	
	@Column(length=13)
	@ISBN(buscar = false)
	String isbn;
}
