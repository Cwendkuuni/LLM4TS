import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.ListUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        GeoAddress address = new GeoAddress();
        Country country = new Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode("US")));
        address.setAddressDetails(country);
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("United States");

        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);
        assertEquals(expected, result);
    }
}