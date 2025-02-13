package org.educa;

import org.educa.entity.ReservaEntity;
import org.educa.service.ReservaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Actividad1 {

    public static void main(String[] args) {
        System.out.println("Bienvenido al CRUD de reserva: que desea hacer");
        ReservaService reservaService = new ReservaService();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Menú de Reservas:");
            System.out.println("1. Crear reserva");
            System.out.println("2. Actualizar reserva");
            System.out.println("3. Eliminar reserva");
            System.out.println("4. Consultar reserva por ID");
            System.out.println("5. Consultar todas las reservas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                case 1:
                    System.out.println("Crear reserva...");
                    ReservaEntity reserva = crearReserva(scanner);
                    Integer newId = reservaService.save(reserva);
                    System.out.println("Insertada reserva con Id: " + newId);
                    break;
                case 2:
                    System.out.print("Ingrese el ID de la reserva a actualizar: ");
                    int idActualizar = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea
                    System.out.println("Actualizar reserva con ID: " + idActualizar);
                    ReservaEntity reservaToUpdate = reservaService.findById(idActualizar);
                    if (reservaToUpdate != null) {
                        actualizaReserva(scanner, reservaToUpdate);
                        Long updateNumber = reservaService.update(reservaToUpdate);
                        System.out.println("Reservas actualizadas " + updateNumber);
                    } else {
                        System.out.println("Reserva no encontrada");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese el ID de la reserva a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea
                    System.out.println("Eliminar reserva con ID: " + idEliminar);
                    Long deleteNumber = reservaService.delete(idEliminar);
                    System.out.println("Reservas eliminadas " + deleteNumber);
                    break;
                case 4:
                    System.out.print("Ingrese el ID de la reserva a consultar: ");
                    int idConsultar = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea
                    System.out.println("Consultar reserva con ID: " + idConsultar);
                    System.out.println(reservaService.findById(idConsultar));
                    break;
                case 5:
                    System.out.println("Consultar todas las reservas...");
                    List<ReservaEntity> reservas = reservaService.findAll();
                    reservas.forEach(System.out::println);
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static void actualizaReserva(Scanner scanner, ReservaEntity reservaToUpdate) {
        System.out.print("Ingrese el nuevo precio de la reserva (actual " + reservaToUpdate.getPrecio() + "): ");
        BigDecimal precio = scanner.nextBigDecimal();
        scanner.nextLine(); // Consumir la nueva línea
        //si el precio es mayor que 0 actualizo el precio, si no dejo el precio actual
        if (precio.doubleValue()>0) {
            reservaToUpdate.setPrecio(precio);
        }
        System.out.print("Ingrese el nuevo asiento de la reserva (actual " + reservaToUpdate.getAsiento() + "): ");
        String asiento = scanner.nextLine();
        if (asiento != null && !asiento.isEmpty()) {
            reservaToUpdate.setAsiento(asiento);
        }
        System.out.print("Ingrese el nuevo estado de la reserva (Confirmada/Cancelada) (actual " + reservaToUpdate.getEstado() + "): ");
        String estado = scanner.nextLine();
        if (estado != null && !estado.isEmpty()) {
            reservaToUpdate.setEstado(estado);
        }
    }

    private static ReservaEntity crearReserva(Scanner scanner) {
        ReservaEntity reservaEntity = new ReservaEntity();
        System.out.print("Ingrese el ID de la reserva: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea
        reservaEntity.setId(id);
        System.out.print("Ingrese el precio de la reserva: ");
        BigDecimal precio = scanner.nextBigDecimal();
        scanner.nextLine(); // Consumir la nueva línea
        reservaEntity.setPrecio(precio);
        System.out.print("Ingrese el asiento de la reserva: ");
        String asiento = scanner.nextLine();
        reservaEntity.setAsiento(asiento);
        System.out.print("Ingrese el ID del pasajero de la reserva: ");
        int idPasajero = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea
        reservaEntity.setPasajeroId(idPasajero);
        System.out.print("Ingrese el ID del vuelo de la reserva: ");
        int idVuelo = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea
        reservaEntity.setVueloId(idVuelo);
        reservaEntity.setEstado("Confirmada");
        return reservaEntity;
    }
}
