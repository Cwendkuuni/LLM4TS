package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor;
 private GeoAddress geoAddress;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 functor = AddressToUsAddressFunctor.getInstance();
 geoAddress = mock(GeoAddress.class);
 }

 @Test
 public void testExecute_whenAddressDetailsIsNull_thenUsAddressIsNull() {
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertNull(usAddress);
 }

 @Test
 public void testExecute_whenCountryIsPresent_thenUsAddressHasCountry() {
 String countryName = "United States";
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(new AddressDetails.Country().setCountryNameCode(new AddressDetails.Country.CountryNameCode().setContent(countryName)));
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(countryName, usAddress.getCountry());
 }

 @Test
 public void testExecute_whenAdministrativeAreaIsPresent_thenUsAddressHasState() {
 String stateName = "California";
 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().setContent(stateName));
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setAdministrativeArea(administrativeArea);
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(stateName, usAddress.getState());
 }

 @Test
 public void testExecute_whenSubAdministrativeAreaIsPresent_thenUsAddressHasCounty() {
 String countyName = "Los Angeles";
 AdministrativeArea administrativeArea = new AdministrativeArea();
 AdministrativeArea.SubAdministrativeArea subAdministrativeArea = new AdministrativeArea.SubAdministrativeArea();
 subAdministrativeArea.setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().setContent(countyName));
 administrativeArea.setSubAdministrativeArea(subAdministrativeArea);
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setAdministrativeArea(administrativeArea);
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(countyName, usAddress.getCounty());
 }

 @Test
 public void testExecute_whenLocalityIsPresent_thenUsAddressHasCity() {
 String cityName = "Los Angeles";
 Locality locality = new Locality();
 locality.setLocalityName(new Locality.LocalityName().setContent(cityName));
 AdministrativeArea administrativeArea = new AdministrativeArea();
 addressDetails.setAdministrativeArea(administrativeArea);
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(locality.getLocalityName()).thenReturn(locality.getLocalityName());
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(cityName, usAddress.getCity());
 }

 @Test
 public void testExecute_whenPostalCodeIsPresent_thenUsAddressHasPostalCode() {
 String postalCode = "90001";
 PostalCode postalCodeObj = new PostalCode();
 postalCodeObj.setPostalCodeNumber(new PostalCode.PostalCodeNumber().setContent(postalCode));
 Locality locality = new Locality();
 locality.setPostalCode(postalCodeObj);
 AdministrativeArea administrativeArea = new AdministrativeArea();
 addressDetails.setAdministrativeArea(administrativeArea);
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(locality.getPostalCode()).thenReturn(postalCodeObj);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(postalCode, usAddress.getPostalCode());
 }

 @Test
 public void testExecute_whenThoroughfareIsPresent_thenUsAddressHasAddressLine1() {
 String thoroughfareName = "Main St";
 ThoroughfareNameType thoroughfareNameType = new ThoroughfareNameType();
 thoroughfareNameType.setContent(thoroughfareName);
 List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
 thoroughfares.add(thoroughfareNameType);
when(locality.getThoroughfare().getThoroughfareName()).thenReturn(thoroughfares);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(thoroughfareName, usAddress.getAddressLine1());
 }

 @Test
 public void testExecute_whenThoroughfareHasTwoLines_thenUsAddressHasAddressLine1AndAddressLine2() {
 String thoroughfareLine1 = "Main St";
 String thoroughfareLine2 = "123";
 ThoroughfareNameType thoroughfareNameType1 = new ThoroughfareNameType();
 thoroughfareNameType1.setContent(thoroughfareLine1);
 ThoroughfareNameType thoroughfareNameType2 = new ThoroughfareNameType();
 thoroughfareNameType2.setContent(thoroughfareLine2);
 List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
 thoroughfares.add(thoroughfareNameType1);
 thoroughfares.add(thoroughfareNameType2);
 when(locality.getThoroughfare().getThoroughfareName()).thenReturn(thoroughfares);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 assertEquals(thoroughfareLine1, usAddress.getAddressLine1());
 assertEquals(thoroughfareLine2, usAddress.getAddressLine2());
 }

 @Test
 public void testExecute_whenCoordinateIsPresent_thenUsAddressHasCoordinate() {
 GeoCoordinate coordinate = new GeoCoordinate(12.345, 67.890);
 when(geoAddress.getCoordinate()).thenReturn(coordinate);
 GeoUsAddress usAddress = functor.execute(geoAddress);
 GeoCoordinate usAddressCoordinate = usAddress.getCoordinate();
 assertEquals(coordinate.getLatitude(), usAddressCoordinate.getLatitude(), 0.001);
 assertEquals(coordinate.getLongitude(), usAddressCoordinate.getLongitude(), 0.001);
 }
}