import static org.junit.Assert.*;

import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = registry.createBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);

    Node invalidParameters = Node.newList();
    try {
      ArrowType arrowTypeInvalid = new ArrowType(registry, invalidParameters, returnType);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }

    Node invalidReturnType = null;
    try {
      ArrowType arrowTypeInvalidReturn = new ArrowType(registry, parameters, invalidReturnType);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }

    Node invalidReturnTypeInferred = Node.newBoolean(true);
    ArrowType arrowTypeInvalidReturnInferred = new ArrowType(registry, parameters, returnType, true, invalidReturnTypeInferred);
    assertNull(arrowTypeInvalidReturnInferred.returnType);
  }

  // Test other methods like isSubtype, hasEqualParameters, etc. here
}