package geo.google.mapping;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea.Country;
import oasis.names.tc.ciq.xsdschema.xal._2.AdministrativeArea.SubAdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.Locality;
import oasis.names.tc.ciq.xsdschema.xal._2.ThoroughfareNameType;
import org.junit.Before;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    private AddressDetails addressDetails;
    private GeoAddress geoAddress;
    private GeoUsAddress expectedUsAddress;
    private AddressToUsAddressFunctor functor;

    @Before
    public void setup() {
        addressDetails = new AddressDetails();
        geoAddress = new GeoAddress();
        expectedUsAddress = new GeoUsAddress();
        functor = AddressToUsAddressFunctor.getInstance();

        Country country = new Country();
        country.setCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountryName(new AddressDetails.CountryName("United States"));
        addressDetails.setCountry(country);

        AdministrativeArea adminArea = new AdministrativeArea();
        Locality locality = new Locality();
        locality.setLocalityName(new Locality.LocalityName("New York"));
        locality.setPostalCode(new PostalCode("10001"));
        ThoroughfareNameType thoroughfare = new ThoroughfareNameType("Broadway");
        locality.setThoroughfare(thoroughfare);
        adminArea.setLocality(locality);
        adminArea.setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        addressDetails.setAdministrativeArea(adminArea);

        Coordinate coordinate = new Coordinate(40.7128, -74.0060);
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(coordinate);
        geoAddress.setAccuracy(5.5f);

        expectedUsAddress.setCountry("United States");
        expectedUsAddress.setState("New York");
        expectedUsAddress.setCity("New York");
        expectedUsAddress.setPostalCode("10001");
        expectedUsAddress.setAddressLine1("Broadway");
        expectedUsAddress.setAddressLine2(null);
        expectedUsAddress.setCoordinate(coordinate);
        expectedUsAddress.setAccuracy(5.5f);
    }

    @Test
    public void testConvertValidAddressToUsAddress() {
        when(addressDetails.getCountry()).thenReturn(country);
        when(addressDetails.getAdministrativeArea()).thenReturn(adminArea);
        when(adminArea.getLocality()).thenReturn(locality);

        GeoUsAddress actualUsAddress = functor.execute(geoAddress);

        assertEquals("Country should be set correctly.", expectedUsAddress.getCountry(), actualUsAddress.getCountry());
        assertEquals("State should be set correctly.", expectedUsAddress.getState(), actualUsAddress.getState());
        assertEquals("City should be set correctly.", expectedUsAddress.getCity(), actualUsAddress.getCity());
        assertEquals("PostalCode should be set correctly.", expectedUsAddress.getPostalCode(), actualUsAddress.getPostalCode());
        assertEquals("AddressLine1 should be set correctly.", expectedUsAddress.getAddressLine1(), actualUsAddress.getAddressLine1());
        assertNull("AddressLine2 should be null if not present.", actualUsAddress.getAddressLine2());
        assertArrayEquals("Coordinate should be cloned and set correctly.", expectedUsAddress.getCoordinate().toArray(), actualUsAddress.getCoordinate().toArray());
        assertEquals("Accuracy should be set correctly.", expectedUsAddress.getAccuracy(), actualUsAddress.getAccuracy());
    }
}