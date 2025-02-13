package org.educa;

import org.educa.entity.ReservaWithRelations;
import org.educa.service.ReservaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Actividad2 {
    public static void main(String[] args) {
        ReservaService reservaService = new ReservaService();
        try(Scanner sc = new Scanner(System.in)) {
            System.out.println("Introduce un cantidad");
            BigDecimal cantidad = sc.nextBigDecimal();
            sc.nextLine();
            List<ReservaWithRelations> reservas = reservaService.findReservasByCantidad(cantidad);
            reservas.forEach(System.out::println);
        }

    }
}
