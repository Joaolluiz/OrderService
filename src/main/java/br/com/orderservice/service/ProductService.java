package br.com.orderservice.service;

import br.com.orderservice.dto.ProductDTO;
import br.com.orderservice.exceptions.RequiredObjectIsNullException;
import br.com.orderservice.exceptions.ResourceNotFoundException;
import br.com.orderservice.mappers.ProductMapper;
import br.com.orderservice.model.Product;
import br.com.orderservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ProductService {

    private Logger logger = Logger.getLogger(ProductService.class.getName());

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    public Page<ProductDTO> findAllProducts(Pageable pageable) {

        logger.info("Finding all products.");

        return productRepository
                .findAll(pageable)
                .map(product -> productMapper.toProductDTO(product));
    }

    public ProductDTO findProductById(Long id) {

        logger.info("Finding a Product.");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No products for this id!"));

        return productMapper.toProductDTO(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {

        logger.info("Saving a product.");

        Product product = productMapper.toProduct(productDTO);
        return productMapper.toProductDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        if (productDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a product.");

        Product product = update(id, productDTO);

        return productMapper.toProductDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        logger.info("Deleting a product.");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No product for this ID!"));

        productRepository.delete(product);
    }

    public Product update(Long id, ProductDTO productDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No product for this id!"));

        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setValue(productDTO.getValue());
        product.setQuantity(productDTO.getQuantity());

        return product;
    }
}
