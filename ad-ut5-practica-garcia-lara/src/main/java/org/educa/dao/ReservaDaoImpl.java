package org.educa.dao;




import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.PasajeroEntity;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.entity.VueloEntity;
import org.educa.settings.DatabaseSettings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Filter;


public class ReservaDaoImpl implements ReservaDao {
    private static final String RESERVAS = "reservas";

    /**
     * @param reserva 
     * @return
     */
    @Override
    public Integer insert(ReservaEntity reserva) {
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase= mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(RESERVAS);

            Document doc = new Document()
                    .append("_id", reserva.getId())
                    .append("vuelo_id", reserva.getVueloId())
                    .append("pasajero_id", reserva.getPasajeroId())
                    .append("asiento", reserva.getAsiento())
                    .append("estado", reserva.getEstado())
                    .append("precio", reserva.getPrecio().doubleValue());

            mongoCollection.insertOne(doc);
            return reserva.getId();

        }catch(Exception e){
            e.printStackTrace();
            return null;

        }

    }

    /**
     * @param reservaToUpdate 
     * @return
     */
    @Override
    public Long updateReser(ReservaEntity reservaToUpdate) {

        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(RESERVAS);

            Bson filter = Filters.eq("_id", reservaToUpdate.getId());


            Document docDatos = new Document()
                    .append("precio", reservaToUpdate.getPrecio())
                    .append("asiento", reservaToUpdate.getAsiento())
                    .append("estado", reservaToUpdate.getEstado());

            Document docUpdate = new Document("$set", docDatos);

            UpdateResult result= mongoCollection.updateOne(filter,docUpdate);

            return result.getModifiedCount();

        }catch(Exception e){
            System.out.println(e);
            return null;
        }


    }

    /**
     * @return
     */
    @Override
    public ReservaEntity finById(int id) {
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(RESERVAS);

            Bson filters = Filters.eq("_id", id);

            Document doc = mongoCollection.find(filters).first();

            if(doc != null){
                ReservaEntity reservaEntity = new ReservaEntity();
                reservaEntity.setId(doc.getInteger("_id"));
                reservaEntity.setVueloId(doc.getInteger("vuelo_id"));
                reservaEntity.setPasajeroId(doc.getInteger("pasajero_id"));
                reservaEntity.setAsiento(doc.getString("asiento"));
                reservaEntity.setEstado(doc.getString("estado"));
                Object priceObject = doc.get("precio");
                if (priceObject instanceof org.bson.types.Decimal128) {
                    reservaEntity.setPrecio(((org.bson.types.Decimal128) priceObject).bigDecimalValue());
                }

                return reservaEntity;

            }else return null;


        }catch(Exception e){
            System.out.println(e);
            return null;
        }




    }

    /**VUELOS
     * @param id 
     * @return
     */
    @Override
    public Long deleteReser(int id) {
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase =mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(RESERVAS);

            Bson filter = Filters.eq("_id", id);

            DeleteResult result = mongoCollection.deleteOne(filter);
            return result.getDeletedCount();

        }

    }

    /**
     * @return 
     */
    @Override
    public List<ReservaEntity> findAll() {
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(RESERVAS);

            List<ReservaEntity> list = new ArrayList<>();

            for(Document doc : mongoCollection.find()){
                ReservaEntity reserEntity = new ReservaEntity();
                reserEntity.setId(doc.getInteger("_id"));
                reserEntity.setVueloId(doc.getInteger("vuelo_id"));
                reserEntity.setPasajeroId(doc.getInteger("pasajero_id"));
                reserEntity.setAsiento(doc.getString("asiento"));
                reserEntity.setEstado(doc.getString("estado"));

                Object priceObject = doc.get("precio");
                if (priceObject instanceof org.bson.types.Decimal128) {
                    reserEntity.setPrecio(((org.bson.types.Decimal128) priceObject).bigDecimalValue());
                }


                list.add(reserEntity);


            }

            return list;

        }

    }

    /**
     * @param cantidad 
     * @return
     */
    @Override
    public List<ReservaWithRelations> getReservasPrice(BigDecimal cantidad) {
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(RESERVAS);

            List<Bson> pipeline = Arrays.asList(
                    Aggregates.match(Filters.gte("precio", cantidad)),
                    Aggregates.lookup("vuelos", "vuelo_id", "_id", "vuelo"),
                    Aggregates.lookup("pasajeros", "pasajero_id", "_id", "pasajero"),
                    Aggregates.unwind("$vuelo"),
                    Aggregates.unwind("$pasajero")
            );

            AggregateIterable<Document> results = mongoCollection.aggregate(pipeline);

            List<ReservaWithRelations> reservas = new ArrayList<>();

            for (Document doc : results) {
                ReservaWithRelations reserva = new ReservaWithRelations();

                reserva.setId(doc.getInteger("_id"));

                BigDecimal precio = BigDecimal.valueOf(doc.getDouble("precio")).setScale(2, RoundingMode.HALF_UP);
                reserva.setPrecio(precio);

                reserva.setAsiento(doc.getString("asiento"));
                reserva.setEstado(doc.getString("estado"));

                Document vueloDoc = (Document) doc.get("vuelo");
                if (vueloDoc != null) {
                    VueloEntity vuelo = new VueloEntity();
                    vuelo.setId(vueloDoc.getInteger("_id"));
                    vuelo.setCodigoVuelo(vueloDoc.getString("codigo_vuelo"));
                    vuelo.setOrigenId(vueloDoc.getInteger("origen_id"));
                    vuelo.setDestinoId(vueloDoc.getInteger("destino_id"));
                    vuelo.setDuracion(vueloDoc.getInteger("duracion"));
                    vuelo.setEstado(vueloDoc.getString("estado"));
                    vuelo.setFecha(vueloDoc.getString("fecha"));
                    vuelo.setCoste(BigDecimal.valueOf(vueloDoc.getDouble("coste")).setScale(2, RoundingMode.HALF_UP));
                    reserva.setVuelo(vuelo);
                }

               
                Document pasajeroDoc = (Document) doc.get("pasajero");
                if (pasajeroDoc != null) {
                    PasajeroEntity pasajero = new PasajeroEntity();
                    pasajero.setId(pasajeroDoc.getInteger("_id"));
                    pasajero.setNombre(pasajeroDoc.getString("nombre"));
                    pasajero.setNacionalidad(pasajeroDoc.getString("nacionalidad"));
                    pasajero.setPasaporte(pasajeroDoc.getString("pasaporte"));
                    reserva.setPasajero(pasajero);
                }

                reservas.add(reserva);
            }

            return reservas;
        }
    }


}
