package geo.google.mapping;

import org.junit.*;
import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    @BeforeClass
    public static void setup() {
        // Initialization if needed
    }

    @AfterClass
    public static void teardown() {
        // Teardown if needed
    }

    @Before
    public void beforeEachTest() {
        // Setup before each test
    }

    @After
    public void afterEachTest() {
        // Teardown after each test
    }

    @Test
    public void testExecute_ValidGeoAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US", "United States"));
        country.setCountryName(new CountryName("United States"));
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeAreaName("California"));
        country.getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new SubAdministrativeAreaName("Los Angeles"));
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(new LocalityName("Beverly Hills"));
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode("90210"));
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ThoroughfareNameType("Rodeo Drive"));
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new GeoCoordinate(34.0522355, -118.3654515));
        geoAddress.setAccuracy(50);

        // Act
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(geoAddress);

        // Assert
        assertEquals("United States", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Beverly Hills", result.getCity());
        assertEquals("90210", result.getPostalCode());
        assertEquals("Rodeo Drive", result.getAddressLine1());

        // Test for addressLine2
        assertNull(result.getAddressLine2());

        // Assert that other fields are correctly set
        assertEquals(geoAddress.getCoordinate(), result.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), result.getAccuracy());
    }

    @Test
    public void testExecute_EmptyGeoAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        geoAddress.setAddressDetails(new AddressDetails());
        geoAddress.setCoordinate(new GeoCoordinate(37.7749, -122.4194));
        geoAddress.setAccuracy(55);

        // Act
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(geoAddress);

        // Assert
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine2());

        // Assert that other fields are correctly set
        assertEquals(geoAddress.getCoordinate(), result.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), result.getAccuracy());
    }

    @Test(expected = GeoException.class)
    public void testExecute_InvalidGeoAddress() {
        // Arrange
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        country.setCountryNameCode(new CountryNameCode("US", "United States"));
        country.setCountryName(new CountryName("United States"));
        addressDetails.setCountry(country);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(null);
        geoAddress.setAccuracy(50);

        // Act and Assert
        AddressToUsAddressFunctor.getInstance().execute(geoAddress);
    }

    @After
    public void teardown() {
        // Teardown if needed
    }
}