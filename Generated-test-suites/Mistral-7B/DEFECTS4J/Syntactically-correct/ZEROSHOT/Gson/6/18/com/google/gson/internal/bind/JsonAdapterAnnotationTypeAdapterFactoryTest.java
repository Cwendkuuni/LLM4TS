import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private Gson gson;
  private TypeToken<String> stringTypeToken;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    stringTypeToken = new TypeToken<String>() {};
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void shouldReturnNullWhenNoJsonAdapterAnnotationPresent() {
    assertNull(factory.create(gson, new TypeToken<Integer>() {}));
  }

  @Test
  public void shouldCreateTypeAdapterWhenJsonAdapterClassIsProvided() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(StringToIntAdapter.class);
    when(constructorConstructor.get(TypeToken.get(StringToIntAdapter.class))
        .construct()).thenReturn(new StringToIntAdapter());

    TypeAdapter<Integer> adapter = factory.create(gson, stringTypeToken);
    assertInstanceOf(StringToIntAdapter.class, adapter);
  }

  @Test
  public void shouldCreateTypeAdapterFactoryWhenJsonAdapterFactoryClassIsProvided() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(StringToIntAdapterFactory.class);
    TypeAdapterFactory adapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))
        .construct()).thenReturn(adapterFactory);
    when(adapterFactory.create(gson, stringTypeToken)).thenReturn(new StringToIntAdapter());

    TypeAdapter<Integer> adapter = factory.create(gson, stringTypeToken);
    assertInstanceOf(StringToIntAdapter.class, adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenJsonAdapterIsNeitherTypeAdapterNorTypeAdapterFactory() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn("not a valid type adapter or factory");

    factory.create(gson, stringTypeToken);
  }

  @Test
  public void shouldCreateNullSafeTypeAdapter() {
    JsonAdapter jsonAdapterAnnotation = mock(JsonAdapter.class);
    when(jsonAdapterAnnotation.value()).thenReturn(new NullSafeStringToIntAdapter());
    when(constructorConstructor.get(TypeToken.get(NullSafeStringToIntAdapter.class))
        .construct()).thenReturn(new NullSafeStringToIntAdapter());

    TypeAdapter<Integer> adapter = factory.create(gson, stringTypeToken);
    assertInstanceOf(NullSafeStringToIntAdapter.class, adapter);

    verify(adapter).nullSafe();
  }
}

class StringToIntAdapter implements TypeAdapter<Integer> {
  @Override
  public Integer read(JsonReader in) throws IOException {
    return in.nextInt();
  }

  @Override
  public void write(JsonWriter out, Integer value) throws IOException {
    out.value(value);
  }
}

class StringToIntAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    return new StringToIntAdapter();
  }
}

class NullSafeStringToIntAdapter extends StringToIntAdapter {
  @Override
  public Integer nullSafeRead(JsonReader in) throws IOException {
    Integer value = in.nextInt();
    if (value == null) {
      throw new JsonParseException("Null found where non-null int was expected.");
    }
    return value;
  }

  @Override
  public void nullSafeWrite(JsonWriter out, Integer value) throws IOException {
    if (value == null) {
      out.value("null");
    } else {
      out.value(value);
    }
  }
}