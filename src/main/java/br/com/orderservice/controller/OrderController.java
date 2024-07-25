package br.com.orderservice.controller;

import br.com.orderservice.dto.OrderDTO;
import br.com.orderservice.dto.OrderForStatusDTO;
import br.com.orderservice.service.OrderService;
import br.com.orderservice.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Lists all orders", description = "Lists all orders",
            tags = {"Order"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public Page<OrderDTO> listAllOrders(@PageableDefault(size = 10) Pageable pageable) {
        return orderService.findAllOrders(pageable);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Finds a order by id", description = "Finds a order by id",
            tags = {"Order"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public OrderDTO findOrderById(@PathVariable(value = "id") Long id) {
        return orderService.findOrderById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Adds a new order",
            description = "Adds a new order by passing in a JSON representation of the order!",
            tags = {"Order"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO order, UriComponentsBuilder uriBuilder) {

        OrderDTO orderDTO = orderService.createOrder(order);
        URI address = uriBuilder.path("api/order/{id}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(address).body(orderDTO);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates an order",
            description = "Updates an order by passing in a JSON representation of the order!",
            tags = {"Order"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO order, @PathVariable(value = "id") Long id) {
        OrderDTO updated = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(value = "/status",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a status of an order",
            description = "Updates a status of an order by passing in a JSON representation of the order(id, status)!",
            tags = {"Order"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OrderForStatusDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<OrderForStatusDTO> updateOrderStatus(@Valid @RequestBody OrderForStatusDTO order) {
        OrderForStatusDTO updatedStatus = orderService.updateOrderStatus(order);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an order",
            description = "Deletes an id",
            tags = {"Order"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<OrderDTO> deleteOrder(@PathVariable(value = "id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
