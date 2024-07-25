package br.com.orderservice.mappers;

import br.com.orderservice.dto.OrderDTO;
import br.com.orderservice.dto.OrderForStatusDTO;
import br.com.orderservice.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderForStatusMapper {

    private final ModelMapper mapper;

    public OrderForStatusMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Order toOrder(OrderForStatusDTO dto) {
        return mapper.map(dto, Order.class);
    }

    public OrderForStatusDTO toOrderForStatusDTO(Order order)
    {
        return mapper.map(order, OrderForStatusDTO.class);
    }

    public List<Order> toOrderList(List<OrderForStatusDTO> orders) {
        return orders.stream()
                .map(this::toOrder)
                .collect(Collectors.toList());
    }

    public List<OrderForStatusDTO> toOrderForStatusDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderForStatusDTO)
                .collect(Collectors.toList());
    }
}
