package com.tuempresa.facturacion.modelo;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@Embeddable @Getter @Setter
public class Detalle {
	
	int cantidad;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	Producto producto;
	
	@Stereotype("DINERO")
	@Depends("producto.numero, cantidad")
	public BigDecimal getImporte() {
		if (producto == null || producto.getPrecio() == null) return BigDecimal.ZERO;
		return new BigDecimal(cantidad).multiply(producto.getPrecio());
	}

}
