package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.ListUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

 @Test
 public void testExecute_ValidAddress_ShouldCreateUsAddressInstance() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
  inputAddress.setAddressDetails(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  AddressDetails.Locality locality = new AddressDetails.Locality();
  locality.setLocalityName(ListUtils.list(new AddressDetails.Locality.LocalityName().content("CityName")));
  adminArea.setLocality(locality);
  country.setAdministrativeArea(adminArea);
  inputAddress.setAddressDetails(country);
  Thoroughfare thoroughfare = new Thoroughfare();
  ThoroughfareNameType addressLine1 = new ThoroughfareNameType();
  addressLine1.setContent("AddressLine1");
  ThoroughfareNameType addressLine2 = new ThoroughfareNameType();
  addressLine2.setContent("AddressLine2");
  thoroughfare.setThoroughfareName(ListUtils.list(addressLine1, addressLine2));
  locality.setThoroughfare(thoroughfare);
  PostalCode postalCode = new PostalCode();
  PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
  postalCodeNumber.setContent("12345");
  postalCode.setPostalCodeNumber(ListUtils.list(postalCodeNumber));
  locality.setPostalCode(postalCode);
  adminArea.setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("StateName")));
  country.setAdministrativeArea(adminArea);
  inputAddress.setCoordinate(new Coordinate(1.0, 2.0));
  inputAddress.setAccuracy(50.0);

  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("US");
  expectedOutput.setState("StateName");
  expectedOutput.setCity("CityName");
  expectedOutput.setPostalCode("12345");
  expectedOutput.setAddressLine1("AddressLine1");
  expectedOutput.setAddressLine2("AddressLine2");
  expectedOutput.setCoordinate(new Coordinate(1.0, 2.0));
  expectedOutput.setAccuracy(50.0);

  GeoUsAddress actualOutput = functor.execute(inputAddress);

  assertEquals(expectedOutput.getCountry(), actualOutput.getCountry());
  assertEquals(expectedOutput.getState(), actualOutput.getState());
  assertEquals(expectedOutput.getCity(), actualOutput.getCity());
  assertEquals(expectedOutput.getPostalCode(), actualOutput.getPostalCode());
  assertEquals(expectedOutput.getAddressLine1(), actualOutput.getAddressLine1());
  assertEquals(expectedOutput.getAddressLine2(), actualOutput.getAddressLine2());
  assertEquals(expectedOutput.getCoordinate().getLatitude(), actualOutput.getCoordinate().getLatitude(), 0.0);
  assertEquals(expectedOutput.getCoordinate().getLongitude(), actualOutput.getCoordinate().getLongitude(), 0.0);
  assertEquals(expectedOutput.getAccuracy(), actualOutput.getAccuracy(), 0.0);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ShouldThrowGeoException() {
  GeoAddress inputAddress = null;
  functor.execute(inputAddress);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithoutCountry_ShouldThrowGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails();
  inputAddress.setAddressDetails(country);
  functor.execute(inputAddress);
 }

 @Test(expected = GeoException.class)
 public void testExecute_EmptyCountryList_ShouldThrowGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails();
  AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
  countryCode.setContent("US");
  country.setCountryNameCode(ListUtils.list(countryCode));
  inputAddress.setAddressDetails(country);
  functor.execute(inputAddress);
 }

 @Test
 public void testExecute_AddressWithMultipleSubAdminArea_ShouldCreateUsAddressInstanceWithTwoCounties() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
  inputAddress.setAddressDetails(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  AddressDetails.Locality locality1 = new AddressDetails.Locality();
  AdministrativeArea.SubAdministrativeArea subAdminArea1 = new AdministrativeArea.SubAdministrativeArea();
  subAdminArea1.setSubAdministrativeAreaName(ListUtils.list(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("County1")));
  subAdminArea1.setCountry(country);
  locality1.setSubAdministrativeArea(subAdminArea1);
  locality1.setLocalityName(ListUtils.list(new AddressDetails.Locality.LocalityName().content("CityName1")));
  adminArea.setLocality(locality1);
  AddressDetails.Locality locality2 = new AddressDetails.Locality();
  AdministrativeArea.SubAdministrativeArea subAdminArea2 = new AdministrativeArea.SubAdministrativeArea();
  subAdminArea2.setSubAdministrativeAreaName(ListUtils.list(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("County2")));
  subAdminArea2.setCountry(country);
  locality2.setSubAdministrativeArea(subAdminArea2);
  locality2.setLocalityName(ListUtils.list(new AddressDetails.Locality.LocalityName().content("CityName2")));
  adminArea.setLocality(locality2);
  country.setAdministrativeArea(adminArea);
  inputAddress.setAddressDetails(country);
  Thoroughfare thoroughfare = new Thoroughfare();
  ThoroughfareNameType addressLine1 = new ThoroughfareNameType();
  addressLine1.setContent("AddressLine1");
  ThoroughfareNameType addressLine2 = new ThoroughfareNameType();
  addressLine2.setContent("AddressLine2");
  thoroughfare.setThoroughfareName(ListUtils.list(addressLine1, addressLine2));
  locality1.setThoroughfare(thoroughfare);
  PostalCode postalCode = new PostalCode();
  PostalCodeNumber postalCodeNumber1 = new PostalCodeNumber();
  postalCodeNumber1.setContent("12345");
  postalCodeNumber1.setPostalCodeType("USPS");
  PostalCodeNumber postalCodeNumber2 = new PostalCodeNumber();
  postalCodeNumber2.setContent("67890");
  postalCodeNumber2.setPostalCodeType("ZIP4");
  postalCode.setPostalCodeNumber(ListUtils.list(postalCodeNumber1, postalCodeNumber2));
  locality1.setPostalCode(postalCode);
  PostalCode postalCode2 = new PostalCode();
  PostalCodeNumber postalCodeNumber3 = new PostalCodeNumber();
  postalCodeNumber3.setContent("11111");
  postalCodeNumber3.setPostalCodeType("ZIP5");
  postalCode2.setPostalCodeNumber(ListUtils.list(postalCodeNumber3));
  locality2.setPostalCode(postalCode2);
  adminArea.setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("StateName")));
  country.setAdministrativeArea(adminArea);
  inputAddress.setCoordinate(new Coordinate(1.0, 2.0));
  inputAddress.setAccuracy(50.0);

  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("US");
  expectedOutput.setState("StateName");
  expectedOutput.setCity("CityName1");
  expectedOutput.setCounty("County1");
  expectedOutput.setCity("CityName2");
  expectedOutput.setCounty("County2");
  expectedOutput.setPostalCode("12345");
  expectedOutput.setPostalCode("67890");
  expectedOutput.setAddressLine1("AddressLine1");
  expectedOutput.setAddressLine2("AddressLine2");
  expectedOutput.setCoordinate(new Coordinate(1.0, 2.0));
  expectedOutput.setAccuracy(50.0);

  GeoUsAddress actualOutput = functor.execute(inputAddress);

  assertEquals(expectedOutput.getCountry(), actualOutput.getCountry());
  assertEquals(expectedOutput.getState(), actualOutput.getState());
  assertEquals(expectedOutput.getCity(), actualOutput.getCity());
  assertEquals(expectedOutput.getCounty(), actualOutput.getCounty());
  assertEquals(expectedOutput.getCity(), actualOutput.getCity());
  assertEquals(expectedOutput.getCounty(), actualOutput.getCounty());
  assertEquals(expectedOutput.getPostalCode().get(0).getContent(), actualOutput.getPostalCode().get(0).getContent());
  assertEquals(expectedOutput.getPostalCode().get(1).getContent(), actualOutput.getPostalCode().get(1).getContent());
  assertEquals(expectedOutput.getAddressLine1(), actualOutput.getAddressLine1());
  assertEquals(expectedOutput.getAddressLine2(), actualOutput.getAddressLine2());
  assertEquals(expectedOutput.getCoordinate().getLatitude(), actualOutput.getCoordinate().getLatitude(), 0.0);
  assertEquals(expectedOutput.getCoordinate().getLongitude(), actualOutput.getCoordinate().getLongitude(), 0.0);
  assertEquals(expectedOutput.getAccuracy(), actualOutput.getAccuracy(), 0.0);
 }

}