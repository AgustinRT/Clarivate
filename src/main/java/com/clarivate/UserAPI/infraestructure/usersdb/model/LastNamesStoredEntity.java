package com.clarivate.UserAPI.infraestructure.usersdb.model;

import jakarta.persistence.Column;
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
@Table(name = "last_names_stores")
@Getter
@Setter
@NoArgsConstructor
public class LastNamesStoredEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "last_names_stores_seq")
    @SequenceGenerator(sequenceName= "last_names_stores_seq", allocationSize= 1, name= "last_names_stores_seq")
    private Long id;

    @Column(name="name_user", nullable=false, unique=true)
    private String nameUser;
}
