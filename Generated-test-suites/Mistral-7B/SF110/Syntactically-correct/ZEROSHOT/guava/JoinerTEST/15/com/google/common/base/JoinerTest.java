import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class JoinerTest {

    @Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator());
    }

    @Test
    public void testJoinerWithCharSeparator() {
        Joiner joiner = Joiner.on('-');
        assertNotNull(joiner);
        assertEquals("-", joiner.separator());
    }

    @Test
    public void testAppendTo_Iterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendTo_ObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        joiner.appendTo(outputStream, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendTo_Iterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(outputStream, iterator);
        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendTo_Appendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuffer stringBuffer = new StringBuffer();
        joiner.appendTo(stringBuffer, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringBuffer.toString());
    }

    @Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry")));
        assertEquals("", joiner.join(Collections.emptyList()));
    }

    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(new Object[]{"apple", "banana", "cherry"}));
        assertEquals("", joiner.join(new Object[0]));
    }

    @Test
    public void testJoin_Iterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", "banana", "cherry").iterator()));
    }

    @Test
    public void testJoinWithNullText_Iterable() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        assertEquals("apple,NA,banana,NA,cherry", joiner.join(Arrays.asList("apple", null, "banana", null, "cherry")));
    }

    @Test
    public void testJoinWithNullText_ObjectArray() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        assertEquals("apple,NA,banana,NA,cherry", joiner.join(new Object[]{"apple", null, "banana", null, "cherry"}));
    }

    @Test
    public void testJoinWithNullText_Iterator() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        Iterator<Object> iterator = Arrays.asList("apple", null, "banana", null, "cherry").iterator();
        assertEquals("apple,NA,banana,NA,cherry", joiner.join(iterator));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("apple,banana,cherry", joiner.join(new Object[]{"apple", null, "banana", null, "cherry"}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAndSkipNulls() {
        Joiner joiner = Joiner.on(",").useForNull("NA").skipNulls();
    }

    @Test
    public void testAppendTo_MapEntryIterator() throws IOException {
        Joiner joiner = Joiner.on("->");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> map = new HashMap<>();
        map.put("apple", "one");
        map.put("banana", "two");
        map.put("cherry", "three");
        Iterator<Map.Entry<String, String>> mapIterator = map.entrySet().iterator();
        joiner.appendTo(outputStream, mapIterator);
        assertEquals("apple->one->banana->two->cherry->three", outputStream.toString());
    }

    @Test
    public void testJoin_MapEntryIterator() {
        Joiner joiner = Joiner.on("->");
        String result = joiner.join(mapEntryIterator("apple", "one", "banana", "two", "cherry", "three"));
        assertEquals("apple->one->banana->two->cherry->three", result);
    }

    @Test
    public void testUseForNull_MapJoiner() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("->");
        assertEquals("apple->one,NA->banana->two,NA->cherry->three", mapJoiner.join(mapEntryIterator("apple", "one", "banana", null, "cherry", "three")));
    }

    @Test
    public void testSkipNulls_MapJoiner() {
        Joiner joiner = Joiner.on(",").skipNulls();
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("->");
        assertEquals("apple->one,banana->two,cherry->three", mapJoiner.join(mapEntryIterator("apple", "one", "banana", null, "cherry", "three")));
    }

    private Iterator<Map.Entry<String, String>> mapEntryIterator(String... pairs) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        return map.entrySet().iterator();
    }

    @Test
    public void testNullChecks() {
        Joiner joiner = Mockito.spy(Joiner.on(","));
        assertThrows(NullPointerException.class, () -> joiner.appendTo(null, Arrays.asList("apple", "banana", "cherry")));
        assertThrows(NullPointerException.class, () -> joiner.appendTo(new StringBuffer(), null));
        assertThrows(NullPointerException.class, () -> joiner.join(null));
    }
}