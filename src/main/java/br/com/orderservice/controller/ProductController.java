package br.com.orderservice.controller;

import br.com.orderservice.dto.ProductDTO;
import br.com.orderservice.service.ProductService;
import br.com.orderservice.util.MediaType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public Page<ProductDTO> listAllProducts(@PageableDefault(size = 10)Pageable pageable) {
        return productService.findAllProducts(pageable);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON)
    public ProductDTO findProductById(@PathVariable(value = "id") Long id) {
        return productService.findProductById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product, UriComponentsBuilder uriBuilder) {

        ProductDTO productDTO = productService.saveProduct(product);
        URI address = uriBuilder.path("api/product/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(address).body(productDTO);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO product, @PathVariable(value = "id") Long id) {
        ProductDTO updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable(value = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
