import com.google.gson.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ReflectiveTypeAdapterFactory factory;

  @Before
  public void setup() {
    gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .setExcluder(new Excluder())
      .create();
    factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(gson),
        new FieldNamingPolicy.Standard(),
        new Excluder()
    );
  }

  @Test
  public void testAdaptingSimpleClass() {
    class TestClass {
      private int field;

      @JsonAdapter(AdaptedIntTypeAdapter.class)
      public int getField() {
        return field;
      }

      public void setField(int field) {
        this.field = field;
      }
    }

    TestClass testClass = new TestClass();
    testClass.setField(42);

    String json = gson.toJson(testClass);
    TestClass deserialized = gson.fromJson(json, TestClass.class);

    assertEquals(42, deserialized.getField());
  }

  @Test
  public void testAdaptingClassWithCustomFieldNames() {
    class TestClass {
      @SerializedName("value")
      private int field;

      @JsonAdapter(AdaptedIntTypeAdapter.class)
      public int getValue() {
        return field;
      }

      public void setValue(int value) {
        this.field = value;
      }
    }

    TestClass testClass = new TestClass();
    testClass.setValue(42);

    String json = gson.toJson(testClass);
    TestClass deserialized = gson.fromJson(json, TestClass.class);

    assertEquals(42, deserialized.getValue());
  }

  @Test
  public void testAdaptingInterface() {
    interface TestInterface {
      void method();
    }

    class TestClass implements TestInterface {
      @Override
      public void method() {
      }
    }

    TestClass testClass = new TestClass();

    String json = gson.toJson(testClass);
    TestInterface deserialized = gson.fromJson(json, TestInterface.class);

    deserialized.method();
  }

  @Test(expected = JsonSyntaxException.class)
  public void testExcludingField() {
    class TestClass {
      @Exclude
      private int excludedField;
    }

    TestClass testClass = new TestClass();
    testClass.setExcludedField(42);

    String json = gson.toJson(testClass);
    TestClass deserialized = gson.fromJson(json, TestClass.class);

    fail("Expected JsonSyntaxException");
  }
}

class AdaptedIntTypeAdapter extends TypeAdapter<Integer> {
  @Override public Integer read(JsonReader in) throws IOException {
    return in.nextInt();
  }

  @Override public void write(JsonWriter out, Integer value) throws IOException {
    out.value(value);
  }
}