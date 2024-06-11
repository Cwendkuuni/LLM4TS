package com.google.common.base;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JoinerTest {

 @Test
 public void testStringJoiner() {
 Joiner joiner = Joiner.on(",");
 StringWriter stringWriter = new StringWriter();
 try {
 joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testCharJoiner() {
 Joiner joiner = Joiner.on(';');
 StringWriter stringWriter = new StringWriter();
 try {
 joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals("apple;banana;cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Joiner joiner = Joiner.on(":");
 StringWriter stringWriter = new StringWriter();
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 joiner.appendTo(stringWriter, iterator);
 assertEquals("apple:banana:cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 Joiner joiner = Joiner.on("-");
 StringWriter stringWriter = new StringWriter();
 joiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple-banana-cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArrayNull() throws IOException {
 Joiner joiner = Joiner.on("-");
 StringWriter stringWriter = new StringWriter();
 joiner.appendTo(stringWriter, new Object[]{null, "banana", "cherry"});
 assertEquals("null-banana-cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectNullFirst() throws IOException {
 Joiner joiner = Joiner.on("-");
 StringWriter stringWriter = new StringWriter();
 joiner.appendTo(stringWriter, new Object[]{null, null, "cherry"});
 assertEquals("null-null-cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectNullSecond() throws IOException {
 Joiner joiner = Joiner.on("-");
 StringWriter stringWriter = new StringWriter();
 joiner.appendTo(stringWriter, new Object[]{"apple", null, "cherry"});
 assertEquals("apple-null-cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectNullAll() throws IOException {
 Joiner joiner = Joiner.on("-");
 StringWriter stringWriter = new StringWriter();
 joiner.appendTo(stringWriter, new Object[]{null, null, null});
 assertEquals("null-null-null", stringWriter.toString());
 }

 @Test
 public void testAppendToStringBuilder() {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on("|");
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple|banana|cherry", stringBuilder.toString());
 }

 @Test
 public void testJoinIterable() {
 Joiner joiner = Joiner.on("@");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple@banana@cherry", result);
 }

 @Test
 public void testJoinObjectArray() {
 Joiner joiner = Joiner.on("$");
 String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple$banana$cherry", result);
 }

 @Test
 public void testJoinObjectArrayNull() {
 Joinner joiner = Joiner.on("$");
 String result = joiner.join(new Object[]{null, "banana", "cherry"});
 assertEquals("null$banana$cherry", result);
 }

 @Test
 public void testJoinObjectNullFirst() {
 Joiner joiner = Joiner.on("$");
 String result = joiner.join(new Object[]{null, null, "cherry"});
 assertEquals("null$null$cherry", result);
 }

 @Test
 public void testJoinObjectNullSecond() {
 Joiner joiner = Joiner.on("$");
 String result = joiner.join(new Object[]{"apple", null, "cherry"});
 assertEquals("apple$null$cherry", result);
 }

 @Test
 public void testJoinObjectNullAll() {
 Joiner joiner = Joiner.on("$");
 String result = joiner.join(new Object[]{null, null, null});
 assertEquals("null$null$null", result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 String result = joiner.join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple,N/A,cherry", result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",").skipNulls();
 String result = joiner.join(Arrays.asList((Object[])new Object[]{"apple", null, "cherry"}));
 assertEquals("apple,cherry", result);
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new ConcurrentHashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "fruit");
 map.put("cherry", "fruit");
 Joiner mapJoiner = Joiner.on(":");
 String result = mapJoiner.join(map.entrySet());
 assertEquals("apple:fruit:banana:fruit:cherry:fruit", result);
 }
}