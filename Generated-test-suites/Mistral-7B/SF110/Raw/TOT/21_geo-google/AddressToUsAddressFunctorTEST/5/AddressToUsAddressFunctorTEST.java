package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_ValidAddressWithCountryAndAdminArea_ReturnsUsAddressWithCorrectValues() {
        // Given
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AddressDetails.Country.CountryNameCode> countryNames = Arrays.asList(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountryNameCode(countryNames);
        country.setAdministrativeArea(adminArea);
        adminArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("New York")));
        addressDetails.setCountry(country);
        inputAddress.setAddressDetails(addressDetails);
        GeoCoordinate coordinate = new GeoCoordinate(1.2, 3.4);
        inputAddress.setCoordinate(coordinate);
        inputAddress.setAccuracy(10);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress outputAddress = functor.execute(inputAddress);

        // Then
        GeoUsAddress expectedAddress = new GeoUsAddress();
        expectedAddress.setCountry("US");
        expectedAddress.setState("New York");
        expectedAddress.setCity("N/A"); // not set in the given input
        expectedAddress.setPostalCode("N/A"); // not set in the given input
        expectedAddress.setAddressLine1("N/A"); // not set in the given input
        expectedAddress.setAddressLine2("N/A"); // not set in the given input
        expectedAddress.setCoordinate(coordinate);
        expectedAddress.setAccuracy(10);

        assertEquals(expectedAddress, outputAddress);
    }

    @Test
    public void testExecute_ValidAddressWithNoCountry_ThrowsGeoException() {
        // Given
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        inputAddress.setAddressDetails(addressDetails);

        // When & Then
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        assertThrows(GeoException.class, () -> functor.execute(inputAddress));
    }

    @Test
    public void testExecute_ValidAddressWithMultipleCountryNames_ReturnsUsAddressWithFirstCountryName() {
        // Given
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = Arrays.asList(new AddressDetails.Country.CountryNameCode("US"), new AddressDetails.Country.CountryNameCode("CA"));
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        inputAddress.setAddressDetails(addressDetails);
        GeoCoordinate coordinate = new GeoCoordinate(1.2, 3.4);
        inputAddress.setCoordinate(coordinate);
        inputAddress.setAccuracy(10);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress outputAddress = functor.execute(inputAddress);

        // Then
        GeoUsAddress expectedAddress = new GeoUsAddress();
        expectedAddress.setCountry("US");
        expectedAddress.setState("N/A"); // not set in the given input
        expectedAddress.setCity("N/A"); // not set in the given input
        expectedAddress.setPostalCode("N/A"); // not set in the given input
        expectedAddress.setAddressLine1("N/A"); // not set in the given input
        expectedAddress.setAddressLine2("N/A"); // not set in the given input
        expectedAddress.setCoordinate(coordinate);
        expectedAddress.setAccuracy(10);

        assertEquals(expectedAddress, outputAddress);
    }
}