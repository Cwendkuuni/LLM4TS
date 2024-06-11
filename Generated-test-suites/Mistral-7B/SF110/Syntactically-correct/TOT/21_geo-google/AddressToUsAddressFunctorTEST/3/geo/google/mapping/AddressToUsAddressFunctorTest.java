
package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.list.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecuteValidAddress() {
        // Arrange
        final GeoAddress addr = mock(GeoAddress.class);
        when(addr.getAddressDetails()).thenReturn(new AddressDetails());
        final AddressDetails addressDetails = new AddressDetails();
        final Country country = new Country();
        country.setCountryNameCode(Arrays.asList(new Country.CountryNameCode().content("US")));
        country.setAdministrativeArea(new AdministrativeArea());
        final AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName().content("California")));
        country.setLocality(new Locality());
        final Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName().content("San Francisco")));
        locality.setPostalCode(new PostalCode().postalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().content("94102"))));
        locality.setThoroughfare(new Thoroughfare());
        final Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.setThoroughfareName(Arrays.asList(new ThoroughfareNameType().content("Market Street")));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(administrativeArea);
        addressDetails.setLocality(locality);
        when(addr.getAddressDetails()).thenReturn(addressDetails);
        when(addr.getCoordinate()).thenReturn(null);
        when(addr.getAccuracy()).thenReturn(null);

        final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        // Act
        final GeoUsAddress result = functor.execute(addr);

        // Assert
        assertNotNull(result);
        assertEquals("California", result.getState());
        assertEquals("San Francisco", result.getCity());
        assertEquals("94102", result.getPostalCode());
        assertEquals("Market Street", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCountry());
        assertEquals(0.0, result.getAccuracy(), 0.01); // Assuming accuracy is in meters
        assertNull(result.getCoordinate());
    }

    @Test
    public void testExecuteInvalidAddress() {
        // Arrange
        final GeoAddress addr = mock(GeoAddress.class);
        when(addr.getAddressDetails()).thenReturn(new AddressDetails());
        final AddressDetails addressDetails = new AddressDetails();
        final Country country = new Country();
        country.setCountryNameCode(Arrays.asList(new Country.CountryNameCode().content("CA"))); // Invalid country for US
        country.setAdministrativeArea(new AdministrativeArea());
        final AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName().content("California")));
        country.setLocality(new Locality());
        final Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName().content("San Francisco")));
        locality.setPostalCode(new PostalCode().postalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().content("94102"))));
        locality.setThoroughfare(new Thoroughfare());
        final Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.setThoroughfareName(Arrays.asList(new ThoroughfareNameType().content("Market Street")));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(administrativeArea);
        addressDetails.setLocality(locality);
        when(addr.getAddressDetails()).thenReturn(addressDetails);
        when(addr.getCoordinate()).thenReturn(null);
        when(addr.getAccuracy()).thenReturn(null);

        final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        // Act & Assert
        assertThrows(GeoException.class, () -> functor.execute(addr));
    }

    @Test
    public void testExecuteAddressWithCoordinate() {
        // Arrange
        final GeoAddress addr = mock(GeoAddress.class);
        when(addr.getAddressDetails()).thenReturn(new AddressDetails());
        final AddressDetails addressDetails = new AddressDetails();
        when(addr.getCoordinate()).thenReturn(new Coordinate());
        when(addr.getAccuracy()).thenReturn(12.5);

        final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        // Act
        final GeoUsAddress result = functor.execute(addr);

        // Assert
        assertNotNull(result);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertEquals(12.5, result.getAccuracy(), 0.01);
        assertEquals(addr.getCoordinate(), result.getCoordinate());
    }
}