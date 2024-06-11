import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithValidParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParameters(
        registry.createParameter("name", JSType.STRING),
        registry.createParameter("age", JSType.NUMBER)
    );
    JSType returnType = registry.createNativeType(JSType.NUMBER);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubtype(arrowType));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, null)));

    ArrowType arrowType1 = new ArrowType(registry, null, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, null);
    assertFalse(arrowType1.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, parameters, JSType.BOOLEAN);
    ArrowType arrowType4 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType3.isSubtype(arrowType4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, JSType.NUMBER);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParameters(registry.createParameter("name", JSType.STRING));
    ArrowType arrowType = new ArrowType(registry, parameters, null);
  }
}