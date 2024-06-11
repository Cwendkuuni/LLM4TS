package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.list.ArrayListList;
import org.junit.Test;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  address.getAddressDetails().setCountry(new AddressDetails.Country());
  address.getAddressDetails().getCountry().setCountryNameCode(new ArrayListList<AddressDetails.Country.CountryNameCode>());
  address.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
  address.getAddressDetails().getAdministrativeArea(0).setAdministrativeAreaName(new ArrayListList<AdministrativeArea.AdministrativeAreaName>());
  address.getAddressDetails().getAdministrativeArea(0).getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().content("California"));
  address.getAddressDetails().getAdministrativeArea(0).getSubAdministrativeArea(0).setSubAdministrativeAreaName(new ArrayListList<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName>());
  address.getAddressDetails().getAdministrativeArea(0).getSubAdministrativeArea(0).getSubadministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco"));
  address.getAddressDetails().getAdministrativeArea(0).getLocality().setLocalityName(new ArrayListList<Locality.LocalityName>());
  address.getAddressDetails().getAdministrativeArea(0).getLocality().getLocalityName().add(new Locality.LocalityName().content("San Francisco"));
  address.getAddressDetails().getAdministrativeArea(0).getLocality().setPostalCode(new PostalCode());
  address.getAddressDetails().getAdministrativeArea(0).getLocality().getPostalCode().setPostalCodeNumber(new ArrayListList<PostalCode.PostalCodeNumber>());
  address.getAddressDetails().getAdministrativeArea(0).getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().content("94111"));
  address.getAddressDetails().getAdministrativeArea(0).getLocality().setThoroughfare(new ThoroughfareType());
  address.getAddressDetails().getAdministrativeArea(0).getLocality().getThoroughfare().setThoroughfareName(new ArrayListList<ThoroughfareNameType>());
  address.getAddressDetails().getAdministrativeArea(0).getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Market Street"));

  GeoUsAddress usAddress = functor.execute(address);

  assertEquals("United States", usAddress.getCountry());
  assertEquals("California", usAddress.getState());
  assertEquals("San Francisco", usAddress.getCity());
  assertEquals("94111", usAddress.getPostalCode());
  assertEquals("Market Street", usAddress.getAddressLine1());
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsException() {
  GeoAddress address = null;
  functor.execute(address);
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithoutCountry_ThrowsException() {
  GeoAddress address = new GeoAddress();
  GeoUsAddress usAddress = functor.execute(address);
  fail("Exception expected");
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithoutState_ThrowsException() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  address.getAddressDetails().setCountry(new AddressDetails.Country());
  address.getAddressDetails().getCountry().setCountryNameCode(new ArrayListList<AddressDetails.Country.CountryNameCode>());
  address.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
  GeoUsAddress usAddress = functor.execute(address);
  fail("Exception expected");
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithoutCity_ThrowsException() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  address.getAddressDetails().setCountry(new AddressDetails.Country());
  address.getAddressDetails().getCountry().setCountryNameCode(new ArrayListList<AddressDetails.Country.CountryNameCode>());
  address.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
  address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
  address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayListList<AdministrativeArea.AdministrativeAreaName>());
  address.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().content("California"));
  GeoUsAddress usAddress = functor.execute(address);
  fail("Exception expected");
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithoutPostalCode_ThrowsException() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  address.getAddressDetails().setCountry(new AddressDetails.Country());
  address.getAddressDetails().getCountry().setCountryNameCode(new ArrayListList<AddressDetails.Country.CountryNameCode>());
  address.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
  address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
  address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayListList<AdministrativeArea.AdministrativeAreaName>());
  address.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().content("California"));
  address.getAddressDetails().setLocality(new Locality());
  address.getAddressDetails().getLocality().setLocalityName(new ArrayListList<Locality.LocalityName>());
  address.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName().content("San Francisco"));
  GeoUsAddress usAddress = functor.execute(address);
  fail("Exception expected");
 }

 @Test
 public void testExecute_AddressWithoutAddressLine1_ReturnsEmptyAddressLine1() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  address.getAddressDetails().setCountry(new AddressDetails.Country());
  address.getAddressDetails().getCountry().setCountryNameCode(new ArrayListList<AddressDetails.Country.CountryNameCode>());
  address.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
  address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
  address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayListList<AdministrativeArea.AdministrativeAreaName>());
  address.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().content("California"));
  address.getAddressDetails().setLocality(new Locality());
  address.getAddressDetails().getLocality().setLocalityName(new ArrayListList<Locality.LocalityName>());
  address.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName().content("San Francisco"));
  address.getAddressDetails().getLocality().setPostalCode(new PostalCode());
  address.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(new ArrayListList<PostalCode.PostalCodeNumber>());
  address.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().content("94111"));
  GeoUsAddress usAddress = functor.execute(address);

  assertNull(usAddress.getAddressLine1());
 }

 @Test
 public void testExecute_AddressWithMultipleAddressLines_ReturnsBothLines() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  address.getAddressDetails().setCountry(new AddressDetails.Country());
  address.getAddressDetails().getCountry().setCountryNameCode(new ArrayListList<AddressDetails.Country.CountryNameCode>());
  address.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("US"));
  address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
  address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayListList<AdministrativeArea.AdministrativeAreaName>());
  address.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().content("California"));
  address.getAddressDetails().setLocality(new Locality());
  address.getAddressDetails().getLocality().setLocalityName(new ArrayListList<Locality.LocalityName>());
  address.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName().content("San Francisco"));
  address.getAddressDetails().getLocality().setPostalCode(new PostalCode());
  address.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(new ArrayListList<PostalCode.PostalCodeNumber>());
  address.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().content("94111"));
  address.getAddressDetails().getLocality().setThoroughfare(new ThoroughfareType());
  address.getAddressDetails().getLocality().getThoroughfare().setThoroughfareName(new ArrayListList<ThoroughfareNameType>());
  address.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Market Street"));
  address.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Main Street"));

  GeoUsAddress usAddress = functor.execute(address);

  assertEquals("Market Street", usAddress.getAddressLine1());
  assertEquals("Main Street", usAddress.getAddressLine2());
 }

}