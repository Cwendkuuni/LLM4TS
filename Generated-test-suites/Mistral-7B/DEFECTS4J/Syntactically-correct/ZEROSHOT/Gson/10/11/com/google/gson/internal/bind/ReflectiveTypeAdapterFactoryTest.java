import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReflectiveTypeAdapterFactoryTest {

 private ReflectiveTypeAdapterFactory factory;
 private Gson gson;

 @Before
 public void setUp() {
 FieldNamingPolicy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
 Excluder excluder = new Excluder() {
 @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
 return false;
 }

 @Override public boolean excludeField(Field field, boolean serialize) {
 return false;
 }
 };
 ConstructorConstructor constructorConstructor = new ConstructorConstructor();
 factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
 gson = new GsonBuilder().setPrettyPrinting().create();
 }

 @Test
 public void testCreateTypeAdapter() throws Exception {
 Class<?> clazz = TestClass.class;
 TypeToken<?> typeToken = TypeToken.get(clazz);
 TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
 assertEquals(TestClass.class, adapter.getClass());
 }

 @Test
 public void testCreateTypeAdapterForInterface() throws Exception {
 Class<?> clazz = TestInterface.class;
 TypeToken<?> typeToken = TypeToken.get(clazz);
 assertNull(factory.create(gson, typeToken));
 }

 @Test
 public void testCreateTypeAdapterForPrimitive() throws Exception {
 Class<?> clazz = int.class;
 TypeToken<?> typeToken = TypeToken.get(clazz);
 assertNull(factory.create(gson, typeToken));
 }

 @Test
 public void testWriteReadObject() throws Exception {
 TestClass objectToWrite = new TestClass();
 objectToWrite.field1 = "value1";
 objectToWrite.field2 = true;
 objectToWrite.field3 = new TestInnerClass();

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.write(writer, objectToWrite);
 writer.flush();

 TestClass objectToRead = new TestClass();
 JsonObject jsonObject = new JsonParser(new ByteArrayInputStream(outputStream.toByteArray())).getAsJsonObject();
 TypeAdapter<TestClass> adapter2 = factory.create(gson, TypeToken.get(TestClass.class));
 adapter2.read(new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()))).getValue();

 assertEquals(objectToWrite.field1, objectToRead.field1);
 assertEquals(objectToWrite.field2, objectToRead.field2);
 assertEquals(objectToWrite.field3, objectToRead.field3);
 }

 @Test
 public void testExcludeFields() throws Exception {
 TestClass objectToWrite = new TestClass();
 objectToWrite.field1 = "value1";
 objectToWrite.field2 = true;
 objectToWrite.field3 = new TestInnerClass();
 objectToWrite.excludedField = "excludedValue";

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.write(writer, objectToWrite);
 writer.flush();

 JsonElement jsonElement = new JsonParser(new ByteArrayInputStream(outputStream.toByteArray())).getRoot();
 assertTrue(jsonElement.isJsonObject());
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertFalse(jsonObject.has("excludedField"));
 }

 @Test
 public void testWriteField() throws Exception {
 TestClass object = new TestClass();
 TestClass original = new TestClass();
 object.field1 = "newValue";
 Field field = TestClass.class.getDeclaredField("field1");
 field.setAccessible(true);

 JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 assertFalse(adapter.writeField(object));
 original.field1 = "newValue";
 assertTrue(adapter.writeField(original));
 }

 @Test
 public void testJsonAdapter() throws Exception {
 TestClass objectToWrite = new TestClass();
 objectToWrite.field1 = new TestJsonAdapterClass();

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.write(writer, objectToWrite);
 writer.flush();

 TestClass objectToRead = new TestClass();
 JsonObject jsonObject = new JsonParser(new ByteArrayInputStream(outputStream.toByteArray())).getAsJsonObject();
 TypeAdapter<TestClass> adapter2 = factory.create(gson, TypeToken.get(TestClass.class));
 adapter2.read(new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()))).getValue();

 assertEquals(objectToWrite.field1, objectToRead.field1);
 assertInstanceOf(TestJsonAdapterClass.class, objectToRead.field1);
 }

 @Test
 public void testSerializedName() throws Exception {
 TestClass objectToWrite = new TestClass();
 objectToWrite.fieldWithSerializedName = "value1";

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.write(writer, objectToWrite);
 writer.flush();

 JsonElement jsonElement = new JsonParser(new ByteArrayInputStream(outputStream.toByteArray())).getRoot();
 assertTrue(jsonElement.isJsonObject());
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("value1", jsonObject.get("fieldWithSerializedName").getAsString());
 }

 @Test
 public void testAlternateSerializedName() throws Exception {
 TestClass objectToWrite = new TestClass();
 objectToWrite.fieldWithAlternateSerializedName = "value1";

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.write(writer, objectToWrite);
 writer.flush();

 JsonElement jsonElement = new JsonParser(new ByteArrayInputStream(outputStream.toByteArray())).getRoot();
 assertTrue(jsonElement.isJsonObject());
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("value1", jsonObject.get("fieldWithSerializedNameOrAlternate").getAsString());
 }

 @Test
 public void testWriteFieldWithJsonAdapter() throws Exception {
 TestClass object = new TestClass();
 TestClass original = new TestClass();
 TestJsonAdapterClass testJsonAdapterClass = new TestJsonAdapterClass();
 object.fieldWithJsonAdapter = testJsonAdapterClass;
 Field field = TestClass.class.getDeclaredField("fieldWithJsonAdapter");
 field.setAccessible(true);

 JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 assertFalse(adapter.writeField(object));
 original.fieldWithJsonAdapter = testJsonAdapterClass;
 assertTrue(adapter.writeField(original));
 }

 @Test
 public void testReadFieldWithJsonAdapter() throws Exception {
 TestClass objectToWrite = new TestClass();
 TestClass objectToRead = new TestClass();
 TestJsonAdapterClass testJsonAdapterClass = new TestJsonAdapterClass();
 objectToWrite.fieldWithJsonAdapter = testJsonAdapterClass;

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 adapterWrite(writer, objectToWrite);
 writer.flush();

 JsonReader reader = new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()));
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.read(reader).getValue();

 assertEquals(testJsonAdapterClass, objectToRead.fieldWithJsonAdapter);
 }

 @Test
 public void testWriteFieldWithJsonAdapterAlternateName() throws Exception {
 TestClass object = new TestClass();
 TestClass original = new TestClass();
 TestJsonAdapterClass testJsonAdapterClass = new TestJsonAdapterClass();
 object.fieldWithJsonAdapter = testJsonAdapterClass;
 Field field = TestClass.class.getDeclaredField("fieldWithJsonAdapter");
 field.setAccessible(true);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 writer.name("fieldWithJsonAdapterAlternateName");
 adapterWrite(writer, object);
 writer.flush();

 TestClass objectToRead = new TestClass();
 JsonReader reader = new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()));
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.read(reader).getValue();

 assertEquals(testJsonAdapterClass, objectToRead.fieldWithJsonAdapter);
 }

 @Test
 public void testReadFieldWithJsonAdapterAlternateName() throws Exception {
 TestClass objectToWrite = new TestClass();
 TestClass objectToRead = new TestClass();
 TestJsonAdapterClass testJsonAdapterClass = new TestJsonAdapterClass();
 objectToWrite.fieldWithJsonAdapter = testJsonAdapterClass;

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonWriter writer = new JsonWriter(outputStream);
 writer.name("fieldWithJsonAdapterAlternateName");
 adapterWrite(writer, objectToWrite);
 writer.flush();

 JsonReader reader = new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()));
 reader.beginObject();
 reader.nextName("fieldWithJsonAdapterAlternateName");
 reader.nextNull();
 reader.endObject();

 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.read(reader).getValue();

 assertEquals(testJsonAdapterClass, objectToRead.fieldWithJsonAdapter);
 }

 private void adapterWrite(JsonWriter writer, Object obj) throws IOException {
 TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));
 adapter.write(writer, obj);
 }

 private static class TestJsonAdapterClass {
 }

 private static class TestClass {
 @SerializedName("fieldWithSerializedName")
 String fieldWithSerializedName;
 @SerializedName({"fieldWithSerializedNameOrAlternate", "anotherName"})
 String fieldWithAlternateSerializedName;
 @JsonAdapter(TestJsonAdapterClass.class)
 TestJsonAdapterClass fieldWithJsonAdapter;
 @JsonAdapter(TestJsonAdapterClass2.class)
 TestJsonAdapterClass2 fieldWithJsonAdapter2;
 @JsonAdapter(TestJsonAdapterClass.class)
 @SerializedName("fieldWithSerializedNameAndJsonAdapter")
 String fieldWithSerializedNameAndJsonAdapter;
 @JsonAdapter(TestJsonAdapterClass2.class)
 @SerializedName("fieldWithSerializedNameAndJsonAdapter2")
 String fieldWithSerializedNameAndJsonAdapter2;
 @JsonAdapter(TestJsonAdapterClass.class)
 @SerializedName("fieldWithSerializedNameAndJsonAdapter3")
 String fieldWithSerializedNameAndJsonAdapter3;
 @JsonAdapter(TestJsonAdapterClass3.class)
 @SerializedName("fieldWithSerializedNameAndJsonAdapter4")
 String fieldWithSerializedNameAndJsonAdapter4;
 @JsonAdapter(TestJsonAdapterClass.class)
 @SerializedName("fieldWithJsonAdapter")
 TestJsonAdapterClass fieldWithJsonAdapter;
 @JsonAdapter(TestJsonAdapterClass2.class)
 @SerializedName("fieldWithJsonAdapter2")
 TestJsonAdapterClass2 fieldWithJsonAdapter2;
 @JsonAdapter(TestJsonAdapterClass.class)
 @SerializedName("fieldWithJsonAdapter3")
 TestJsonAdapterClass fieldWithJsonAdapter3;
 @JsonAdapter(TestJsonAdapterClass3.class)
 @SerializedName("fieldWithJsonAdapter4")
 TestJsonAdapterClass3 fieldWithJsonAdapter4;
 @JsonAdapter(TestJsonAdapterClass.class)
 @SerializedName("excludedField")
 String excludedField;
 TestInnerClass field3;

 static class TestInnerClass {
 }
 }

 private static class TestInterface {
 }
}