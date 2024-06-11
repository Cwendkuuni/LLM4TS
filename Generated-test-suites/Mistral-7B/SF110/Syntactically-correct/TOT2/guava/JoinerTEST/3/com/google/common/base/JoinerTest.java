import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.common.base.Joiner;
import org.junit.Test;

public class JoinerTest {
    @Test
    public void testAppendTo() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("test1").append(",").append("test2").append(",").append("test3");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("test1", "test2", "test3"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendTo_NullIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        Iterable<String> iterable = null;
        joiner.appendTo(actual, iterable);
        assertEquals("", actual.toString());
    }

    @Test
    public void testAppendTo_EmptyIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        Iterable<String> iterable = Collections.emptyList();
        joiner.appendTo(actual, iterable);
        assertEquals("", actual.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_NullIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        Iterator<String> iterator = null;
        joiner.appendTo(actual, iterator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendTo_NonStringElement() {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        Iterator<Object> iterator = Arrays.asList(1, "test2", 3).iterator();
        joiner.appendTo(actual, iterator);
        assertEquals("1,test2,3", actual.toString()); // unexpected result
    }

    @Test(expected = IOException.class)
    public void testAppendTo_IOException() {
        Joiner joiner = Joiner.on(",");
        StringWriter writer = new StringWriter();
        Iterator<String> iterator = Arrays.asList("test1", "test2", "test3").iterator();
        assertThrows(IOException.class, () -> joiner.appendTo(writer, iterator));
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String expected = "test1,test2,test3";
        String actual = joiner.join(Arrays.asList("test1", "test2", "test3"));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_EmptyList() {
        Joiner joiner = Joiner.on(",");
        String expected = "";
        String actual = joiner.join(Collections.emptyList());
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_NullElement() {
        Joiner joiner = Joiner.on(",");
        String expected = null;
        String actual = joiner.join(Arrays.asList(null, "test2", "test3"));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_UseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        String expected = "test1,NA,test2,test3";
        String actual = joiner.join(Arrays.asList("test1", null, "test2", "test3"));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoin_NullIterable() {
        Joiner joiner = Joiner.on(",");
        String actual = joiner.join(null);
        assertEquals(null, actual);
    }

    @Test
    public void testAppendTo_StringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("test1").append(",").append("test2").append(",").append("test3");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("test1", "test2", "test3"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendTo_StringBuilder_EmptyIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Collections.emptyList());
        assertEquals("", actual.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_StringBuilder_NullIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        Iterator<String> iterator = null;
        joiner.appendTo(actual, iterator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendTo_StringBuilder_NonStringElement() {
        Joiner joiner = Joiner.on(",");
        StringBuilder actual = new StringBuilder();
        Iterator<Object> iterator = Arrays.asList(1, "test2", 3).iterator();
        joiner.appendTo(actual, iterator);
        assertEquals("1,test2,3", actual.toString()); // unexpected result
    }
}