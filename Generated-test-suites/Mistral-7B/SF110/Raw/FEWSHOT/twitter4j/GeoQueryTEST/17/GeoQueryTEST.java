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
        GeoQuery query3 = new GeoQuery(1L, "accuracyValue");
        GeoQuery query4 = new GeoQuery(null, "ipValue");

        // Testing equality with same object
        assertTrue(query1.equals(query1));

        // Testing equality with null object
        assertFalse(query1.equals(null));

        // Testing equality with different class object
        assertFalse(query1.equals(new Object()));

        // Testing equality with different GeoQuery objects having same values
        assertTrue(query1.equals(query2));

        // Testing equality with different GeoQuery objects having different values
        assertFalse(query1.equals(query3));
        assertFalse(query1.equals(query4));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery(1L, "accuracyValue");
        GeoQuery query4 = new GeoQuery(null, "ipValue");

        // Testing hashCode with same object
        int hashCode1 = query1.hashCode();
        int hashCode2 = query1.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Testing hashCode with null object
        int hashCodeNull = query1.hashCode();
        int hashCode5 = new Object().hashCode();
        assertNotEquals(hashCodeNull, hashCode5);

        // Testing hashCode with different GeoQuery objects having same values
        int hashCode3 = query1.hashCode();
        int hashCode4 = query2.hashCode();
        assertEquals(hashCode3, hashCode4);

        // Testing hashCode with different GeoQuery objects having different values
        int hashCode5 = query1.hashCode();
        int hashCode6 = query3.hashCode();
        assertNotEquals(hashCode5, hashCode6);
        int hashCode7 = query1.hashCode();
        int hashCode8 = query4.hashCode();
        assertNotEquals(hashCode7, hashCode8);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=0}";
        String result = query.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        HttpParameter[] httpParameters = query.asHttpParameterArray();
        assertEquals(5, httpParameters.length);

        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (HttpParameter parameter : httpParameters) {
            names.add(parameter.getName());
            values.add(parameter.getValue());
        }

        List<String> expectedNames = Arrays.asList("lat", "long", "accuracy", "granularity", "max_results");
        List<String> expectedValues = Arrays.asList(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), "accuracyValue", "granularityValue", String.valueOf(query.getMaxResults()));

        assertEquals(expectedNames, names);
        assertEquals(expectedValues, values);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);

        HttpParameter[] httpParameters = query.asHttpParameterArray();
        assertEquals(4, httpParameters.length);

        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (HttpParameter parameter : httpParameters) {
            names.add(parameter.getName());
            values.add(parameter.getValue());
        }

        List<String> expectedNames = Arrays.asList("ip", "accuracy", "granularity", "max_results");
        List<String> expectedValues = Arrays.asList(ip, "accuracyValue", "granularityValue", String.valueOf(query.getMaxResults()));

        assertEquals(expectedNames, names);
        assertEquals(expectedValues, values);
    }
}