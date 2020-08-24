package eu.mrndesign.matned.service;

import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(long l) {
        return productRepository.findProductById(l);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public boolean deleteById(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Product update(Long id, Product updatedData) {
        Product toUpdate = productRepository.findProductById(id);
        if (toUpdate != null) {
            toUpdate.setName(updatedData.getName());
            toUpdate.setDescription(updatedData.getDescription());
            toUpdate.setQuantity(updatedData.getQuantity());
            toUpdate.setVersion(updatedData.getVersion());
            return toUpdate;
        }else return null;
    }
}
