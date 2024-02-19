package com.tuempresa.facturacion.modelo;
 
import javax.persistence.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

import lombok.*;
 
@MappedSuperclass
@Getter @Setter
public class Identificable {
 
    @Id @GeneratedValue(generator="system-uuid") @Hidden
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(length=32)
    String oid;
 
}
