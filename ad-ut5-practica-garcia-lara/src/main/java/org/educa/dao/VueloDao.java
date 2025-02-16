package org.educa.dao;

import org.educa.wrappers.BeneficioVuelo;

import java.util.List;

public interface VueloDao {
    List<BeneficioVuelo> obtenerBeneficiosVuelos();
}
