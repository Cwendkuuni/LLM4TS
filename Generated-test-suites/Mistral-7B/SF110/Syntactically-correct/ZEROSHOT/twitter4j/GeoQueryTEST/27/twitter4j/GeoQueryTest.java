package com.yourpackagename.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        List<HttpParameter> expectedParameters = new ArrayList<>();
        this.appendParameter("lat", "1.0", expectedParameters);
        this.appendParameter("long", "2.0", expectedParameters);

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParameters.toArray(new HttpParameter[0]), actualParameters);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        List<HttpParameter> expectedParameters = new ArrayList<>();
        this.appendParameter("ip", ip, expectedParameters);

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParameters.toArray(new HttpParameter[0]), actualParameters);
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));

        geoQuery.setAccuracy("10m");
        Assert.assertEquals("10m", geoQuery.getAccuracy());

        geoQuery.setGranularity("city");
        Assert.assertEquals("city", geoQuery.getGranularity());

        geoQuery.setMaxResults(3);
        Assert.assertEquals(3, geoQuery.getMaxResults());

        geoQuery.setLocation(null);
        Assert.assertNull(geoQuery.getLocation());

        geoQuery.setIp("192.168.1.1");
        Assert.assertEquals("192.168.1.1", geoQuery.getIp());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery sameQuery = new GeoQuery(location1);
        GeoQuery differentQuery1 = new GeoQuery(new GeoLocation(1.0, 3.0));
        GeoQuery differentQuery2 = new GeoQuery("192.168.1.1");

        Assert.assertTrue(geoQuery1.equals(sameQuery));
        Assert.assertTrue(sameQuery.equals(geoQuery1));

        Assert.assertFalse(geoQuery1.equals(differentQuery1));
        Assert.assertFalse(differentQuery1.equals(geoQuery1));

        Assert.assertFalse(geoQuery1.equals(differentQuery2));
        Assert.assertFalse(differentQuery2.equals(geoQuery1));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expectedString = "GeoQuery{location=" + location + ", ip='' , accuracy='' , granularity='' , maxResults=0}";
        String actualString = geoQuery.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, value));
    }

    private void appendParameter(String name, int value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    private void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }
}