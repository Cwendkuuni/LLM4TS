import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Valid construction
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(JSTypeNative.VOID));
    assertEquals(JSTypeNative.ARROW, arrowType1.getTypeCode());
    assertNull(arrowType1.parameters);
    assertNull(arrowType1.returnType);
    assertFalse(arrowType1.returnTypeInferred);

    // Construction with parameters
    Node paramsNode = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, paramsNode, getNativeType(JSTypeNative.NUMBER));
    assertEquals(JSTypeNative.ARROW, arrowType2.getTypeCode());
    assertEquals(paramsNode, arrowType2.parameters);
    assertEquals(JSTypeNative.NUMBER, arrowType2.returnType.getTypeCode());
    assertFalse(arrowType2.returnTypeInferred);

    // Construction with return type
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(JSTypeNative.STRING), true);
    assertEquals(JSTypeNative.ARROW, arrowType3.getTypeCode());
    assertNull(arrowType3.parameters);
    assertEquals(getNativeType(JSTypeNative.STRING), arrowType3.returnType);
    assertTrue(arrowType3.returnTypeInferred);

    // Construction with invalid arguments
    assertThrows(NullPointerException.class, () -> new ArrowType(registry, null, null));
    assertThrows(NullPointerException.class, () -> new ArrowType(registry, null, getNativeType(JSTypeNative.VOID), true));
    assertThrows(NullPointerException.class, () -> new ArrowType(registry, getNativeType(JSTypeNative.NUMBER), null));
    assertThrows(NullPointerException.class, () -> new ArrowType(registry, getNativeType(JSTypeNative.NUMBER), null, true));
  }

  // ... other test cases for other methods ...
}