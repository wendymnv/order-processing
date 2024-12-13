package org.example.orderprocessing.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CustomerApiResponse {
    @JsonProperty("pokemon_v2_item")
    private List<PokemonItem> pokemonItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PokemonItem {
        private String name;
        private int id;
    }

}
