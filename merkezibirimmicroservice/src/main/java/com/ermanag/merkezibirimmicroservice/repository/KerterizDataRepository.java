package com.ermanag.merkezibirimmicroservice.repository;

import com.ermanag.merkezibirimmicroservice.model.KerterizData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KerterizDataRepository extends MongoRepository<KerterizData,String> {
    MongoTemplate mongoTemplate = null;

    public default List<KerterizData> findAllKerterizData() {
        return mongoTemplate.findAll(KerterizData.class);
    }

    public default KerterizData findKerterizDataById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, KerterizData.class);
    }
}
