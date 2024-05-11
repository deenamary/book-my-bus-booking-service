package com.example.bookmybusbookingservice;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class BusInventoryService {

    private WebClient.Builder webClientBuilder;
    private  String inventory_service_url;
    private  String admin_service_url;

    BusInventoryService(WebClient.Builder webClientBuilder,
                        String inventory_service_url,
                        String admin_service_url) {
        this.webClientBuilder = webClientBuilder;
        this.inventory_service_url = inventory_service_url;
        this.admin_service_url = admin_service_url;
    }

    public int getAvailableSeats (String busId) {
       BusInventory busInventory = webClientBuilder.build().get().uri(inventory_service_url + busId)
                .retrieve()
                .bodyToMono(BusInventory.class)
                .block();
       if(Objects.isNull(busInventory)){
           BusRoute busRoute = webClientBuilder.build().get().uri(admin_service_url + busId)
                   .retrieve()
                   .bodyToMono(BusRoute.class)
                   .block();
           if(Objects.isNull(busRoute)){
               throw new RuntimeException("Could not retrieve seat availability");
           } else {
               return busRoute.getTotalSeats();
           }
       } else {
           return busInventory.getAvailableSeats();
       }
    }
}
