package com.tuempresa.facturacion.modelo;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@Entity @Getter @Setter
@View(extendsView = "super.DEFAULT",
	members ="pedidos{pedidos}")

@View(name = "SinClienteNiPedidos",
	members =
		"anyo, numero, fecha;"
		+ "detalles;"
		+ "observaciones"
)

@Tab(baseCondition = "${eliminado} = false")
@Tab(name="Eliminado", baseCondition = "${eliminado} = true") // Tab con nombre
public class Factura extends DocumentoComercial {
	
	@OneToMany(mappedBy = "factura")
	@CollectionView("SinClienteNiFactura")
	Collection<Pedido> pedidos;
	
	//@Hidden // No se mostrará por defecto en las vistas y los tabs
	//@Column(columnDefinition="BOOLEAN DEFAULT FALSE") // Para llenar con falses en lugar de con nulos
	//boolean eliminado;

}
