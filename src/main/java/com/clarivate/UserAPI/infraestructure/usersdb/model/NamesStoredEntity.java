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
@Table(name = "names_stored")
@Getter
@Setter
@NoArgsConstructor
public class NamesStoredEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "names_stored_seq")
    @SequenceGenerator(sequenceName= "names_stored_seq", allocationSize= 1, name= "names_stored_seq")
    private Long id;

    @Column(name="name_user", nullable=false)
    private String nameUser;

    @Column(name="gender_user", nullable=false)
    private String genderUser;
}
