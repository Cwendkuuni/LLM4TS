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
 inputAddress.getAddressDetails().setCountry(new AddressDetails.Country());
 inputAddress.getAddressDetails().getCountry().setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 inputAddress.getAddressDetails().getCountry().getCountryNameCode().get(0).setContent("USA");
 inputAddress.getAddressDetails().getAdministrativeArea(0).setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getAdministrativeAreaName().get(0).setContent("New York");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).setLocalityName(Collections.singletonList(new Locality.LocalityName()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getLocalityName().get(0).setContent("New York City");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).setPostalCode(new PostalCode());
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getPostalCode().setPostalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getPostalCode().getPostalCodeNumber().get(0).setContent("10001");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getThoroughfare().setThoroughfareName(Collections.singletonList(new ThoroughfareNameType()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getThoroughfare().getThoroughfareName().get(0).setContent("Broadway");

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertEquals("USA", outputAddress.getCountry());
 assertEquals("New York", outputAddress.getState());
 assertEquals("New York City", outputAddress.getCity());
 assertEquals("10001", outputAddress.getPostalCode());
 assertEquals("Broadway", outputAddress.getAddressLine1());
 assertNull(outputAddress.getAddressLine2());
 assertNotNull(outputAddress.getCoordinate());
 assertEquals(addr -> addr.getAccuracy() != null, outputAddress.getAccuracy());
 }

 @Test
 public void testExecute_NullAddress_ThrowsNullPointerException() {
 assertThrows(NullPointerException.class, () -> functor.execute(null));
 }

 @Test
 public void testExecute_NullAddressDetails_ThrowsNullPointerException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(null);

 assertThrows(NullPointerException.class, () -> functor.execute(inputAddress));
 }

 @Test
 public void testExecute_EmptyCountryNameCodeList_ReturnsEmptyUsAddress() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 inputAddress.getAddressDetails().setCountry(new AddressDetails.Country());
 inputAddress.getAddressDetails().getCountry().setCountryNameCode(Collections.emptyList());

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertNull(outputAddress.getCountry());
 assertNull(outputAddress.getState());
 assertNull(outputAddress.getCity());
 assertNull(outputAddress.getPostalCode());
 assertNull(outputAddress.getAddressLine1());
 assertNull(outputAddress.getAddressLine2());
 assertNull(outputAddress.getCoordinate());
 assertNull(outputAddress.getAccuracy());
 }

 @Test
 public void testExecute_NullCoordinate_ReturnsUsAddressWithoutCoordinate() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 inputAddress.getAddressDetails().setCountry(new AddressDetails.Country());
 inputAddress.getAddressDetails().getCountry().setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 inputAddress.getAddressDetails().getCountry().getCountryNameCode().get(0).setContent("USA");
 inputAddress.getAddressDetails().getAdministrativeArea(0).setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getAdministrativeAreaName().get(0).setContent("New York");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).setLocalityName(Collections.singletonList(new Locality.LocalityName()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getLocalityName().get(0).setContent("New York City");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).setPostalCode(new PostalCode());
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getPostalCode().setPostalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getPostalCode().getPostalCodeNumber().get(0).setContent("10001");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getThoroughfare().setThoroughfareName(Collections.singletonList(new ThoroughfareNameType()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getThoroughfare().getThoroughfareName().get(0).setContent("Broadway");
 inputAddress.setCoordinate(null);

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertNull(outputAddress.getCoordinate());
 }

 @Test
 public void testExecute_NullPostalCodeNumberList_ReturnsUsAddressWithoutPostalCode() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 inputAddress.getAddressDetails().setCountry(new AddressDetails.Country());
 inputAddress.getAddressDetails().getCountry().setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 inputAddress.getAddressDetails().getCountry().getCountryNameCode().get(0).setContent("USA");
 inputAddress.getAddressDetails().getAdministrativeArea(0).setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getAdministrativeAreaName().get(0).setContent("New York");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).setLocalityName(Collections.singletonList(new Locality.LocalityName()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getLocalityName().get(0).setContent("New York City");
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).setPostalCode(new PostalCode());
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getPostalCode().setPostalCodeNumber(null);
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getThoroughfare().setThoroughfareName(Collections.singletonList(new ThoroughfareNameType()));
 inputAddress.getAddressDetails().getAdministrativeArea(0).getLocality(0).getThoroughfare().getThoroughfareName().get(0).setContent("Broadway");

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertNull(outputAddress.getPostalCode());
 }

}