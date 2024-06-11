package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import org.apache.commons.collections.CollectionUtils;
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
 private AddressDetails country;
 @Mock
 private AddressDetails adminArea;
 @Mock
 private AdministrativeArea subAdminArea;
 @Mock
 private Locality locality;
 @Mock
 private PostalCode postalCode;
 @Mock
 private ThoroughfareNameType thoroughfareName1;
 @Mock
 private ThoroughfareNameType thoroughfareName2;
 @InjectMocks
 private AddressToUsAddressFunctor functor;

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
 // Given
 when(addr.getAddressDetails()).thenReturn(country);
 when(country.getCountry()).thenReturn("US");
 when(country.getAdministrativeArea()).thenReturn(adminArea);
 when(adminArea.getAdministrativeAreaName()).thenReturn(Arrays.asList(new AdministrativeAreaName("State")));
 when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
 when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(Arrays.asList(new SubAdministrativeAreaName("County")));
 when(locality.getLocalityName()).thenReturn(Arrays.asList(new LocalityName("City")));
 when(locality.getPostalCode()).thenReturn(postalCode);
 when(postalCode.getPostalCodeNumber()).thenReturn(Arrays.asList(new PostalCodeNumber("12345")));
 when(locality.getThoroughfare()).thenReturn(thoroughfareName1);
 when(thoroughfareName1.getThoroughfareName()).thenReturn("Main St");

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertEquals("US", usAddress.getCountry());
 assertEquals("State", usAddress.getState());
 assertEquals("County", usAddress.getCounty());
 assertEquals("City", usAddress.getCity());
 assertEquals("12345", usAddress.getPostalCode());
 assertEquals("Main St", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 }

 @Test
 public void testExecute_AddressWithMultipleThoroughfares_ReturnsAddressLine2() {
 // Given
 when(addr.getAddressDetails()).thenReturn(country);
 when(country.getCountry()).thenReturn("US");
 when(country.getAdministrativeArea()).thenReturn(adminArea);
 when(adminArea.getAdministrativeAreaName()).thenReturn(Arrays.asList(new AdministrativeAreaName("State")));
 when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
 when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(Arrays.asList(new SubAdministrativeAreaName("County")));
 when(locality.getLocalityName()).thenReturn(Arrays.asList(new LocalityName("City")));
 when(locality.getPostalCode()).thenReturn(postalCode);
 when(postalCode.getPostalCodeNumber()).thenReturn(Arrays.asList(new PostalCodeNumber("12345")));
 when(locality.getThoroughfare()).thenReturn(thoroughfareName1, thoroughfareName2);
 when(thoroughfareName1.getThoroughfareName()).thenReturn("Main St");
 when(thoroughfareName2.getThoroughfareName()).thenReturn("Elm St");

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertEquals("US", usAddress.getCountry());
 assertEquals("State", usAddress.getState());
 assertEquals("County", usAddress.getCounty());
 assertEquals("City", usAddress.getCity());
 assertEquals("12345", usAddress.getPostalCode());
 assertEquals("Main St", usAddress.getAddressLine1());
 assertEquals("Elm St", usAddress.getAddressLine2());
 }

 @Test
 public void testExecute_NullAddress_ReturnsEmptyUsAddress() {
 // Given
 when(addr.getAddressDetails()).thenReturn(null);

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertNull(usAddress);
 }

 @Test
 public void testExecute_AddressWithoutCountry_ReturnsEmptyUsAddress() {
 // Given
 when(addr.getAddressDetails()).thenReturn(country);
 when(country.getCountry()).thenReturn(null);

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertEquals("", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 }

 @Test
 public void testExecute_AddressWithoutState_ReturnsUsAddressWithCountry() {
 // Given
 when(addr.getAddressDetails()).thenReturn(country);
 when(country.getCountry()).thenReturn("US");
 when(country.getAdministrativeArea()).thenReturn(null);

 // When
 GeoUsAddress usAddress = functor.execute(addr);

 // Then
 assertEquals("US", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertEquals("", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 }
}