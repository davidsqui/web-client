package com.dasc.webclient.client;

import com.dasc.webclient.domain.Beer;
import com.dasc.webclient.domain.BeerPagedList;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface BeerClient {

  Mono<BeerPagedList> getAllBeers(Integer pageNumber, Integer pageSize, String beerName,
      String beerStyle, Boolean showInventoryOnHand);

  Mono<Beer> getBeerById(UUID id, Boolean showInventoryOnHand);

  Mono<Beer> getBeerByUPC(String upc, Boolean showInventoryOnHand);

  Mono<ResponseEntity<Void>> createBeer(Beer beer);

  Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer beer);

  Mono<ResponseEntity<Void>> deleteBeer(UUID id);


}
