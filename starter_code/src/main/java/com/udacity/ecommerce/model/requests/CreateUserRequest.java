package com.udacity.ecommerce.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	private String password;

	@JsonProperty
	private String confirmPassword;

}