package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroEntity implements Serializable {
    @SerializedName("_id")
    private Integer id;
    private String nombre;
    private String nacionalidad;
    private String pasaporte;
}
