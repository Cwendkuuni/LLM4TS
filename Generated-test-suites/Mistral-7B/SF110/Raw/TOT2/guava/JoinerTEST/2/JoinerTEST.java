import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.*;

public class JoinerTest {

    @Test
    public void testAppendTo_Iterable() throws IOException {
        String expected = "test1 test2 test3";
        StringBuilder appendable = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(appendable, Arrays.asList("test1", "test2", "test3"));
        assertEquals(expected, appendable.toString());

        // Edge cases and exception handling tests
        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        joiner.appendTo(appendable, Collections.emptyList()); // Empty Iterable
        assertEquals("", appendable.toString());

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        try {
            joiner.appendTo(appendable, null); // Null Iterable
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        joiner.appendTo(appendable, Arrays.asList("test1")); // Iterable containing a single element
        assertEquals("test1", appendable.toString());

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        joiner.appendTo(appendable, Arrays.asList("test1", null, "test3")); // Iterable containing null elements
        assertEquals("test1, null, test3", appendable.toString());

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        joiner.appendTo(appendable, Arrays.asList(new Object() { /* Custom object */ })); // Iterable containing custom objects
        assertEquals("", appendable.toString()); // Custom objects should not be appended due to the implementation of Joiner

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        try {
            joiner.appendTo(appendable, Arrays.asList(new StringBuilder("test1")).iterator()); // IOException during appending
            fail();
        } catch (IOException e) {
            // Expected
        }

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        try {
            joiner.appendTo(appendable, appendable); // Appendable is null
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAppendTo_ObjectArray() throws IOException {
        String expected = "test1 test2 test3";
        StringBuilder appendable = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(appendable, new Object[]{"test1", "test2", "test3"});
        assertEquals(expected, appendable.toString());

        // Edge cases and exception handling tests
        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        joiner.appendTo(appendable, new Object[0]); // Empty Object Array
        assertEquals("", appendable.toString());

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        try {
            joiner.appendTo(appendable, new Object[]{null, "test2", "test3"}); // Object Array containing null elements
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        joiner.appendTo(appendable, new Object[]{"test1", new Object() { /* Custom object */ }}); // Object Array containing custom objects
        assertEquals("test1", appendable.toString()); // Custom objects should not be appended due to the implementation of Joiner

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        try {
            joiner.appendTo(appendable, appendable.iterator()); // IOException during appending
            fail();
        } catch (IOException e) {
            // Expected
        }

        appendable = new StringBuilder();
        joiner = Joiner.on(",");
        try {
            joiner.appendTo(appendable, appendable); // Appendable is null
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }
}