package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class AddressToUsAddressFunctorTest {

    private static final String US_COUNTRY_CODE = "US";
    private GeoUsAddress expectedUsAddress;
    private GeoAddress geoAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        expectedUsAddress = new GeoUsAddress();
        expectedUsAddress.setCountry("United States");
        expectedUsAddress.setState("California");
        expectedUsAddress.setCity("Los Angeles");
        expectedUsAddress.setPostalCode("90001");
        expectedUsAddress.setAddressLine1("1600 Amphitheatre Parkway");
        expectedUsAddress.setAddressLine2("Mountain View");
        expectedUsAddress.setCoordinate(new GeoCoordinate());
        expectedUsAddress.setAccuracy(10.0);

        geoAddress = mock(GeoAddress.class);
        when(geoAddress.getAddressDetails()).thenReturn(new AddressDetails());
        when(geoAddress.getAddressDetails().getCountry()).thenReturn(new AddressDetails.Country());
        when(geoAddress.getAddressDetails().getCountry().getCountryNameCode()).thenReturn(Collections.singletonList(new AddressDetails.Country.CountryNameCode()));
        when(geoAddress.getAddressDetails().getCountry().getCountryNameCode().get(0).getContent()).thenReturn(US_COUNTRY_CODE);
        when(geoAddress.getAddressDetails().getAdministrativeArea()).thenReturn(new AdministrativeArea());
        when(geoAddress.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName()).thenReturn(Collections.singletonList(new AdministrativeArea.AdministrativeAreaName()));
        when(geoAddress.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).thenReturn("California");
        when(geoAddress.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea()).thenReturn(null);
        when(geoAddress.getAddressDetails().getLocality()).thenReturn(new Locality());
        when(geoAddress.getAddressDetails().getLocality().getLocalityName()).thenReturn(Collections.singletonList(new Locality.LocalityName()));
        when(geoAddress.getAddressDetails().getLocality().getLocalityName().get(0).getContent()).thenReturn("Los Angeles");
        when(geoAddress.getAddressDetails().getLocality().getPostalCode()).thenReturn(new PostalCode());
        when(geoAddress.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber()).thenReturn(Collections.singletonList(new PostalCode.PostalCodeNumber()));
        when(geoAddress.getAddressDetails().getLocality().getPostalCode().getPostalCodeNumber().get(0).getContent()).thenReturn("90001");
        when(geoAddress.getAddressDetails().getLocality().getThoroughfare()).thenReturn(new Thoroughfare());
        when(geoAddress.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName()).thenReturn(Arrays.asList(new ThoroughfareNameType(), new ThoroughfareNameType()));
        when(geoAddress.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().get(0).getContent()).thenReturn("1600 Amphitheatre Parkway");
        when(geoAddress.getAddressDetails().getLocality().getThoroughfare().getThoroughfareName().get(1).getContent()).thenReturn("Mountain View");
        when(geoAddress.getCoordinate()).thenReturn(new GeoCoordinate());
        when(geoAddress.getAccuracy()).thenReturn(10.0);
    }

    @Test
    public void testExecute_ValidGeoAddress_ReturnsUsAddress() {
        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddress);

        assertEquals(expectedUsAddress, actualUsAddress);
    }

    @Test
    public void testExecute_NullGeoAddress_ReturnsNullUsAddress() {
        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(null);

        assertNull(actualUsAddress);
    }

    @Test
    public void testExecute_EmptyGeoAddress_ReturnsEmptyUsAddress() {
        AddressDetails addressDetails = new AddressDetails();
        GeoAddress geoAddressWithEmptyDetails = new GeoAddress();
        geoAddressWithEmptyDetails.setAddressDetails(addressDetails);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(geoAddressWithEmptyDetails);

        assertTrue(StringUtils.isBlank(actualUsAddress.getCountry()));
        assertTrue(StringUtils.isBlank(actualUsAddress.getState()));
        assertTrue(StringUtils.isBlank(actualUsAddress.getCity()));
        assertTrue(StringUtils.isBlank(actualUsAddress.getPostalCode()));
        assertTrue(StringUtils.isBlank(actualUsAddress.getAddressLine1()));
        assertTrue(StringUtils.isBlank(actualUsAddress.getAddressLine2()));
        assertNull(actualUsAddress.getCoordinate());
        assertEquals(0.0, actualUsAddress.getAccuracy());
    }
}