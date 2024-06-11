
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "2.0")
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.1, 2.1));
        Assert.assertTrue(geoQuery1.equals(geoQuery2));
        Assert.assertFalse(geoQuery1.equals(geoQuery3));
    }

    @Test
    public void testGetLocationWithNullLocation() {
        GeoQuery geoQuery = new GeoQuery(null);
        Assert.assertNull(geoQuery.getLocation());
    }

    @Test
    public void testGetLocationWithEmptyLocation() {
        GeoLocation emptyLocation = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(emptyLocation);
        Assert.assertNull(geoQuery.getLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullGeoQuery() {
        GeoQuery geoQuery = null;
        geoQuery.getLocation();
    }

    @Test
    public void testAsHttpParameterArrayWithEmptyGeoQuery() {
        GeoQuery geoQuery = new GeoQuery();
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(new HttpParameter[0], actualParams);
    }

    @Test(expected = NullPointerException.class)
    public void testAsHttpParameterArrayWithNullGeoQuery() {
        GeoQuery geoQuery = null;
        geoQuery.asHttpParameterArray();
    }

    @Test
    public void testEqualsWithNullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        Assert.assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        Object obj = new Object();
        Assert.assertFalse(geoQuery.equals(obj));
    }

    @Test
    public void testEqualsWithDifferentDataTypes() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int i = 1;
        Assert.assertFalse(geoQuery.equals(i));
    }
}
