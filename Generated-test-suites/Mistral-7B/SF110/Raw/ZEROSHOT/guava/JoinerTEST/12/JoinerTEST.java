import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JoinerTest {

 private Joiner commaJoiner;
 private Joiner dotJoiner;
 private Joiner hyphenJoiner;
 private Joiner emptyJoiner;
 private Joiner nullTextJoiner;
 private Joiner skipNullsJoiner;
 private Joiner mapJoiner;

 @Before
 public void setup() {
 commaJoiner = Joiner.on(",");
 dotJoiner = Joiner.on(".");
 hyphenJoiner = Joiner.on("-");
 emptyJoiner = Joiner.on("");
 nullTextJoiner = commaJoiner.useForNull("N/A");
 skipNullsJoiner = commaJoiner.skipNulls();
 mapJoiner = commaJoiner.withKeyValueSeparator(": ");
 }

 @Test
 public void testNewJoiner() {
 Joiner joiner = Joiner.on(",");
 assertNotNull(joiner);
 assertEquals(",", joiner.separator());
 }

 @Test
 public void testNewJoinerWithCharSeparator() {
 Joiner joiner = Joiner.on('-');
 assertNotNull(joiner);
 assertEquals("-", joiner.separator());
 }

 @Test(expected = NullPointerException.class)
 public void testNullSeparator() {
 Joiner joiner = Joiner.on(null);
 fail("Expected NullPointerException but got: " + joiner);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
 commaJoiner.appendTo(stringWriter, iterable);
 assertEquals("apple, banana, cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Object[] array = {"apple", "banana", "cherry"};
 commaJoiner.appendTo(stringWriter, array);
 assertEquals("apple, banana, cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToSingleObject() throws IOException {
 StringWriter stringWriter = new StringWriter();
 String str = "apple";
 commaJoiner.appendTo(stringWriter, Arrays.asList(str));
 assertEquals("apple", stringWriter.toString());
 }

 @Test
 public void testAppendToEmptyIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Iterable<String> emptyIterable = Collections.emptyList();
 commaJoiner.appendTo(stringWriter, emptyIterable);
 assertEquals("", stringWriter.toString());
 }

 @Test
 public void testAppendToNullIterable() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Iterable<String> nullIterable = null;
 commaJoiner.appendTo(stringWriter, nullIterable);
 assertEquals("", stringWriter.toString());
 }

 @Test
 public void testAppendToNullString() throws IOException {
 StringWriter stringWriter = new StringWriter();
 String nullString = null;
 commaJoiner.appendTo(stringWriter, Arrays.asList(nullString));
 assertEquals("N/A", stringWriter.toString());
 }

 @Test
 public void testJoinIterable() {
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 assertEquals("apple, banana, cherry", commaJoiner.join(list));
 }

 @Test
 public void testJoinObjectArray() {
 Object[] array = {"apple", "banana", "cherry"};
 assertEquals("apple, banana, cherry", commaJoiner.join(array));
 }

 @Test
 public void testJoinSingleObject() {
 String str = "apple";
 assertEquals("apple", commaJoiner.join(str));
 }

 @Test
 public void testJoinEmptyList() {
 List<String> emptyList = Collections.emptyList();
 assertEquals("", commaJoiner.join(emptyList));
 }

 @Test
 public void testJoinNullList() {
 List<String> nullList = null;
 assertEquals("", commaJoiner.join(nullList));
 }

 @Test
 public void testJoinMap() {
 Map<String, String> map = new ConcurrentHashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("cherry", "green");
 assertEquals("apple: red, banana: yellow, cherry: green", mapJoiner.join(map));
 }

 @Test
 public void testUseForNull() {
 String nullText = null;
 assertEquals("N/A, apple, banana, cherry", nullTextJoiner.join(Arrays.asList("apple", "banana", "cherry")));
 }

 @Test
 public void testSkipNulls() {
 String[] array = {"apple", null, "banana", null, "cherry"};
 assertEquals("apple, banana, cherry", skipNullsJoiner.join(array));
 }
}