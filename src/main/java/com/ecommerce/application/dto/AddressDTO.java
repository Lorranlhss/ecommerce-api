package com.ecommerce.application.dto;

import com.ecommerce.domain.valueobjects.Address;

/**
 * DTO para transferência de dados de endereços.
 */
public record AddressDTO(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        String country
) {

    public static AddressDTO from(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDTO(
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry()
        );
    }

    public Address toDomain() {
        return Address.builder()
                .street(street)
                .number(number)
                .complement(complement)
                .neighborhood(neighborhood)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .country(country != null ? country : "Brasil")
                .build();
    }
}