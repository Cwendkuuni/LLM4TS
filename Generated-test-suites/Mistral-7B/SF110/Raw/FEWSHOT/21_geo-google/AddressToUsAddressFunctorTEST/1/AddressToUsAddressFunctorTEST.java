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

    private GeoAddress testAddress;
    private GeoUsAddress expectedUsAddress;

    @Before
    public void setup() {
        testAddress = new GeoAddress();
        expectedUsAddress = new GeoUsAddress();
    }

    @Test
    public void testNullInputs() {
        assertNull(AddressToUsAddressFunctor.getInstance().execute(null));
    }

    @Test
    public void testEmptyAddress() {
        testAddress.setAddressDetails(new AddressDetails());
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(testAddress);
        assertNull(result);
    }

    @Test
    public void testUsAddressWithCountry() {
        final AddressDetails addressDetails = mock(AddressDetails.class);
        final Country country = new Country();
        final CountryNameCode countryName = new CountryNameCode();
        countryName.setContent("US");
        country.setCountryNameCodes(Collections.singletonList(countryName));
        when(addressDetails.getCountry()).thenReturn(country);

        final List<AdministrativeArea> administrativeAreas = Arrays.asList(new AdministrativeArea(), new AdministrativeArea());
        final AdministrativeArea adminArea = administrativeAreas.get(0);
        adminArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeAreaName()));
        final AdministrativeAreaName adminAreaName = adminArea.getAdministrativeAreaName().get(0);
        adminAreaName.setContent("California");
        when(addressDetails.getAdministrativeArea()).thenReturn(adminArea);

        final List<Locality> localities = Arrays.asList(new Locality(), new Locality());
        final Locality locality = localities.get(0);
        locality.setLocalityName(Collections.singletonList(new LocalityName()));
        final LocalityName localityName = locality.getLocalityName().get(0);
        localityName.setContent("San Francisco");
        when(adminArea.getLocality()).thenReturn(locality);

        final PostalCode postalCode = new PostalCode();
        final PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
        postalCodeNumber.setContent("94102");
        postalCode.setPostalCodeNumber(Collections.singletonList(postalCodeNumber));
        when(locality.getPostalCode()).thenReturn(postalCode);

        final List<ThoroughfareNameType> thoroughfares = Arrays.asList(new ThoroughfareNameType(), new ThoroughfareNameType());
        final ThoroughfareNameType thoroughfare1 = thoroughfares.get(0);
        thoroughfare1.setContent("Market Street");
        when(locality.getThoroughfare()).thenReturn(thoroughfares);

        expectedUsAddress.setCountry("US");
        expectedUsAddress.setState("California");
        expectedUsAddress.setCity("San Francisco");
        expectedUsAddress.setPostalCode("94102");
        expectedUsAddress.setAddressLine1("Market Street");

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(testAddress);
        assertEquals(expectedUsAddress, result);
    }

    @Test
    public void testUsAddressWithoutSubAdminArea() {
        final AddressDetails addressDetails = mock(AddressDetails.class);
        final Country country = new Country();
        final CountryNameCode countryName = new CountryNameCode();
        countryName.setContent("US");
        country.setCountryNameCodes(Collections.singletonList(countryName));
        when(addressDetails.getCountry()).thenReturn(country);

        final List<AdministrativeArea> administrativeAreas = Arrays.asList(new AdministrativeArea(), new AdministrativeArea());
        final AdministrativeArea adminArea = administrativeAreas.get(0);
        adminArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeAreaName()));
        final AdministrativeAreaName adminAreaName = adminArea.getAdministrativeAreaName().get(0);
        adminAreaName.setContent("California");
        when(addressDetails.getAdministrativeArea()).thenReturn(adminArea);

        final List<Locality> localities = Arrays.asList(new Locality(), new Locality());
        final Locality locality = localities.get(0);
        locality.setLocalityName(Collections.singletonList(new LocalityName()));
        final LocalityName localityName = locality.getLocalityName().get(0);
        localityName.setContent("San Francisco");
        when(locality.getThoroughfare()).thenReturn(null);

        expectedUsAddress.setCountry("US");
        expectedUsAddress.setState("California");
        expectedUsAddress.setCity("San Francisco");
        expectedUsAddress.setAddressLine1(null);

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(testAddress);
        assertEquals(expectedUsAddress, result);
    }

    @Test
    public void testUsAddressWithSubAdminArea() {
        final AddressDetails addressDetails = mock(AddressDetails.class);
        final Country country = new Country();
        final CountryNameCode countryName = new CountryNameCode();
        countryName.setContent("US");
        country.setCountryNameCodes(Collections.singletonList(countryName));
        when(addressDetails.getCountry()).thenReturn(country);

        final List<AdministrativeArea> administrativeAreas = Arrays.asList(new AdministrativeArea(), new AdministrativeArea());
        final AdministrativeArea adminArea = administrativeAreas.get(0);
        adminArea.setAdministrativeAreaName(Collections.singletonList(new AdministrativeAreaName()));
        final AdministrativeAreaName adminAreaName = adminArea.getAdministrativeAreaName().get(0);
        adminAreaName.setContent("California");
        when(addressDetails.getAdministrativeArea()).thenReturn(adminArea);

        final AdministrativeArea subAdminArea = new AdministrativeArea();
        final List<SubAdministrativeArea> subAdminAreas = Arrays.asList(new SubAdministrativeArea(), new SubAdministrativeArea());
        final SubAdministrativeArea subAdminArea1 = subAdminAreas.get(0);
        subAdminArea1.setSubAdministrativeAreaName(Collections.singletonList(new SubAdministrativeAreaName()));
        final SubAdministrativeAreaName subAdminAreaName1 = subAdminArea1.getSubAdministrativeAreaName().get(0);
        subAdminAreaName1.setContent("City");
        subAdminArea.setSubAdministrativeAreaName(Collections.singletonList(subAdminAreaName1));

        final List<Locality> localities = Arrays.asList(new Locality(), new Locality());
        final Locality locality = localities.get(0);
        locality.setLocalityName(Collections.singletonList(new LocalityName()));
        final LocalityName localityName = locality.getLocalityName().get(0);
        localityName.setContent("San Francisco");
        when(subAdminArea.getLocality()).thenReturn(locality);

        final PostalCode postalCode = new PostalCode();
        final PostalCodeNumber postalCodeNumber = new PostalCodeNumber();
        postalCodeNumber.setContent("94102");
        postalCode.setPostalCodeNumber(Collections.singletonList(postalCodeNumber));
        when(locality.getPostalCode()).thenReturn(postalCode);

        final List<ThoroughfareNameType> thoroughfares = Arrays.asList(new ThoroughfareNameType(), new ThoroughfareNameType());
        final ThoroughfareNameType thoroughfare1 = thoroughfares.get(0);
        thoroughfare1.setContent("Market Street");
        when(locality.getThoroughfare()).thenReturn(thoroughfares);

        expectedUsAddress.setCountry("US");
        expectedUsAddress.setState("California");
        expectedUsAddress.setCity("San Francisco");
        expectedUsAddress.setCounty("City");
        expectedUsAddress.setPostalCode("94102");
        expectedUsAddress.setAddressLine1("Market Street");

        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(testAddress);
        assertEquals(expectedUsAddress, result);
    }
}