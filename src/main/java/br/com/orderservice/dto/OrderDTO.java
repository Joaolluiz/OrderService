package br.com.orderservice.dto;

import br.com.orderservice.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @Size(max = 50, message = "The client name must have a maximum of 50 characters.")
    private String clientName;

    @NotNull
    @Size(max = 13, message = "The phone number must have a maximum of 13 digits.")
    private String phoneNumber;

    @NotNull
    @Size(max = 100, message = "The address must have a maximum of 100 characters.")
    private String address;

    @NotNull
    private Date createdDate;

    @Positive
    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    private Status status;

    private List<OrderProductDTO> products = new ArrayList<>();

}
