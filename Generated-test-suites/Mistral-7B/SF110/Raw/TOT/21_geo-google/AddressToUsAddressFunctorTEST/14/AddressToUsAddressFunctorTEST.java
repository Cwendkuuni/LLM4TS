package geo.google.mapping;

import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    // Expert 1's test case
    @Test
    public void testExecuteWithValidAddress() {
        // ...
    }

    // Expert 2's test case
    @Test
    public void testExecuteWithNullAddress() {
        // ...
    }

    // Expert 3's test case
    @Test
    public void testExecuteWithIncompleteAddress() {
        // ...
    }

    // Additional test cases

    // Test for execute with an empty address
    @Test
    public void testExecuteWithEmptyAddress() {
        GeoAddress emptyAddress = new GeoAddress();
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(emptyAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with no country
    @Test
    public void testExecuteWithNoCountryAddress() {
        GeoAddress noCountryAddress = new GeoAddress();
        noCountryAddress.setAddressDetails(new AddressDetails());
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(noCountryAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with no administrative area
    @Test
    public void testExecuteWithNoAdminAreaAddress() {
        GeoAddress noAdminAreaAddress = new GeoAddress();
        noAdminAreaAddress.setAddressDetails(new AddressDetails());
        noAdminAreaAddress.getAddressDetails().setCountry(new AddressDetails.Country());
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(noAdminAreaAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with no locality
    @Test
    public void testExecuteWithNoLocalityAddress() {
        GeoAddress noLocalityAddress = new GeoAddress();
        noLocalityAddress.setAddressDetails(new AddressDetails());
        noLocalityAddress.getAddressDetails().setCountry(new AddressDetails.Country());
        noLocalityAddress.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(noLocalityAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with no postal code
    @Test
    public void testExecuteWithNoPostalCodeAddress() {
        GeoAddress noPostalCodeAddress = new GeoAddress();
        noPostalCodeAddress.setAddressDetails(new AddressDetails());
        noPostalCodeAddress.getAddressDetails().setCountry(new AddressDetails.Country());
        noPostalCodeAddress.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
        noPostalCodeAddress.getAddressDetails().getCountry().getAdministrativeArea().setLocality(new Locality());
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(noPostalCodeAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with empty postal code
    @Test
    public void testExecuteWithEmptyPostalCodeAddress() {
        GeoAddress emptyPostalCodeAddress = new GeoAddress();
        emptyPostalCodeAddress.setAddressDetails(new AddressDetails());
        emptyPostalCodeAddress.getAddressDetails().setCountry(new AddressDetails.Country());
        emptyPostalCodeAddress.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
        emptyPostalCodeAddress.getAddressDetails().getCountry().getAdministrativeArea().setLocality(new Locality());
        emptyPostalCodeAddress.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(emptyPostalCodeAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with non-US country
    @Test
    public void testExecuteWithNonUsAddress() {
        GeoAddress nonUsAddress = new GeoAddress();
        nonUsAddress.setAddressDetails(new AddressDetails());
        nonUsAddress.getAddressDetails().setCountry(new AddressDetails.Country());
        nonUsAddress.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>());
        nonUsAddress.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().content("CA"));
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(nonUsAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }

    // Test for execute with an address with no coordinate
    @Test
    public void testExecuteWithNoCoordinateAddress() {
        GeoAddress noCoordinateAddress = new GeoAddress();
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(noCoordinateAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.ROOFTOP, usAddress.getAccuracy());
    }
}