package com.deevyanshu.Orchestrator.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Workers(@JsonProperty(required = true) TaskEnum task,@JsonProperty(required = true) String prompt) {
}
