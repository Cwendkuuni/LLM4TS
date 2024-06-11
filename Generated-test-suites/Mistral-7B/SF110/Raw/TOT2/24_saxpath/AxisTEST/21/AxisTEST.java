import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(org.saxpath.test.UnitTest.class)
public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        String expected = "child";
        int axisNum = 1;
        String result = Axis.lookup(axisNum);
        assertEquals(expected, result);
    }

    @Test
    public void testLookupByAxisName() {
        int expected = 1;
        String axisName = "child";
        int result = Axis.lookup(axisName);
        assertEquals(expected, result);
    }

    @Test
    public void testLookupByInvalidAxisNumber() {
        String result = Axis.lookup(-1);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupByNullInputStringMethod() {
        Axis.lookup(null);
    }

    @Test
    public void testLookupByEmptyStringInputStringMethod() {
        String result = Axis.lookup("");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByUnsupportedAxisName() {
        String axisName = "unsupportedAxis";
        Axis.lookup(axisName);
    }

    @Test
    public void testLookupByAxisNameNotFound() {
        int result = Axis.lookup("nonexistentAxis");
        assertEquals(Axis.INVALID_AXIS, result);
    }
}