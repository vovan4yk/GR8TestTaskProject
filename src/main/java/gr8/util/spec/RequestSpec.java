package gr8.util.spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static gr8.util.ConfigUtil.getBreweriesUrl;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class RequestSpec {

    public static RequestSpecification getRequestSearchBrewerySpec() {
        return new RequestSpecBuilder()
                .setBaseUri(getBreweriesUrl() + "search/")
                .setContentType(JSON)
                .build();
    }

    public static RequestSpecification getRequestSpecSearchBrewery(String query) {
        return given()
                .spec(getRequestSearchBrewerySpec())
                .params("query", query);
    }

    public static Response getRequestSearchBrewery(String query) {
        return getRequestSpecSearchBrewery(query)
                .get();
    }

    public static Response getRequestSearchBrewery(String query, Integer limit) {
        return getRequestSpecSearchBrewery(query)
                .params("per_page", limit)
                .get();
    }
}
