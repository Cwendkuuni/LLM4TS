import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @Test
    public void testJoinerCreation() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        Joiner joinerWithPrototype = new Joiner(joiner);
        Joiner joinerWithNullText = joiner.useForNull("NA");
        Joiner joinerWithSkipNulls = joiner.skipNulls();
        Joiner joinerWithMapJoiner = joiner.withKeyValueSeparator(",");

        assertNotNull(joiner);
        assertNotNull(joinerWithPrototype);
        assertNotNull(joinerWithNullText);
        assertNotNull(joinerWithSkipNulls);
        assertNotNull(joinerWithMapJoiner);
        assertEquals(separator, joiner.separator);
        assertEquals(joiner, joinerWithPrototype);
        assertNotSame(joiner, joinerWithNullText);
        assertNotSame(joiner, joinerWithSkipNulls);
        assertNotSame(joiner, joinerWithMapJoiner);
    }

    @Test
    public void testAppendTo() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();

        // Iterable
        joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringWriter.toString());

        // Object[]
        joiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", stringWriter.toString());

        // Iterator
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Iterator<String> iterator = list.iterator();
        joiner.appendTo(stringWriter, iterator);
        assertEquals("apple,banana,cherry", stringWriter.toString());

        // null
        String nullText = "NA";
        Joiner joinerWithNullText = joiner.useForNull(nullText);
        joinerWithNullText.appendTo(stringWriter, Arrays.asList("apple", null, "cherry"));
        assertEquals("apple,NA,cherry", stringWriter.toString());

        // MapJoiner
        Map<String, String> map = ImmutableMap.of("apple", "fruit", "banana", "tree", "cherry", "berry");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(", ");
        StringWriter stringWriterMap = new StringWriter();
        mapJoiner.appendTo(stringWriterMap, map);
        assertEquals("apple: fruit, banana: tree, cherry: berry", stringWriterMap.toString());
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");

        // Iterable
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry")));

        // Object[]
        assertEquals("apple,banana,cherry", joiner.join(new Object[]{"apple", "banana", "cherry"}));

        // Iterator
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Iterator<String> iterator = list.iterator();
        assertEquals("apple,banana,cherry", joiner.join(iterator));

        // null
        String nullText = "NA";
        Joiner joinerWithNullText = joiner.useForNull(nullText);
        assertEquals("apple,NA,cherry", joinerWithNullText.join(Arrays.asList("apple", null, "cherry")));

        // MapJoiner
        Map<String, String> map = ImmutableMap.of("apple", "fruit", "banana", "tree", "cherry", "berry");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(", ");
        assertEquals("apple: fruit, banana: tree, cherry: berry", mapJoiner.join(map));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");

        // Iterable
        assertEquals("apple,cherry", joiner.skipNulls().join(Arrays.asList("apple", null, "cherry")));

        // Object[]
        assertEquals("apple,cherry", joiner.skipNulls().join(new Object[]{"apple", null, "cherry"}));

        // Iterator
        List<String> list = Arrays.asList("apple", null, "cherry");
        Iterator<String> iterator = list.iterator();
        assertEquals("apple,cherry", joiner.skipNulls().join(iterator));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");

        // Iterable
        assertEquals("apple,NA,cherry", joiner.useForNull("NA").join(Arrays.asList("apple", null, "cherry")));

        // Object[]
        assertEquals("apple,NA,cherry", joiner.useForNull("NA").join(new Object[]{"apple", null, "cherry"}));

        // Iterator
        List<String> list = Arrays.asList("apple", null, "cherry");
        Iterator<String> iterator = list.iterator();
        assertEquals("apple,NA,cherry", joiner.useForNull("NA").join(iterator));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");

        // Iterable
        Map<String, String> map = ImmutableMap.of("apple", "fruit", "banana", "tree", "cherry", "berry");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(", ");
        assertEquals("apple: fruit, banana: tree, cherry: berry", mapJoiner.join(map));

        // Object[]
        Map<String, String> mapObject = ImmutableMap.of("apple", "fruit", "banana", null, "cherry", "berry");
        Map<String, String> mapWithoutNull = ImmutableMap.of("apple", "fruit", "cherry", "berry");
        MapJoiner mapJoinerWithNullText = joiner.withKeyValueSeparator(", ").useForNull("NA");
        assertEquals("apple: fruit, banana: NA, cherry: berry", mapJoinerWithNullText.join(mapObject));
        assertEquals("apple: fruit, cherry: berry", mapJoinerWithNullText.join(mapWithoutNull));
    }
}