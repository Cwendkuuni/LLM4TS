import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testCreateTypeAdapter() {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<MyClass> adapter = factory.create(gson, new TypeToken<MyClass>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeField() {
    Class<?> clazz = MyClassWithExcludedField.class;
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(
        clazz.getDeclaredField("excludedField"), true));
    assertTrue(ReflectiveTypeAdapterFactory.excludeField(
        clazz.getDeclaredField("nonExcludedField"), true));
    assertFalse(ReflectiveTypeAdapterFactory.excludeField(
        clazz.getDeclaredField("nonExcludedField"), false));
  }

  @Test
  public void testGetFieldNames() {
    Class<?> clazz = MyClassWithCustomAnnotations.class;
    Field field = clazz.getDeclaredField("customField");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    assertEquals(2, fieldNames.size());
    assertTrue(fieldNames.contains("customField"));
    assertTrue(fieldNames.contains("customAlternate"));
  }

  @Test
  public void testCreateBoundField() {
    Gson gson = new GsonBuilder().create();
    Class<?> clazz = MyClassWithBoundFields.class;
    Field field = clazz.getDeclaredField("boundField");
    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<?> typeAdapter = gson.getAdapter(field.getType());
    BoundField boundField = factory.createBoundField(gson, field, "defaultName",
        new TypeToken<Object>() {}, true, false);
    assertNotNull(boundField);
    assertEquals("defaultName", boundField.name);
    assertTrue(boundField.serialized);
    assertFalse(boundField.deserialized);
  }

  @Test
  public void testCreateTypeAdapterWithInterface() {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<MyInterface> adapter = factory.create(gson, new TypeToken<MyInterface>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testWriteReadSimpleTypes() throws IOException {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<Integer> intAdapter = factory.create(gson, new TypeToken<Integer>() {});
    TypeAdapter<String> stringAdapter = factory.create(gson, new TypeToken<String>() {});
    JsonWriter writer = new JsonWriter(System.out);
    JsonReader reader = new JsonReader(new StringReader("{\"int\":42,\"string\":\"hello\"}"));

    writer.beginObject();
    intAdapter.write(writer, 42);
    stringAdapter.write(writer, "world");
    writer.endObject();

    int value1 = intAdapter.read(reader).getValue().getAsInt();
    assertEquals(42, value1);

    String value2 = stringAdapter.read(reader).getValue().getAsString();
    assertEquals("hello", value2);
  }

  @Test
  public void testWriteReadNullValues() throws IOException {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<MyClassWithNullValues> adapter = factory.create(gson, new TypeToken<MyClassWithNullValues>() {});
    JsonWriter writer = new JsonWriter(System.out);
    JsonReader reader = new JsonReader(new StringReader("{}"));

    writer.beginObject();
    adapter.write(writer, null);
    writer.endObject();

    MyClassWithNullValues value = adapter.read(reader).getValue();
    assertNull(value);
  }

  @Test
  public void testWriteReadComplexObject() throws IOException {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<MyClassWithComplexObject> adapter = factory.create(gson, new TypeToken<MyClassWithComplexObject>() {});
    JsonWriter writer = new JsonWriter(System.out);
    JsonReader reader = new JsonReader(new StringReader("{" +
        "\"name\":\"John\", " +
        "\"age\":30, " +
        "\"address\":{" +
        "\"street\":\"Main St.\", " +
        "\"city\":\"Anytown\", " +
        "\"country\":\"USA\"" +
        "}" +
        "}"));

    writer.beginObject();
    adapter.write(writer, new MyClassWithComplexObject("John", 30, new MyAddress("Main St.", "Anytown", "USA")));
    writer.endObject();

    MyClassWithComplexObject value = adapter.read(reader).getValue();
    assertEquals("John", value.name);
    assertEquals(30, value.age);
    assertEquals("Main St.", value.address.street);
    assertEquals("Anytown", value.address.city);
    assertEquals("USA", value.address.country);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testWriteReadWithInvalidJson() throws IOException {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<MyClassWithInvalidJson> adapter = factory.create(gson, new TypeToken<MyClassWithInvalidJson>() {});
    JsonWriter writer = new JsonWriter(System.out);
    JsonReader reader = new JsonReader(new StringReader("{\"invalid\":[]}"));

    writer.beginObject();
    adapter.write(writer, new MyClassWithInvalidJson());
    writer.endObject();

    adapter.read(reader).getValue();
  }

  @Test
  public void testWriteField() throws IOException, IllegalAccessException {
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
    TypeAdapter<MyClassWithWriteField> adapter = factory.create(gson, new TypeToken<MyClassWithWriteField>() {});
    JsonWriter writer = new JsonWriter(System.out);
    MyClassWithWriteField value = new MyClassWithWriteField();
    value.writeField = true;

    adapter.writeField(value);
    writer.beginObject();
    adapter.write(writer, value);
    writer.endObject();

    JsonParser parser = new JsonParser();
    MyClassWithWriteField deserializedValue = adapter.read(parser.parse("{" +
        "\"name\":\"John\", " +
        "\"age\":30, " +
        "\"writeField\":true" +
        "}"));
    assertEquals(true, deserializedValue.writeField);
  }
}

class MyClassWithExcludedField {
  @SuppressWarnings("unused")
  private boolean excludedField;
  private String nonExcludedField;
}

class MyClassWithCustomAnnotations {
  @SerializedName("customField")
  @JsonAdapter(MyCustomJsonAdapter.class)
  private String customField;
  @SerializedName("customAlternate")
  private String customAlternate;
}

class MyClassWithBoundFields {
  private int boundField;
}

interface MyInterface {}

class MyClassWithNullValues {
  private Integer nullableInteger;
  private MyClassWithNullValues nestedNullValue;
}

class MyAddress {
  private String street;
  private String city;
  private String country;
}

class MyClassWithComplexObject {
  private String name;
  private int age;
  private MyAddress address;

  public MyClassWithComplexObject(String name, int age, MyAddress address) {
    this.name = name;
    this.age = age;
    this.address = address;
  }
}

class MyClassWithInvalidJson {
  // empty class
}

class MyClassWithWriteField {
  private boolean writeField;
}