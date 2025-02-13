package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AeropuertoEntity implements Serializable {
    @SerializedName("_ID")
    private Integer id;
    private String nombre;
    private String ciudad;
    private String pais;
    @SerializedName("codigo_IATA")
    private String codigoIATA;
}
