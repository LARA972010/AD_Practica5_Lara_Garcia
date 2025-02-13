package org.educa.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.educa.entity.VueloEntity;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueloWithPrecio {
    private VueloEntity vuelo;
    private BigDecimal precio;
    private String estado;
}
