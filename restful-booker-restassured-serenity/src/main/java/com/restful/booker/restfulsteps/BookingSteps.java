package com.restful.booker.restfulsteps;

import com.restful.booker.constants.EndPoints;
import com.restful.booker.models.BookingPojo;
import com.restful.booker.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;

public class BookingSteps {

    public static String token;

    @Step("Creating Auth token for user with username: {0}, password: {1}")
    public ValidatableResponse authUser(String username, String password) {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setUsername(username);
        bookingPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .when()
                .post(EndPoints.CREATE_AUTH)
                .then();

    }

    @Step("Creating booking with firstname: {0}, lastname: {1}, totalprice: {2}, depositpaid: {3}, " +
            "BookingDatesData: {4}, additionalneeds: {5}")

    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, boolean depositpaid,
                                             HashMap<Object, Object> bookingsDatesData, String additionalneeds) {

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingsDatesData);
        bookingPojo.setAdditionalneeds(additionalneeds);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .when()
                .post(EndPoints.GET_BOOKING)
                .then();
    }

    @Step("Verifying Booking is added with BookingID: {0}")
    public ValidatableResponse getBookingInfoByID(int bookingID) {

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .contentType(ContentType.JSON)
                .pathParam("bookingID", bookingID)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then();
    }

    @Step("Updating booking with bookingID: {0}, firstname: {1}, lastname: {2}, totalprice: {3}, depositpaid: {4}, " +
            "BookingDatesData: {5}, additionalneeds: {6}")

    public ValidatableResponse updateBooking(int bookingID, String firstname, String lastname, int totalprice,
                                             boolean depositpaid, HashMap<Object, Object> bookingsDatesData,
                                             String additionalneeds) {

        firstname = "Mishti" + TestUtils.getRandomValue();
        lastname = "Patel" + TestUtils.getRandomValue();

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(999);
        bookingPojo.setDepositpaid(false);
        bookingPojo.setBookingdates(bookingsDatesData);
        bookingPojo.setAdditionalneeds("No Dairy Product");

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .pathParam("bookingID", bookingID)
                .auth().preemptive().basic("admin", "password123")
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then();
    }

    @Step("Updating booking with bookingID: {0}, firstname: {1}, lastname: {2}, totalprice: {3}, depositpaid: {4}, " +
            "BookingDatesData: {5}, additionalneeds: {6}")

    public ValidatableResponse partialUpdateBooking(int bookingID, String firstname, String lastname, int totalprice,
                                                    boolean depositpaid, HashMap<Object, Object> bookingsDatesData,
                                                    String additionalneeds) {

        firstname = firstname + "_updated";
        lastname = lastname + "_updated";

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setBookingdates(bookingsDatesData);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .pathParam("bookingID", bookingID)
                .auth().preemptive().basic("admin", "password123")
                .when()
                .patch(EndPoints.UPDATE_BOOKING_BY_ID)
                .then();
    }

    @Step("Deleting booking with BookingID: {0}")
    public ValidatableResponse deleteBooking(int bookingID) {

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("bookingID", bookingID)
                .auth().preemptive().basic("admin", "password123")
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then();
    }

    @Step("Verifying Updated Booking info with BookingID: {0}")
    public ValidatableResponse getUpdatedBookingInfoByID(int bookingID) {

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .contentType(ContentType.JSON)
                .pathParam("bookingID", bookingID)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then();
    }

    @Step("Verifying Booking is deleted with BookingID: {0}")
    public ValidatableResponse getBookingByID(int bookingID) {

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("bookingID", bookingID)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then();
    }
}
