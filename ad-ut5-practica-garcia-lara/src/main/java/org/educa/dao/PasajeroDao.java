package org.educa.dao;

import org.educa.wrappers.InfoPasajero;

public interface PasajeroDao {
    InfoPasajero findReservasPasaport(String pasaporte);
}
