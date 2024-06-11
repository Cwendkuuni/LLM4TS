package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery("1.2.3.4");

        assertEquals(query1, query1);
        assertEquals(query1, query2);
        assertNotEquals(query1, query3);

        query1.setAccuracy("high");
        assertNotEquals(query1, query2);

        query2.setAccuracy("high");
        assertEquals(query1, query2);

        query1.setGranularity("city");
        assertNotEquals(query1, query2);

        query2.setGranularity("city");
        assertEquals(query1, query2);

        query1.setMaxResults(5);
        assertNotEquals(query1, query2);

        query2.setMaxResults(5);
        assertEquals(query1, query2);

        query1.setIp("1.2.3.4");
        assertNotEquals(query1, query2);

        query2.setIp("1.2.3.4");
        assertEquals(query1, query2);
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location);
        GeoQuery query2 = new GeoQuery(location);
        GeoQuery query3 = new GeoQuery("1.2.3.4");

        int hashCode1 = query1.hashCode();
        int hashCode2 = query1.hashCode();
        int hashCode3 = query3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        query1.setAccuracy("high");
        int newHashCode1 = query1.hashCode();
        assertNotEquals(hashCode1, newHashCode1);

        query2.setAccuracy("high");
        int newHashCode2 = query2.hashCode();
        assertEquals(newHashCode1, newHashCode2);

        query1.setGranularity("city");
        int newHashCode3 = query1.hashCode();
        assertNotEquals(newHashCode1, newHashCode3);

        query2.setGranularity("city");
        int newHashCode4 = query2.hashCode();
        assertEquals(newHashCode3, newHashCode4);

        query1.setMaxResults(5);
        int newHashCode5 = query1.hashCode();
        assertNotEquals(newHashCode3, newHashCode5);

        query2.setMaxResults(5);
        int newHashCode6 = query2.hashCode();
        assertEquals(newHashCode5, newHashCode6);

        query1.setIp("1.2.3.4");
        int newHashCode7 = query1.hashCode();
        assertNotEquals(newHashCode3, newHashCode7);

        query2.setIp("1.2.3.4");
        int newHashCode8 = query2.hashCode();
        assertEquals(newHashCode7, newHashCode8);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='null', granularity='null', maxResults=0}";
        String actual = query.toString();

        assertEquals(expected, actual);

        query.setAccuracy("high");
        expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='high', granularity='null', maxResults=0}";
        actual = query.toString();
        assertEquals(expected, actual);

        query.setGranularity("city");
        expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='high', granularity='city', maxResults=0}";
        actual = query.toString();
        assertEquals(expected, actual);

        query.setMaxResults(5);
        expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='high', granularity='city', maxResults=5}";
        actual = query.toString();
        assertEquals(expected, actual);

        query.setIp("1.2.3.4");
        expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='1.2.3.4', accuracy='high', granularity='city', maxResults=5}";
        actual = query.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("high");
        query.setGranularity("city");
        query.setMaxResults(5);
        query.setIp("1.2.3.4");

        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("lat", String.valueOf(location.getLatitude())),
            new HttpParameter("long", String.valueOf(location.getLongitude())),
            new HttpParameter("ip", "1.2.3.4"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "5")
        };

        HttpParameter[] actual = query.asHttpParameterArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyConstructor() {
        GeoQuery query = new GeoQuery();
        assertNull(query.getLocation());
        assertNull(query.getIp());
    }

    @Test
    public void testIpConstructor() {
        GeoQuery query = new GeoQuery("1.2.3.4");
        assertNull(query.getLocation());
        assertEquals("1.2.3.4", query.getIp());
    }
}