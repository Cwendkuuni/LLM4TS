import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getIp());
        assertNotNull(geoQuery.getLocation());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testConstructorWithIP() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertNull(geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery();
        assertNull(geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());

        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        String accuracy = "10m";
        String granularity = GeoQuery.NEIGHBORHOOD;
        geoQuery.setLocation(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(5);

        assertSame(location, geoQuery.getLocation());
        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(5, geoQuery.getMaxResults());
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.appendParameter("lat", "1.0", params);
        geoQuery.appendParameter("long", "2.0", params);
        geoQuery.appendParameter("accuracy", "10m", params);
        geoQuery.appendParameter("granularity", "city", params);
        geoQuery.appendParameter("max_results", "3", params);

        assertEquals(5, params.size());
        assertNotNull(params.get(0));
        assertNotNull(params.get(1));
        assertNotNull(params.get(2));
        assertNotNull(params.get(3));
        assertNotNull(params.get(4));
        assertEquals("lat", params.get(0).getName());
        assertEquals("1.0", params.get(0).getValue());
        assertEquals("long", params.get(1).getName());
        assertEquals("2.0", params.get(1).getValue());
        assertEquals("accuracy", params.get(2).getName());
        assertEquals("10m", params.get(2).getValue());
        assertEquals("granularity", params.get(3).getName());
        assertEquals("city", params.get(3).getValue());
        assertEquals("max_results", params.get(4).getName());
        assertEquals("3", params.get(4).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNullValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.appendParameter("name", null, params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterEmptyString() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.appendParameter("name", "", params);
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery();
        GeoQuery geoQuery2 = new GeoQuery();
        GeoLocation location1 = new GeoLocation(1.0D, 2.0D);
        GeoLocation location2 = new GeoLocation(1.0D, 2.0D);

        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(new Object()));
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(location1));

        geoQuery1.setLocation(location1);
        geoQuery2.setLocation(location2);
        geoQuery2.setLocation(location1);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery();
        GeoQuery geoQuery2 = new GeoQuery();
        GeoLocation location1 = new GeoLocation(1.0D, 2.0D);
        GeoLocation location2 = new GeoLocation(1.0D, 2.0D);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCodeLocation1 = location1.hashCode();
        int hashCodeLocation2 = location2.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode2, hashCode2);
        assertEquals(hashCodeLocation1, hashCodeLocation1);
        assertEquals(hashCodeLocation2, hashCodeLocation2);

        geoQuery1.setLocation(location1);
        geoQuery2.setLocation(location2);
        geoQuery2.setLocation(location1);

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery();
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        geoQuery.setLocation(location);

        String expected = "GeoQuery{location=" + location + ", ip=''"+ ", accuracy=''"+ ", granularity=''"+ ", maxResults=0}";
        String actual = geoQuery.toString();

        assertEquals(expected, actual);
    }
}