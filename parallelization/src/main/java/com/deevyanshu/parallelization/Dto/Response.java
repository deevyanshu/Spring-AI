package com.deevyanshu.parallelization.Dto;

import java.util.List;

public record Response(String summary, List<String> testcases) {
}
