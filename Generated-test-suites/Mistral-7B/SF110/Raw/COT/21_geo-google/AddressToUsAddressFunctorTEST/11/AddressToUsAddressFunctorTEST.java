package geo.google.mapping.test;

 import static org.junit.Assert.*;

 import geo.google.datamodel.*;
 import geo.google.mapping.*;
 import org.apache.commons.collections.ListUtils;
 import org.junit.Test;

 public class AddressToUsAddressFunctorTest {

 private AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

 @Test
 public void testExecute_ValidAddress_ReturnsUSAddress() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 Country usCountry = addressDetails.getCountry();
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(ListUtils.singletonList(new AdministrativeAreaName("CA")));
 AdministrativeArea caAdminArea = addressDetails.getAdministrativeArea();
 addressDetails.setSubAdministrativeArea(new AdministrativeArea());
 addressDetails.getSubAdministrativeArea().setSubAdministrativeAreaName(ListUtils.singletonList(new SubAdministrativeAreaName("Los Angeles")));
 AdministrativeArea laSubAdminArea = addressDetails.getSubAdministrativeArea();
 addressDetails.setLocality(new Locality());
 addressDetails.getLocality().setLocalityName(ListUtils.singletonList(new LocalityName("Los Angeles")));
 Locality laLocality = addressDetails.getLocality();
 addressDetails.getLocality().setPostalCode(new PostalCode());
 addressDetails.getLocality().getPostalCode().setPostalCodeNumber(ListUtils.singletonList(new PostalCodeNumber("90001")));
 PostalCode laPostalCode = addressDetails.getLocality().getPostalCode();
 addressDetails.getLocality().setThoroughfare(new ThoroughfareType());
 addressDetails.getLocality().getThoroughfare().setThoroughfareName(ListUtils.singletonList(new ThoroughfareNameType("Sunset Boulevard")));
 ThoroughfareType sunsetBoulevard = addressDetails.getLocality().getThoroughfare();

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertEquals("US", outputAddress.getCountry());
 assertEquals("CA", outputAddress.getState());
 assertEquals("Los Angeles", outputAddress.getCity());
 assertEquals("90001", outputAddress.getPostalCode());
 assertEquals("Sunset Boulevard", outputAddress.getAddressLine1());
 assertNull(outputAddress.getAddressLine2());
 assertNotNull(outputAddress.getCoordinate());
 assertEquals(InputAddress.DEFAULT_ACCURACY, outputAddress.getAccuracy());
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowsGeoException() {
 GeoAddress inputAddress = null;

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullCountry_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 inputAddress.getAddressDetails().setCountry(null);

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptyCountryNameList_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 inputAddress.getAddressDetails().setCountry(new Country(""));

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullAdminArea_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(null);

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptyAdminAreaNameList_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullSubAdminArea_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
 addressDetails.setSubAdministrativeArea(null);

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptySubAdminAreaNameList_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(new ArrayList<>());
 addressDetails.setSubAdministrativeArea(new AdministrativeArea());
 addressDetails.getSubAdministrativeArea().setSubAdministrativeAreaName(new ArrayList<>());

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithNullLocality_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(ListUtils.singletonList(new AdministrativeAreaName("CA")));
 addressDetails.setSubAdministrativeArea(null);

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test(expected = GeoException.class)
 public void testExecute_AddressWithEmptyLocalityNameList_ThrowsGeoException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(ListUtils.singletonList(new AdministrativeAreaName("CA")));
 addressDetails.setLocality(new Locality());
 addressDetails.getLocality().setLocalityName(new ArrayList<>());

 try {
 functor.execute(inputAddress);
 fail("Expected GeoException to be thrown");
 } catch (GeoException e) {
 // Expected
 }
 }

 @Test
 public void testExecute_AddressWithNullPostalCode_DoesNotThrowException() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(ListUtils.singletonList(new AdministrativeAreaName("CA")));
 addressDetails.setLocality(new Locality());
 addressDetails.getLocality().setLocalityName(ListUtils.singletonList(new LocalityName("Los Angeles")));
 addressDetails.setThoroughfare(new ThoroughfareType());
 addressDetails.getThoroughfare().setThoroughfareName(ListUtils.singletonList(new ThoroughfareNameType("Sunset Boulevard")));

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertNull(outputAddress.getPostalCode());
 }

 @Test
 public void testExecute_AddressWithEmptyPostalCodeNumberList_DoesNotSetPostalCode() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(ListUtils.singletonList(new AdministrativeAreaName("CA")));
 addressDetails.setLocality(new Locality());
 addressDetails.getLocality().setLocalityName(ListUtils.singletonList(new LocalityName("Los Angeles")));
 addressDetails.getLocality().setPostalCode(new PostalCode());
 addressDetails.getLocality().getPostalCode().setPostalCodeNumber(new ArrayList<>());
 addressDetails.getLocality().getThoroughfare().setThoroughfareName(ListUtils.singletonList(new ThoroughfareNameType("Sunset Boulevard")));

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertNull(outputAddress.getPostalCode());
 }

 @Test
 public void testExecute_AddressWithMultiplePostalCodeNumbers_SetsFirstOne() {
 GeoAddress inputAddress = new GeoAddress();
 inputAddress.setAddressDetails(new AddressDetails());
 AddressDetails addressDetails = inputAddress.getAddressDetails();
 addressDetails.setCountry(new Country("US"));
 addressDetails.setAdministrativeArea(new AdministrativeArea());
 addressDetails.getAdministrativeArea().setAdministrativeAreaName(ListUtils.singletonList(new AdministrativeAreaName("CA")));
 addressDetails.setLocality(new Locality());
 addressDetails.getLocality().setLocalityName(ListUtils.singletonList(new LocalityName("Los Angeles")));
 addressDetails.getLocality().setPostalCode(new PostalCode());
 addressDetails.getLocality().getPostalCode().setPostalCodeNumber(ListUtils.nCopies(2, new PostalCodeNumber("12345")));
 addressDetails.getLocality().getThoroughfare().setThoroughfareName(ListUtils.singletonList(new ThoroughfareNameType("Sunset Boulevard")));

 GeoUsAddress outputAddress = functor.execute(inputAddress);

 assertEquals("12345", outputAddress.getPostalCode());
 }
 }