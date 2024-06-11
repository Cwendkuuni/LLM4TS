import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JoinerTest {

    private Joiner joiner;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }

    @Test
    public void testAppendToIterable() throws IOException {
        outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringBuilder.toString());
    }

    @Test
    public void testJoinIterable() {
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test
    public void testJoinObjectArray() {
        assertEquals("apple,banana,cherry", joiner.join(new Object[]{"apple", "banana", "cherry"}));
    }

    @Test
    public void testJoinSingleObject() {
        assertEquals("apple", joiner.join(Arrays.asList("apple")));
    }

    @Test(expected = NullPointerException.class)
    public void testJoinNullIterator() {
        joiner.join(null);
    }

    @Test
    public void testUseForNull() {
        joiner = joiner.useForNull("NA");
        assertEquals("NA,apple,NA,banana,NA,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test
    public void testSkipNulls() {
        joiner = joiner.skipNulls();
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", null, "banana", null, "cherry")));
    }

    @Test
    public void testMapJoiner() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);
        map.put("Cherry", 3);

        Joiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        ByteArrayOutputStream mapOutput = new ByteArrayOutputStream();
        mapJoiner.appendTo(mapOutput, map);

        assertEquals("Apple:1,Banana:2,Cherry:3", mapOutput.toString());
    }
}