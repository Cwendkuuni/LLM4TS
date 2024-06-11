package geo.google.mapping;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_ValidAddressWithCountry_UsAddressCreated() {
        GeoAddress inputAddress = createSampleGeoAddress();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertEquals("US", outputAddress.getCountry());
        assertEquals("CA", outputAddress.getState());
        assertEquals("City", outputAddress.getCity());
        assertEquals("12345", outputAddress.getPostalCode());
        assertEquals("123 Main St", outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNotNull(outputAddress.getCoordinate());
        assertEquals(inputAddress.getAccuracy(), outputAddress.getAccuracy());
    }

    @Test
    public void testExecute_ValidAddressWithCountryAndExtraFields_UsAddressCreatedWithAllFields() {
        GeoAddress inputAddress = createSampleGeoAddressWithExtraFields();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertEquals("US", outputAddress.getCountry());
        assertEquals("CA", outputAddress.getState());
        assertEquals("City", outputAddress.getCity());
        assertEquals("12345", outputAddress.getPostalCode());
        assertEquals("123 Main St", outputAddress.getAddressLine1());
        assertEquals("456 Elm St", outputAddress.getAddressLine2());
        assertNotNull(outputAddress.getCoordinate());
        assertEquals(inputAddress.getAccuracy(), outputAddress.getAccuracy());
    }

    @Test
    public void testExecute_NullAddress_NullOutput() {
        GeoAddress inputAddress = null;
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithEmptyCountry_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptyCountry();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithoutCountry_NullOutput() {
        GeoAddress inputAddress = createAddressWithoutCountry();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithEmptyCountryAndOtherFields_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptyCountryAndOtherFields();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithoutCountryAndOtherFields_NullOutput() {
        GeoAddress inputAddress = createAddressWithoutCountryAndOtherFields();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithSingleCountryNameCode_UsAddressCreated() {
        GeoAddress inputAddress = createAddressWithSingleCountryNameCode();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertEquals("US", outputAddress.getCountry());
        assertNull(outputAddress.getState());
        assertNull(outputAddress.getCity());
        assertNull(outputAddress.getPostalCode());
        assertNull(outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNotNull(outputAddress.getCoordinate());
        assertEquals(inputAddress.getAccuracy(), outputAddress.getAccuracy());
    }

    @Test
    public void testExecute_AddressWithMultipleCountryNameCodes_FirstOneIsUsed() {
        GeoAddress inputAddress = createAddressWithMultipleCountryNameCodes();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertEquals("US", outputAddress.getCountry());
        assertNull(outputAddress.getState());
        assertNull(outputAddress.getCity());
        assertNull(outputAddress.getPostalCode());
        assertNull(outputAddress.getAddressLine1());
        assertNull(outputAddress.getAddressLine2());
        assertNotNull(outputAddress.getCoordinate());
        assertEquals(inputAddress.getAccuracy(), outputAddress.getAccuracy());
    }

    @Test
    public void testExecute_AddressWithEmptyCountryNameCodes_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptyCountryNameCodes();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithNoCountryNameCodes_NullOutput() {
        GeoAddress inputAddress = createAddressWithNoCountryNameCodes();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithEmptyCountryAdminArea_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptyCountryAdminArea();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithNoCountryAdminArea_NullOutput() {
        GeoAddress inputAddress = createAddressWithNoCountryAdminArea();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithEmptySubAdminArea_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptySubAdminArea();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithNoSubAdminArea_NullOutput() {
        GeoAddress inputAddress = createAddressWithNoSubAdminArea();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithEmptyLocality_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptyLocality();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithNoLocality_NullOutput() {
        GeoAddress inputAddress = createAddressWithNoLocality();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithEmptyThoroughfareName_NullOutput() {
        GeoAddress inputAddress = createAddressWithEmptyThoroughfareName();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    @Test
    public void testExecute_AddressWithNoThoroughfareName_NullOutput() {
        GeoAddress inputAddress = createAddressWithNoThoroughfareName();
        GeoUsAddress outputAddress = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        assertNull(outputAddress);
    }

    private GeoAddress createSampleGeoAddress() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(1.0d);
        coordinate.setLongitude(2.0d);
        address.setCoordinate(coordinate);
        return address;
    }

    private GeoAddress createSampleGeoAddressWithExtraFields() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setName("AdminArea");
        addressDetails.setAdministrativeArea(adminArea);
        address.setAddressDetails(addressDetails);
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(1.0d);
        coordinate.setLongitude(2.0d);
        address.setCoordinate(coordinate);
        return address;
    }

    private GeoAddress createAddressWithEmptyCountry() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithoutCountry() {
        GeoAddress address = new GeoAddress();
        Coordinate coordinate = new Coordinate();
        address.setCoordinate(coordinate);
        return address;
    }

    private GeoAddress createAddressWithEmptyCountryAndOtherFields() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        AdministrativeArea adminArea = new AdministrativeArea();
        addressDetails.setAdministrativeArea(adminArea);
        Coordinate coordinate = new Coordinate();
        address.setCoordinate(coordinate);
        return address;
    }

    private GeoAddress createAddressWithoutCountryAndOtherFields() {
        GeoAddress address = new GeoAddress();
        Coordinate coordinate = new Coordinate();
        address.setCoordinate(coordinate);
        return address;
    }

    private GeoAddress createAddressWithSingleCountryNameCode() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithMultipleCountryNameCodes() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode().content("US"),
                new AddressDetails.Country.CountryNameCode().content("CA")));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithEmptyCountryNameCodes() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithNoCountryNameCodes() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithEmptyCountryAdminArea() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithNoCountryAdminArea() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithEmptySubAdminArea() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        AdministrativeArea adminArea = new AdministrativeArea();
        addressDetails.setAdministrativeArea(adminArea);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithNoSubAdminArea() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithEmptyLocality() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        AdministrativeArea adminArea = new AdministrativeArea();
        addressDetails.setAdministrativeArea(adminArea);
        Locality locality = new Locality();
        addressDetails.setLocality(locality);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithNoLocality() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithEmptyThoroughfareName() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        AdministrativeArea adminArea = new AdministrativeArea();
        addressDetails.setAdministrativeArea(adminArea);
        Locality locality = new Locality();
        addressDetails.setLocality(locality);
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        address.setAddressDetails(addressDetails);
        return address;
    }

    private GeoAddress createAddressWithNoThoroughfareName() {
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setName("Country");
        country.setNameCode(Collections.singletonList(new AddressDetails.Country.CountryNameCode().content("US")));
        addressDetails.setCountry(country);
        AddressDetails administrativeArea = new AddressDetails();
        addressDetails.setAdministrativeArea(administrativeArea);
        AddressDetails locality = new AddressDetails();
        addressDetails.setLocality(locality);
        address.setAddressDetails(addressDetails);
        return address;
    }
}