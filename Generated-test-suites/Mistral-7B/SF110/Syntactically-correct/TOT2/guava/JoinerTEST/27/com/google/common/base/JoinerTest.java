import static org.junit.Assert.*;
import static com.google.common.base.Joiner.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testAppendToIterableAppendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder expected = new StringBuilder();
        expected.append("apple:banana:orange");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("apple", "banana", "orange"));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToIterableAppendableIOException() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder appendable = new StringBuilder();
        FileOutputStream outputStream = new FileOutputStream("test.txt");
        try {
            joiner.appendTo(appendable, Arrays.asList("apple", "banana", "orange"), outputStream);
        } finally {
            outputStream.close();
        }
    }

    @Test
    public void testAppendToEmptyIterableAppendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Collections.emptyList());
        assertEquals("", actual.toString());
    }

    @Test
    public void testAppendToEmptyAppendableIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, Arrays.asList("apple", "banana", "orange"));
        assertEquals("", appendable.toString());
    }

    @Test
    public void testAppendToNullIterableAppendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, null);
        assertEquals("", actual.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToNullIterableNullAppendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        String nullAppendable = null;
        joiner.appendTo(nullAppendable, Arrays.asList("apple", "banana", "orange"));
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToIterableNullAppendable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, null);
        assertNotSame("", actual.toString());
        joiner.appendTo(actual, Arrays.asList(null));
    }

    @Test(expected = NullPointerException.class)
    public void testIterableNull() {
        Joiner joiner = Joiner.on(":");
        joiner.appendTo(new StringBuilder(), null);
    }
}