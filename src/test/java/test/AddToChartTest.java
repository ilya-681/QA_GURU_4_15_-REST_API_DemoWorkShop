package test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AddToChartTest extends TestBase {


    private String authorizationCookie;
    private int quantityOfProduct;

    @Test
    @Tag("api")
    @DisplayName("Successful adding the product to the chart with set cookie by API and verify by UI")
    void addToChartByNewUserTest() {
        step("Get cookie by API", () -> {
            authorizationCookie =
                    given()
                            .log().uri()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .when()
                            .get("/")
                            .then()
                            .log().body()
                            .statusCode(200)
                            .extract()
                            .cookie("Nop.customer");
        });

        step("Get the quantity of product in the chart by API", () -> {
            String quantityOfProductString =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .cookie("Nop.customer", authorizationCookie)
                            .when()
                            .post("/addproducttocart/catalog/31/1/1")
                            .then()
                            .log().body()
                            .statusCode(200)
                            .body("success", is(true))
                            .extract()
                            .path("updatetopcartsectionhtml");
            quantityOfProduct = Integer.parseInt(quantityOfProductString.substring(1, quantityOfProductString.length() - 1));
        });

        step("Add the product in the chart by API", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .cookie("Nop.customer", authorizationCookie)
                    .when()
                    .post("/addproducttocart/catalog/31/1/1")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("updatetopcartsectionhtml", is("(" + (quantityOfProduct + 1) + ")"));
        });

        step("Open minimal content and set the cookie for UI check", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");

            getWebDriver().manage().addCookie(new Cookie("Nop.customer", authorizationCookie));


        });

        step("Open main page and check the chart items", () -> {
            open("/");
            $(".cart-qty").shouldHave(text("(" + (quantityOfProduct + 1) + ")"));
        });


    }
}


