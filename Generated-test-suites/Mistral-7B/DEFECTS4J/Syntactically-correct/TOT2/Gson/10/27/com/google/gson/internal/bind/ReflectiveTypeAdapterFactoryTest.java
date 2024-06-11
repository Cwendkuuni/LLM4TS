import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

 private ReflectiveTypeAdapterFactory factory;
 private TypeAdapter<JsonObject> jsonObjectAdapter;

 @Before
 public void setup() {
 Gson gson = new Gson();
 factory = new ReflectiveTypeAdapterFactory(gson.newInstance(null),
 new FieldNamingStrategy() {
 @Override public String translateName(Field field) {
 return field.getName();
 }
 },
 gson.newExcluder());
 jsonObjectAdapter = factory.create(gson, new TypeToken<JsonObject>() {});
 }

 @Test
 public void testSerializeAndDeserializeSimpleObject() throws Exception {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name", "John Doe");
 jsonObject.addProperty("age", 30);

 String json = jsonObjectAdapter.toJson(jsonObject);
 assertNotNull(json);
 assertEquals("{\"name\":\"John Doe\",\"age\":30}", json);

 JsonElement jsonElement = new JsonParser().parse(json);
 JsonObject deserializedObject = jsonObjectAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
 assertEquals(jsonObject.get("name").getAsString(), deserializedObject.get("name").getAsString());
 assertEquals(jsonObject.get("age").getAsInt(), deserializedObject.get("age").getAsInt());
 }

 @Test
 public void testSerializeAndDeserializeObjectWithExcludedField() throws Exception {
 JsonObject jsonObject = new JsonObject();
 jsonObject.addProperty("name", "John Doe");
 jsonObject.addProperty("age", 30);
 jsonObject.add("transientField", new JsonPrimitive(123));

 String json = jsonObjectAdapter.toJson(jsonObject);
 assertNotNull(json);
 assertEquals("{\"name\":\"John Doe\",\"age\":30}", json);

 JsonElement jsonElement = new JsonParser().parse(json);
 JsonObject deserializedObject = jsonObjectAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
 assertEquals(jsonObject.get("name").getAsString(), deserializedObject.get("name").getAsString());
 assertEquals(jsonObject.get("age").getAsInt(), deserializedObject.get("age").getAsInt());
 assertNull(deserializedObject.get("transientField"));
 }

 @Test
 public void testSerializeAndDeserializeObjectWithJsonAdapter() throws Exception {
 JsonObject jsonObject = new JsonObject();
 jsonObject.add("name", new JsonPrimitive("John Doe"));
 jsonObject.add("age", new JsonPrimitive(30));
 jsonObject.addProperty("transientField", "transient value");

 Class<MyClass> clazz = MyClass.class;
 jsonObject.add("myCustomField", new JsonPrimitive(new MyClass()));
 jsonObject.addProperty("myCustomField.name", "test");
 jsonObject.addProperty("myCustomField.age", 5);

 String json = jsonObjectAdapter.toJson(jsonObject);
 assertNotNull(json);
 assertEquals("{" +
 "\"name\":{\"className\":\"com.example.MyClass\",\"value\":{\"name\":\"test\",\"age\":5}},\"age\":30," +
 "\"transientField\":\"transient value\"" +
 "}", json);

 JsonElement jsonElement = new JsonParser().parse(json);
 JsonObject deserializedObject = jsonObjectAdapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
 MyClass deserializedObjectMyCustomField = deserializedObject.get("myCustomField").getAsJsonObject().get("value").getAsJsonPrimitive().getAsObject().getAsInstanceOf(MyClass.class);
 assertEquals("test", deserializedObjectMyCustomField.name);
 assertEquals(5, deserializedObjectMyCustomField.age);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testDeserializationWithMalformedJson() throws Exception {
 String json = "{\"name\":\"John Doe\",\"age\":[30]";
 jsonObjectAdapter.read(new JsonReader(new StringReader(json)));
 }

 @Test(expected = IllegalAccessException.class)
 public void testWriteFieldWithPrivateField() throws Exception {
 MyPrivateClass myPrivateClass = new MyPrivateClass();
 myPrivateClass.privateField = "test";
 String json = jsonObjectAdapter.toJson(myPrivateClass);
 assertEquals("{\"name\":\"MyPrivateClass\",\"writeFieldWithPrivateField\":{\"name\":\"test\",\"writeFieldWithPrivateField\":[\"test\"]}}", json);
 }
}

class MyClass {
 String name;
 int age;

 public MyClass() {
}

 public MyClass(String name, int age) {
 this.name = name;
 this.age = age;
}
}

class MyPrivateClass {
 private String privateField;

 public String getPrivateField() {
 return privateField;
}

 public void setPrivateField(String privateField) {
 this.privateField = privateField;
}
}