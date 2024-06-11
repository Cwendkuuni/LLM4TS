import org.junit.Test; import static org.junit.Assert.*;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode_ValidCodes() {
 // Test valid codes
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 String expectedName = "G_GEO_SUCCESS";
 int actualCode = status.getCode();
 String actualName = status.getCodeName();

 assertEquals("Code should match", expectedCode, actualCode);
 assertEquals("Name should match", expectedName, actualName);

 status = GeoStatusCode.G_GEO_BAD_REQUEST;
 expectedCode = 400;
 expectedName = "G_GEO_BAD_REQUEST";
 actualCode = status.getCode();
 actualName = status.getCodeName();

 assertEquals("Code should match", expectedCode, actualCode);
 assertEquals("Name should match", expectedName, actualName);
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
 // Test invalid code
 int invalidCode = -1;
 GeoStatusCode status = GeoStatusCode.getStatusCode(invalidCode);
 GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode = -1;
 String expectedName = "G_GEO_UNKOWN_STATUS";
 int actualCode = status.getCode();
 String actualName = status.getCodeName();

 assertEquals("Code should match", expectedCode, actualCode);
 assertEquals("Name should match", expectedName, actualName);
 }

 @Test
 public void testGetStatusCode_OutOfRangeCode() {
 // Test out of range code
 int outOfRangeCode = 621;
 GeoStatusCode status = GeoStatusCode.getStatusCode(outOfRangeCode);
 GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode = -1;
 String expectedName = "G_GEO_UNKOWN_STATUS";
 int actualCode = status.getCode();
 String actualName = status.getCodeName();

 assertEquals("Code should match", expectedCode, actualCode);
 assertEquals("Name should match", expectedName, actualName);
 }

 @Test
 public void testGetStatusCode_NullCode() {
 // Test null code
 int nullCode = null;
 GeoStatusCode status = GeoStatusCode.getStatusCode(nullCode);
 GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode = -1;
 String expectedName = "G_GEO_UNKOWN_STATUS";
 int actualCode = status.getCode();
 String actualName = status.getCodeName();

 assertEquals("Code should match", expectedCode, actualCode);
 assertEquals("Name should match", expectedName, actualName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_NegativeCode() {
 // Test negative code
 int negativeCode = -10;
 GeoStatusCode status = GeoStatusCode.getStatusCode(negativeCode);
 }

 @Test
 public void testGetStatusCode_ZeroCode() {
 // Test zero code
 GeoStatusCode status = GeoStatusCode.getStatusCode(0);
 GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode = 0;
 String expectedName = "G_GEO_UNKOWN_STATUS";
 int actualCode = status.getCode();
 String actualName = status.getCodeName();

 assertEquals("Code should match", expectedCode, actualCode);
 assertEquals("Name should match", expectedName, actualName);
 }
}