import org.junit.Test;
import org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class JoinerTest {
    @Test
    public void testJoinerWithStringSeparator() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        String expected = "item1|item2|item3";
        String actual = joiner.join(Arrays.asList("item1", "item2", "item3"));
        assertEquals(expected, actual);
    }

    @Test
    public void testNullSeparator() {
        Joiner joiner = Joiner.on(null);
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("item1", "item2", "item3"));
        String expected = "item1|item2|item3";
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendToWithEmptyIterable() throws IOException {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Collections.emptyList());
        String actual = stringWriter.toString();
        assertEquals("", actual);
    }

    @Test
    public void testNullAppendable() {
        Joiner joiner = Joiner.on("|");
        joiner.appendTo(null, Arrays.asList("item1", "item2", "item3"));
    }

    @Test
    public void testNullIterable() {
        Joiner joiner = Joiner.on("|");
        joiner.appendTo(new StringWriter(), null);
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new Object[]{"item1", "item2", "item3"});
        String expected = "item1|item2|item3";
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendToWithEmptyObjectArray() throws IOException {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new Object[]{});
        String actual = stringWriter.toString();
        assertEquals("", actual);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAppendableWithObjectArray() {
        Joiner joiner = Joiner.on("|");
        joiner.appendTo(null, new Object[]{"item1", "item2", "item3"});
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectArray() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        joiner.appendTo(new StringWriter(), (Object[])null);
    }

    @Test
    public void testJoinWithNull() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        String expected = "";
        String actual = joiner.join(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithEmptyIterable() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        String expected = "";
        String actual = joiner.join(Collections.emptyList());
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithSingleObject() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator);
        String expected = "item";
        String actual = joiner.join(Arrays.asList("item"));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithIterableContainingNull() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator).useForNull("null");
        String expected = "item1|null|item3";
        String actual = joiner.join(Arrays.asList("item1", null, "item3"));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithMap() {
        String separator = "=";
        Joiner joiner = Joiner.on(separator).withKeyValueSeparator(separator);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String expected = "key1=value1=key2=value2";
        String actual = joiner.join(map);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMap() {
        String separator = "|";
        Joiner joiner = Joiner.on(separator).withKeyValueSeparator(separator);
        joiner.join((Map<String, String>)null);
    }
}