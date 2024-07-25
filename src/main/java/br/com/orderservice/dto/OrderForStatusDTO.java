package br.com.orderservice.dto;

import br.com.orderservice.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;
}
