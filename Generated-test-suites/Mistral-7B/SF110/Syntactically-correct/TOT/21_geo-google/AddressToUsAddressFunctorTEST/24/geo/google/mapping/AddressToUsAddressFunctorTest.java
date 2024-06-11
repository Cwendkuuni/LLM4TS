// Test Case for execute method with valid GeoAddress
package geo.google.mapping;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AddressToUsAddressFunctorTest {
    private AddressDetails validAddressDetails;
    private GeoAddress validGeoAddress;
    private AddressToUsAddressFunctor functor;

    @Before
    public void setup() {
        validAddressDetails = new AddressDetails();
        final List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(new AddressDetails.Country.CountryNameCode().content("US"));
        validAddressDetails.setCountry(new AddressDetails.Country().countryNameCode(countryNames));

        final AdministrativeArea adminArea = new AdministrativeArea();
        final List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        names.add(new AdministrativeArea.AdministrativeAreaName().content("California"));
        adminArea.setAdministrativeAreaName(names);

        final AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        final List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        subNames.add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco"));
        subAdminArea.setSubAdministrativeAreaName(subNames);

        validAddressDetails.setAdministrativeArea(adminArea);
        validAddressDetails.setSubAdministrativeArea(subAdminArea);

        final Locality locality = new Locality();
        final List<Locality.LocalityName> localNames = new ArrayList<>();
        localNames.add(new Locality.LocalityName().content("San Francisco"));
        locality.setLocalityName(localNames);

        final PostalCode postalCode = new PostalCode();
        final List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        numbers.add(new PostalCode.PostalCodeNumber().content("94111"));
        postalCode.setPostalCodeNumber(numbers);

        locality.setPostalCode(postalCode);

        final Thoroughfare thoroughfare = new Thoroughfare();
        final List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        thoroughfares.add(new ThoroughfareNameType().content("Market Street"));
        thoroughfare.setThoroughfareName(thoroughfares);

        locality.setThoroughfare(thoroughfare);

        validGeoAddress = new GeoAddress();
        validGeoAddress.setAddressDetails(validAddressDetails);
        validGeoAddress.setCoordinate(new Coordinate(37.7749, -122.4194));
        validGeoAddress.setAccuracy(15);

        functor = AddressToUsAddressFunctor.getInstance();
        initMocks(functor);
    }

    @Test
    public void testExecuteWithValidGeoAddress() {
        GeoUsAddress expectedResult = new GeoUsAddress();
        expectedResult.setCountry("US");
        expectedResult.setState("California");
        expectedResult.setCity("San Francisco");
        expectedResult.setCounty(null);
        expectedResult.setPostalCode("94111");
        expectedResult.setAddressLine1("Market Street");
        expectedResult.setAddressLine2(null);
        expectedResult.setCoordinate(new Coordinate(37.7749, -122.4194));
        expectedResult.setAccuracy(15);

        GeoUsAddress result = functor.execute(validGeoAddress);

        assertEquals(expectedResult, result);
        verify(validGeoAddress, times(1)).getAddressDetails();
    }
}