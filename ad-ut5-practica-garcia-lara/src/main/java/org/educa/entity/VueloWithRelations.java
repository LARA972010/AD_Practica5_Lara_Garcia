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
public class VueloWithRelations implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("codigo_vuelo")
    private String codigoVuelo;
    @SerializedName("origen_id")
    private AeropuertoEntity origen;
    @SerializedName("destino_id")
    private AeropuertoEntity destino;
    @SerializedName("duracion")
    private Integer duracion;
    @SerializedName("estado")
    private String estado;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("coste")
    private BigDecimal coste;
}
