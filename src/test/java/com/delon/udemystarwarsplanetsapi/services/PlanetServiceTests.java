package com.delon.udemystarwarsplanetsapi.services;

import com.delon.udemystarwarsplanetsapi.entities.Planet;
import com.delon.udemystarwarsplanetsapi.repositories.PlanetRepository;
import com.delon.udemystarwarsplanetsapi.utils.QueryBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static com.delon.udemystarwarsplanetsapi.utils.PlanetConstants.INVALID_PLANET;
import static com.delon.udemystarwarsplanetsapi.utils.PlanetConstants.PLANET;
import static com.delon.udemystarwarsplanetsapi.utils.TestTags.UNIT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Tag(UNIT)
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTests {

    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        var planet = planetService.create(PLANET);

        assertThat(planet).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));

        var planet = planetService.get(1L);

        assertThat(planet).isNotEmpty();
        assertThat(planet).isEqualTo(Optional.of(PLANET));
    }

    @Test
    public void getPlanet_ByNonExistentId_ReturnsEmpty() {
        when(planetRepository.findById(999L)).thenReturn(Optional.empty());

        var planet = planetService.get(999L);

        assertThat(planet).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        var planet = planetService.getByName(PLANET.getName());

        assertThat(planet).isNotEmpty();
        assertThat(planet).isEqualTo(Optional.of(PLANET));
    }

    @Test
    public void getPlanet_ByNonExistentName_ReturnsEmpty() {
        final String nonExistentName = "NonExistentName";
        when(planetRepository.findByName(nonExistentName)).thenReturn(Optional.empty());

        var planet = planetService.getByName(nonExistentName);

        assertThat(planet).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        var planetsList = new ArrayList<Planet>() {
            {
                add(PLANET);
            }
        };
        var query = QueryBuilder.makeQuery(new Planet(PLANET.getTerrain(), PLANET.getName()));

        when(planetRepository.findAll(query)).thenReturn(planetsList);
        var planets = planetService.list(PLANET.getTerrain(), PLANET.getName());

        assertThat(planets).isNotEmpty();
        assertThat(planets).hasSize(1);
        assertThat(planets.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        Example<Planet> noPlanetExample = any();
        when(planetRepository.findAll(noPlanetExample)).thenReturn(Collections.emptyList());

        var planets = planetService.list(PLANET.getTerrain(), PLANET.getName());

        assertThat(planets).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_DoesNotThrowsAnyException() {
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithNonExistentId_ThrowsException() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(999L);

        assertThatThrownBy(() -> planetService.remove(999L)).isInstanceOf(RuntimeException.class);
    }
}
