

package com.qeats.exchanges;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mongodb.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@Data
@RequiredArgsConstructor
public class GetRestaurantsRequest {

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private final Double latitude;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private final Double longitude;


    private String searchFor;


    public boolean isSearchQueryPresent() {
        return searchFor != null;
    }

}

