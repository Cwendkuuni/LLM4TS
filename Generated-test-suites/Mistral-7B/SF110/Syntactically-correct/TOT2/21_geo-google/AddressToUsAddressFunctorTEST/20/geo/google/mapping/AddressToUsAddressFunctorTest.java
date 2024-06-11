import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.junit.Test;
import org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().setContent("US"));
        }});
        addr.getAddressDetails().setCountry(country);

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);

        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("US");
        assertEquals(expected, result);
    }

    @Test
    public void testExecuteWithNullAddress() {
        Assert.assertThrows(NullPointerException.class, () -> AddressToUsAddressFunctor.getInstance().execute(null));
    }

    @Test
    public void testExecuteWithNullAddressDetails() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(null);
        Assert.assertThrows(NullPointerException.class, () -> AddressToUsAddressFunctor.getInstance().execute(address));
    }

    // Test for country is null
    // Test for countryNames is empty
    // Test for adminArea is null
    // Test for adminAreaName is empty or null
    // Test for subAdminArea is null
    // Test for subAdminAreaName is empty or null
    // Test for locality is null
    // Test for localityName is empty or null
    // Test for postalCode is null
    // Test for postalCodeNumber is empty or null
    // Test for thoroughfare is null
    // Test for thoroughfareName is empty or null
    // Test for coordinate is null
    // Test for addr.getAccuracy() is null or less than 0
}
//