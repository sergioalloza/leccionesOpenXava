package com.tuempresa.facturacion.acciones;
 
import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;
 
public class EliminarParaFacturacion extends ViewBaseAction {
 
    public void execute() throws Exception {
        if (!getView().getMetaModel().containsMetaProperty("eliminado")) {
            executeAction("CRUD.delete"); // LLamamos a la acción estándar
            return;                       //   de OpenXava para borrar
        }

        // Cuando "eliminado" existe usamos nuestra propia lógica de borrado
        Map<String, Object> valores = new HashMap<>();
        valores.put("eliminado", true);
        MapFacade.setValues(getModelName(), getView().getKeyValues(), valores);
        resetDescriptionsCache();
        addMessage("object_deleted", getModelName());
        getView().clear();
        getView().setKeyEditable(true);
        getView().setEditable(false);
    }
}