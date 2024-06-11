import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    assertNotNull(adapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNullGson() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapter<String> adapter = factory.create(null, targetType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNullTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, null);
  }

  @Test
  public void testCreateWithoutJsonAdapterAnnotation() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<Integer> adapter = factory.create(gson, targetType);
    assertNull(adapter);
  }
}