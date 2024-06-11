package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    private AddressDetails addressDetails;
    private GeoAddress geoAddress;
    private GeoUsAddress geoUsAddress;

    @Before
    public void setup() {
        addressDetails = mock(AddressDetails.class);
        when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country());
        when(addressDetails.getAdministrativeArea()).thenReturn(new AdministrativeArea());
        when(addressDetails.getLocality()).thenReturn(new Locality());
        when(addressDetails.getPostalCode()).thenReturn(new PostalCode());
        geoAddress = new GeoAddress();
        geoAddress.setAddressDetails(addressDetails);
        geoUsAddress = new GeoUsAddress();
    }

    @Test
    public void testEmptyAddress() {
        given(addressDetails.getCountry()).willReturn(null);
        given(addressDetails.getAdministrativeArea()).willReturn(null);
        given(addressDetails.getLocality()).willReturn(null);
        given(addressDetails.getPostalCode()).willReturn(null);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCountryOnly() {
        String country = "US";
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testStateCountry() {
        String country = "US";
        String state = "CA";
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCityStateCountry() {
        String country = "US";
        String state = "CA";
        String city = "Los Angeles";
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);
        given(addressDetails.getLocality().getLocalityName().get(0).getContent()).willReturn(city);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertEquals(city, result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCityStateCountryPostalCode() {
        String country = "US";
        String state = "CA";
        String city = "Los Angeles";
        String postalCode = "90001";
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);
        given(addressDetails.getLocality().getLocalityName().get(0).getContent()).willReturn(city);
        given(addressDetails.getPostalCode().getPostalCodeNumber().get(0).getContent()).willReturn(postalCode);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertEquals(city, result.getCity());
        assertEquals(postalCode, result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCityStateCountryPostalCodeAddressLine1() {
        String country = "US";
        String state = "CA";
        String city = "Los Angeles";
        String postalCode = "90001";
        String addressLine1 = "123 Main St";
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);
        given(addressDetails.getLocality().getLocalityName().get(0).getContent()).willReturn(city);
        given(addressDetails.getPostalCode().getPostalCodeNumber().get(0).getContent()).willReturn(postalCode);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(0).getContent()).willReturn(addressLine1);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertEquals(city, result.getCity());
        assertEquals(postalCode, result.getPostalCode());
        assertEquals(addressLine1, result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCityStateCountryPostalCodeAddressLine1AddressLine2() {
        String country = "US";
        String state = "CA";
        String city = "Los Angeles";
        String postalCode = "90001";
        String addressLine1 = "123 Main St";
        String addressLine2 = "456 Oak St";
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);
        given(addressDetails.getLocality().getLocalityName().get(0).getContent()).willReturn(city);
        given(addressDetails.getPostalCode().getPostalCodeNumber().get(0).getContent()).willReturn(postalCode);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(0).getContent()).willReturn(addressLine1);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(1).getContent()).willReturn(addressLine2);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertEquals(city, result.getCity());
        assertEquals(postalCode, result.getPostalCode());
        assertEquals(addressLine1, result.getAddressLine1());
        assertEquals(addressLine2, result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCityStateCountryPostalCodeAddressLine1AddressLine2Coordinate() {
        String country = "US";
        String state = "CA";
        String city = "Los Angeles";
        String postalCode = "90001";
        String addressLine1 = "123 Main St";
        String addressLine2 = "456 Oak St";
        Coordinate coordinate = new Coordinate();
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);
        given(addressDetails.getLocality().getLocalityName().get(0).getContent()).willReturn(city);
        given(addressDetails.getPostalCode().getPostalCodeNumber().get(0).getContent()).willReturn(postalCode);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(0).getContent()).willReturn(addressLine1);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(1).getContent()).willReturn(addressLine2);
        given(geoAddress.getCoordinate()).willReturn(coordinate);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertEquals(city, result.getCity());
        assertEquals(postalCode, result.getPostalCode());
        assertEquals(addressLine1, result.getAddressLine1());
        assertEquals(addressLine2, result.getAddressLine2());
        assertEquals(coordinate, result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testCityStateCountryPostalCodeAddressLine1AddressLine2CoordinateAccuracy() {
        String country = "US";
        String state = "CA";
        String city = "Los Angeles";
        String postalCode = "90001";
        String addressLine1 = "123 Main St";
        String addressLine2 = "456 Oak St";
        Coordinate coordinate = new Coordinate();
        Accuracy accuracy = new Accuracy();
        given(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).willReturn(country);
        given(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).willReturn(state);
        given(addressDetails.getLocality().getLocalityName().get(0).getContent()).willReturn(city);
        given(addressDetails.getPostalCode().getPostalCodeNumber().get(0).getContent()).willReturn(postalCode);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(0).getContent()).willReturn(addressLine1);
        given(localAddressDetails.getThoroughfare().getThoroughfareName().get(1).getContent()).willReturn(addressLine2);
        given(geoAddress.getCoordinate()).willReturn(coordinate);
        given(geoAddress.getAccuracy()).willReturn(accuracy);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(geoAddress);

        assertEquals(country, result.getCountry());
        assertEquals(state, result.getState());
        assertEquals(city, result.getCity());
        assertEquals(postalCode, result.getPostalCode());
        assertEquals(addressLine1, result.getAddressLine1());
        assertEquals(addressLine2, result.getAddressLine2());
        assertEquals(coordinate, result.getCoordinate());
        assertEquals(accuracy, result.getAccuracy());
    }
}