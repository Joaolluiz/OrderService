package br.com.orderservice.mappers;

import br.com.orderservice.dto.ProductDTO;
import br.com.orderservice.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ProductMapper {

    private final ModelMapper mapper;

    public ProductMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Product toProduct(ProductDTO vo) {
        return mapper.map(vo, Product.class);
    }

    public ProductDTO toProductDTO(Product product)
    {
        return mapper.map(product, ProductDTO.class);
    }

    public List<Product> toProductList(List<ProductDTO> products) {
        return products.stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> toProductDTOList(List<Product> products) {
        return products.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }
}
