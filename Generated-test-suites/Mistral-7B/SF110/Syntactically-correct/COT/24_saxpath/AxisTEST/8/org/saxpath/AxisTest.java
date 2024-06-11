import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            String result = Axis.lookup(i);
            switch (i) {
                case 1: {
                    Assert.assertEquals("child", result);
                    break;
                }
                case 2: {
                    Assert.assertEquals("descendant", result);
                    break;
                }
                case 3: {
                    Assert.assertEquals("parent", result);
                    break;
                }
                case 4: {
                    Assert.assertEquals("ancestor", result);
                    break;
                }
                case 5: {
                    Assert.assertEquals("following-sibling", result);
                    break;
                }
                case 6: {
                    Assert.assertEquals("preceding-sibling", result);
                    break;
                }
                case 7: {
                    Assert.assertEquals("following", result);
                    break;
                }
                case 8: {
                    Assert.assertEquals("preceding", result);
                    break;
                }
                case 9: {
                    Assert.assertEquals("attribute", result);
                    break;
                }
                case 10: {
                    Assert.assertEquals("namespace", result);
                    break;
                }
                case 11: {
                    Assert.assertEquals("self", result);
                    break;
                }
                case 12: {
                    Assert.assertEquals("descendant-or-self", result);
                    break;
                }
                case 13: {
                    Assert.assertEquals("ancestor-or-self", result);
                    break;
                }
                default: {
                    Assert.assertNull(result);
                }
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidAxisNumber() {
        String result = Axis.lookup(-1);
        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidAxisName() {
        String result = Axis.lookup("invalidAxis");
        Assert.assertNull(result);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int axisNumber = Axis.lookup(axisName);
            switch (axisName) {
                case "child": {
                    Assert.assertEquals(1, axisNumber);
                    break;
                }
                case "descendant": {
                    Assert.assertEquals(2, axisNumber);
                    break;
                }
                case "parent": {
                    Assert.assertEquals(3, axisNumber);
                    break;
                }
                case "ancestor": {
                    Assert.assertEquals(4, axisNumber);
                    break;
                }
                case "following-sibling": {
                    Assert.assertEquals(5, axisNumber);
                    break;
                }
                case "preceding-sibling": {
                    Assert.assertEquals(6, axisNumber);
                    break;
                }
                case "following": {
                    Assert.assertEquals(7, axisNumber);
                    break;
                }
                case "preceding": {
                    Assert.assertEquals(8, axisNumber);
                    break;
                }
                case "attribute": {
                    Assert.assertEquals(9, axisNumber);
                    break;
                }
                case "namespace": {
                    Assert.assertEquals(10, axisNumber);
                    break;
                }
                case "self": {
                    Assert.assertEquals(11, axisNumber);
                    break;
                }
                case "descendant-or-self": {
                    Assert.assertEquals(12, axisNumber);
                    break;
                }
                case "ancestor-or-self": {
                    Assert.assertEquals(13, axisNumber);
                    break;
                }
            }
        }
    }
}