package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.mockito.Mockito;

public class AddressToUsAddressFunctorTest {

 GeoAddress mockGeoAddress;
 GeoUsAddress mockGeoUsAddress;

 @Before
 public void setUp() {
 mockGeoAddress = mock(GeoAddress.class);
 mockGeoUsAddress = mock(GeoUsAddress.class);
 }

 @Test
 public void testExecute_ValidAddress_ReturnUsAddress() {
 AddressDetails mockAddressDetails = mock(AddressDetails.class);
 when(mockAddressDetails.getCountry()).thenReturn(new AddressDetails.Country());
 when(mockAddressDetails.getCountry().getCountryNameCode()).thenReturn(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
 when(mockAddressDetails.getCountry().getAdministrativeArea()).thenReturn(new AdministrativeArea());
 when(mockAddressDetails.getCountry().getAdministrativeArea().getAdministrativeAreaName()).thenReturn(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName()));
 when(mockAddressDetails.getCountry().getAdministrativeArea().getSubAdministrativeArea()).thenReturn(null);

 when(mockAddressDetails.getLocality()).thenReturn(new Locality());
 when(mockAddressDetails.getLocality().getLocalityName()).thenReturn(Collections.singletonList(new Locality.LocalityName()));
 when(mockAddressDetails.getLocality().getPostalCode()).thenReturn(new PostalCode());
 when(mockAddressDetails.getLocality().getPostalCode().getPostalCodeNumber()).thenReturn(Collections.singletonList(new PostalCode.PostalCodeNumber()));
 when(mockAddressDetails.getLocality().getThoroughfare()).thenReturn(new Thoroughfare());
 when(mockAddressDetails.getLocality().getThoroughfare().getThoroughfareName()).thenReturn(Collections.singletonList(new ThoroughfareNameType()));

 when(mockGeoAddress.getAddressDetails()).thenReturn(mockAddressDetails);
 when(mockGeoAddress.getCoordinate()).thenReturn(new Coordinate());

 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = addressToUsAddressFunctor.execute(mockGeoAddress);

 assertEquals(mockGeoUsAddress, result);

 verify(mockGeoAddress).getAddressDetails();
 verify(mockAddressDetails).getCountry();
 verify(mockAddressDetails.getCountry()).getCountryNameCode();
 verify(mockAddressDetails.getCountry().getAdministrativeArea()).getAdministrativeAreaName();
 verify(mockAddressDetails.getLocality()).getLocalityName();
 verify(mockAddressDetails.getLocality()).getPostalCode();
 verify(mockAddressDetails.getLocality().getPostalCode()).getPostalCodeNumber();
 verify(mockAddressDetails.getLocality().getThoroughfare()).getThoroughfareName();
 verify(mockGeoAddress).getCoordinate();
 }

 @Test
 public void testExecute_NullAddress_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = addressToUsAddressFunctor.execute(null);

 assertEquals(new GeoUsAddress(), result);
 }

 @Test
 public void testExecute_AddressWithoutCountry_ReturnEmptyUsAddress() {
 AddressDetails mockAddressDetails = mock(AddressDetails.class);
 when(mockAddressDetails.getCountry()).thenReturn(null);

 when(mockGeoAddress.getAddressDetails()).thenReturn(mockAddressDetails);

 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = addressToUsAddressFunctor.execute(mockGeoAddress);

 assertEquals(new GeoUsAddress(), result);
 }

 @Test
 public void testExecute_AddressWithoutCountryOrAdministrativeArea_ReturnEmptyUsAddress() {
 AddressDetails mockAddressDetails = mock(AddressDetails.class);
 when(mockAddressDetails.getCountry()).thenReturn(new AddressDetails.Country());
 when(mockAddressDetails.getCountry().getAdministrativeArea()).thenReturn(null);

 when(mockGeoAddress.getAddressDetails()).thenReturn(mockAddressDetails);

 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = addressToUsAddressFunctor.execute(mockGeoAddress);

 assertEquals(new GeoUsAddress(), result);
 }

 @Test
 public void testExecute_AddressWithMultipleSubAdministrativeAreas_ReturnUsAddressWithFirstSubAdminName() {
 AddressDetails mockAddressDetails = mock(AddressDetails.class);when(mockAddressDetails.getCountry()).thenReturn(new AddressDetails.Country());
 when(mockAddressDetails.getCountry().getAdministrativeArea()).thenReturn(new AdministrativeArea());
 when(mockAddressDetails.getCountry().getAdministrativeArea().getAdministrativeAreaName()).thenReturn(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName()));
 when(mockAddressDetails.getCountry().getAdministrativeArea().getSubAdministrativeArea()).thenReturn(new AdministrativeArea());
 when(mockAddressDetails.getCountry().getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName()).thenReturn(Arrays.asList(new AdministrativeArea.SubAdministrativeAreaName(), new AdministrativeArea.SubAdministrativeAreaName()));

 when(mockAddressDetails.getLocality()).thenReturn(null);

 when(mockGeoAddress.getAddressDetails()).thenReturn(mockAddressDetails);

 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress result = addressToUsAddressFunctor.execute(mockGeoAddress);

 assertEquals("US", result.getCountry());
 assertEquals(null, result.getState());
 assertEquals(null, result.getCounty());
 assertEquals(null, result.getCity());
 assertEquals(null, result.getPostalCode());
 assertEquals(null, result.getAddressLine1());
 assertEquals(null, result.getAddressLine2());
 assertEquals(null, result.getCoordinate());
 verify(mockAddressDetails, times(1)).getCountry();
 verify(mockAddressDetails.getCountry(), times(1)).getAdministrativeArea();
 verify(mockAddressDetails.getCountry().getAdministrativeArea(), times(1)).getSubAdministrativeArea();
 verify(mockAddressDetails.getCountry().getAdministrativeArea().getSubAdministrativeArea(), times(1)).getSubAdministrativeAreaName();
 }
}