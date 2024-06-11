import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Class<?> testClass = TestClass.class;
    Gson gson = new GsonBuilder()
        .setFieldNamingStrategy(FieldNamingPolicy.SIMPLE_NAME)
        .setExcluder(Excluder.DEFAULT)
        .create();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor,
        FieldNamingPolicy.SIMPLE_NAME, Excluder.DEFAULT);

    // When
    TypeAdapter<TestClass> testClassAdapter = adapterFactory.create(gson, new TypeToken<TestClass>() {});

    // Then
    TestClass testObject = new TestClass("Test");
    String jsonString = gson.toJson(testObject);
    JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Test", jsonObject.get("name").getAsString());

    // Edge case: Test serialization and deserialization with empty constructor
    TestClass emptyConstructorTestObject = new TestClass();
    jsonString = gson.toJson(emptyConstructorTestObject);
    JsonElement emptyJsonElement = gson.fromJson(jsonString, JsonElement.class);
    assertNotNull(emptyJsonElement);
    TestClass deserializedEmptyConstructorObject = gson.fromJson(jsonString, TestClass.class);
    assertNotNull(deserializedEmptyConstructorObject);

    // Edge case: Test serialization and deserialization with custom constructor
    TestClass customConstructorTestObject = new TestClass("Custom");
    jsonString = gson.toJson(customConstructorTestObject);
    JsonElement customJsonElement = gson.fromJson(jsonString, JsonElement.class);
    assertNotNull(customJsonElement);
    TestClass deserializedCustomConstructorObject = gson.fromJson(jsonString, TestClass.class);
    assertEquals("Custom", deserializedCustomConstructorObject.getName());

    // Error case: Test serialization and deserialization with inaccessible fields
    TestClass inaccessibleFieldsTestObject = new TestClass("Inaccessible");
    inaccessibleFieldsTestObject.setPrivateField("Private");
    try {
      jsonString = gson.toJson(inaccessibleFieldsTestObject);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
    try {
      TestClass deserializedInaccessibleFieldsTestObject = gson.fromJson(jsonString, TestClass.class);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Error case: Test serialization and deserialization with non-matching types
    TestClassWithMismatchingType testObjectWithMismatchingType = new TestClassWithMismatchingType("Mismatching");
    testObjectWithMismatchingType.setStringField("String");
    testObjectWithMismatchingType.setIntegerField(1);

    jsonString = gson.toJson(testObjectWithMismatchingType);
    JsonElement mismatchingJsonElement = gson.fromJson(jsonString, JsonElement.class);
    assertNotNull(mismatchingJsonElement);

    TestClassWithMismatchingType deserializedTestObjectWithMismatchingType = gson.fromJson(jsonString, TestClass.class);
    assertNotNull(deserializedTestObjectWithMismatchingType);
    assertEquals("String", deserializedTestObjectWithMismatchingType.getStringField());
    assertEquals(1, deserializedTestObjectWithMismatchingType.getIntegerField());

    // Error case: Test serialization and deserialization with non-serializable fields
    TestClassWithNonSerializableField testObjectWithNonSerializableField = new TestClassWithNonSerializableField("NonSerializable");

    try {
      jsonString = gson.toJson(testObjectWithNonSerializableField);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    try {
      TestClassWithNonSerializableField deserializedTestObjectWithNonSerializableField = gson.fromJson(jsonString, TestClass.class);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Edge case: Test serialization and deserialization with null fields
    TestClassWithNullFields testObjectWithNullFields = new TestClassWithNullFields("NullFields");
    testObjectWithNullFields.setStringField(null);
    testObjectWithNullFields.setIntegerField(null);

    jsonString = gson.toJson(testObjectWithNullFields);
    JsonElement nullJsonElement = gson.fromJson(jsonString, JsonElement.class);
    assertNotNull(nullJsonElement);

    TestClassWithNullFields deserializedTestObjectWithNullFields = gson.fromJson(jsonString, TestClass.class);
    assertNotNull(deserializedTestObjectWithNullFields);
    assertEquals(null, deserializedTestObjectWithNullFields.getStringField());
    assertEquals(null, deserializedTestObjectWithNullFields.getIntegerField());
  }

  static class TestClass {
    private static final long serialVersionUID = 1L;
    private String name;

    TestClass() {
    }

    TestClass(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  static class TestClassWithMismatchingType {
    private String stringField;
    private int integerField;

    public String getStringField() {
      return stringField;
    }

    public void setStringField(String stringField) {
      this.stringField = stringField;
    }

    public int getIntegerField() {
      return integerField;
    }

    public void setIntegerField(int integerField) {
      this.integerField = integerField;
    }
  }

  static class TestClassWithNonSerializableField {
    private transient String nonSerializableField;

    public String getNonSerializableField() {
      return nonSerializableField;
    }

    public void setNonSerializableField(String nonSerializableField) {
      this.nonSerializableField = nonSerializableField;
    }
  }

  static class TestClassWithNullFields {
    private String stringField;
    private Integer integerField;

    public String getStringField() {
      return stringField;
    }

    public void setStringField(String stringField) {
      this.stringField = stringField;
    }

    public Integer getIntegerField() {
      return integerField;
    }

    public void setIntegerField(Integer integerField) {
      this.integerField = integerField;
    }
  }
}

class FieldNamingPolicy {
  public static final FieldNamingPolicy SIMPLE_NAME = new FieldNamingPolicy() {
    @Override public String translateName(Field field) {
      return field.getName();
    }
  };
}

class Excluder {
  public static final Excluder DEFAULT = new Excluder() {
    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
      return false;
    }

    @Override public boolean excludeField(Field field, boolean serialize) {
      return false;
    }
  };
}