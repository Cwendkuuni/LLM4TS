package geo.google.mapping.test;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.*;
import geo.google.datamodel.*;
import geo.google.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testGetInstance() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        assertNotNull(addressToUsAddressFunctor);
    }

    @Test
    public void testExecuteValidAddress() {
        GeoAddress geoAddress = new GeoAddress();
        geoAddress.setAddressDetails(new AddressDetails());
        AddressDetails addressDetails = geoAddress.getAddressDetails();
        addressDetails.setCountry(new Country());
        Country country = addressDetails.getCountry();
        country.setCountryNameCode(new ArrayList<Country.CountryNameCode>());
        country.getCountryNameCode().add(new Country.CountryNameCode().content("US"));
        AddressDetails.AdministrativeArea administrativeArea = new AddressDetails.AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(new ArrayList<AddressDetails.AdministrativeArea.AdministrativeAreaName>());
        administrativeArea.getAdministrativeAreaName().add(new AddressDetails.AdministrativeArea.AdministrativeAreaName().content("California"));
        country.setAdministrativeArea(administrativeArea);
        GeoUsAddress expectedUsAddress = new GeoUsAddress();
        expectedUsAddress.setCountry("United States");
        expectedUsAddress.setState("California");
        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals(expectedUsAddress, actualUsAddress);
    }

    @Test(expected = GeoException.class)
    public void testExecuteEmptyAddress() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoException exception = assertThrows(GeoException.class, () -> addressToUsAddressFunctor.execute(new GeoAddress()));
        assertEquals("The address is empty.", exception.getMessage());
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingCountry() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoException exception = assertThrows(GeoException.class, () -> addressToUsAddressFunctor.execute(new GeoAddress()));
        assertEquals("The country information is missing.", exception.getMessage());
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingAdminArea() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new ArrayList<Country.CountryNameCode>());
        country.getCountryNameCode().add(new Country.CountryNameCode().content("US"));
        addressDetails.setCountry(country);
        GeoAddress geoAddress = new GeoAddress();
        geoAddress.setAddressDetails(addressDetails);
        GeoException exception = assertThrows(GeoException.class, () -> addressToUsAddressFunctor.execute(geoAddress));
        assertEquals("The administrative area information is missing.", exception.getMessage());
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingLocality() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new ArrayList<Country.CountryNameCode>());
        country.getCountryNameCode().add(new Country.CountryNameCode().content("US"));
        AddressDetails.AdministrativeArea administrativeArea = new AddressDetails.AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(new ArrayList<AddressDetails.AdministrativeArea.AdministrativeAreaName>());
        administrativeArea.getAdministrativeAreaName().add(new AddressDetails.AdministrativeArea.AdministrativeAreaName().content("California"));
        country.setAdministrativeArea(administrativeArea);
        GeoAddress geoAddress = new GeoAddress();
        geoAddress.setAddressDetails(addressDetails);
        GeoException exception = assertThrows(GeoException.class, () -> addressToUsAddressFunctor.execute(geoAddress));
        assertEquals("The locality information is missing.", exception.getMessage());
    }

    @BeforeAll
    static void setup() {
        AddressToUsAddressFunctor.getInstance();
    }

    @AfterAll
    static void teardown() {
        // No cleanup needed
    }
}