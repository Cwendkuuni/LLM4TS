package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.ListUtils;
import org.junit.Test;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.AddressToUsAddressFunctor.GeoUsAddress;
import geo.google.mapping.AddressToUsAddressFunctor.GeoException;

public class AddressToUsAddressFunctorTest {

 private static final GeoAddress ADDRESS_WITH_COUNTRY = new GeoAddress();
 private static final GeoAddress ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA = new GeoAddress();
 private static final GeoAddress ADDRESS_WITH_COORDINATE = new GeoAddress();

 static {
 AddressWithCountryInitializer.init(ADDRESS_WITH_COUNTRY);
 AddressWithCountryAndAdminAreaInitializer.init(ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA);
 AddressWithCoordinateInitializer.init(ADDRESS_WITH_COORDINATE);
 }

 @Test
 public void testExecuteWithNullAddress() {
 try {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 functor.execute(null);
 fail("Should have thrown a NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testExecuteWithEmptyAddress() {
 try {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = functor.execute(new GeoAddress());
 assertEquals(new GeoUsAddress(), result);
 } catch (GeoException e) {
 fail("Should not have thrown a GeoException");
 }
 }

 @Test
 public void testExecuteWithAddressContainingCountry() {
 try {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = functor.execute(ADDRESS_WITH_COUNTRY);
 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("United States");
 expected.setState("New York");
 expected.setCity("Albany");
 expected.setPostalCode("12201");
 expected.setAddressLine1("Livingston Avenue");
 expected.setAddressLine2("123");
 expected.setCoordinate(ADDRESS_WITH_COUNTRY.getCoordinate().clone());
 expected.setAccuracy(ADDRESS_WITH_COUNTRY.getAccuracy());
 assertEquals(expected, result);
 } catch (GeoException e) {
 fail("Should not have thrown a GeoException");
 }
 }

 @Test
 public void testExecuteWithAddressContainingCountryAndAdminArea() {
 try {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = functor.execute(ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA);
 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("United States");
 expected.setState("New York");
 expected.setCounty("Albany County");
 expected.setCity("Albany");
 expected.setPostalCode("12201");
 expected.setAddressLine1("Main Street");
 expected.setAddressLine2("456");
 expected.setCoordinate(ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA.getCoordinate().clone());
 expected.setAccuracy(ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA.getAccuracy());
 assertEquals(expected, result);
 } catch (GeoException e) {
 fail("Should not have thrown a GeoException");
 }
 }

 @Test
 public void testExecuteWithAddressContainingCoordinate() {
 try {
 AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = functor.execute(ADDRESS_WITH_COORDINATE);
 GeoUsAddress expected = new GeoUsAddress();
 expected.setCountry("United States");
 expected.setAccuracy(ADDRESS_WITH_COORDINATE.getAccuracy());
 expected.setCoordinate(ADDRESS_WITH_COORDINATE.getCoordinate().clone());
 assertEquals(expected, result);
 } catch (GeoException e) {
 fail("Should not have thrown a GeoException");
 }
 }

}

class AddressWithCountryInitializer {
 static {
 ADDRESS_WITH_COUNTRY.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = ADDRESS_WITH_COUNTRY.getAddressDetails();
 Country country = new Country();
 country.setCountryNameCode(ListUtils.arrayToList(new AddressDetails.Country.CountryNameCode[1], "US"));
 country.setCountryName(ListUtils.arrayToList(new AddressDetails.Country.CountryName[1], "United States"));
 country.setAdministrativeArea(new AdministrativeArea());
 addressDetails.setCountry(country);
 }
}

class AddressWithCountryAndAdminAreaInitializer {
 static {
 ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = ADDRESS_WITH_COUNTRY_AND_ADMIN_AREA.getAddressDetails();
 Country country = new Country();
 country.setCountryNameCode(ListUtils.arrayToList(new AddressDetails.Country.CountryNameCode[1], "US"));
 country.setCountryName(ListUtils.arrayToList(new AddressDetails.Country.CountryName[1], "United States"));
 country.setAdministrativeArea(new AdministrativeArea());
 country.getAdministrativeArea().setAdministrativeAreaName(ListUtils.arrayToList(new AdministrativeArea.AdministrativeAreaName[1], "New York"));
 country.getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
 addressDetails.setCountry(country);
 }
}

class AddressWithCoordinateInitializer {
 static {
 ADDRESS_WITH_COORDINATE.setAddressDetails(new AddressDetails());
 ADDRESS_WITH_COORDINATE.getAddressDetails().setCountry(new Country());
 ADDRESS_WITH_COORDINATE.setCoordinate(new Coordinate(42.603717, -73.756215));
 ADDRESS_WITH_COORDINATE.setAccuracy(10.0);
 }
}