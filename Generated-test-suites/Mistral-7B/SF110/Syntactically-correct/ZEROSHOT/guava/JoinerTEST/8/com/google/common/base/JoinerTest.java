import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JoinerTest {

 private Joiner separatorJoiner;
 private Joiner charJoiner;
 private StringBuilder stringBuilder;
 private Appendable appendable;
 private Map<String, String> map;

 @Before
 public void setUp() {
 separatorJoiner = Joiner.on("|");
 charJoiner = Joiner.on('$');
 stringBuilder = new StringBuilder();
 appendable = new StringBuffer();
 map = new ConcurrentHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 }

 @Test
 public void testJoinerCreation() {
 assertNotNull(separatorJoiner);
 assertNotNull(charJoiner);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 separatorJoiner.appendTo(appendable, Arrays.asList("test1", "test2", "test3"));
 assertEquals("test1|test2|test3", appendable.toString());

 charJoiner.appendTo(stringBuilder, Arrays.asList('a', 'b', 'c'));
 assertEquals("abc", stringBuilder.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 separatorJoiner.appendTo(appendable, new Object[]{"test1", "test2", "test3"});
 assertEquals("test1|test2|test3", appendable.toString());

 charJoiner.appendTo(stringBuilder, new Object[]{'a', 'b', 'c'});
 assertEquals("abc", stringBuilder.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 separatorJoiner.appendTo(appendable, Arrays.asList("test1", "test2", "test3").iterator());
 assertEquals("test1|test2|test3", appendable.toString());

 charJoiner.appendTo(stringBuilder, Arrays.asList('a', 'b', 'c').iterator());
 assertEquals("abc", stringBuilder.toString());
 }

 @Test
 public void testAppendToMapEntrySet() throws IOException {
 separatorJoiner.appendTo(appendable, map.entrySet());
 assertEquals("key1|value1|key2|value2", appendable.toString());

 charJoiner.appendTo(stringBuilder, map.entrySet());
 assertEquals("key1$value1$key2$value2", stringBuilder.toString());
 }

 @Test
 public void testJoinIterable() {
 assertEquals("test1|test2|test3", separatorJoiner.join(Arrays.asList("test1", "test2", "test3")));
 assertEquals("abc", charJoiner.join(Arrays.asList('a', 'b', 'c')));
 }

 @Test
 public void testJoinObjectArray() {
 assertEquals("test1|test2|test3", separatorJoiner.join(new Object[]{"test1", "test2", "test3"}));
 assertEquals("abc", charJoiner.join(new Object[]{'a', 'b', 'c'}));
 }

 @Test
 public void testJoinMapEntrySet() {
 assertEquals("key1|value1|key2|value2", separatorJoiner.join(map.entrySet()));
 assertEquals("key1$value1$key2$value2", charJoiner.join(map.entrySet()));
 }

 @Test
 public void testUseForNull() {
 Joiner nullJoiner = separatorJoiner.useForNull("NULL");
 assertEquals("test1|NULL|test2|NULL|test3", nullJoiner.join(Arrays.asList("test1", null, "test2", null, "test3")));

 Joiner mapNullJoiner = separatorJoiner.useForNull("NULL");
 assertEquals("key1|value1|NULL|value2|NULL", mapNullJoiner.join(map.entrySet()));
 }

 @Test
 public void testSkipNulls() {
 Joiner nullJoiner = separatorJoiner.skipNulls();
 assertEquals("test1|test2|test3", nullJoiner.join(Arrays.asList("test1", null, "test2", null, "test3")));

 Joiner mapNullJoiner = separatorJoiner.skipNulls();
 assertEquals("key1|value1|key2|value2", mapNullJoiner.join(map.entrySet()));
 }

 @Test
 public void testWithKeyValueSeparator() {
 Joiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");
 String join = mapJoiner.join(map);
 assertNotNull(join);
 String[] split = join.split(separatorJoiner.separator);
 assertEquals(split.length, 3);
 assertEquals(split[0], "key1:value1");
 assertEquals(split[1], "key2:value2");
 }
}