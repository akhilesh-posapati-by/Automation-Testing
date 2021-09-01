package com.by.dp.azure.models;

import lombok.Getter;

import java.util.List;

@Getter
public class EventFilterRequest {
    private Events events;
    private List<Events> statusFilter;
}
