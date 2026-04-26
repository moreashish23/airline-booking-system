package com.airline.service.impl;

import com.airline.mapper.AirportMapper;
import com.airline.model.Airport;
import com.airline.model.City;
import com.airline.payload.request.AirportRequest;
import com.airline.payload.response.AirportResponse;
import com.airline.repository.AirportRepository;
import com.airline.repository.CityRepository;
import com.airline.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest request) throws Exception {

        if(airportRepository.findByIataCode(request.getIataCode()).isPresent()){
            throw new Exception("Airport with IATA code " + request.getIataCode() + " already exists.");
        }

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new Exception("City with ID " + request.getCityId() + " not found."));
        Airport airport = AirportMapper.toEntity(request);
        airport.setCity(city);
        Airport savedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    public AirportResponse getAirportById(Long id) throws Exception {
        Airport airport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport with ID " + id + " not found.")
        );
        return AirportMapper.toResponse(airport);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest request) throws Exception {
        Airport existingAirport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport not exist with id" + id)
        );
        if(request.getIataCode() != null
        && !existingAirport.getIataCode().equals(request.getIataCode())
        && airportRepository.findByIataCode(request.getIataCode()).isPresent()
        ){
            throw  new Exception("Airport with Iata Code Already Exist");
        }
        AirportMapper.updateEntity(existingAirport, request);
        Airport updatedAirport = airportRepository.save(existingAirport);
        return AirportMapper.toResponse(updatedAirport);
    }

    @Override
    public void deleteAirport(Long id) throws Exception {
        Airport airport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport with ID " + id + " not found.")
        );
        airportRepository.delete(airport);
    }

    @Override
    public List<AirportResponse> getAirportsByCityId(Long cityId) {
        return airportRepository.findByCityId(cityId).stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }
}
