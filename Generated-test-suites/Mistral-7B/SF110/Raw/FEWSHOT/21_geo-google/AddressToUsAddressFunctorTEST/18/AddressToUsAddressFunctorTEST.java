package geo.google.mapping.test;

import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_ValidAddressData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().content("US"));
        }});
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>() {{
            add(new AdministrativeArea.AdministrativeAreaName().content("California"));
        }});
        country.getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new ArrayList<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName>() {{
            add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("San Francisco"));
        }});
        country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>() {{
            add(new Locality.LocalityName().content("The City"));
        }});
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>() {{
            add(new PostalCode.PostalCodeNumber().content("94111"));
        }});
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<ThoroughfareNameType>() {{
            add(new ThoroughfareNameType().content("Market Street"));
            add(new ThoroughfareNameType().content("Number Street"));
        }});
        address.setAddressDetails(country);
        address.setCoordinate(new Coordinate(37.7749, -122.4194));
        address.setAccuracy(AddressAccuracy.ADDRESS);

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

        assertEquals("US", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertEquals("San Francisco", usAddress.getCity());
        assertEquals("The City", usAddress.getCity());
        assertEquals("94111", usAddress.getPostalCode());
        assertEquals("Market Street", usAddress.getAddressLine1());
        assertEquals("Number Street", usAddress.getAddressLine2());
        assertEquals(37.7749, usAddress.getCoordinate().getLatitude(), 0.01);
        assertEquals(-122.4194, usAddress.getCoordinate().getLongitude(), 0.01);
    }

    @Test
    public void testExecute_NullAddressData() {
        GeoAddress address = null;

        try {
            AddressToUsAddressFunctor.getInstance().execute(address);
            fail("Expected GeoException to be thrown.");
        } catch (GeoException e) {
            // Expected
        }
    }

    @Test
    public void testExecute_EmptyAddressData() {
        GeoAddress address = new GeoAddress();

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

        assertEquals(null, usAddress.getCountry());
        assertEquals(null, usAddress.getState());
        assertEquals(null, usAddress.getCity());
        assertEquals(null, usAddress.getPostalCode());
        assertEquals(null, usAddress.getAddressLine1());
        assertEquals(null, usAddress.getAddressLine2());
        assertNull(usAddress.getCoordinate());
        assertEquals(AddressAccuracy.NO_ADDRESS, usAddress.getAccuracy());
    }

    @Test
    public void testExecute_MissingCountryData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>());
        address.setAddressDetails(country);

        GeoUsAddress usAddress = null;
        try {
            usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
        } catch (GeoException e) {
            // Expected
        }

        assertNull(usAddress);
    }

    @Test
    public void testExecute_MissingStateData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().content("US"));
        }});
        country.setAdministrativeArea(new AdministrativeArea());
        address.setAddressDetails(country);

        GeoUsAddress usAddress = null;
        try {
            usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
        } catch (GeoException e) {
            // Expected
        }

        assertNull(usAddress);
    }

    @Test
    public void testExecute_MissingCityData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().content("US"));
        }});
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>() {{
            add(new AdministrativeArea.AdministrativeAreaName().content("California"));
        }});
        address.setAddressDetails(country);

        GeoUsAddress usAddress = null;
        try {
            usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
        } catch (GeoException e) {
            // Expected
        }

        assertNull(usAddress);
    }

    @Test
    public void testExecute_MissingPostalCodeData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().content("US"));
        }});
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>() {{
            add(new AdministrativeArea.AdministrativeAreaName().content("California"));
        }});
        country.getAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>() {{
            add(new Locality.LocalityName().content("The City"));
        }});
        address.setAddressDetails(country);

        GeoUsAddress usAddress = null;
        try {
            usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
        } catch (GeoException e) {
            // Expected
        }

        assertNull(usAddress);
    }

    @Test
    public void testExecute_MissingThoroughfareData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().content("US"));
        }});
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>() {{
            add(new AdministrativeArea.AdministrativeAreaName().content("California"));
        }});
        country.getAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>() {{
            add(new Locality.LocalityName().content("The City"));
        }});
        country.getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>() {{
            add(new PostalCode.PostalCodeNumber().content("94111"));
        }});
        address.setAddressDetails(country);

        GeoUsAddress usAddress = null;
        try {
            usAddress = AddressToUsAddressFunctor.getInstance().execute(address);
        } catch (GeoException e) {
            // Expected
        }

        assertNull(usAddress);
    }

    @Test
    public void testExecute_EmptyThoroughfareData() {
        GeoAddress address = new GeoAddress();
        address.setAddressDetails(new AddressDetails());
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<AddressDetails.Country.CountryNameCode>() {{
            add(new AddressDetails.Country.CountryNameCode().content("US"));
        }});
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<AdministrativeArea.AdministrativeAreaName>() {{
            add(new AdministrativeArea.AdministrativeAreaName().content("California"));
        }});
        country.getAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getLocality().setLocalityName(new ArrayList<Locality.LocalityName>() {{
            add(new Locality.LocalityName().content("The City"));
        }});
        country.getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<PostalCode.PostalCodeNumber>() {{
            add(new PostalCode.PostalCodeNumber().content("94111"));
        }});
        country.getAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<ThoroughfareNameType>());
        address.setAddressDetails(country);

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
    }
}