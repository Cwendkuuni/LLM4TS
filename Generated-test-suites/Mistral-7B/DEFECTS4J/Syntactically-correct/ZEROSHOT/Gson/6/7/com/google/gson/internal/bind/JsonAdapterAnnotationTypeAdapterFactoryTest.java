import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor();
    gson = new GsonBuilder().setPrePreProcessingReader(new JsonAdapterAnnotationProcessor()).create();
  }

  @Test
  public void testCreateNullTypeToken() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Assert.assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreateWithNoJsonAdapterAnnotation() {
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Assert.assertNull(factory.create(gson, new TypeToken<Integer>() {}));
  }

  @Test
  public void testCreateWithTypeAdapterValue() {
    int[] input = {1, 2, 3};
    TypeAdapter<int[]> expectedTypeAdapter = new ArrayTypeAdapter(Integer.class);
    JsonAdapter annotation = int[].class.getAnnotation(JsonAdapter.class);
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<int[]> actualTypeAdapter = factory.create(gson, new TypeToken<int[]>() {});
    Assert.assertEquals(expectedTypeAdapter, actualTypeAdapter);
    Assert.assertEquals(expectedTypeAdapter.toString(), actualTypeAdapter.toString());
    Assert.assertEquals(expectedTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))),
        actualTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))));
    Assert.assertEquals(expectedTypeAdapter.write(new JsonWriter(new StringWriter())).toString(),
        actualTypeAdapter.write(new JsonWriter(new StringWriter())).toString());

    Assert.assertEquals(expectedTypeAdapter.nullSafe().read(new JsonReader(new StringReader("null"))),
        actualTypeAdapter.nullSafe().read(new JsonReader(new StringReader("null"))));

    Assert.assertArrayEquals(input, expectedTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))));
    Assert.assertArrayEquals(input, actualTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))));
  }

  @Test
  public void testCreateWithTypeAdapterFactoryValue() {
    int[] input = {1, 2, 3};
    TypeAdapter<int[]> expectedTypeAdapter = new ArrayTypeAdapter(Integer.class);
    JsonAdapter annotation = int[].class.getAnnotation(JsonAdapter.class);
    TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<int[]> actualTypeAdapter =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
            .create(gson, new TypeToken<int[]>() {});
    Assert.assertEquals(expectedTypeAdapter, actualTypeAdapter);
    Assert.assertEquals(expectedTypeAdapter.toString(), actualTypeAdapter.toString());
    Assert.assertEquals(expectedTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))),
        actualTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))));
    Assert.assertEquals(expectedTypeAdapter.write(new JsonWriter(new StringWriter())).toString(),
        actualTypeAdapter.write(new JsonWriter(new StringWriter())).toString());

    Assert.assertEquals(expectedTypeAdapter.nullSafe().read(new JsonReader(new StringReader("null"))),
        actualTypeAdapter.nullSafe().read(new JsonReader(new StringReader("null"))));

    Assert.assertArrayEquals(input, expectedTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))));
    Assert.assertArrayEquals(input, actualTypeAdapter.read(new JsonReader(new StringReader("[1, 2, 3]"))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidJsonAdapterValue() {
    int[] input = {1, 2, 3};
    JsonAdapter annotation = int[].class.getAnnotation(JsonAdapter.class);
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factory.create(gson, new TypeToken<int[]>() {}).read(new JsonReader(new StringReader("invalidValue")));
  }
}

class ArrayTypeAdapter extends TypeAdapter<int[]> {
  public ArrayTypeAdapter(Class<Integer> elementType) {
    super(new TypeToken<int[]>() {}.where(new TypeToken<Integer>() {}).getType());
  }

  @Override
  public int[] read(JsonReader jsonReader) throws IOException {
    JsonElement jsonElement = jsonReader.nextTree();
    return jsonElement.isJsonArray() ? jsonElement.getAsJsonArray().mapTo(new IntJsonDeserializer()) : null;
  }

  @Override
  public void write(JsonWriter jsonWriter, int[] ints) throws IOException {
    jsonWriter.beginArray();
    for (int i : ints) {
      jsonWriter.value(i);
    }
    jsonWriter.endArray();
  }
}

class IntJsonDeserializer implements JsonDeserializer<Integer> {
  @Override
  public Integer deserialize(JsonElement jsonElement) throws TypeMismatchException {
    return jsonElement.getAsInt();
  }
}

class JsonAdapterProcessor implements PreProcessingReader {
  @Override
  public JsonReader read(InputStream inputStream) throws IOException {
    return new JsonReader(new InputStreamReader(inputStream));
  }
}