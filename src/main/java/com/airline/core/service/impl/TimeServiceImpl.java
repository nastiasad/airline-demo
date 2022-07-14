package com.airline.core.service.impl;

import com.airline.core.service.TimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TimeServiceImpl implements TimeService {

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
