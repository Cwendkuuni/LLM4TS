import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(List.of("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinerOnCharacter() {
        Joiner joiner = Joiner.on('-');
        String result = joiner.join(List.of("apple", "banana", "cherry"));
        assertEquals("apple-banana-cherry", result);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, List.of("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToNullFirstAndSecond() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, null, null, "apple", "banana", "cherry");
        assertEquals("null,null,apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToIterableStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, List.of("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringBuilder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(List.of("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinNullFirstAndSecond() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(null, null, "apple", "banana", "cherry");
        assertEquals("null,null,apple,banana,cherry", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        String result = joiner.join(List.of("apple", null, "banana", null, "cherry"));
        assertEquals("apple,NA,banana,NA,cherry", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join(List.of("apple", null, "banana", null, "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = ImmutableMap.of("name", "John Doe", "age", "30");
        String result = mapJoiner.join(map.entrySet());
        assertEquals("name:John Doe,age:30", result);
    }
}