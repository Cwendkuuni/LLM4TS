package geo.google.mapping;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.*;
import geo.google.*;

public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor;
 private GeoAddress inputAddress;
 private GeoUsAddress expectedOutput;

 @Before
 public void setup() {
 functor = AddressToUsAddressFunctor.getInstance();
 inputAddress = new GeoAddress();

 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(new ArrayList<Country.CountryNameCode>() {{
 add(new Country.CountryNameCode().content("US"));
 }});
 addressDetails.setCountry(country);
 AdminArea adminArea = new AdminArea();
 adminArea.setAdministrativeAreaName(new ArrayList<AdminArea.AdministrativeAreaName>() {{
 add(new AdministrativeAreaName().content("California"));
 }});
 country.setAdminArea(adminArea);
 Locality locality = new Locality();
 locality.setLocalityName(new ArrayList<Locality.LocalityName>() {{
 add(new LocalityName().content("San Francisco"));
 }});
 locality.setPostalCode(new PostalCode().postalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>() {{
 add(new PostalCodeNumber().content("94102"));
 }}));
 adminArea.setLocality(locality);
 inputAddress.setAddressDetails(addressDetails);
 inputAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 inputAddress.setAccuracy(10);
 expectedOutput = new GeoUsAddress();
 expectedOutput.setCountry("US");
 expectedOutput.setState("California");
 expectedOutput.setCity("San Francisco");
 expectedOutput.setPostalCode("94102");
 expectedOutput.setAddressLine1("San Francisco");
 expectedOutput.setAddressLine2(null);
 expectedOutput.setCoordinate(new Coordinate(37.7749, -122.4194));
 expectedOutput.setAccuracy(10);
 }

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
 GeoUsAddress output = functor.execute(inputAddress);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testExecute_NullAddress_ThrowsNullPointerException() {
 try {
 functor.execute(null);
 fail("Expected a NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testExecute_NullCountry_DoesNotSetCountry() {
 inputAddress.setAddressDetails(null);
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCountry());
 }

 @Test
 public void testExecute_EmptyCountryNameCode_DoesNotSetCountry() {
 Country emptyCountry = new Country();
 emptyCountry.setCountryNameCode(new ArrayList<Country.CountryNameCode>());
 AddressDetails addressDetails = new AddressDetails();
 addressDetails.setCountry(emptyCountry);
 inputAddress.setAddressDetails(addressDetails);
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCountry());
 }

 @Test
 public void testExecute_NullAdminArea_DoesNotSetStateOrCity() {
 AdminArea nullAdminArea = new AdminArea();
 inputAddress.setAddressDetails(new AddressDetails().adminArea(nullAdminArea));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getState());
 assertNull(output.getCity());
 }

 @Test
 public void testExecute_EmptyAdminAreaName_DoesNotSetStateOrCity() {
 AdminArea adminArea = new AdminArea();
 adminArea.setAdministrativeAreaName(new ArrayList<AdminArea.AdministrativeAreaName>());
 inputAddress.setAddressDetails(new AddressDetails().adminArea(adminArea));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getState());
 assertNull(output.getCity());
 }

 @Test
 public void testExecute_NullSubAdminArea_DoesNotSetCounty() {
 AdminArea adminArea = new AdminArea();
 SubAdminArea subAdminArea = new SubAdminArea();
 subAdminArea.setSubAdministrativeAreaName(new ArrayList<SubAdminArea.SubAdministrativeAreaName>());
 adminArea.setSubAdministrativeArea(subAdminArea);
 inputAddress.setAddressDetails(new AddressDetails().adminArea(adminArea));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCounty());
 }

 @Test
 public void testExecute_EmptySubAdminAreaName_DoesNotSetCounty() {
 AdminArea adminArea = new AdminArea();
 SubAdminArea subAdminArea = new SubAdminArea();
 subAdminArea.setSubAdministrativeAreaName(new ArrayList<SubAdminArea.SubAdministrativeAreaName>());
 inputAddress.setAddressDetails(new AddressDetails().adminArea(adminArea).subAdministrativeArea(subAdminArea));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCounty());
 }

 @Test
 public void testExecute_NullLocality_DoesNotSetCityOrPostalCodeOrAddressLine() {
 Locality nullLocality = new Locality();
 inputAddress.setAddressDetails(new AddressDetails().locality(nullLocality));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCity());
 assertNull(output.getPostalCode());
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 }

 @Test
 public void testExecute_EmptyLocalityName_DoesNotSetCityOrPostalCodeOrAddressLine() {
 Locality locality = new Locality();
 locality.setLocalityName(new ArrayList<Locality.LocalityName>());
 inputAddress.setAddressDetails(new AddressDetails().locality(locality));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCity());
 assertNull(output.getPostalCode());
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 }

 @Test
 public void testExecute_NullThoroughfare_DoesNotSetAddressLine1OrAddressLine2() {
 ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
 locality.setThoroughfare(new Thoroughfare().thoroughfareName(thoroughfare));
 inputAddress.setAddressDetails(new AddressDetails().locality(locality));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 }

 @Test
 public void testExecute_EmptyThoroughfareName_DoesNotSetAddressLine1OrAddressLine2() {
 ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
 thoroughfare.setThoroughfareName(new ArrayList<ThoroughfareNameType>());
 locality.setThoroughfare(new Thoroughfare().thoroughfareName(thoroughfare));
 inputAddress.setAddressDetails(new AddressDetails().locality(locality));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getAddressLine1());
 assertNull(output.getAddressLine2());
 }

 @Test
 public void testExecute_NullPostalCode_DoesNotSetPostalCode() {
 PostalCode nullPostalCode = new PostalCode();
 locality.setPostalCode(nullPostalCode);
 inputAddress.setAddressDetails(new AddressDetails().locality(locality));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getPostalCode());
 }

 @Test
 public void testExecute_EmptyPostalCodeNumber_DoesNotSetPostalCode() {
 PostalCode postalCode = new PostalCode();
 postalCode.setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>());
 locality.setPostalCode(postalCode);
 inputAddress.setAddressDetails(new AddressDetails().locality(locality));
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getPostalCode());
 }

 @Test
 public void testExecute_NullCoordinate_DoesNotSetCoordinate() {
 inputAddress.setCoordinate(null);
 GeoUsAddress output = functor.execute(inputAddress);
 assertNull(output.getCoordinate());
 }

 @Test
 public void testExecute_EmptyAccuracy_SetsAccuracyToZero() {
 inputAddress.setAccuracy(0);
 GeoUsAddress output = functor.execute(inputAddress);
 assertEquals(0, output.getAccuracy());
 }

 @Test
 public void testExecute_NegativeAccuracy_SetsAccuracyToZero() {
 inputAddress.setAccuracy(-1);
 GeoUsAddress output = functor.execute(inputAddress);
 assertEquals(0, output.getAccuracy());
 }

 @Test
 public void testExecute_NonZeroAccuracy_SetsAccuracyCorrectly() {
 inputAddress.setAccuracy(15);
 GeoUsAddress output = functor.execute(inputAddress);
 assertEquals(15, output.getAccuracy());
 }
}