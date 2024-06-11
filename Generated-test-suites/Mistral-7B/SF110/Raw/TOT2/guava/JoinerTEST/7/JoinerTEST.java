import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class JoinerTest {

    @Test
    public void testAppendTo_Iterable() throws IOException {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        List<String> parts = Arrays.asList("apple", "banana", "orange");
        StringBuilder expected = new StringBuilder("apple,");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, parts.iterator());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_NullAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        String appendable = null;
        joiner.appendTo(appendable, Arrays.asList("test").iterator());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_NullParts() throws IOException {
        Joiner joiner = Joiner.on(",");
        Iterator<Object> parts = null;
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
    }

    @Test
    public void testAppendTo_EmptyParts() throws IOException {
        Joiner joiner = Joiner.on(",");
        Iterator<Object> parts = Collections.emptyIterator();
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("", appendable.toString());
    }

    @Test
    public void testAppendTo_DifferentTypesParts() throws IOException {
        Joiner joiner = Joiner.on(",");
        Iterator<Object> parts = Arrays.asList("apple", 1, 'a').iterator();
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, parts);
        assertEquals("apple,1,a", appendable.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendTo_ExceptionDuringIteration() throws IOException {
        Joiner joiner = Joiner.on(",");
        String[] parts = new String[]{"apple", "banana"};
        StringBuilder appendable = new StringBuilder();
        Iterator<Object> partsIterator = Arrays.asList(parts).iterator();
        partsIterator.next();
        throw new IOException();
        joiner.appendTo(appendable, partsIterator);
    }

    @Test
    public void testJoin_Iterable() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        List<String> parts = Arrays.asList("apple", "banana", "orange");
        String expected = "apple,banana,orange";
        String actual = joiner.join(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void testJoin_StringArray() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        String[] parts = new String[]{"apple", "banana", "orange"};
        String expected = "apple,banana,orange";
        String actual = joiner.join(parts);
        assertEquals(expected, actual);
    }
}