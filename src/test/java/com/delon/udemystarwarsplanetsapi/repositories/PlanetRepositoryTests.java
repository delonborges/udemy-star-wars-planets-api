package com.delon.udemystarwarsplanetsapi.repositories;

import com.delon.udemystarwarsplanetsapi.entities.Planet;
import com.delon.udemystarwarsplanetsapi.utils.QueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static com.delon.udemystarwarsplanetsapi.utils.PlanetConstants.PLANET;
import static com.delon.udemystarwarsplanetsapi.utils.PlanetConstants.TATOOINE;
import static com.delon.udemystarwarsplanetsapi.utils.TestTags.UNIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag(UNIT)
@DataJpaTest
public class PlanetRepositoryTests {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static Stream<Arguments> providesInvalidPlanets() {
        return Stream.of(Arguments.of(new Planet(null, "climate", "terrain")),
                         Arguments.of(new Planet("name", null, "terrain")),
                         Arguments.of(new Planet("name", "climate", null)),
                         Arguments.of(new Planet(null, null, "terrain")),
                         Arguments.of(new Planet(null, "climate", null)),
                         Arguments.of(new Planet("name", null, null)),
                         Arguments.of(new Planet(null, null, null)),
                         Arguments.of(new Planet("", "climate", "terrain")),
                         Arguments.of(new Planet("name", "", "terrain")),
                         Arguments.of(new Planet("name", "climate", "")),
                         Arguments.of(new Planet("", "", "terrain")),
                         Arguments.of(new Planet("", "climate", "")),
                         Arguments.of(new Planet("name", "", "")),
                         Arguments.of(new Planet("", "", "")));
    }

    @AfterEach
    public void cleanUp() {
        PLANET.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        var planet = planetRepository.save(PLANET);

        var result = testEntityManager.find(Planet.class, planet.getId());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(planet.getName());
        assertThat(result.getClimate()).isEqualTo(planet.getClimate());
        assertThat(result.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @ParameterizedTest
    @MethodSource("providesInvalidPlanets")
    public void createPlanet_WithInvalidData_ThrowsException(Planet planet) {
        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        var result = planetRepository.findById(planet.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByNonExistingId_ReturnsEmpty() {
        var result = planetRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        var planet = testEntityManager.persistFlushFind(PLANET);
        var result = planetRepository.findByName(planet.getName());

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByNonExistingName_ReturnsEmpty() {
        var result = planetRepository.findByName("name");

        assertThat(result).isEmpty();
    }

    @Test
    @Sql(scripts = "/import_planets.sql")
    public void listPlanets_ReturnsFilteredPlanets() {
        var queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        var queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

        var responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
        var responseWithFilters = planetRepository.findAll(queryWithFilters);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(3);
        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        var query = QueryBuilder.makeQuery(new Planet());

        var response = planetRepository.findAll(query);

        assertThat(response).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
        var planet = testEntityManager.persistFlushFind(PLANET);

        planetRepository.deleteById(planet.getId());

        var removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();
    }
}

