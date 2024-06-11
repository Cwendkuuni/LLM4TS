import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_NoJsonAdapterAnnotation_ReturnsNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> adapter = factory.create(new Gson(), targetType);

    assertNull(adapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_ReturnsTypeAdapterInstance() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    Class<String> stringClass = String.class;
    JsonAdapter jsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<String> stringToken = new TypeToken<String>() {};

    Class<MyStringAdapter> stringAdapterClass = MyStringAdapter.class;
    JsonAdapter myJsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<MyStringAdapter> stringAdapterToken = new TypeToken<MyStringAdapter>() {};

    TypeAdapter<String> stringAdapter = factory.create(new Gson(), stringToken);
    TypeAdapter<MyStringAdapter> myStringAdapter = factory.create(new Gson(), stringAdapterToken);

    assertNotNull(stringAdapter);
    assertNotNull(myStringAdapter);
    assertNotSame(stringAdapter, myStringAdapter);

    assertSame(String.class, stringAdapter.getType());
    assertSame(MyStringAdapter.class, myStringAdapter.getType());
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    Class<String> stringClass = String.class;
    JsonAdapter jsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<String> stringToken = new TypeToken<String>() {};

    Class<MyStringAdapter> stringAdapterClass = MyStringAdapter.class;
    JsonAdapter myJsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<MyStringAdapter> stringAdapterToken = new TypeToken<MyStringAdapter>() {};

    TypeAdapter<String> stringAdapter = factory.create(new Gson(), stringToken);
    TypeAdapter<MyStringAdapter> myStringAdapter = factory.create(new Gson(), stringAdapterToken);

    assertNotNull(stringAdapter);
    assertNotNull(myStringAdapter);
    assertNotSame(stringAdapter, myStringAdapter);

    assertSame(String.class, stringAdapter.getType());
    assertSame(MyStringAdapter.class, myStringAdapter.getType());

    assertSame(MyStringAdapter.class, stringAdapter.getClass().getComponentType());

    MyStringAdapter stringAdapterInstance = constructorConstructor.get(stringAdapterToken).construct();
    assertSame(stringAdapterInstance, stringAdapter.getDelegate());
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    Class<String> stringClass = String.class;
    JsonAdapter jsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<String> stringToken = new TypeToken<String>() {};

    Class<MyStringAdapterFactory> stringAdapterFactoryClass = MyStringAdapterFactory.class;
    JsonAdapter myJsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<MyStringAdapterFactory> stringAdapterToken = new TypeToken<MyStringAdapterFactory>() {};

    TypeAdapter<String> stringAdapter = factory.create(new Gson(), stringToken);
    TypeAdapter<MyStringAdapter> myStringAdapter = factory.create(new Gson(), stringAdapterToken);

    assertNotNull(stringAdapter);
    assertNotNull(myStringAdapter);
    assertNotSame(stringAdapter, myStringAdapter);

    assertSame(String.class, stringAdapter.getType());
    assertSame(MyStringAdapter.class, myStringAdapter.getType());

    assertSame(MyStringAdapter.class, stringAdapter.getClass().getComponentType().getRawType());

    MyStringAdapterFactory stringAdapterFactoryInstance = constructorConstructor.get(stringAdapterToken).construct();
    assertSame(stringAdapterFactoryInstance, stringAdapter.getDelegate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate_JsonAdapterAnnotation_InvalidValue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    Class<String> stringClass = String.class;
    JsonAdapter jsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
    TypeToken<String> stringToken = new TypeToken<String>() {};

    Class<Integer> intClass = Integer.class;
    JsonAdapter intJsonAdapterAnnotation = intClass.getAnnotation(JsonAdapter.class);
    TypeToken<Integer> intToken = new TypeToken<Integer>() {};

    TypeAdapter<String> stringAdapter = factory.create(new Gson(), stringToken);
    TypeAdapter<Integer> intAdapter = factory.create(new Gson(), intToken);

    fail("Expected IllegalArgumentException");
  }

  private static class MyStringAdapter implements TypeAdapter<String> {
    @Override
    public String read(JsonReader jsonReader) throws IOException {
      return jsonReader.nextString();
    }

    @Override
    public void write(JsonWriter jsonWriter, String value) throws IOException {
      jsonWriter.value(value);
    }
  }

  private static class MyStringAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      if (!targetType.getType().equals(String.class)) {
        throw new UnsupportedOperationException();
      }
      return new MyStringAdapter();
    }
  }
}