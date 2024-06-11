package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AddressToUsAddressFunctorTest {

    private GeoAddress mockGeoAddress;
    private GeoUsAddress mockGeoUsAddress;

    @Before
    public void setup() {
        mockGeoAddress = mock(GeoAddress.class);
        mockGeoUsAddress = mock(GeoUsAddress.class);
    }

    @Test
    public void testEmptyAddress() {
        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails());
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(mockGeoAddress);
        assertNull(result);
    }

    @Test
    public void testAddressWithCountry() {
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));
        addressDetails.setCountry(country);

        when(mockGeoAddress.getAddressDetails()).thenReturn(addressDetails);
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("United States", result.getCountry());
    }

    @Test
    public void testAddressWithState() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));

        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);

        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails().setCountry(country));
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("California", result.getState());
    }

    @Test
    public void testAddressWithCounty() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));

        AdministrativeArea adminArea = new AdministrativeArea();
        SubAdministrativeArea subAdminArea = new SubAdministrativeArea();
        subAdminArea.setSubAdministrativeAreaName(new SubAdministrativeAreaName("Los Angeles County"));
        adminArea.setSubAdministrativeArea(subAdminArea);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);

        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails().setCountry(country));
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("Los Angeles County", result.getCounty());
    }

    @Test
    public void testAddressWithCity() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));

        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        locality.setLocalityName(new LocalityName("Los Angeles"));
        adminArea.setLocality(locality);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);

        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails().setCountry(country));
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("Los Angeles", result.getCity());
    }

    @Test
    public void testAddressWithPostalCode() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));

        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(new PostalCodeNumber("90001"));
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);

        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails().setCountry(country));
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("90001", result.getPostalCode());
    }

    @Test
    public void testAddressWithThoroughfare() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));

        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Main Street");
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);

        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails().setCountry(country));
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("Main Street", result.getAddressLine1());
    }

    @Test
    public void testAddressWithMultipleThoroughfare() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));

        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareName1 = new ThoroughfareNameType();
        thoroughfareName1.setContent("Main Street");
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("First Street");
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName1, thoroughfareName2));
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);

        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails().setCountry(country));
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals("Main Street", result.getAddressLine1());
        assertEquals("First Street", result.getAddressLine2());
    }

    @Test
    public void testAddressWithCoordinate() {
        GeoCoordinate coordinate = new GeoCoordinate(34.0522356, -118.2436844);
        GeoAddress geoAddress = new GeoAddress();
        geoAddress.setCoordinate(coordinate);

        when(mockGeoAddress.getCoordinate()).thenReturn(coordinate);
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertNotNull(result.getCoordinate());
        assertEquals(coordinate.getLatitude(), result.getCoordinate().getLatitude(), 0.0001);
        assertEquals(coordinate.getLongitude(), result.getCoordinate().getLongitude(), 0.0001);
    }

    @Test
    public void testAddressWithoutCountry() {
        when(mockGeoAddress.getAddressDetails()).thenReturn(new AddressDetails());
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(mockGeoAddress);
        assertEquals(0, result.getState().length());
    }

    @Test
    public void testAddressWithoutState() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));
        AddressDetails addressDetails = new AddressDetails().setCountry(country);

        when(mockGeoAddress.getAddressDetails()).thenReturn(addressDetails);
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals(0, result.getCounty().length());
    }

    @Test
    public void testAddressWithoutCounty() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);
        AddressDetails addressDetails = new AddressDetails().setCountry(country);

        when(mockGeoAddress.getAddressDetails()).thenReturn(addressDetails);
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals(0, result.getCity().length());
    }

    @Test
    public void testAddressWithoutCity() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        locality.setLocalityName(new LocalityName("Los Angeles"));
        adminArea.setLocality(locality);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);
        AddressDetails addressDetails = new AddressDetails().setCountry(country);

        when(mockGeoAddress.getAddressDetails()).thenReturn(addressDetails);
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals(0, result.getPostalCode().length());
    }

    @Test
    public void testAddressWithoutPostalCode() {
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US"));
        country.setCountryName(new CountryName("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.setAdministrativeArea(adminArea);
        AddressDetails addressDetails = new AddressDetails().setCountry(country);

        when(mockGeoAddress.getAddressDetails()).thenReturn(addressDetails);
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(mockGeoAddress);
        assertEquals(0, result.getAddressLine1().length());
    }
}