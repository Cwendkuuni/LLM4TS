package geo.google.mapping.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.Locality;
import geo.google.datamodel.ThoroughfareNameType;
import geo.google.datamodel.coordinates.Coordinate;
import geo.google.datamodel.usaddress.GeoUsAddress;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.AddressToUsAddressFunctor.GeoException;

public class AddressToUsAddressFunctorTest {

 private GeoUsAddress createBaseUsAddress() {
 return new GeoUsAddress();
 }

 @Test
 public void testExecute_ValidAddress_ShouldReturnPopulatedUsAddress() {
 GeoAddress address = createDummyGeoAddress();
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertEquals("USA", usAddress.getCountry());
 assertEquals("State", usAddress.getState());
 assertEquals("County", usAddress.getCounty());
 assertEquals("City", usAddress.getCity());
 assertEquals("PostalCode", usAddress.getPostalCode());
 assertEquals("AddressLine1", usAddress.getAddressLine1());
 assertEquals("AddressLine2", usAddress.getAddressLine2());
 assertNull(usAddress.getThoroughfare());
 assertEquals(12.345678d, usAddress.getAccuracy(), 0.0001);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ShouldThrowGeoException() {
 assertThrows(GeoException.class, () -> AddressToUsAddressFunctor.getInstance().execute(null));
 }

 @Test
 public void testExecute_AddressWithoutCountry_ShouldReturnEmptyUsAddress() {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithEmptyCountryNames_ShouldReturnEmptyUsAddress() {
 GeoAddress address = createDummyGeoAddress();
 address.getAddressDetails().getCountry().setCountryNameCode(Collections.emptyList());
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithMultipleCountryNames_ShouldReturnUsAddressWithFirstCountryName() {
 GeoAddress address = createDummyGeoAddress();
 List<AddressDetails.Country.CountryNameCode> countryNames = Arrays.asList(new AddressDetails.Country.CountryNameCode("CAN"), new AddressDetails.Country.CountryNameCode("USA"));
 address.getAddressDetails().getCountry().setCountryNameCode(countryNames);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertEquals("USA", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithoutAdminArea_ShouldReturnEmptyUsAddress() {
 GeoAddress address = createDummyGeoAddress();
 address.getAddressDetails().setAdministrativeArea(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithoutState_ShouldReturnEmptyUsAddress() {
 GeoAddress address = createDummyGeoAddress();
 AdministrativeArea adminArea = new AdministrativeArea();
 adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("State"));
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setAdministrativeArea(adminArea);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithoutCountryNameAndState_ShouldReturnEmptyUsAddress() {
 GeoAddress address = createDummyGeoAddress();
 AddressDetails addressDetails = address.getAddressDetails();
 addressDetails.setCountry(null);
 addressDetails.setAdministrativeArea(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithEmptySubAdminArea_ShouldReturnUsAddressWithoutCounty() {
 GeoAddress address = createDummyGeoAddress();
 AdministrativeArea adminArea = new AdministrativeArea();
 adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("State"));
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setAdministrativeArea(adminArea);
 SubAdministrativeArea subAdminArea = new SubAdministrativeArea();
 subAdminArea.setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeAreaName("County"));
 address.getAddressDetails().setSubAdministrativeArea(subAdminArea);
 subAdminArea.setSubAdministrativeAreaName(Collections.emptyList());
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertEquals("USA", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithNullPostalCode_ShouldReturnUsAddressWithoutPostalCode() {
 GeoAddress address = createDummyGeoAddress();
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName("City"));
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setLocality(locality);
 PostalCode postalCode = new PostalCode();
 address.getAddressDetails().setPostalCode(postalCode);
 postalCode.setPostalCodeNumber(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertEquals("USA", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithEmptyThoroughfare_ShouldReturnUsAddressWithoutAddressLine1() {
 GeoAddress address = createDummyGeoAddress();
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName("City"));
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setLocality(locality);
 ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
 locality.setThoroughfare(new Thoroughfare());
 locality.getThoroughfare().setThoroughfareName(Arrays.asList(thoroughfare));
 thoroughfare.setContent(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertEquals("USA", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithMultipleThoroughfare_ShouldReturnUsAddressWithFirstAddressLine1() {
 GeoAddress address = createDummyGeoAddress();
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName("City"));
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setLocality(locality);
 ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
 ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
 locality.setThoroughfare(new Thoroughfare());
 locality.getThoroughfare().setThoroughfareName(Arrays.asList(thoroughfare, thoroughfare2));
 thoroughfare.setContent("AddressLine1");
 thoroughfare2.setContent("AddressLine2");
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertEquals("USA", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertEquals("AddressLine1", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithInvalidThoroughfare_ShouldReturnUsAddressWithoutAddressLine1() {
 GeoAddress address = createDummyGeoAddress();
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName("City"));
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setLocality(locality);
 ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
 locality.setThoroughfare(new Thoroughfare());
 locality.getThoroughfare().setThoroughfareName(Arrays.asList(thoroughfare));
 thoroughfare.setContent("Invalid");
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertEquals("USA", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithNullCoordinate_ShouldReturnUsAddressWithoutCoordinate() {
 GeoAddress address = createDummyGeoAddress();
 address.setCoordinate(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 @Test
 public void testExecute_AddressWithInvalidCoordinate_ShouldReturnUsAddressWithoutCoordinate() {
 GeoAddress address = createDummyGeoAddress();
 address.setCoordinate(new Coordinate(10.0, 10.0));
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertEquals(0.0, usAddress.getAccuracy(), 0.001);
 }

 private GeoAddress createDummyGeoAddress() {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 return address;
 }
}