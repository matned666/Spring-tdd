package eu.mrndesign.matned.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int quantity;
    private int version;

    public Product() {
    }

    public Product(String name, String description, int quantity, int version) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.version = version;
    }

    public Product(int id, String name, String description, int quantity, int version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.version = version;
    }
}
