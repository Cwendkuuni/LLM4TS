package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new Country());
  Country country = addressDetails.getCountry();
  country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
  addressDetails.setAdministrativeArea(new AdministrativeArea());
  AdministrativeArea administrativeArea = addressDetails.getAdministrativeArea();
  administrativeArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName().content("California")));
  administrativeArea.setSubAdministrativeArea(new SubAdministrativeArea());
  SubAdministrativeArea subAdministrativeArea = administrativeArea.getSubAdministrativeArea();
  subAdministrativeArea.setSubAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco")));
  Locality locality = (subAdministrativeArea == null) ? administrativeArea : subAdministrativeArea;
  locality.setLocalityName(Collections.singletonList(new Locality.LocalityName().content("The City")));
  PostalCode postalCode = locality.getPostalCode();
  postalCode.setPostalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber().content("94111")));
  ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
  thoroughfare.setContent("Main Street");
  locality.setThoroughfare(new Thoroughfare());
  locality.getThoroughfare().setThoroughfareName(Collections.singletonList(thoroughfare));
  inputAddress.setCoordinate(new Coordinate());
  inputAddress.setAccuracy(new Accuracy());

  GeoUsAddress outputAddress = functor.execute(inputAddress);

  assertEquals("United States", outputAddress.getCountry());
  assertEquals("California", outputAddress.getState());
  assertEquals("San Francisco", outputAddress.getCounty());
  assertEquals("The City", outputAddress.getCity());
  assertEquals("94111", outputAddress.getPostalCode());
  assertEquals("Main Street", outputAddress.getAddressLine1());
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
  GeoAddress inputAddress = null;

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_InvalidCountry_ThrowsGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new Country());
  Country country = addressDetails.getCountry();
  country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("CA")));

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_MissingCountryName_ThrowsGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(null);

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_MissingState_ThrowsGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new Country());
  Country country = addressDetails.getCountry();
  country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
  addressDetails.setAdministrativeArea(new AdministrativeArea());

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_MissingCity_ThrowsGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new Country());
  Country country = addressDetails.getCountry();
  country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
  addressDetails.setAdministrativeArea(new AdministrativeArea());
  AdministrativeArea administrativeArea = addressDetails.getAdministrativeArea();
  administrativeArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName().content("California")));

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_MissingPostalCode_ThrowsGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new Country());
  Country country = addressDetails.getCountry();
  country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
  addressDetails.setAdministrativeArea(new AdministrativeArea());
  AdministrativeArea administrativeArea = addressDetails.getAdministrativeArea();
  administrativeArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName().content("California")));
  administrativeArea.setSubAdministrativeArea(new SubAdministrativeArea());
  SubAdministrativeArea subAdministrativeArea = administrativeArea.getSubAdministrativeArea();
  subAdministrativeArea.setSubAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco")));
  Locality locality = subAdministrativeArea; // or administrativeArea
  locality.setLocalityName(Collections.singletonList(new Locality.LocalityName().content("The City")));

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_MissingAddressLine1_ThrowsGeoException() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new Country());
  Country country = addressDetails.getCountry();
  country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
  addressDetails.setAdministrativeArea(new AdministrativeArea());
  AdministrativeArea administrativeArea = addressDetails.getAdministrativeArea();
  administrativeArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName().content("California")));
  administrativeArea.setSubAdministrativeArea(new SubAdministrativeArea());
  SubAdministrativeArea subAdministrativeArea = administrativeArea.getSubAdministrativeArea();
  subAdministrativeArea.setSubAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco")));
  Locality locality = subAdministrativeArea; // or administrativeArea
  locality.setLocalityName(Collections.singletonList(new Locality.LocalityName().content("The City")));
  locality.setPostalCode(new PostalCode());
  PostalCode postalCode = locality.getPostalCode();
  postalCode.setPostalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber().content("94111")));

  try {
   functor.execute(inputAddress);
   fail("Expected GeoException to be thrown");
  } catch (GeoException e) {
   // Expected exception
  }
 }

 @Test
 public void testExecute_EmptyAddress_ReturnsEmptyUsAddress() {
  GeoAddress inputAddress = new GeoAddress();
  GeoUsAddress outputAddress = functor.execute(inputAddress);
  assertNull(outputAddress.getCountry());
  assertNull(outputAddress.getState());
  assertNull(outputAddress.getCounty());
  assertNull(outputAddress.getCity());
  assertNull(outputAddress.getPostalCode());
  assertNull(outputAddress.getAddressLine1());
 }

}