import org.junit.Assert;
import org.junit.Test;
import com.google.gson.*;
import static org.mockito.Mockito.*; // for mocking Gson instance

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  @Test
  public void testCreate_NullGson() {
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    Assert.assertNull(factory.create(null, targetType));
  }

  @Test
  public void testCreate_NullTypeToken() {
    Gson gson = mock(Gson.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    Assert.assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreate_NoJsonAdapterAnnotation() {
    Gson gson = mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(ConstructorConstructor.class));
    Assert.assertNull(factory.create(gson, targetType));
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapter() {
    Gson gson = mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
    TypeAdapter<String> expectedTypeAdapter = mock(TypeAdapter.class);
    Constructor<TypeAdapter<String>> constructor = mock(Constructor.class);
    when(constructor.newInstance(eq(gson), eq(targetType))).thenReturn(expectedTypeAdapter);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(eq(TypeToken.get(typeAdapterClass)))).thenReturn(constructor);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    verify(constructor).newInstance(eq(gson), eq(targetType));
    Assert.assertSame(expectedTypeAdapter, actualTypeAdapter);
  }

  @Test
  public void testCreate_JsonAdapterAnnotation_TypeAdapterFactory() {
    Gson gson = mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    Class<TypeAdapterFactory<String>> typeAdapterFactoryClass = TypeAdapterFactory.class;
    TypeAdapterFactory<String> expectedTypeAdapterFactory = mock(TypeAdapterFactory.class);
    Constructor<TypeAdapterFactory<String>> constructor = mock(Constructor.class);
    when(constructor.newInstance(eq(gson), eq(TypeToken.get(typeAdapterFactoryClass)))).thenReturn(expectedTypeAdapterFactory);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(eq(TypeToken.get(typeAdapterFactoryClass)))).thenReturn(constructor);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actualTypeAdapter = factory.create(gson, targetType);
    verify(constructor).newInstance(eq(gson), eq(targetType));
    Assert.assertSame(expectedTypeAdapterFactory, factory.get(gson, targetType));
  }
}