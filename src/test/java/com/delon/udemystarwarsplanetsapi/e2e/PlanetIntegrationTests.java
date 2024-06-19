package com.delon.udemystarwarsplanetsapi.e2e;

import com.delon.udemystarwarsplanetsapi.entities.Planet;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static com.delon.udemystarwarsplanetsapi.utils.PlanetConstants.*;
import static com.delon.udemystarwarsplanetsapi.utils.TestTags.INTEGRATION;
import static org.assertj.core.api.Assertions.assertThat;

@Tag(INTEGRATION)
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void createPlanet_ReturnsCreatedPlanet() {
        var planet = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(planet.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(planet.getBody()).getId()).isNotNull();
        assertThat(planet.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(planet.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(planet.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        var planet = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(planet.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planet.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanetByName_ReturnsPlanet() {
        var planet = restTemplate.getForEntity("/planets/name/Tatooine", Planet.class);

        assertThat(planet.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(planet.getBody()).getId()).isEqualTo(1);
        assertThat(planet.getBody().getName()).isEqualTo("Tatooine");
        assertThat(planet.getBody().getClimate()).isEqualTo(TATOOINE.getClimate());
        assertThat(planet.getBody().getTerrain()).isEqualTo(TATOOINE.getTerrain());
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        var planets = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(planets.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planets.getBody()).hasSize(3);
        assertThat(planets.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByClimate_ReturnsPlanets() {
        var planets = restTemplate.getForEntity("/planets?climate=temperate, tropical", Planet[].class);

        assertThat(planets.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planets.getBody()).hasSize(1);
        assertThat(planets.getBody()[0]).isEqualTo(YAVINIV);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets() {
        var planets = restTemplate.getForEntity("/planets?terrain=grasslands, mountains", Planet[].class);

        assertThat(planets.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planets.getBody()).hasSize(1);
        assertThat(planets.getBody()[0]).isEqualTo(ALDERAAN);
    }

    @Test
    public void removePlanet_ReturnsNoContent() {
        var planets = restTemplate.exchange("/planets/".concat(String.valueOf(TATOOINE.getId())), HttpMethod.DELETE, null, Void.class);

        assertThat(planets.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
