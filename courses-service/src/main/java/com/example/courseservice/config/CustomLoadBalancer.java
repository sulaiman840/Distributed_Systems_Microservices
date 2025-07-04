package com.example.courseservice.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

  private int lastIndex = -1;
  private final ServiceInstanceListSupplier supplier;

  public CustomLoadBalancer(ServiceInstanceListSupplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public Mono<Response<ServiceInstance>> choose(Request request) {
    // get the latest instances from Eureka (or your discovery)
    return supplier.get().next().map(this::pickInstance);
  }

  private Response<ServiceInstance> pickInstance(List<ServiceInstance> instances) {
    if (instances.isEmpty()) {
      return new EmptyResponse();
    }

    // build a weighted list
    List<ServiceInstance> weighted = new ArrayList<>();
    for (ServiceInstance inst : instances) {
      int w = parseWeight(inst);
      for (int i = 0; i < w; i++) {
        weighted.add(inst);
      }
    }

    // round-robin over the weighted list
    lastIndex = (lastIndex + 1) % weighted.size();
    ServiceInstance chosen = weighted.get(lastIndex);
    return new DefaultResponse(chosen);
  }

  private int parseWeight(ServiceInstance inst) {
    Map<String, String> meta = inst.getMetadata();
    String wStr = meta.getOrDefault("weight", "1");
    try {
      return Math.max(1, Integer.parseInt(wStr));
    } catch (NumberFormatException e) {
      return 1;
    }
  }
}
