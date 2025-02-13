package org.educa.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.educa.entity.PasajeroEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoPasajero {
    private PasajeroEntity pasajero;
    private List<VueloWithPrecio> vuelos;
}
