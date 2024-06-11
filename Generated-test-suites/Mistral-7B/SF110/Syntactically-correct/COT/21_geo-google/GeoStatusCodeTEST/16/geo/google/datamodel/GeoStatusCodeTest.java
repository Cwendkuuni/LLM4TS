package geo.google.datamodel.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode_ValidCode() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(-1);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testGetStatusCode_ValidCodeName() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals("G_GEO_SUCCESS", status.getCodeName());
 }

 @Test
 public void testGetStatusCode_InvalidCodeName() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertNotNull(status.getCodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_NegativeCode() {
 GeoStatusCode.getStatusCode(-1);
 }

 @Test
 public void testGetStatusCode_UnknownCode() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(621);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

}