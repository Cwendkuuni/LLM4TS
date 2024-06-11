import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.*;
import java.util.*;

public class JoinerTest {

 private Joiner joiner;

 @Before
 public void setUp() {
 joiner = Joiner.on(",");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple,").append(joiner.separator).append("banana,").append(joiner.separator).append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 Object[] parts = new Object[]{"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple,").append(joiner.separator).append("banana,").append(joiner.separator).append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 StringBuilder expected = new StringBuilder();
 expected.append("apple,").append(joiner.separator).append("banana,").append(joiner.separator).append("cherry");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, iterator);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToNullIterator() throws IOException {
 Iterator<Object> iterator = null;
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, iterator);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testAppendToEmptyIterable() throws IOException {
 Iterable<String> parts = Collections.emptyList();
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals("", actual.toString());
 }

 @Test
 public void testJoinIterable() {
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple," + joiner.separator + "banana," + joiner.separator + "cherry";
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 Object[] parts = new Object[]{"apple", "banana", "cherry"};
 String expected = "apple," + joiner.separator + "banana," + joiner.separator + "cherry";
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinNullFirst() {
 Object first = null;
 Object second = "banana";
 Object[] rest = new Object[]{"cherry", "date"};
 String expected = null;
 String actual = joiner.join(first, second, rest);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinNullSecond() {
 Object first = "apple";
 Object second = null;
 Object[] rest = new Object[]{"cherry", "date"};
 String expected = "apple" + joiner.separator;
 String actual = joiner.join(first, second, rest);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinNullRest() {
 Object first = "apple";
 Object second = "banana";
 Object[] rest = null;
 String expected = first + joiner.separator + second;
 String actual = joiner.join(first, second, rest);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinEmptyArray() {
 Object[] parts = {};
 String expected = "";
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testJoinNullParts() {
 Object[] parts = null;
 joiner.join(parts);
 }

 @Test
 public void testUseForNull() {
 String nullText = "null";
 Joiner joinerWithNullText = joiner.useForNull(nullText);
 assertEquals(nullText, joinerWithNullText.toString(null));
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerSkippingNulls = joiner.skipNulls();
 Object[] parts = new Object[]{null, "apple", null, "banana", null};
 String expected = "apple," + joiner.separator + "banana";
 String actual = joinerSkippingNulls.join(parts);
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUseForNullAndSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerUseForNull = joiner.useForNull("nullText");
 Joiner joinerSkippingNulls = joinerUseForNull.skipNulls();
 throw new AssertionError("Should not be able to use both useForNull and skipNulls");
 }

 @Test
 public void testMapJoinerAppendToMapEntrySet() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 StringBuilder expected = new StringBuilder();
 expected.append("key1=").append(joiner.keyValueSeparator).append(joiner.separator).append("value1,").append(joiner.separator).append("key2=").append(joiner.separator).append("value2");
 StringBuilder actual = new StringBuilder();
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(joiner.separator);
 mapJoiner.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testMapJoinerJoinMapEntrySet() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 StringBuilder expected = new StringBuilder();
 expected.append("key1=").append(joiner.keyValueSeparator).append("value1,").append(joiner.separator).append("key2=").append(joiner.keyValueSeparator).append("value2");
 String actual = joiner.withKeyValueSeparator(joiner.separator).join(map.entrySet());
 assertEquals(expected.toString(), actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapJoinerWithSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner mapJoiner = joiner.skipNulls().withKeyValueSeparator(joiner.separator);
 throw new AssertionError("Can't use .skipNulls() with maps");
 }
}