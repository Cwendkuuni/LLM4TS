import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Arrays;
import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class JoinerTest {

 private Joiner stringJoiner;
 private Joiner charJoiner;
 private Joiner nullTextJoiner;
 private Joiner skipNullsJoiner;
 private MapJoiner mapJoiner;

 @Before
 public void setup() {
 stringJoiner = Joiner.on(",");
 charJoiner = Joiner.on('-');
 nullTextJoiner = stringJoiner.useForNull("NA");
 skipNullsJoiner = stringJoiner.skipNulls();
 mapJoiner = stringJoiner.withKeyValueSeparator(":");
 }

 @Test
 public void testSeparators() {
 assertEquals(",", stringJoiner.separator());
 assertEquals("-", charJoiner.separator());
 }

 @Test
 public void testCreateJoinerWithString() {
 Joiner joiner = Joiner.on(",");
 assertNotNull(joiner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateJoinerWithNull() {
 Joiner joiner = Joiner.on(null);
 }

 @Test
 public void testCreateJoinerWithChar() {
 Joiner joiner = Joiner.on('-');
 assertNotNull(joiner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateJoinerWithNullChar() {
 Joiner joiner = Joiner.on((char) 0);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 stringJoiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", outputStream.toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 stringJoiner.appendTo(outputStream, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", outputStream.toString());
 }

 @Test
 public void testAppendToSingleObject() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 stringJoiner.appendTo(outputStream, Arrays.asList("apple"));
 assertEquals("apple", outputStream.toString());
 }

 @Test
 public void testAppendToEmptyIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 stringJoiner.appendTo(outputStream, Collections.emptyList());
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testAppendToNullIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 stringJoiner.appendTo(outputStream, null);
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 stringJoiner.appendTo(outputStream, iterator);
 assertEquals("apple,banana,cherry", outputStream.toString());
 }

 @Test
 public void testAppendToNullIterator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Iterator<String> iterator = null;
 stringJoiner.appendTo(outputStream, iterator);
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testJoinIterable() {
 assertEquals("apple,banana,cherry", stringJoiner.join(Arrays.asList("apple", "banana", "cherry")));
 }

 @Test
 public void testJoinArray() {
 assertEquals("apple,banana,cherry", stringJoiner.join(new Object[]{"apple", "banana", "cherry"}));
 }

 @Test
 public void testJoinSingleObject() {
 assertEquals("apple", stringJoiner.join("apple"));
 }

 @Test
 public void testJoinEmptyIterable() {
 assertEquals("", stringJoiner.join(Collections.emptyList()));
 }

 @Test
 public void testJoinNullIterable() {
 assertEquals("", stringJoiner.join(null));
 }

 @Test
 public void testJoinSingleNullObject() {
 assertEquals("NA", nullTextJoiner.join("apple"));
 }

 @Test
 public void testJoinMultipleNullObjects() {
 assertEquals("NA,NA,NA", nullTextJoiner.join(new Object[]{"apple", null, "cherry"}));
 }

 @Test
 public void testSkipNulls() {
 assertEquals("banana,cherry", skipNullsJoiner.join(Arrays.asList("apple", null, "banana", "cherry")));
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("name", "Alice");
 map.put("age", "25");
 map.put("city", null);

 assertEquals("name:Alice,age:25", mapJoiner.join(map));
 }
}