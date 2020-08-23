package eu.mrndesign.matned.service;

import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(long l) {
        return productRepository.findProductById(l);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }
}
