package twitter4j.test;

import org.junit.Test;
import twitter4j.*;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals("Location should match", expectedLocation, actualLocation);
    }
    
    @Test
    public void testGetIp() {
        GeoQuery geoQuery1 = new GeoQuery(); // default constructor with null ip
        Assert.assertNull("Ip should be null", geoQuery1.getIp());
        String ip = "123.45.67.89";
        GeoQuery geoQuery2 = new GeoQuery(ip);
        Assert.assertEquals("Ip should match", ip, geoQuery2.getIp());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.1, 2.1);
        String ip1 = "123.45.67.89";
        String ip2 = "123.45.67.90";
        String accuracy1 = "accurate";
        String accuracy2 = "inaccurate";
        String granularity1 = "city";
        String granularity2 = "neighborhood";
        int maxResults1 = 5;
        int maxResults2 = 10;

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery(ip1);
        GeoQuery query4 = new GeoQuery(ip1);
        GeoQuery query5 = new GeoQuery();
        GeoQuery query6 = new GeoQuery();

        GeoQuery query7 = new GeoQuery(location1);
        query7.setAccuracy(accuracy1);
        query7.setGranularity(granularity1);
        query7.setMaxResults(maxResults1);

        GeoQuery query8 = new GeoQuery(location1);
        query8.setAccuracy(accuracy2);
        query8.setGranularity(granularity2);
        query8.setMaxResults(maxResults2);

        Assert.assertFalse("Different locations should not equal", query1.equals(query2));
        Assert.assertFalse("Different ip addresses should not equal", query3.equals(query4));
        Assert.assertTrue("Same object should equal itself", query4.equals(query4));
        Assert.assertTrue("Same object should equal itself", query1.equals(query1));
        Assert.assertTrue("Equal objects should equal", query7.equals(query7));
        Assert.assertFalse("Different objects with different fields should not equal", query1.equals(query8));
    }
}