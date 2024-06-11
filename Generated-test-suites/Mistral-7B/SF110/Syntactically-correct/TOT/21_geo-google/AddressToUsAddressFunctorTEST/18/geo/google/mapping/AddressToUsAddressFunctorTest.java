package geo.google.mapping.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.mapping.AddressToUsAddressFunctor;
import oasis.names.tc.ciq.xsdschema.xal._2.CountryNameCodeType;
import oasis.names.tc.ciq.xsdschema.xal._2.PostalCodeNumberType;

public class AddressToUsAddressFunctorTest {

	@Test
	public void testValidAddress() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		AddressDetails addressDetails = address.getAddressDetails();
		addressDetails.setCountry(new AddressDetails.Country());
		addressDetails.getCountry().setCountryNameCode(Arrays.asList(new AddressDetails.Country.CountryNameCode(new CountryNameCodeType())));
		addressDetails.setAdministrativeArea(new AdministrativeArea());
		addressDetails.getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeArea.AdministrativeAreaName(new LocalizedStringType())));
		addressDetails.getAdministrativeArea().setSubAdministrativeArea(new SubAdministrativeArea());
		addressDetails.getSubAdministrativeArea().setSubAdministrativeAreaName(Arrays.asList(new SubAdministrativeArea.SubAdministrativeAreaName(new LocalizedStringType())));
		addressDetails.getLocality().setLocalityName(Arrays.asList(new Locality.LocalityName(new LocalizedStringType())));
		addressDetails.getLocality().setPostalCode(new PostalCode());
		addressDetails.getLocality().getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCode.PostalCodeNumber(new PostalCodeNumberType())));
		addressDetails.getLocality().setThoroughfare(new Thoroughfare());
		addressDetails.getLocality().getThoroughfare().setThoroughfareName(Arrays.asList(new ThoroughfareNameType(), new ThoroughfareNameType()));
		address.setAccuracy((float) 5.0);
		address.setCoordinate(new Coordinate(1.0, 2.0));

		GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);

		assertEquals("Country name", "USA", result.getCountry());
		assertEquals("State", "StateName", result.getState());
		assertEquals("County", "CountyName", result.getCounty());
		assertEquals("City", "CityName", result.getCity());
		assertEquals("Postal Code", "12345", result.getPostalCode());
		assertEquals("Address Line 1", "ThoroughfareName1", result.getAddressLine1());
		assertEquals("Address Line 2", "ThoroughfareName2", result.getAddressLine2());
		assertEquals("Coordinate", new Coordinate(1.0, 2.0), result.getCoordinate());
		assertEquals("Accuracy", 5.0f, result.getAccuracy(), 0.001);
	}

	@Test
	public void testMissingCountry() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testEmptyCountryNames() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Collections.emptyList());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testMissingState() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testEmptyStateNames() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(Collections.emptyList());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testMissingCity() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeAreaNameType()));
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testEmptyCityNames() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeAreaNameType()));
		address.getAddressDetails().setLocality(new Locality());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testMissingPostalCode() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeAreaNameType()));
		address.getAddressDetails().setLocality(new Locality());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testEmptyPostalCodeNumbers() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeAreaNameType()));
		address.getAddressDetails().setLocality(new Locality());
		address.getAddressDetails().getLocality().setLocalityName(Arrays.asList(new LocalityNameType()));
		address.getAddressDetails().getLocality().setPostalCode(new PostalCode());
		address.setCoordinate(new Coordinate(1.0, 2.0));

		try {
			AddressToUsAddressFunctor.getInstance().execute(address);
			fail("Expected GeoException");
		} catch (GeoException e) {
			// Expected
		}
	}

	@Test
	public void testValidAddressWithComplexThoroughfare() {
		GeoAddress address = new GeoAddress();
		address.setAddressDetails(new AddressDetails());
		address.getAddressDetails().setCountry(new AddressDetails.Country());
		address.getAddressDetails().getCountry().setCountryNameCode(Arrays.asList(new CountryNameCodeType()));
		address.getAddressDetails().setAdministrativeArea(new AdministrativeArea());
		address.getAddressDetails().getAdministrativeArea().setAdministrativeAreaName(Arrays.asList(new AdministrativeAreaNameType()));
		address.getAddressDetails().setLocality(new Locality());
		address.getAddressDetails().getLocality().setLocalityName(Arrays.asList(new LocalityNameType(), new LocalityNameType()));
		address.getAddressDetails().getLocality().setPostalCode(new PostalCode());
		address.getAddressDetails().getLocality().getPostalCode().setPostalCodeNumber(Arrays.asList(new PostalCodeNumberType(), new PostalCodeNumberType()));
		address.getAddressDetails().getLocality().setThoroughfare(new Thoroughfare());
		address.getAddressDetails().getLocality().getThoroughfare().setThoroughfareName(Arrays.asList(new ThoroughfareNameType(), new ThoroughfareNameType(), new ThoroughfareNameType()));
		address.setCoordinate(new Coordinate(1.0, 2.0));
		address.setAccuracy((float) 5.0);

		GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(address);

		assertEquals("Country name", "USA", result.getCountry());
		assertEquals("State", "StateName", result.getState());
		assertEquals("County", "CountyName", result.getCounty());
		assertEquals("City", "CityName1 CityName2", result.getCity());
		assertEquals("Postal Code", "12345 67890", result.getPostalCode());
		assertEquals("Address Line 1", "ThoroughfareName1 ThoroughfareName2", result.getAddressLine1());
		assertEquals("Address Line 2", "ThoroughfareName3", result.getAddressLine2());
		assertEquals("Coordinate", new Coordinate(1.0, 2.0), result.getCoordinate());
		assertEquals("Accuracy", 5.0f, result.getAccuracy(), 0.001);
	}
}