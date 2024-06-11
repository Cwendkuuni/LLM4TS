import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {
  @org.junit.Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(1); // single parameter
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(Integer.TYPE));

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(Integer.TYPE, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);

    ArrowType arrowTypeWithInferredReturnType = new ArrowType(registry, parameters, null, true);
    assertTrue(arrowTypeWithInferredReturnType.returnTypeInferred);
  }

  @org.junit.Test
  public void testIsSubtypeArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = new Node(1); // single parameter
    Node parameters2 = new Node(2); // two parameters
    ArrowType arrowType1 = new ArrowType(registry, parameters1, getNativeType(Integer.TYPE));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, getNativeType(Integer.TYPE));
    ArrowType arrowType3 = new ArrowType(registry, parameters1, getNativeType(String.TYPE));

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2)); // more parameters is a supertype
    assertTrue(arrowType1.isSubtype(arrowType3)); // different return type is a subtype
    assertFalse(arrowType1.isSubtype(new JSTypeRegistry().createNumberType()));
    assertFalse(arrowType1.isSubtype(new JSTypeRegistry().createFunctionType()));
  }

  // Add edge cases and exception handling test cases here
}