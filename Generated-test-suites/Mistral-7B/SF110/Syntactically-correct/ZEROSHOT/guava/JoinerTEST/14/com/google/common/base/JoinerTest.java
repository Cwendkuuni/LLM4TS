import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import java.util.*;

public class JoinerTest {

 private Joiner joiner;
 private Joiner charJoiner;

 @Before
 public void setUp() {
 joiner = Joiner.on(",");
 charJoiner = Joiner.on('-');
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> list = Arrays.asList("apple", "banana", "cherry");
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, list);
 assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 String[] array = {"apple", "banana", "cherry"};
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, array);
 assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testAppendToObject() throws IOException {
 Object[] objArray = {1, "two", 3.0};
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, objArray);
 assertEquals("1,two,3.0", sb.toString());
 }

 @Test
 public void testAppendToObjectNull() throws IOException {
 Object[] objArray = {null, "two", 3.0};
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, objArray);
 assertEquals("null,two,3.0", sb.toString());
 }

 @Test
 public void testAppendToIterableNull() throws IOException {
 Iterable<Object> nullIterable = Arrays.asList(null, "two", 3.0);
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, nullIterable);
 assertEquals("null,two,3.0", sb.toString());
 }

 @Test
 public void testAppendToIterableNullPart() throws IOException {
 Iterator<Object> it = Arrays.asList(null, "two", 3.0).iterator();
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, it);
 assertEquals("null,two,3.0", sb.toString());
 }

 @Test
 public void testAppendToCharSeparator() throws IOException {
 Iterable<Character> charList = Arrays.asList('a', 'b', 'c');
 StringBuilder sb = new StringBuilder();
 charJoiner.appendTo(sb, charList);
 assertEquals("a-b-c", sb.toString());
 }

 @Test
 public void testAppendToCharSeparatorArray() throws IOException {
 char[] charArray = {'a', 'b', 'c'};
 StringBuilder sb = new StringBuilder();
 charJoiner.appendTo(sb, charArray);
 assertEquals("a-b-c", sb.toString());
 }

 @Test
 public void testAppendToCharSeparatorObject() throws IOException {
 Object[] objArray = {'a', 'b', 'c'};
 StringBuilder sb = new StringBuilder();
 charJoiner.appendTo(sb, objArray);
 assertEquals("a-b-c", sb.toString());
 }

 @Test
 public void testJoinIterable() {
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 assertEquals("apple,banana,cherry", joiner.join(list));
 }

 @Test
 public void testJoinArray() {
 String[] array = {"apple", "banana", "cherry"};
 assertEquals("apple,banana,cherry", joiner.join(array));
 }

 @Test
 public void testJoinObject() {
 Object[] objArray = {1, "two", 3.0};
 assertEquals("1,two,3.0", joiner.join(objArray));
 }

 @Test
 public void testJoinObjectNull() {
 Object[] objArray = {null, "two", 3.0};
 assertEquals("null,two,3.0", joiner.join(objArray));
 }

 @Test
 public void testJoinIterableNull() {
 Iterable<Object> nullIterable = Arrays.asList(null, "two", 3.0);
 assertEquals("null,two,3.0", joiner.join(nullIterable));
 }

 @Test
 public void testJoinIterableNullPart() {
 Iterator<Object> it = Arrays.asList(null, "two", 3.0).iterator();
 assertEquals("null,two,3.0", joiner.join(it));
 }

 @Test
 public void testJoinCharSeparator() {
 char[] charArray = {'a', 'b', 'c'};
 assertEquals("a-b-c", charJoiner.join(charArray));
 }

 @Test
 public void testJoinCharSeparatorArray() {
 Character[] charArray = {'a', 'b', 'c'};
 assertEquals("a-b-c", joiner.join(charArray));
 }

 @Test
 public void testJoinCharSeparatorObject() {
 Object[] objArray = {'a', 'b', 'c'};
 assertEquals("a-b-c", joiner.join(objArray));
 }

 @Test
 public void testUseForNull() {
 Joiner joinerNull = joiner.useForNull("NULL");
 assertEquals("NULL,apple,banana,cherry", joinerNull.join(Arrays.asList("apple", "banana", "cherry")));
 }

 @Test
 public void testSkipNulls() {
 Joiner joinerSkipNulls = joiner.skipNulls();
 assertEquals("apple,banana", joinerSkipNulls.join(Arrays.asList(null, "apple", "banana", null)));
 }

 @Test
 public void testMapJoiner() {
 Map<String, Integer> map = new HashMap<>();
 map.put("Apple", 1);
 map.put("Banana", 2);
 map.put("Cherry", 3);
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
 assertEquals("Apple: 1, Banana: 2, Cherry: 3", mapJoiner.join(map.entrySet()));
 }

}