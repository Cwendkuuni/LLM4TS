package geo.google.mapping.test;

import static org.junit.Assert.*;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;
import org.apache.commons.collections.ListUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AddressToUsAddressFunctorTest {

 private static final GeoAddress TEST_ADDRESS = new GeoAddress();
 static {
 TEST_ADDRESS.setAddressDetails(new AddressDetails());
 TEST_ADDRESS.getAddressDetails().setCountry(new AddressDetails.Country());
 TEST_ADDRESS.getAddressDetails().getCountry().setCountryNameCode(ListUtils.integerArrayList(1));
 TEST_ADDRESS.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
 TEST_ADDRESS.getAddressDetails().getCountry().getAdministrativeArea().setAdministrativeAreaName(ListUtils.stringArrayList("Admin Area Name"));
 TEST_ADDRESS.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
 TEST_ADDRESS.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().setSubAdministrativeAreaName(ListUtils.stringArrayList("Sub Admin Area Name"));
 TEST_ADDRESS.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().setLocalityName(ListUtils.stringArrayList("Locality Name"));
 TEST_ADDRESS.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
 TEST_ADDRESS.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getPostalCode().setPostalCodeNumber(ListUtils.stringArrayList("12345"));
 TEST_ADDRESS.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
 TEST_ADDRESS.getAddressDetails().getCountry().getSubAdministrativeArea().getLocality().getThoroughfare().setThoroughfareName(ListUtils.stringArrayList("Thoroughfare Name", "Thoroughfare Name 2"));
 TEST_ADDRESS.setCoordinate(new Coordinate(1.0F, 2.0F, 3.0F));
 TEST_ADDRESS.setAccuracy(10.0F);
 }

 @Test
 public void testExecute_ValidAddress_ReturnsUsAddressWithCorrectValues() {
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(TEST_ADDRESS);

 assertEquals("Admin Area Name", usAddress.getState());
 assertEquals("Sub Admin Area Name", usAddress.getCounty());
 assertEquals("Locality Name", usAddress.getCity());
 assertEquals("12345", usAddress.getPostalCode());
 assertEquals("Thoroughfare Name", usAddress.getAddressLine1());
 assertEquals("Thoroughfare Name 2", usAddress.getAddressLine2());
 assertEquals(Arrays.asList(1.0F, 2.0F, 3.0F), usAddress.getCoordinate().getCoordinates());
 assertEquals(10.0F, usAddress.getAccuracy(), 0.1F);
 }

 @Test
 public void testExecute_NullAddress_ThrowsNullPointerException() {
 assertThrows(NullPointerException.class, () -> AddressToUsAddressFunctor.getInstance().execute(null));
 }

 @Test
 public void testExecute_AddressWithoutCountry_ThrowsIllegalArgumentException() {
 TEST_ADDRESS.getAddressDetails().setCountry(null);
 assertThrows(IllegalArgumentException.class, () -> AddressToUsAddressFunctor.getInstance().execute(TEST_ADDRESS));
 }

}