package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AddressToUsAddressFunctor.class})
public class AddressToUsAddressFunctorTest {

 private GeoAddress createTestAddress() {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(createTestAddressDetails());
 address.setCoordinate(new Coordinate(1.0, 2.0));
 address.setAccuracy(10.0);
 return address;
 }

 private AddressDetails createTestAddressDetails() {
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(CollectionUtils.singletonList(new CountryNameCode("US")));
 country.setAdministrativeArea(createTestAdminArea());
 addressDetails.setCountry(country);
 return addressDetails;
 }

 private AdministrativeArea createTestAdminArea() {
 AdministrativeArea adminArea = new AdministrativeArea();
 adminArea.setAdministrativeAreaName(CollectionUtils.singletonList(new AdministrativeAreaName("California")));
 return adminArea;
 }

 @Test
 public void testEmptyAddress() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoAddress input = new GeoAddress();
 GeoUsAddress output = functor.execute(input);
 assertNull(output.getCountry());
 assertNull(output.getState());
 assertNull(output.getCounty());
 assertNull(output.getCity());
 assertNull(output.getPostalCode());
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 assertNull(output.getCoordinate());
 assertNull(output.getAccuracy());
 }

 @Test
 public void testUsAddress() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoAddress input = createTestAddress();
 GeoUsAddress output = functor.execute(input);
 assertEquals("California", output.getCountry());
 assertEquals("California", output.getState());
 assertNull(output.getCounty());
 assertEquals("CityName", output.getCity());
 assertNull(output.getPostalCode());
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 assertEquals(input.getCoordinate(), output.getCoordinate());
 assertEquals(input.getAccuracy(), output.getAccuracy());
 }

 @Test
 public void testAddressWithPostalCode() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoAddress input = createTestAddress();
 input.getLocality().setPostalCode(new PostalCode("12345"));
 GeoUsAddress output = functor.execute(input);
 assertEquals("California", output.getCountry());
 assertEquals("California", output.getState());
 assertNull(output.getCounty());
 assertEquals("CityName", output.getCity());
 assertEquals("12345", output.getPostalCode());
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 assertEquals(input.getCoordinate(), output.getCoordinate());
 assertEquals(input.getAccuracy(), output.getAccuracy());
 }

 @Test
 public void testAddressWithThoroughfare() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoAddress input = createTestAddress();
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(CollectionUtils.singletonList(new ThoroughfareNameType("TestStreet")));
 input.getLocality().setThoroughfare(thoroughfare);
 GeoUsAddress output = functor.execute(input);
 assertEquals("California", output.getCountry());
 assertEquals("California", output.getState());
 assertNull(output.getCounty());
 assertEquals("CityName TestStreet", output.getCity());
 assertNull(output.getPostalCode());
 assertEquals("TestStreet", output.getAddressLine1());
 assertNull(output.getAddressLine2());
 assertEquals(input.getCoordinate(), output.getCoordinate());
 assertEquals(input.getAccuracy(), output.getAccuracy());
 }

 @Test
 public void testAddressWithThoroughfareAndPostalCode() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoAddress input = createTestAddress();
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(CollectionUtils.singletonList(new ThoroughfareNameType("TestStreet")));
 input.getLocality().setThoroughfare(thoroughfare);
 input.getLocality().setPostalCode(new PostalCode("12345"));
 GeoUsAddress output = functor.execute(input);
 assertEquals("California", output.getCountry());
 assertEquals("California", output.getState());
 assertNull(output.getCounty());
 assertEquals("CityName TestStreet", output.getCity());
 assertEquals("12345", output.getPostalCode());
 assertEquals("TestStreet", output.getAddressLine1());
 assertNull(output.getAddressLine2());
 assertEquals(input.getCoordinate(), output.getCoordinate());
 assertEquals(input.getAccuracy(), output.getAccuracy());
 }

 @Test
 public void testAddressWithoutCountryCode() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoAddress input = createTestAddress();
 input.getAddressDetails().setCountry(null);
 GeoUsAddress output = PowerMockito.spy(functor.execute(input));
 assertNull(output.getCountry());
 assertNull(output.getState());
 assertNull(output.getCounty());
 assertNull(output.getCity());
 assertNull(output.getPostalCode());
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 assertEquals(input.getCoordinate(), output.getCoordinate());
 assertEquals(input.getAccuracy(), output.getAccuracy());
 PowerMockito.verifyZeroInteractions(output);
 }
}