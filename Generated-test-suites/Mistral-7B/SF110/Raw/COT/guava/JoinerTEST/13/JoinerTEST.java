import static org.junit.Assert.*;
import com.google.common.base.*;
import org.junit.Test;

public class JoinerTest {

 private static final Joiner JOINER = Joiner.on(":");

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder("apple:banana:cherry");
 StringBuilder actual = new StringBuilder();
 JOINER.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToIterableWithNullAppendable() throws IOException {
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 String appendable = null;
 JOINER.appendTo(appendable, parts);
 fail("Expecting IOException");
 }

 @Test
 public void testAppendToIterator() throws IOException {
 Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
 StringBuilder expected = new StringBuilder("apple:banana:cherry");
 StringBuilder actual = new StringBuilder();
 JOINER.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToIteratorWithNullIterator() throws IOException {
 StringBuilder actual = new StringBuilder();
 Joiner joiner = new Joiner(null);
 joiner.appendTo(actual, null);
 fail("Expecting IOException");
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 Object[] parts = new Object[]{"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder("apple:banana:cherry");
 StringBuilder actual = new StringBuilder();
 JOINER.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToObjectArrayWithNullAppendable() throws IOException {
 Object[] parts = new Object[]{"apple", "banana", "cherry"};
 String appendable = null;
 JOINER.appendTo(appendable, parts);
 fail("Expecting IOException");
 }

 @Test
 public void testAppendToWithNullFirst() throws IOException {
 Iterable<Object> parts = Arrays.asList(null, "banana", "cherry");
 StringBuilder expected = new StringBuilder((String)Joiner.NULL_TEXT + ":banana:cherry");
 StringBuilder actual = new StringBuilder();
 JOINER.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToWithNullSecond() throws IOException {
 Object[] parts = new Object[]{null, "banana", "cherry"};
 StringBuilder expected = new StringBuilder("null:banana:cherry");
 StringBuilder actual = new StringBuilder();
 JOINER.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToWithNullRest() throws IOException {
 Object[] parts = new Object[]{null, null, "cherry"};
 StringBuilder expected = new StringBuilder("null:null:cherry");
 StringBuilder actual = new StringBuilder();
 JOINER.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterable() {
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple:banana:cherry";
 String actual = JOINER.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
 String expected = "apple:banana:cherry";
 String actual = JOINER.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 Object[] parts = new Object[]{"apple", "banana", "cherry"};
 String expected = "apple:banana:cherry";
 String actual = JOINER.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinWithNullFirst() {
 Object[] parts = new Object[]{null, "banana", "cherry"};
 String expected = Joiner.NULL_TEXT + ":banana:cherry";
 String actual = JOINER.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinWithNullSecond() {
 Object[] parts = new Object[]{null, null, "cherry"};
 String expected = Joiner.NULL_TEXT + ":null:cherry";
 String actual = JOINER.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinWithNullRest() {
 Object[] parts = new Object[]{null, null, null};
 String expected = Joiner.NULL_TEXT + ":null:null";
 String actual = JOINER.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testUseForNull() throws IOException {
 Joiner joiner = Joiner.on(":").useForNull("NA");
 String[] parts = new String[]{"apple", null, "cherry"};
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 String expected = "apple:NA:cherry";
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testSkipNulls() throws IOException {
 Joiner joiner = Joiner.on(":").skipNulls();
 String[] parts = new String[]{"apple", null, "cherry"};
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 String expected = "apple:cherry";
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testMapJoinerAppendToMapEntrySet() throws IOException {
 Map<Integer, String> map = new HashMap<>();
 map.put(1, "one");
 map.put(2, "two");
 map.put(3, "three");
 MapJoiner mapJoiner = JOINER.withKeyValueSeparator(" = ");
 StringBuilder expected = new StringBuilder("1 = one:2 = two:3 = three");
 StringBuilder actual = new StringBuilder();
 mapJoiner.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testMapJoinerJoinMapEntrySet() {
 Map<Integer, String> map = new HashMap<>();
 map.put(1, "one");
 map.put(2, "two");
 map.put(3, "three");
 MapJoiner mapJoiner = JOINER.withKeyValueSeparator(" = ");
 String expected = "1 = one:2 = two:3 = three";
 String actual = mapJoiner.join(map.entrySet());
 assertEquals(expected, actual);
 }
}