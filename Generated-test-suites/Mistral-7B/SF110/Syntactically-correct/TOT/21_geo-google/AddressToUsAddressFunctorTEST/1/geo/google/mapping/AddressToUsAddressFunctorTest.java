import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

public class AddressToUsAddressFunctorTest {

 private GeoAddress createValidGeoAddress() {
 GeoAddress addr = new GeoAddress();
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCode("US"));
 country.setCountryName(new CountryName("United States"));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeAreaName("CA"));
 country.getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
 country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new SubAdministrativeAreaName("Los Angeles"));
 Locality locality = new Locality();
 locality.setLocalityName(new LocalityName("Santa Monica"));
 locality.setPostalCode(new PostalCode());
 locality.getPostalCode().setPostalCodeNumber(new PostalCodeNumber("90401"));
 addressDetails.setCountry(country);
 addr.setAddressDetails(addressDetails);
 addr.setCoordinate(new Coordinate(34.0, -118.3));
 addr.setAccuracy(10.0);
 return addr;
 }

 @Test
 public void testValidAddressToUsAddressConversion() {
 GeoAddress addr = createValidGeoAddress();
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 expectedAddress.setCountry("United States");
 expectedAddress.setState("CA");
 expectedAddress.setCounty("Los Angeles");
 expectedAddress.setCity("Santa Monica");
 expectedAddress.setPostalCode("90401");
 expectedAddress.setAddressLine1("Santa Monica");
 expectedAddress.setCoordinate(addr.getCoordinate().clone());
 expectedAddress.setAccuracy(addr.getAccuracy());
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test
 public void testEmptyAddressToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertNull(expectedAddress.getCountry());
 Assert.assertNull(expectedAddress.getState());
 Assert.assertNull(expectedAddress.getCounty());
 Assert.assertNull(expectedAddress.getCity());
 Assert.assertNull(expectedAddress.getPostalCode());
 Assert.assertNull(expectedAddress.getAddressLine1());
 Assert.assertEquals(null, usAddress.getCoordinate());
 Assert.assertEquals(0.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test(expected = GeoException.class)
 public void testNullAddressToUsAddressConversion() {
 GeoAddress addr = null;
 AddressToUsAddressFunctor.getInstance().execute(addr);
 }

 @Test
 public void testAddressWithNullCoordinateToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 addr.setAddressDetails(new AddressDetails());
 addr.setAccuracy(10.0);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertNull(expectedAddress.getCountry());
 Assert.assertNull(expectedAddress.getState());
 Assert.assertNull(expectedAddress.getCounty());
 Assert.assertNull(expectedAddress.getCity());
 Assert.assertNull(expectedAddress.getPostalCode());
 Assert.assertNull(expectedAddress.getAddressLine1());
 Assert.assertEquals(null, usAddress.getCoordinate());
 Assert.assertEquals(10.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test
 public void testAddressWithMissingCountryDataToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 AddressDetails addressDetails = new AddressDetails();
 addr.setAddressDetails(addressDetails);
 addr.setCoordinate(new Coordinate(34.0, -118.3));
 addr.setAccuracy(10.0);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertNull(expectedAddress.getCountry());
 Assert.assertNull(expectedAddress.getState());
 Assert.assertNull(expectedAddress.getCounty());
 Assert.assertNull(expectedAddress.getCity());
 Assert.assertNull(expectedAddress.getPostalCode());
 Assert.assertNull(expectedAddress.getAddressLine1());
 Assert.assertEquals(null, usAddress.getCoordinate());
 Assert.assertEquals(10.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test
 public void testAddressWithMissingStateDataToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCode("US"));
 country.setCountryName(new CountryName("United States"));
 addressDetails.setCountry(country);
 addr.setAddressDetails(addressDetails);
 addr.setCoordinate(new Coordinate(34.0, -118.3));
 addr.setAccuracy(10.0);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertEquals("United States", expectedAddress.getCountry());
 Assert.assertNull(expectedAddress.getState());
 Assert.assertNull(expectedAddress.getCounty());
 Assert.assertNull(expectedAddress.getCity());
 Assert.assertNull(expectedAddress.getPostalCode());
 Assert.assertNull(expectedAddress.getAddressLine1());
 Assert.assertEquals(null, usAddress.getCoordinate());
 Assert.assertEquals(10.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test
 public void testAddressWithMissingLocalityDataToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCode("US"));
 country.setCountryName(new CountryName("United States"));
 AdministrativeArea adminArea = new AdministrativeArea();
 addressDetails.setCountry(country);
 adminArea.setAdministrativeAreaName(new AdministrativeAreaName("CA"));
 addressDetails.setAdministrativeArea(adminArea);
 addr.setAddressDetails(addressDetails);
 addr.setCoordinate(new Coordinate(34.0, -118.3));
 addr.setAccuracy(10.0);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertEquals("United States", expectedAddress.getCountry());
 Assert.assertEquals("CA", expectedAddress.getState());
 Assert.assertNull(expectedAddress.getCounty());
 Assert.assertNull(expectedAddress.getCity());
 Assert.assertNull(expectedAddress.getPostalCode());
 Assert.assertNull(expectedAddress.getAddressLine1());
 Assert.assertEquals(null, usAddress.getCoordinate());
 Assert.assertEquals(10.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test
 public void testAddressWithMissingPostalCodeDataToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCode("US"));
 country.setCountryName(new CountryName("United States"));
 AdministrativeArea adminArea = new AdministrativeArea();
 addressDetails.setCountry(country);
 adminArea.setAdministrativeAreaName(new AdministrativeAreaName("CA"));
 addressDetails.setAdministrativeArea(adminArea);
 Locality locality = new Locality();
 addressDetails.setLocality(locality);
 addr.setAddressDetails(addressDetails);
 addr.setCoordinate(new Coordinate(34.0, -118.3));
 addr.setAccuracy(10.0);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertEquals("United States", expectedAddress.getCountry());
 Assert.assertEquals("CA", expectedAddress.getState());
 Assert.assertEquals("Los Angeles", expectedAddress.getCounty());
 Assert.assertNull(expectedAddress.getCity());
 Assert.assertNull(expectedAddress.getPostalCode());
 Assert.assertNull(expectedAddress.getAddressLine1());
 Assert.assertEquals(null, usAddress.getCoordinate());
 Assert.assertEquals(10.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }

 @Test
 public void testAddressWithMultipleLinesToUsAddressConversion() {
 GeoAddress addr = new GeoAddress();
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCode("US"));
 country.setCountryName(new CountryName("United States"));
 AdministrativeArea adminArea = new AdministrativeArea();
 addressDetails.setCountry(country);
 adminArea.setAdministrativeAreaName(new AdministrativeAreaName("CA"));
 addressDetails.setAdministrativeArea(adminArea);
 Locality locality = new Locality();
 addressDetails.setLocality(locality);
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(new ThoroughfareNameType("Main Street"));
 locality.setThoroughfare(thoroughfare);
 PostalCode postalCode = new PostalCode();
 postalCode.setPostalCodeNumber(new PostalCodeNumber("90401"));
 locality.setPostalCode(postalCode);
 addr.setAddressDetails(addressDetails);
 addr.setCoordinate(new Coordinate(34.0, -118.3));
 addr.setAccuracy(10.0);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(addr);
 GeoUsAddress expectedAddress = new GeoUsAddress();
 Assert.assertEquals("United States", expectedAddress.getCountry());
 Assert.assertEquals("CA", expectedAddress.getState());
 Assert.assertEquals("Los Angeles", expectedAddress.getCounty());
 Assert.assertEquals("Santa Monica", expectedAddress.getCity());
 Assert.assertEquals("Main Street", expectedAddress.getAddressLine1());
 Assert.assertEquals("Santa Monica", expectedAddress.getAddressLine2());
 Assert.assertEquals("90401", expectedAddress.getPostalCode());
 Assert.assertEquals(new Coordinate(34.0, -118.3), usAddress.getCoordinate());
 Assert.assertEquals(10.0, usAddress.getAccuracy(), 0.01);
 Assert.assertEquals(expectedAddress, usAddress);
 }
}