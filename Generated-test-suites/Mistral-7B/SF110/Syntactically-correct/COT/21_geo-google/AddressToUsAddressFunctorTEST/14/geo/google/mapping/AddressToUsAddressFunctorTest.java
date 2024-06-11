package geo.google.mapping.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

 private GeoAddress createTestAddress() {
 GeoAddress addr = new GeoAddress();
 addr.setAddressDetails(new AddressDetails());
 AddressDetails country = new AddressDetails.Country();
 country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>());
 country.setCountry(new Country());
 country.getCountry().setName("USA");
 addr.setAddressDetails(country);
 return addr;
 }

 @Test
 public void testNullAddress() {
 assertThrows(NullPointerException.class, () -> AddressToUsAddressFunctor.getInstance().execute(null));
 }

 @Test
 public void testEmptyAddress() {
 GeoAddress addr = createTestAddress();
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertNull(usAddr.getCountry());
 assertNull(usAddr.getState());
 assertNull(usAddr.getCounty());
 assertNull(usAddr.getCity());
 assertNull(usAddr.getPostalCode());
 assertNull(usAddr.getAddressLine1());
 assertNull(usAddr.getAddressLine2());
 assertNull(usAddr.getCoordinate());
 assertNull(usAddr.getAccuracy());
 }

 @Test
 public void testUSAddressWithCountryName() {
 GeoAddress addr = createTestAddress();
 addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().setContent("US"));
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertEquals("USA", usAddr.getCountry());
 }

 @Test
 public void testUSAddressWithState() {
 GeoAddress addr = createTestAddress();
 AddressDetails country = addr.getAddressDetails();
 country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>());
 country.setCountry(new Country());
 country.getCountry().setName("USA");
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
 country.getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().setContent("NY"));
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertEquals("NY", usAddr.getState());
 }

 @Test
 public void testUSAddressWithCounty() {
 GeoAddress addr = createTestAddress();
 addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().setContent("US"));
 addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
 addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().setContent("NY"));
 addr.getAddressDetails().getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
 addr.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new ArrayList<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName>());
 addr.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().setContent("New York County"));
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertEquals("New York County", usAddr.getCounty());
 }

 @Test
 public void testUSAddressWithCity() {
 GeoAddress addr = createTestAddress();
 addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().setContent("US"));
 addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
 addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().setContent("NY"));
 addr.getAddressDetails().getAdministrativeArea().setLocality(new Locality());
 addr.getAddressDetails().getAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName().setContent("New York"));
 addr.getAddressDetails().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().setContent("10001"));
 addr.getAddressDetails().getAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<ThoroughfareNameType>());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().setContent("Broadway"));
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertEquals("New York", usAddr.getCity());
 assertEquals("10001", usAddr.getPostalCode());
 assertEquals("Broadway", usAddr.getAddressLine1());
 }

 @Test
 public void testUSAddressWithMultipleThoroughfares() {
 GeoAddress addr = createTestAddress();
 addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().setContent("US"));
 addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
 addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().setContent("NY"));
 addr.getAddressDetails().getAdministrativeArea().setLocality(new Locality());
 addr.getAddressDetails().getAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName().setContent("New York"));
 addr.getAddressDetails().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().setContent("10001"));
 addr.getAddressDetails().getAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<ThoroughfareNameType>());
 addr.getAddressDetails().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().setContent("Broadway"));
 addr.getAddressDetails().getAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().setContent("5th Avenue"));
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertEquals("New York", usAddr.getCity());
 assertEquals("10001", usAddr.getPostalCode());
 assertEquals("Broadway", usAddr.getAddressLine1());
 assertEquals("5th Avenue", usAddr.getAddressLine2());
 }

 @Test
 public void testUSAddressWithCoordinate() {
 GeoAddress addr = createTestAddress();
 addr.setCoordinate(new Coordinate(1.0, 2.0, 3.0));
 GeoUsAddress usAddr = AddressToUsAddressFunctor.getInstance().execute(addr);
 assertEquals(1.0, usAddr.getCoordinate().getLatitude(), 0.001);
 assertEquals(2.0, usAddr.getCoordinate().getLongitude(), 0.001);
 assertEquals(3.0, usAddr.getCoordinate().getAltitude(), 0.001);
 }

}