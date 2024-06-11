import static org.junit.Assert.assertNotNull;
import com.google.gson.Gson;
import com.google.gson.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreateMethod() {
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {
    };
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    assertNotNull(typeAdapter);
  }
}