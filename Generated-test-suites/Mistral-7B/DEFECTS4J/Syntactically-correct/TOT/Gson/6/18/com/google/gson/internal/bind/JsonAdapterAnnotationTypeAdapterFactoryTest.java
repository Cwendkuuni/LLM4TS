import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private Gson gson = new Gson();

  @Test
  public void testJsonAdapterType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    TypeAdapter<String> stringTypeAdapter = factory.create(gson, new TypeToken<String>() {});
    assertNotNull(stringTypeAdapter);

    String jsonString = "\"Hello World!\"";
    String jsonObject = "{\"name\": \"John Doe\", \"age\": 30}";

    String result = gson.toJson(new String("Hello World!"), stringTypeAdapter.getType());
    assertEquals(jsonString, result);

    result = gson.toJson(new String("{\"name\": \"John Doe\", \"age\": 30}"), stringTypeAdapter.getType());
    fail("Expected JsonParseException: Unsupported type: com.google.gson.json.JsonParseException");
  }

  @Test
  public void testJsonAdapterFactoryType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    TypeAdapterFactory adapterFactory = factory.create(gson, new TypeToken<Integer>() {});
    assertNotNull(adapterFactory);

    TypeAdapter<Integer> integerTypeAdapter = adapterFactory.create(gson, new TypeToken<Integer>() {});
    assertNotNull(integerTypeAdapter);

    Integer numberOne = 1;
    String jsonNumberOne = "1";

    String result = gson.toJson(numberOne, integerTypeAdapter.getType());
    assertEquals(jsonNumberOne, result);

    result = gson.toJson(new Integer(42), integerTypeAdapter.getType());
    assertEquals("42", result);

    result = gson.toJson(new String("42"), integerTypeAdapter.getType());
    fail("Expected JsonParseException: Unsupported class: java.lang.String");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterInvalidValueType() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    TypeAdapterFactory adapterFactory = factory.create(gson, new TypeToken<String>() {});
    assertNotNull(adapterFactory);

    // JsonAdapterFactory should throw an IllegalArgumentException when given an invalid value type
    TypeAdapter<Boolean> booleanAdapter = adapterFactory.create(gson, new TypeToken<Boolean>() {});
  }
}