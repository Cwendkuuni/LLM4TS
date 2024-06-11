package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import org.junit.*;
import org.junit.runner.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.mockito.MockitoAnnotations;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress testAddress;
 private GeoUsAddress expectedUsAddress;
 private AddressToUsAddressFunctor addressToUsAddressFunctor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 testAddress = new GeoAddress();
 testAddress.setAddressDetails(new AddressDetails());
 expectedUsAddress = new GeoUsAddress();
 addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 }

 @Test
 public void testExecutingAddressWithValidCountryAndAdministrativeArea_SetsCorrectValues() {
 // Given
 testAddress.getAddressDetails().getCountry().setCountryNameCode(new CountryNameCode("US", "USA"));
 testAddress.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeAreaName("California"));
 testAddress.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeAreaName("San Francisco"));
 testAddress.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new SubAdministrativeAreaName("The City"));
 testAddress.getAddressDetails().getAdministrativeArea().getLocality().setLocalityName(new LocalityName("San Francisco"));
 testAddress.getAddressDetails().getAdministrativeArea().getLocality().setPostalCodeNumber(new PostalCodeNumber("94102"));
 testAddress.getAddressDetails().getAdministrativeArea().getLocality().setThoroughfareName(new ThoroughfareNameType("Market Street"));
 testAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 testAddress.setAccuracy(10.0);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(testAddress);

 // Then
 assertEquals("USA", result.getCountry());
 assertEquals("California", result.getState());
 assertEquals("San Francisco", result.getCity());
 assertEquals("The City", result.getCounty());
 assertEquals("94102", result.getPostalCode());
 assertEquals("Market Street", result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertEquals(37.7749, result.getCoordinate().getLatitude(), 0.0);
 assertEquals(-122.4194, result.getCoordinate().getLongitude(), 0.0);
 assertEquals(10.0, result.getAccuracy());
 }

 @Test
 public void testExecutingAddressWithNullCountry_ReturnsEmptyUsAddress() {
 // Given
 testAddress.setAddressDetails(new AddressDetails());
 testAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 testAddress.setAccuracy(10.0);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(testAddress);

 // Then
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCity());
 assertNull(result.getCounty());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertEquals(37.7749, result.getCoordinate().getLatitude(), 0.0);
 assertEquals(-122.4194, result.getCoordinate().getLongitude(), 0.0);
 assertEquals(10.0, result.getAccuracy());
 }

 @Test
 public void testExecutingAddressWithInvalidCountryCode_ReturnsEmptyUsAddress() {
 // Given
 testAddress.getAddressDetails().getCountry().setCountryNameCode(new CountryNameCode("CA", "Canada"));
 testAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 testAddress.setAccuracy(10.0);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(testAddress);

 // Then
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCity());
 assertNull(result.getCounty());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertEquals(37.7749, result.getCoordinate().getLatitude(), 0.0);
 assertEquals(-122.4194, result.getCoordinate().getLongitude(), 0.0);
 assertEquals(10.0, result.getAccuracy());
 }

 @Test
 public void testExecutingAddressWithNoCountryOrAdministrativeArea_ReturnsEmptyUsAddress() {
 // Given
 testAddress.setAddressDetails(null);
 testAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
 testAddress.setAccuracy(10.0);

 // When
 GeoUsAddress result = addressToUsAddressFunctor.execute(testAddress);

 // Then
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCity());
 assertNull(result.getCounty());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertEquals(37.7749, result.getCoordinate().getLatitude(), 0.0);
 assertEquals(-122.4194, result.getCoordinate().getLongitude(), 0.0);
 assertEquals(10.0, result.getAccuracy());
 }
}