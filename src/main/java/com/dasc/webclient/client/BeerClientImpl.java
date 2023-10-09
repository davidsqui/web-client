package com.dasc.webclient.client;

import com.dasc.webclient.config.WebClientProperties;
import com.dasc.webclient.domain.Beer;
import com.dasc.webclient.domain.BeerPagedList;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

  private final WebClient webClient;

  @Override
  public Mono<BeerPagedList> getAllBeers(Integer pageNumber, Integer pageSize, String beerName,
      String beerStyle, Boolean showInventoryOnHand) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_v1_PATH)
            .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
            .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
            .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
            .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
            .build())
        .retrieve()
        .bodyToMono(BeerPagedList.class);
  }

  @Override
  public Mono<Beer> getBeerById(UUID id, Boolean showInventoryOnHand) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_v1_PATH)
            .pathSegment(id.toString())
            .queryParam("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
            .build())
        .retrieve()
        .bodyToMono(Beer.class);
  }

  @Override
  public Mono<Beer> getBeerByUPC(String upc, Boolean showInventoryOnHand) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_UPC_v1_PATH)
            .pathSegment(upc)
            .queryParam("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
            .build())
        .retrieve()
        .bodyToMono(Beer.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> createBeer(Beer beer) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_v1_PATH).build())
        .body(BodyInserters.fromValue(beer))
        .retrieve()
        .toBodilessEntity();
  }

  @Override
  public Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer beer) {
    return webClient.put()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_v1_PATH_GET_BY_ID).build(id))
        .body(BodyInserters.fromValue(beer))
        .retrieve()
        .toBodilessEntity()
        .log();
  }

  @Override
  public Mono<ResponseEntity> deleteBeer(UUID id) {
    return null;
  }
}
