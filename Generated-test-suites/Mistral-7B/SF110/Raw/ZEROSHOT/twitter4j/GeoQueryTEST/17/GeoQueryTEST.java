package twitter4j.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

	@Test
	public void testGeoQueryWithLocation() {
		// Create a GeoQuery object with a GeoLocation
		GeoLocation location = new GeoLocation(51.5074, -0.1278);
		GeoQuery query = new GeoQuery(location);

		// Test getting the location
		assertEquals(location, query.getLocation());

		// Test setting and getting the accuracy
		query.setAccuracy("address");
		assertEquals("address", query.getAccuracy());

		// Test setting and getting the granularity
		query.setGranularity("city");
		assertEquals("city", query.getGranularity());

		// Test setting and getting the maxResults
		query.setMaxResults(10);
		assertEquals(10, query.getMaxResults());

		// Test creating HttpParameter array
		HttpParameter[] parameters = query.asHttpParameterArray();
		List<HttpParameter> params = new ArrayList<HttpParameter>();
		query.appendParameter("lat", 51.5074, params);
		query.appendParameter("long", -0.1278, params);
		query.appendParameter("accuracy", "address", params);
		query.appendParameter("granularity", "city", params);
		query.appendParameter("max_results", 10, params);
		assertEquals(5, params.size());
		for (int i = 0; i < params.size(); i++) {
			assertEquals(parameters[i].getName(), params.get(i).getName());
			assertEquals(parameters[i].getValue(), params.get(i).getValue());
		}
	}

	@Test
	public void testGeoQueryWithIP() {
		// Create a GeoQuery object with an IP address
		GeoQuery query = new GeoQuery("192.168.0.1");

		// Test getting the IP address
		assertEquals("192.168.0.1", query.getIp());

		// Test setting and getting the accuracy
		query.setAccuracy("address");
		assertEquals("address", query.getAccuracy());

		// Test setting and getting the granularity
		query.setGranularity("city");
		assertEquals("city", query.getGranularity());

		// Test setting and getting the maxResults
		query.setMaxResults(10);
		assertEquals(10, query.getMaxResults());

		// Test creating HttpParameter array
		HttpParameter[] parameters = query.asHttpParameterArray();
		List<HttpParameter> params = new ArrayList<HttpParameter>();
		query.appendParameter("ip", "192.168.0.1", params);
		query.appendParameter("accuracy", "address", params);
		query.appendParameter("granularity", "city", params);
		query.appendParameter("max_results", 10, params);
		assertEquals(4, params.size());
		for (int i = 0; i < params.size(); i++) {
			assertEquals(parameters[i].getName(), params.get(i).getName());
			assertEquals(parameters[i].getValue(), params.get(i).getValue());
		}
	}

	@Test
	public void testEquals() {
		// Create two GeoQuery objects with the same parameters
		GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
		GeoQuery query1 = new GeoQuery(location1);
		query1.setAccuracy("address");
		query1.setGranularity("city");
		query1.setMaxResults(10);
		GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
		GeoQuery query2 = new GeoQuery(location2);
		query2.setAccuracy("address");
		query2.setGranularity("city");
		query2.setMaxResults(10);

		// Test equality
		assertTrue(query1.equals(query2));

		// Test inequality with different maxResults
		query1.setMaxResults(20);
		assertFalse(query1.equals(query2));

		// Test inequality with different accuracy
		query2.setAccuracy("neighborhood");
		assertFalse(query1.equals(query2));

		// Test inequality with different granularity
		query1.setGranularity("city");
		query2.setGranularity("neighborhood");
		assertFalse(query1.equals(query2));

		// Test inequality with different location
		GeoLocation location3 = new GeoLocation(51.5174, -0.1278);
		GeoQuery query3 = new GeoQuery(location3);
		query3.setAccuracy("address");
		query3.setGranularity("city");
		query3.setMaxResults(10);
		assertFalse(query1.equals(query3));

		// Test inequality with null object
		GeoQuery nullQuery = null;
		assertFalse(query1.equals(nullQuery));
	}

	@Test
	public void testHashCode() {
		// Create a GeoQuery object
		GeoLocation location = new GeoLocation(51.5074, -0.1278);
		GeoQuery query = new GeoQuery(location);
		query.setAccuracy("address");
		query.setGranularity("city");
		query.setMaxResults(10);

		// Test hashCode
		int hashCode1 = query.hashCode();

		// Change the accuracy and test hashCode again
		query.setAccuracy("neighborhood");
		int hashCode2 = query.hashCode();

		// Change the granularity and test hashCode again
		query.setGranularity("country");
		int hashCode3 = query.hashCode();

		// Change the maxResults and test hashCode again
		query.setMaxResults(20);
		int hashCode4 = query.hashCode();

		// Change the location and test hashCode again
		GeoLocation location2 = new GeoLocation(52.5074, -0.1278);
		query.setLocation(location2);
		int hashCode5 = query.hashCode();

		// Test that changing any parameter results in a different hashCode
		assertNotEquals(hashCode1, hashCode2);
		assertNotEquals(hashCode1, hashCode3);
		assertNotEquals(hashCode1, hashCode4);
		assertNotEquals(hashCode1, hashCode5);
	}

	@Test
	public void testToString() {
		// Create a GeoQuery object
		GeoLocation location = new GeoLocation(51.5074, -0.1278);
		GeoQuery query = new GeoQuery(location);
		query.setAccuracy("address");
		query.setGranularity("city");
		query.setMaxResults(10);

		// Test toString
		String stringQuery = query.toString();
		String expectedString = "GeoQuery{location=" + location + ", ip=''"+ ", accuracy='address'"+ ", granularity='city'"+ ", maxResults=10}";
		assertEquals(expectedString, stringQuery);
	}
}