import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  @Test
  public void testCreate() {
    Gson gson = new Gson();
    TypeToken<String> targetType = new TypeToken<String>() {};
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<String> adapter = factory.create(gson, targetType);
    Assert.assertNotNull(adapter);
  }
}