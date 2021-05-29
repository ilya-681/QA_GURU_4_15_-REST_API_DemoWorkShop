package test;

import com.codeborne.selenide.Condition;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class NewTest extends TestBase{

    @Test
    void successfulAuthTest(){

        Response response =
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=8cb0542a-fbb7-4aef-a2db-0e05fec50eae; ARRAffinity=1fa9158750fcf7cee1728ac683a12594fe016bf3b1c0544237f51a4ffe2ef5ea; __utmc=78382081; __utmz=78382081.1617211040.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __RequestVerificationToken=jIWQx0wM8XfRQEbj8QP5EbgOhBcgv-RXZ2SfjxsXocujK1B6UnB-x329Q22s9Fw56Q0CMvOBWHCgVvLzzHSp3fdVvIaRcC0kpLNJC70G3xU1; __utma=78382081.1481126277.1617211040.1617295614.1617389855.3; __utmt=1; nop.CompareProducts=CompareProductIds=79&CompareProductIds=72; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=74&RecentlyViewedProductIds=79&RecentlyViewedProductIds=72&RecentlyViewedProductIds=45; __atuvc=7%7C13; __atuvs=6067695c7bfc16bd006; __utmb=78382081.28.10.1617389855")
                .body("product_attribute_74_5_26=81&product_attribute_74_6_27=83&product_attribute_74_3_28=86&product_attribute_74_8_29=88&addtocart_74.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/74/1")
                .then()
                .log().body()
                .statusCode(200)
                .body("success", is(true))
                .extract().response();


        System.out.println(response);

    }

    @Test
    void successfulLoginTest(){

        open("http://demowebshop.tricentis.com/login");
        $("#Email").val("qa_guru123@ya.ru");
        $("#Password").val("qa_guru123");
        $(".login-button").click();

        $(".account").shouldHave(Condition.text("qa_guru123@ya.ru"));

    }

    @Test
    void successfulLoginAPITest(){

        open("http://demowebshop.tricentis.com");
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("Email=qa_guru123%40ya.ru&Password=qa_guru123&RememberMe=false")
                .when()
                .post("/login")
                .then()
                .log().body()
                .statusCode(302);
       
        $(".account").shouldHave(Condition.text("qa_guru123@ya.ru"));
    }
}
