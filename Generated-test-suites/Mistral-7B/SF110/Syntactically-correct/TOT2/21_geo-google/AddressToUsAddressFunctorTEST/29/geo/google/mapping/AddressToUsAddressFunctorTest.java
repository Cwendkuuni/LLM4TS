package geo.google.mapping.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.*;
import org.apache.commons.collections.*;
import org.junit.*;
import org.junit.Test;
import org.mockito.Mockito;

public class AddressToUsAddressFunctorTest {

    private AddressDetails addressDetails;
    private GeoAddress geoAddress;
    private GeoUsAddress geoUsAddress;
    private AddressToUsAddressFunctor addressToUsAddressFunctor;

    @Before
    public void setup() {
        addressDetails = mock(AddressDetails.class);
        geoAddress = new GeoAddress();
        geoAddress.setAddressDetails(addressDetails);
        geoUsAddress = new GeoUsAddress();
        addressToUsAddressFunctor = new AddressToUsAddressFunctor();
    }

    @Test
    public void testGetInstance() {
        AddressToUsAddressFunctor addressToUsAddressFunctor1 = AddressToUsAddressFunctor.getInstance();
        AddressToUsAddressFunctor addressToUsAddressFunctor2 = AddressToUsAddressFunctor.getInstance();
        assertSame(addressToUsAddressFunctor1, addressToUsAddressFunctor2);
    }

    @Test
    public void testExecuteValidAddress() {
        when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country());
        when(addressDetails.getCountry().getCountryNameCode()).thenReturn(new ArrayList<AddressDetails.Country.CountryNameCode>());
        when(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).thenReturn("US");
        when(addressDetails.getAdministrativeArea()).thenReturn(new AdministrativeArea());
        when(addressDetails.getAdministrativeArea().getAdministrativeAreaName()).thenReturn(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
        when(addressDetails.getAdministrativeArea().getAdministrativeAreaName().get(0).getContent()).thenReturn("CA");
        when(addressDetails.getCoordinate()).thenReturn(new Coordinate());

        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        verify(addressDetails, times(1)).getCountry();
        verify(addressDetails, times(1)).getAdministrativeArea();

        GeoUsAddress expectedGeoUsAddress = new GeoUsAddress();
        expectedGeoUsAddress.setCountry("US");
        expectedGeoUsAddress.setState("CA");
        assertEquals(expectedGeoUsAddress, geoUsAddress);
    }

    @Test
    public void testExecuteNullAddress() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        assertNull(addressToUsAddressFunctor.execute(null));
    }

    @Test(expected = GeoException.class)
    public void testExecuteInvalidAddress() {
        when(addressDetails.getCountry()).thenReturn(null);
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        addressToUsAddressFunctor.execute(geoAddress);
    }

    @Test
    public void testExecuteEmptyAddress() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);
        assertNull(geoUsAddress);
    }

    @Test
    public void testExecuteAddressWithoutCountry() {
        when(addressDetails.getCountry()).thenReturn(null);
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);
        assertNull(geoUsAddress);
    }

    @Test
    public void testExecuteAddressWithoutState() {
        when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country());
        when(addressDetails.getCountry().getCountryNameCode()).thenReturn(new ArrayList<AddressDetails.Country.CountryNameCode>());
        when(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).thenReturn("US");
        when(addressDetails.getAdministrativeArea()).thenReturn(null);
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);
        assertNull(geoUsAddress);
    }

    @Test
    public void testExecuteAddressWithEmptyCountryNameCode() {
        when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country());
        when(addressDetails.getCountry().getCountryNameCode()).thenReturn(new ArrayList<AddressDetails.Country.CountryNameCode>());
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);
        assertNull(geoUsAddress);
    }

    @Test
    public void testExecuteAddressWithEmptyAdministrativeArea() {
        when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country());
        when(addressDetails.getCountry().getCountryNameCode()).thenReturn(new ArrayList<AddressDetails.Country.CountryNameCode>());
        when(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).thenReturn("US");
        when(addressDetails.getAdministrativeArea()).thenReturn(new AdministrativeArea());
        when(addressDetails.getAdministrativeArea().getAdministrativeAreaName()).thenReturn(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);
        assertNull(geoUsAddress);
    }

    @Test
    public void testExecuteAddressWithEmptyAdministrativeAreaName() {
        when(addressDetails.getCountry()).thenReturn(new AddressDetails.Country());
        when(addressDetails.getCountry().getCountryNameCode()).thenReturn(new ArrayList<AddressDetails.Country.CountryNameCode>());
        when(addressDetails.getCountry().getCountryNameCode().get(0).getContent()).thenReturn("US");
        when(addressDetails.getAdministrativeArea()).thenReturn(new AdministrativeArea());
        when(addressDetails.getAdministrativeArea().getAdministrativeAreaName()).thenReturn(new ArrayList<AdministrativeArea.AdministrativeAreaName>());
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);
        assertNull(geoUsAddress);
    }
}