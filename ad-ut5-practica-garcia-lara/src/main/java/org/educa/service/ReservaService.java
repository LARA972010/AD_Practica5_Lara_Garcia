package org.educa.service;

import org.educa.dao.ReservaDao;
import org.educa.dao.ReservaDaoImpl;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {

    private final ReservaDao reserDao = new ReservaDaoImpl();

    public List<ReservaEntity> findReservasByVueloId(Integer vueloId) {
        return null;
    }

    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        return null;
    }

    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        List<ReservaWithRelations> listReservas = reserDao.getReservasPrice(cantidad);

        return listReservas;
    }

    public Integer save(ReservaEntity reserva) {
    //Este servicio envia al dao la información del cliente a insertar
        return reserDao.insert(reserva);
    }

    public ReservaEntity findById(int id) {
        //buscamos la resrva por el id
        return reserDao.finById(id);
    }

    public Long update(ReservaEntity reservaToUpdate) {
        //mandamos la reservaEntity para actualizar la que indique el usuario:
        return reserDao.updateReser(reservaToUpdate);
    }

    public Long delete(int id) {
        return reserDao.deleteReser(id);

    }

    public List<ReservaEntity> findAll() {

        return reserDao.findAll();
    }
}
