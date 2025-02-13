package org.educa.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficioVuelo {
    //La pantalla tiene que mostrar, el código del vuelo, aeropuerto origen y aeropuerto destino,
    // numero de pasajeros que vuelan, total de dinero recaudado, coste y beneficio final
    private String codigoVuelo;
    private String origen;
    private String destino;
    private Integer numPasajeros;
    private BigDecimal total;
    private BigDecimal coste;
    private BigDecimal beneficio;

}
