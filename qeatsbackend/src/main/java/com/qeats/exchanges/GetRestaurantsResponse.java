package com.qeats.exchanges;

import com.qeats.dto.Restaurant;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantsResponse {
    private List<Restaurant> restaurants;
}



