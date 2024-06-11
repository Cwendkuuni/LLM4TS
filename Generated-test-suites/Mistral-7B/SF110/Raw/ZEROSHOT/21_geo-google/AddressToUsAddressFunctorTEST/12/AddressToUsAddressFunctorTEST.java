package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;

import java.util.*;

public class AddressToUsAddressFunctorTest {

 private GeoAddress createAddressInstance() {
 GeoAddress geoAddress = new GeoAddress();
 GeoCoordinate coordinate = new GeoCoordinate(40.7128, -74.0060);
 geoAddress.setCoordinate(coordinate);
 geoAddress.setAccuracy(10);

 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryCode("US");
 country.setCountryName(new CountryNameCode("US", "United States"));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeAreaName("NY", "New York"));
 country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new SubAdministrativeAreaName("NYC", "New York City"));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(new LocalityName("Manhattan", "Manhattan"));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode(10001));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ThoroughfareNameType("5th Avenue"));
 geoAddress.setAddressDetails(addressDetails);

 return geoAddress;
 }

 @Test
 public void testNullAddress() {
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(null);
 assertNull(usAddress);
 }

 @Test
 public void testEmptyAddress() {
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(new GeoAddress());
 assertEquals(0, usAddress.getCountry().length());
 assertEquals(0, usAddress.getState().length());
 assertEquals(0, usAddress.getCity().length());
 assertEquals(0, usAddress.getCounty().length());
 assertEquals(0, usAddress.getPostalCode().length());
 assertEquals(0, usAddress.getAddressLine1().length());
 assertEquals(0, usAddress.getAddressLine2().length());
 assertEquals(null, usAddress.getCoordinate());
 assertEquals(0, usAddress.getAccuracy());
 }

 @Test
 public void testValidAddressToUsAddress() {
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(createAddressInstance());
 assertEquals("United States", usAddress.getCountry());
 assertEquals("NY", usAddress.getState());
 assertEquals("Manhattan", usAddress.getCity());
 assertEquals("NYC", usAddress.getCounty());
 assertEquals("10001", usAddress.getPostalCode());
 assertEquals("5th Avenue", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNotNull(usAddress.getCoordinate());
 assertEquals(10, usAddress.getAccuracy());
 }
}