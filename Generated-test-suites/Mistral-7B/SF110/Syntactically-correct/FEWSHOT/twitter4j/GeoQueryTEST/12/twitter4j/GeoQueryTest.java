package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        GeoLocation actualLocation = query.getLocation();
        assertEquals(location, actualLocation);
        assertNull(query.getIp());

        List<HttpParameter> httpParameters = Arrays.asList(query.asHttpParameterArray());
        List<HttpParameter> expectedHttpParameters = Arrays.asList(
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "2.0")
        );
        assertArrayEquals(expectedHttpParameters.toArray(new HttpParameter[0]), httpParameters.toArray(new HttpParameter[0]));
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);

        String actualIp = query.getIp();
        assertEquals(ip, actualIp);
        assertNull(query.getLocation());

        List<HttpParameter> httpParameters = Arrays.asList(query.asHttpParameterArray());
        List<HttpParameter> expectedHttpParameters = Collections.singletonList(new HttpParameter("ip", ip));
        assertArrayEquals(expectedHttpParameters.toArray(new HttpParameter[0]), httpParameters.toArray(new HttpParameter[0]));
    }

    @Test
    public void testSettingAndGettingProperties() {
        String accuracy = "10km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        query.setAccuracy(accuracy);
        query.setGranularity(granularity);
        query.setMaxResults(maxResults);

        String actualAccuracy = query.getAccuracy();
        assertEquals(accuracy, actualAccuracy);
        String actualGranularity = query.getGranularity();
        assertEquals(granularity, actualGranularity);
        int actualMaxResults = query.getMaxResults();
        assertEquals(maxResults, actualMaxResults);

        HttpParameter[] actualHttpParameters = query.asHttpParameterArray();
        List<HttpParameter> expectedHttpParameters = Arrays.asList(
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "2.0"),
                new HttpParameter("accuracy", accuracy),
                new HttpParameter("granularity", granularity),
                new HttpParameter("max_results", String.valueOf(maxResults))
        );
        assertArrayEquals(expectedHttpParameters.toArray(new HttpParameter[0]), actualHttpParameters);
    }

    @Test
    public void testEqualsAndHashcode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ip = "192.168.0.1";
        String accuracy = "10km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(ip);
        GeoQuery query4 = new GeoQuery(location2);
        GeoQuery query5 = new GeoQuery(new GeoLocation(1.1, 2.1));

        GeoQuery query6 = query1.accuracy(accuracy).granularity(granularity).maxResults(maxResults);
        GeoQuery query7 = query6.setIp(ip);

        assertEquals(query1, query6);
        assertEquals(query6, query7);

        assertNotEquals(query1, query3);
        assertNotEquals(query1, query4);
        assertNotEquals(query1, query5);

        int hashcode1 = query1.hashCode();
        int hashcode2 = query6.hashCode();
        int hashcode3 = query7.hashCode();
        int hashcode4 = query3.hashCode();
        int hashcode5 = query4.hashCode();
        int hashcode6 = query5.hashCode();

        assertEquals(hashcode1, hashcode6);
        assertEquals(hashcode6, hashcode7);
        assertNotEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode1, hashcode4);
        assertNotEquals(hashcode1, hashcode5);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "192.168.0.1";
        String accuracy = "10km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery query = new GeoQuery(location).accuracy(accuracy).granularity(granularity).maxResults(maxResults).ip(ip);

        String actualString = query.toString();
        String expectedString = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='" + ip + "', accuracy='" + accuracy + "', granularity='" + granularity + "', maxResults=10}";
        assertEquals(expectedString, actualString);
    }
}