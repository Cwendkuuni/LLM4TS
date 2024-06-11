import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        attributes.remove("key");
        actual = attributes.hasKey("key");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        String actual = attributes.get("Key");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        attributes.remove("key");
        actual = attributes.hasKey("key");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        attributes.remove("Key");
        actual = attributes.hasKeyIgnoreCase("key");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        String actual1 = attributes3.get("key1");
        String expected1 = "value1";
        Assert.assertEquals(expected1, actual1);

        String actual2 = attributes3.get("key2");
        String expected2 = "value2";
        Assert.assertEquals(expected2, actual2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }
}