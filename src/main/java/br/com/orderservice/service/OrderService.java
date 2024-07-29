package br.com.orderservice.service;

import br.com.orderservice.dto.OrderDTO;
import br.com.orderservice.dto.OrderForStatusDTO;
import br.com.orderservice.dto.OrderProductDTO;
import br.com.orderservice.enums.Status;
import br.com.orderservice.exceptions.OrderCanceledException;
import br.com.orderservice.exceptions.RequiredObjectIsNullException;
import br.com.orderservice.exceptions.ResourceNotFoundException;
import br.com.orderservice.mappers.OrderForStatusMapper;
import br.com.orderservice.mappers.OrderMapper;
import br.com.orderservice.mappers.ProductMapper;
import br.com.orderservice.model.Order;
import br.com.orderservice.model.OrderProduct;
import br.com.orderservice.model.Product;
import br.com.orderservice.repository.OrderRepository;
import br.com.orderservice.repository.ProductRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OrderService {

    private Logger logger = Logger.getLogger(OrderService.class.getName());

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderForStatusMapper orderForStatusMapper;

    public Page<OrderDTO> findAllOrders(Pageable pageable) {

        logger.info("Finding all orders.");

        return orderRepository
                .findAll(pageable)
                .map(order -> orderMapper.toOrderDTO(order));
    }

    public OrderDTO findOrderById(Long id) {

        logger.info("Finding an order.");

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No order for this id!"));

        return orderMapper.toOrderDTO(order);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {

        logger.info("creating an order.");

        Order order = new Order();

        order = orderMapper.toOrder(orderDTO);

        order.setProducts(new ArrayList<>());

        BigDecimal totalValue = BigDecimal.ZERO;

        for (OrderProductDTO orderProductDTO : orderDTO.getProducts()) {
            Long productId = orderProductDTO.getProduct().getId();
            Optional<Product> productOpt = productRepository.findById(productId);

            if (!productOpt.isPresent()) {
                throw new ResourceNotFoundException("Product with id " + productId + " does not exist.");
            } else {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setQuantity(orderProductDTO.getQuantity());
                Product product = productOpt.get();
                orderProduct.setProduct(product);
                orderProduct.setOrder(order);

                BigDecimal productTotal = product.getValue().multiply(BigDecimal.valueOf(orderProductDTO.getQuantity()));
                totalValue = totalValue.add(productTotal);

                order.setTotalValue(totalValue);
                order.setStatus(Status.EVALUATION);
                order.getProducts().add(orderProduct);
            }
        }

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderDTO(savedOrder);
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {

        if (orderDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a order.");

        Order order = update(id, orderDTO);

        return orderMapper.toOrderDTO(orderRepository.save(order));
    }

    public OrderForStatusDTO updateOrderStatus(OrderForStatusDTO orderDTO) {

        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No order for this ID!"));

        switch (order.getStatus()) {
            case EVALUATION -> {
                if (checkProductAvailability(order)) {
                    order.setStatus(Status.PREPARATION);
                } else {
                    order.setStatus(Status.CANCELED);
                }
                break;
            }
            case CANCELED -> {
                throw new OrderCanceledException("The order has been canceled");
            }
            case PREPARATION -> {
                order.setStatus(Status.READY);
                break;
            }
            case READY -> {
                order.setStatus(Status.DELIVERY);
                decrementProductQuantities(order);
                break;
            }
            case DELIVERY -> {
                order.setStatus(Status.CONCLUDED);
                break;
            }
            default -> {
                throw new IllegalStateException("Unexpected status: " + order.getStatus());
            }
        }

        Order updatedOrderStatus = orderRepository.save(order);

        return orderForStatusMapper.toOrderForStatusDTO(updatedOrderStatus);
    }

    public void deleteOrder(Long id) {
        logger.info("Deleting a order.");

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No order for this ID!"));

        orderRepository.delete(order);
    }

    public Order update(Long id, OrderDTO orderDTO) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No order for this id!"));

        order.setClientName(orderDTO.getClientName());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setAddress(orderDTO.getAddress());
        order.setCreatedDate(orderDTO.getCreatedDate());

        return order;
    }

    private boolean checkProductAvailability(Order order) {
        for(OrderProduct orderProduct : order.getProducts()) {
            Product product = orderProduct.getProduct();
            if (product.getQuantity() < orderProduct.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    private void decrementProductQuantities(Order order) {
        for (OrderProduct orderProduct : order.getProducts()) {
            Product product = orderProduct.getProduct();
            product.setQuantity(product.getQuantity() - orderProduct.getQuantity());
            productRepository.save(product);
        }
    }
}
