import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.HttpParameter;
import twitter4j.internal.http.HttpParameterArray;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery query = new GeoQuery(location);

        HttpParameter[] parameters = query.asHttpParameterArray();
        HttpParameter expectedParameter1 = new HttpParameter("lat", Double.toString(location.getLatitude()));
        HttpParameter expectedParameter2 = new HttpParameter("long", Double.toString(location.getLongitude()));

        assertArrayEquals(new HttpParameter[]{expectedParameter1, expectedParameter2}, parameters);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);

        HttpParameter[] parameters = query.asHttpParameterArray();
        HttpParameter expectedParameter = new HttpParameter("ip", ip);

        assertArrayEquals(new HttpParameter[]{expectedParameter}, parameters);
    }

    @Test(expected = NullPointerException.class)
    public void testGeoQueryWithNullLocation() {
        GeoQuery query = new GeoQuery(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGeoQueryWithNullIP() {
        GeoQuery query = new GeoQuery(null);
    }

    @Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(1.0, 1.0);
        GeoQuery query = new GeoQuery(location);

        assertEquals(location, query.getLocation());
        query.setLocation(null);
        assertNull(query.getLocation());

        String ip = "255.255.255.255";
        query.setIp(ip);
        assertEquals(ip, query.getIp());
        query.setIp(null);
        assertNull(query.getIp());

        String accuracy = "10m";
        query.setAccuracy(accuracy);
        assertEquals(accuracy, query.getAccuracy());
        query.setAccuracy(null);
        assertNull(query.getAccuracy());

        String granularity = "country";
        query.setGranularity(granularity);
        assertEquals(granularity, query.getGranularity());
        query.setGranularity(null);
        assertNull(query.getGranularity());

        int maxResults = 5;
        query.setMaxResults(maxResults);
        assertEquals(maxResults, query.getMaxResults());
        query.setMaxResults(0);
        assertEquals(0, query.getMaxResults());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxResultsNegative() {
        GeoQuery query = new GeoQuery();
        query.setMaxResults(-1);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 1.0);
        GeoLocation location2 = new GeoLocation(1.0, 1.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        GeoQuery query3 = new GeoQuery();
        GeoQuery query4 = new GeoQuery();

        assertEquals(query1, query1);
        assertEquals(query1, query2);

        HttpParameter[] parameters1 = query1.asHttpParameterArray();
        query1.setAccuracy("50m");
        HttpParameter[] parameters2 = query1.asHttpParameterArray();

        assertFalse(query1.equals(query3));
        assertFalse(query1.equals(query4));

        assertFalse(query1.equals(new Object()));
        assertFalse(query1.equals(null));

        assertEquals(query1, query1);
        assertEquals(query1, query2);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 1.0);
        GeoLocation location2 = new GeoLocation(1.0, 1.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        int hashCode1 = query1.hashCode();
        query1.setAccuracy("50m");
        int hashCode2 = query1.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);

        GeoQuery query3 = new GeoQuery();
        GeoQuery query4 = new GeoQuery();

        int hashCode3 = query3.hashCode();
        int hashCode4 = query4.hashCode();

        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 1.0);
        GeoQuery query = new GeoQuery(location);

        String expectedString = "GeoQuery{location=" + location + ", ip='' , accuracy='' , granularity='' , maxResults=0}";
        assertEquals(expectedString, query.toString());
    }
}