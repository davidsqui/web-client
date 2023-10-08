package com.dasc.webclient.client;

import com.dasc.webclient.domain.Beer;
import com.dasc.webclient.domain.BeerPagedList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

  @Override
  public Mono<BeerPagedList> getAllBeers(Integer pageNumber, Integer pageSize, String beerName,
      String beerStyle, Boolean showInventoryOnHand) {
    return null;
  }

  @Override
  public Mono<Beer> getBeerById(UUID id) {
    return null;
  }

  @Override
  public Mono<Beer> getBeerByUPC(String upc) {
    return null;
  }

  @Override
  public Mono<ResponseEntity> createBeer(Beer beer) {
    return null;
  }

  @Override
  public Mono<ResponseEntity> updateBeer(UUID id, Beer beer) {
    return null;
  }

  @Override
  public Mono<ResponseEntity> deleteBeer(UUID id) {
    return null;
  }
}
