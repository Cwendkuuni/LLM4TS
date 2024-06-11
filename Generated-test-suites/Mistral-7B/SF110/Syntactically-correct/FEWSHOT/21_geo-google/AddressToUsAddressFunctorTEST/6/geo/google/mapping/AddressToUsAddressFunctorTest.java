package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.*;
import geo.google.datamodel.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddressToUsAddressFunctorTest {

 @Mock
 private GeoAddress geoAddress;

 @Mock
 private GeoUsAddress geoUsAddress;

 @InjectMocks
 private AddressToUsAddressFunctor addressToUsAddressFunctor;

 @Test
 public void testExecute_ValidGeoAddress_ReturnsUsAddress() {
 // given
 when(geoAddress.getAddressDetails()).thenReturn(getAddressDetails());
 when(geoAddress.getCoordinate()).thenReturn(getCoordinate());

 // when
 GeoUsAddress usAddress = addressToUsAddressFunctor.execute(geoAddress);

 // then
 assertEquals(geoUsAddress, usAddress);
 // verify that execute method was called with the given GeoAddress
 verify(geoAddress).getAddressDetails();
 verify(geoAddress).getCoordinate();
 }

 @Test
 public void testExecute_NullGeoAddress_ReturnsNullUsAddress() {
 // given
 when(geoAddress.getAddressDetails()).thenReturn(null);
 when(geoAddress.getCoordinate()).thenReturn(null);

 // when
 GeoUsAddress usAddress = addressToUsAddressFunctor.execute(geoAddress);

 // then
 assertNull(usAddress);
 // verify that execute method was called with the given GeoAddress
 verify(geoAddress).getAddressDetails();
 verify(geoAddress).getCoordinate();
 }

 @Test
 public void testExecute_EmptyAddressDetails_ReturnsEmptyUsAddress() {
 // given
 AddressDetails addressDetails = new AddressDetails();
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(geoAddress.getCoordinate()).thenReturn(getCoordinate());

 // when
 GeoUsAddress usAddress = addressToUsAddressFunctor.execute(geoAddress);

 // then
 assertEquals(new GeoUsAddress(), usAddress);
 // verify that execute method was called with the given GeoAddress
 verify(geoAddress).getAddressDetails();
 verify(geoAddress).getCoordinate();
 }

 @Test
 public void testSetCountry() {
 // given
 String country = "US";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setCountry(usAddress, country);

 // then
 assertEquals(country, usAddress.getCountry());
 }

 @Test
 public void testSetState() {
 // given
 String state = "CA";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setState(usAddress, state);

 // then
 assertEquals(state, usAddress.getState());
 }

 @Test
 public void testSetCity() {
 // given
 String city = "San Francisco";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setCity(usAddress, city);

 // then
 assertEquals(city, usAddress.getCity());
 }

 @Test
 public void testSetCounty() {
 // given
 String county = "San Mateo";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setCounty(usAddress, county);

 // then
 assertEquals(county, usAddress.getCounty());
 }

 @Test
 public void testSetPostalCode() {
 // given
 String postalCode = "94110";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setPostalCode(usAddress, postalCode);

 // then
 assertEquals(postalCode, usAddress.getPostalCode());
 }

 @Test
 public void testSetAddressLine1() {
 // given
 String addressLine1 = "Market St";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setAddressLine1(usAddress, addressLine1);

 // then
 assertEquals(addressLine1, usAddress.getAddressLine1());
 }

 @Test
 public void testSetAddressLine2() {
 // given
 String addressLine2 = "No. 1600";
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setAddressLine2(usAddress, addressLine2);

 // then
 assertEquals(addressLine2, usAddress.getAddressLine2());
 }

 @Test
 public void testSetCoordinate() {
 // given
 GeoCoordinate coordinate = getCoordinate();
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setCoordinate(usAddress, coordinate);

 // then
 assertEquals(coordinate, usAddress.getCoordinate());
 }

 @Test
 public void testSetAccuracy() {
 // given
 double accuracy = 1.2;
 GeoUsAddress usAddress = new GeoUsAddress();

 // when
 addressToUsAddressFunctor.setAccuracy(usAddress, accuracy);

 // then
 assertEquals(accuracy, usAddress.getAccuracy());
 }

 private AddressDetails getAddressDetails() {
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new CountryNameCode("US", "United States"));
 country.setAdministrativeArea(new AdministrativeArea());
 country.setLocality(new Locality());
 country.setPostalCode(new PostalCode());
 addressDetails.setCountry(country);
 return addressDetails;
 }

 private GeoCoordinate getCoordinate() {
 GeoCoordinate coordinate = new GeoCoordinate();
 coordinate.setLatitude(37.7749295);
 coordinate.setLongitude(-122.4194155);
 return coordinate;
 }
}