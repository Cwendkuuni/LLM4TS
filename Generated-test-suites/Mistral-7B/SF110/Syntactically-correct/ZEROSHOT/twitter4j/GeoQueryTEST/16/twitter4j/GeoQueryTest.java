package test;

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GeoQueryTest {

	@Test
	public void testGeoQueryWithLocation() {
		GeoLocation location = new GeoLocation(1.0d, 2.0d);
		GeoQuery query = new GeoQuery(location);

		HttpParameter[] params = query.asHttpParameterArray();
		List<String> expectedParams = Arrays.asList("lat", "1.0", "long", "2.0");
		assertEquals(expectedParams.size(), params.length);
		for (int i = 0; i < expectedParams.size(); i++) {
			assertEquals(expectedParams.get(i), params[i].getName());
		}
	}

	@Test
	public void testGeoQueryWithIP() {
		GeoQuery query = new GeoQuery("192.168.0.1");

		HttpParameter[] params = query.asHttpParameterArray();
		assertEquals("ip", params[0].getName());
		assertEquals("192.168.0.1", params[0].getValue());
	}

	@Test
	public void testSetGetAccuracy() {
		GeoQuery query = new GeoQuery(new GeoLocation(1.0d, 2.0d));
		query.accuracy("accurate");

		assertEquals("accurate", query.getAccuracy());
		assertEquals("accurate", query.accuracy().getAccuracy());
	}

	@Test
	public void testSetGetGranularity() {
		GeoQuery query = new GeoQuery(new GeoLocation(1.0d, 2.0d));
		query.granularity("city");

		assertEquals("city", query.getGranularity());
		assertEquals("city", query.granularity().getGranularity());
	}

	@Test
	public void testSetGetMaxResults() {
		GeoQuery query = new GeoQuery(new GeoLocation(1.0d, 2.0d));
		query.maxResults(20);

		assertEquals(20, query.getMaxResults());
		assertEquals(20, query.maxResults().getMaxResults());
	}

	@Test
	public void testEquals() {
		GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
		GeoLocation location2 = new GeoLocation(1.0d, 2.0d);

		GeoQuery query1 = new GeoQuery(location1);
		GeoQuery query2 = new GeoQuery(location2);
		GeoQuery query3 = new GeoQuery(location1);

		assertEquals(query1, query3);
		assertEquals(query1, query1);
		assertEquals(query1, query2);

		query2.setAccuracy("accurate");
		assertNotEquals(query1, query2);

		GeoQuery query4 = new GeoQuery("192.168.0.1");
		assertNotEquals(query1, query4);
	}

	@Test
	public void testHashCode() {
		GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
		GeoLocation location2 = new GeoLocation(2.0d, 1.0d);

		GeoQuery query1 = new GeoQuery(location1);
		GeoQuery query2 = new GeoQuery(location2);

		int hashcode1 = query1.hashCode();
		int hashcode2 = query2.hashCode();

		GeoQuery query3 = new GeoQuery(location1);
		int hashcode3 = query3.hashCode();

		assertEquals(hashcode1, hashcode3);
		assertNotEquals(hashcode1, hashcode2);
	}

	@Test
	public void testToString() {
		GeoLocation location = new GeoLocation(1.0d, 2.0d);
		GeoQuery query = new GeoQuery(location);

		String expectedToString = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=5}";
		assertEquals(expectedToString, query.toString());
	}
}