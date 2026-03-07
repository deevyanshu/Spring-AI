package com.deevyanshu.Recipe.Dto;

import java.util.List;

public record Recipe(String recipe, List<String> nutritionalData) {
}
