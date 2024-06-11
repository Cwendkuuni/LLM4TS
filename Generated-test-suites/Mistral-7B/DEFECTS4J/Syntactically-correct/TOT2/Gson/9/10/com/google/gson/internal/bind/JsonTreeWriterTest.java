package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

 @Test
 public void testJsonTreeWriterConstructionAndGettingTopLevelObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 Assert.assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriterArray() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 Assert.assertTrue(jsonElement instanceof JsonArray);
 }

 @Test
 public void testJsonTreeWriterObjectWithValidName() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 Assert.assertTrue(jsonElement instanceof JsonObject);
 JsonObject jsonObject = (JsonObject) jsonElement;
 Assert.assertEquals("testName", jsonObject.name());
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterObjectWithNullName() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name(null);
 jsonTreeWriter.endObject();
 }

 @Test
 public void testJsonTreeWriterAddingValueToEmptyStack() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.value("testValue");
 JsonElement jsonElement = jsonTreeWriter.get();
 Assert.assertEquals(new JsonPrimitive("testValue"), jsonElement);
 }

 @Test
 public void testJsonTreeWriterAddingValueToArray() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value(new JsonPrimitive(1));
 jsonTreeWriter.value(new JsonPrimitive(2));
 jsonTreeWriter.value(new JsonPrimitive(3));
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 Assert.assertEquals(3, jsonArray.size());
 }

 @Test
 public void testJsonTreeWriterAddingValueToObject() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value("testValue");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 JsonPrimitive jsonPrimitive = jsonObject.get("testName").getAsJsonPrimitive();
 Assert.assertEquals("testValue", jsonPrimitive.getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsageOfBeginArrayAndEndArray() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value(new JsonPrimitive(1));
 jsonTreeWriter.endObject(); // wrong usage of endObject()
 jsonTreeWriter.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsageOfNameWithEmptyStack() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.name("testName");
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsageOfValueWithEmptyStack() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.value("testValue");
 }

 @Test
 public void testJsonTreeWriterHandlingNullValue() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value(null);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 JsonPrimitive jsonPrimitive = jsonObject.get("testName");
 Assert.assertEquals(JsonNull.INSTANCE, jsonPrimitive);
 }

 @Test
 public void testJsonTreeWriterHandlingEmptyInput() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.close();
 JsonElement jsonElement = jsonTreeWriter.get();
 Assert.assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriterHandlingInfiniteRecursion() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.endObject();
 jsonTreeWriter.endObject();
 jsonTreeWriter.get();
 }

 @Test(expected = StackOverflowError.class)
 public void testJsonTreeWriterHandlingStackOverflow() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 for (int i = 0; i < 10000; i++) {
 jsonTreeWriter.value(new JsonPrimitive(i));
 }
 jsonTreeWriter.get();
 }

}