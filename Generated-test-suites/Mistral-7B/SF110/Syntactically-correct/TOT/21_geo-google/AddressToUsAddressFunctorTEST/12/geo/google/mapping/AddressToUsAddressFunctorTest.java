import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.ListUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecuteValidAddress() {
        // Arrange
        GeoAddress validAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = ListUtils.list(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeAreaName> adminAreaNames = ListUtils.list(new AdministrativeAreaName("New York"));
        adminArea.setAdministrativeAreaName(adminAreaNames);
        addressDetails.setAdministrativeArea(adminArea);
        Locality locality = new Locality();
        List<LocalityName> localityNames = ListUtils.list(new LocalityName("New York City"));
        locality.setLocalityName(localityNames);
        locality.setPostalCode(new PostalCode());
        List<PostalCodeNumber> postalCodeNumbers = ListUtils.list(new PostalCodeNumber("10001"));
        locality.getPostalCode().setPostalCodeNumber(postalCodeNumbers);
        addressDetails.setLocality(locality);
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfareNames = ListUtils.list(new ThoroughfareNameType("Main Street"));
        thoroughfare.setThoroughfareName(thoroughfareNames);
        locality.setThoroughfare(thoroughfare);
        validAddress.setAddressDetails(addressDetails);
        GeoCoordinate coordinate = new GeoCoordinate();
        validAddress.setCoordinate(coordinate);
        validAddress.setAccuracy(5);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act
        GeoUsAddress result = functor.execute(validAddress);

        // Assert
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("US");
        expected.setState("New York");
        expected.setCity("New York City");
        expected.setPostalCode("10001");
        expected.setAddressLine1("Main Street");
        expected.setAddressLine2(null);
        expected.setCoordinate(coordinate);
        expected.setAccuracy(5);
        assertEquals(expected, result);

        // Verify mocks
        verify(country, times(1)).getCountryNameCode();
        verify(adminArea, times(1)).getAdministrativeAreaName();
        verify(locality, times(1)).getLocalityName();
        verify(postalCode, times(1)).getPostalCodeNumber();
        verify(thoroughfare, times(1)).getThoroughfareName();
    }

    @Test
    public void testConstructor() {
        // Arrange

        // Act
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Assert
        assertNotNull(functor);
    }

    @Test
    public void testGetInstance() {
        // Arrange

        // Act
        AddressToUsAddressFunctor functor1 = AddressToUsAddressFunctor.getInstance();
        AddressToUsAddressFunctor functor2 = AddressToUsAddressFunctor.getInstance();

        // Assert
        assertEquals(functor1, functor2);
    }

    @Test
    public void testExecuteEmptyAddress() {
        // Arrange
        GeoAddress emptyAddress = new GeoAddress();
        emptyAddress.setAddressDetails(null);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act
        GeoUsAddress result = functor.execute(emptyAddress);

        // Assert
        GeoUsAddress expected = new GeoUsAddress();
        assertNull(expected.getCountry());
        assertNull(expected.getState());
        assertNull(expected.getCity());
        assertNull(expected.getPostalCode());
        assertNull(expected.getAddressLine1());
        assertNull(expected.getAddressLine2());
        assertNull(expected.getCoordinate());
        assertEquals(0, expected.getAccuracy());
        assertEquals(result, expected);
    }

    @Test(expected = GeoException.class)
    public void testExecuteInvalidCountry() {
        // Arrange
        GeoAddress invalidAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = ListUtils.list(new AddressDetails.Country.CountryNameCode("CA"));
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        invalidAddress.setAddressDetails(addressDetails);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act and Assert
        functor.execute(invalidAddress);
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingCountry() {
        // Arrange
        GeoAddress missingCountryAddress = new GeoAddress();
        missingCountryAddress.setAddressDetails(null);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act and Assert
        functor.execute(missingCountryAddress);
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingAdminArea() {
        // Arrange
        GeoAddress missingAdminAreaAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = ListUtils.list(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(null);
        missingAdminAreaAddress.setAddressDetails(addressDetails);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act and Assert
        functor.execute(missingAdminAreaAddress);
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingLocality() {
        // Arrange
        GeoAddress missingLocalityAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = ListUtils.list(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountryNameCode(countryNames);
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeAreaName> adminAreaNames = ListUtils.list(new AdministrativeAreaName("New York"));
        adminArea.setAdministrativeAreaName(adminAreaNames);
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);
        addressDetails.setLocality(null);
        missingLocalityAddress.setAddressDetails(addressDetails);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act and Assert
        functor.execute(missingLocalityAddress);
    }

    @Test(expected = GeoException.class)
    public void testExecuteMissingPostalCode() {
        // Arrange
        GeoAddress missingPostalCodeAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        Country country = new Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = ListUtils.list(new AddressDetails.Country.CountryNameCode("US"));
        country.setCountryNameCode(countryNames);
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeAreaName> adminAreaNames = ListUtils.list(new AdministrativeAreaName("New York"));
        adminArea.setAdministrativeAreaName(adminAreaNames);
        Locality locality = new Locality();
        List<LocalityName> localityNames = ListUtils.list(new LocalityName("New York City"));
        locality.setLocalityName(localityNames);
        locality.setPostalCode(null);
        addressDetails.setCountry(country);
        addressDetails.setAdministrativeArea(adminArea);
        addressDetails.setLocality(locality);
        missingPostalCodeAddress.setAddressDetails(addressDetails);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Act and Assert
        functor.execute(missingPostalCodeAddress);
    }

}