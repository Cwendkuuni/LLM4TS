package com.example.geoquery;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

	@Test
	public void testGeoQueryWithLocation() {
		GeoLocation location = new GeoLocation(1.0, 2.0);
		GeoQuery query = new GeoQuery(location);

		HttpParameter[] expectedParameters = { new HttpParameter("lat", "1.0"), new HttpParameter("long", "2.0") };
		HttpParameter[] actualParameters = query.asHttpParameterArray();

		assertArrayEquals(expectedParameters, actualParameters);
	}

	@Test
	public void testGeoQueryWithIp() {
		String ip = "192.168.1.1";
		GeoQuery query = new GeoQuery(ip);

		HttpParameter[] expectedParameters = { new HttpParameter("ip", ip) };
		HttpParameter[] actualParameters = query.asHttpParameterArray();

		assertArrayEquals(expectedParameters, actualParameters);
	}

	@Test
	public void testGettersAndSetters() {
		GeoQuery query = new GeoQuery(null);

		query.setAccuracy("10m");
		query.setGranularity(GeoQuery.CITY);
		query.setMaxResults(5);

		assertEquals("10m", query.getAccuracy());
		assertEquals(GeoQuery.CITY, query.getGranularity());
		assertEquals(5, query.getMaxResults());
	}

	@Test
	public void testAppendingParameters() {
		GeoQuery query = new GeoQuery(null);

		List<HttpParameter> parameters = new ArrayList<>();

		query.appendParameter("accuracy", "10m", parameters);
		query.appendParameter("granularity", GeoQuery.CITY, parameters);
		query.appendParameter("max_results", 5, parameters);

		HttpParameter[] expectedParameters = { new HttpParameter("accuracy", "10m"), new HttpParameter("granularity", GeoQuery.CITY), new HttpParameter("max_results", "5") };
		HttpParameter[] actualParameters = query.asHttpParameterArray();

		assertArrayEquals(expectedParameters, actualParameters);
	}

	@Test
	public void testEquals() {
		GeoQuery query1 = new GeoQuery(new GeoLocation(1.0, 2.0));
		GeoQuery query2 = new GeoQuery(new GeoLocation(1.0, 2.0));

		assertTrue(query1.equals(query2));
	}

	@Test
	public void testHashCode() {
		GeoQuery query1 = new GeoQuery(new GeoLocation(1.0, 2.0));
		GeoQuery query2 = new GeoQuery(new GeoLocation(1.0, 2.0));

		int hashCode1 = query1.hashCode();
		int hashCode2 = query2.hashCode();

		assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testToString() {
		GeoLocation location = new GeoLocation(1.0, 2.0);
		GeoQuery query = new GeoQuery(location);

		String expectedString = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=}";
		String actualString = query.toString();

		assertEquals(expectedString, actualString);
	}
}