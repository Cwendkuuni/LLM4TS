import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.AddressToUsAddressFunctorTest;
import org.apache.commons.collections.ListUtils;
import org.junit.Before;
import org.junit.Test;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

public class AddressToUsAddressFunctorTest {

   private AddressToUsAddressFunctor functor;
   private GeoAddress address;

   @Before
   public void setup() {
      functor = AddressToUsAddressFunctor.getInstance();
      address = new GeoAddress();
      address.setAddressDetails(new AddressDetails());
      address.setAddressDetails(address.getAddressDetails()).setCountry(new Country());
      address.getAddressDetails().getCountry().setCountryNameCode(ListUtils.list(new CountryNameCode()));
      address.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
      address.getAddressDetails().getCountry().getAdministrativeArea().setAdministrativeAreaName(ListUtils.list(new AdministrativeAreaName()));
      address.getAddressDetails().getCountry().getAdministrativeArea().getAdministrativeAreaName().get(0).setContent("State");
      address.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
      address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(ListUtils.list(new SubAdministrativeAreaName()));
      address.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().get(0).setContent("County");
      address.getAddressDetails().getCountry().getAdministrativeArea().getLocality(0).setLocalityName(ListUtils.list(new LocalityName()));
      address.getAddressDetails().getCountry().getAdministrativeArea().getLocality(0).getLocalityName().get(0).setContent("City");
      address.getAddressDetails().getCountry().getAdministrativeArea().getLocality(0).getPostalCode().setPostalCodeNumber(ListUtils.list(new PostalCodeNumber()));
      address.getAddressDetails().getCountry().getAdministrativeArea().getLocality(0).getPostalCode().getPostalCodeNumber().get(0).setContent("12345");
      address.getAddressDetails().getCountry().getAdministrativeArea().getLocality(0).getThoroughfare().setThoroughfareName(ListUtils.list(new ThoroughfareNameType()));
      address.getAddressDetails().getCountry().getAdministrativeArea().getLocality(0).getThoroughfare().getThoroughfareName().get(0).setContent("Address Line 1");
      address.getAddressDetails().setCoordinate(new Coordinate());
      address.getAddressDetails().setAccuracy(1.0);
   }

   @Test
   public void testExecute() {
      GeoUsAddress result = functor.execute(address);
      assertEquals("Country", result.getCountry());
      assertEquals("State", result.getState());
      assertEquals("County", result.getCounty());
      assertEquals("City", result.getCity());
      assertEquals("12345", result.getPostalCode());
      assertEquals("Address Line 1", result.getAddressLine1());
      assertEquals("Address Line 2", result.getAddressLine2());
      assertEquals(address.getCoordinate().clone(), result.getCoordinate());
      assertEquals(address.getAccuracy(), result.getAccuracy());
      verify(address, times(1)).getAddressDetails();
   }
}