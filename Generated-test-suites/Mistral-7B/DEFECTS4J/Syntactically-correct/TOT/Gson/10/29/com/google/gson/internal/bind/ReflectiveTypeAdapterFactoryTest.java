import com.google.gson.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() {
    // Given
    Class testClass = MyTestClass.class;
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy.Standard(), new Excluder());

    // When
    TypeAdapter<MyTestClass> adapter = adapterFactory.create(gson, new TypeToken<MyTestClass>() {});
    MyTestClass original = new MyTestClass("test1", "test2", 1, 2.0f);
    String json = gson.toJson(original);
    MyTestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
    MyTestClass readFromJson = adapter.read(new JsonReader(new StringReader(json)));

    // Then
    assertEquals(original, deserialized);
    assertEquals(original, readFromJson);

    // Edge cases
    MyTestClass nullObject = new MyTestClass(null, null, null, null);
    json = gson.toJson(nullObject);
    assertNull(adapter.read(new JsonReader(new StringReader(json))));

    MyTestClass emptyObject = new MyTestClass();
    json = gson.toJson(emptyObject);
    MyTestClass emptyDeserialized = adapter.read(new JsonReader(new StringReader(json)));
    assertNotNull(emptyDeserialized);
    assertEquals(0, emptyDeserialized.getInt1());
    assertEquals(0.0, emptyDeserialized.getFloat2(), 0.01);

    // Error cases
    JsonReader reader = new JsonReader(new StringReader("{"));
    try {
      adapter.read(reader);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }

    reader = new JsonReader(new StringReader("{test: 1}"));
    try {
      adapter.read(reader);
      fail("Expected JsonMappingException");
    } catch (JsonMappingException e) {
      // Expected
    }
  }

  static class MyTestClass {
    String str1;
    String str2;
    int int1;
    float float2;

    MyTestClass(String str1, String str2, int int1, float float2) {
      this.str1 = str1;
      this.str2 = str2;
      this.int1 = int1;
      this.float2 = float2;
    }

    MyTestClass() {
      // default constructor
    }
  }
}