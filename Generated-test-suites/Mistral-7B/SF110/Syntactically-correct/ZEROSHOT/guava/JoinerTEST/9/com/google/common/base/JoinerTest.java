import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static com.google.common.base.Joiner.on;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testJoinerConstruction() {
 Joiner joiner = on('');
 checkNotNull(joiner);
 }

 @Test
 public void testJoinerWithStringSeparator() {
 Joiner joiner = on(",");
 String joined = joiner.join(Arrays.asList("one", "two", "three"));
 assertEquals("one,two,three", joined);
 }

 @Test
 public void testJoinerWithCharSeparator() {
 Joiner joiner = on('-');
 String joined = joiner.join(Arrays.asList("one", "two", "three"));
 assertEquals("one-two-three", joined);
 }

 @Test(expected = NullPointerException.class)
 public void testJoinerWithNullSeparator() {
 Joiner joiner = new Joiner(null);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = on(",");
 joiner.appendTo(stringWriter, Arrays.asList("one", "two", "three"));
 assertEquals("one,two,three", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = on(",");
 joiner.appendTo(stringWriter, new Object[]{"one", "two", "three"});
 assertEquals("one,two,three", stringWriter.toString());
 }

 @Test
 public void testAppendToSingleObject() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = on(",");
 Joiner joinerWithNull = joiner.useForNull("null");
 joinerWithNull.appendTo(stringWriter, "one");
 assertEquals("one", stringWriter.toString());
 joinerWithNull.appendTo(stringWriter, null);
 assertEquals("one,null", stringWriter.toString());
 }

 @Test
 public void testAppendToNullAndNotSkipNulls() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = on(",");
 joiner.appendTo(stringWriter, new Object[]{"one", null, "three"});
 assertEquals("one,null,three", stringWriter.toString());
 }

 @Test
 public void testAppendToNullAndSkipNulls() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Joiner joiner = on(",");
 Joiner joinerWithNull = joiner.skipNulls();
 joinerWithNull.appendTo(stringWriter, new Object[]{"one", null, "three"});
 assertEquals("one,three", stringWriter.toString());
 }

 @Test
 public void testJoinIterable() {
 String joined = Joiner.on(",").join(Arrays.asList("one", "two", "three"));
 assertEquals("one,two,three", joined);
 }

 @Test
 public void testJoinObjectArray() {
 String joined = Joiner.on(",").join(new Object[]{"one", "two", "three"});
 assertEquals("one,two,three", joined);
 }

 @Test
 public void testJoinSingleObject() {
 String joined = Joiner.on(",").join("one");
 assertEquals("one", joined);
 }

 @Test
 public void testJoinNullAndNotSkipNulls() {
 String joined = Joiner.on(",").join(new Object[]{"one", null, "three"});
 assertEquals("one,null,three", joined);
 }

 @Test
 public void testJoinNullAndSkipNulls() {
 String joined = Joiner.on(",").skipNulls().join(new Object[]{"one", null, "three"});
 assertEquals("one,three", joined);
 }

 @Test
 public void testMapJoinerConstruction() {
 Joiner joiner = on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 checkNotNull(mapJoiner);
 }

 @Test
 public void testMapJoinerAppendToMapEntryIterator() throws IOException {
 StringWriter stringWriter = new StringWriter();
 MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
 Map<String, String> map = Maps.newHashMap();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 mapJoiner.appendTo(stringWriter, map.entrySet());
 assertEquals("key1:value1,key2:value2,key3:value3", stringWriter.toString());
 }

 @Test
 public void testMapJoinerAppendToMapEntryIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
 List<Map.Entry<String, String>> entryList = Lists.newArrayList(
 Map.entry("key1", "value1"), Map.entry("key2", "value2"), Map.entry("key3", "value3")
 );
 mapJoiner.appendTo(stringWriter, entryList);
 assertEquals("key1:value1,key2:value2,key3:value3", stringWriter.toString());
 }

 @Test
 public void testMapJoinerJoinMapEntryIterator() {
 String joined = Joiner.on(",").withKeyValueSeparator(":").join(Map.entrySet(Maps.newHashMap()));
 assertEquals("", joined);
 Map<String, String> map = Maps.newHashMap();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 joined = Joiner.on(",").withKeyValueSeparator(":").join(map.entrySet());
 assertEquals("key1:value1,key2:value2,key3:value3", joined);
 }

 @Test
 public void testMapJoinerJoinMapEntryIterable() {
 String joined = Joiner.on(",").withKeyValueSeparator(":").join(Collections.emptyList());
 assertEquals("", joined);
 List<Map.Entry<String, String>> entryList = Lists.newArrayList(
 Map.entry("key1", "value1"), Map.entry("key2", "value2"), Map.entry("key3", "value3")
 );
 joined = Joiner.on(",").withKeyValueSeparator(":").join(entryList);
 assertEquals("key1:value1,key2:value2,key3:value3", joined);
 }

 @Test
 public void testMapJoinerUseForNull() {
 MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":").useForNull("null");
 Map<String, String> map = Maps.newHashMap();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 StringWriter stringWriter = new StringWriter();
 mapJoiner.appendTo(stringWriter, map.entrySet());
 assertEquals("key1:value1,key2:null,key3:value3", stringWriter.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapJoinerUseForNullAndMapJoiner() {
 MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":").useForNull("null");
 mapJoiner.skipNulls();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapJoinerWithKeyValueSeparatorAndMapJoiner() {
 MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":").skipNulls();
 mapJoiner.useForNull("null");
 }
}