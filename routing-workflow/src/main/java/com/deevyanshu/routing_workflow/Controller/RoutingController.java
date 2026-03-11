package com.deevyanshu.routing_workflow.Controller;

import com.deevyanshu.routing_workflow.Dto.Response;
import com.deevyanshu.routing_workflow.Service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class RoutingController {

    private RouteService routeService;

    public RoutingController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Response> get(@RequestParam(value = "q",required = true) String q)
    {
        return ResponseEntity.ok(this.routeService.route(q));
    }
}
