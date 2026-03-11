package com.deevyanshu.routing_workflow.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Routing_class(@JsonProperty(required = true) Routes routes, @JsonProperty(required = true) String reasoning) {
}
