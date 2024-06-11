package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import geo.google.*;

import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AddressToUsAddressFunctor.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress createTestAddress() {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails country = new AddressDetails.Country();
 country.setCountryNameCode(ListUtils.customize(new ArrayList<>(), c -> new AddressDetails.Country.CountryNameCode() {{
 setContent("US");
 }}));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdminAreaName(ListUtils.customize(new ArrayList<>(), a -> new AdministrativeArea.AdministrativeAreaName() {{
 setContent("California");
 }}));
 country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(ListUtils.customize(new ArrayList<>(), s -> new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName() {{
 setContent("San Francisco");
 }}));
 country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(ListUtils.customize(new ArrayList<>(), l -> new Locality.LocalityName() {{
 setContent("San Francisco");
 }}));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(ListUtils.customize(new ArrayList<>(), p -> new PostalCode.PostalCodeNumber() {{
 setContent("94102");
 }}));
 address.getAddressDetails().setCountry(country);
 address.setCoordinate(new Coordinate(37.7749, -122.4194));
 address.setAccuracy(AddressAccuracy.ROOFTOP);
 return address;
 }

 @Test
 public void testEmptyAddress() {
 GeoAddress address = new GeoAddress();
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = functor.execute(address);
 assertEquals(0, result.getCountry().length);
 assertEquals(0, result.getState().length);
 assertEquals(0, result.getCity().length);
 assertEquals(0, result.getCounty().length);
 assertEquals(0, result.getPostalCode().length);
 assertEquals(0, result.getAddressLine1().length);
 assertNull(result.getAddressLine2());
 assertNull(result.getCoordinate());
 assertNull(result.getAccuracy());
 }

 @Test
 public void testValidAddress() {
 GeoAddress address = createTestAddress();
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 PowerMockito.mockStatic(CollectionUtils.class);
 when(CollectionUtils.isNotEmpty((Collection)anyObject())).thenReturn(true);
 GeoUsAddress result = functor.execute(address);
 String[] expected = {"US", "California", "San Francisco", "San Francisco", "94102"};
 assertArrayEquals(expected, result.getCountryValue());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("San Francisco", result.getCounty());
 assertEquals("94102", result.getPostalCode());
 assertEquals("San Francisco", result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertEquals(AddressAccuracy.ROOFTOP, result.getAccuracy());
 }

 @Test
 public void testValidAddressWithemptyPostalCode() {
 GeoAddress address = createTestAddress();
 address.getAddressDetails().getSubAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(ListUtils.emptyList());
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = functor.execute(address);
 String[] expected = {"US", "California", "San Francisco", "San Francisco", ""};
 assertArrayEquals(expected, result.getCountryValue());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("San Francisco", result.getCounty());
 assertEquals("", result.getPostalCode());
 assertEquals("San Francisco", result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertEquals(AddressAccuracy.ROOFTOP, result.getAccuracy());
 }

 @Test(expected = GeoException.class)
 public void testNullAddress() {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 functor.execute(null);
 }

}