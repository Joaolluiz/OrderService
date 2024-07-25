package br.com.orderservice.mappers;

import br.com.orderservice.dto.OrderDTO;
import br.com.orderservice.dto.ProductDTO;
import br.com.orderservice.model.Order;
import br.com.orderservice.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final ModelMapper mapper;

    public OrderMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Order toOrder(OrderDTO dto) {
        return mapper.map(dto, Order.class);
    }

    public OrderDTO toOrderDTO(Order order)
    {
        return mapper.map(order, OrderDTO.class);
    }

    public List<Order> toOrderList(List<OrderDTO> orders) {
        return orders.stream()
                .map(this::toOrder)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> toOrderDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }
}
