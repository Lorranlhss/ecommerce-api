package com.ecommerce.domain.valueobjects;

import java.util.Objects;

/**
 * Value Object para representar endereços.
 * Imutável e com validações básicas.
 */
public class Address {

    private final String street;
    private final String number;
    private final String complement;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;

    private Address(String street, String number, String complement,
                    String neighborhood, String city, String state,
                    String zipCode, String country) {

        this.street = validateAndTrim(street, "Street");
        this.number = validateAndTrim(number, "Number");
        this.complement = complement != null ? complement.trim() : null;
        this.neighborhood = validateAndTrim(neighborhood, "Neighborhood");
        this.city = validateAndTrim(city, "City");
        this.state = validateAndTrim(state, "State");
        this.zipCode = validateAndTrim(zipCode, "Zip Code");
        this.country = validateAndTrim(country, "Country");
    }

    public static Builder builder() {
        return new Builder();
    }

    private String validateAndTrim(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value.trim();
    }

    // Getters
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", ").append(number);

        if (complement != null && !complement.isEmpty()) {
            sb.append(" - ").append(complement);
        }

        sb.append(", ").append(neighborhood)
                .append(", ").append(city)
                .append(" - ").append(state)
                .append(", ").append(zipCode)
                .append(", ").append(country);

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Address address = (Address) obj;
        return Objects.equals(street, address.street) &&
                Objects.equals(number, address.number) &&
                Objects.equals(complement, address.complement) &&
                Objects.equals(neighborhood, address.neighborhood) &&
                Objects.equals(city, address.city) &&
                Objects.equals(state, address.state) &&
                Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, complement, neighborhood,
                city, state, zipCode, country);
    }

    @Override
    public String toString() {
        return getFullAddress();
    }

    // Builder Pattern
    public static class Builder {
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;
        private String zipCode;
        private String country = "Brasil"; // Padrão

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public Builder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(street, number, complement, neighborhood,
                    city, state, zipCode, country);
        }
    }
}