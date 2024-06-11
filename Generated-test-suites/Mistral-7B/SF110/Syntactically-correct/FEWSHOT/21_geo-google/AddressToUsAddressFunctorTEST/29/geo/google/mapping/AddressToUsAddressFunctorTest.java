package geo.google.mapping;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testEmptyAddress() {
        GeoAddress emptyAddress = new GeoAddress();
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(emptyAddress);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithCountryName() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        address.setAddressDetails(country);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithStateName() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        AddressDetails adminArea = new AddressDetails();
        adminArea.setAdministrativeArea(new AdministrativeArea());
        adminArea.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        adminArea.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        country.setAdministrativeArea(adminArea);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithCountyName() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        AddressDetails adminArea = new AddressDetails();
        adminArea.setAdministrativeArea(new AdministrativeArea());
        adminArea.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        adminArea.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        AddressDetails subAdminArea = new AddressDetails();
        subAdminArea.setSubAdministrativeArea(new AdministrativeArea());
        subAdminArea.getSubAdministrativeArea().setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeAreaName());
        subAdminArea.getSubAdministrativeArea().getSubAdministrativeAreaName().setContent("Los Angeles");
        adminArea.setSubAdministrativeArea(subAdminArea);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        assertNotNull(result.getCounty());
        assertEquals("Los Angeles", result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithCityName() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        AddressDetails adminArea = new AddressDetails();
        adminArea.setAdministrativeArea(new AdministrativeArea());
        adminArea.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        adminArea.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        AddressDetails locality = new AddressDetails();
        locality.setLocality(new Locality());
        locality.getLocality().setLocalityName(new Locality.LocalityName());
        locality.getLocality().getLocalityName().setContent("Los Angeles");
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(new PostalCode.PostalCodeNumber());
        locality.getPostalCode().getPostalCodeNumber().setContent("90001");
        adminArea.setLocality(locality);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNotNull(result.getCity());
        assertEquals("Los Angeles", result.getCity());
        assertNotNull(result.getPostalCode());
        assertEquals("90001", result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithPostalCode() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        AddressDetails adminArea = new AddressDetails();
        adminArea.setAdministrativeArea(new AdministrativeArea());
        adminArea.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        adminArea.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        AddressDetails locality = new AddressDetails();
        locality.setLocality(new Locality());
        locality.getLocality().setLocalityName(new Locality.LocalityName());
        locality.getLocality().getLocalityName().setContent("Los Angeles");
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(new PostalCode.PostalCodeNumber());
        locality.getPostalCode().getPostalCodeNumber().setContent("90001");
        adminArea.setLocality(locality);
        address.setAddressDetails(adminArea);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNotNull(result.getPostalCode());
        assertEquals("90001", result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithThoroughfareName() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        AddressDetails adminArea = new AddressDetails();
        adminArea.setAdministrativeArea(new AdministrativeArea());
        adminArea.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        adminArea.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        AddressDetails locality = new AddressDetails();
        locality.setLocality(new Locality());
        locality.getLocality().setLocalityName(new Locality.LocalityName());
        locality.getLocality().getLocalityName().setContent("Los Angeles");
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(new PostalCode.PostalCodeNumber());
        locality.getPostalCode().getPostalCodeNumber().setContent("90001");
        AddressDetails thoroughfare = new AddressDetails();
        thoroughfare.setThoroughfare(new Thoroughfare());
        thoroughfare.getThoroughfare().setThoroughfareName(new ThoroughfareNameType());
        thoroughfare.getThoroughfare().getThoroughfareName().setContent("Main Street");
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        address.setAddressDetails(adminArea);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNotNull(result.getAddressLine1());
        assertEquals("Main Street", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getPostalCode());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithMultipleThoroughfareName() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        AddressDetails adminArea = new AddressDetails();
        adminArea.setAdministrativeArea(new AdministrativeArea());
        adminArea.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        adminArea.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        AddressDetails locality = new AddressDetails();
        locality.setLocality(new Locality());
        locality.getLocality().setLocalityName(new Locality.LocalityName());
        locality.getLocality().getLocalityName().setContent("Los Angeles");
        locality.setPostalCode(new PostalCode());
        locality.getPostalCode().setPostalCodeNumber(new PostalCode.PostalCodeNumber());
        locality.getPostalCode().getPostalCodeNumber().setContent("90001");
        AddressDetails thoroughfare1 = new AddressDetails();
        thoroughfare1.setThoroughfare(new Thoroughfare());
        thoroughfare1.getThoroughfare().setThoroughfareName(new ThoroughfareNameType());
        thoroughfare1.getThoroughfare().getThoroughfareName().setContent("Main Street");
        AddressDetails thoroughfare2 = new AddressDetails();
        thoroughfare2.setThoroughfare(new Thoroughfare());
        thoroughfare2.getThoroughfare().setThoroughfareName(new ThoroughfareNameType());
        thoroughfare2.getThoroughfare().getThoroughfareName().setContent("Second Street");
        locality.setThoroughfare(new ArrayList<AddressDetails>());
        locality.getThoroughfare().add(thoroughfare1);
        locality.getThoroughfare().add(thoroughfare2);
        adminArea.setLocality(locality);
        address.setAddressDetails(adminArea);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNotNull(result.getAddressLine1());
        assertEquals("Main Street", result.getAddressLine1());
        assertNotNull(result.getAddressLine2());
        assertEquals("Second Street", result.getAddressLine2());
        assertNull(result.getPostalCode());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }

    @Test
    public void testValidAddressWithCoordinate() {
        GeoAddress address = new GeoAddress();
        AddressDetails country = new AddressDetails();
        country.setCountry(new AddressDetails.Country());
        country.getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode());
        country.getCountry().getCountryNameCode().setContent("US");
        GeoCoordinate coordinate = new GeoCoordinate();
        coordinate.setLatitude(12.3456);
        coordinate.setLongitude(78.9012);
        address.setAddressDetails(country);
        address.setCoordinate(coordinate);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertNotNull(result.getCountry());
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertNotNull(result.getCoordinate().getLatitude());
        assertEquals(12.3456, result.getCoordinate().getLatitude());
        assertNotNull(result.getCoordinate().getLongitude());
        assertEquals(78.9012, result.getCoordinate().getLongitude());
        assertNull(result.getAccuracy());
    }
}