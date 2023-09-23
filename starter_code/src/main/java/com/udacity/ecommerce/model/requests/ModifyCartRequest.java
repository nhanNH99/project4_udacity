package com.udacity.ecommerce.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class ModifyCartRequest {

	@JsonProperty
	private long itemId;

	@JsonProperty
	private String username;

	@JsonProperty
	private int quantity;

}
