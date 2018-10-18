package com.dorow.alexander.seniortest.service;

import com.dorow.alexander.seniortest.model.City;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class IntegrationService {

    @Value("${city.csv.temp.path}")
    private String cityTempPath;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity<String> processCSV(MultipartFile file) throws IOException {
        File tempFile = new File(String.format(cityTempPath, UUID.randomUUID()));
        Files.copy(file.getInputStream(), tempFile.toPath());
        CSVParser parser = new CSVParser(new FileReader(tempFile), CSVFormat.DEFAULT
                .withTrim()
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase());
        for (CSVRecord record : parser) {
            City city = new City();
            city.setIbgeId(record.get("ibge_id"));
            city.setUf(record.get("uf"));
            city.setName(record.get("name"));
            city.setCapital(Boolean.parseBoolean(record.get("capital")));
            city.setLon(Double.parseDouble(record.get("lon")));
            city.setLat(Double.parseDouble(record.get("lat")));
            city.setNoAccents(record.get("no_accents"));
            city.setAlternativeNames(record.get("alternative_names"));
            city.setMicroregion(record.get("microregion"));
            city.setMesoregion(record.get("mesoregion"));
            mongoTemplate.save(city);
        }
        parser.close();
        Files.delete(tempFile.toPath());
        return new ResponseEntity<>("CSV IMPORTED WITH SUCCESS", HttpStatus.OK);
    }
}
