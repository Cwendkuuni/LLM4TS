package geo.google.mapping;

import static org.junit.Assert.*;
import org.junit.Test;

import geo.google.datamodel.*;
import java.util.Collections;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_ValidAddressData_ReturnsPopulatedUsAddressObject() {
        GeoAddress inputAddress = createValidGeoAddress();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNotNull(outputAddress);
        assertEquals("CountryName", outputAddress.getCountry());
        assertEquals("StateName", outputAddress.getState());
        assertEquals("CountyName", outputAddress.getCounty());
        assertEquals("CityName", outputAddress.getCity());
        assertEquals("PostalCodeNumber", outputAddress.getPostalCode());
        assertEquals("AddressLine1", outputAddress.getAddressLine1());
        assertEquals("AddressLine2", outputAddress.getAddressLine2());
        assertNotNull(outputAddress.getCoordinate());
        assertEquals(inputAddress.getAccuracy(), outputAddress.getAccuracy());
    }

    @Test
    public void testExecute_NullAddress_ReturnsNullUsAddress() {
        GeoAddress inputAddress = null;
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertNull(outputAddress);
    }

    @Test
    public void testExecute_EmptyAddress_ReturnsEmptyUsAddress() {
        GeoAddress inputAddress = new GeoAddress();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertNotNull(outputAddress);
        assertEquals(emptyString(), outputAddress.getCountry());
        assertEquals(emptyString(), outputAddress.getState());
        assertEquals(emptyString(), outputAddress.getCounty());
        assertEquals(emptyString(), outputAddress.getCity());
        assertEquals(emptyString(), outputAddress.getPostalCode());
        assertEquals(emptyString(), outputAddress.getAddressLine1());
        assertEquals(emptyString(), outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertEquals(GeoAddress.ACCURACY_LOW, outputAddress.getAccuracy());
    }

    @Test
    public void testExecute_MissingCountryData_SetsEmptyCountry() {
        AddressDetails country = new AddressDetails();
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAddressDetails(country);
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals(emptyString(), outputAddress.getCountry());
    }

    @Test
    public void testExecute_MissingStateData_SetsEmptyState() {
        AddressDetails country = new AddressDetails();
        country.setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode("CountryCode", "CountryName")));
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAddressDetails(country);
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals(emptyString(), outputAddress.getState());
    }

    @Test
    public void testExecute_MissingCountyData_SetsEmptyCounty() {
        AddressDetails country = new AddressDetails();
        country.setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode("CountryCode", "CountryName")));
        country.setAdministrativeArea(new AdministrativeArea());
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAddressDetails(country);
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals(emptyString(), outputAddress.getCounty());
    }

    @Test
    public void testExecute_MissingCityData_SetsEmptyCity() {
        AddressDetails country = new AddressDetails();
        country.setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode("CountryCode", "CountryName")));
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName("StateName", "StateCode")));
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAddressDetails(country);
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals(emptyString(), outputAddress.getCity());
    }

    @Test
    public void testExecute_MissingPostalCodeData_SetsEmptyPostalCode() {
        AddressDetails country = new AddressDetails();
        country.setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode("CountryCode", "CountryName")));
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName("StateName", "StateCode")));
        country.getAdministrativeArea().setLocality(new Locality());
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAddressDetails(country);
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals(emptyString(), outputAddress.getPostalCode());
    }

    private GeoAddress createValidGeoAddress() {
        AddressDetails country = new AddressDetails();
        country.setCountryNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode("CountryCode", "CountryName")));
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName("StateName", "StateCode")));
        country.getAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getLocality().setLocalityName(Collections.singletonList(new Locality.LocalityName("CityName", "CityCode")));
        country.getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(Collections.singletonList(new PostalCode.PostalCodeNumber("PostalCodeNumber", "PostalCode")));
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(country);
        address.setCoordinate(new Coordinate(1.0, 2.0, 3.0));
        return address;
    }

    private String emptyString() {
        return "";
    }
}