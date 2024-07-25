package br.com.orderservice.exceptions;

public class OrderCanceledException extends RuntimeException {
    public OrderCanceledException(String message) {
        super(message);
    }
}
