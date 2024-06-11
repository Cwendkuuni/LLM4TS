package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.AddressToUsAddressFunctor.GeoUsAddress;
import geo.google.mapping.AddressToUsAddressFunctor.GeoAddress;
import geo.google.mapping.AddressToUsAddressFunctor.GeoException;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.AdministrativeAreaName;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.SubAdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.SubAdministrativeAreaName;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Locality;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Locality.LocalityName;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Thoroughfare;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.ThoroughfareNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode.PostalCodeNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AddressToUsAddressFunctor.class)
public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_ValidAddress_ReturnsUsAddress() throws Exception {
        // Given
        GeoAddress address = createAddress();
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertNotNull(usAddress.getCountry());
        assertNotNull(usAddress.getState());
        assertNotNull(usAddress.getCity());
        assertNotNull(usAddress.getPostalCode());
        assertNotNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertNotNull(usAddress.getAccuracy());
    }

    @Test
    public void testExecute_NullAddress_ReturnsNull() throws Exception {
        // Given
        GeoAddress address = null;
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNull(usAddress);
    }

    @Test
    public void testExecute_EmptyAddress_ReturnsEmptyUsAddress() throws Exception {
        // Given
        GeoAddress address = new GeoAddress();
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertEquals("", usAddress.getCountry());
        assertEquals("", usAddress.getState());
        assertEquals("", usAddress.getCity());
        assertEquals("", usAddress.getPostalCode());
        assertEquals("", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNull(usAddress.getCoordinate());
        assertEquals(0.0, usAddress.getAccuracy(), 0.01);
    }

    @Test
    public void testExecute_AddressWithOnlyCountry_ReturnsUsAddressWithStateAndCityFromCountry() throws Exception {
        // Given
        Country country = new Country();
        country.setCountryCode("US");
        country.setCountryName(new CountryNameType("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        address.getAddressDetails().setCountry(country);
        address.getAddressDetails().setAdministrativeArea(adminArea);
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertEquals("California", usAddress.getState());
        assertEquals("", usAddress.getCity());
        assertEquals("", usAddress.getCounty());
        assertEquals("", usAddress.getPostalCode());
        assertEquals("", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertNotNull(usAddress.getAccuracy());
    }

    @Test
    public void testExecute_AddressWithCountryAndAdminArea_ReturnsUsAddressWithStateCityAndCounty() throws Exception {
        // Given
        Country country = new Country();
        country.setCountryCode("US");
        country.setCountryName(new CountryNameType("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        SubAdministrativeArea subAdminArea = new SubAdministrativeArea();
        subAdminArea.setSubAdministrativeAreaName(new SubAdministrativeAreaName("San Francisco"));
        Locality locality = new Locality();
        locality.setLocalityName(new LocalityName("The City"));
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType("Market Street");
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
        locality.setThoroughfare(thoroughfare);
        PostalCode postalCode = new PostalCode();
        PostalCodeNumber postalCodeNumber = new PostalCodeNumber("94102");
        postalCode.setPostalCodeNumber(Arrays.asList(postalCodeNumber));
        adminArea.setLocality(locality);
        adminArea.setSubAdministrativeArea(subAdminArea);
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        address.getAddressDetails().setCountry(country);
        address.getAddressDetails().setAdministrativeArea(adminArea);
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertEquals("California", usAddress.getState());
        assertEquals("The City", usAddress.getCity());
        assertEquals("San Francisco", usAddress.getCounty());
        assertEquals("94102", usAddress.getPostalCode());
        assertEquals("Market Street", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertNotNull(usAddress.getAccuracy());
    }

    @Test
    public void testExecute_AddressWithCountryAdminAreaAndThoroughfare_ReturnsUsAddressWithStateCityAndAddressLine1() throws Exception {
        // Given
        Country country = new Country();
        country.setCountryCode("US");
        country.setCountryName(new CountryNameType("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        address.getAddressDetails().setCountry(country);
        address.getAddressDetails().setAdministrativeArea(adminArea);
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType("Market Street");
        Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
        address.getAddressDetails().setThoroughfare(thoroughfare);
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertEquals("California", usAddress.getState());
        assertEquals("", usAddress.getCity());
        assertEquals("", usAddress.getCounty());
        assertEquals("", usAddress.getPostalCode());
        assertEquals("Market Street", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertNotNull(usAddress.getAccuracy());
    }

    @Test
    public void testExecute_AddressWithCountryAdminAreaAndThoroughfareAndPostalCode_ReturnsUsAddressWithStateCityAddressLine1AndPostalCode() throws Exception {
        // Given
        Country country = new Country();
        country.setCountryCode("US");
        country.setCountryName(new CountryNameType("United States"));
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaName("California"));
        SubAdministrativeArea subAdminArea = new SubAdministrativeArea();
        subAdminArea.setSubAdministrativeAreaName(new SubAdministrativeAreaName("San Francisco"));
        Locality locality = new Locality();
        locality.setLocalityName(new LocalityName("The City"));
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType("Market Street");
        Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName));
        locality.setThoroughfare(thoroughfare);
        PostalCode postalCode = new PostalCode();
        PostalCodeNumber postalCodeNumber = new PostalCodeNumber("94102");
        postalCode.setPostalCodeNumber(Arrays.asList(postalCodeNumber));
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        adminArea.setSubAdministrativeArea(subAdminArea);
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        address.getAddressDetails().setCountry(country);
        address.getAddressDetails().setAdministrativeArea(adminArea);
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // When
        GeoUsAddress usAddress = functor.execute(address);

        // Then
        assertNotNull(usAddress);
        assertEquals("California", usAddress.getState());
        assertEquals("The City", usAddress.getCity());
        assertEquals("San Francisco", usAddress.getCounty());
        assertEquals("94102", usAddress.getPostalCode());
        assertEquals("Market Street", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertNotNull(usAddress.getAccuracy());
    }

    private GeoAddress createAddress() {
        Country country = PowerMockito.mock(Country.class);
        AddressDetails addressDetails = PowerMockito.mock(AddressDetails.class);
        PowerMockito.whenNew(Country.class).withAnyArgs().thenReturn(country);
        PowerMockito.when(country.getCountryCode()).thenReturn("US");
        PowerMockito.when(country.getCountryName()).thenReturn(new CountryNameType("United States"));
        AddressDetails.AdministrativeArea administrativeArea = PowerMockito.mock(AddressDetails.AdministrativeArea.class);
        PowerMockito.whenNew(AddressDetails.AdministrativeArea.class).withAnyArgs().thenReturn(administrativeArea);
        PowerMockito.when(administrativeArea.getAdministrativeAreaName()).thenReturn(new AdministrativeAreaName("California"));
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(addressDetails);
        return address;
    }
}