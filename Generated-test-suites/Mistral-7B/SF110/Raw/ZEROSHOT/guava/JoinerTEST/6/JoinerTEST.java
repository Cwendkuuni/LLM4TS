import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class JoinerTest {

 private Joiner joiner;
 private Iterator<Object> iterator;
 private Iterator<Map.Entry<String, String>> mapIterator;
 private Appendable appendable;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() {
 joiner = Joiner.on(":");
 outputStream = new ByteArrayOutputStream();
 appendable = outputStream;
 }

 @Test
 public void testAppendToWithIterable() throws IOException {
 Iterable<Object> iterable = Arrays.asList("Test1", "Test2", "Test3");
 joiner.appendTo(appendable, iterable);
 assertEquals("Test1:" + joiner.separator + "Test2:" + joiner.separator + "Test3", outputStream.toString());
 }

 @Test
 public void testAppendToWithNullFirstAndNullSecond() throws IOException {
 joiner.appendTo(appendable, null, null);
 assertNull(outputStream.toString());
 }

 @Test
 public void testAppendToWithNullFirstAndValidSecond() throws IOException {
 joiner.appendTo(appendable, null, "Test2");
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testAppendToWithValidFirstAndNullSecond() throws IOException {
 joiner.appendTo(appendable, "Test1", null);
 assertEquals("Test1", outputStream.toString());
 }

 @Test
 public void testAppendToWithValidFirstAndValidSecond() throws IOException {
 joiner.appendTo(appendable, "Test1", "Test2");
 assertEquals("Test1:" + joiner.separator + "Test2", outputStream.toString());
 }

 @Test
 public void testAppendToWithObjectArray() throws IOException {
 joiner.appendTo(appendable, new Object[]{"Test1", "Test2", "Test3"});
 assertEquals("Test1:" + joiner.separator + "Test2:" + joiner.separator + "Test3", outputStream.toString());
 }

 @Test
 public void testAppendToWithNullFirstNullSecondObjectArray() throws IOException {
 joiner.appendTo(appendable, null, null, (Object[])null);
 assertNull(outputStream.toString());
 }

 @Test
 public void testAppendToWithValidFirstNullSecondObjectArray() throws IOException {
 joiner.appendTo(appendable, "Test1", null, new Object[]{"Test3", "Test4"});
 assertEquals("Test1", outputStream.toString());
 }

 @Test
 public void testAppendToWithNullFirstValidSecondObjectArray() throws IOException {
 joiner.appendTo(appendable, null, "Test2", new Object[]{"Test3", "Test4"});
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testAppendToWithValidFirstValidSecondObjectArray() throws IOException {
 joiner.appendTo(appendable, "Test1", "Test2", new Object[]{"Test3", "Test4"});
 assertEquals("Test1:" + joiner.separator + "Test2:" + joiner.separator + "Test3:" + joiner.separator + "Test4", outputStream.toString());
 }

 @Test
 public void testAppendToWithStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, Arrays.asList("Test1", "Test2", "Test3"));
 assertEquals("Test1:" + joiner.separator + "Test2:" + joiner.separator + "Test3", stringBuilder.toString());
 }

 @Test
 public void testAppendToWithNullFirstNullSecondStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, null, null);
 assertNull(stringBuilder.toString());
 }

 @Test
 public void testAppendToWithNullFirstValidSecondStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, null, "Test2");
 assertEquals("", stringBuilder.toString());
 }

 @Test
 public void testAppendToWithValidFirstNullSecondStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, "Test1", null);
 assertEquals("Test1", stringBuilder.toString());
 }

 @Test
 public void testAppendToWithValidFirstValidSecondStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, "Test1", "Test2");
 assertEquals("Test1:" + joiner.separator + "Test2", stringBuilder.toString());
 }

 @Test
 public void testJoinWithIterable() {
 Iterable<Object> iterable = Arrays.asList("Test1", "Test2", "Test3");
 assertEquals("Test1:" + joiner.separator + "Test2:" + joiner.separator + "Test3", joiner.join(iterable));
 }

 @Test
 public void testJoinWithNullIterable() {
 assertNull(joiner.join(null));
 }

 @Test
 public void testJoinWithObjectArray() {
 Object[] objectArray = {"Test1", "Test2", "Test3"};
 assertEquals("Test1:" + joiner.separator + "Test2:" + joiner.separator + "Test3", joiner.join(objectArray));
 }

 @Test
 public void testJoinWithNullFirstNullSecondObjectArray() {
 Object[] objectArray = {null, null};
 assertNull(joiner.join(objectArray));
 }

 @Test
 public void testJoinWithValidFirstNullSecondObjectArray() {
 Object[] objectArray = {"Test1", null};
 assertEquals("Test1", joiner.join(objectArray));
 }

 @Test
 public void testJoinWithNullFirstValidSecondObjectArray() {
 Object[] objectArray = {null, "Test2"};
 assertEquals("Test2", joiner.join(objectArray));
 }

 @Test
 public void testJoinWithValidFirstValidSecondObjectArray() {
 Object[] objectArray = {"Test1", "Test2"};
 assertEquals("Test1:" + joiner.separator + "Test2", joiner.join(objectArray));
 }

 @Test
 public void testUseForNull() throws IOException {
 joiner = joiner.useForNull("NULL");
 Iterator<Object> nullIterator = Collections.emptyIterator();
 joiner.appendTo(appendable, nullIterator);
 assertEquals("", outputStream.toString());
 }

 @Test
 public void testSkipNulls() throws IOException {
 joiner = joiner.skipNulls();
 Object[] objects = {"Test1", null, "Test3"};
 joiner.appendTo(appendable, objects);
 assertEquals("Test1:" + joiner.separator + "Test3", outputStream.toString());
 }

 @Test
 public void testMapJoinerAppendToWithMap() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("Key1", "Value1");
 map.put("Key2", "Value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 mapJoiner.appendTo(appendable, map);
 assertEquals("Key1=" + joiner.separator + "Value1" + joiner.separator + "Key2=" + joiner.separator + "Value2", outputStream.toString());
 }

 @Test
 public void testMapJoinerJoinWithMap() {
 Map<String, String> map = new HashMap<>();
 map.put("Key1", "Value1");
 map.put("Key2", "Value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 assertEquals("Key1=" + joiner.separator + "Value1" + joiner.separator + "Key2=" + joiner.separator + "Value2", mapJoiner.join(map));
 }

 @Test
 public void testMapJoinerUseForNull() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("Key1", "Value1");
 map.put("Key2", null);
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=").useForNull("NULL");
 mapJoiner.appendTo(appendable, map.entrySet());
 assertEquals("Key1=Value1" + joiner.separator + "Key2=NULL", outputStream.toString());
 }
}