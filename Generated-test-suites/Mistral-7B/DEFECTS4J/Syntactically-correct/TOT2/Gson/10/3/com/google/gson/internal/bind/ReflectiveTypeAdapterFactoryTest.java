import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;
 private ReflectiveTypeAdapterFactory factory;

 @Before
 public void setup() {
 gson = new GsonBuilder().create();
 factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
 }

 @Test
 public void testCreateTypeAdapter() {
 TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
 TypeAdapter<MyClass> adapter = factory.create(gson, typeToken);
 assertNotNull(adapter);
 }

 @Test
 public void testWriteReadNull() throws IOException {
 MyClass obj = null;
 String json = "{}";
 MyClass readObj = gson.fromJson(json, MyClass.class);
 assertNull(readObj);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
 MyClass writtenObj = adapter.read(reader);
 assertNull(writtenObj);
 }

 @Test
 public void testWriteReadEmptyObject() throws IOException {
 MyClass obj = new MyClass();
 String json = gson.toJson(obj);
 assertEquals("{}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
 MyClass readObj = adapter.read(reader);
 assertEquals(obj, readObj);
 }

 @Test
 public void testWriteReadObjectWithSerializedName() throws IOException {
 MyClass obj = new MyClass("test");
 String json = gson.toJson(obj);
 assertEquals("{\"test\":\"test\"}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
 MyClass readObj = adapter.read(reader);
 assertEquals(obj, readObj);
 }

 @Test
 public void testWriteReadObjectWithAlternateName() throws IOException {
 MyClass obj = new MyClass("test");
 String json = gson.toJson(obj);
 assertEquals("{\"alternateName\":\"test\"}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
 MyClass readObj = adapter.read(reader);
 assertEquals(obj, readObj);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testWriteReadObjectWithInvalidName() throws IOException {
 MyClass obj = new MyClass("test");
 String json = gson.toJson(obj);
 String invalidJson = json.replace("test", "invalidName");

 JsonReader reader = new JsonReader(new StringReader(invalidJson));
 TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
 MyClass readObj = adapter.read(reader);
 fail("Should throw JsonSyntaxException");
 }

 @Test
 public void testWriteReadObjectWithPrimitiveType() throws IOException {
 MyClass obj = new MyClass(1, 2f, 3.0);
 String json = gson.toJson(obj);
 assertEquals("{\"intField\":1,\"floatField\":2.0,\"doubleField\":3.0}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
 MyClass readObj = adapter.read(reader);
 assertEquals(obj, readObj);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testWriteReadObjectWithJsonAdapter() throws IOException {
 MyClass obj = new MyClassWithJsonAdapter();
 String json = gson.toJson(obj);
 assertEquals("{\"customField\":\"customValue\"}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeAdapter<MyClassWithJsonAdapter> adapter = factory.create(gson, new TypeToken<MyClassWithJsonAdapter>() {});
 MyClassWithJsonAdapter readObj = adapter.read(reader);
 fail("Should throw JsonSyntaxException");
 }

 @Test
 public void testWriteReadObjectWithExcludedField() throws IOException {
 MyClass obj = new MyClass("test", 1);
 String json = gson.toJson(obj);
 assertEquals("{\"test\":\"test\"}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
 TypeAdapter<MyClass> adapter = factory.create(gson, typeToken);
 MyClass readObj = adapter.read(reader);
 assertEquals(obj.name, readObj.name);
 assertEquals(1, readObj.intField);
 assertNull(readObj.excludedField);
 }

 @Test
 public void testWriteReadObjectWithExcludedClass() throws IOException {
 MyClassObj obj = new MyClassObj("test", new MyNestedClass());
 String json = gson.toJson(obj);
 assertEquals("{\"test\":\"test\"}", json);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeToken<MyClassObj> typeToken = new TypeToken<MyClassObj>() {};
 TypeAdapter<MyClassObj> adapter = factory.create(gson, typeToken);
 MyClassObj readObj = adapter.read(reader);
 assertEquals(obj.name, readObj.name);
 assertNull(readObj.nestedObj);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testWriteReadObjectWithExcludedClassSerialization() throws IOException {
 MyClassObj obj = new MyClassObj("test", new MyNestedClass());
 String json = gson.toJson(obj);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeToken<MyClassObj> typeToken = new TypeToken<MyClassObj>() {};
 TypeAdapter<MyClassObj> adapter = factory.create(gson, typeToken);
 MyClassObj readObj = adapter.read(reader);
 fail("Should throw JsonSyntaxException");
 }

 @Test
 public void testWriteReadObjectWithExcludedClassDeserialization() throws IOException {
 MyClassObj obj = new MyClassObj("test", null);
 String json = gson.toJson(obj);

 JsonReader reader = new JsonReader(new StringReader(json));
 TypeToken<MyClassObj> typeToken = new TypeToken<MyClassObj>() {};
 TypeAdapter<MyClassObj> adapter = factory.create(gson, typeToken);
 MyClassObj readObj = adapter.read(reader);
 assertEquals(obj.name, readObj.name);
 assertNull(readObj.nestedObj);
 }

 private static class MyClass {
 @SerializedName("name")
 private String name;
 private int intField;
 private float floatField;
 private double doubleField;

 public MyClass(String name, int intField, float floatField, double doubleField) {
 this.name = name;
 this.intField = intField;
 this.floatField = floatField;
 this.doubleField = doubleField;
 }

 public MyClass(String name, int intField) {
 this(name, intField, 0f, 0.0);
 }
 }

 private static class MyClassWithJsonAdapter {
 @JsonAdapter(MyClassJsonAdapter.class)
 private String customField;

 public MyClassWithJsonAdapter() {
 this("customValue");
 }

 public MyClassWithJsonAdapter(String customField) {
 this.customField = customField;
 }
 }

 private static class MyClassJsonAdapter extends TypeAdapter<MyClassWithJsonAdapter> {
 @Override public MyClassWithJsonAdapter read(JsonReader reader) throws IOException {
 return new MyClassWithJsonAdapter("deserializedValue");
 }

 @Override public void write(JsonWriter writer, MyClassWithJsonAdapter value) throws IOException {
 writer.value(value.customField);
 }
 }

 private static class MyNestedClass {

 }

 private static class MyClassObj {
 @SerializedName("test")
 private String name;
 private MyNestedClass nestedObj;

 public MyClassObj(String name, MyNestedClass nestedObj) {
 this.name = name;
 this.nestedObj = nestedObj;
 }
 }
}