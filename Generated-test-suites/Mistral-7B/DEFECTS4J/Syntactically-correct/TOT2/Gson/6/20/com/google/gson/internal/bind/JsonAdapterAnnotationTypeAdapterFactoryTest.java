import static org.junit.Assert.assertNotNull;
import com.google.gson.Gson;
import com.google.gson.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreateMethod() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor();
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> adapter = factory.create(gson, targetType);

    assertNotNull(adapter);
  }
}