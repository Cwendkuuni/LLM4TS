package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
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
 private Country country;
 @Mock
 private AdministrativeArea adminArea;
 @Mock
 private SubAdministrativeArea subAdminArea;
 @Mock
 private Locality locality;
 @Mock
 private PostalCode postalCode;
 @InjectMocks
 private AddressToUsAddressFunctor addressToUsAddressFunctor;

 @Test
 public void testExecute_validAddressData_returnsUsAddress() {
 // Arrange
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(addressDetails.getCountry()).thenReturn(country);
 when(country.getAdministrativeArea()).thenReturn(adminArea);
 when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
 when(subAdminArea.getLocality()).thenReturn(locality);
 when(locality.getPostalCode()).thenReturn(postalCode);
 when(country.getCountryNameCode()).thenReturn(Arrays.asList(new AddressDetails.Country.CountryNameCode("US")));
 when(adminArea.getAdministrativeAreaName()).thenReturn(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("California")));
 when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(Arrays.asList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName("San Francisco")));
 when(locality.getLocalityName()).thenReturn(Arrays.asList(new Locality.LocalityName("City Name")));
 when(postalCode.getPostalCodeNumber()).thenReturn(Arrays.asList(new PostalCode.PostalCodeNumber("12345")));
 when(locality.getThoroughfare()).thenReturn(mock(Thoroughfare.class));

 GeoUsAddress expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCountry("US");
 expectedUsAddress.setState("California");
 expectedUsAddress.setCounty("San Francisco");
 expectedUsAddress.setCity("City Name");
 expectedUsAddress.setPostalCode("12345");
 expectedUsAddress.setAddressLine1("Thoroughfare Name");
 expectedUsAddress.setAddressLine2("Thoroughfare Name 2");
 expectedUsAddress.setCoordinate(mock(GeoCoordinate.class));
 expectedUsAddress.setAccuracy(12.5);

 // Act
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Assert
 assertEquals(expectedUsAddress, actualUsAddress);
 }

 @Test
 public void testExecute_nullCountryData_returnsEmptyUsAddress() {
 // Arrange
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(addressDetails.getCountry()).thenReturn(null);

 GeoUsAddress expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCountry(null);
 expectedUsAddress.setState(null);
 expectedUsAddress.setCounty(null);
 expectedUsAddress.setCity(null);
 expectedUsAddress.setPostalCode(null);
 expectedUsAddress.setAddressLine1(null);
 expectedUsAddress.setAddressLine2(null);
 expectedUsAddress.setCoordinate(null);
 expectedUsAddress.setAccuracy(0.0);

 // Act
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Assert
 assertEquals(expectedUsAddress, actualUsAddress);
 }

 @Test
 public void testExecute_emptyCountryNameCode_returnsEmptyUsAddress() {
 // Arrange
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(addressDetails.getCountry()).thenReturn(country);
 when(country.getCountryNameCode()).thenReturn(new ArrayList<>());

 GeoUsAddress expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCountry(null);
 expectedUsAddress.setState(null);
 expectedUsAddress.setCounty(null);
 expectedUsAddress.setCity(null);
 expectedUsAddress.setPostalCode(null);
 expectedUsAddress.setAddressLine1(null);
 expectedUsAddress.setAddressLine2(null);
 expectedUsAddress.setCoordinate(null);
 expectedUsAddress.setAccuracy(0.0);

 // Act
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Assert
 assertEquals(expectedUsAddress, actualUsAddress);
 }

 @Test
 public void testExecute_nullAdminArea_returnsEmptyUsAddress() {
 // Arrange
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 when(addressDetails.getCountry()).thenReturn(country);
 when(country.getAdministrativeArea()).thenReturn(null);

 GeoUsAddress expectedUsAddress = new GeoUsAddress();
 expectedUsAddress.setCountry("US");
 expectedUsAddress.setState(null);
 expectedUsAddress.setCounty(null);
 expectedUsAddress.setCity(null);
 expectedUsAddress.setPostalCode(null);
 expectedUsAddress.setAddressLine1(null);
 expectedUsAddress.setAddressLine2(null);
 expectedUsAddress.setCoordinate(null);
 expectedUsAddress.setAccuracy(0.0);

 // Act
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(geoAddress);

 // Assert
 assertEquals(expectedUsAddress, actualUsAddress);
 }
}