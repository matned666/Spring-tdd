package eu.mrndesign.matned.controller;

import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProdById(@PathVariable Long id){
        Product product = productService.findById(id);
        if(product!= null) return ResponseEntity.ok().body(product);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> postProduct(@RequestBody Product product){
        return ResponseEntity.created(URI.create("/product/"+product.getId())).body(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        if(productService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putProdct(@PathVariable Long id, @RequestBody Product updated){
        Product toUpdated = productService.update(id, updated);
        if(toUpdated != null){
            return ResponseEntity.ok().body(toUpdated);
        }else return ResponseEntity.notFound().build();
    }

}
