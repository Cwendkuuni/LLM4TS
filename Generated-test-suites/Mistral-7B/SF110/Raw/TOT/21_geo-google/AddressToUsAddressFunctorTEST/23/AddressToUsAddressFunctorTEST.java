import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.Country;
import oasis.names.tc.ciq.xsdschema.xal._2.Locality;
import oasis.names.tc.ciq.xsdschema.xal._2.ThoroughfareNameType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AddressToUsAddressFunctorTest {

 @Mock
 private GeoAddress geoAddress;
 private AddressToUsAddressFunctor addressToUsAddressFunctor;

 @Before
 public void setUp() {
 addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 }

 @Test
 public void testValidAddressToUsAddressConversion() {
 // Given
 AddressDetails addressDetails = new AddressDetails();
 Country country = new Country();
 country.setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode("US", "United States")));
 addressDetails.setCountry(country);
 AdministrativeArea administrativeArea = new AdministrativeArea();
 administrativeArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
 country.setAdministrativeArea(administrativeArea);
 Locality locality = new Locality();
 locality.setLocalityName(Collections.singletonList(new Locality.LocalityName("San Francisco", "San Francisco")));
 administrativeArea.setLocality(locality);
 ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
 thoroughfare1.setContent("Market Street");
 ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
 thoroughfare2.setContent("Castro Street");
 locality.setThoroughfare(Arrays.asList(thoroughfare1, thoroughfare2));
 PostalCode postalCode = new PostalCode();
 postalCode.setPostalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber("94110")));
 locality.setPostalCode(postalCode);
 when(geoAddress.getAddressDetails()).thenReturn(addressDetails);
 // When
 GeoUsAddress convertedAddress = addressToUsAddressFunctor.execute(geoAddress);
 // Then
 assertEquals("United States", convertedAddress.getCountry());
 assertEquals("California", convertedAddress.getState());
 assertEquals("San Francisco", convertedAddress.getCity());
 assertEquals("Market Street", convertedAddress.getAddressLine1());
 assertEquals("Castro Street", convertedAddress.getAddressLine2());
 assertEquals("94110", convertedAddress.getPostalCode());
 assertEquals(geoAddress.getCoordinate(), convertedAddress.getCoordinate());
 assertEquals(geoAddress.getAccuracy(), convertedAddress.getAccuracy());
 }

 @Test
 public void testEmptyAddressDetails() {
 // Given
 when(geoAddress.getAddressDetails()).thenReturn(new AddressDetails());
 // When
 GeoUsAddress convertedAddress = addressToUsAddressFunctor.execute(geoAddress);
 // Then
 assertNull(convertedAddress.getCountry());
 assertNull(convertedAddress.getState());
 assertNull(convertedAddress.getCity());
 assertNull(convertedAddress.getAddressLine1());
 assertNull(convertedAddress.getAddressLine2());
 assertNull(convertedAddress.getPostalCode());
 assertNull(convertedAddress.getCoordinate());
 assertEquals(geoAddress.getAccuracy(), convertedAddress.getAccuracy());
 }

 @Test
 public void testNullCoordinate() {
 // Given
 when(geoAddress.getAddressDetails()).thenReturn(new AddressDetails());
 when(geoAddress.getCoordinate()).thenReturn(null);
 // When
 GeoUsAddress convertedAddress = addressToUsAddressFunctor.execute(geoAddress);
 // Then
 assertNull(convertedAddress.getCountry());
 assertNull(convertedAddress.getState());
 assertNull(convertedAddress.getCity());
 assertNull(convertedAddress.getAddressLine1());
 assertNull(convertedAddress.getAddressLine2());
 assertNull(convertedAddress.getPostalCode());
 assertNull(convertedAddress.getCoordinate());
 assertEquals(geoAddress.getAccuracy(), convertedAddress.getAccuracy());
 }
}