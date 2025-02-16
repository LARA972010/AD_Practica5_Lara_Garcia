package org.educa.service;

import org.educa.dao.VueloDao;
import org.educa.dao.VueloDaoImpl;
import org.educa.wrappers.BeneficioVuelo;

import java.util.List;

public class VueloService {
    private final VueloDao vueloDao = new VueloDaoImpl();
    public List<BeneficioVuelo> getBeneficioVuelo(){

        return vueloDao.obtenerBeneficiosVuelos();
    }
}
