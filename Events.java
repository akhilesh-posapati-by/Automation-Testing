package com.by.dp.azure.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Events {

	private String component;
	private String service;
	private String status;
}
