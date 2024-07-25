package br.com.orderservice.controller;

import br.com.orderservice.dto.OrderDTO;
import br.com.orderservice.dto.OrderForStatusDTO;
import br.com.orderservice.service.OrderService;
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
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public Page<OrderDTO> listAllOrders(@PageableDefault(size = 10) Pageable pageable) {
        return orderService.findAllOrders(pageable);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON)
    public OrderDTO findOrderById(@PathVariable(value = "id") Long id) {
        return orderService.findOrderById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO order, UriComponentsBuilder uriBuilder) {

        OrderDTO orderDTO = orderService.createOrder(order);
        URI address = uriBuilder.path("api/order/{id}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(address).body(orderDTO);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO order, @PathVariable(value = "id") Long id) {
        OrderDTO updated = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(value = "/status",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<OrderForStatusDTO> updateOrderStatus(@Valid @RequestBody OrderForStatusDTO order) {
        OrderForStatusDTO updatedStatus = orderService.updateOrderStatus(order);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> deleteOrder(@PathVariable(value = "id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
