package com.airline.core.service;

import com.airline.core.model.Route;

import java.util.List;

public interface RouteService {

    List<Route> getRoutes(Long airlineId, boolean availableOnly);
}
