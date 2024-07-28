package br.com.orderservice.dto;

import br.com.orderservice.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {}
