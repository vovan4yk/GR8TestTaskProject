package gr8.breweries;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static gr8.util.spec.RequestSpec.getRequestSearchBrewery;
import static gr8.util.spec.RequestSpec.getRequestSearchBrewerySpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SearchBreweriesTest {

    private final String welcomeMessage = "Welcome to the Breweries API, see the documentation at"
            + " https://www.openbrewerydb.org/documentation";

    static Stream<Object[]> breweryValidDataProvider() {
        return Stream.of(
                new Object[]{"San Diego", "city"},
                new Object[]{"California", "state"},
                new Object[]{"Deft Brewing", "name"}
        );
    }

    @ParameterizedTest
    @MethodSource("breweryValidDataProvider")
    public void verifySearchByValidQuery(String query, String field) {
        getRequestSearchBrewery(query)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("brewery_type", notNullValue())
                .body("address_1", notNullValue())
                .body("address_2", notNullValue())
                .body("address_3", notNullValue())
                .body("city", notNullValue())
                .body("state_province", notNullValue())
                .body("postal_code", notNullValue())
                .body("country", notNullValue())
                .body("longitude", notNullValue())
                .body("latitude", notNullValue())
                .body("phone", notNullValue())
                .body("website_url", notNullValue())
                .body("state", notNullValue())
                .body("street", notNullValue())
                .body(field, everyItem(containsString(query)));
    }

    static Stream<Object[]> breweryValidPartiallyDataProvider() {
        return Stream.of(
                new Object[]{"Diego", "San Diego", "city"},
                new Object[]{"lifornia", "California", "state"},
                new Object[]{"rewing", "Deft Brewing", "name"}
        );
    }

    @ParameterizedTest
    @MethodSource("breweryValidPartiallyDataProvider")
    public void verifySearchByValidPartiallyQuery(String query, String text, String field) {
        getRequestSearchBrewery(query)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body(field, everyItem(containsString(text)));
    }

    static Stream<Object[]> breweryValidDataWithPaginationProvider() {
        return Stream.of(
                new Object[]{"San Diego", 12},
                new Object[]{"California", 1},
                new Object[]{"Deft Brewing", 0}
        );
    }

    @ParameterizedTest
    @MethodSource("breweryValidDataWithPaginationProvider")
    public void verifySearchByValidQueryWithPagination(String query, int perPage) {
        var validatableResponse = getRequestSearchBrewery(query, perPage)
                .then()
                .statusCode(200);

        if (perPage == 0) {
            validatableResponse.body("message", equalTo(welcomeMessage));
        } else {
            validatableResponse.body("size()", equalTo(perPage));
        }
    }

    static Stream<String> breweryInvalidDataProvider() {
        return Stream.of(
                RandomStringUtils.secure().next(10, true, false),
                RandomStringUtils.secure().next(10, true, true),
                new RandomStringGenerator.Builder()
                        .withinRange(33, 126)
                        .filteredBy(c -> !Character.isLetterOrDigit(c))
                        .build().generate(10)
        );
    }

    @ParameterizedTest
    @MethodSource("breweryInvalidDataProvider")
    public void verifySearchByInvalidQuery(String query) {
        getRequestSearchBrewery(query)
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    public void verifySearchWithEmptyQuery() {
        given()
                .spec(getRequestSearchBrewerySpec())
                .get()
                .then()
                .statusCode(200)
                .body("message", equalTo(welcomeMessage));
    }

    @Test
    public void verifySearchWithTwoCharacters() {
        getRequestSearchBrewery("te")
                .then()
                .statusCode(200)
                .body("message", equalTo(welcomeMessage));
    }
}
