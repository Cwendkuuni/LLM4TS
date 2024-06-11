package geo.google.mapping.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.AddressDetails.Country;
import geo.google.datamodel.AddressDetails.Country.CountryNameCode;
import geo.google.datamodel.AddressDetails.AdministrativeArea;
import geo.google.datamodel.AddressDetails.AdministrativeArea.AdministrativeAreaName;
import geo.google.datamodel.AddressDetails.AdministrativeArea.SubAdministrativeArea;
import geo.google.datamodel.AddressDetails.AdministrativeArea.SubAdministrativeAreaName;
import geo.google.datamodel.AddressDetails.Locality;
import geo.google.datamodel.AddressDetails.Locality.LocalityName;
import geo.google.datamodel.AddressDetails.PostalCode;
import geo.google.datamodel.AddressDetails.PostalCode.PostalCodeNumber;
import geo.google.datamodel.ThoroughfareNameType;
import geo.google.mapping.AddressToUsAddressFunctor;

public class AddressToUsAddressFunctorTest {

 private static final GeoAddress ADDRESS = new GeoAddress();
 static {
 ADDRESS.setAddressDetails(new AddressDetails());
 }

 @Test
 public void testEmptyAddress() {
 GeoUsAddress expected = new GeoUsAddress();
 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountry() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setState("State Name"); // not set as there is no administrative area
 expected.setCounty("County Name"); // not set as there is no sub-administrative area
 expected.setCity("City Name");
 expected.setPostalCode("12345");
 expected.setAddressLine1("Thoroughfare Name 1");
 expected.setAddressLine2("Thoroughfare Name 2");

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithOnlyCountryAndThoroughfare() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setThoroughfare(new Thoroughfare());
 Thoroughfare thoroughfare = new Thoroughfare();
 ThoroughfareNameType name = new ThoroughfareNameType();
 name.setContent("Thoroughfare Name 1");
 thoroughfare.setThoroughfareName(Arrays.asList(name));
 addressDetails.setThoroughfare(thoroughfare);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setAddressLine1("Thoroughfare Name 1");

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndPostalCode() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));
 PostalCode postalCode = new PostalCode();
 PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
 postalCodeNumber.setContent("12345");
 postalCode.setPostalCodeNumber(Arrays.asList(postalCodeNumber));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setPostalCode(postalCode);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setPostalCode("12345");

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndLocality() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));

 Locality locality = new Locality();
 LocalityName localityName = new LocalityName();
 localityName.setContent("City Name");
 locality.setLocalityName(Arrays.asList(localityName));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setLocality(locality);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setCity("City Name");

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndLocalityAndThoroughfare() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));

 Locality locality = new Locality();
 LocalityName localityName = new LocalityName();
 localityName.setContent("City Name");
 locality.setLocalityName(Arrays.asList(localityName));
 ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
 thoroughfareName.setContent("Thoroughfare Name 1");
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
 locality.setThoroughfare(thoroughfare);
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setLocality(locality);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setCity("City Name");
 expected.setAddressLine1("Thoroughfare Name 1");

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndAdministrativeArea() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));
 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setName("State Name");
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(administrativeArea);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setState("State Name");
 expected.setCity("City Name"); // not set as there is no locality

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndAdministrativeAreaAndSubAdministrativeArea() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));

 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setName("State Name");
 SubAdministrativeArea subAdministrativeArea = new SubAdministrativeArea();
 subAdministrativeArea.setName("County Name");
 administrativeArea.setSubAdministrativeArea(subAdministrativeArea);
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(administrativeArea);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setState("State Name");
 expected.setCounty("County Name");
 expected.setCity("City Name"); // not set as there is no locality

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndAdministrativeAreaAndLocality() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));

 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setName("State Name");
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(administrativeArea);

 Locality locality = new Locality();
 LocalityName localityName = new LocalityName();
 localityName.setContent("City Name");
 locality.setLocalityName(Arrays.asList(localityName));
 addressDetails.setLocality(locality);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setState("State Name");
 expected.setCity("City Name");
 expected.setPostalCode("12345"); // not set as there is no postal code

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndAdministrativeAreaAndLocalityAndThoroughfare() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));

 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setName("State Name");
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(administrativeArea);

 Locality locality = new Locality();
 LocalityName localityName = new LocalityName();
 localityName.setContent("City Name");
 locality.setLocalityName(Arrays.asList(localityName));
 locality.setThoroughfare(new Thoroughfare());
 ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
 thoroughfareName.setContent("Thoroughfare Name 1");
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
 locality.setThoroughfare(thoroughfare);
 addressDetails.setLocality(locality);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setState("State Name");
 expected.setCity("City Name");
 expected.setAddressLine1("Thoroughfare Name 1");
 expected.setPostalCode("12345"); // not set as there is no postal code

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCountryAndAdministrativeAreaAndLocalityAndThoroughfareAndPostalCode() {
 Country country = new Country();
 country.setName("Country Name");
 country.setCountryNameCode(Arrays.asList(new CountryNameCode().content("US")));

 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setName("State Name");
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(administrativeArea);

 Locality locality = new Locality();
 LocalityName localityName = new LocalityName();
 localityName.setContent("City Name");
 locality.setLocalityName(Arrays.asList(localityName));
 locality.setThoroughfare(new Thoroughfare());
 ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
 thoroughfareName.setContent("Thoroughfare Name 1");
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
 locality.setThoroughfare(thoroughfare);
 locality.setPostalCode(new PostalCode());
 PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
 postalCodeNumber.setContent("12345");
 postalCode.setPostalCodeNumber(Arrays.asList(postalCodeNumber));
 locality.setPostalCode(postalCode);
 addressDetails.setLocality(locality);
 ADDRESS.setAddressDetails(addressDetails);

 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setState("State Name");
 expected.setCity("City Name");
 expected.setAddressLine1("Thoroughfare Name 1");
 expected.setPostalCode("12345");

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 @Test
 public void testAddressWithCoordinate() {
 ADDRESS.getAddressDetails().setCoordinate(new Coordinate());
 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("Country Name");
 expected.setPostalCode("12345"); // not set as there is no postal code
 expected.setCoordinate(ADDRESS.getAddressDetails().getCoordinate().clone());

 GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(ADDRESS);
 assertEquality(expected, actual);
 }

 private void assertEquality(GeoUsAddress expected, GeoUsAddress actual) {
 assertEquals(expected.getCountry(), actual.getCountry());
 assertEquals(expected.getState(), actual.getState());
 assertEquals(expected.getCounty(), actual.getCounty());
 assertEquals(expected.getCity(), actual.getCity());
 assertEquals(expected.getPostalCode(), actual.getPostalCode());
 assertEquals(expected.getAddressLine1(), actual.getAddressLine1());
 assertEquals(expected.getAddressLine2(), actual.getAddressLine2());
 assertEquals(expected.getCoordinate(), actual.getCoordinate());
 assertEquals(expected.getAccuracy(), actual.getAccuracy());
 }
}