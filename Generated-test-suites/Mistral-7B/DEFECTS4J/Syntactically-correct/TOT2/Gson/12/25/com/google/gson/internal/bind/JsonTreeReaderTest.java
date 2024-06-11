import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    Assert.assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.beginArray();
    reader.beginObject();
    reader.push("key");
    reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextNull();
    reader.nextInt();
    reader.nextLong();
    reader.nextDouble();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();

    Assert.assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.close();
    reader.nextName();
  }

  @Test
  public void testNextName() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.push("name1");
    Assert.assertEquals("name1", reader.nextName());
    reader.push("name2");
    Assert.assertEquals("name2", reader.nextName());
    reader.endObject();

    reader.beginObject();
    reader.push("name3");
    Assert.assertEquals("name3", reader.nextName());
    reader.endObject();

    reader.nextName(); // should return null
    Assert.assertNull(reader.nextName());
  }

  @Test
  public void testNextString() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.push("{\"string\": \"Hello World\"}");
    Assert.assertEquals("Hello World", reader.nextString());
    reader.endObject();

    reader.beginObject();
    reader.push("{\"string\": null}");
    Assert.assertNull(reader.nextString());
    reader.endObject();
  }

  @Test
  public void testNextBoolean() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.push(new JsonPrimitive(true));
    Assert.assertTrue(reader.nextBoolean());
    reader.endObject();

    reader.beginObject();
    reader.push(new JsonPrimitive(false));
    Assert.assertFalse(reader.nextBoolean());
    reader.endObject();
  }

  @Test
  public void testNextNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.push(JsonNull.INSTANCE);
    reader.nextNull();
    reader.endObject();
  }

  @Test
  public void testNextNumber() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.push(new JsonPrimitive(123));
    Assert.assertEquals(123, reader.nextInt());
    reader.endObject();

    reader.beginObject();
    reader.push(new JsonPrimitive(123.45));
    Assert.assertEquals(123.45, reader.nextDouble(), 0.001);
    reader.endObject();

    reader.beginObject();
    reader.push(new JsonPrimitive(123L));
    Assert.assertEquals(123, reader.nextLong());
    reader.endObject();

    reader.beginObject();
    reader.push(new JsonPrimitive(new JsonObject()));
    Assert.assertNull(reader.nextNumber());
    reader.endObject();
  }

  @Test
  public void testNextArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.push(new JsonPrimitive(1));
    reader.push(new JsonPrimitive(2));
    reader.push(new JsonPrimitive(3));
    reader.endArray();

    Assert.assertEquals(1, reader.nextInt());
    Assert.assertEquals(2, reader.nextInt());
    Assert.assertEquals(3, reader.nextInt());
  }

  @Test
  public void testNextObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.push("obj1");
    reader.nextName();
    reader.push(new JsonObject());
    reader.push("key1", new JsonPrimitive("value1"));
    reader.push("key2", new JsonPrimitive(1));
    reader.push("key3", new JsonPrimitive(123L));
    reader.push("key4", new JsonPrimitive(true));
    reader.push("key5", new JsonArray());
    reader.push("key5", new JsonPrimitive("value2"));
    reader.endObject();
    reader.endObject();

    Assert.assertEquals("value1", reader.nextString());
    Assert.assertEquals(1, reader.nextInt());
    Assert.assertEquals(123L, reader.nextLong());
    Assert.assertTrue(reader.nextBoolean());
    reader.beginArray();
    Assert.assertEquals("value2", reader.nextString());
    reader.endArray();

    reader.nextName(); // should return null
    Assert.assertNull(reader.nextString());
  }

  @Test
  public void testClose() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.close();
    Assert.assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test(expected = AssertionError.class)
  public void testAssertionError() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.push("null");
    reader.nextNull();
    reader.nextInt();
  }
}