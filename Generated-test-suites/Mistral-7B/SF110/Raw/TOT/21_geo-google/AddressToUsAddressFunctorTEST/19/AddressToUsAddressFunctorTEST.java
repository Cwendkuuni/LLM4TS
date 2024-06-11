package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.junit.Before;
import org.junit.Test;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

import java.util.*;

public class AddressToUsAddressFunctorTest {

 private AddressDetails addressDetails;
 private GeoAddress geoAddress;
 private GeoUsAddress expectedUsAddress;
 private AddressToUsAddressFunctor functor;

 @Before
 public void setup() {
 addressDetails = mock(AddressDetails.class);
 geoAddress = new GeoAddress();
 expectedUsAddress = new GeoUsAddress();
 functor = AddressToUsAddressFunctor.getInstance();
 }

 @Test
 public void testValidUsAddress() {
 // Given
 when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country() {{
 setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode() {{
 setContent("US");
 }}));
 }});
 when(addressDetails.getAdministrativeArea()).thenReturn(new AdministrativeArea() {{
 setAdministrativeAreaName(ListUtils.list(new AdministrativeArea.AdministrativeAreaName() {{
 setContent("California");
 }}));
 }});
 when(addressDetails.getAdministrativeArea().getSubAdministrativeArea()).thenReturn(null);
 when(addressDetails.getAdministrativeArea().getLocality()).thenReturn(new Locality() {{
 setLocalityName(ListUtils.list(new Locality.LocalityName() {{
 setContent("San Francisco");
 }}));
 setPostalCode(new PostalCode() {{
 setPostalCodeNumber(ListUtils.list(new PostalCode.PostalCodeNumber() {{
 setContent("94101");
 }}));
 }});
 setThoroughfare(new Thoroughfare() {{
 setThoroughfareName(ListUtils.list(new ThoroughfareNameType() {{
 setContent("Market St");
 }}));
 }});
 }});
 geoAddress.setAddressDetails(addressDetails);
 geoAddress.setCoordinate(new Coordinate() {{
 setLatitude(37.7749);
 setLongitude(-122.4194);
 }});
 geoAddress.setAccuracy(5.0);

 // When
 GeoUsAddress actualUsAddress = functor.execute(geoAddress);

 // Then
 assertEquals(expectedUsAddress, actualUsAddress);
 expectedUsAddress.setCountry("United States");
 assertEquals("United States", actualUsAddress.getCountry());
 expectedUsAddress.setState("California");
 assertEquals("California", actualUsAddress.getState());
 expectedUsAddress.setCity("San Francisco");
 assertEquals("San Francisco", actualUsAddress.getCity());
 expectedUsAddress.setPostalCode("94101");
 assertEquals("94101", actualUsAddress.getPostalCode());
 expectedUsAddress.setAddressLine1("Market St");
 assertEquals("Market St", actualUsAddress.getAddressLine1());
 expectedUsAddress.setAddressLine2(null);
 assertNull(actualUsAddress.getAddressLine2());
 expectedUsAddress.setCoordinate(new Coordinate() {{
 setLatitude(37.7749);
 setLongitude(-122.4194);
 }});
 assertEquals(5.0, actualUsAddress.getAccuracy());
 }

 @Test(expected = GeoException.class)
 public void testNullAddressDetails() {
 geoAddress.setAddressDetails(null);
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testNullCountry() {
 when(addressDetails.getCountry()).thenReturn(null);
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testEmptyCountryNames() {
 when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country() {{
 setCountryNameCode(Collections.emptyList());
 }});
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testNullAdministrativeArea() {
 when(addressDetails.getAdministrativeArea()).thenReturn(null);
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testEmptyAdministrativeAreaNames() {
 when(addressDetails.getAdministrativeArea().getAdministrativeAreaName()).thenReturn(Collections.emptyList());
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testNullLocality() {
 when(addressDetails.getAdministrativeArea().getLocality()).thenReturn(null);
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testEmptyLocalityNames() {
 when(addressDetails.getAdministrativeArea().getLocality().getLocalityName()).thenReturn(Collections.emptyList());
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testNullPostalCode() {
 when(addressDetails.getAdministrativeArea().getLocality().getPostalCode()).thenReturn(null);
 functor.execute(geoAddress);
 }

 @Test(expected = GeoException.class)
 public void testEmptyPostalCodeNumbers() {
 when(addressDetails.getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber()).thenReturn(Collections.emptyList());
 functor.execute(geoAddress);
 }
}