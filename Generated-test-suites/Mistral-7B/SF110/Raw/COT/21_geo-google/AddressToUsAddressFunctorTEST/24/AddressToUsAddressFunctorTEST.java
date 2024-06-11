package geo.google.mapping.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.AddressToUsAddressFunctor.GeoException;

public class AddressToUsAddressFunctorTest {

 private GeoAddress createTestAddress() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>(Arrays.asList(new AddressDetails.Country.CountryNameCode()
      .withContent("US"))));
  details.setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setName("New York");
  adminArea.setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>(Arrays.asList(new AdministrativeArea.AdministrativeAreaName()
      .withContent("NY"))));
  country.setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  locality.setName("New York");
  locality.setLocalityName(new ArrayList<Locality.LocalityName>(Arrays.asList(new Locality.LocalityName()
      .withContent("NY"))));
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>(Collections.singletonList(new PostalCode.PostalCodeNumber()
      .withContent("10001"))));
  locality.setPostalCode(postalCode);
  ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
  thoroughfare.setContent("Broadway");
  locality.setThoroughfare(new Thoroughfare(thoroughfare));
  address.setAddressDetails(details);
  address.setCoordinate(new GeoCoordinate(40.7128, -74.0060));
  return address;
 }

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
  GeoAddress inputAddress = createTestAddress();
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("USA");
  expectedOutput.setState("NY");
  expectedOutput.setCity("New York");
  expectedOutput.setPostalCode("10001");
  expectedOutput.setAddressLine1("Broadway");
  GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
  assertEquals(expectedOutput.getCountry(), outputAddress.getCountry());
  assertEquals(expectedOutput.getState(), outputAddress.getState());
  assertEquals(expectedOutput.getCity(), outputAddress.getCity());
  assertEquals(expectedOutput.getPostalCode(), outputAddress.getPostalCode());
  assertEquals(expectedOutput.getAddressLine1(), outputAddress.getAddressLine1());
  assertTrue(StringUtils.isBlank(outputAddress.getAddressLine2()));
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsException() {
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(null);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullAddressDetails_ThrowsException() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(null);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullCountry_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  address.setAddressDetails(details);
  details.setCountry(null);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptyCountryNameCodes_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  country.setCountryNameCode(new ArrayList<>());
  address.setAddressDetails(details);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullAdminArea_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  country.setAdministrativeArea(null);
  address.setAddressDetails(details);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptyAdminAreaName_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setName(null);
  country.setAdministrativeArea(adminArea);
  address.setAddressDetails(details);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullSubAdminArea_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setName("New York");
  country.setAdministrativeArea(adminArea);
  address.setAddressDetails(details);
  adminArea.setSubAdministrativeArea(null);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptySubAdminAreaName_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setName("New York");
  country.setAdministrativeArea(adminArea);
  SubAdministrativeArea subAdminArea = new SubAdministrativeArea();
  subAdminArea.setName(null);
  adminArea.setSubAdministrativeArea(subAdminArea);
  address.setAddressDetails(details);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullLocality_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setName("New York");
  country.setAdministrativeArea(adminArea);
  address.setAddressDetails(details);
  address.setLocality(null);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullThoroughfare_ThrowsException() {
  GeoAddress address = new GeoAddress();
  AddressDetails details = new AddressDetails();
  Country country = new Country();
  country.setName("USA");
  details.setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setName("New York");
  country.setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  locality.setName("New York");
  address.setAddressDetails(details);
  address.setLocality(locality);
  locality.setThoroughfare(null);
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  functor.execute(address);
 }

 @Test
 public void testExecute_AddressWithMultipleThoroughfareNames_ReturnsUsAddressWithBothLines() {
  GeoAddress inputAddress = createTestAddress();
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("USA");
  expectedOutput.setState("NY");
  expectedOutput.setCity("New York");
  expectedOutput.setPostalCode("10001");
  expectedOutput.setAddressLine1("Broadway");
  expectedOutput.setAddressLine2("Something Else");
  GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
  assertEquals(expectedOutput.getCountry(), outputAddress.getCountry());
  assertEquals(expectedOutput.getState(), outputAddress.getState());
  assertEquals(expectedOutput.getCity(), outputAddress.getCity());
  assertEquals(expectedOutput.getPostalCode(), outputAddress.getPostalCode());
  assertEquals(expectedOutput.getAddressLine1(), outputAddress.getAddressLine1());
  assertEquals(expectedOutput.getAddressLine2(), outputAddress.getAddressLine2());
 }

 @Test
 public void testExecute_AddressWithNullPostalCode_ReturnsUsAddressWithEmptyPostalCode() {
  GeoAddress inputAddress = createTestAddress();
  PostalCode postalCode = null;
  inputAddress.setAddressDetails(inputAddress.getAddressDetails()).getPostalCode().setPostalCodeNumber(CollectionUtils.list(postalCode));
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("USA");
  expectedOutput.setState("NY");
  expectedOutput.setCity("New York");
  expectedOutput.setAddressLine1("Broadway");
  expectedOutput.setPostalCode("");
  GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
  assertEquals(expectedOutput.getCountry(), outputAddress.getCountry());
  assertEquals(expectedOutput.getState(), outputAddress.getState());
  assertEquals(expectedOutput.getCity(), outputAddress.getCity());
  assertEquals(expectedOutput.getAddressLine1(), outputAddress.getAddressLine1());
  assertEquals(expectedOutput.getPostalCode(), outputAddress.getPostalCode());
 }

}