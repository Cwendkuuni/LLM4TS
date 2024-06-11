package geo.google.mapping;

import static org.junit.Assert.*;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import geo.google.datamodel.*;
import java.util.*;

@RunWith(Parameterized.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress inputAddress;
 private GeoUsAddress expectedOutput;

 public AddressToUsAddressFunctorTest(GeoAddress inputAddress, GeoUsAddress expectedOutput) {
 this.inputAddress = inputAddress;
 this.expectedOutput = expectedOutput;
 }

 @Parameters(name = "{index}: inputAddress={0}, expectedOutput={1}")
 public static Collection<Object[]> testData() {
 return Arrays.asList(new Object[][]{
 // Test case 1
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setCountry(new Country()
 .setCountryNameCode(new CountryNameCode("US")))
 .setAdministrativeArea(new AdministrativeArea()
 .setAdministrativeAreaName(new AdministrativeAreaName("New York")))
 .setSubAdministrativeArea(new SubAdministrativeArea()
 .setSubAdministrativeAreaName(new SubAdministrativeAreaName("New York City")))
 .setLocality(new Locality()
 .setLocalityName(new LocalityName("Manhattan")))
 .setPostalCode(new PostalCode()
 .setPostalCodeNumber(new PostalCodeNumber("10001")))
 .setThoroughfare(new Thoroughfare()
 .setThoroughfareName(new ThoroughfareNameType("Broadway")))
 ),
 new GeoUsAddress()
 .setCountry("United States")
 .setState("New York")
 .setCity("Manhattan")
 .setPostalCode("10001")
 .setAddressLine1("Broadway")
 },

 // Test case 2 (missing country)
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setAdministrativeArea(new AdministrativeArea()
 .setAdministrativeAreaName(new AdministrativeAreaName("New York")))
 .setLocality(new Locality()
 .setLocalityName(new LocalityName("Manhattan")))
 .setPostalCode(new PostalCode()
 .setPostalCodeNumber(new PostalCodeNumber("10001")))
 ),
 null
 },

 // Test case 3 (missing administrative area)
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setCountry(new Country()
 .setCountryNameCode(new CountryNameCode("US")))
 .setLocality(new Locality()
 .setLocalityName(new LocalityName("Manhattan")))
 ),
 null
 },

 // Test case 4 (missing locality)
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setCountry(new Country()
 .setCountryNameCode(new CountryNameCode("US")))
 .setAdministrativeArea(new AdministrativeArea()
 .setAdministrativeAreaName(new AdministrativeAreaName("New York")))
 ),
 null
 },

 // Test case 5 (missing postal code)
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setCountry(new Country()
 .setCountryNameCode(new CountryNameCode("US")))
 .setAdministrativeArea(new AdministrativeArea()
 .setAdministrativeAreaName(new AdministrativeAreaName("New York")))
 .setLocality(new Locality()
 .setLocalityName(new LocalityName("Manhattan")))
 ),
 new GeoUsAddress()
 },

 // Test case 6 (missing address line)
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setCountry(new Country()
 .setCountryNameCode(new CountryNameCode("US")))
 .setAdministrativeArea(new AdministrativeArea()
 .setAdministrativeAreaName(new AdministrativeAreaName("New York")))
 .setLocality(new Locality()
 .setLocalityName(new LocalityName("Manhattan")))
 .setPostalCode(new PostalCode()
 .setPostalCodeNumber(new PostalCodeNumber("10001")))
 ),
 new GeoUsAddress()
 .setCountry("United States")
 .setState("New York")
 .setCity("Manhattan")
 .setPostalCode("10001")
 },

 // Test case 7 (missing coordinate)
 new Object[] {
 new GeoAddress()
 .setAddressDetails(new AddressDetails()
 .setCountry(new Country()
 .setCountryNameCode(new CountryNameCode("US")))
 .setAdministrativeArea(new AdministrativeArea()
 .setAdministrativeAreaName(new AdministrativeAreaName("New York")))
 .setLocality(new Locality()
 .setLocalityName(new LocalityName("Manhattan")))
 .setPostalCode(new PostalCode()
 .setPostalCodeNumber(new PostalCodeNumber("10001")))
 ),
 new GeoUsAddress()
 .setCountry("United States")
 .setState("New York")
 .setCity("Manhattan")
 .setPostalCode("10001")
 },

 // Test case 8 (empty input)
 new Object[] {
 null,
 null
 }
 });
 }

 @Test
 public void testExecute() {
 GeoUsAddress output = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
 assertEquals(expectedOutput, output);
 }
}