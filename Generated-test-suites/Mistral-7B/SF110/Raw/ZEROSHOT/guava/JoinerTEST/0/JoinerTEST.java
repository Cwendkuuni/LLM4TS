import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 @Test
 public void testJoinerCreation() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerWithCharSeparator = Joiner.on(',');
 joiner = joiner.useForNull("N/A");
 Joiner joinerWithNullText = joiner.skipNulls();
 Joiner joinerWithMapSeparator = joiner.withKeyValueSeparator(":");
 Joiner joinerWithPrototype = new Joiner(joiner);
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 String expected = "apple,banana,orange";
 Joiner joiner = Joiner.on(",");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 String expected = "apple,banana,orange";
 Joiner joiner = Joiner.on(",");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, new Object[]{"apple", "banana", "orange"});
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendToIterableWithNull() throws IOException {
 String expected = "apple,orange";
 Joiner joiner = Joiner.on(",");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, Lists.newArrayList("apple", null, "orange"));
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendToObjectArrayWithNull() throws IOException {
 String expected = "apple,orange";
 Joiner joiner = Joiner.on(",");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, new Object[]{"apple", null, "orange"});
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testJoinIterable() {
 String expected = "apple,banana,orange";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(Arrays.asList("apple", "banana", "orange")));
 }

 @Test
 public void testJoinObjectArray() {
 String expected = "apple,banana,orange";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(new Object[]{"apple", "banana", "orange"}));
 }

 @Test
 public void testJoinIterableWithNull() {
 String expected = "apple,orange";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(Lists.newArrayList("apple", null, "orange")));
 }

 @Test
 public void testJoinObjectArrayWithNull() {
 String expected = "apple,orange";
 Joiner joiner = Joiner.on(",");
 assertEquals(expected, joiner.join(new Object[]{"apple", null, "orange"}));
 }

 @Test
 public void testUseForNull() throws IOException {
 String expected = "N/A,apple,orange";
 Joiner joiner = Joiner.on(",");
 StringBuilder stringBuilder = new StringBuilder();
 joiner = joiner.useForNull("N/A");
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "orange"));
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testSkipNulls() throws IOException {
 String expected = "apple,orange";
 Joiner joiner = Joiner.on(",");
 StringBuilder stringBuilder = new StringBuilder();
 joiner = joiner.skipNulls();
 joiner.appendTo(stringBuilder, new Object[]{null, "apple", null, "orange"});
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testMapJoinerAppendToMap() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 String expected = "key1:value1,key2:value2,key3:value3";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testMapJoinerJoinMap() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String expected = "key1:value1,key2:value2,key3:value3";
 assertEquals(expected, mapJoiner.join(map));
 }

 @Test
 public void testMapJoinerUseForNull() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 mapJoiner = mapJoiner.useForNull("N/A");
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 String expected = "N/A:key1:value1,key3:value3";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testMapJoinerSkipNulls() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("key1", null);
 map.put("key2", "value2");
 map.put("key3", "value3");
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 mapJoiner = mapJoiner.skipNulls();
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 String expected = "key2:value2,key3:value3";
 assertEquals(expected, stringBuilder.toString());
 }
}