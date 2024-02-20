package com.tuempresa.facturacion.modelo;

import lombok.*;

@Getter @Setter
public class Incidencia {
	
	@Getter @Setter
	int cantidad;
	
	@Getter @Setter
	int precio;
	
	public int getImporte() {
		return cantidad * precio;
	}

}
