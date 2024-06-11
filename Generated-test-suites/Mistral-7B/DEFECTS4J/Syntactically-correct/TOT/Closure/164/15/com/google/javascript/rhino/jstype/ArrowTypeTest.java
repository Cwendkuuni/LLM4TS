import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createNumberNode;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;

public class ArrowTypeTest {
  private static ErrorReporter errorReporter = new ErrorReporter();
  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = createJSTypeRegistry();
  }

  // Test case for ArrowType constructor with null parameters and return type
  @org.junit.Test
  public void testArrowTypeWithNullParamsAndReturnType() {
    Node parameters = null;
    JSType returnType = JSTypeNative.VOID;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(JSTypeNative.FUNCTION, arrowType.getBaseType());
  }

  // Test case for ArrowType constructor with valid parameters and return type
  @org.junit.Test
  public void testArrowTypeWithValidParamsAndReturnType() {
    Node parameters = createNode(createVariable("x"), createVariable("y"));
    JSType returnType = JSTypeNative.NUMBER;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(JSTypeNative.FUNCTION, arrowType.getBaseType());
    assertEquals(2, arrowType.parameters.getChildCount());
    assertEquals("x", arrowType.parameters.getChild(0).getId().getName());
    assertEquals("y", arrowType.parameters.getChild(1).getId().getName());
    assertEquals(returnType, arrowType.returnType);
  }

  // Test case for ArrowType constructor with varargs
  @org.junit.Test
  public void testArrowTypeWithVarargs() {
    Node parameters = createNode(
        createVariable("x"),
        createNode(createVariable("y"), createNode(createNode("..."), null))
    );

    JSType returnType = JSTypeNative.VOID;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(JSTypeNative.FUNCTION, arrowType.getBaseType());
    assertEquals(2, arrowType.parameters.getChildCount());
    assertEquals("x", arrowType.parameters.getChild(0).getId().getName());
    assertEquals("y", arrowType.parameters.getChild(1).getId().getName());
    assertTrue(arrowType.parameters.getChild(1).isVarArgs());
  }

  // Test case for ArrowType constructor with empty parameters
  @org.junit.Test
  public void testArrowTypeWithEmptyParams() {
    Node parameters = Node.NULL;
    JSType returnType = JSTypeNative.NUMBER;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(JSTypeNative.FUNCTION, arrowType.getBaseType());
    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
  }

  // Test case for ArrowType constructor with return type inferred
  @org.junit.Test
  public void testArrowTypeWithReturnTypeInferred() {
    Node parameters = createNode(createVariable("x"), createVariable("y"));
    JSType returnType = null;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, true);

    assertEquals(JSTypeNative.FUNCTION, arrowType.getBaseType());
    assertEquals(2, arrowType.parameters.getChildCount());
    assertEquals("x", arrowType.parameters.getChild(0).getId().getName());
    assertEquals("y", arrowType.parameters.getChild(1).getId().getName());
    assertNull(arrowType.returnType);
  }

  // Test case for ArrowType isSubtype
  @org.junit.Test
  public void testArrowTypeIsSubtype() {
    Node params1 = createNode(createVariable("x"), createVariable("y"));
    Node params2 = createNode(createVariable("y"), createVariable("z"));
    Node params3 = Node.NULL;

    JSType returnType1 = JSTypeNative.NUMBER;
    JSType returnType2 = JSTypeNative.STRING;
    JSType returnType3 = null;

    ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, params2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, params3, returnType3);

    // arrowType1 is subtype of arrowType1
    assertTrue(arrowType1.isSubtype(arrowType1));

    // arrowType1 is not subtype of arrowType2
    assertFalse(arrowType1.isSubtype(arrowType2));

    // arrowType1 is not subtype of arrowType3 (different number of params)
    assertFalse(arrowType1.isSubtype(arrowType3));

    // arrowType2 is not subtype of arrowType1 (different param types)
    assertFalse(arrowType2.isSubtype(arrowType1));

    // arrowType1 is subtype of arrowType1 with inferred return type
    ArrowType arrowType1Inferred = new ArrowType(registry, params1, returnType1, true);
    assertTrue(arrowType1.isSubtype(arrowType1Inferred));

    // arrowType1 is subtype of arrowType2 with inferred return type
    ArrowType arrowType2Inferred = new ArrowType(registry, params2, returnType2, true);
    assertTrue(arrowType1.isSubtype(arrowType2Inferred));
  }

  // Test case for ArrowType hasEqualParameters
  @org.junit.Test
  public void testArrowTypeHasEqualParameters() {
    Node params1 = createNode(createVariable("x"), createVariable("y"));
    Node params2 = createNode(createVariable("y"), createVariable("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, null);
    ArrowType arrowType2 = new ArrowType(registry, params2, null);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));

    Node params3 = createNode(createVariable("x"), createVariable("x"));

    ArrowType arrowType3 = new ArrowType(registry, params3, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType3));
  }

  // Test case for ArrowType equals
  @org.junit.Test
  public void testArrowTypeEquals() {
    Node params1 = createNode(createVariable("x"), createVariable("y"));
    Node params2 = createNode(createVariable("y"), createVariable("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, null);
    ArrowType arrowType2 = new ArrowType(registry, params2, null);

    assertFalse(arrowType1.equals(arrowType2));

    Node params3 = createNode(createVariable("x"), createVariable("x"));

    ArrowType arrowType3 = new ArrowType(registry, params3, null);

    assertTrue(arrowType1.equals(arrowType3));
  }

  // Test case for ArrowType hashCode
  @org.junit.Test
  public void testArrowTypeHashCode() {
    Node params1 = createNode(createVariable("x"), createVariable("y"));
    Node params2 = createNode(createVariable("y"), createVariable("x"));

    ArrowType arrowType1 = new ArrowType(registry, params1, null);
    ArrowType arrowType2 = new ArrowType(registry, params2, null);

    assertNotSame(arrowType1.hashCode(), arrowType2.hashCode());

    Node params3 = createNode(createVariable("x"), createVariable("x"));

    ArrowType arrowType3 = new ArrowType(registry, params3, null);

    assertEquals(arrowType1.hashCode(), arrowType3.hashCode());
  }

  // Test case for ArrowType resolveInternal
  @org.junit.Test
  public void testArrowTypeResolveInternal() {
    Node params = createNode(createVariable("x"), createNumberNode(42));

    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.NUMBER);

    assertNotNull(arrowType.resolveInternal(errorReporter, null));
  }

  // Test case for ArrowType resolveInternal with unknown types
  @org.junit.Test
  public void testArrowTypeResolveInternalWithUnknownTypes() {
    Node params = createNode(createVariable("x"), Node.NULL);

    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.NUMBER);

    assertNotNull(arrowType.resolveInternal(errorReporter, null));

    JSType resolvedType = arrowType.returnType;

    assertFalse(resolvedType.isUnknownType());
  }

  // Test case for ArrowType resolveInternal with unknown parameters
  @org.junit.Test
  public void testArrowTypeResolveInternalWithUnknownParameters() {
    Node params = Node.NULL;

    ArrowType arrowType = new ArrowType(registry, params, JSTypeNative.NUMBER);

    assertNotNull(arrowType.resolveInternal(errorReporter, null));

    JSType resolvedType = arrowType.returnType;

    assertFalse(resolvedType.isUnknownType());

    for (Node paramNode = arrowType.parameters.getFirstChild();
         paramNode != null; paramNode = paramNode.getNext()) {
      JSType paramType = paramNode.getJSType();

      assertTrue(paramType.hasUnknownParamsOrReturn());
    }
  }

  // Test case for ArrowType resolveInternal with invalid return type
  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeResolveInternalWithInvalidReturnType() {
    Node params = createNode(createVariable("x"), createVariable("y"));

    ArrowType arrowType = new ArrowType(registry, params, null);

    arrowType.resolveInternal(errorReporter, null);
  }
}