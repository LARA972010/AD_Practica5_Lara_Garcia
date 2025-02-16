package org.educa.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.BeneficioVuelo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VueloDaoImpl implements VueloDao {
    private static final String VUELOS = "vuelos";
    private static final String RESERVAS = "reservas";
    /**
     * @return
     */
    @Override
    public List<BeneficioVuelo> obtenerBeneficiosVuelos() {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> vuelosCollection = mongoDatabase.getCollection(VUELOS);
            MongoCollection<Document> reservasCollection = mongoDatabase.getCollection(RESERVAS);

            // Pipeline para obtener los beneficios de los vuelos
            List<Bson> pipeline = Arrays.asList(
                    // Unimos las reservas con los vuelos
                    Aggregates.lookup("reservas", "_id", "vuelo_id", "reservas"),

                    // Proyectamos solo los campos necesarios
                    Aggregates.project(Projections.fields(
                            Projections.include("codigo_vuelo", "origen_id", "destino_id", "coste"), // Vuelos
                            Projections.computed("numReservas", new Document("$size", "$reservas")), // Contamos las reservas
                            Projections.computed("total", new Document("$sum", "$reservas.precio")) // Sumamos los precios
                    )),

                    // Agrupamos por vuelo (por código de vuelo)
                    Aggregates.group(
                            "$codigo_vuelo",
                            Aggregates.first("origen", "$origen_id"),
                            Aggregates.first("destino", "$destino_id"),
                            Aggregates.sum("numPasajeros", "$numReservas"), // Usamos el tamaño de las reservas
                            Aggregates.sum("total", "$total"),
                            Aggregates.first("coste", "$coste")
                    ),

                    // Proyectamos el resultado final para mostrar
                    Aggregates.project(Projections.fields(
                            Projections.include("codigo_vuelo", "origen", "destino", "numPasajeros", "total", "coste"),
                            Projections.computed("beneficio", new Document("$subtract", Arrays.asList("$total", "$coste")))
                    )),

                    // Ordenamos los resultados (opcional, si quieres ordenar por beneficio o algún otro criterio)
                    Aggregates.sort(Sorts.descending("beneficio"))
            );

            // Ejecutamos la agregación
            List<Document> result = vuelosCollection.aggregate(pipeline).into(new java.util.ArrayList<>());

            // Convertimos el resultado en una lista de objetos BeneficioVuelo
            return result.stream()
                    .map(document -> {
                        String codigoVuelo = document.getString("codigo_vuelo");
                        String origen = document.getString("origen");
                        String destino = document.getString("destino");
                        Integer numPasajeros = document.getInteger("numPasajeros");
                        BigDecimal total = new BigDecimal(document.getDouble("total"));
                        BigDecimal coste = new BigDecimal(document.getDouble("coste"));
                        BigDecimal beneficio = new BigDecimal(document.getDouble("beneficio"));
                        return new BeneficioVuelo(codigoVuelo, origen, destino, numPasajeros, total, coste, beneficio);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}