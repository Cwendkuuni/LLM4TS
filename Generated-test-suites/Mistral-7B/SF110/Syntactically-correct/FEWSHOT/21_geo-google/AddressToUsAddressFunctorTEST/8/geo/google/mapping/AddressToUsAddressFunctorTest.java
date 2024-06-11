package geo.google.mapping;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import geo.google.datamodel.*;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress getTestAddress() {
 final GeoAddress address = new GeoAddress();
 address.setAddressDetails(Mockito.spy(new AddressDetails()));
 return address;
 }

 @Test
 public void testNullAddress() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 assertNull(functor.execute(null));
 }

 @Test
 public void testEmptyAddress() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final GeoUsAddress ret = functor.execute(address);
 assertNull(ret.getCountry());
 assertNull(ret.getState());
 assertNull(ret.getCounty());
 assertNull(ret.getCity());
 assertNull(ret.getPostalCode());
 assertNull(ret.getAddressLine1());
 assertNull(ret.getAddressLine2());
 assertNull(ret.getCoordinate());
 assertNull(ret.getAccuracy());
 }

 @Test
 public void testValidAddressWithCountry() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 addressDetails.setCountry(country);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("US", ret.getCountry());
 }

 @Test
 public void testValidAddressWithState() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AdministrativeArea adminArea = Mockito.spy(new AdministrativeArea());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 final List<AdministrativeArea.AdministrativeAreaName> stateNames = Mockito.singletonList(new AdministrativeArea.AdministrativeAreaName());
 adminArea.setAdministrativeAreaName(stateNames);
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("CA", ret.getState());
 }

 @Test
 public void testValidAddressWithCounty() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AdministrativeArea adminArea = Mockito.spy(new AdministrativeArea());
 final SubAdministrativeArea subAdminArea = Mockito.spy(new SubAdministrativeArea());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 final List<AdministrativeArea.AdministrativeAreaName> stateNames = Mockito.singletonList(new AdministrativeArea.AdministrativeAreaName());
 adminArea.setAdministrativeAreaName(stateNames);
 final List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> countyNames = Mockito.singletonList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
 subAdminArea.setSubAdministrativeAreaName(countyNames);
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 addressDetails.setSubAdministrativeArea(subAdminArea);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("Los Angeles", ret.getCounty());
 }

 @Test
 public void testValidAddressWithCity() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AdministrativeArea adminArea = Mockito.spy(new AdministrativeArea());
 final Locality locality = Mockito.spy(new Locality());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 final List<AdministrativeArea.AdministrativeAreaName> stateNames = Mockito.singletonList(new AdministrativeArea.AdministrativeAreaName());
 adminArea.setAdministrativeAreaName(stateNames);
 final List<Locality.LocalityName> cityNames = Mockito.singletonList(new Locality.LocalityName());
 locality.setLocalityName(cityNames);
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 addressDetails.setLocality(locality);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("New York", ret.getCity());
 }

 @Test
 public void testValidAddressWithPostalCode() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AdministrativeArea adminArea = Mockito.spy(new AdministrativeArea());
 final Locality locality = Mockito.spy(new Locality());
 final PostalCode postalCode = Mockito.spy(new PostalCode());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 final List<AdministrativeArea.AdministrativeAreaName> stateNames = Mockito.singletonList(new AdministrativeArea.AdministrativeAreaName());
 adminArea.setAdministrativeAreaName(stateNames);
 final List<Locality.LocalityName> cityNames = Mockito.singletonList(new Locality.LocalityName());
 locality.setLocalityName(cityNames);
 final List<PostalCode.PostalCodeNumber> postalCodeNumbers = Mockito.singletonList(new PostalCode.PostalCodeNumber());
 postalCode.setPostalCodeNumber(postalCodeNumbers);
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 addressDetails.setLocality(locality);
 addressDetails.setPostalCode(postalCode);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("12345", ret.getPostalCode());
 }

 @Test
 public void testValidAddressWithAddressLine1() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AdministrativeArea adminArea = Mockito.spy(new AdministrativeArea());
 final Locality locality = Mockito.spy(new Locality());
 final ThoroughfareNameType addressLine1 = Mockito.spy(new ThoroughfareNameType());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 final List<AdministrativeArea.AdministrativeAreaName> stateNames = Mockito.singletonList(new AdministrativeArea.AdministrativeAreaName());
 adminArea.setAdministrativeAreaName(stateNames);
 final List<Locality.LocalityName> cityNames = Mockito.singletonList(new Locality.LocalityName());
 locality.setLocalityName(cityNames);
 final List<ThoroughfareNameType> thoroughfares = Mockito.singletonList(addressLine1);
 locality.setThoroughfare(new Thoroughfare(thoroughfares));
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 addressDetails.setLocality(locality);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("123 Main St", ret.getAddressLine1());
 }

 @Test
 public void testValidAddressWithAddressLine2() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final AddressCountry country = Mockito.spy(new AddressCountry());
 final AdministrativeArea adminArea = Mockito.spy(new AdministrativeArea());
 final Locality locality = Mockito.spy(new Locality());
 final ThoroughfareNameType addressLine1 = Mockito.spy(new ThoroughfareNameType());
 final ThoroughfareNameType addressLine2 = Mockito.spy(new ThoroughfareNameType());
 final AddressDetails addressDetails = Mockito.spy(new AddressDetails());
 final List<AddressDetails.Country.CountryNameCode> countryNames = Mockito.spy(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 country.setCountryNameCode(countryNames);
 final List<AdministrativeArea.AdministrativeAreaName> stateNames = Mockito.singletonList(new AdministrativeArea.AdministrativeAreaName());
 adminArea.setAdministrativeAreaName(stateNames);
 final List<Locality.LocalityName> cityNames = Mockito.singletonList(new Locality.LocalityName());
 locality.setLocalityName(cityNames);
 final List<ThoroughfareNameType> thoroughfares = Mockito.singletonList(addressLine1);
 locality.setThoroughfare(new Thoroughfare(thoroughfares));
 final List<ThoroughfareNameType> thoroughfares2 = Mockito.singletonList(addressLine2);
 locality.setThoroughfare(new Thoroughfare(thoroughfares2));
 addressDetails.setCountry(country);
 addressDetails.setAdministrativeArea(adminArea);
 addressDetails.setLocality(locality);
 address.setAddressDetails(addressDetails);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals("123 Main St", ret.getAddressLine1());
 assertEquals("456 Elm St", ret.getAddressLine2());
 }

 @Test
 public void testValidAddressWithCoordinate() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final GeoCoordinate coordinate = Mockito.spy(new GeoCoordinate());
 address.setCoordinate(coordinate);
 final GeoUsAddress ret = functor.execute(address);
 assertNotNull(ret.getCoordinate());
 }

 @Test
 public void testValidAddressWithAccuracy() {
 final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 final GeoAddress address = getTestAddress();
 final GeoAddressAccuracy accuracy = Mockito.spy(new GeoAddressAccuracy());
 address.setAccuracy(accuracy);
 final GeoUsAddress ret = functor.execute(address);
 assertEquals(accuracy.getAccuracyType(), ret.getAccuracy().getAccuracyType());
 }
}