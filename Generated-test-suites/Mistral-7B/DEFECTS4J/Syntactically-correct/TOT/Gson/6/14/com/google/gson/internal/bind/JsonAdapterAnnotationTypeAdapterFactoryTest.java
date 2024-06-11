import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mockTypeAdapter());
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(mockTypeAdapterFactory());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    @JsonAdapter(MyTypeAdapter.class)
    class TestType {
    }

    TypeToken<TestType> testTypeToken = new TypeToken<TestType>() {
    };

    TypeAdapter<TestType> testTypeAdapter = factory.create(gson, testTypeToken);

    // Then
    assertNotNull(testTypeAdapter);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_NullJsonAdapterAnnotation() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // When
    @SuppressWarnings("unchecked")
    TypeToken<Object> testTypeToken = new TypeToken<Object>() {
    };

    // Then
    assertNull(factory.create(gson, testTypeToken));
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_JsonAdapterClassNotFound() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    @JsonAdapter(NonExistentTypeAdapter.class)
    class TestType {
    }

    TypeToken<TestType> testTypeToken = new TypeToken<TestType>() {
    };

    // When and Then
    assertNull(factory.create(gson, testTypeToken));
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_JsonAdapterValueIsTypeAdapter() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mockTypeAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    @JsonAdapter(TypeAdapter.class)
    class TestType {
    }

    TypeToken<TestType> testTypeToken = new TypeToken<TestType>() {
    };

    // When
    TypeAdapter<TestType> testTypeAdapter = factory.create(gson, testTypeToken);

    // Then
    assertNotNull(testTypeAdapter);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_JsonAdapterValueIsTypeAdapterFactory() {
    // Given
    Gson gson = new Gson();
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(mockTypeAdapterFactory());

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    @JsonAdapter(TypeAdapterFactory.class)
    class TestType {
    }

    TypeToken<TestType> testTypeToken = new TypeToken<TestType>() {
    };

    // When
    TypeAdapter<TestType> testTypeAdapter = factory.create(gson, testTypeToken);

    // Then
    assertNotNull(testTypeAdapter);
  }

  private TypeAdapter mockTypeAdapter() {
    return mock(TypeAdapter.class);
  }

  private TypeAdapterFactory mockTypeAdapterFactory() {
    return mock(TypeAdapterFactory.class);
  }
}