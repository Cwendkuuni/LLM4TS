package geo.google.mapping.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.mapping.AddressDetails;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.mapping.ThoroughfareNameType;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AddressToUsAddressFunctor.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress testAddress;
 private GeoUsAddress expectedUsAddress;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 testAddress = new GeoAddress();
 testAddress.setAddressDetails(new AddressDetails());
 expectedUsAddress = new GeoUsAddress();

 testAddress.getAddressDetails().getCountry().setCountryNameCode(new AddressDetails.Country.CountryNameCode().content("US"));
 testAddress.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("California"));
 testAddress.getAddressDetails().getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea() {{
 setSubAdministrativeAreaName(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("Los Angeles"));
 }});
 testAddress.getAddressDetails().getAddressDetailsLocation().setLocality(new Locality() {{
 setLocalityName(new Locality.LocalityName().content("Downtown"));
 setPostalCode(new PostalCode() {{
 setPostalCodeNumber(new PostalCode.PostalCodeNumber().content("90012"));
 }});
 setThoroughfare(new Thoroughfare() {{
 setThoroughfareName(new ThoroughfareNameType().content("Main Street"));
 }});
 }});
 testAddress.setCoordinate(new org.geotoolkit.gml3.model.Coordinate());
 testAddress.setAccuracy(10.0);
 }

 @Test
 public void testExecute_ValidAddressWithCountryUS_ReturnUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(expectedUsAddress, actualUsAddress);
 }

 @Test(expected = GeoException.class)
 public void testExecute_NullAddress_ThrowException() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 GeoAddress nullAddress = null;
 try {
 addressToUsAddressFunctor.execute(nullAddress);
 } catch (GeoException e) {
fail("Expected GeoException but got: " + e.getMessage());
}
}

 @Test
 public void testExecute_ValidAddressWithEmptyDetails_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().setCountry(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithNullCountryNameCode_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getCountry().setCountryNameCode(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithEmptyAdminArea_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().setAdministrativeArea(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithNullAdministrativeAreaName_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithEmptySubAdminArea_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAdministrativeArea().setSubAdministrativeArea(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithEmptySubAdministrativeAreaName_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithNullLocality_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().setLocality(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithNullPostalCode_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().setPostalCode(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithEmptyThoroughfareName_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().setThoroughfareName(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithEmptyThoroughfareNameAndEmptyAddressLine1_ReturnEmptyUsAddress() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().setThoroughfareName(null);
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().clear();
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(new GeoUsAddress(), actualUsAddress);
}

 @Test
 public void testExecute_ValidAddressWithEmptyThoroughfareNameAndNonEmptyAddressLine1_ReturnUsAddressWithAddressLine1() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().setThoroughfareName(null);
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().clear();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Main Street"));
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals("Main Street", actualUsAddress.getAddressLine1());
}

 @Test
 public void testExecute_ValidAddressWithNonEmptyThoroughfareNameAndEmptyAddressLine1_ReturnUsAddressWithThoroughfareName() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().clear();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Main Street"));
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals("Main Street", actualUsAddress.getAddressLine1());
}

 @Test
 public void testExecute_ValidAddressWithNonEmptyThoroughfareNameAndNonEmptyAddressLine1_ReturnUsAddressWithBoth() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().clear();
 testAddress.getAddressDetails().getAddressDetailsLocation().getThoroughfare().getThoroughfareName().add(new ThoroughfareNameType().content("Main Street"));
 testAddress.getAddressDetails().getAddressDetailsLocation().setAddressDetailsLocationName(new AddressDetailsLocationNameType().content("1234 Main Street"));
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals("Main Street", actualUsAddress.getAddressLine1());
 assertEquals("1234", actualUsAddress.getAddressLine2());
}

 @Test
 public void testExecute_ValidAddressWithNullCoordinate_ReturnUsAddressWithEmptyCoordinate() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.setCoordinate(null);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertNull(actualUsAddress.getCoordinate());
}

 @Test
 public void testExecute_ValidAddressWithEmptyCoordinate_ReturnUsAddressWithEmptyCoordinate() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.setCoordinate(new org.geotoolkit.gml3.model.Coordinate());
 testAddress.getCoordinate().clear();
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertArrayEquals(new double[0], actualUsAddress.getCoordinate().getCoordinates());
}

 @Test
 public void testExecute_ValidAddressWithNonEmptyCoordinate_ReturnUsAddressWithCoordinate() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 double[] expectedCoordinates = { 1.0, 2.0, 3.0 };
 testAddress.setCoordinate(new org.geotoolkit.gml3.model.Coordinate());
 testAddress.getCoordinate().setCoordinates(expectedCoordinates);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertArrayEquals(expectedCoordinates, actualUsAddress.getCoordinate().getCoordinates());
}

 @Test
 public void testExecute_ValidAddressWithZeroAccuracy_ReturnUsAddressWithZeroAccuracy() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.setAccuracy(0.0);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(0.0, actualUsAddress.getAccuracy(), 0.0);
}

 @Test
 public void testExecute_ValidAddressWithNonZeroAccuracy_ReturnUsAddressWithAccuracy() {
 AddressToUsAddressFunctor addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
 testAddress.setAccuracy(10.0);
 GeoUsAddress actualUsAddress = addressToUsAddressFunctor.execute(testAddress);
 assertEquals(10.0, actualUsAddress.getAccuracy(), 0.0);
}
}