package geo.google.mapping.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Cloneable;
import org.junit.Test;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

public final class AddressToUsAddressFunctorTest {
    @Test
    public void testExecute_ValidAddress_ReturnsUsAddress() {
        GeoAddress inputAddress = new GeoAddress();
        inputAddress.setAddressDetails(new AddressDetails());
        AddressDetails addressDetails = inputAddress.getAddressDetails();
        addressDetails.setCountry(new Country());
        Country country = addressDetails.getCountry();
        List<Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(new Country.CountryNameCode());
        country.setCountryNameCode(countryNames);
        countryNames.get(0).setContent("US");
        List<AdministrativeArea> administrativeAreas = new ArrayList<>();
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setAdministrativeAreaName(new AdministrativeAreaName());
        administrativeArea.setAdministrativeAreaName(administrativeArea.getAdministrativeAreaName()).setContent("California");
        List<Locality> localities = new ArrayList<>();
        Locality locality = new Locality();
        locality.setLocalityName(new LocalityName());
        locality.setLocalityName(locality.getLocalityName()).setContent("San Francisco");
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
        thoroughfare.setContent("1600 Market St");
        thoroughfares.add(thoroughfare);
        locality.setThoroughfare(new Thoroughfare());
        locality.getThoroughfare().setThoroughfareName(thoroughfares);
        administrativeArea.setLocality(locality);
        administrativeAreas.add(administrativeArea);
        country.setAdministrativeArea(administrativeArea);
        addressDetails.setAdministrativeArea(administrativeAreas);
        inputAddress.setCoordinate(new Coordinate());
        inputAddress.getCoordinate().setLatitude(37.7749295);
        inputAddress.getCoordinate().setLongitude(-122.4194155);
        GeoUsAddress expectedOutput = new GeoUsAddress();
        expectedOutput.setCountry("US");
        expectedOutput.setState("California");
        expectedOutput.setCity("San Francisco");
        expectedOutput.setPostalCode("1"); // Assuming the first postal code number is used
        expectedOutput.setAddressLine1("1600 Market St");
        expectedOutput.setAddressLine2("2"); // Assuming the second thoroughfare name is used as the second address line
        expectedOutput.setCoordinate((Cloneable) inputAddress.getCoordinate().clone());
        expectedOutput.setAccuracy(inputAddress.getAccuracy());

        GeoUsAddress actualOutput = AddressToUsAddressFunctor.getInstance().execute(inputAddress);

        Assert.assertEquals(expectedOutput.getCountry(), actualOutput.getCountry());
        Assert.assertEquals(expectedOutput.getState(), actualOutput.getState());
        Assert.assertEquals(expectedOutput.getCity(), actualOutput.getCity());
        Assert.assertEquals(expectedOutput.getPostalCode(), actualOutput.getPostalCode());
        Assert.assertEquals(expectedOutput.getAddressLine1(), actualOutput.getAddressLine1());
        Assert.assertEquals(expectedOutput.getAddressLine2(), actualOutput.getAddressLine2());
        Assert.assertEquals(expectedOutput.getCoordinate(), actualOutput.getCoordinate());
        Assert.assertEquals(expectedOutput.getAccuracy(), actualOutput.getAccuracy());
    }

    @Test
    public void testGetInstance_ReturnsSameInstance() {
        AddressToUsAddressFunctor instance1 = AddressToUsAddressFunctor.getInstance();
        AddressToUsAddressFunctor instance2 = AddressToUsAddressFunctor.getInstance();

        Assert.assertSame(instance1, instance2);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_PrivateConstructor_ThrowsIllegalStateException() {
        try {
            new AddressToUsAddressFunctor();
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    // Add more test cases for edge cases and exception handling scenarios here
}