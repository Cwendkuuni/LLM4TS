package geo.google.mapping;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testEmptyGeoAddress() {
        GeoAddress addr = new GeoAddress();
        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNull(ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getCounty());
        assertNull(ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }

    @Test
    public void testCountryOnlyAddress() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode() {{
            setContent("US");
        }});

        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("US", ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getCounty());
        assertNull(ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }

    @Test
    public void testCountryWithStateAddress() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode() {{
            setContent("US");
        }});
        addr.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
        addr.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName() {{
            setContent("California");
        }});

        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("US", ret.getCountry());
        assertEquals("California", ret.getState());
        assertNull(ret.getCity());
        assertNull(ret.getCounty());
        assertNull(ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }

    @Test
    public void testCountryWithStateAndCityAddress() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode() {{
            setContent("US");
        }});
        addr.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
        addr.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName() {{
            setContent("California");
        }});
        addr.getAddressDetails().setLocality(new Locality());
        addr.getAddressDetails().getLocality().setLocalityName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName() {{
            setContent("Los Angeles");
        }});
        addr.getAddressDetails().getLocality().setPostalCode(new PostalCode());
        addr.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<>());
        addr.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber() {{
            setContent("90001");
        }});

        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("US", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("Los Angeles", ret.getCity());
        assertNull(ret.getCounty());
        assertEquals("90001", ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNotNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }

    @Test
    public void testCountryWithStateAndCityAndAddressLine1Address() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode() {{
            setContent("US");
        }});
        addr.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
        addr.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName() {{
            setContent("California");
        }});
        addr.getAddressDetails().setLocality(new Locality());
        addr.getAddressDetails().getLocality().setLocalityName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName() {{
            setContent("Los Angeles");
        }});
        addr.getAddressDetails().getLocality().setPostalCode(new PostalCode());
        addr.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<>());
        addr.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber() {{
            setContent("90001");
        }});
        addr.getAddressDetails().getLocality().setThoroughfare(new Thoroughfare());
        addr.getAddressDetails().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType() {{
            setContent("Main Street");
        }});

        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("US", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("Los Angeles", ret.getCity());
        assertEquals("Main Street", ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertEquals("90001", ret.getPostalCode());
        assertNotNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }

    @Test
    public void testCountryWithStateAndCityAndAddressLine1AndAddressLine2Address() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode() {{
            setContent("US");
        }});
        addr.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
        addr.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName() {{
            setContent("California");
        }});
        addr.getAddressDetails().setLocality(new Locality());
        addr.getAddressDetails().getLocality().setLocalityName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName() {{
            setContent("Los Angeles");
        }});
        addr.getAddressDetails().getLocality().setPostalCode(new PostalCode());
        addr.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<>());
        addr.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber() {{
            setContent("90001");
        }});
        addr.getAddressDetails().getLocality().setThoroughfare(new Thoroughfare());
        addr.getAddressDetails().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType() {{
            setContent("Main Street");
        }});
        addr.getAddressDetails().getLocality().getThoroughfare().setThoroughfareName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType() {{
            setContent("First Avenue");
        }});

        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("US", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("Los Angeles", ret.getCity());
        assertEquals("Main Street", ret.getAddressLine1());
        assertEquals("First Avenue", ret.getAddressLine2());
        assertEquals("90001", ret.getPostalCode());
        assertNotNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }

    @Test
    public void testCountryWithStateAndCityAndNoPostalCodeAddress() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode() {{
            setContent("US");
        }});
        addr.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
        addr.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName() {{
            setContent("California");
        }});
        addr.getAddressDetails().setLocality(new Locality());
        addr.getAddressDetails().getLocality().setLocalityName(new ArrayList<>());
        addr.getAddressDetails().getLocality().getLocalityName().add(new Locality.LocalityName() {{
            setContent("Los Angeles");
        }});

        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("US", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("Los Angeles", ret.getCity());
        assertNull(ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNotNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }
}