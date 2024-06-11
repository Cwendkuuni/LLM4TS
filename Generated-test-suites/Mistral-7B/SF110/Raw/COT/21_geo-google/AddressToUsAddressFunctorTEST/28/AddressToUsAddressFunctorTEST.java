package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
  GeoAddress inputAddress = new GeoAddress();
  inputAddress.setAddressDetails(new AddressDetails());
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90001"));
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare = new Thoroughfare();
  thoroughfare.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.setThoroughfare(thoroughfare);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setState("California");
  expectedOutput.setCity("Los Angeles");
  expectedOutput.setPostalCode("90001");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setCoordinate(new GeoCoordinate());
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsException() {
  GeoAddress inputAddress = null;
  try {
   functor.execute(inputAddress);
  } catch (GeoException e) {
   // Expected exception, test message is not necessary
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NoCountry_ThrowsException() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(null);
  try {
   functor.execute(inputAddress);
  } catch (GeoException e) {
   // Expected exception, test message is not necessary
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_EmptyCountryName_ThrowsException() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails addressDetails = inputAddress.getAddressDetails();
  addressDetails.setCountry(new AddressDetails.Country());
  addressDetails.getCountry().getCountryName().clear();
  try {
   functor.execute(inputAddress);
  } catch (GeoException e) {
   // Expected exception, test message is not necessary
  }
 }

 @Test
 public void testExecute_MissingState_ReturnsUsAddressWithEmptyState() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90001"));
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare = new Thoroughfare();
  thoroughfare.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.setThoroughfare(thoroughfare);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setCity("Los Angeles");
  expectedOutput.setPostalCode("90001");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setState("");
  expectedOutput.setCoordinate(new GeoCoordinate());
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

 @Test
 public void testExecute_MissingCity_ReturnsUsAddressWithEmptyCity() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  locality.setLocalityName(null);
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90001"));
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare = new Thoroughfare();
  thoroughfare.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.setThoroughfare(thoroughfare);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setState("California");
  expectedOutput.setCity("");
  expectedOutput.setPostalCode("90001");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setCoordinate(new GeoCoordinate());
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

 @Test
 public void testExecute_MissingPostalCode_ReturnsUsAddressWithEmptyPostalCode() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
  PostalCode postalCode = null;
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare = new Thoroughfare();
  thoroughfare.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.setThoroughfare(thoroughfare);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setState("California");
  expectedOutput.setCity("Los Angeles");
  expectedOutput.setPostalCode("");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setCoordinate(new GeoCoordinate());
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

 @Test
 public void testExecute_MultipleThoroughfareNames_FillsAddressLine2() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90001"));
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare1 = new Thoroughfare();
  thoroughfare1.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.getThoroughfare().add(thoroughfare1);
  Thoroughfare thoroughfare2 = new Thoroughfare();
  thoroughfare2.setThoroughfareName(new ThoroughfareNameType().content("Second Street"));
  locality.getThoroughfare().add(thoroughfare2);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setState("California");
  expectedOutput.setCity("Los Angeles");
  expectedOutput.setPostalCode("90001");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setAddressLine2("Second Street");
  expectedOutput.setCoordinate(new GeoCoordinate());
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

 @Test
 public void testExecute_AddressWithCoordinate_ReturnsUsAddressWithCoordinate() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90001"));
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare = new Thoroughfare();
  thoroughfare.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.setThoroughfare(thoroughfare);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoCoordinate coordinate = new GeoCoordinate();
  coordinate.setLatitude(37.7749);
  coordinate.setLongitude(-122.4194);
  inputAddress.setCoordinate(coordinate);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setState("California");
  expectedOutput.setCity("Los Angeles");
  expectedOutput.setPostalCode("90001");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setCoordinate(new GeoCoordinate(coordinate.getLatitude(), coordinate.getLongitude()));
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

 @Test
 public void testExecute_AddressWithoutCoordinate_ReturnsUsAddressWithNullCoordinate() {
  GeoAddress inputAddress = new GeoAddress();
  AddressDetails country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.getCountryName().add(country.getCountryName().newCountryName().content("United States"));
  inputAddress.getAddressDetails().setCountry(country);
  AdministrativeArea adminArea = new AdministrativeArea();
  adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  inputAddress.getAddressDetails().setAdministrativeArea(adminArea);
  Locality locality = new Locality();
  PostalCode postalCode = new PostalCode();
  postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90001"));
  locality.setPostalCode(postalCode);
  Thoroughfare thoroughfare = new Thoroughfare();
  thoroughfare.setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
  locality.setThoroughfare(thoroughfare);
  inputAddress.getAddressDetails().setLocality(locality);
  GeoUsAddress expectedOutput = new GeoUsAddress();
  expectedOutput.setCountry("United States");
  expectedOutput.setState("California");
  expectedOutput.setCity("Los Angeles");
  expectedOutput.setPostalCode("90001");
  expectedOutput.setAddressLine1("Main Street");
  expectedOutput.setCoordinate(null);
  expectedOutput.setAccuracy(0.0);
  GeoUsAddress output = functor.execute(inputAddress);
  assertEquals(expectedOutput, output);
 }

}