package geo.google.mapping;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testEmptyAddress() {
        GeoAddress inputAddress = new GeoAddress();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertNull(outputAddress.getCountry());
        assertNull(outputAddress.getState());
        assertNull(outputAddress.getCity());
        assertNull(outputAddress.getPostalCode());
        assertNull(outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertNull(outputAddress.getAccuracy());
    }

    @Test
    public void testAddressWithCountry() {
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountry(new CountryCity("United States"));
        addressDetails.setCountry(country);
        inputAddress.setAddressDetails(addressDetails);

        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals("United States", outputAddress.getCountry());
        assertNull(outputAddress.getState());
        assertNull(outputAddress.getCity());
        assertNull(outputAddress.getPostalCode());
        assertNull(outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertNull(outputAddress.getAccuracy());
    }

    @Test
    public void testAddressWithCountryState() {
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountry(new CountryCity("United States"));
        AddressDetails.AdministrativeArea adminArea = new AddressDetails.AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("California"));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);

        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals("United States", outputAddress.getCountry());
        assertEquals("California", outputAddress.getState());
        assertNull(outputAddress.getCity());
        assertNull(outputAddress.getPostalCode());
        assertNull(outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertNull(outputAddress.getAccuracy());
    }

    @Test
    public void testAddressWithCountryStateCity() {
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountry(new CountryCity("United States"));
        AddressDetails.AdministrativeArea adminArea = new AddressDetails.AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("California"));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);
        AddressDetails.Locality locality = new AddressDetails.Locality();
        locality.setLocalityName(new Locality.LocalityName("Los Angeles"));
        addressDetails.setLocality(locality);
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber("90001"));
        locality.setPostalCode(postalCode);

        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals("United States", outputAddress.getCountry());
        assertEquals("California", outputAddress.getState());
        assertEquals("Los Angeles", outputAddress.getCity());
        assertEquals("90001", outputAddress.getPostalCode());
        assertNull(outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertNull(outputAddress.getAccuracy());
    }

    @Test
    public void testAddressWithCountryStateCityPostalCodeAndAddressLine1() {
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountry(new CountryCity("United States"));
        AddressDetails.AdministrativeArea adminArea = new AddressDetails.AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("California"));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);
        AddressDetails.Locality locality = new AddressDetails.Locality();
        locality.setLocalityName(new Locality.LocalityName("Los Angeles"));
        addressDetails.setLocality(locality);
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber("90001"));
        locality.setPostalCode(postalCode);
        ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
        thoroughfare.setContent("1600 Amphitheatre Parkway");
        locality.setThoroughfare(new Thoroughfare(thoroughfare));

        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals("United States", outputAddress.getCountry());
        assertEquals("California", outputAddress.getState());
        assertEquals("Los Angeles", outputAddress.getCity());
        assertEquals("90001", outputAddress.getPostalCode());
        assertEquals("1600 Amphitheatre Parkway", outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertNull(outputAddress.getAccuracy());
    }

    @Test
    public void testAddressWithCountryStateCityPostalCodeAddressLine1AndAddressLine2() {
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountry(new CountryCity("United States"));
        AddressDetails.AdministrativeArea adminArea = new AddressDetails.AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("California"));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);
        AddressDetails.Locality locality = new AddressDetails.Locality();
        locality.setLocalityName(new Locality.LocalityName("Los Angeles"));
        addressDetails.setLocality(locality);
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber("90001"));
        locality.setPostalCode(postalCode);
        ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
        thoroughfare1.setContent("1600 Amphitheatre Parkway");
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfare2.setContent("Building 43");
        locality.setThoroughfare(new Thoroughfare(new ThoroughfareName[]{thoroughfare1, thoroughfare2}));

        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals("United States", outputAddress.getCountry());
        assertEquals("California", outputAddress.getState());
        assertEquals("Los Angeles", outputAddress.getCity());
        assertEquals("90001", outputAddress.getPostalCode());
        assertEquals("1600 Amphitheatre Parkway", outputAddress.getAddressLine1());
        assertEquals("Building 43", outputAddress.getAddressLine2());
        assertNull(outputAddress.getCoordinate());
        assertNull(outputAddress.getAccuracy());
    }

    @Test
    public void testAddressWithCountryStateCityPostalCodeAddressLine1AddressLine2AndCoordinate() {
        GeoAddress inputAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountry(new CountryCity("United States"));
        AddressDetails.AdministrativeArea adminArea = new AddressDetails.AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName("California"));
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);
        AddressDetails.Locality locality = new AddressDetails.Locality();
        locality.setLocalityName(new Locality.LocalityName("Los Angeles"));
        addressDetails.setLocality(locality);
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(new PostalCode.PostalCodeNumber("90001"));
        locality.setPostalCode(postalCode);
        ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
        thoroughfare1.setContent("1600 Amphitheatre Parkway");
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfare2.setContent("Building 43");
        locality.setThoroughfare(new Thoroughfare(new ThoroughfareName[]{thoroughfare1, thoroughfare2}));
        Coordinate coordinate = new Coordinate(new LatLon(37.4220111, -122.0840556));
        inputAddress.setAddressDetails(addressDetails);
        inputAddress.setCoordinate(coordinate);

        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);
        assertEquals("United States", outputAddress.getCountry());
        assertEquals("California", outputAddress.getState());
        assertEquals("Los Angeles", outputAddress.getCity());
        assertEquals("90001", outputAddress.getPostalCode());
        assertEquals("1600 Amphitheatre Parkway", outputAddress.getAddressLine1());
        assertEquals("Building 43", outputAddress.getAddressLine2());
        assertArrayEquals(new double[]{37.4220111, -122.0840556}, outputAddress.getCoordinate().getLatLon().toArray(), 0.00001);
        assertNull(outputAddress.getAccuracy());
    }
}