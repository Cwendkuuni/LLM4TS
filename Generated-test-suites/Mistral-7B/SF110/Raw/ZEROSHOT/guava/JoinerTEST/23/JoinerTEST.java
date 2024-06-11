import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util. collections.Iterable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class JoinerTest {

 private Joiner stringJoiner;
 private Joiner charJoiner;
 private Joiner mapJoiner;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 stringJoiner = Joiner.on("/");
 charJoiner = Joiner.on('-');
 mapJoiner = Joiner.on("/").useForNull("NA");
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
 stringJoiner.appendTo(outputStream, iterable);
 assertEquals("/apple/banana/cherry", outputStream.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 stringJoiner.appendTo(outputStream, iterator);
 assertEquals("/apple/banana/cherry", outputStream.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 Object[] objects = new Object[]{"apple", "banana", "cherry"};
 stringJoiner.appendTo(outputStream, objects);
 assertEquals("/apple/banana/cherry", outputStream.toString());
 }

 @Test
 public void testAppendToObjectNullFirst() throws IOException {
 Object[] objects = new Object[]{null, "banana", "cherry"};
 stringJoiner.appendTo(outputStream, objects);
 assertEquals("NA/banana/cherry", outputStream.toString());
 }

 @Test
 public void testAppendToObjectNullSecond() throws IOException {
 Object[] objects = new Object[]{"apple", null, "cherry"};
 stringJoiner.appendTo(outputStream, objects);
 assertEquals("apple/NA/cherry", outputStream.toString());
 }

 @Test
 public void testAppendToObjectNullBoth() throws IOException {
 Object[] objects = new Object[]{null, null, "cherry"};
 stringJoiner.appendTo(outputStream, objects);
 assertEquals("NA/NA/cherry", outputStream.toString());
 }

 @Test
 public void testAppendToStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 stringJoiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("/apple/banana/cherry", stringBuilder.toString());
 }

 @Test
 public void testJoinIterable() {
 Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
 assertEquals("/apple/banana/cherry", stringJoiner.join(iterable));
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 assertEquals("/apple/banana/cherry", stringJoiner.join(iterator));
 }

 @Test
 public void testJoinObjectArray() {
 Object[] objects = new Object[]{"apple", "banana", "cherry"};
 assertEquals("/apple/banana/cherry", stringJoiner.join(objects));
 }

 @Test
 public void testJoinObjectNullFirst() {
 Object[] objects = new Object[]{null, "banana", "cherry"};
 assertEquals("NA/banana/cherry", stringJoiner.join(objects));
 }

 @Test
 public void testJoinObjectNullSecond() {
 Object[] objects = new Object[]{"apple", null, "cherry"};
 assertEquals("apple/NA/cherry", stringJoiner.join(objects));
 }

 @Test
 public void testJoinObjectNullBoth() {
 Object[] objects = new Object[]{null, null, "cherry"};
 assertEquals("NA/NA/cherry", stringJoiner.join(objects));
 }

 @Test
 public void testAppendToAppendableWithIOException() {
 ByteArrayOutputStream outputStreamSpy = Mockito.spy(new ByteArrayOutputStream());
 stringJoiner.appendTo(outputStreamSpy, Arrays.asList("apple", "banana", "cherry").iterator());
 Mockito.verify(outputStreamSpy, Mockito.times(3)).append(Mockito.anyString());
 Mockito.verify(outputStreamSpy, Mockito.times(2)).append(stringJoiner.separator);
 Mockito.verify(outputStreamSpy, Mockito.times(3)).flush();
 }

 @Test(expected = AssertionError.class)
 public void testAppendToAppendableWithIOException2() throws IOException {
 stringJoiner.appendTo(outputStream, Arrays.asList("apple", "banana", "cherry").iterator(), true);
 }

 @Test
 public void testJoinMapEntrySet() {
 Iterable<Map.Entry<String, String>> mapEntries = Arrays.asList(
 new AbstractMap.SimpleImmutableEntry<>("name", "John"),
 new AbstractMap.SimpleImmutableEntry<>("age", "25"),
 new AbstractMap.SimpleImmutableEntry<>("city", "New York")).iterator();
 assertEquals("/name/John/age/25/city/New York/", mapJoiner.join(mapEntries));
 }

 @Test
 public void testAppendToMapEntrySetStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, Arrays.asList(
 new AbstractMap.SimpleImmutableEntry<>("name", "John"),
 new AbstractMap.SimpleImmutableEntry<>("age", "25"),
 new AbstractMap.SimpleImmutableEntry<>("city", "New York")).iterator());
 assertEquals("/name/John/age/25/city/New York/", stringBuilder.toString());
 }

 @Test
 public void testJoinMapEntrySetString() {
 assertEquals("/name/John/age/25/city/New York/", mapJoiner.join(Arrays.asList(
 new AbstractMap.SimpleImmutableEntry<>("name", "John"),
 new AbstractMap.SimpleImmutableEntry<>("age", "25"),
 new AbstractMap.SimpleImmutableEntry<>("city", "New York")).iterator()));
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = mapJoiner.useForNull("NA");
 Object[] objects = new Object[]{"apple", null, "cherry"};
 assertEquals("NA/apple/NA/cherry", joiner.join(objects));
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = mapJoiner.skipNulls();
 Object[] objects = new Object[]{null, "apple", null, "cherry"};
 assertEquals("apple/cherry", joiner.join(objects));
 }

 @Test
 public void testWithKeyValueSeparator() {
 Joiner joiner = mapJoiner.withKeyValueSeparator(":");
 Iterable<Map.Entry<String, String>> mapEntries = Arrays.asList(
 new AbstractMap.SimpleImmutableEntry<>("name", "John"),
 new AbstractMap.SimpleImmutableEntry<>("age", "25"),
 new AbstractMap.SimpleImmutableEntry<>("city", "New York")).iterator();
 assertEquals("/name:John/age:25/city:New York/", joiner.join(mapEntries));
 }
}