package test;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AddToChartTest extends TestBase {

  /*  @Test
    @Tag("api")
    @Disabled("In progress...")
    @DisplayName("Successful authorization with set cookie received by API and buy some product")
    void addToChartByExistUserTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie("Nop.customer=876e3a7a-2058-49bd-a0f1-29f44f2ffb5c; ARRAffinity=55622bac41413dfac968dd8f036553a9415557909fd0cd3244e7e0e656e4adc8; __utma=78382081.2111181333.1622322993.1622322993.1622322993.1; __utmc=78382081; __utmz=78382081.1622322993.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __utmt=1; __utmb=78382081.4.10.1622322993")
                        .when()
                        .post("/addproducttocart/catalog/31/1/1")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("success", is(true))
                        .extract().response();

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=876e3a7a-2058-49bd-a0f1-29f44f2ffb5c; ARRAffinity=55622bac41413dfac968dd8f036553a9415557909fd0cd3244e7e0e656e4adc8; __utma=78382081.2111181333.1622322993.1622322993.1622322993.1; __utmc=78382081; __utmz=78382081.1622322993.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __utmt=1; __utmb=78382081.4.10.1622322993")
                .when()
                .post("/addproducttocart/catalog/31/1/1")
                .then()
                .log().body()
                .statusCode(200)
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(5)"));


    }*/


    private String authorizationCookie;
    int quantityOfProduct;

    @Test
    @Tag("api")
    @DisplayName("Successful authorization with set cookie, received by API")
    void addToChartByNewUserTest() {
        step("Get cookie by api", () -> {
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

        System.out.println(authorizationCookie);

        step("Get the quantity of product in the chart", () -> {
            String quantityOfProductString =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .log().cookies()
                            .cookie("Nop.customer", authorizationCookie)
                            .log().cookies()
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

        System.out.println(quantityOfProduct + 1);

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

        step("Open minimal content and set the cookie", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");

            getWebDriver().manage().addCookie(new Cookie("Nop.customer", authorizationCookie));


        });

        step("Open main page and check the chart items", () -> {
            open("/");
            $(".cart-qty").shouldHave(text("(" + (quantityOfProduct + 1) + ")"));
        });


    }
}


