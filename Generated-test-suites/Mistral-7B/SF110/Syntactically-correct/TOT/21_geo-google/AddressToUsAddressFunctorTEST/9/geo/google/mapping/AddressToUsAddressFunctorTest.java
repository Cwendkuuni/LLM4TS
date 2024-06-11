package geo.google.mapping;

import static org.junit.Assert.*;
import static org.apache.commons.collections.CollectionUtils.*;

import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testAddressWithEmptyCountryAndAdministrativeAreaFields() {
        // Given
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        address.setCoordinate(new Coordinate(40.7128, -74.0060));
        address.setAccuracy(10);

        // When
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getCounty());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNull(usAddress.getCoordinate());
        assertEquals(10, usAddress.getAccuracy());
    }

    @Test
    public void testValidAddressToUsAddressFunctor() {
        // Given
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setAdministrativeArea(new AdministrativeArea());
        AdministrativeArea administrativeArea = country.getAdministrativeArea();
        administrativeArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        administrativeArea.setSubAdministrativeArea(new SubAdministrativeArea());
        SubAdministrativeArea subAdministrativeArea = administrativeArea.getSubAdministrativeArea();
        subAdministrativeArea.setSubAdministrativeAreaName(new SubAdministrativeAreaName("Los Angeles"));
        Locality locality = subAdministrativeArea != null ? subAdministrativeArea.getLocality() : administrativeArea.getLocality();
        locality.setLocalityName(new LocalityName("Los Angeles"));
        locality.setPostalCode(new PostalCode());
        PostalCode postalCode = locality.getPostalCode();
        postalCode.setPostalCodeNumber(new PostalCodeNumber("90001"));
        address.setAddressDetails(addressDetails);
        address.setCoordinate(new Coordinate(40.7128, -74.0060));
        address.setAccuracy(10);

        // When
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertEquals("California", usAddress.getState());
        assertEquals("Los Angeles", usAddress.getCity());
        assertEquals("90001", usAddress.getPostalCode());
        assertEquals("Los Angeles", usAddress.getCity()); // Checking if city is set properly
        assertNotNull(usAddress.getCountry());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(10, usAddress.getAccuracy());
    }

    @Test
    public void testAddressWithInvalidCountryNameCode() {
        // Given
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("CA"));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        address.setCoordinate(new Coordinate(40.7128, -74.0060));
        address.setAccuracy(10);

        // When
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        try {
            GeoUsAddress usAddress = functor.execute(address);
            fail("Expected GeoException");
        } catch (GeoException e) {
            // Then
            assertTrue(e.getMessage().contains("Invalid country code CA."));
        }
    }
}