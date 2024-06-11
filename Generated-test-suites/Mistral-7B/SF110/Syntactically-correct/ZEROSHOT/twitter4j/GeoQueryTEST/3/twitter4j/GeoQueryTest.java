package test;

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GeoQueryTest {

	@Test
	public void testGeoQueryWithLocation() {
		GeoLocation location = new GeoLocation(1.0, 2.0);
		GeoQuery query = new GeoQuery(location);

		assertEquals(location, query.getLocation());
		assertNull(query.getIp());
		assertNull(query.getAccuracy());
		assertNull(query.getGranularity());
		assertEquals(0, query.getMaxResults());

		HttpParameter[] params = query.asHttpParameterArray();
		List<HttpParameter> expectedParams = new ArrayList<HttpParameter>();
		query.appendParameter("lat", String.valueOf(location.getLatitude()), expectedParams);
		query.appendParameter("long", String.valueOf(location.getLongitude()), expectedParams);

		assertEquals(expectedParams.size(), params.length);
		for (int i = 0; i < params.length; i++) {
			assertEquals(expectedParams.get(i).getName(), params[i].getName());
			assertEquals(expectedParams.get(i).getValue(), params[i].getValue());
		}
	}

	@Test
	public void testGeoQueryWithIP() {
		String ip = "127.0.0.1";
		GeoQuery query = new GeoQuery(ip);

		assertNull(query.getLocation());
		assertEquals(ip, query.getIp());
		assertNull(query.getAccuracy());
		assertNull(query.getGranularity());
		assertEquals(0, query.getMaxResults());

		HttpParameter[] params = query.asHttpParameterArray();
		List<HttpParameter> expectedParams = new ArrayList<HttpParameter>();
		query.appendParameter("ip", ip, expectedParams);

		assertEquals(expectedParams.size(), params.length);
		for (int i = 0; i < params.length; i++) {
			assertEquals(expectedParams.get(i).getName(), params[i].getName());
			assertEquals(expectedParams.get(i).getValue(), params[i].getValue());
		}
	}

	@Test
	public void testSettersAndGetters() {
		GeoLocation location = new GeoLocation(1.0, 2.0);
		GeoQuery query = new GeoQuery(location);

		assertEquals(location, query.getLocation());
		assertNull(query.getIp());
		assertNull(query.getAccuracy());
		assertNull(query.getGranularity());
		assertEquals(0, query.getMaxResults());

		query.setAccuracy("accurate");
		assertEquals("accurate", query.getAccuracy());

		query.setGranularity("city");
		assertEquals("city", query.getGranularity());

		query.setMaxResults(10);
		assertEquals(10, query.getMaxResults());
	}

	@Test
	public void testEqualsAndHashcode() {
		GeoLocation location1 = new GeoLocation(1.0, 2.0);
		GeoLocation location2 = new GeoLocation(1.0, 2.0);
		GeoQuery query1 = new GeoQuery(location1);
		GeoQuery query2 = new GeoQuery(location2);

		GeoQuery query3 = new GeoQuery(location1);
		GeoQuery query4 = new GeoQuery(new GeoLocation(1.1, 2.1));
		GeoQuery query5 = new GeoQuery();

		assertTrue(query1.equals(query3));
		assertTrue(query1.equals(query1));
		assertTrue(query1.equals(query1));
		assertFalse(query1.equals(query2));
		assertFalse(query1.equals(query4));
		assertFalse(query1.equals(query5));

		int hashcode1 = query1.hashCode();
		int hashcode2 = query2.hashCode();
		int hashcode3 = query3.hashCode();
		int hashcode4 = query4.hashCode();
		int hashcode5 = query5.hashCode();

		assertNotSame(hashcode1, hashcode2);
		assertNotSame(hashcode1, hashcode3);
		assertNotSame(hashcode1, hashcode4);
		assertNotSame(hashcode1, hashcode5);
	}

	@Test
	public void testToString() {
		GeoLocation location = new GeoLocation(1.0, 2.0);
		GeoQuery query = new GeoQuery(location);

		String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='null', granularity='null', maxResults=0}";
		String actual = query.toString();

		assertEquals(expected, actual);
	}
}