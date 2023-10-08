package com.dasc.webclient.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.dasc.webclient.config.WebClientConfig;
import com.dasc.webclient.domain.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

}