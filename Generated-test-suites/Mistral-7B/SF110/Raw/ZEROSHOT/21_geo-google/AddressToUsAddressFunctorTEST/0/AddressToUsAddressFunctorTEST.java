package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.*;
import geo.google.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

public class AddressToUsAddressFunctorTest {

 private GeoAddress address;
 private GeoUsAddress usAddress;
 private static final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

 @Before
 public void setUp() {
 address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setCountry(new AddressDetails.Country() {{
 CountryNameCode countryNameCode = new CountryNameCode();
 countryNameCode.setContent("US");
 getCountryNameCode().add(countryNameCode);
 AdminArea adminArea = new AdminArea();
 adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName() {{
 setContent("California");
 }});
 getCountry().setAdministrativeArea(adminArea);
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName() {{
 setContent("San Francisco");
 }});
 locality.setPostalCode(new PostalCode() {{
 PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
 postalCodeNumber.setContent("94102");
 getPostalCodeNumber().add(postalCodeNumber);
 }});
 getCountry().setLocality(locality);
 Thoroughfare thoroughfare = new Thoroughfare();
 ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
 thoroughfareName.setContent("Market Street");
 getCountry().setThoroughfare(thoroughfare);
 getCountry().getThoroughfare().getThoroughfareName().add(thoroughfareName);
 }});
 usAddress = new GeoUsAddress();
 }

 @Test
 public void testExecute_ValidAddress_ShouldReturnUsAddress() {
 GeoUsAddress result = functor.execute(address);
 assertEquals("US", result.getCountry());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("94102", result.getPostalCode());
 assertEquals("Market Street", result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ShouldThrowGeoException() {
 GeoException exception = null;
 try {
 functor.execute(null);
 } catch (GeoException e) {
 exception = e;
 }
 assertNotNull(exception);
 }

 @Test
 public void testExecute_EmptyAddress_ShouldReturnEmptyUsAddress() {
 GeoUsAddress result = functor.execute(new GeoAddress());
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCoordinate());
 assertNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithoutCountry_ShouldReturnEmptyUsAddress() {
 address.getAddressDetails().setCountry(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCoordinate());
 assertNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithoutState_ShouldReturnUsAddressWithEmptyState() {
 address.getAddressDetails().getCountry().getAdministrativeArea().setAdministrativeAreaName(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getState());
 assertEquals("US", result.getCountry());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithoutCity_ShouldReturnUsAddressWithEmptyCity() {
 address.getAddressDetails().getLocality().setLocalityName(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCity());
 assertEquals("California", result.getState());
 assertEquals("US", result.getCountry());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithoutPostalCode_ShouldReturnUsAddressWithEmptyPostalCode() {
 address.getAddressDetails().getLocality().setPostalCode(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getPostalCode());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("US", result.getCountry());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithoutAddressLine1_ShouldReturnUsAddressWithEmptyAddressLine1() {
 address.getAddressDetails().getThoroughfare().getThoroughfareName().clear();
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getCountry());
 assertNull(result.getCoordinate());
 assertNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithMultipleThoroughfareNames_ShouldReturnUsAddressWithBothAddressLines() {
 address.getAddressDetails().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType() {{
 setContent("Mission Street");
 }});
 GeoUsAddress result = functor.execute(address);
 assertEquals("Market Street", result.getAddressLine1());
 assertEquals("Mission Street", result.getAddressLine2());
 }

 @Test
 public void testExecute_AddressWithoutCoordinates_ShouldReturnUsAddressWithNullCoordinate() {
 GeoCoordinate coordinate = address.getCoordinate();
 address.setCoordinate(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCoordinate());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("94102", result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCountry());
 assertNull(result.getAccuracy());
 // Restore original coordinate
 address.setCoordinate(coordinate);
 }

 @Test
 public void testExecute_AddressWithInvalidCountry_ShouldReturnEmptyUsAddress() {
 AddressDetails.Country country = new AddressDetails.Country();
 country.setCountryNameCode(new CountryNameCode() {{
 setContent("CA");
 }});
 address.setAddressDetails(country);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCoordinate());
 assertNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithInvalidState_ShouldReturnEmptyUsAddress() {
 address.getAddressDetails().getCountry().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName() {{
 setContent("Invalid State");
 }});
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getState());
 assertEquals("US", result.getCountry());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithInvalidCity_ShouldReturnEmptyUsAddress() {
 address.getAddressDetails().getLocality().setLocalityName(new Locality.LocalityName() {{
 setContent("Invalid City");
 }});
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCity());
 assertEquals("California", result.getState());
 assertEquals("US", result.getCountry());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }

 @Test
 public void testExecute_AddressWithInvalidPostalCode_ShouldReturnEmptyUsAddress() {
 address.getAddressDetails().getLocality().setPostalCode(new PostalCode() {{
 PostalCodeNumber postalCodeNumber = new PostalCodeNumber() {{
 setContent("A1 Z1");
 }};
 getPostalCodeNumber().add(postalCodeNumber);
 }});
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getPostalCode());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("US", result.getCountry());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNotNull(result.getCoordinate());
 assertNotNull(result.getAccuracy());
 }
}