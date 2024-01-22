package com.restful.booker.crudsuite;

import com.restful.booker.restfulsteps.BookingSteps;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasKey;

@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class BookingCRUDTestWithSteps extends TestBase {
    public static String username = "admin";
    public static String password = "password123";
    public static String firstname = "Disha" + TestUtils.getRandomValue();
    public static String lastname = "Patel" + TestUtils.getRandomValue();
    public static Integer totalprice = 777;
    public static Boolean depositpaid = true;
    public static String additionalneeds = "Breakfast & Dinner";
    public static int bookingID;
    public static String token;

    //=============================================================================//

    @Steps
    BookingSteps steps;

    @Title("Create Auth token")
    @Test
    public void test001() {
        ValidatableResponse response = steps.authUser(username, password);

        response.log().all().statusCode(200);

        HashMap<Object, Object> tokenMap = response.log().all().extract().path("");

        Assert.assertThat(tokenMap, hasKey("token"));
        String jsonString = response.extract().asString();
        token = JsonPath.from(jsonString).get("token");

        System.out.println(token);
    }

    @Title("Creating a booking")
    @Test
    public void test002() {

        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-10-18");
        bookingsDatesData.put("checkout", "2025-10-18");

        ValidatableResponse response = steps.createBooking(firstname, lastname, totalprice,
                depositpaid, bookingsDatesData, additionalneeds);

        response.log().all().statusCode(200);
        bookingID = response.log().all().extract().path("bookingid");

        HashMap<Object, Object> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }

    @Title("Verifying a new booking")
    @Test
    public void test003() {

        ValidatableResponse response = steps.getBookingInfoByID(bookingID);
        response.log().all().statusCode(200);

    }

    @Title("Updating a new booking")
    @Test
    public void test004() {

        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-11-18");
        bookingsDatesData.put("checkout", "2025-11-18");

        ValidatableResponse response = steps.updateBooking(bookingID, firstname, lastname,
                totalprice, depositpaid, bookingsDatesData, additionalneeds);

        response.log().all().statusCode(200);

        HashMap<Object, Object> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }

    @Title("Partially Updating a new booking")
    @Test
    public void test005() {

        HashMap<Object, Object> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2024-12-18");
        bookingsDatesData.put("checkout", "2025-12-18");

        ValidatableResponse response = steps.partialUpdateBooking(bookingID, firstname, lastname,
                totalprice, depositpaid, bookingsDatesData, additionalneeds);

        response.log().all().statusCode(200);

        HashMap<Object, Object> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }

    @Title("Verifying updated booking with bookingId")
    @Test
    public void test006() {

        ValidatableResponse response = steps.getUpdatedBookingInfoByID(bookingID);
        response.log().all().statusCode(200);

    }

    @Title("Deleting with Booking")
    @Test
    public void test007() {

        ValidatableResponse response = steps.deleteBooking(bookingID);
        response.log().all().statusCode(201);

        ValidatableResponse response1 = steps.getBookingByID(bookingID);
        response1.log().all().statusCode(404);

    }
}
