import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.Country;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.ThoroughfareNameType;
import geo.google.mapping.AddressToUsAddressFunctor;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetailsType;
import oasis.names.tc.ciq.xsdschema.xal._2.CountryNameCodeType;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCodeNumberType;
import oasis.names.tc.ciq.xsdschema.xal._2.SubAdministrativeAreaNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.ThoroughfareNameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AddressToUsAddressFunctorTest {

 private AddressDetailsType addressDetails;
 private GeoAddress geoAddress;
 private GeoUsAddress geoUsAddress;
 private AddressToUsAddressFunctor addressToUsAddressFunctor;

 @Before
 public void setUp() {
 addressDetails = new AddressDetailsType();
 geoAddress = new GeoAddress();
 geoUsAddress = new GeoUsAddress();
 addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithOneCountryNameCodeAndCountryName() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCodeType("US", "United States"));
 addressDetails.setCountry(country);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("United States", result.getCountry());
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithMultipleCountryNameCodesAndFirstIsUs() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(Arrays.asList(
 new CountryNameCodeType("US", "United States"),
 new CountryNameCodeType("CA", "Canada")
 ));
 addressDetails.setCountry(country);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("United States", result.getCountry());
 }

 @Test(expected = GeoException.class)
 public void testExecuteWhenAddressHasNoCountry() {
 // Given
 geoAddress.setAddressDetails(new AddressDetailsType());

 // When
 addressToUsAddressFunctor.execute(geoAddress);

 // Then
 fail("Should throw GeoException when address has no country");
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithOneAdministrativeAreaAndOneLocality() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCodeType("US", "United States"));
 adminArea = new AdministrativeArea();
 adminArea.setAdministrativeAreaName(new AdministrativeAreaNameType("California"));
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 locality = new Locality();
 locality.setLocalityName(new LocalityNameType("Los Angeles"));
 adminArea.setLocality(locality);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("California", result.getState());
 assertEquals("Los Angeles", result.getCity());
 assertEquals("", result.getCounty());
 assertEquals("", result.getAddressLine1());
 assertEquals("", result.getAddressLine2());
 assertEquals("", result.getPostalCode());
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithOneAdministrativeAreaAndTwoLocalities() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCodeType("US", "United States"));
 adminArea = new AdministrativeArea();
 adminArea.setAdministrativeAreaName(new AdministrativeAreaNameType("California"));
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 locality1 = new Locality();
 locality1.setLocalityName(new LocalityNameType("Los Angeles"));
 adminArea.setLocality(locality1);
 locality2 = new Locality();
 locality2.setLocalityName(new LocalityNameType("San Francisco"));
 adminArea.setLocality(locality2);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("California", result.getState());
 assertEquals("Los Angeles", result.getCity());
 assertEquals("", result.getCounty());
 assertEquals("", result.getAddressLine1());
 assertEquals("", result.getAddressLine2());
 assertEquals("", result.getPostalCode());
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithOneAdministrativeAreaAndNoLocalities() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCodeType("US", "United States"));
 adminArea = new AdministrativeArea();
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("", result.getState());
 assertEquals("", result.getCity());
 assertEquals("", result.getCounty());
 assertEquals("", result.getAddressLine1());
 assertEquals("", result.getAddressLine2());
 assertEquals("", result.getPostalCode());
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithNoAdministrativeAreaButHasLocality() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCodeType("US", "United States"));
 locality = new Locality();
 locality.setLocalityName(new LocalityNameType("New York"));
 addressDetails.setCountry(country);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("", result.getState());
 assertEquals("New York", result.getCity());
 assertEquals("", result.getCounty());
 assertEquals("", result.getAddressLine1());
 assertEquals("", result.getAddressLine2());
 assertEquals("", result.getPostalCode());
 }

 @Test
 public void testExecuteWhenAddressHasCountryWithNoAdministrativeAreaNorLocality() {
 // Given
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCodeType("US", "United States"));
 addressDetails.setCountry(country);
 geoAddress.setAddressDetails(addressDetails);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("", result.getState());
 assertEquals("", result.getCity());
 assertEquals("", result.getCounty());
 assertEquals("", result.getAddressLine1());
 assertEquals("", result.getAddressLine2());
 assertEquals("", result.getPostalCode());
 }

 @Test
 public void testExecuteWhenAddressHasCoordinate() {
 // Given
 double lat = 37.7749;
 double lon = -122.4194;
 GeoCoordinateType coordinate = new GeoCoordinateType();
 coordinate.setLatitudeDegrees(lat);
 coordinate.setLongitudeDegrees(lon);
 geoAddress.setCoordinate(coordinate);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals(lat, result.getCoordinate().getLatitudeDegrees(), 0.0);
 assertEquals(lon, result.getCoordinate().getLongitudeDegrees(), 0.0);
 }
}