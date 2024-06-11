package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import geo.google.datamodel.*;
import geo.google.mapping.AddressToUsAddressFunctor;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AddressToUsAddressFunctor.class)
public class AddressToUsAddressFunctorTest {

 private GeoAddress getTestAddress() {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails country = new AddressDetails.Country();
 country.setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode().content("US")));
 address.setAddressDetails(country);
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 administrativeArea.setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName().content("CA")));
 country.setAdministrativeArea(administrativeArea);
 AddressDetails subAdministrativeArea = new AddressDetails.SubAdministrativeArea();
 subAdministrativeArea.setSubAdministrativeAreaName(Arrays.asList(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName().content("LA")));
 administrativeArea.setSubAdministrativeArea(subAdministrativeArea);
 AddressDetails locality = new AddressDetails.Locality();
 locality.setLocalityName(Arrays.asList(new Locality.LocalityName().content("Los Angeles")));
 locality.setPostalCode(new PostalCode().postalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().content("90001"))));
 locality.setThoroughfare(new Thoroughfare().thoroughfareName(Arrays.asList(new ThoroughfareNameType().content("Main St"))));
 address.setAddressDetails(locality);
 address.setCoordinate(new Coordinate().setLatitude(34.0522355).setLongitude(-118.2436848));
 address.setAccuracy(50.0);
 return address;
 }

 @Test
 public void testExecute_validAddress_returnsUsAddress() throws Exception {
 GeoAddress address = getTestAddress();
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertEquals("US", usAddress.getCountry());
 assertEquals("CA", usAddress.getState());
 assertEquals("LA", usAddress.getCounty());
 assertEquals("Los Angeles", usAddress.getCity());
 assertEquals("90001", usAddress.getPostalCode());
 assertEquals("Main St", usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNotNull(usAddress.getCoordinate());
 assertEquals(50.0, usAddress.getAccuracy(), 0.0);
 }

 @Test
 public void testExecute_emptyAddress_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_nullAddress_throwsException() throws Exception {
 try {
 AddressToUsAddressFunctor.getInstance().execute(null);
 fail("Address should be null but no exception was thrown");
 } catch (GeoException e) {
 // Expected
 }
}

 @Test
 public void testExecute_addressWithNullCountry_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 address.setAddressDetails(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullAdministrativeArea_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullSubAdministrativeArea_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("CA"));
 AddressDetails subAdministrativeArea = new AddressDetails.SubAdministrativeArea();
 address.setAddressDetails(subAdministrativeArea);
 subAdministrativeArea.setSubAdministrativeAreaName(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullLocality_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("CA"));
 AddressDetails locality = new AddressDetails.Locality();
 address.setAddressDetails(locality);
 locality.setLocalityName(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullThoroughfare_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("CA"));
 AddressDetails locality = new AddressDetails.Locality();
 address.setAddressDetails(locality);
 locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
 locality.setPostalCode(new PostalCode().postalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().content("90001"))));
 AddressDetails thoroughfare = new AddressDetails.Thoroughfare();
 address.setAddressDetails(thoroughfare);
 thoroughfare.setThoroughfareName(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullPostalCode_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("CA"));
 AddressDetails locality = new AddressDetails.Locality();
 address.setAddressDetails(locality);
 locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
 locality.setPostalCode(null);
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullCoordinate_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("CA"));
 AddressDetails locality = new AddressDetails.Locality();
 address.setAddressDetails(locality);
 locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
 locality.setPostalCode(new PostalCode().postalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().content("90001"))));
 AddressDetails thoroughfare = new AddressDetails.Thoroughfare();
 address.setAddressDetails(thoroughfare);
 thoroughfare.setThoroughfareName(Arrays.asList(new ThoroughfareNameType().content("Main St")));
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance().execute(address);

 assertNotNull(usAddress);
 assertNull(usAddress.getCountry());
 assertNull(usAddress.getState());
 assertNull(usAddress.getCounty());
 assertNull(usAddress.getCity());
 assertNull(usAddress.getPostalCode());
 assertNull(usAddress.getAddressLine1());
 assertNull(usAddress.getAddressLine2());
 assertNull(usAddress.getCoordinate());
 assertNull(usAddress.getAccuracy());
 }

 @Test
 public void testExecute_addressWithNullAccuracy_returnsEmptyUsAddress() throws Exception {
 GeoAddress address = new GeoAddress();
 address.setAddressDetails(new AddressDetails());
 AddressDetails administrativeArea = new AddressDetails.AdministrativeArea();
 address.setAddressDetails(administrativeArea);
 administrativeArea.setAdministrativeAreaName(new AdministrativeArea.AdministrativeAreaName().content("CA"));
 AddressDetails locality = new AddressDetails.Locality();
 address.setAddressDetails(locality);
 locality.setLocalityName(new Locality.LocalityName().content("Los Angeles"));
 locality.setPostalCode(new PostalCode().postalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber().content("90001"))));
 AddressDetails thoroughfare = new AddressDetails.Thoroughfare();
 address.setAddressDetails(thoroughfare);
 thoroughfare.setThoroughfareName(Arrays.asList(new ThoroughfareNameType().content("Main St")));
 GeoUsAddress usAddress = AddressToUsAddressFunctor.getInstance();
 PowerMockito.mockStatic(CollectionUtils.class);
 PowerMockito.when(CollectionUtils.isNotEmpty((Collection)PowerMockito.any(Collection.class))).thenReturn(false);
 PowerMockito.doReturn(address).when(AddressToUsAddressFunctor.class, "getTestAddress");
 PowerMockito.when(CollectionUtils.isEmpty((Collection)PowerMockito.any(Collection.class))).thenReturn(true);
 GeoUsAddress result = usAddress.execute(PowerMockito.mock(GeoAddress.class));

 assertNotNull(result);
 assertNull(result.getCountry());
 assertNull(result.getState());
 assertNull(result.getCounty());
 assertNull(result.getCity());
 assertNull(result.getPostalCode());
 assertNull(result.getAddressLine1());
 assertNull(result.getAddressLine2());
 assertNull(result.getCoordinate());
 assertNull(result.getAccuracy());
 }
}