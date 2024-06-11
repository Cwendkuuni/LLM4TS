import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;

  @Before
  public void setup() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    FieldNamingStrategy fieldNamingPolicy = new FieldNameAnnotationStrategy();
    Excluder excluder = new Excluder();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    gson = new GsonBuilder().setFieldNamingStrategy(fieldNamingPolicy).create();
  }

  @Test
  public void testSerializingAndDeserializingSimpleClass() {
    // given
    class TestClass {
      private int id;
      private String name;

      public TestClass(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    // when
    TestClass testObject = new TestClass(1, "TestObject");
    String json = gson.toJson(testObject, TestClass.class);
    TestClass deserializedObject = gson.fromJson(json, TestClass.class);

    // then
    assertEquals(testObject.id, deserializedObject.id);
    assertEquals(testObject.name, deserializedObject.name);
  }

  @Test
  public void testSerializingAndDeserializingClassWithAnnotations() {
    // given
    class AnnotatedTestClass {
      @SerializedName("id")
      private int id;
      private String name;

      public AnnotatedTestClass(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    // when
    AnnotatedTestClass annotatedTestObject = new AnnotatedTestClass(2, "AnnotatedTestObject");
    String json = gson.toJson(annotatedTestObject, AnnotatedTestClass.class);
    AnnotatedTestClass deserializedObject = gson.fromJson(json, AnnotatedTestClass.class);

    // then
    assertEquals(annotatedTestObject.id, deserializedObject.id);
    assertEquals(annotatedTestObject.name, deserializedObject.name);
  }

  @Test
  public void testExcludingFields() {
    // given
    class TestClass {
      private int id;
      private boolean excludeMe;

      public TestClass(int id, boolean excludeMe) {
        this.id = id;
        this.excludeMe = excludeMe;
      }

      @JsonAdapter(ExcludingAdapter.class)
      public boolean getExcludeMe() {
        return excludeMe;
      }
    }

    class ExcludingAdapter extends TypeAdapter<Boolean> {
      @Override
      public Boolean read(JsonReader in) throws IOException {
        return null;
      }

      @Override
      public void write(JsonWriter out, Boolean value) throws IOException {
        out.skipValue();
      }
    }

    // when
    TestClass testObject = new TestClass(1, true);
    String json = gson.toJson(testObject, TestClass.class);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonElement excludeMeElement = jsonElement.getAsJsonObject().get("excludeMe");

    // then
    assertNotNull(excludeMeElement);
    assertEquals(JsonElement.NULL, excludeMeElement);

    TestClass deserializedObject = gson.fromJson(json, TestClass.class);
    assertNotNull(deserializedObject.excludeMe);
    assertFalse(deserializedObject.excludeMe);
  }
}