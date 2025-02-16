package org.educa.dao;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.PasajeroEntity;
import org.educa.entity.ReservaEntity;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.InfoPasajero;
import org.educa.wrappers.VueloWithPrecio;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasajeroDaoImpl implements PasajeroDao {
    private static final String PASAJEROS = "pasajeros";

    /**
     * @param pasaporte
     * @return
     */
    @Override
    public InfoPasajero findReservasPasaport(String pasaporte) {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(PASAJEROS);

            // Pipeline de agregación
            List<Bson> pipeline = Arrays.asList(
                    // Filtramos por pasaporte
                    Aggregates.match(Filters.eq("pasaporte", pasaporte)),

                    // Realizamos el join con reservas para obtener las reservas del pasajero
                    Aggregates.lookup("reservas", "_id", "pasajero_id", "reservas"),

                    // Proyectamos solo los campos que nos interesan: nombre, pasaporte, y las reservas con precio y estado
                    Aggregates.project(Projections.fields(
                            Projections.include("nombre", "pasaporte"), // Incluir nombre y pasaporte
                            Projections.computed("reservas", // Crear el campo 'reservas' con el estado y precio de cada reserva
                                    new Document("$map", new Document("input", "$reservas") // Iteramos sobre el array de reservas
                                                    .append("as", "reserva")
                                                    .append("in", new Document("precio", "$$reserva.precio")
                                                            .append("estado", "$$reserva.estado"))) // Proyectamos solo el precio y estado de cada reserva
                            ))
                    ));

            // Ejecutar la agregación
            MongoCursor<Document> cursor = mongoCollection.aggregate(pipeline).iterator();

            if (cursor.hasNext()) {
                Document document = cursor.next();
                // Convertimos el documento resultante a PasajeroEntity
                PasajeroEntity pasajero = new Gson().fromJson(document.toJson(), PasajeroEntity.class);

                // Convertimos las reservas a VueloWithPrecio
                List<ReservaEntity> reservas = new Gson().fromJson(document.get("reservas").toString(),
                        new TypeToken<List<ReservaEntity>>(){}.getType());

                // Convertimos cada reserva en un VueloWithPrecio
                List<VueloWithPrecio> vuelosConPrecio = reservas.stream()
                        .map(reserva -> new VueloWithPrecio(null, reserva.getPrecio(), reserva.getEstado())) // Pasamos null para el vuelo, ya que no lo necesitamos
                        .collect(Collectors.toList());

                return new InfoPasajero(pasajero, vuelosConPrecio); // Retornamos el InfoPasajero con la lista de VueloWithPrecio
            }
        }
        return null;
    }



}
