package com.clarivate.UserAPI.infraestructure.usersdb.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable.Cities;
import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonsConverter.CitiesAttributeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "locations_stored")
@Getter
@Setter
@NoArgsConstructor
public class LocationsStoredEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "locations_stored_seq")
    @SequenceGenerator(sequenceName= "locations_stored_seq", allocationSize= 1, name= "locations_stored_seq")
    private Long id;

    @Column(name="country_name", nullable=false)
    private String countryName;

    @Column(name="state_name")
    private String stateName;

    @Convert(converter= CitiesAttributeConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="cities", nullable=false)
    private Cities cities;
}
