package org.educa;

import org.educa.service.VueloService;
import org.educa.wrappers.BeneficioVuelo;

import java.util.List;

public class Actividad4 {
    public static void main(String[] args) {
        VueloService vueloService = new VueloService();
        List<BeneficioVuelo> beneficioVueloList = vueloService.getBeneficioVuelo();
        beneficioVueloList.forEach(System.out::println);
    }
}