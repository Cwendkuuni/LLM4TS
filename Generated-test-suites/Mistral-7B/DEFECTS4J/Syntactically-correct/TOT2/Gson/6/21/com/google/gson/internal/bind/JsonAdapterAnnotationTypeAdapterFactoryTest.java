import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testConstructor() {
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    // No need to assert anything here, we just want to verify the constructor call
  }
}