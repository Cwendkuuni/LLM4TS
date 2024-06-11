import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_WhenGivenValidArguments_ReturnsTypeAdapter() {
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
    assertNotNull(typeAdapter);
  }
}