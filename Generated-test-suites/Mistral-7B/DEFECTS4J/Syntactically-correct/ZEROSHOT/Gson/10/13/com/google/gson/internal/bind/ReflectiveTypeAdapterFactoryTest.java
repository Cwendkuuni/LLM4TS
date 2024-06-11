import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Test constructor
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
    Excluder excluder = new Excluder();
    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

    // Test excludeField method
    Class testClass = TestClass.class;
    Field testField = testClass.getDeclaredField("testField");
    assertTrue(reflectiveTypeAdapterFactory.excludeField(testField, false)); // Deserialize
    assertFalse(reflectiveTypeAdapterFactory.excludeField(testField, true)); // Serialize

    // Test getFieldNames method
    Field testFieldWithAnnotation = testClass.getDeclaredField("testFieldWithAnnotation");
    List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(testFieldWithAnnotation);
    assertEquals(2, fieldNames.size());
    assertEquals("testFieldWithAnnotation_alternate", fieldNames.get(1));

    // Test create method
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    TypeAdapter<TestClass> adapter = reflectiveTypeAdapterFactory.create(new Gson(), typeToken);
    assertNotNull(adapter);

    // Test getBoundFields method
    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapter.getBoundFields();
    assertEquals(4, boundFields.size());

    // Test Adapter class
    ReflectiveTypeAdapterFactory.BoundField boundField = boundFields.get("testField");
    assertNotNull(boundField);
    assertEquals("testField", boundField.name);

    // Test write and read methods
    TestClass testInstance = new TestClass();
    testInstance.testField = 123;
    testInstance.testFieldWithAnnotation = new TestFieldWithAnnotation("test");
    testInstance.testFieldWithAnnotation2 = new TestFieldWithAnnotation("test2");

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("testField", 456);
    jsonObject.add("testFieldWithAnnotation", createJsonElementForTestFieldWithAnnotation("test3"));
    jsonObject.add("testFieldWithAnnotation2", createJsonElementForTestFieldWithAnnotation("test4"));

    try {
      JsonReader jsonReader = new JsonReader(new StringReader(jsonObject.toString()));
      TestClass deserializedInstance = adapter.read(jsonReader);
      assertEquals(deserializedInstance.testField, 456);
      assertEquals(deserializedInstance.testFieldWithAnnotation.value, "test3");
      assertEquals(deserializedInstance.testFieldWithAnnotation2.value, "test4");
    } catch (IOException e) {
      fail();
    }

    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, testInstance);
    String jsonString = jsonWriter.toString();
    JsonObject jsonObject2 = JsonParser.parseString(jsonString).getAsJsonObject();
    assertEquals(jsonObject2.get("testField").getAsInt(), 123);
    assertEquals(jsonObject2.getAsJsonObject("testFieldWithAnnotation").get("value").getAsString(), "test");
    assertEquals(jsonObject2.getAsJsonObject("testFieldWithAnnotation2").get("value").getAsString(), "test2");

    // Test writeField method
    assertEquals(testInstance.hashCode(), adapter.writeField(testInstance).hashCode());
    assertFalse(adapter.writeField(testInstance2));

  }

  private static JsonElement createJsonElementForTestFieldWithAnnotation(String value) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("value", value);
    return jsonObject;
  }

  static class TestClass {
    int testField = 0;
    TestFieldWithAnnotation testFieldWithAnnotation;
    TestFieldWithAnnotation testFieldWithAnnotation2;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestClass testClass = (TestClass) o;
      return testField == testClass.testField &&
          Objects.equals(testFieldWithAnnotation, testClass.testFieldWithAnnotation) &&
          Objects.equals(testFieldWithAnnotation2, testClass.testFieldWithAnnotation2);
    }

    @Override
    public int hashCode() {
      return Objects.hash(testField, testFieldWithAnnotation, testFieldWithAnnotation2);
    }
  }

  static class TestFieldWithAnnotation {
    @SerializedName("testFieldWithAnnotation_alternate")
    String value;

    TestFieldWithAnnotation(String value) {
      this.value = value;
    }
  }

}