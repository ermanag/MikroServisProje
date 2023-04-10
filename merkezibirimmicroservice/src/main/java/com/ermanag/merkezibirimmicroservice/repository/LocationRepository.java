package com.ermanag.merkezibirimmicroservice.repository;

import com.ermanag.merkezibirimmicroservice.model.Location;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
    MongoTemplate mongoTemplate = null;

    public default List<Location> findAllLocations() {
        return mongoTemplate.findAll(Location.class);
    }

    public default Location findLocationById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Location.class);
    }
}
