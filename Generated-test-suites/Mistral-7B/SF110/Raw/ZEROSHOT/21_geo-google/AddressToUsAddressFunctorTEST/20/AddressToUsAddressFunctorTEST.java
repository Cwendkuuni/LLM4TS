package geo.google.mapping.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.ThoroughfareNameType;
import geo.google.mapping.AddressToUsAddressFunctor;

public class AddressToUsAddressFunctorTest {

 private GeoAddress inputAddress;
 private GeoUsAddress expectedOutput;

 @Before
 public void setUp() {
 InputAddressSetup();
 }

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddressWithCorrectValues() {
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
 AddressToUsAddressFunctor.getInstance().execute(null);
 fail("Expected GeoException to be thrown but none was.");
 }

 @Test
 public void testExecute_AddressWithCoordinate_ReturnsUsAddressWithCorrectValuesAndCoordinate() {
 inputAddress.setCoordinate(new Coordinate(1.0d, 2.0d));
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals(expectedOutput.clone(), actualOutput);
 }

 @Test
 public void testExecute_AddressWithoutCountry_ReturnsEmptyUsAddress() {
 inputAddress.getAddressDetails().setCountry(null);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals(new GeoUsAddress(), actualOutput);
 }

 @Test
 public void testExecute_AddressWithMultipleCountryNames_ReturnsUsAddressWithFirstCountryName() {
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(new AddressDetails.Country());
 List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
 countryNames.add(new AddressDetails.Country.CountryNameCode());
 countryNames.add(new AddressDetails.Country.CountryNameCode());
 countryNames.add(new AddressDetails.Country.CountryNameCode());
 countryNames.set(0, new AddressDetails.Country.CountryNameCode().withContent("USA"));
 addressDetails.setCountryNameCode(countryNames);
 inputAddress.setAddressDetails(addressDetails);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals("USA", actualOutput.getCountry());
 }

 @Test
 public void testExecute_AddressWithMultipleAdministrativeAreas_ReturnsUsAddressWithFirstState() {
 AdministrativeArea adminArea1 = new AdministrativeArea();
 adminArea1.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
 adminArea1.getAdministrativeAreaName().setContent("State1");
 AdministrativeArea adminArea2 = new AdministrativeArea();
 adminArea2.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
 adminArea2.getAdministrativeAreaName().setContent("State2");
 List<AdministrativeArea> administrativeAreas = new ArrayList<>();
 administrativeAreas.add(adminArea1);
 administrativeAreas.add(adminArea2);
 inputAddress.setAdministrativeArea(administrativeAreas);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals("State1", actualOutput.getState());
 }

 @Test
 public void testExecute_AddressWithMultipleSubAdministrativeAreas_ReturnsUsAddressWithFirstCounty() {
 AdministrativeArea adminArea = new AdministrativeArea();
 adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
 adminArea.getAdministrativeAreaName().setContent("State");
 AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
 subAdminArea.setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeAreaName());
 subAdminArea.getSubAdministrativeAreaName().setContent("County1");
 List<AdministrativeArea.SubAdministrativeArea> subAdministrativeAreas = new ArrayList<>();
 subAdministrativeAreas.add(subAdminArea);
 adminArea.setSubAdministrativeArea(subAdministrativeAreas);
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName());
 locality.getLocalityName().setContent("City");
 inputAddress.setAdministrativeArea(Arrays.asList(adminArea));
 inputAddress.setLocality(locality);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals("County1", actualOutput.getCounty());
 }

 @Test
 public void testExecute_AddressWithMultipleLocalityNames_ReturnsUsAddressWithFirstCity() {
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName());
 locality.getLocalityName().setContent("City1");
 List<Locality.LocalityName> localityNames = new ArrayList<>();
 localityNames.add(new Locality.LocalityName());
 localityNames.add(new Locality.LocalityName());
 localityNames.add(new Locality.LocalityName());
 localityNames.set(0, localityName);
 locality.setLocalityName(localityNames);
 inputAddress.setLocality(locality);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals("City1", actualOutput.getCity());
 }

 @Test
 public void testExecute_AddressWithMultipleThoroughfareNames_ReturnsUsAddressWithFirstAddressLine1() {
 ThoroughfareNameType thoroughfareName1 = new ThoroughfareNameType();
 thoroughfareName1.setContent("Street1");
 ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
 thoroughfareName2.setContent("Street2");
 List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
 thoroughfareNames.add(thoroughfareName1);
 thoroughfareNames.add(thoroughfareName2);
 Locality locality = new Locality();
 locality.setThoroughfare(new Thoroughfare());
 locality.getThoroughfare().setThoroughfareName(thoroughfareNames);
 inputAddress.setLocality(locality);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals("Street1", actualOutput.getAddressLine1());
 }

 @Test
 public void testExecute_AddressWithMultiplePostalCodeNumbers_ReturnsUsAddressWithFirstPostalCode() {
 PostalCode postalCode = new PostalCode();
 List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
 postalCodeNumbers.add(new PostalCode.PostalCodeNumber());
 postalCodeNumbers.add(new PostalCode.PostalCodeNumber());
 postalCodeNumbers.set(0, new PostalCode.PostalCodeNumber().withContent("12345"));
 postalCode.setPostalCodeNumber(postalCodeNumbers);
 Locality locality = new Locality();
 locality.setPostalCode(postalCode);
 inputAddress.setLocality(locality);
 GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals("12345", actualOutput.getPostalCode());
 }

 private void InputAddressSetup() {
 inputAddress = new GeoAddress();
 inputAddress.setAccuracy(1.0d);
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(new AddressDetails.Country());
 addressDetails.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode().withContent("USA"));
 AddressDetails.Country country = addressDetails.getCountry();
 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().withContent("State"));
 administrativeArea.setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName().withContent("City"));
 locality.setPostalCode(new PostalCode().withPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().withContent("12345"))));
 locality.setThoroughfare(new Thoroughfare().withThoroughfareName(Arrays.asList(new ThoroughfareNameType().withContent("Street"))));
 addressDetails.setAdministrativeArea(Collections.singletonList(administrativeArea));
 addressDetails.setLocality(locality);
 inputAddress.setAddressDetails(addressDetails);
 }

}