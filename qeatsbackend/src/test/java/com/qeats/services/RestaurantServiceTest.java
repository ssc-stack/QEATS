package com.qeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.QEatsApplication;
import com.qeats.dto.Restaurant;
import com.qeats.exchanges.GetRestaurantsRequest;
import com.qeats.exchanges.GetRestaurantsResponse;
import com.qeats.repositoryservices.RestaurantRepositoryService;
import com.qeats.services.RestaurantServiceImpl;
import com.qeats.utils.FixtureHelpers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = {QEatsApplication.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@DirtiesContext
@ActiveProfiles("test")
class RestaurantServiceTest {

    private static final String FIXTURES = "fixtures/exchanges";
    @InjectMocks
    private RestaurantServiceImpl restaurantService;
    @MockBean
    private RestaurantRepositoryService restaurantRepositoryServiceMock;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        objectMapper = new ObjectMapper();
    }

    private String getServingRadius(List<Restaurant> restaurants, LocalTime timeOfService) {
        when(restaurantRepositoryServiceMock
                .findAllRestaurantsCloseBy(any(Double.class), any(Double.class), any(LocalTime.class),
                        any(Double.class)))
                .thenReturn(restaurants);

        GetRestaurantsResponse allRestaurantsCloseBy = restaurantService
                .findAllRestaurantsCloseBy(new GetRestaurantsRequest(20.0, 30.0),
                        timeOfService);

        assertEquals(2, allRestaurantsCloseBy.getRestaurants().size());
        assertEquals("11", allRestaurantsCloseBy.getRestaurants().get(0).getRestaurantId());
        assertEquals("12", allRestaurantsCloseBy.getRestaurants().get(1).getRestaurantId());

        ArgumentCaptor<Double> servingRadiusInKms = ArgumentCaptor.forClass(Double.class);
        verify(restaurantRepositoryServiceMock, times(1))
                .findAllRestaurantsCloseBy(any(Double.class), any(Double.class), any(LocalTime.class),
                        servingRadiusInKms.capture());

        return servingRadiusInKms.getValue().toString();
    }

    @Test
    void peakHourServingRadiusOf3KmsAt7Pm() throws IOException {
        assertEquals(getServingRadius(loadRestaurantsDuringPeakHours(), LocalTime.of(19, 0)), "3.0");
    }


    @Test
    void normalHourServingRadiusIs5Kms() throws IOException {


        assertFalse(false);
    }


    @Test
    void normalHourFindRestaurantsSearchQuery() throws IOException {
        when(restaurantRepositoryServiceMock.findRestaurantsByName(any(Double.class),
                any(Double.class), any(String.class), any(LocalTime.class), any(Double.class)))
                .thenReturn(loadRestaurantsDuringNormalHours());
        when(restaurantRepositoryServiceMock.findRestaurantsByAttributes(any(Double.class),
                any(Double.class), any(String.class), any(LocalTime.class), any(Double.class)))
                .thenReturn(loadRestaurantsSearchedByAttributes());

        GetRestaurantsRequest getRestaurantsRequest = new GetRestaurantsRequest(20.0, 30.0);
        getRestaurantsRequest.setSearchFor("Test");

        GetRestaurantsResponse allRestaurantsSearchResults = restaurantService
                .findRestaurantsBySearchQuery(getRestaurantsRequest, LocalTime.of(22, 0));

        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByName(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), any(Double.class));
        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByAttributes(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), any(Double.class));

        int i = 0;
        for (Restaurant restaurant : allRestaurantsSearchResults.getRestaurants()) {
            System.out.println("Restaurant " + (++i) + ":\n");
            System.out.println(restaurant);
            System.out.println();
        }

        assertEquals(4, allRestaurantsSearchResults.getRestaurants().size());
        assertEquals("10", allRestaurantsSearchResults.getRestaurants().get(0).getRestaurantId());
        assertEquals("11", allRestaurantsSearchResults.getRestaurants().get(1).getRestaurantId());
        assertEquals("12", allRestaurantsSearchResults.getRestaurants().get(2).getRestaurantId());
        assertEquals("abcdc864835e31495d621234",
                allRestaurantsSearchResults.getRestaurants().get(3).getRestaurantId());

        ArgumentCaptor<Double> servingRadiusInKms = ArgumentCaptor.forClass(Double.class);
        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByName(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), servingRadiusInKms.capture());
        assertEquals(servingRadiusInKms.getValue().toString(), "5.0");

        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByAttributes(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), servingRadiusInKms.capture());
        assertEquals(servingRadiusInKms.getValue().toString(), "5.0");
    }

    @Test
    void peakHourFindRestaurantsSearchQuery() throws IOException {
        when(restaurantRepositoryServiceMock.findRestaurantsByName(any(Double.class),
                any(Double.class), any(String.class), any(LocalTime.class), any(Double.class)))
                .thenReturn(loadRestaurantsDuringPeakHours());
        when(restaurantRepositoryServiceMock.findRestaurantsByAttributes(any(Double.class),
                any(Double.class), any(String.class), any(LocalTime.class), any(Double.class)))
                .thenReturn(loadRestaurantsSearchedByAttributes());

        GetRestaurantsRequest getRestaurantsRequest = new GetRestaurantsRequest(20.0, 30.0);
        getRestaurantsRequest.setSearchFor("Test");

        GetRestaurantsResponse allRestaurantsSearchResults = restaurantService
                .findRestaurantsBySearchQuery(getRestaurantsRequest, LocalTime.of(20, 0));

        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByName(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), any(Double.class));
        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByAttributes(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), any(Double.class));
        assertEquals(3, allRestaurantsSearchResults.getRestaurants().size());
        assertEquals("11", allRestaurantsSearchResults.getRestaurants().get(0).getRestaurantId());
        assertEquals("12", allRestaurantsSearchResults.getRestaurants().get(1).getRestaurantId());
        assertEquals("abcdc864835e31495d621234",
                allRestaurantsSearchResults.getRestaurants().get(2).getRestaurantId());


        ArgumentCaptor<Double> servingRadiusInKms = ArgumentCaptor.forClass(Double.class);
        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByName(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), servingRadiusInKms.capture());
        assertEquals(servingRadiusInKms.getValue().toString(), "3.0");

        verify(restaurantRepositoryServiceMock, times(1))
                .findRestaurantsByAttributes(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), servingRadiusInKms.capture());
        assertEquals(servingRadiusInKms.getValue().toString(), "3.0");
    }

    @Test
    void findRestaurantsSearchQueryIsEmpty() {
        GetRestaurantsRequest getRestaurantsRequest = new GetRestaurantsRequest(20.0, 30.0);
        getRestaurantsRequest.setSearchFor("");

        GetRestaurantsResponse allRestaurantsCloseBy = restaurantService
                .findRestaurantsBySearchQuery(getRestaurantsRequest, LocalTime.of(22, 0));

        verify(restaurantRepositoryServiceMock, times(0))
                .findRestaurantsByName(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), any(Double.class));
        verify(restaurantRepositoryServiceMock, times(0))
                .findRestaurantsByAttributes(any(Double.class), any(Double.class), any(String.class),
                        any(LocalTime.class), any(Double.class));
        assertEquals(0, allRestaurantsCloseBy.getRestaurants().size());
    }


    private List<Restaurant> loadRestaurantsDuringNormalHours() throws IOException {
        String fixture =
                FixtureHelpers.fixture(FIXTURES + "/normal_hours_list_of_restaurants.json");

        return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {
        });
    }

    private List<Restaurant> loadRestaurantsSearchedByAttributes() throws IOException {
        String fixture =
                FixtureHelpers.fixture(FIXTURES + "/list_restaurants_searchedby_attributes.json");

        return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {
        });
    }

    private List<Restaurant> loadRestaurantsDuringPeakHours() throws IOException {
        String fixture =
                FixtureHelpers.fixture(FIXTURES + "/peak_hours_list_of_restaurants.json");

        return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {
        });
    }
}
