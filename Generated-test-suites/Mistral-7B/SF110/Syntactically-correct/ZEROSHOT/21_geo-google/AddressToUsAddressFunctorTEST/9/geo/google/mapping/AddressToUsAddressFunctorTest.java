import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor;
 private GeoAddress address;

 @Before
 public void setup() {
 functor = AddressToUsAddressFunctor.getInstance();
 address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 address.getAddressDetails().setCountry(new AddressDetails.Country());
 address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US")));
 address.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
 address.getAddressDetails().getCountry().getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA")));
 address.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(Arrays.asList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName("San Francisco")));
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(Arrays.asList(new Locality.LocalityName("The City"), new Locality.LocalityName("San Francisco")));
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber("94112")));
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(Arrays.asList(new ThoroughfareNameType("Market"), new ThoroughfareNameType("Street")));
 address.setCoordinate(new Coordinate(37.7749, -122.4194));
 address.setAccuracy(50.0);
 }

 @Test
 public void testNullAddress() {
 GeoUsAddress result = functor.execute(null);
 assertNull(result);
 }

 @Test
 public void testEmptyAddress() {
 GeoUsAddress result = functor.execute(new GeoAddress());
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCounty());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCoordinate());
 assertEquals(0.0, result.getAccuracy(), 0.01);
 }

 @Test
 public void testValidAddress() {
 GeoUsAddress result = functor.execute(address);
 assertEquals("US", result.getCountry());
 assertEquals("CA", result.getState());
 assertEquals("San Francisco", result.getCounty());
 assertEquals("The City", result.getCity());
 assertEquals("94112", result.getPostalCode());
 assertEquals("Market", result.getAddressLine1());
 assertEquals("Street", result.getAddressLine2());
 assertNull(result.getThoroughfare());
 assertEquals(37.7749, result.getCoordinate().getLatitude(), 0.01);
 assertEquals(-122.4194, result.getCoordinate().getLongitude(), 0.01);
 assertEquals(50.0, result.getAccuracy(), 0.01);
 }

 @Test
 public void testAddressWithoutCountry() {
 address.getAddressDetails().setCountry(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCountry());
 }

 @Test
 public void testAddressWithoutState() {
 address.getAddressDetails().getCountry().setAdministrativeArea(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getState());
 }

 @Test
 public void testAddressWithoutCounty() {
 address.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCounty());
 }

 @Test
 public void testAddressWithoutCity() {
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().setLocality(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCity());
 }

 @Test
 public void testAddressWithoutPostalCode() {
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getPostalCode());
 }

 @Test
 public void testAddressWithoutAddressLines() {
 address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 }

 @Test
 public void testAddressWithoutCoordinate() {
 address.setCoordinate(null);
 GeoUsAddress result = functor.execute(address);
 assertNull(result.getCoordinate());
 }

 @Test(expected = GeoException.class)
 public void testInvalidCountryCode() {
 address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("XX")));
 GeoUsAddress result = null;
 try {
 result = functor.execute(address);
 } catch (GeoException e) {
 // Expected
 }
 assertNull(result);
 }
}