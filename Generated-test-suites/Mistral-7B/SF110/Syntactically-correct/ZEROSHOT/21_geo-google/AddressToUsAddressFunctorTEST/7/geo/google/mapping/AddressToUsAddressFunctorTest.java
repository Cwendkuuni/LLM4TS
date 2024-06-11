import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor;
 private GeoAddress address;

 @Before
 public void setUp() {
 functor = AddressToUsAddressFunctor.getInstance();
 address = new GeoAddress();
 }

 @Test
 public void testExecuteWithValidAddressShouldPopulateUsAddress() {
 // given
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName().content("California")));
 country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco")));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(Collections.singletonList(new Locality.LocalityName().content("The City")));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode().postalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber().content("12345"))));
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
 ThoroughfareNameType thoroughfareName1 = new ThoroughfareNameType();
 thoroughfareName1.setContent("Main Street");
 ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
 thoroughfareName2.setContent("Elm Street");
 List<ThoroughfareNameType> thoroughfares = Arrays.asList(thoroughfareName1, thoroughfareName2);
 country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(thoroughfares);
 address.setAddressDetails(addressDetails);
 address.setCoordinate(new Coordinate(12.0, 34.0));
 address.setAccuracy(5.0);

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertEquals("United States", usAddress.getCountry());
 assertEquals("California", usAddress.getState());
 assertEquals("San Francisco", usAddress.getCounty());
 assertEquals("The City", usAddress.getCity());
 assertEquals("12345", usAddress.getPostalCode());
 assertEquals("Main Street", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertEquals(12.0, usAddress.getCoordinate().getLatitude(), 0.0);
 assertEquals(34.0, usAddress.getCoordinate().getLongitude(), 0.0);
 assertEquals(5.0, usAddress.getAccuracy(), 0.0);
 }

 @Test
 public void testExecuteWithInvalidAddressShouldReturnEmptyUsAddress() {
 // given
 address.setAddressDetails(null);

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutCountryShouldReturnEmptyUsAddress() {
 // given
 address.setAddressDetails(new AddressDetails());

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutCountryNameCodeShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.emptyList());
 address.setAddressDetails(new AddressDetails().country(country));

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutAdministrativeAreaShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 address.setAddressDetails(new AddressDetails().country(country));

 // when
 GeoUsAddress usAddress = functor.execute(address);
 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutStateShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 AdministrativeArea adminArea = new AdministrativeArea();
 address.setAddressDetails(new AddressDetails().country(country).administrativeArea(adminArea));

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutCityShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 AdministrativeArea adminArea = new AdministrativeArea();
 Locality locality = new Locality();
 address.setAddressDetails(new AddressDetails().country(country).administrativeArea(adminArea).locality(locality));

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutPostalCodeShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 AdministrativeArea adminArea = new AdministrativeArea();
 Locality locality = new Locality();
 PostalCode postalCode = new PostalCode();
 address.setAddressDetails(new AddressDetails().country(country).administrativeArea(adminArea).locality(locality).postalCode(postalCode));

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutThoroughfareShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 AdministrativeArea adminArea = new AdministrativeArea();
 Locality locality = new Locality();
 Thoroughfare thoroughfare = new Thoroughfare();
 address.setAddressDetails(new AddressDetails().country(country).administrativeArea(adminArea).locality(locality).thoroughfare(thoroughfare));

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithAddressWithoutLatitudeAndLongitudeShouldReturnEmptyUsAddress() {
 // given
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new Country.CountryNameCode().content("US")));
 AdministrativeArea adminArea = new AdministrativeArea();
 Locality locality = new Locality();
 Thoroughfare thoroughfare = new Thoroughfare();
 GeoCoordinate coordinate = new GeoCoordinate();
 address.setAddressDetails(new AddressDetails().country(country).administrativeArea(adminArea).locality(locality).thoroughfare(thoroughfare).coordinate(coordinate));

 // when
 GeoUsAddress usAddress = functor.execute(address);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithNullAddressShouldReturnEmptyUsAddress() {
 // given

 // when
 GeoUsAddress usAddress = functor.execute(null);

 // then
 assertNull(usAddress);
 }

 @Test
 public void testExecuteWithNullFunctorShouldThrowNullPointerException() {
 // given
 GeoAddress address = Mockito.mock(GeoAddress.class);

 // when, then
 try {
 AddressToUsAddressFunctor functor = null;
 GeoUsAddress usAddress = functor.execute(address);
 } catch (NullPointerException e) {
 // passed
 }
 }
}