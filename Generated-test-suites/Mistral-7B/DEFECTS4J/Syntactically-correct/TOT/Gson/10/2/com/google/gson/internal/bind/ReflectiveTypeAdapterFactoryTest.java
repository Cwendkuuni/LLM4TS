import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  private static final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .excludeFieldsWithoutExposeAnnotation()
      .create();

  private ReflectiveTypeAdapterFactory factory;

  @Before
  public void setup() {
    factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson),
        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        });
  }

  @Test
  public void testBasicSerializationDeserialization() throws IOException {
    // Prepare data
    Map<String, String> data = new HashMap<>();
    data.put("key1", "value1");
    data.put("key2", "value2");

    // Serialize data to JSON
    String json = gson.toJson(data);

    // Deserialize JSON back to Map
    Map<String, String> result = gson.fromJson(json, Map.class);

    // Check if deserialized data matches original data
    Assert.assertEquals(data, result);
  }

  @Test
  public void testFieldWithJsonAdapterAnnotation() throws IOException {
    // Prepare data
    Map<String, MyNumber> data = new HashMap<>();
    data.put("key1", new MyNumber(123.456));
    data.put("key2", new MyNumber(789.012));

    // Serialize data to JSON
    String json = gson.toJson(data);

    // Deserialize JSON back to Map
    Map<String, MyNumber> result = gson.fromJson(json, Map.class);

    // Check if deserialized data matches original data
    Assert.assertEquals(data, result);

    // Check if JsonAdapter was used for deserialization
    for (Object value : result.values()) {
      MyNumber number = (MyNumber) value;
      Assert.assertEquals(number.value, number._jsonValue);
    }
  }

  @Test
  public void testFieldWithSerializedNameAnnotation() throws IOException {
    // Prepare data
    Map<String, MyObject> data = new HashMap<>();
    MyObject obj1 = new MyObject("name1", "value1");
    MyObject obj2 = new MyObject("name2", "value2");
    data.put("name1", obj1);
    data.put("name2", obj2);

    // Serialize data to JSON
    String json = gson.toJson(data);

    // Deserialize JSON back to Map
    Map<String, MyObject> result = gson.fromJson(json, Map.class);

    // Check if deserialized data matches original data
    Assert.assertEquals(data, result);

    // Check field names in deserialized objects
    for (Object value : result.values()) {
      MyObject obj = (MyObject) value;
      Assert.assertEquals(obj.name, obj.serialName);
    }
  }

  @Test
  public void testIgnoringFieldsWithExcluder() throws IOException {
    // Prepare data
    MyExcludableData data = new MyExcludableData(1, "value1", "ignored");

    // Serialize data to JSON
    String json = gson.toJson(data);

    // Deserialize JSON back to MyExcludableData
    MyExcludableData result = gson.fromJson(json, MyExcludableData.class);

    // Check if ignored field was excluded from deserialization
    Assert.assertEquals(data.value, result.value);
    Assert.assertNull(result.ignored);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testInvalidJson() throws IOException {
    // Prepare data
    String json = "{\"key\":1}"; // missing value for key

    // Deserialize JSON back to Map (should throw JsonSyntaxException)
    Map<String, Object> result = gson.fromJson(json, Map.class);
  }

  static class MyNumber {
    double value;
    @JsonAdapter(NumberAdapter.class) double _jsonValue;

    MyNumber(double value) {
      this.value = value;
      this._jsonValue = value;
    }
  }

  static class MyObject {
    String name;
    @SerializedName("serialName") String serialName;

    MyObject(String name, String serialName) {
      this.name = name;
      this.serialName = serialName;
    }
  }

  static class MyExcludableData {
    int id;
    String value;
    @Exclude String ignored;

    MyExcludableData(int id, String value, String ignored) {
      this.id = id;
      this.value = value;
      this.ignored = ignored;
    }
  }

  static class NumberAdapter extends TypeAdapter<Number> {
    @Override public Number read(JsonReader in) throws IOException {
      return in.nextInt();
    }

    @Override public void write(JsonWriter out, Number value) throws IOException {
      out.value(value.intValue());
    }
  }
}