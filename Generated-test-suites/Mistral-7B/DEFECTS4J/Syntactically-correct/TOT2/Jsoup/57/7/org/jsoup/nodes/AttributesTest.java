import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("Key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size1 = attributes1.size();
        int expectedSize1 = 2;
        Assert.assertEquals(expectedSize1, size1);

        String actual1 = attributes1.get("key1");
        String expected1 = "value1";
        Assert.assertEquals(expected1, actual1);

        String actual2 = attributes1.get("key2");
        String expected2 = "value2";
        Assert.assertEquals(expected2, actual2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyException() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueException() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }
}