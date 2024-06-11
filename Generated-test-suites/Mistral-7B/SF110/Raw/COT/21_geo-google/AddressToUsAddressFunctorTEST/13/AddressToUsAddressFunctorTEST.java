package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.ListUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class AddressToUsAddressFunctorTest {

 private static GeoAddress testAddress;
 private static GeoUsAddress expectedUsAddress;

 @BeforeClass
 public static void init() {
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("California")));
 country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(ListUtils.list(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco")));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(ListUtils.list(new Locality.LocalityName().content("The City")));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode().postalCodeNumber(ListUtils.list(new PostalCode.PostalCodeNumber().content("94111"))));
 Thoroughfare thoroughfare = new Thoroughfare();
 thoroughfare.setThoroughfareName(ListUtils.list(new ThoroughfareNameType().content("Market Street")));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(thoroughfare);
 testAddress = new GeoAddress();
 testAddress.setAddressDetails(addressDetails);
 testAddress.setCoordinate(new Coordinate().newCoordinate(37.7749, -122.4194));
 testAddress.setAccuracy(10);
 expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCountry("United States");
 expectedUsAddress.setState("California");
 expectedUsAddress.setCity("The City");
 expectedUsAddress.setCounty("San Francisco");
 expectedUsAddress.setPostalCode("94111");
 expectedUsAddress.setAddressLine1("Market Street");
 expectedUsAddress.setAddressLine2("");
 expectedUsAddress.setCoordinate(new Coordinate().newCoordinate(37.7749, -122.4194));
 expectedUsAddress.setAccuracy(10);
 }

 @Test
 public void testExecute_ValidAddress_ReturnsCorrectUsAddress() {
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
 assertEquals(expectedUsAddress, actualUsAddress);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
 AddressToUsAddressFunctor.getInstance().execute(null);
 }

 @Test
 public void testExecute_EmptyAddress_ReturnsEmptyUsAddress() {
 GeoAddress emptyAddress = new GeoAddress();
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(emptyAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
 }

 @Test
 public void testExecute_AddressWithoutCountry_ReturnsEmptyUsAddress() {
 Country emptyCountry = new Country();
 AddressDetails emptyAddressDetails = new AddressDetails();
 emptyAddressDetails.setCountry(emptyCountry);
 GeoAddress addressWithoutCountry = new GeoAddress();
 addressWithoutCountry.setAddressDetails(emptyAddressDetails);
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(addressWithoutCountry);
 assertEquals(new GeoUsAddress(), actualUsAddress);
 }

 @Test
 public void testExecute_AddressWithoutState_ReturnsUsAddressWithEmptyState() {
 Country usCountry = new Country();
 usCountry.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(usCountry);
 GeoAddress addressWithoutState = new GeoAddress();
 addressDetails.setAddressDetails(addressDetails);
 addressWithoutState.setAddressDetails(addressDetails);
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(addressWithoutState);
 assertEquals(new GeoUsAddress().setState(""), actualUsAddress);
 }

 @Test
 public void testExecute_AddressWithoutCity_ReturnsUsAddressWithEmptyCity() {
 Country usCountry = new Country();
 usCountry.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("California")));
 Country country = new Country();
 country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 country.setAdministrativeArea(administrativeArea);
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 GeoAddress addressWithoutCity = new GeoAddress();
 addressDetails.setAddressDetails(addressDetails);
 addressWithoutCity.setAddressDetails(addressDetails);
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(addressWithoutCity);
 assertEquals(new GeoUsAddress().setCity(""), actualUsAddress);
 }

 @Test
 public void testExecute_AddressWithoutPostalCode_ReturnsUsAddressWithEmptyPostalCode() {
 Country usCountry = new Country();
 usCountry.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 Country country = new Country();
 country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("California")));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 GeoAddress addressWithoutPostalCode = new GeoAddress();
 addressDetails.setAddressDetails(addressDetails);
 addressWithoutPostalCode.setAddressDetails(addressDetails);
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(addressWithoutPostalCode);
 assertEquals(new GeoUsAddress().setPostalCode(""), actualUsAddress);
 }

 @Test
 public void testExecute_AddressWithMultipleLocalities_ReturnsCorrectUsAddress() {
 Country usCountry = new Country();
 usCountry.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("California")));
 Country country = new Country();
 country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 country.setAdministrativeArea(administrativeArea);
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 Thoroughfare thoroughfare1 = new Thoroughfare();
 thoroughfare1.setThoroughfareName(ListUtils.list(new ThoroughfareNameType().content("Market Street 1")));
 Thoroughfare thoroughfare2 = new Thoroughfare();
 thoroughfare2.setThoroughfareName(ListUtils.list(new ThoroughfareNameType().content("Market Street 2")));
 Locality locality1 = new Locality();
 locality1.setLocalityName(ListUtils.list(new Locality.LocalityName().content("The City 1")));
 locality1.setPostalCode(new PostalCode().postalCodeNumber(ListUtils.list(new PostalCode.PostalCodeNumber().content("94111"))));
 locality1.setThoroughfare(thoroughfare1);
 Locality locality2 = new Locality();
 locality2.setLocalityName(ListUtils.list(new Locality.LocalityName().content("The City 2")));
 locality2.setPostalCode(new PostalCode().postalCodeNumber(ListUtils.list(new PostalCode.PostalCodeNumber().content("94112"))));
 locality2.setThoroughfare(thoroughfare2);
 country.getAdministrativeArea().getSubAdministrativeArea().setLocality(ListUtils.list(locality1, locality2));
 GeoAddress addressWithMultipleLocalities = new GeoAddress();
 addressDetails.setAddressDetails(addressDetails);
 addressWithMultipleLocalities.setAddressDetails(addressDetails);
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(addressWithMultipleLocalities);
 GeoUsAddress expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCountry("United States");
 expectedUsAddress.setState("California");
 expectedUsAddress.setCity("The City 1, The City 2");
 expectedUsAddress.setCounty("");
 expectedUsAddress.setPostalCode("");
 expectedUsAddress.setAddressLine1("Market Street 1, Market Street 2");
 expectedUsAddress.setAddressLine2("");
 expectedUsAddress.setCoordinate(new Coordinate().newCoordinate(37.7749, -122.4194));
 expectedUsAddress.setAccuracy(10);
 assertEquals(expectedUsAddress, actualUsAddress);
 }

 @Test
 public void testExecute_AddressWithNullThoroughfare_ReturnsEmptyUsAddress() {
 Country usCountry = new Country();
 usCountry.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 Country country = new Country();
 country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode().content("US")));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName().content("California")));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(country);
 GeoAddress addressWithNullThoroughfare = new GeoAddress();
 addressDetails.setAddressDetails(addressDetails);
 addressWithNullThoroughfare.setAddressDetails(addressDetails);
 GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(addressWithNullThoroughfare);
 assertEquals(new GeoUsAddress(), actualUsAddress);
 }
}