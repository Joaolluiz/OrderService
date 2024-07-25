package br.com.orderservice.model;

import br.com.orderservice.enums.Status;
import jakarta.persistence.*;
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
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "client_name")
    private String clientName;

    @NotNull
    @Size(max = 13)
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Size(max = 100)
    private String address;

    @NotNull
    @Column(name = "created_date")
    private Date createdDate;

    @NotNull
    @Positive
    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    private Status status; //updateStatus

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> products = new ArrayList<>();

    /*

    produto existe? na criação order
    se o pedido existe?
    para verificar se é aprovado é,verificar se o produto existe e se tem em estoque

    */
}
