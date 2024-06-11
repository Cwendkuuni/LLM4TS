import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;

 @Before
 public void setup() {
 gson = new GsonBuilder()
 .setFieldNamingPolicy(new FieldNamingPolicy())
 .create();
 }

 @Test
 public void testCreateAdapterForSimpleClass() {
 Class<SimpleClass> simpleClassType = SimpleClass.class;
 TypeAdapter<SimpleClass> adapter = gson.getDelegateAdapterFactory().getAdapter(simpleClassType);
 assertNotNull(adapter);
 }

 @Test
 public void testCreateAdapterForInterface() {
 Class<TestInterface> testInterfaceType = TestInterface.class;
 TypeAdapter<TestInterface> adapter = gson.getDelegateAdapterFactory().getAdapter(testInterfaceType);
 assertNull(adapter);
 }

 @Test
 public void testSerializedNameAnnotations() throws IOException {
 SimpleClass serializableClass = new SimpleClass("John Doe", 30);
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(serializableClass, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("{\"name\":\"John Doe\",\"age\":30}", jsonElement.getAsString());
 }

 @Test
 public void testJsonAdapterAnnotations() throws IOException {
 SimpleClassWithJsonAdapter serializableClass = new SimpleClassWithJsonAdapter();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(serializableClass, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("{\"foo\":[1,2,3]}", jsonElement.getAsString());
 }

 @Test
 public void testReadNullValue() throws IOException {
 SimpleClass nullValue = null;
 JsonElement jsonElement = new JsonObject().get("nullValue");
 TypeAdapter<SimpleClass> adapter = gson.getDelegateAdapterFactory().getAdapter(SimpleClass.class);
 SimpleClass deserializedClass = adapter.read(new JsonReader(new JsonObject(jsonElement.getAsJsonArray().get(0).getAsJsonObject()). streams()));
 assertNull(deserializedClass);
 }

 @Test
 public void testReadWithoutDefaultName() throws IOException {
 SimpleClass withoutDefaultName = new SimpleClass("Jane Doe", 25);
 JsonObject jsonObject = new JsonObject();
 jsonObject.add("age", new JsonPrimitive(25));
 jsonObject.add("name", new JsonPrimitive("Jane Doe"));
 TypeToken<SimpleClass> simpleClassType = new TypeToken<SimpleClass>() {};
 TypeAdapter<SimpleClass> adapter = gson.getDelegateAdapterFactory().getAdapter(simpleClassType);
 SimpleClass deserializedClass = adapter.read(new JsonReader(new JsonObject(jsonObject)));
 assertEquals("Jane Doe", deserializedClass.name);
 assertEquals(25, deserializedClass.age);
 }

 @Test
 public void testReadMultipleFieldsWithSameName() throws IOException {
 SimpleClassWithMultipleFields multipleFields = new SimpleClassWithMultipleFields("John Doe", 30, "Alice", 25);
 JsonObject jsonObject = new JsonObject();
 jsonObject.add("name", new JsonPrimitive("Alice"));
 jsonObject.add("name", new JsonPrimitive("John Doe"));
 jsonObject.add("age", new JsonPrimitive(25));
 jsonObject.add("age", new JsonPrimitive(30));
 TypeToken<SimpleClassWithMultipleFields> simpleClassType = new TypeToken<SimpleClassWithMultipleFields>() {};
 TypeAdapter<SimpleClassWithMultipleFields> adapter = gson.getDelegateAdapterFactory().getAdapter(simpleClassType);
 SimpleClassWithMultipleFields deserializedClass = adapter.read(new JsonReader(jsonObject));
 assertEquals("John Doe", deserializedClass.name1);
 assertEquals("Alice", deserializedClass.name2);
 assertEquals(30, deserializedClass.age1);
 assertEquals(25, deserializedClass.age2);
 }

 @Test
 public void testReadFieldsWithExcludedName() throws IOException {
 SimpleClassWithExcludedName excludedName = new SimpleClassWithExcludedName("John Doe", 30);
 JsonObject jsonObject = new JsonObject();
 jsonObject.add("age", new JsonPrimitive(30));
 TypeToken<SimpleClassWithExcludedName> simpleClassType = new TypeToken<SimpleClassWithExcludedName>() {};
 TypeAdapter<SimpleClassWithExcludedName> adapter = gson.getDelegateAdapterFactory().getAdapter(simpleClassType);
 SimpleClassWithExcludedName deserializedClass = adapter.read(new JsonReader(jsonObject));
 assertFalse(deserializedClass.nameSerialized);
 assertEquals("John Doe", deserializedClass.name);
 assertEquals(30, deserializedClass.age);
 }

 @Test
 public void testWriteFieldsInCorrectOrder() throws IOException {
 SimpleClassWithOrderedFields orderedFields = new SimpleClassWithOrderedFields("John Doe", 30, "Alice Doe", 25);
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(orderedFields, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("{\"name1\":\"Alice Doe\",\"name2\":\"John Doe\",\"age1\":30,\"age2\":25}", jsonElement.getAsString());
 }

 @Test
 public void testWriteFieldsWithJsonAdapter() throws IOException {
 SimpleClassWithJsonAdapter jsonAdapter = new SimpleClassWithJsonAdapter();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(jsonAdapter, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("{\"foo\":[1,2,3]}", jsonElement.getAsString());
 }

 @Test
 public void testReadFieldsWithJsonAdapter() throws IOException {
 SimpleClassWithJsonAdapter jsonAdapter = new SimpleClassWithJsonAdapter();
 JsonElement jsonElement = new JsonObject().get("jsonAdapter");
 TypeToken<SimpleClassWithJsonAdapter> simpleClassType = new TypeToken<SimpleClassWithJsonAdapter>() {};
 TypeAdapter<SimpleClassWithJsonAdapter> adapter = gson.getDelegateAdapterFactory().getAdapter(simpleClassType);
 SimpleClassWithJsonAdapter deserializedClass = adapter.read(new JsonReader(new JsonObject(jsonElement.getAsJsonArray().get(0).getAsJsonObject()). streams()));
 assertEquals(new int[] {1, 2, 3}, deserializedClass.foo);
 }

 @Test
 public void testWriteNullValue() throws IOException {
 SimpleClassWithNullValue nullValue = null;
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(nullValue, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("null", jsonElement.getAsString());
 }

 @Test
 public void testWriteFieldWithExcludedName() throws IOException {
 SimpleClassWithExcludedName excludedName = new SimpleClassWithExcludedName("John Doe", 30);
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(excludedName, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("{\"age\":30}", jsonElement.getAsString());
 }

 @Test
 public void testWriteFieldWithoutSerialization() throws IOException {
 SimpleClassWithoutSerialization serialization = new SimpleClassWithoutSerialization();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(serialization, jsonWriter);
 JsonElement jsonElement = jsonWriter.getReader().peek();
 assertEquals("{}", jsonElement.getAsString());
 }

 @Test(expected = IllegalAccessException.class)
 public void testWriteFieldWithPrivateAccess() throws IOException {
 SimpleClassWithPrivateAccess privateAccess = new SimpleClassWithPrivateAccess();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(privateAccess, jsonWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteFieldWithWrongType() throws IOException {
 SimpleClassWithWrongType wrongType = new SimpleClassWithWrongType();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(wrongType, jsonWriter);
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteFieldWithCircularDependency() throws IOException {
 SimpleClassWithCircularDependency circularDependency = new SimpleClassWithCircularDependency();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 gson.toJson(circularDependency, jsonWriter);
 }
}