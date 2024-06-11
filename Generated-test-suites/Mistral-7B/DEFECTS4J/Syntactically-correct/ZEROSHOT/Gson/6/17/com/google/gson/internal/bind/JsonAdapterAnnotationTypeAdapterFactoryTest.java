import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflection.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory typeAdapterFactory;

  @Before
  public void setup() {
    this.gson = new GsonBuilder().setPrePreProcessor(new TypeAdapterFactory() {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        return null;
      }
    }).create();
    this.constructorConstructor = gson.getDelegateAdapterFactories().get(0)
        .getFactory(ConstructorConstructor.class);
    this.typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testNullInput() {
    assertNull(typeAdapterFactory.create(gson, null));
  }

  @Test
  public void testNoJsonAdapterAnnotation() {
    assertNull(typeAdapterFactory.create(gson, new TypeToken<Integer>() {}));
  }

  @Test
  public void testTypeAdapter() {
    class TestTypeAdapter implements TypeAdapter<String> {
      @Override
      public String read(JsonReader in) throws IOException {
        return null;
      }

      @Override
      public void write(JsonWriter out, String value) throws IOException {

      }
    }

    class TestType {
      @JsonAdapter(TestTypeAdapter.class)
      private String test;
    }

    assertEquals(TestTypeAdapter.class, typeAdapterFactory.create(gson, new TypeToken<TestType>() {}).getClass());
  }

  @Test
  public void testTypeAdapterFactory() {
    class TestTypeAdapterFactory implements TypeAdapterFactory {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        return null;
      }
    }

    class TestType {
      @JsonAdapter(TestTypeAdapterFactory.class)
      private String test;
    }

    TypeAdapterFactory testTypeAdapterFactory = new TestTypeAdapterFactory();
    assertEquals(TestTypeAdapterFactory.class, typeAdapterFactory.create(gson, new TypeToken<TestType>() {})
        .getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonAdapterAnnotationValue() {
    class TestType {
      @JsonAdapter(Integer.class)
      private String test;
    }
    typeAdapterFactory.create(gson, new TypeToken<TestType>() {});
  }
}