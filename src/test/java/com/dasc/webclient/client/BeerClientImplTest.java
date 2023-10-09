package com.dasc.webclient.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dasc.webclient.config.WebClientConfig;
import com.dasc.webclient.domain.Beer;
import com.dasc.webclient.domain.BeerPagedList;
import com.dasc.webclient.domain.BeerStyle;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

class BeerClientImplTest {

  BeerClientImpl beerClient;

  @BeforeEach
  void setUp() {
    beerClient = new BeerClientImpl(new WebClientConfig().webClient());
  }

  @Test
  void getAllBeers() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(null, null, null, null, null);

    BeerPagedList beerPagedList = beerPagedListMono.block();

    assertThat(beerPagedList).isNotNull();
  }

  @Test
  void getAllBeersPageSize10() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(1, 10, null, null, null);

    BeerPagedList beerPagedList = beerPagedListMono.block();

    assertThat(beerPagedList).isNotNull();
    assertThat(beerPagedList.getContent().size()).isEqualTo(10);
  }

  @Test
  void getAllBeersNoRecords() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(100, 10, null, null, null);

    BeerPagedList beerPagedList = beerPagedListMono.block();

    assertThat(beerPagedList).isNotNull();
    assertThat(beerPagedList.getContent().size()).isEqualTo(0);
  }

  @Test
  void getBeerById() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(1, 1, null, null, true);
    BeerPagedList beerPagedList = beerPagedListMono.block();
    Beer beerToFind = Objects.requireNonNull(beerPagedList).getContent().get(0);

    Mono<Beer> beerMono = beerClient.getBeerById(beerToFind.getId(), true);

    Beer foundBeer = beerMono.block();

    assertThat(foundBeer).isNotNull();
    assertThat(foundBeer.getId()).isEqualTo(beerToFind.getId());
  }

  @Test
  void getBeerByUPC() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(1, 1, null, null, true);
    BeerPagedList beerPagedList = beerPagedListMono.block();
    Beer beerToFind = Objects.requireNonNull(beerPagedList).getContent().get(0);

    Mono<Beer> beerMono = beerClient.getBeerByUPC(beerToFind.getUpc(), true);

    Beer foundBeer = beerMono.block();

    assertThat(foundBeer).isNotNull();
    assertThat(foundBeer.getUpc()).isEqualTo(beerToFind.getUpc());
  }

  @Test
  void createBeer() {
    Beer newBeer = Beer.builder()
        .beerName("Test Beer")
        .beerStyle(BeerStyle.IPA)
        .upc("1234567890")
        .price(new BigDecimal("10.99"))
        .build();

    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(newBeer);

    ResponseEntity responseEntity = responseEntityMono.block();

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void updateBeer() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(1, 1, null, null, true);
    BeerPagedList beerPagedList = beerPagedListMono.block();
    Beer beerToFind = Objects.requireNonNull(beerPagedList).getContent().get(0);

    Beer newBeer = Beer.builder()
        .beerName("Update Test Beer")
        .beerStyle(BeerStyle.IPA)
        .upc("1234567890")
        .price(new BigDecimal("10.99"))
        .build();

    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerToFind.getId(),
        newBeer);
    ResponseEntity responseEntity = responseEntityMono.block();

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deleteBeer() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.getAllBeers(1, 1, null, null, true);
    BeerPagedList beerPagedList = beerPagedListMono.block();
    Beer beerToFind = Objects.requireNonNull(beerPagedList).getContent().get(0);

    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(beerToFind.getId());
    ResponseEntity responseEntity = responseEntityMono.block();

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deleteBeerNotFound() {
    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(UUID.randomUUID());
    assertThrows(WebClientResponseException.class, () -> {
      ResponseEntity<Void> responseEntity = responseEntityMono.block();
      assertThat(responseEntity).isNotNull();
      assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    });
  }

  @Test
  void deleteBeerHandleException() {
    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(UUID.randomUUID());
    ResponseEntity<Void> responseEntity = responseEntityMono
        .onErrorResume(throwable -> {
          if (throwable instanceof WebClientResponseException exception) {
            return Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
          } else {
            throw new RuntimeException(throwable);
          }
        }).block();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}