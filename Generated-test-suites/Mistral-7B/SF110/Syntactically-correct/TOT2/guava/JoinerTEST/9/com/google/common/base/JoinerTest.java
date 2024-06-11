import org.junit.Test;
import org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class JoinerTest {
    @Test
    public void testJoinerInitialization() {
        Joiner joiner1 = Joiner.on(".");
        Joiner joiner2 = Joiner.on('-');
        assertEquals(".", joiner1.separator);
        assertEquals("-", joiner2.separator);
    }
    
    @Test
    public void testAppendTo() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        String[] parts = {"Apple", "Banana", "Orange"};
        joiner.appendTo(writer, Arrays.asList(parts));

        String expected = "Apple,Banana,Orange";
        assertEquals(expected, outputStream.toString());
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        Object[] parts = {"Apple", "Banana", "Orange"};
        joiner.appendTo(writer, parts);

        String expected = "Apple,Banana,Orange";
        assertEquals(expected, outputStream.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"Apple", "Banana", "Orange"};

        String expected = "Apple,Banana,Orange";
        assertEquals(expected, joiner.join(Arrays.asList(parts)));
    }
    
    @Test
    public void testJoinIterableEmpty() {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        Iterable<String> emptyList = Collections.emptyList();
        joiner.appendTo(writer, emptyList);

        String expected = "";
        assertEquals(expected, outputStream.toString());
    }
    
    @Test
    public void testAppendToObjectArrayEmpty() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        Object[] emptyArray = {};
        joiner.appendTo(writer, emptyArray);

        String expected = "";
        assertEquals(expected, outputStream.toString());
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithNullText = joiner.useForNull("(null)");

        String[] parts = {"Apple", null, "Orange"};
        StringBuilder builder = new StringBuilder();
        joinerWithNullText.appendTo(builder, parts);

        String expected = "Apple,(null),Orange";
        assertEquals(expected, builder.toString());
    }
    
    @Test
    public void testJoinIteratorEmpty() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        Iterator<String> emptyIterator = Collections.emptyIterator();
        joiner.appendTo(writer, emptyIterator);

        String expected = "";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testJoinObjectArrayEmpty() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, (Iterable<?>) new Object[0]);

        String expected = "";
        assertEquals(expected, builder.toString());
    }

    @Test
    public void testJoinNullParts() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, (Iterable<?>)null);

        String expected = "";
        assertEquals(expected, builder.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithIOException() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        writer.write("error message");
        joiner.appendTo(writer, Arrays.asList("Apple", "Banana", "Orange"));
    }

    @Test
    public void testJoinNullSeparator() {
        Joiner joiner = Joiner.on(null);
        String joined = joiner.join(Arrays.asList("Apple", "Banana", "Orange"));

        String expected = null;
        assertEquals(expected, joined);
    }
}