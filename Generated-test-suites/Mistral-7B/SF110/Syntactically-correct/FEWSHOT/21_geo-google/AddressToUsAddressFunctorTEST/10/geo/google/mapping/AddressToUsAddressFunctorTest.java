package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddressToUsAddressFunctorTest {

 @Mock
 private GeoAddress addr;
 @Mock
 private PostalCode postalCode;
 @Mock
 private ThoroughfareNameType thoroughfare1;
 @Mock
 private ThoroughfareNameType thoroughfare2;
 @Mock
 private Locality locality;
 @Mock
 private AdministrativeArea adminArea;
 @Mock
 private AdministrativeArea subAdminArea;
 @Mock
 private Country country;
 @InjectMocks
 private AddressToUsAddressFunctor functor;

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
 // Given
 when(addr.getAddressDetails()).thenReturn(new AddressDetails());
 when(addr.getAddressDetails().getCountry()).thenReturn(country);
 when(country.getCountryNameCode()).thenReturn(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
 add(new AddressDetails.Country.CountryNameCode() {{
 setContent("US");
 }});
 }});
 when(country.getAdministrativeArea()).thenReturn(adminArea);
 when(adminArea.getAdministrativeAreaName()).thenReturn(new ArrayList<AdministrativeArea.AdministrativeAreaName>() {{
 add(new AdministrativeArea.AdministrativeAreaName() {{
 setContent("State");
 }});
 }});
 when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
 when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(new ArrayList<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName>() {{
 add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName() {{
 setContent("County");
 }});
 }});
 when(subAdminArea.getLocality()).thenReturn(locality);
 when(locality.getLocalityName()).thenReturn(new ArrayList<Locality.LocalityName>() {{
 add(new Locality.LocalityName() {{
 setContent("City");
 }});
 }});
 when(locality.getPostalCode()).thenReturn(postalCode);
 when(postalCode.getPostalCodeNumber()).thenReturn(new ArrayList<PostalCode.PostalCodeNumber>() {{
 add(new PostalCode.PostalCodeNumber() {{
 setContent("12345");
 }});
 }});
 when(addr.getCoordinate()).thenReturn(new Coordinate());
 when(addr.getAccuracy()).thenReturn(1.0);

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertEquals("US", usAddress.getCountry());
 assertEquals("State", usAddress.getState());
 assertEquals("County", usAddress.getCounty());
 assertEquals("City", usAddress.getCity());
 assertEquals("12345", usAddress.getPostalCode());
 assertNotNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNotNull(usAddress.getCoordinate());
 assertEquals(1.0, usAddress.getAccuracy(), 0.01);
 }

 @Test
 public void testExecute_NullCountry_ReturnsEmptyUsAddress() {
 // Given
 when(addr.getAddressDetails()).thenReturn(new AddressDetails());
 when(addr.getAddressDetails().getCountry()).thenReturn(null);

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertNotNull(usAddress);
 assertEquals(0, usAddress.getCountry().length());
 assertEquals(0, usAddress.getState().length());
 assertEquals(0, usAddress.getCounty().length());
 assertEquals(0, usAddress.getCity().length());
 assertEquals(0, usAddress.getPostalCode().length());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertEquals(0.0, usAddress.getAccuracy(), 0.01);
 }

 @Test
 public void testExecute_EmptyAddressDetails_ReturnsEmptyUsAddress() {
 // Given
 when(addr.getAddressDetails()).thenReturn(new AddressDetails());

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertNotNull(usAddress);
 assertEquals(0, usAddress.getCountry().length());
 assertEquals(0, usAddress.getState().length());
 assertEquals(0, usAddress.getCounty().length());
 assertEquals(0, usAddress.getCity().length());
 assertEquals(0, usAddress.getPostalCode().length());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertEquals(0.0, usAddress.getAccuracy(), 0.01);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
 // Given
 GeoAddress nullAddr = null;

 // When & Then
 try {
 functor.execute(nullAddr);
 fail("GeoException expected");
 } catch (GeoException e) {
 // Pass
 }
 }
}