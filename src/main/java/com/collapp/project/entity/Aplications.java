package com.collapp.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Aplications") 
public class Aplications {

    @Id
    @GeneratedValue
    private Integer id;


}
