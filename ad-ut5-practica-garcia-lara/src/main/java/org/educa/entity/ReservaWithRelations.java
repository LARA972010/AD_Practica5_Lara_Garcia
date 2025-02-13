package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaWithRelations implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("vuelo")
    private VueloEntity vuelo;
    @SerializedName("pasajero")
    private PasajeroEntity pasajero;
    @SerializedName("asiento")
    private String asiento;
    @SerializedName("estado")
    private String estado;
    @SerializedName("precio")
    private BigDecimal precio;
}
