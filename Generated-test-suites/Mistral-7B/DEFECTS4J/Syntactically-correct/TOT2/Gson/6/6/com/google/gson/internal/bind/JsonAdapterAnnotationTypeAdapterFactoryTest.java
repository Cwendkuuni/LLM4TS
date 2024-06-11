import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreateTypeAdapter() {
    ConstructorConstructor constructorConstructor = null;
    Gson gson = new Gson();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    Assert.assertNull(adapter);
  }

  @Test
  public void testGetTypeAdapterWithNullConstructorConstructor() {
    ConstructorConstructor constructorConstructor = null;
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
    TypeAdapter<String> expectedAdapter = new StringAdapter();
    try {
      TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
      Assert.fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void testGetTypeAdapterWithNullGson() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = null;
    TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
    JsonAdapter annotation = new JsonAdapter(NumberTypeAdapterFactory.class);
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test(expected = NullPointerException.class)
  public void testGetTypeAdapterWithNullTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = null;
    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test(expected = NullPointerException.class)
  public void testGetTypeAdapterWithNullJsonAdapterAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
    JsonAdapter annotation = null;
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithInvalidAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<?> fieldType = new TypeToken<Object>() {};
    JsonAdapter annotation = new JsonAdapter("InvalidClass");
    JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
  }

  @Test
  public void testCreateTypeAdapterWithValidAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotation annotation = new JsonAdapter(NumberTypeAdapter.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    Assert.assertNotNull(adapter);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapter() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
    TypeAdapter<String> expectedAdapter = new StringAdapter();
    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    Assert.assertEquals(expectedAdapter, adapter);
  }

  @Test
  public void testGetTypeAdapterWithTypeAdapterFactory() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> fieldType = new TypeToken<Integer>() {};
    JsonAdapter annotation = new JsonAdapter(NumberTypeAdapterFactory.class);
    TypeAdapterFactory factory = new NumberTypeAdapterFactory();
    TypeAdapter<?> expectedAdapter = factory.create(gson, fieldType);
    TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    Assert.assertEquals(expectedAdapter, adapter);
  }
}