package com.tuempresa.facturacion.modelo;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.beanutils.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.tuempresa.facturacion.acciones.*;

import lombok.*;

@Entity @Getter @Setter
@View(extendsView = "super.DEFAULT",
		members = "diasEntregaEstimados, entregado," +
				"factura { factura }"
)
@View(name = "SinClienteNiFactura",
	members =
		"anyo, numero, fecha;"
		+ "detalles;"
		+ "observaciones"
)

@Tab(baseCondition = "${eliminado} = false")
@Tab(name="Eliminado", baseCondition = "${eliminado} = true") // Tab con nombre
public class Pedido extends DocumentoComercial{
	
	@ManyToOne
	@ReferenceView("SinClienteNiPedidos")
	@OnChange(MostrarOcultarCrearFactura.class)
	@OnChangeSearch(BuscarAlCambiarFactura.class)
	@SearchAction("Pedido.buscarFactura") // Define nuestra acción para buscar facturas
	Factura factura;
	
	@Depends("fecha")
	public int getDiasEntregaEstimados() {
		if (getFecha().getDayOfYear() < 15) {
			return 20 - getFecha().getDayOfYear();
		}
		if (getFecha().getDayOfWeek() == DayOfWeek.SUNDAY) return 2;
		if (getFecha().getDayOfWeek() == DayOfWeek.SATURDAY) return 3;
		return 1;
	}
	
	@Column(columnDefinition = "INTEGER DEFAULT 1")
	int diasEntrega;
	
	@PrePersist @PreUpdate
	private void recalcularDiasEntrega() {
		setDiasEntrega(getDiasEntregaEstimados());
	}
	
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	@OnChange(MostrarOcultarCrearFactura.class) // Añade esto
	boolean entregado;
	
	@AssertTrue(message = "pedido_debe_estar_entregado")
	private boolean isEntregadoParaEstarEnFactura() {
		return factura == null || isEntregado();
	}
	
	//Método alternativo para validación
	/*public void setFactura(Factura factura) {
		if (factura != null && !isEntregado()) {
			throw new javax.validation.ValidationException(
					XavaResources.getString(
							"pedido_debe_estar_entregado",
							getAnyo(),
							getNumero())
					);
		}
		
		this.factura = factura;
	} */
	
	@PreRemove
	private void validarPreBorrar() {
		if(factura != null) {
			throw new javax.validation.ValidationException(
					XavaResources.getString(
							"no_se_puede_borrar_pedido_con_factura"));
		}
		
	}
	
	public void setEliminado(boolean eliminado) {
        if (eliminado) validarPreBorrar(); // Llamamos a la validación explícitamente
        super.setEliminado(eliminado);
    }
	
	public void crearFactura()
		    throws CrearFacturaException // Una excepción de aplicación (1)
		{
		    if (this.factura != null) { // Si ya tiene una factura no podemos crearla
		        throw new CrearFacturaException( 
		            "pedido_ya_tiene_factura"); // Admite un id de 18n como argumento
		    }
		    if (!isEntregado()) { // Si el pedido no está entregado no podemos crear la factura
		        throw new CrearFacturaException("pedido_no_entregado");
		    }
		    try {
		        Factura factura = new Factura(); 
		        BeanUtils.copyProperties(factura, this); 
		        factura.setOid(null); 
		        factura.setFecha(LocalDate.now()); 
		        factura.setDetalles(new ArrayList<>(getDetalles())); 
		        XPersistence.getManager().persist(factura);
		        this.factura = factura; 
		    }
		    catch (Exception ex) { // Cualquier excepción inesperada (2)
		        throw new SystemException( // Se lanza una excepción runtime (3)
		            "imposible_crear_factura", ex);
		    }
		}
	
	public void copiarDetallesAFactura() { 
        factura.getDetalles().addAll(getDetalles()); // Copia las líneas
        factura.setIva(factura.getIva().add(getIva())); // Acumula el IVA
        factura.setImporteTotal( // y el importe total
		    factura.getImporteTotal().add(getImporteTotal()));
    }
	
	// Este método ha de devolver true para que este pedido sea válido
    @AssertTrue(message="cliente_pedido_factura_coincidir") 
    private boolean isClienteFacturaCoincide() {
    	return factura == null || // factura es opcional
    		factura.getCliente().getNumero() == getCliente().getNumero();
    }


}
