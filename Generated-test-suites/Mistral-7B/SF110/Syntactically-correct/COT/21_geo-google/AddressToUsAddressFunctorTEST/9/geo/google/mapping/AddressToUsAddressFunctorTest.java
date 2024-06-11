import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

 private GeoAddress createGeoAddress() {
  GeoAddress address = new GeoAddress();
  address.setAddressDetails(new AddressDetails());
  return address;
 }

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddress() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails.Country country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.setAdministrativeArea(new AdministrativeArea());
  country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
  country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("Los Angeles"));
  country.getAddressDetails().setCountry(country);
  geoAddress.setAddressDetails(country);
  geoAddress.setCoordinate(new Coordinate());

  GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(geoAddress);

  assertEquals("US", result.getCountry());
  assertEquals("California", result.getState());
  assertEquals("Los Angeles", result.getCity());
  assertEquals("", result.getCounty());
  assertNull(result.getPostalCode());
  assertNull(result.getAddressLine1());
  assertNull(result.getAddressLine2());
  assertNull(result.getCoordinate());
  assertEquals(0, result.getAccuracy());
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
  GeoAddress geoAddress = null;
  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddressDetails_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  geoAddress.setAddressDetails(null);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullCountry_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  addressDetails.setCountry(null);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_EmptyCountryNameCode_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  AddressDetails.Country country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode());
  addressDetails.setCountry(country);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAdminArea_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  addressDetails.getCountry().setAdministrativeArea(null);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullState_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  AddressDetails.Country country = addressDetails.getCountry();
  country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
  country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(null);
  geoAddress.setAddressDetails(addressDetails);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullLocality_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  AddressDetails.Country country = addressDetails.getCountry();
  country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
  country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("Los Angeles"));
  country.getAddressDetails().setLocality(null);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullPostalCode_ThrowsGeoException() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  AddressDetails.Country country = addressDetails.getCountry();
  country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
  country.getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("Los Angeles"));
  country.getAddressDetails().setLocality(new Locality());
  country.getAddressDetails().getLocality().setPostalCode(null);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

  try {
   functor.execute(geoAddress);
   fail("GeoException expected");
  } catch (GeoException e) {
   // expected
  }
 }

 @Test
 public void testExecute_EmptyPostalCode_ReturnsUsAddressWithEmptyPostalCode() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  AddressDetails.Country country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.setAdministrativeArea(new AdministrativeArea());
  country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  country.getAddressDetails().setCountry(country);
  geoAddress.setAddressDetails(addressDetails);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  GeoUsAddress result = functor.execute(geoAddress);

  assertEquals("US", result.getCountry());
  assertEquals("California", result.getState());
  assertEquals("", result.getCity());
  assertEquals("", result.getCounty());
  assertEquals("", result.getPostalCode());
  assertNull(result.getAddressLine1());
  assertNull(result.getAddressLine2());
  assertNull(result.getCoordinate());
  assertEquals(0, result.getAccuracy());
 }

 @Test
 public void testExecute_MultipleThoroughfares_ReturnsUsAddressWithTwoAddressLines() {
  GeoAddress geoAddress = createGeoAddress();
  AddressDetails addressDetails = geoAddress.getAddressDetails();
  AddressDetails.Country country = new AddressDetails.Country();
  country.setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
  country.setAdministrativeArea(new AdministrativeArea());
  country.getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
  country.getAddressDetails().setCountry(country);
  geoAddress.setAddressDetails(addressDetails);

  ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
  thoroughfare1.setContent("1st Street");
  ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
  thoroughfare2.setContent("Main Street");

  List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
  thoroughfares.add(thoroughfare1);
  thoroughfares.add(thoroughfare2);

  AddressDetails.Locality locality = new AddressDetails.Locality();
  locality.setThoroughfare(new ThoroughfareType().setThoroughfareName(thoroughfares));
  locality.setPostalCode(new PostalCodeType().setPostalCodeNumber(new PostalCodeNumberType().setContent("12345")));
  locality.setLocalityName(new LocalityNameType().setContent("Anytown"));

  addressDetails.setLocality(locality);

  AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
  GeoUsAddress result = functor.execute(geoAddress);

  assertEquals("US", result.getCountry());
  assertEquals("California", result.getState());
  assertEquals("Anytown", result.getCity());
  assertEquals("", result.getCounty());
  assertEquals("12345", result.getPostalCode());
  assertEquals("1st Street", result.getAddressLine1());
  assertEquals("Main Street", result.getAddressLine2());
  assertNull(result.getCoordinate());
  assertEquals(0, result.getAccuracy());
 }
}