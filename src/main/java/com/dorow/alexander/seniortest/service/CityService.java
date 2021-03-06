package com.dorow.alexander.seniortest.service;

import com.dorow.alexander.seniortest.dto.UF;
import com.dorow.alexander.seniortest.model.City;
import com.dorow.alexander.seniortest.util.GrahamScan;
import com.dorow.alexander.seniortest.util.Point2D;
import org.bson.Document;
import org.omg.PortableInterceptor.DISCARDING;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity<List<City>> getCapitalCitiesOrderedByName() {
        Query query = new Query(Criteria.where("capital").is(true));
        query.with(new Sort(Sort.Direction.ASC, "name"));
        return new ResponseEntity<>(mongoTemplate.find(query, City.class), HttpStatus.OK);
    }

    public ResponseEntity<List<UF>> getUfWithMostAndLessCities() {
        List<UF> mappedResults = getNumOfCitiesByUF();
        return new ResponseEntity<>(Arrays.asList(mappedResults.get(0), mappedResults.get(mappedResults.size() - 1)), HttpStatus.OK);
    }

    public ResponseEntity<List<UF>> getUfCityCount() {
        return new ResponseEntity<>(getNumOfCitiesByUF(), HttpStatus.OK);
    }

    private List<UF> getNumOfCitiesByUF() {
        GroupOperation sumZips = Aggregation.group("uf").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "count");
        Aggregation aggregation = Aggregation.newAggregation(sumZips, sortOperation);
        AggregationResults<UF> result = mongoTemplate.aggregate(aggregation, "city", UF.class);
        return result.getMappedResults();
    }

    public ResponseEntity<City> getCityInfoByIBGEId(String ibgeId) {
        City city = mongoTemplate.findOne(new Query(Criteria.where("ibge_id").is(ibgeId)), City.class);
        return new ResponseEntity<>(city, city != null ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<City>> getCitiesByUf(String uf) {
        List<City> cities = mongoTemplate.find(new Query(Criteria.where("uf").is(uf)), City.class);
        return new ResponseEntity<>(cities, !cities.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<String> createCity(City city) {
        mongoTemplate.save(city);
        return new ResponseEntity<>("City created with success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteCity(String ibgeId) {
        long result = mongoTemplate.remove(new Query(Criteria.where("ibge_id").is(ibgeId)), City.class).getDeletedCount();
        return new ResponseEntity<>(result == 0 ? "City not found" : "City deleted with success", result == 0 ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    public ResponseEntity<List<City>> queryByColumn(String column, String queryStr) {
        BasicQuery query = new BasicQuery(String.format("{\"%s\": {\"$regex\": \"%s\",\"$options\" : \"i\"} }", column, queryStr));
        List<City> cities = mongoTemplate.find(query, City.class);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    public ResponseEntity<Long> getColumnRegisterNumber(String column) {
        long result = mongoTemplate.getCollection("city").distinct(column, getClassByColumnName(column)).into(new ArrayList<>()).size();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Class<?> getClassByColumnName(String column) {
        switch (column.toLowerCase()) {
            case "capital":
                return Boolean.class;
            case "lat":
            case "lon":
                return Double.class;
            default:
                return String.class;

        }
    }

    public ResponseEntity<Long> getCount() {
        return new ResponseEntity<>(mongoTemplate.count(new Query(), City.class), HttpStatus.OK);
    }

    public ResponseEntity<List<City>> getTheMostDistantCities() {
        List<Point2D> points = mongoTemplate.findAll(City.class).parallelStream().map(city -> new Point2D(city.getLon(), city.getLat(), city.getIbgeId())).collect(Collectors.toList());
        double distance = 0;
        Point2D firstPoint = null;
        Point2D lastPoint = null;
        for (Point2D externalPoint : new GrahamScan(points.toArray(new Point2D[0])).hull()) {
            for (Point2D cityPoint : points) {
                Double newDistance = distanceByLatLng(externalPoint.y(), externalPoint.x(), cityPoint.y(), cityPoint.x());
                if (newDistance > distance) {
                    distance = newDistance;
                    firstPoint = externalPoint;
                    lastPoint = cityPoint;
                }
            }
        }
        Criteria criteria = new Criteria().orOperator(Criteria.where("ibge_id").is(firstPoint.ibge_id()), Criteria.where("ibge_id").is(lastPoint.ibge_id()));
        return new ResponseEntity<>(mongoTemplate.find(new Query(criteria), City.class), HttpStatus.OK);
    }


    private Double distanceByLatLng(Double lat1, Double lng1, Double lat2, Double lng2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (earthRadius * c);
    }

}
