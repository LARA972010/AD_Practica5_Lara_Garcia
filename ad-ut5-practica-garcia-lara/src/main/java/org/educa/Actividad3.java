package org.educa;

import org.educa.service.ReservaService;
import org.educa.wrappers.InfoPasajero;

import java.util.Scanner;

public class Actividad3 {
    public static void main(String[] args) {
        ReservaService reservaService = new ReservaService();
        try(Scanner sc = new Scanner(System.in)) {
            System.out.println("Introduce un pasaporte");
            String pasaporte = sc.nextLine();
            InfoPasajero reserva = reservaService.findReservasByPasaporte(pasaporte);
            System.out.println(reserva.getPasajero());
            reserva.getVuelos().forEach(System.out::println);
        }

    }
}
