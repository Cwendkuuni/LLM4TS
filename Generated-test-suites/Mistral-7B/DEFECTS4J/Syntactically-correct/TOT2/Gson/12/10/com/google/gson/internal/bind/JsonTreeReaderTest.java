import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 private JsonElement jsonElement;
 private JsonTreeReader jsonTreeReader;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 jsonElement = new JsonObject();
 jsonTreeReader = new JsonTreeReader(jsonElement);
 }

 @Test
 public void testBeginArray() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.endArray();
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 }

 @Test
 public void testBeginObject() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.endObject();
 jsonTreeReader.expect(JsonToken.END_OBJECT);
 }

 @Test
 public void testHasNext() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(new JsonPrimitive("array1"));
 jsonTreeReader.push(new JsonPrimitive(1));
 jsonTreeReader.push(new JsonPrimitive(true));
 jsonTreeReader.expect(JsonToken.END_ARRAY); // this should fail
 jsonTreeReader.hasNext();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test
 public void testPeek() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(new JsonPrimitive("array1"));
 jsonTreeReader.push(new JsonPrimitive(1));
 jsonTreeReader.push(new JsonPrimitive(true));
 jsonTreeReader.expect(JsonToken.END_ARRAY); // this should fail
 jsonTreeReader.peek();
 assertFalse(jsonTreeReader.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testClose() throws IOException {
 jsonTreeReader.close();
 jsonTreeReader.nextInt();
 }

 @Test(expected = IllegalStateException.class)
 public void testCloseDuringArray() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.push(new JsonPrimitive("array1"));
 jsonTreeReader.close();
 jsonTreeReader.nextInt();
 }

 @Test(expected = IllegalStateException.class)
 public void testCloseDuringObject() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.push(new JsonObject());
 jsonTreeReader.close();
 jsonTreeReader.nextInt();
 }

 @Test
 public void testNextName() throws IOException {
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.NAME);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.NAME);
 }

 @Test
 public void testNextNull() throws IOException {
 jsonTreeReader.nextNull();
 jsonTreeReader.expect(JsonToken.NULL);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testNextString() throws IOException {
 jsonTreeReader.nextString();
 jsonTreeReader.expect(JsonToken.STRING);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.STRING);
 }

 @Test
 public void testNextBoolean() throws IOException {
 jsonTreeReader.nextBoolean();
 jsonTreeReader.expect(JsonToken.BOOLEAN);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.BOOLEAN);
 }

 @Test
 public void testNextNumber() throws IOException {
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.nextDouble();
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NUMBER);
 }

 @Test
 public void testBeginArrayWithNullName() throws IOException {
 jsonTreeReader.beginArray();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 }

 @Test
 public void testBeginObjectWithNullName() throws IOException {
 jsonTreeReader.beginObject();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.END_OBJECT);
 }

 @Test
 public void testPeekStackEmpty() throws IOException {
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.END_DOCUMENT);
 }

 @Test
 public void testPeekStackWithNullValue() throws IOException {
 jsonTreeReader.nextNull();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPeekStackWithJsonObject() throws IOException {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name1", "value1");
 jsonTreeReader.push(jsonObject);
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NAME);
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.STRING);
 jsonTreeReader.nextString();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPeekStackWithJsonArray() throws IOException {
 JsonArray jsonArray = new JsonArray();
 jsonArray.add(new JsonPrimitive(1));
 jsonTreeReader.push(jsonArray.iterator());
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.popStack(); // empty iterator
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 }

 @Test
 public void testPushNullValue() throws IOException {
 jsonTreeReader.nextNull();
 jsonTreeReader.pushNull();
 jsonTreeReader.expect(JsonToken.NULL);
 jsonTreeReader.nextNull();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPushJsonObject() throws IOException {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name1", "value1");
 jsonTreeReader.push(jsonObject);
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NAME);
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.STRING);
 jsonTreeReader.nextString();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPushJsonArray() throws IOException {
 JsonArray jsonArray = new JsonArray();
 jsonArray.add(new JsonPrimitive(1));
 jsonTreeReader.push(jsonArray.iterator());
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.popStack(); // empty iterator
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 }

 @Test
 public void testPushIterableWithNullValue() throws IOException {
 JsonArray jsonArray = new JsonArray();
 jsonArray.add(new JsonPrimitive("null"));
 jsonTreeReader.push(jsonArray.iterator());
 jsonTreeReader.expect(JsonToken.NULL);
 jsonTreeReader.nextNull();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPushIterableWithJsonObject() throws IOException {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name1", "value1");
 JsonArray jsonArray = new JsonArray();
 jsonArray.add(jsonObject);
 jsonTreeReader.push(jsonArray.iterator());
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.popStack(); // empty iterator
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.STRING);
 jsonTreeReader.nextString();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPushIterableWithJsonArray() throws IOException {
 JsonArray jsonArray1 = new JsonArray();
 jsonArray1.add(new JsonPrimitive(1));
 JsonArray jsonArray2 = new JsonArray();
 jsonArray2.add(jsonArray1);
 jsonTreeReader.push(jsonArray2.iterator());
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.popStack(); // empty iterator
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.popStack(); // array
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 }

 @Test
 public void testPopStackEmpty() throws IOException {
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPopStackWithJsonObject() throws IOException {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name1", "value1");
 jsonTreeReader.push(jsonObject);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NAME);
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.STRING);
 jsonTreeReader.nextString();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test
 public void testPopStackWithJsonArray() throws IOException {
 JsonArray jsonArray = new JsonArray();
 jsonArray.add(new JsonPrimitive(1));
 jsonTreeReader.push(jsonArray.iterator());
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.popStack(); // empty iterator
 jsonTreeReader.expect(JsonToken.NUMBER);
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 }

 @Test
 public void testPopStackWithNullValue() throws IOException {
 jsonTreeReader.nextNull();
 jsonTreeReader.pushNull();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 jsonTreeReader.nextNull();
 jsonTreeReader.popStack();
 jsonTreeReader.expect(JsonToken.NULL);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringName() throws IOException {
 jsonTreeReader.nextName();
 jsonTreeReader.expect(JsonToken.NUMBER);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringNull() throws IOException {
 jsonTreeReader.nextNull();
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringString() throws IOException {
 jsonTreeReader.nextString();
 jsonTreeReader.expect(JsonToken.BOOLEAN);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringBoolean() throws IOException {
 jsonTreeReader.nextBoolean();
 jsonTreeReader.expect(JsonToken.NUMBER);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringNumber() throws IOException {
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.END_DOCUMENT);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringEndArray() throws IOException {
 jsonTreeReader.expect(JsonToken.END_ARRAY);
 jsonTreeReader.nextInt();
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringEndObject() throws IOException {
 jsonTreeReader.expect(JsonToken.END_OBJECT);
 jsonTreeReader.nextInt();
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringEndDocument() throws IOException {
 jsonTreeReader.nextInt();
 jsonTreeReader.expect(JsonToken.END_DOCUMENT);
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringBeginArray() throws IOException {
 jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
 jsonTreeReader.nextInt();
 }

 @Test(expected = AssertionError.class)
 public void testAssertionErrorDuringBeginObject() throws IOException {
 jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
 jsonTreeReader.nextInt();
 }
}