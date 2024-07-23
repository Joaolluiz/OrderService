package br.com.orderservice.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends RepresentationModel<ProductDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "It is necessary the product code.")
    @Size(max = 10, message = "The code must have a maximum of 10 characters.")
    private String sku;

    @NotBlank(message = "It is necessary the product name.")
    @Size(max = 30, message = "The name must have a maximum of 30 characters.")
    private String name;

    @Size(max = 300, message = "The description must have a maximum of 200 characters.")
    private String description;

    @NotNull(message = "the value cannot be null.")
    @Positive
    private BigDecimal value;

    @NotNull(message = "the quantity cannot be null")
    private Integer quantity;
}
