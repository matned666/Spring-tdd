package eu.mrndesign.matned.repository;

import eu.mrndesign.matned.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findProductById(int i);
}
