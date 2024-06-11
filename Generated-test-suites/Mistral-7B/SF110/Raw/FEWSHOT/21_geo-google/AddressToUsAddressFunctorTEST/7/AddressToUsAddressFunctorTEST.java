package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.junit.Test;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.Functor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testEmptyAddress() {
        GeoAddress geoAddress = new GeoAddress();
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCounty());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNull(usAddress.getCoordinate());
        assertEquals(0.0, usAddress.getAccuracy(), 0.0);
    }

    @Test
    public void testUSAddress() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName("San Francisco", "San Francisco")));
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber("94102")));
        addressDetails.setLocality(locality);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new Coordinate(37.7749, -122.4194));

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals("United States", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertNull(usAddress.getCounty());
        assertEquals("San Francisco", usAddress.getCity());
        assertEquals("94102", usAddress.getPostalCode());
        assertEquals("San Francisco", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), usAddress.getAccuracy(), 0.0);
    }

    @Test
    public void testAddressWithSubAdminArea() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        SubAdministrativeArea subAdministrativeArea = new SubAdministrativeArea();
        subAdministrativeArea.setSubAdministrativeAreaName(Arrays.asList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName("SF", "San Francisco")));
        administrativeArea.setSubAdministrativeArea(subAdministrativeArea);
        Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName("The Mission", "Mission")));
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber("94110")));
        addressDetails.setLocality(locality);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new Coordinate(37.7667, -122.4063));

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals("United States", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertEquals("San Francisco", usAddress.getCounty());
        assertEquals("The Mission", usAddress.getCity());
        assertEquals("94110", usAddress.getPostalCode());
        assertEquals("The Mission", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), usAddress.getAccuracy(), 0.0);
    }

    @Test
    public void testAddressWithMultipleThoroughfares() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName("San Francisco", "San Francisco")));
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber("94102")));
        addressDetails.setLocality(locality);
        Thoroughfare thoroughfare1 = new Thoroughfare();
        thoroughfare1.setThoroughfareName(Arrays.asList(new ThoroughfareNameType("Market Street")));
        locality.setThoroughfare(thoroughfare1);

        Thoroughfare thoroughfare2 = new Thoroughfare();
        thoroughfare2.setThoroughfareName(Arrays.asList(new ThoroughfareNameType("Mission Street")));
        locality.getThoroughfare().setThoroughfare(thoroughfare2);

        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new Coordinate(37.7749, -122.4194));

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals("United States", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertNull(usAddress.getCounty());
        assertEquals("San Francisco", usAddress.getCity());
        assertEquals("94102", usAddress.getPostalCode());
        assertEquals("Market Street", usAddress.getAddressLine1());
        assertEquals("Mission Street", usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), usAddress.getAccuracy(), 0.0);
    }

    @Test
    public void testAddressWithEmptyThoroughfare() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName("San Francisco", "San Francisco")));
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber("94102")));
        addressDetails.setLocality(locality);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new Coordinate(37.7749, -122.4194));

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals("United States", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertNull(usAddress.getCounty());
        assertEquals("San Francisco", usAddress.getCity());
        assertEquals("94102", usAddress.getPostalCode());
        assertEquals("", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), usAddress.getAccuracy(), 0.0);
    }

    @Test
    public void testAddressWithNullThoroughfare() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        Locality locality = new Locality();
        locality.setLocalityName(Arrays.asList(new Locality.LocalityName("San Francisco", "San Francisco")));
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber("94102")));
        addressDetails.setLocality(locality);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new Coordinate(37.7749, -122.4194));

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals("United States", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertNull(usAddress.getCounty());
        assertEquals("San Francisco", usAddress.getCity());
        assertEquals("94102", usAddress.getPostalCode());
        assertEquals("", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), usAddress.getAccuracy(), 0.0);
    }

    @Test
    public void testAddressWithEmptyCountry() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setCountry(null);
        geoAddress.setAddressDetails(addressDetails);

        try {
            AddressToUsAddressFunctor.getInstance().execute(geoAddress);
            fail("Should have thrown an exception");
        } catch (GeoException e) {
            // Expected
        }
    }

    @Test
    public void testAddressWithEmptyAdministrativeArea() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        addressDetails.setAdministrativeArea(administrativeArea);

        try {
            AddressToUsAddressFunctor.getInstance().execute(geoAddress);
            fail("Should have thrown an exception");
        } catch (GeoException e) {
            // Expected
        }
    }

    @Test
    public void testAddressWithEmptyLocality() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        Locality locality = new Locality();
        addressDetails.setLocality(locality);

        try {
            AddressToUsAddressFunctor.getInstance().execute(geoAddress);
            fail("Should have thrown an exception");
        } catch (GeoException e) {
            // Expected
        }
    }

    @Test
    public void testAddressWithNullPostalCode() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country usa = new Country();
        usa.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode("US", "United States")));
        addressDetails.setCountry(usa);
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName("CA", "California")));
        addressDetails.setAdministrativeArea(administrativeArea);
        Locality locality = new Locality();
        addressDetails.setLocality(locality);
        geoAddress.setAddressDetails(addressDetails);

        try {
            AddressToUsAddressFunctor.getInstance().execute(geoAddress);
            fail("Should have thrown an exception");
        } catch (GeoException e) {
            // Expected
        }
    }
}