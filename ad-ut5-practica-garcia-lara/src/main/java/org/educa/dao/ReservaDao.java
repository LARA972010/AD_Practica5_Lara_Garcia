package org.educa.dao;

import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;

import java.math.BigDecimal;
import java.util.List;

public interface ReservaDao {
    Integer insert(ReservaEntity reserva);

    Long updateReser(ReservaEntity reservaToUpdate);

    ReservaEntity finById(int id);

    Long deleteReser(int id);

    List<ReservaEntity> findAll();

    List<ReservaWithRelations> getReservasPrice(BigDecimal cantidad);
}
