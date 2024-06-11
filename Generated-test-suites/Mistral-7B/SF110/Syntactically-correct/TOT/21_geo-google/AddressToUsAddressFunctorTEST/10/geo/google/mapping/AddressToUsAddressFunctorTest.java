import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.ListUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AddressToUsAddressFunctorTest {

    private AddressDetails addressDetails;
    private GeoAddress geoAddress;
    private GeoUsAddress geoUsAddress;
    private AddressToUsAddressFunctor addressToUsAddressFunctor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addressDetails = mock(AddressDetails.class);
        geoAddress = new GeoAddress();
        geoUsAddress = new GeoUsAddress();
        addressToUsAddressFunctor = new AddressToUsAddressFunctor();
    }

    @Test
    public void testExecute_ValidAddress_ReturnsUsAddress() {
        when(addressDetails.getCountry()).thenReturn(new Country("US"));
        when(addressDetails.getAdministrativeArea()).thenReturn(new AdministrativeArea("TX", new Locality("Dallas", new PostalCode("75201"), new Thoroughfare("Main St"))));
        geoAddress.setAddressDetails(addressDetails);
        geoAddress.setCoordinate(new Coordinate(32.7767, -96.7975));
        geoAddress.setAccuracy((float) 10.0);

        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = addressToUsAddressFunctor.execute(geoAddress);

        assertEquals("US", result.getCountry());
        assertEquals("TX", result.getState());
        assertEquals("Dallas", result.getCity());
        assertEquals("75201", result.getPostalCode());
        assertEquals("Main St", result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertEquals(10.0f, result.getAccuracy(), 0.1f);

        verify(addressDetails).getCountry();
        verify(addressDetails).getAdministrativeArea();
        verify(addressDetails.getAdministrativeArea()).getLocality();
        verify(addressDetails.getAdministrativeArea()).getSubAdministrativeArea();
        verifyZeroInteractions(geoAddress.getCoordinate());
    }

    @Test(expected = GeoException.class)
    public void testExecute_NullAddress_ThrowsGeoException() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        addressToUsAddressFunctor.execute(null);
    }
}