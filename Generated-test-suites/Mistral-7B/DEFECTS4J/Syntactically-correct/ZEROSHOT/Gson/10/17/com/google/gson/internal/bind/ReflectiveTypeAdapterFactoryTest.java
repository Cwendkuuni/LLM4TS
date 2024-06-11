import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

  private static final class TestClass {
    private int primitiveField;
    @SerializedName("customName")
    private String customNameField;
    @JsonAdapter(CustomJsonAdapter.class)
    private CustomClass customClassField;

    public TestClass(int primitiveField, String customNameField, CustomClass customClassField) {
      this.primitiveField = primitiveField;
      this.customNameField = customNameField;
      this.customClassField = customClassField;
    }

    public static class CustomClass {
      private List<String> listField;

      public CustomClass(List<String> listField) {
        this.listField = listField;
      }
    }
  }

  private static final class CustomJsonAdapter implements TypeAdapter<TestClass.CustomClass> {
    @Override public TestClass.CustomClass read(JsonReader reader) throws IOException {
      JsonObject jsonObject = reader.beginObject();
      List<String> list = new ArrayList<>();
      while (jsonObject.hasNext()) {
        String name = jsonObject.nextName();
        JsonElement jsonElement = jsonObject.get(name);
        list.add(jsonElement.getAsString());
      }
      jsonObject.endObject();
      return new TestClass.CustomClass(list);
    }

    @Override public void write(JsonWriter writer, TestClass.CustomClass value) throws IOException {
      writer.beginArray();
      for (String valueElement : value.listField) {
        writer.value(valueElement);
      }
      writer.endArray();
    }
  }

  @Before
  public void setup() {
    FieldNamingPolicy fieldNamingPolicy = mock(FieldNamingPolicy.class);
    Excluder excluder = mock(Excluder.class);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    when(fieldNamingPolicy.translateName(any(Field.class))).thenReturn("defaultName");

    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(fieldNamingPolicy)
        .setExcluder(excluder)
        .setConstructorConstructor(constructorConstructor)
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    when(constructorConstructor.get(eq(TypeToken.get(TestClass.class))))
        .thenReturn(new ObjectConstructor<TestClass>() {
          @Override public TestClass construct() {
            return new TestClass(0, null, null);
          }
        });

    TypeAdapterFactory reflectiveTypeAdapterFactoryMock = reflectiveTypeAdapterFactory;
    when(constructorConstructor.get(eq(TypeToken.getType(TestClass.CustomClass.class))))
        .thenReturn(new ObjectConstructor<TestClass.CustomClass>() {
          @Override public TestClass.CustomClass construct() {
            return new TestClass.CustomClass(new ArrayList<>());
          }
        });
    when(excluder.excludeClass(eq(TestClass.CustomClass.class), anyBoolean()))
        .thenReturn(false);
    when(excluder.excludeField(any(Field.class), anyBoolean()))
        .thenReturn(false);

    TypeAdapter<TestClass> testClassAdapter = gson.getAdapter(TypeToken.get(TestClass.class));
    TypeAdapter<TestClass.CustomClass> customClassAdapter = gson.getDelegateAdapter(
        TypeToken.getType(TestClass.CustomClass.class), reflectiveTypeAdapterFactoryMock);
    TypeAdapterFactory reflectiveTypeAdapterFactoryWithCustomAdapter = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder) {
      @Override public TypeAdapter<TestClass.CustomClass> create(Gson gson, TypeToken<TestClass.CustomClass> type) {
        return customClassAdapter;
      }
    };
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    TestClass testClass = new TestClass(1, "customName", new TestClass.CustomClass(List.of("element1", "element2")));
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("defaultName", 1);
    jsonObject.addProperty("customName", "customName");
    jsonObject.add("customClass", createJsonArrayForCustomClass(jsonObject, "element1", "element2"));

    String jsonString = jsonObject.toString();
    TypeAdapter<TestClass> testClassAdapter = gson.getAdapter(TypeToken.get(TestClass.class));
    TestClass deserializedTestClass = testClassAdapter.read(new JsonReader(new StringReader(jsonString)));

    assertEquals(testClass, deserializedTestClass);
  }

  private JsonArray createJsonArrayForCustomClass(JsonObject jsonObject, String... elements) {
    JsonArray jsonArray = jsonObject.beginArray();
    for (String element : elements) {
      jsonArray.value(element);
    }
    jsonObject.endArray();
    return jsonArray;
  }
}