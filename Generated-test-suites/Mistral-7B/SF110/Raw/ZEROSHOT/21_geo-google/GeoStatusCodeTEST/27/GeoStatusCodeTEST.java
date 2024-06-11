package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

/**
 * GeoStatusCodeTest class to test all methods in GeoStatusCode enum class.
 */
public class GeoStatusCodeTest {

 /**
  * Test method for {@link GeoStatusCode#GeoStatusCode(int, String)} constructor.
  */
 @Test
 public void testGeoStatusCodeConstructor() {
 for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
 assertEquals(geoStatusCode.getCode(), geoStatusCode._code);
 assertEquals(geoStatusCode.getDescription(), geoStatusCode._description);
 }
 }

 /**
 * Test method for {@link GeoStatusCode#getCode()} method.
 */
 @Test
 public void testGetCode() {
 for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
 assertEquals(geoStatusCode.getCode(), geoStatusCode.ordinal() + 200);
 }
 }

 /**
 * Test method for {@link GeoStatusCode#getCodeName()} method.
 */
 @Test
 public void testGetCodeName() {
 for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
 assertEquals(geoStatusCode.getName(), geoStatusCode.getCodeName());
 }
 }

 /**
 * Test method for {@link GeoStatusCode#getDescription()} method.
 */
 @Test
 public void testGetDescription() {
 for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
 assertEquals(geoStatusCode.getDescription(), geoStatusCode._description);
 }
 }

 /**
 * Test method for {@link GeoStatusCode#getStatusCode(int)} method.
 */
 @Test
 public void testGetStatusCode() {
 for (int code : new int[] { 200, 400, 500, 601, 602, 603, 604, 610, 620 }) {
 GeoStatusCode expectedStatusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(expectedStatusCode, GeoStatusCode.valueOf(expectedStatusCode.getName()));
 }

 for (int code : new int[] { -1, Integer.MAX_VALUE, Integer.MIN_VALUE }) {
 assertNull(GeoStatusCode.getStatusCode(code));
 }
 }
}