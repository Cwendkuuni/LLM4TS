import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode.Content;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode.Language;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode.Name;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryCodeType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryCodeType.Code;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryCodeType.CountrySubdivision;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryNameCode.CountryNameCodeType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryNameCodeType.CodeType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryNameCodeType.Locale;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.CountryNameCodeType.NameType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.AdministrativeAreaName;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.AdministrativeAreaNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.SubAdministrativeArea;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.AdministrativeArea.SubAdministrativeAreaName;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Locality;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.LocalityName;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.LocalityNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.ThoroughfareNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.GeoAddress;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCode.PostalCodeNumber;
import org.junit.Test;
import org.mockito.Mockito;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testValidAddressToUsAddress() throws Exception {
        GeoAddress address = Mockito.mock(GeoAddress.class);

        // Set up address details
        AddressDetails details = new AddressDetails();
        Country country = new Country();
        country.setCountryCode(new CountryCodeType().setCountrySubdivision(CountrySubdivision.US).setCode(Code.US));
        country.setCountryNameCode(new CountryNameCodeType().setName(new Name().setContent("United States"))
                .setNameType(NameType.PREFERRED).setLocale(Locale.EN).setCodeType(CodeType.OFFICIAL));
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.setAdministrativeAreaName(new AdministrativeAreaNameType().setContent("California"));
        Locality locality = new Locality();
        locality.setLocalityName(new LocalityNameType().setContent("San Francisco"));
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(new PostalCodeNumber().setContent("94123"));
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType().setContent("Market Street");
        locality.setThoroughfare(new ThoroughfareType().setThoroughfareName(thoroughfareName));
        details.setCountry(country);
        details.setAdministrativeArea(adminArea);
        details.setLocality(locality);
        details.setPostalCode(postalCode);
        address.setAddressDetails(details);

        // Test conversion
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress usAddress = functor.execute(address);

        // Assert result
        assertEquals("United States", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertEquals("San Francisco", usAddress.getCity());
        assertEquals("94123", usAddress.getPostalCode());
        assertEquals("Market Street", usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNotNull(usAddress.getCoordinate());
        assertEquals(25.0, usAddress.getCoordinate().getLatitude(), 0.01);
        assertEquals(-122.3, usAddress.getCoordinate().getLongitude(), 0.01);
        assertEquals(addr.getAccuracy(), usAddress.getAccuracy());
    }

    @Test
    public void testEmptyAddress() throws Exception {
        GeoAddress address = Mockito.mock(GeoAddress.class);
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress usAddress = functor.execute(address);
        assertNull(usAddress.getCountry());
        assertNull(usAddress.getState());
        assertNull(usAddress.getCity());
        assertNull(usAddress.getPostalCode());
        assertNull(usAddress.getAddressLine1());
        assertNull(usAddress.getAddressLine2());
        assertNull(usAddress.getCoordinate());
        assertEquals(addr.getAccuracy(), usAddress.getAccuracy());
    }

    @Test
    public void testNullAddress() throws Exception {
        GeoAddress address = null;
        try {
            AddressToUsAddressFunctor.getInstance().execute(address);
            fail("Expected GeoException");
        } catch (GeoException e) {
            // Expected
        }
    }
}