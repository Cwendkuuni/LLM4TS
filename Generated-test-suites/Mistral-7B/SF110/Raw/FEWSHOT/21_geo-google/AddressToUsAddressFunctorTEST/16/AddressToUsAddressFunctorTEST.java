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
 private GeoAddress geoAddress;

 @Mock
 private AddressDetails addressDetails;

 @Mock
 private AdministrativeArea adminArea;

 @Mock
 private SubAdministrativeArea subAdminArea;

 @Mock
 private Locality locality;

 @Mock
 private Thoroughfare thoroughfare;

 @InjectMocks
 private AddressToUsAddressFunctor addressToUsAddressFunctor;

 @Test
 public void testExecute_validAddress_returnsUsAddress() {
 // Given
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country().CountryNameCode(new CountryNameCodeType().Content("US")));
 when(addressDetails.getAdministrativeArea()).thenReturn(adminArea);
 when(adminArea.getAdministrativeAreaName()).thenReturn(new AdministrativeArea.AdministrativeAreaName().Content("California"));
 when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
 when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(new AdministrativeArea.SubAdministrativeAreaName().Content("Los Angeles"));
 when(subAdminArea.getLocality()).thenReturn(locality);
 when(locality.getLocalityName()).thenReturn(new Locality.LocalityName().Content("Downtown"));
 when(locality.getPostalCode()).thenReturn(new PostalCode().PostalCodeNumber(new PostalCodeNumberType().Content("90012")));
 when(locality.getThoroughfare()).thenReturn(thoroughfare);
 when(thoroughfare.getThoroughfareName()).thenReturn(new ThoroughfareNameType().Content("Main Street"));

 // When
 GeoUsAddress usAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertEquals("US", usAddress.getCountry());
 assertEquals("California", usAddress.getState());
 assertEquals("Los Angeles", usAddress.getCity());
 assertEquals("90012", usAddress.getPostalCode());
 assertEquals("Main Street", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNotNull(usAddress.getCoordinate());
 assertNotNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_nullAddress_returnsNull() {
 // Given
 when(geoAddress).thenReturn(null);

 // When
 GeoUsAddress usAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertNull(usAddress);
 }

 @Test
 public void testExecute_addressWithNoCountry_returnsEmptyUsAddress() {
 // Given
 when(geoAddress.getAddressDetails()).thenReturn(null);

 // When
 GeoUsAddress usAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Then
 assertNotNull(usAddress);
 assertEquals("", usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNotNull(usAddress.getAccuracy());
 }
}