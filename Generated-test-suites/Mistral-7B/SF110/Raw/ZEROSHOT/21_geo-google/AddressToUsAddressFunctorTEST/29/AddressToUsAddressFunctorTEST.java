package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.AddressToUsAddressFunctorTest;

public class AddressToUsAddressFunctorTest {

 private GeoAddress testAddress;
 private GeoUsAddress expectedUsAddress;

 @Before
 public void setup() {
 testAddress = new GeoAddress();
 testAddress.setAddressDetails(new AddressDetails());
 testAddress.getAddressDetails().setCountry(new AddressDetails.Country());
 testAddress.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>());
 testAddress.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
 testAddress.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
 testAddress.getAddressDetails().getCountry().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
 testAddress.getAddressDetails().getCountry().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().content("California"));
 testAddress.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().setSubAdministrativeAreaName(new ArrayList<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName>());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco"));
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().setLocality(new Locality());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName().content("The City"));
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().content("94111"));
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<ThoroughfareNameType>());
 testAddress.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Market Street"));
 testAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 testAddress.setAccuracy(10.0);

 expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCity("The City");
 expectedUsAddress.setCounty("San Francisco");
 expectedUsAddress.setState("California");
 expectedUsAddress.setCountry("US");
 expectedUsAddress.setPostalCode("94111");
 expectedUsAddress.setAddressLine1("Market Street");
 expectedUsAddress.setAddressLine2("");
 expectedUsAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 expectedUsAddress.setAccuracy(10.0);
 }

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
 GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(testAddress);
 assertEquals(expectedUsAddress, result);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
 AddressToUsAddressFunctor.getInstance().execute(null);
 }

 @Test
 public void testExecute_AddressWithNoCountry_ReturnsEmptyUsAddress() {
 testAddress.getAddressDetails().setCountry(null);
 GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(testAddress);
 assertEquals(new GeoUsAddress(), result);
 }

 @Test
 public void testExecute_AddressWithInvalidCountryCode_ReturnsEmptyUsAddress() {
 testAddress.getAddressDetails().getCountry().getCountryNameCode().clear();
 testAddress.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("XX"));
 GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(testAddress);
 assertEquals(new GeoUsAddress(), result);
 }

 @Test
 public void testExecute_AddressWithMissingComponents_ReturnsPartialUsAddress() {
 testAddress.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(null);
 GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(testAddress);
 assertEquals(new GeoUsAddress("US", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, null, null, null, null, null, null, null, null, new Coordinate(37.7749, -122.4194), 10.0), result);
 }
}