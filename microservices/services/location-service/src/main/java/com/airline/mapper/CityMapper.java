package com.airline.mapper;

import com.airline.model.City;
import com.airline.payload.request.CityRequest;
import com.airline.payload.response.CityResponse;

public class CityMapper {

    public static City toEntity(CityRequest cityRequest) {

        if (cityRequest == null) {return null;}

        return City.builder()
                .name(cityRequest.getName())
                .cityCode(cityRequest.getCityCode())
                .countryCode(cityRequest.getCountryCode())
                .countryName(cityRequest.getCountryName())
                .regionCode(cityRequest.getRegionCode())
                .timeZoneId(cityRequest.getTimeZoneOffset())
                .build();
    }

    public static CityResponse toResponse(City city){
        if(city == null){return null;}

        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .cityCode(city.getCityCode())
                .countryCode(city.getCountryCode())
                .countryName(city.getCountryName())
                .regionCode(city.getRegionCode())
//                .timeZoneOffset(city.getTimeZoneId())
                .build();
    }

    public static City updateEntity(City city, CityRequest request) {
        if (request.getName() != null) {
            city.setName(request.getName().trim());
        }
        if (request.getCityCode() != null) {
            city.setCityCode(request.getCityCode().toUpperCase().trim());
        }
        if (request.getCountryCode() != null) {
            city.setCountryCode(request.getCountryCode().toUpperCase().trim());
        }
        if (request.getCountryName() != null) {
            city.setCountryName(request.getCountryName().trim());
        }
        if(request.getRegionCode() != null) {
            city.setRegionCode(request.getRegionCode().toUpperCase().trim());
        }
        return city;
    }

}
