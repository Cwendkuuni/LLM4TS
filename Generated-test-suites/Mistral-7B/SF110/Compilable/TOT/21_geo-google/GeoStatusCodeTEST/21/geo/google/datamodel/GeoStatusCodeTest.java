package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeSuccess() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
 int expectedCode = 400;
 String expectedDescription = "A directions request could not be successfully parsed.";

 GeoStatusCode returnedStatus = GeoStatusCode.getStatusCode(expectedCode);

 assertEquals(expectedCode, returnedStatus.getCode());
 assertEquals(expectedDescription, returnedStatus.getDescription());
 }

 @Test
 public void testGetUnknownStatusCode() {
 int unknownCode = 100;
 GeoStatusCode returnedStatus = GeoStatusCode.getStatusCode(unknownCode);
 String expectedDescription = "Uknown Status";
 int expectedCode = -1;

 assertEquals(expectedCode, returnedStatus.getCode());
 assertEquals(expectedDescription, returnedStatus.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithNegativeCode() {
 GeoStatusCode.getStatusCode(-1);
 }

 @Test
 public void testGetStatusCodeWithInvalidCode() {
 int invalidCode = 700;
 GeoStatusCode returnedStatus = GeoStatusCode.getStatusCode(invalidCode);
 String expectedDescription = "Uknown Status";
 int expectedCode = -1;

 assertEquals(expectedCode, returnedStatus.getCode());
 assertEquals(expectedDescription, returnedStatus.getDescription());
 }

 @Test
 public void testGetStatusCodeWithValidCode() {
 int validCode = 602;
 GeoStatusCode returnedStatus = GeoStatusCode.getStatusCode(validCode);
 String expectedDescription = "No corresponding geographic location could be found for the specified address.";
 int expectedCode = 602;

 assertEquals(expectedCode, returnedStatus.getCode());
 assertEquals(expectedDescription, returnedStatus.getDescription());
 }
}