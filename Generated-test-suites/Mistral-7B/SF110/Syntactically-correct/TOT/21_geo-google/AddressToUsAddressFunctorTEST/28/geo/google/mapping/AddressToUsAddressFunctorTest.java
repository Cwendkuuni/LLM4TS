package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecuteWithValidData() {
        // Test case for method execute() when the given GeoAddress object has valid data
        GeoAddress validAddress = new GeoAddress();
        validAddress.setAddressDetails(new AddressDetails());
        validAddress.getAddressDetails().setCountry(new AddressDetails.Country());
        validAddress.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        validAddress.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode().setContent("US"));
        validAddress.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
        validAddress.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
        validAddress.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName().setContent("California"));
        validAddress.getAddressDetails().getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        validAddress.getAddressDetails().getSubAdministrativeArea().setSubAdministrativeAreaName(new ArrayList<>());
        validAddress.getAddressDetails().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().setContent("Los Angeles"));
        validAddress.getAddressDetails().getSubAdministrativeArea().setLocality(new Locality());
        validAddress.getAddressDetails().getSubAdministrativeArea().getLocality().setLocalityName(new ArrayList<>());
        validAddress.getAddressDetails().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName().setContent("Beverly Hills"));
        validAddress.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<>());
        validAddress.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber().setContent("90210"));
        validAddress.getAddressDetails().getLocality().setThoroughfare(new Thoroughfare());
        validAddress.getAddressDetails().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<>());
        validAddress.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().setContent("Rodeo Drive"));
        validAddress.setCoordinate(new Coordinate(34.0522355, -118.2436833));
        validAddress.setAccuracy(10);

        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCountry("US");
        expectedResult.setState("California");
        expectedResult.setCity("Beverly Hills");
        expectedResult.setPostalCode("90210");
        expectedResult.setAddressLine1("Rodeo Drive");
        expectedResult.setAddressLine2("");
        expectedResult.setCoordinate(new Coordinate(34.0522355, -118.2436833));
        expectedResult.setAccuracy(10);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(validAddress);

        // Assert test
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteWithEmptyData() {
        // Test case for method execute() when the given GeoAddress object has empty data
        GeoAddress emptyAddress = new GeoAddress();

        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCountry("");
        expectedResult.setState("");
        expectedResult.setCity("");
        expectedResult.setPostalCode("");
        expectedResult.setAddressLine1("");
        expectedResult.setAddressLine2("");
        expectedResult.setCoordinate(null);
        expectedResult.setAccuracy(0);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(emptyAddress);

        // Assert test
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteWithInvalidData() {
        // Test case for method execute() when the given GeoAddress object has invalid data (missing required fields)
        GeoAddress invalidAddress = new GeoAddress();
        invalidAddress.setAddressDetails(new AddressDetails());
        invalidAddress.getAddressDetails().setCountry(null);

        try {
            AddressToUsAddressFunctor.getInstance().execute(invalidAddress);
            fail("Expected GeoException to be thrown due to missing country data.");
        } catch (GeoException e) {
            // Expected
        }
    }

}