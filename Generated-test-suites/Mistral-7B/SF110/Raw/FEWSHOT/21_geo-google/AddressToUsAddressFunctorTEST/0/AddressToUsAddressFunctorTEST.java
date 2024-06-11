package geo.google.mapping;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AddressToUsAddressFunctorTest {

    private GeoAddress testAddress;
    private GeoUsAddress expectedUsAddress;

    @Before
    public void setup() {
        testAddress = new GeoAddress();
        testAddress.setAddressDetails(new AddressDetails());
        testAddress.setCoordinate(new Coordinate());
        testAddress.setAccuracy(1.0f);

        expectedUsAddress = new GeoUsAddress();

        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(new AddressDetails(){
            {setCountry(country);}
        });

        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = ListUtils.list(new AdministrativeArea.AdministrativeAreaName(){{setContent("California");}});
        adminArea.setAdministrativeAreaName(adminAreaNames);
        country.setAdministrativeArea(adminArea);

        Locality locality = new Locality();
        List<Locality.LocalityName> localityNames = ListUtils.list(new Locality.LocalityName(){{setContent("San Francisco");}});
        locality.setLocalityName(localityNames);
        adminArea.setLocality(locality);

        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = ListUtils.list(new PostalCode.PostalCodeNumber(){{setContent("94111");}});
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        locality.setPostalCode(postalCode);

        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfareNames = ListUtils.list(new ThoroughfareNameType(){{setContent("Market Street");}});
        thoroughfare.setThoroughfareName(thoroughfareNames);
        locality.setThoroughfare(thoroughfare);

        expectedUsAddress.setCountry("US");
        expectedUsAddress.setState("California");
        expectedUsAddress.setCity("San Francisco");
        expectedUsAddress.setPostalCode("94111");
        expectedUsAddress.setAddressLine1("Market Street");
        expectedUsAddress.setAddressLine2(null);
        expectedUsAddress.setCoordinate(testAddress.getCoordinate().clone());
        expectedUsAddress.setAccuracy(testAddress.getAccuracy());
    }

    @Test
    public void testEmptyAddress() {
        AddressDetails addressDetails = new AddressDetails();
        GeoAddress emptyAddress = new GeoAddress();
        emptyAddress.setAddressDetails(addressDetails);

        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(emptyAddress);
        assertNull(usAddress);
    }

    @Test
    public void testNullAddress() {
        GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(null);
        assertNull(usAddress);
    }

    @Test
    public void testUsAddress() {
        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertEquals(expectedUsAddress, actualUsAddress);
    }

    @Test
    public void testNullCountry() {
        testAddress.setAddressDetails(new AddressDetails());

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingCountryNameCode() {
        testAddress.getAddressDetails().getCountry().setCountryNameCode(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingAdministrativeArea() {
        testAddress.getAddressDetails().getCountry().setAdministrativeArea(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingLocality() {
        AddressDetails adminArea = new AddressDetails.Country();
        testAddress.getAddressDetails().setCountry(adminArea);
        adminArea.setAdministrativeArea(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingPostalCode() {
        testAddress.getAddressDetails().getCountry().getAdministrativeArea().setLocality(new Locality());
        testAddress.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingThoroughfare() {
        testAddress.getAddressDetails().getCountry().getAdministrativeArea().setLocality(new Locality());
        testAddress.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        testAddress.getAddressDetails().getCountry().getAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(null);
        testAddress.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setThoroughfare(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingCountryName() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("CA");}}));
        testAddress.setAddressDetails(country);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingAdministrativeAreaName() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingLocalityName() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        country.getAdministrativeArea().getAdministrativeAreaName().setContent(null);
        country.getAdministrativeArea().setLocality(new Locality());

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingSubAdministrativeAreaName() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        country.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(null);

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingSubAdministrativeAreaName_withLocality() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        country.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        country.getAdministrativeArea().setLocality(new Locality());

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testMissingSubAdministrativeAreaName_withoutLocality() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        country.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea());

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testEmptySubAdministrativeAreaName() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        country.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(ListUtils.list(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName(){{setContent("");}}));

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }

    @Test
    public void testEmptyThoroughfareName() {
        AddressDetails country = new AddressDetails.Country();
        country.setCountryNameCode(ListUtils.list(new AddressDetails.Country.CountryNameCode(){{setContent("US");}}));
        testAddress.setAddressDetails(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName());
        country.getAdministrativeArea().getAdministrativeAreaName().setContent("California");
        country.getAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getLocality().setLocalityName(ListUtils.list(new Locality.LocalityName(){{setContent("San Francisco");}}));
        country.getAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(ListUtils.list(new ThoroughfareNameType(){{setContent("");}}));

        GeoUsAddress actualUsAddress = AddressToUsAddressFunctor.getInstance().execute(testAddress);
        assertNull(actualUsAddress);
    }
}