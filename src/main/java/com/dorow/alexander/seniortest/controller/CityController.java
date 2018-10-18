package com.dorow.alexander.seniortest.controller;

import com.dorow.alexander.seniortest.dto.UF;
import com.dorow.alexander.seniortest.model.City;
import com.dorow.alexander.seniortest.service.CityService;
import com.dorow.alexander.seniortest.service.IntegrationService;
import com.sun.javafx.fxml.expression.Expression;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;

@RestController(value = "/city")
@Api(value = "CityAPI", description = "List of city endpoints", tags = {"City"})
public class CityController {

    //12. Dentre todas as cidades, obter as duas cidades mais distantes uma da outra com base
//    na localização (distância em KM em linha reta);
    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private CityService cityService;

    @ApiOperation(
            value = "1º - Import to database the city CSV",
            response = String.class
    )
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<String> importCities(@RequestParam("file") MultipartFile file) throws IOException {
        return integrationService.processCSV(file);
    }

    @ApiOperation(
            value = "2º - List of capital cities ordered by name",
            response = City[].class
    )
    @RequestMapping(value = "/capital-cities", method = RequestMethod.GET)
    public ResponseEntity<List<City>> getCapitalCitiesOrderedByName() {
        return cityService.getCapitalCitiesOrderedByName();
    }

    @ApiOperation(
            value = "3º - UF with most and less cities",
            response = UF[].class
    )
    @RequestMapping(value = "/uf-max-min-cities", method = RequestMethod.GET)
    public ResponseEntity<List<UF>> getUfWithMostAndLess() {
        return cityService.getUfWithMostAndLessCities();
    }

    @ApiOperation(
            value = "4º - UF city count",
            response = UF[].class
    )
    @RequestMapping(value = "/uf-city-count", method = RequestMethod.GET)
    public ResponseEntity<List<UF>> getUfCityCount() {
        return cityService.getUfCityCount();
    }

    @ApiOperation(
            value = "5º - Get city info by IBGE ID",
            response = City.class
    )
    @RequestMapping(value = "/city-info-by-ibge", method = RequestMethod.GET)
    public ResponseEntity<City> getCityInfoByIBGEId(@RequestParam String ibgeId) {
        return cityService.getCityInfoByIBGEId(ibgeId);
    }

    @ApiOperation(
            value = "6º - Get cities by UF",
            response = City[].class
    )
    @RequestMapping(value = "/city-by-uf", method = RequestMethod.GET)
    public ResponseEntity<List<City>> getCitiesByUf(@RequestParam String uf) {
        return cityService.getCitiesByUf(uf);
    }

    @ApiOperation(
            value = "7º - Allow to create a new city",
            response = String.class
    )
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createNewCity(@RequestBody City city) {
        return cityService.createCity(city);
    }

    @ApiOperation(
            value = "8º - Allow to delete a city",
            response = String.class
    )
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> createNewCity(@RequestParam String ibgeId) {
        return cityService.deleteCity(ibgeId);
    }

    @ApiOperation(
            value = "9º - query by CSV column ",
            response = City[].class
    )
    @RequestMapping(value = "/query-by-column", method = RequestMethod.GET)
    public ResponseEntity<List<City>> queryByColumn(@RequestParam String column, @RequestParam String query) {
        return cityService.queryByColumn(column, query);
    }

    @ApiOperation(
            value = "10º - Column distinguished register number",
            response = City[].class
    )
    @RequestMapping(value = "/column-register-number", method = RequestMethod.GET)
    public ResponseEntity<Long> getColumnRegisterNumber(@RequestParam String column) {
        return cityService.getColumnRegisterNumber(column);
    }

    @ApiOperation(
            value = "11º - Get cities count",
            response = City[].class
    )
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseEntity<Long> getCount() {
        return cityService.getCount();
    }

    @ApiOperation(
            value = "12º - Get the most distant cities",
            response = City[].class
    )
    @RequestMapping(value = "/most-distant", method = RequestMethod.GET)
    public ResponseEntity<Long> getTheMostDistantCities() {
        return cityService.getTheMostDistantCities();
    }

}
