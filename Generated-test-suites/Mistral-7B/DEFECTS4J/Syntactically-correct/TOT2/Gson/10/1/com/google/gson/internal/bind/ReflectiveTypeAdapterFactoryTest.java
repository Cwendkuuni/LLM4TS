import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder().create();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field f) {
            return f.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return false;
          }
        }
    );

    // When
    TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

    // Then
    TestClass testObject = new TestClass("Test", 42, new TestNestedClass("Nested", 12));
    String json = gson.toJson(testObject);
    TestClass deserializedObject = gson.fromJson(json, TestClass.class);

    assertEquals(testObject, deserializedObject);

    // Test edge cases and exception handling

    // Test null input
    TestClass nullInput = null;
    try {
      adapter.write(new JsonWriter(System.out), nullInput);
      fail("Expected JsonIOException");
    } catch (JsonIOException e) {
      // Expected
    }

    try {
      TestClass deserializedNullInput = adapter.read(new JsonReader(new StringReader("")));
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Test empty JSON object
    TestClass emptyJsonObject = gson.fromJson("{}", TestClass.class);
    assertNull(emptyJsonObject);

    // Test JSON object with different field order
    String jsonDifferentOrder = "{\"field2\":42,\"field1\":\"Test\"}";
    TestClass deserializedDifferentOrder = gson.fromJson(jsonDifferentOrder, TestClass.class);
    assertEquals(new TestClass("Test", 42, null), deserializedDifferentOrder);

    // Test JSON object with missing field
    String jsonMissingField = "{\"field1\":\"Test\",\"field3\":42}";
    TestClass deserializedMissingField = gson.fromJson(jsonMissingField, TestClass.class);
    assertEquals(new TestClass("Test", null, null), deserializedMissingField);

    // Test JSON object with invalid field
    String jsonInvalidField = "{\"invalidField\":42}";
    try {
      TestClass deserializedInvalidField = gson.fromJson(jsonInvalidField, TestClass.class);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Test JSON object with different field types
    String jsonDifferentTypes = "{\"field1\":\"Test\",\"field2\":4.2}";
    try {
      TestClass deserializedDifferentTypes = gson.fromJson(jsonDifferentTypes, TestClass.class);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    // Test JSON object with circular references
    TestClass circularReference = new TestClass("Circular", new TestClass("Circular"));
    json = gson.toJson(circularReference);
    TestClass deserializedCircularReference = gson.fromJson(json, TestClass.class);
    assertEquals(circularReference, deserializedCircularReference);
  }

  static class TestClass {
    String name;
    Integer number;
    TestNestedClass nested;

    TestClass(String name, Integer number, TestNestedClass nested) {
      this.name = name;
      this.number = number;
      this.nested = nested;
    }
  }

  static class TestNestedClass {
    String name;
    Integer number;

    TestNestedClass(String name, Integer number) {
      this.name = name;
      this.number = number;
    }
  }
}