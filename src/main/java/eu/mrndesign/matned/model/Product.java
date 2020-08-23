package eu.mrndesign.matned.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Integer version;

    public Product() {
    }

    public Product(String name, String description, Integer quantity, Integer version) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.version = version;
    }

    public Product(Long id, String name, String description, Integer quantity, Integer version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.version = version;
    }
}
