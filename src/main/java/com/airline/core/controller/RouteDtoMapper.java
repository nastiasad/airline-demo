package com.airline.core.controller;

import com.airline.core.dto.RouteResponse;
import com.airline.core.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface RouteDtoMapper {

    @Mapping(target = "distance", source = "haversineDistance")
    RouteResponse map(Route route);

    List<RouteResponse> map(List<Route> routes);
}
