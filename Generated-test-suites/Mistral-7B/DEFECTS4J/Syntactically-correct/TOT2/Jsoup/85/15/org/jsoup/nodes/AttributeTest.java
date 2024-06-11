
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.helper.Entities;
import org.jsoup.helper.StringUtil;
import java.io.IOException;
import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key";
        assertEquals(expected, attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        String newKey = "newKey";
        attribute.setKey(newKey);
        String expected = "newKey";
        assertEquals(expected, attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "value";
        assertEquals(expected, attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        attribute.setValue(newValue);
        String expected = "newValue";
        assertEquals(expected, attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = new StringBuilder("<key value=\"value\">");
        attribute.html(expected, null);
        String actual = expected.toString();
        assertEquals(actual, attribute.html());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "key=\"value\"";
        Attribute expected = new Attribute(unencodedKey, "value");
        Attribute actual = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testNullKey() {
        Attribute attribute = new Attribute(null, "value");
        Validate.notNull(attribute.getKey(), "Attribute key should not be null");
    }

    @Test
    public void testEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        Validate.notEmpty(attribute.getKey(), "Attribute key should not be empty");
    }

    @Test
    public void testKeyWithSpecialCharacters() {
        Attribute attribute = new Attribute("key<>", "value");
        StringBuilder expected = new StringBuilder("<key<> value=\"value\">");
        attribute.html(expected, null);
        String actual = expected.toString();
        assertEquals(actual, attribute.html());
    }

    @Test
    public void testSettingSameKeyMultipleTimes() {
        Attribute parent = new Attributes();
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        parent.put(attribute1);
        parent.put(attribute2);
        Attribute expected = attribute2;
        Attribute actual = parent.get("key");
        assertEquals(expected, actual);
    }

    @Test
    public void testSettingKeyWhenParentIsNull() {
        Attribute attribute = new Attribute("key", "value");
        assertThrows(IllegalArgumentException.class, () -> attribute.setParent(null));
    }

    @Test
    public void testSettingKeyWhenParentAlreadyContainsTheSameKey() {
        Attribute parent = new Attributes();
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        parent.put(attribute1);
        Attribute actual = null;
        try {
            actual = new Attribute("key", "value3");
            parent.put(actual);
        } catch (IllegalArgumentException e) {
        }
        assertEquals(null, actual);
    }

    @Test
    public void testNullValue() {
        Attribute attribute = new Attribute("key", null);
        String expected = "key";
        assertEquals(expected, attribute.getKey());
        assertNull(attribute.getValue());
    }

    @Test
    public void testEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        String expected = "key";
        assertEquals(expected, attribute.getKey());
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testValueLongerThanMaxAllowedLength() {
        Attribute attribute = new Attribute("key", "A" + new String(new char[1024 * 1024 * 10]) + "value");
        assertThrows(IndexOutOfBoundsException.class, () -> attribute.html(null, null));
    }

    @Test
    public void testSettingKeyWithSpecialCharactersThatNeedToBeEscapedInHTML() {
        Attribute attribute = new Attribute("key<>", "value");
        StringBuilder expected = new StringBuilder("<key<> value=\"value\">");
        attribute.html(expected, null);
        String actual = expected.toString();
        assertEquals(actual, attribute.html());
    }

    @Test
    public void testCreateAttributeWithNullUnencodedKey() {
        assertThrows(NullPointerException.class, () -> Attribute.createFromEncoded(null, ""));
    }

    @Test
    public void testCreateAttributeWithEmptyUnencodedKey() {
        assertThrows(IllegalArgumentException.class, () -> Attribute.createFromEncoded("", ""));
    }

    @Test
    public void testCreateAttributeWithUnencodedKeyContainingSpecialCharacters() {
        String unencodedKey = "key<>";
        String encodedValue = "key<=\"value\">";
        Attribute expected = new Attribute(unencodedKey, "value");
        Attribute actual = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateAttributeWithEncodedValueNotProperlyEncoded() {
        String unencodedKey = "key";
        String encodedValue = "key=\"value<";
        assertThrows(SerializationException.class, () -> Attribute.createFromEncoded(unencodedKey, encodedValue));
    }
}