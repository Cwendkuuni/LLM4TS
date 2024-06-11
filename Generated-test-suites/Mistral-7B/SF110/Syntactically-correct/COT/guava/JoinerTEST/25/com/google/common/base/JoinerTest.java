import static org.junit.Assert.*;
import com.google.common.base.*;
import org.junit.Test;

public class JoinerTest {

private Joiner joinerWithComma;
private Joiner joinerWithSpace;

@Before
public void setup() {
joinerWithComma = Joiner.on(",");
joinerWithSpace = Joiner.on(" ");
}

@Test
public void testAppendToIterable() throws IOException {
Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
StringBuilder expected = new StringBuilder("apple,banana,cherry");
StringBuilder actual = new StringBuilder();
joinerWithComma.appendTo(actual, parts);
assertEquals(expected.toString(), actual.toString());
}

@Test
public void testAppendToObjectArray() throws IOException {
Object[] parts = new Object[]{"apple", "banana", "cherry"};
StringBuilder expected = new StringBuilder("apple,banana,cherry");
StringBuilder actual = new StringBuilder();
joinerWithComma.appendTo(actual, parts);
assertEquals(expected.toString(), actual.toString());
}

@Test
public void testAppendToIterableNull() throws IOException {
Iterable<String> parts = Arrays.asList(null, "banana", "cherry");
StringBuilder expected = new StringBuilder();
StringBuilder actual = new StringBuilder();
joinerWithComma.appendTo(actual, parts);
assertEquals(expected.toString(), actual.toString());
}

@Test
public void testAppendToObjectArrayNull() throws IOException {
Object[] parts = new Object[]{null, "banana", "cherry"};
StringBuilder expected = new StringBuilder();
StringBuilder actual = new StringBuilder();
joinerWithComma.appendTo(actual, parts);
assertEquals(expected.toString(), actual.toString());
}

@Test
public void testAppendToIterator() throws IOException {
Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
StringBuilder expected = new StringBuilder("apple,banana,cherry");
StringBuilder actual = new StringBuilder();
joinerWithComma.appendTo(actual, parts);
assertEquals(expected.toString(), actual.toString());
}

@Test
public void testJoinIterable() {
Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
String expected = "apple,banana,cherry";
String actual = joinerWithComma.join(parts);
assertEquals(expected, actual);
}

@Test
public void testJoinObjectArray() {
Object[] parts = new Object[]{"apple", "banana", "cherry"};
String expected = "apple,banana,cherry";
String actual = joinerWithComma.join(parts);
assertEquals(expected, actual);
}

@Test
public void testJoinNull() {
String expected = "";
String actual = joinerWithComma.join((Iterable<?>)null);
assertEquals(expected, actual);
}

@Test
public void testJoinObjectNull() {
String expected = "";
String actual = joinerWithComma.join(new Object[]{null});
assertEquals(expected, actual);
}

@Test
public void testJoinIteratorNull() {
Iterator<String> parts = Arrays.asList("apple", null, "cherry").iterator();
String expected = "";
String actual = joinerWithComma.join(parts);
assertEquals(expected, actual);
}

@Test
public void testJoinObjectNullInArray() {
Object[] parts = new Object[]{"apple", null, "cherry"};
String expected = "apple,,";
String actual = joinerWithComma.join(parts);
assertEquals(expected, actual);
}

@Test
public void testUseForNull() {
Joiner joinerWithNullText = joinerWithComma.useForNull("NULL");
String nullString = null;
Object[] nullableParts = new Object[]{"apple", nullString, "cherry"};
String expected = "apple,NULL,cherry";
String actual = joinerWithNullText.join(nullableParts);
assertEquals(expected, actual);
}

@Test
public void testSkipNulls() {
Joiner joinerWithSkipNulls = joinerWithComma.skipNulls();
Object[] nullableParts = new Object[]{"apple", null, "cherry", null, "banana"};
String expected = "apple,banana";
String actual = joinerWithSkipNulls.join(nullableParts);
assertEquals(expected, actual);
}

@Test
public void testMapJoiner() throws IOException {
Map<String, String> map = new HashMap<>();
map.put("key1", "value1");
map.put("key2", "value2");
map.put("key3", "value3");
MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(": ");
StringBuilder expected = new StringBuilder("key1: value1, key2: value2, key3: value3");
StringBuilder actual = new StringBuilder();
mapJoiner.appendTo(actual, map.entrySet());
assertEquals(expected.toString(), actual.toString());
}

@Test (expected = UnsupportedOperationException.class)
public void testUseForNullWithUseForNull() {
Joiner joinerWithNullText = joinerWithComma.useForNull("NULL");
joinerWithNullText.useForNull("anotherNullText");
}

@Test (expected = UnsupportedOperationException.class)
public void testSkipNullsWithUseForNull() {
Joiner joinerWithNullText = joinerWithComma.useForNull("NULL");
joinerWithNullText.skipNulls();
}

@Test (expected = UnsupportedOperationException.class)
public void testMapJoinerWithSkipNulls() {
Joiner joinerWithSkipNulls = joinerWithComma.skipNulls();
MapJoiner mapJoiner = joinerWithSkipNulls.withKeyValueSeparator(": ");
}
}