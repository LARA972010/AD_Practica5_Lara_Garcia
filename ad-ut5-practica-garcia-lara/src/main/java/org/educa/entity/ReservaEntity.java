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
public class ReservaEntity implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("vuelo_id")
    private Integer vueloId;
    @SerializedName("pasajero_id")
    private Integer pasajeroId;
    private String asiento;
    private String estado;
    private BigDecimal precio;
}
