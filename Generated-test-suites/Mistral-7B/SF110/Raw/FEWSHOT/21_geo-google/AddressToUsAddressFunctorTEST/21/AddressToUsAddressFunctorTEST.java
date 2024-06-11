package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AddressToUsAddressFunctorTest {

    private GeoAddress geoAddress;
    private GeoUsAddress geoUsAddress;

    @Before
    public void setup() {
        geoAddress = mock(GeoAddress.class);
        geoUsAddress = new GeoUsAddress();
    }

    @Test
    public void testExecute_ValidAddress_SetsFieldsCorrectly() {
        when(geoAddress.getAddressDetails().getCountry()).thenReturn(new AddressDetails.Country() {{
            final List<AddressDetails.Country.CountryNameCode> countryNameCode = new ArrayList<>();
            countryNameCode.add(new AddressDetails.Country.CountryNameCode() {{
                setContent("US");
            }});
            setCountryNameCode(countryNameCode);
        }});

        when(geoAddress.getAddressDetails().getAdministrativeArea()).thenReturn(new AdministrativeArea() {{
            final List<AdministrativeArea.AdministrativeAreaName> administrativeAreaName = new ArrayList<>();
            administrativeAreaName.add(new AdministrativeArea.AdministrativeAreaName() {{
                setContent("California");
            }});
            setAdministrativeAreaName(administrativeAreaName);

            final AdministrativeArea.SubAdministrativeArea subAdministrativeArea = new AdministrativeArea.SubAdministrativeArea() {{
                final List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdministrativeAreaName = new ArrayList<>();
                subAdministrativeAreaName.add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName() {{
                    setContent("Los Angeles");
                }});
                setSubAdministrativeAreaName(subAdministrativeAreaName);
            }};
            setSubAdministrativeArea(subAdministrativeArea);

            final Locality locality = new Locality() {{
                final List<Locality.LocalityName> localityName = new ArrayList<>();
                localityName.add(new Locality.LocalityName() {{
                    setContent("Beverly Hills");
                }});
                setLocalityName(localityName);

                final PostalCode postalCode = new PostalCode() {{
                    final List<PostalCode.PostalCodeNumber> postalCodeNumber = new ArrayList<>();
                    postalCodeNumber.add(new PostalCode.PostalCodeNumber() {{
                        setContent("90210");
                    }});
                    setPostalCodeNumber(postalCodeNumber);
                }};
                setPostalCode(postalCode);
            }};
            setLocality(locality);

            final Thoroughfare thoroughfare = new Thoroughfare() {{
                final List<ThoroughfareNameType> thoroughfareName = new ArrayList<>();
                thoroughfareName.add(new ThoroughfareNameType() {{
                    setContent("Rodeo Drive");
                }});
                setThoroughfareName(thoroughfareName);
            }};
            setThoroughfare(thoroughfare);
        }});

        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        addressToUsAddressFunctor.execute(geoAddress, geoUsAddress);

        assertEquals("US", geoUsAddress.getCountry());
        assertEquals("California", geoUsAddress.getState());
        assertEquals("Los Angeles", geoUsAddress.getCounty());
        assertEquals("Beverly Hills", geoUsAddress.getCity());
        assertEquals("90210", geoUsAddress.getPostalCode());
        assertEquals("Rodeo Drive", geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
        assertNotNull(geoUsAddress.getCoordinate());
        assertEquals(geoAddress.getAccuracy(), geoUsAddress.getAccuracy());

        verify(geoAddress, times(1)).getAddressDetails();
        verify(geoAddress.getAddressDetails(), times(1)).getCountry();
        verify(geoAddress.getAddressDetails().getCountry(), times(1)).getCountryNameCode();
        verify(geoAddress.getAddressDetails().getCountry().getCountryNameCode().get(0), times(1)).getContent();
        verify(geoAddress.getAddressDetails().getAdministrativeArea(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getAdministrativeAreaName().get(0), times(1)).getContent();
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().get(0), times(1)).getContent();
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getLocality(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getLocality().getLocalityName(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getLocality().getLocalityName().get(0), times(1)).getContent();
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().get(0), times(1)).getContent();
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getThoroughfare(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getThoroughfare().getThoroughfareName(), times(1));
        verify(geoAddress.getAddressDetails().getAdministrativeArea().getThoroughfare().getThoroughfareName().get(0), times(1)).getContent();
        verify(geoAddress.getCoordinate(), times(1));
    }

    @Test
    public void testExecute_NullAddress_ReturnsEmptyUsAddress() {
        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        addressToUsAddressFunctor.execute(null, geoUsAddress);

        assertNull(geoUsAddress.getCountry());
        assertNull(geoUsAddress.getState());
        assertNull(geoUsAddress.getCounty());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
        assertNull(geoUsAddress.getCoordinate());
    }

    @Test
    public void testExecute_AddressWithNoCountry_ReturnsEmptyUsAddress() {
        when(geoAddress.getAddressDetails().getCountry()).thenReturn(null);

        AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
        addressToUsAddressFunctor.execute(geoAddress, geoUsAddress);

        assertNull(geoUsAddress.getCountry());
        assertNull(geoUsAddress.getState());
        assertNull(geoUsAddress.getCounty());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
        assertNull(geoUsAddress.getCoordinate());

        verify(geoAddress, times(1)).getAddressDetails();
    }
}