package saxpath;
import org.junit.Test;
import org.junit.Assert;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNum_ReturnsCorrectString() {
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testLookup_InvalidAxisNum_ReturnsNull() {
        int invalidAxisNum = 14;
        String actual = Axis.lookup(invalidAxisNum);
        Assert.assertNull(actual);
    }

    @Test
    public void testLookup_ValidAxisName_ReturnsCorrectInt() {
        for (String axisName : new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"}) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testLookup_NullAxisName_ThrowsNullPointerException() {
        String nullAxisName = null;
        Axis.lookup(nullAxisName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookup_InvalidAxisName_ThrowsIllegalArgumentException() {
        String invalidAxisName = "invalidAxisName";
        Axis.lookup(invalidAxisName);
    }
}