import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;

  @Before
  public void setup() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
    Excluder excluder = new Excluder();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    gson = new GsonBuilder().setFieldNamingPolicy(fieldNamingPolicy).create();
  }

  @Test
  public void testAdaptingSimpleClass() throws Exception {
    class TestClass {
      @SerializedName("testField")
      private int testField;

      public TestClass(int testField) {
        this.testField = testField;
      }
    }

    TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

    // valid serialization
    JsonElement json = gson.toJsonTree(new TestClass(42), new TypeToken<JsonElement>() {});
    assertEquals("{\"testField\":42}", json.getAsJsonObject().toString());

    // valid deserialization
    TestClass deserialized = adapter.read(JsonParser.parseString("{\"testField\":42}"));
    assertEquals(42, deserialized.testField);

    // null serialization and deserialization
    TestClass nullInstance = null;
    JsonElement nullJson = gson.toJsonTree(nullInstance, new TypeToken<JsonElement>() {});
    assertNull(adapter.read(JsonParser.parseString("null")));
    assertEquals("null", nullJson.getAsJsonObject().toString());

    // invalid serialization and deserialization
    TestClass invalidInstance = new TestClass(-1);
    try {
      adapter.write(null, invalidInstance);
      fail("Expected JsonWriteException");
    } catch (JsonWriteException e) {
      // expected
    }
    try {
      adapter.read(JsonParser.parseString("{\"testField\":-1}"));
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // expected
    }
  }

  @Test
  public void testAdaptingComplexClass() throws Exception {
    class NestedClass {
      private int testField;

      public NestedClass(int testField) {
        this.testField = testField;
      }
    }

    class TestClass {
      private NestedClass nestedClass;

      public TestClass(NestedClass nestedClass) {
        this.nestedClass = nestedClass;
      }
    }

    TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

    // valid serialization
    TestClass instance = new TestClass(new NestedClass(42));
    JsonElement json = gson.toJsonTree(instance, new TypeToken<JsonElement>() {});
    assertEquals("{\"nestedClass\":{\"testField\":42}}", json.getAsJsonObject().toString());

    // valid deserialization
    TestClass deserialized = adapter.read(JsonParser.parseString("{\"nestedClass\":{\"testField\":42}}"));
    NestedClass deserializedNested = deserialized.nestedClass;
    assertEquals(42, deserializedNested.testField);

    // null serialization and deserialization
    TestClass nullInstance = null;
    JsonElement nullJson = gson.toJsonTree(nullInstance, new TypeToken<JsonElement>() {});
    assertNull(adapter.read(JsonParser.parseString("null")));
    assertEquals("null", nullJson.getAsJsonObject().toString());

    // invalid serialization
    NestedClass invalidNested = new NestedClass(-1);
    TestClass invalidInstance = new TestClass(invalidNested);
    try {
      adapter.write(null, invalidInstance);
      fail("Expected JsonWriteException");
    } catch (JsonWriteException e) {
      // expected
    }

    // invalid deserialization
    JsonElement invalidJson = JsonParser.parseString("{\"nestedClass\":{\"testField\":-1}}");
    try {
      adapter.read(invalidJson);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // expected
    }
  }
}