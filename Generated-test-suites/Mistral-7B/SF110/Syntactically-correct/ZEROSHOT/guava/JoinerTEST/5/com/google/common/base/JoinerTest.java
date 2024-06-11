import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 @Test
 public void testJoinerCreation() {
 Joiner joinerWithStringSeparator = Joiner.on(",");
 Joiner joinerWithCharSeparator = Joiner.on('-');
 Joiner joinerWithPrototype = new Joiner("*");
 }

 @Test
 public void testAppendToWithIterable() throws IOException {
 String expectedJoinString = "apple,banana,orange";
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(appendable, Arrays.asList("apple", "banana", "orange"));
 assertEquals(expectedJoinString, appendable.toString());
 }

 @Test
 public void testAppendToWithArray() throws IOException {
 String expectedJoinString = "apple,banana,orange";
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(appendable, new Object[]{"apple", "banana", "orange"});
 assertEquals(expectedJoinString, appendable.toString());
 }

 @Test
 public void testAppendToWithSingleElementIterable() throws IOException {
 String expectedJoinString = "apple";
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(appendable, Lists.newArrayList("apple"));
 assertEquals(expectedJoinString, appendable.toString());
 }

 @Test
 public void testAppendToWithEmptyIterable() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(appendable, Lists.newArrayList());
 assertEquals("", appendable.toString());
 }

 @Test
 public void testJoinWithIterable() {
 String expectedJoinString = "apple,banana,orange";
 Joiner joiner = Joiner.on(",");
 Iterable<String> parts = Arrays.asList("apple", "banana", "orange");
 assertEquals(expectedJoinString, joiner.join(parts));
 }

 @Test
 public void testJoinWithArray() {
 String expectedJoinString = "apple,banana,orange";
 Joiner joiner = Joiner.on(",");
 Object[] parts = new Object[]{"apple", "banana", "orange"};
 assertEquals(expectedJoinString, joiner.join(parts));
 }

 @Test
 public void testJoinWithSingleElementIterable() {
 String expectedJoinString = "apple";
 Joiner joiner = Joiner.on(",");
 Iterable<String> parts = Lists.newArrayList("apple");
 assertEquals(expectedJoinString, joiner.join(parts));
 }

 @Test
 public void testJoinWithEmptyIterable() {
 String expectedJoinString = "";
 Joiner joiner = Joiner.on(",");
 Iterable<String> parts = Lists.newArrayList();
 assertEquals(expectedJoinString, joiner.join(parts));
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",");
 Joiner nullTextJoiner = joiner.useForNull("null");
 String nullText = null;
 Object[] parts = new Object[]{"apple", nullText, "orange"};
 StringBuilder appendable = new StringBuilder();
 nullTextJoiner.appendTo(appendable, parts);
 assertEquals("apple,null,orange", appendable.toString());
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner skipNullsJoiner = joiner.skipNulls();
 String nullText = null;
 Object[] parts = new Object[]{"apple", nullText, "orange"};
 StringBuilder appendable = new StringBuilder();
 skipNullsJoiner.appendTo(appendable, parts);
 assertEquals("apple,orange", appendable.toString());
 }

 @Test
 public void testMapJoinerWithStringKeyValueSeparator() {
 Map<String, String> map = Maps.newHashMap();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("orange", "orange");
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(",=");
 StringBuilder appendable = new StringBuilder();
 mapJoiner.appendTo(appendable, map);
 assertEquals("apple=red,banana=yellow,orange=orange", appendable.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapJoinerWithSkipNulls() {
 Map<String, String> map = Maps.newHashMap();
 map.put("apple", null);
 map.put("banana", "yellow");
 map.put("orange", null);
 Joiner joiner = Joiner.on(",");
 Joiner skipNullsJoiner = joiner.skipNulls();
 MapJoiner mapJoiner = skipNullsJoiner.withKeyValueSeparator(",=");
 StringBuilder appendable = new StringBuilder();
 mapJoiner.appendTo(appendable, map);
 fail();
 }

 @Test
 public void testMapJoinerWithUseForNull() {
 Map<String, String> map = Maps.newHashMap();
 map.put("apple", null);
 map.put("banana", "yellow");
 map.put("orange", null);
 Joiner joiner = Joiner.on(",");
 Joiner nullTextJoiner = joiner.useForNull("null");
 MapJoiner mapJoiner = nullTextJoiner.withKeyValueSeparator(",=");
 StringBuilder appendable = new StringBuilder();
 mapJoiner.appendTo(appendable, map);
 assertEquals("apple=null,banana=yellow,orange=null", appendable.toString());
 }
}