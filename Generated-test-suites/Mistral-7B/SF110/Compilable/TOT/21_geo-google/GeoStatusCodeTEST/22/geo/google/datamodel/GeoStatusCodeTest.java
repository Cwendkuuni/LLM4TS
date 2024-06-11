package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 assertEquals(expectedCode, statusCode.getCode());

 GeoStatusCode statusCode2 = GeoStatusCode.G_GEO_BAD_REQUEST;
 int expectedCode2 = 400;
 assertEquals(expectedCode2, statusCode2.getCode());

 GeoStatusCode statusCode3 = GeoStatusCode.G_GEO_SERVER_ERROR;
 int expectedCode3 = 500;
 assertEquals(expectedCode3, statusCode3.getCode());

 GeoStatusCode statusCode4 = GeoStatusCode.G_GEO_MISSING_QUERY;
 int expectedCode4 = 601;
 assertEquals(expectedCode4, statusCode4.getCode());

 GeoStatusCode statusCode5 = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 int expectedCode5 = 602;
 assertEquals(expectedCode5, statusCode5.getCode());

 GeoStatusCode statusCode6 = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 int expectedCode6 = 603;
 assertEquals(expectedCode6, statusCode6.getCode());

 GeoStatusCode statusCode7 = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 int expectedCode7 = 604;
 assertEquals(expectedCode7, statusCode7.getCode());

 GeoStatusCode statusCode8 = GeoStatusCode.G_GEO_BAD_KEY;
 int expectedCode8 = 610;
 assertEquals(expectedCode8, statusCode8.getCode());

 GeoStatusCode statusCode9 = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 int expectedCode9 = 620;
 assertEquals(expectedCode9, statusCode9.getCode());

 GeoStatusCode statusCode10 = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode10 = -1;
 assertEquals(expectedCode10, statusCode10.getCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 int code = 100;
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
 }
}