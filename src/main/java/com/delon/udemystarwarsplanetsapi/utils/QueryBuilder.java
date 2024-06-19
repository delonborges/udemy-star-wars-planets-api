package com.delon.udemystarwarsplanetsapi.utils;

import com.delon.udemystarwarsplanetsapi.entities.Planet;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@UtilityClass
public class QueryBuilder {

    public static Example<Planet> makeQuery(Planet planet) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        return Example.of(planet, exampleMatcher);
    }
}
