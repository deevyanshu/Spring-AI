package com.deevyanshu.routing_workflow.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Response(@JsonProperty(required = true) Routes routes, @JsonProperty(required = true) String response) {
}
