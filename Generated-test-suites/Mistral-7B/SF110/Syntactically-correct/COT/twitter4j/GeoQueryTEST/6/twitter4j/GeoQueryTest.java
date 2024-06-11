import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGeoQueryLocationConstructor() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getIp());
        Assert.assertEquals(location, geoQuery.getLocation());
        Assert.assertNull(geoQuery.getAccuracy());
        Assert.assertNull(geoQuery.getGranularity());
        Assert.assertEquals(0, geoQuery.getMaxResults());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        int count = 0;
        for (HttpParameter param : params) {
            if (param.getName().equals("lat")) {
                Assert.assertEquals(1.0, Double.parseDouble(param.getValue()));
                count++;
            }
            if (param.getName().equals("long")) {
                Assert.assertEquals(2.0, Double.parseDouble(param.getValue()));
                count++;
            }
            if (count > 1) {
                break;
            }
        }
    }

    @Test
    public void testGeoQueryIpConstructor() {
        GeoQuery geoQuery = new GeoQuery("1.2.3.4");
        assertNull(geoQuery.getLocation());
        Assert.assertEquals("1.2.3.4", geoQuery.getIp());
        Assert.assertNull(geoQuery.getAccuracy());
        Assert.assertNull(geoQuery.getGranularity());
        Assert.assertEquals(0, geoQuery.getMaxResults());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        int count = 0;
        for (HttpParameter param : params) {
            if (param.getName().equals("ip")) {
                Assert.assertEquals("1.2.3.4", param.getValue());
                count++;
            }
            if (count > 1) {
                break;
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeoQueryEmptyLocationConstructor() {
        new GeoQuery(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeoQueryEmptyIpConstructor() {
        new GeoQuery("");
    }

    @Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        Assert.assertEquals(location, geoQuery.getLocation());
        geoQuery.setLocation(null);
        Assert.assertNull(geoQuery.getLocation());

        Assert.assertNull(geoQuery.getIp());
        geoQuery.setIp("1.2.3.4");
        Assert.assertEquals("1.2.3.4", geoQuery.getIp());

        Assert.assertNull(geoQuery.getAccuracy());
        geoQuery.setAccuracy("high");
        Assert.assertEquals("high", geoQuery.getAccuracy());

        Assert.assertNull(geoQuery.getGranularity());
        geoQuery.setGranularity("country");
        Assert.assertEquals("country", geoQuery.getGranularity());

        Assert.assertEquals(0, geoQuery.getMaxResults());
        geoQuery.setMaxResults(5);
        Assert.assertEquals(5, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(location2);

        GeoQuery geoQuery5 = new GeoQuery(location1);
        geoQuery5.setAccuracy("high");

        Assert.assertTrue(geoQuery1.equals(geoQuery3));
        Assert.assertTrue(geoQuery1.equals(geoQuery1));
        Assert.assertTrue(geoQuery1.equals(geoQuery5));

        Assert.assertFalse(geoQuery1.equals(geoQuery2));
        Assert.assertFalse(geoQuery1.equals(null));
        Assert.assertFalse(geoQuery1.equals(""));
        Assert.assertFalse(geoQuery1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);

        GeoQuery geoQuery3 = new GeoQuery(location);
        geoQuery3.setAccuracy("high");
        int hashCode3 = geoQuery3.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expected = "GeoQuery{location=" + location.toString() + ", ip=''"+ ", accuracy=''"+ ", granularity=''"+ ", maxResults=0}";
        String actual = geoQuery.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullName() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.appendParameter(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAppendParameterNullValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.appendParameter("name", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendParameterNegativeMaxResults() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.appendParameter("max_results", -1);
    }
}