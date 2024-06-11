import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;

  @Before
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
  public void testCreateNullTypeAdapterWhenNoJsonAdapterAnnotationPresent() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testCreateTypeAdapterWithTypeAdapterAnnotation() throws IOException {
    String jsonInput = "{\"name\": \"John\"}";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.isLenient()).thenReturn(true);
    when(jsonReader.isLenient()).thenReturn(true);
    when(jsonReader.readString()).thenReturn("John");

    JsonWriter jsonWriter = mock(JsonWriter.class);
    when(jsonWriter.writeString(eq("name"))).thenAnswer(invocation -> {
      jsonWriter.writeString("John");
      return null;
    });

    TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(String.class)).construct())
        .thenReturn(stringTypeAdapter);

    Type type = String.class;
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return String.class;
      }
    };

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> actualTypeAdapter = (TypeAdapter<String>) factory.create(gson, targetType);

    verify(constructorConstructor).get(TypeToken.get(String.class));

    assertNotNull(actualTypeAdapter);
    assertEquals(String.class, actualTypeAdapter.getClass());
  }

  @Test
  public void testCreateTypeAdapterWithTypeAdapterFactoryAnnotation() throws IOException {
    String jsonInput = "{\"name\": \"John\"}";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.isLenient()).thenReturn(true);
    when(jsonReader.isLenient()).thenReturn(true);
    when(jsonReader.readString()).thenReturn("John");

    JsonWriter jsonWriter = mock(JsonWriter.class);
    when(jsonWriter.writeString(eq("name"))).thenAnswer(invocation -> {
      jsonWriter.writeString("John");
      return null;
    });

    TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(String.class)).construct())
        .thenReturn(stringTypeAdapter);

    TypeAdapterFactory stringAdapterFactory = mock(TypeAdapterFactory.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class))
        .construct())
        .thenReturn(stringAdapterFactory);
    when(stringAdapterFactory.create(gson, eq(String.class)))
        .thenReturn(stringTypeAdapter);

    Type type = String.class;
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapterFactory> value() {
        return StringAdapterFactory.class;
      }
    };

    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> actualTypeAdapter = (TypeAdapter<String>) factory.create(gson, targetType);

    verify(constructorConstructor).get(TypeToken.get(StringAdapterFactory.class));
    verify(stringAdapterFactory).create(gson, eq(String.class));

    assertNotNull(actualTypeAdapter);
    assertEquals(String.class, actualTypeAdapter.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTypeAdapterWithInvalidJsonAdapterAnnotation() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends TypeAdapter<?>> value() {
        return Integer.class;
      }
    };

    TypeAdapter<String> actualTypeAdapter = (TypeAdapter<String>) factory.create(gson, targetType);
  }
}

class StringAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return null;
  }
}