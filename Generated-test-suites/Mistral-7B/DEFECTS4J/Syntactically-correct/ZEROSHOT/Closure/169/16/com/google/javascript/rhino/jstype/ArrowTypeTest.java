import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;

  @BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    Node parameters = new Node(JSTypeNative.VOID_TYPE, new Node[]{});
    Node returnType = getNativeType(INT_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Unexpected type", ARROW_TYPE, arrowType.getType());
    assertEquals("Unexpected return type", INT_TYPE, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    Node parameters = new Node(JSTypeNative.VOID_TYPE, new Node[]{});

    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals("Unexpected type", ARROW_TYPE, arrowType.getType());
    assertTrue("Unexpected value of returnTypeInferred", arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeCreationWithParameters() {
    Node parameters = new Node(
        JSTypeNative.createParametersWithVarArgs(INT_TYPE),
        new Node[]{new Node(STRING_TYPE), new Node(BOOLEAN_TYPE)});

    Node returnType = getNativeType(VOID_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Unexpected type", ARROW_TYPE, arrowType.getType());
    assertEquals("Unexpected number of parameters", 3, arrowType.getParameterCount());
    assertEquals("Unexpected parameter types", INT_TYPE, arrowType.getParameterType(0));
    assertEquals("Unexpected parameter types", STRING_TYPE, arrowType.getParameterType(1));
    assertEquals("Unexpected parameter types", BOOLEAN_TYPE, arrowType.getParameterType(2));
    assertEquals("Unexpected return type", VOID_TYPE, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeCreationWithVarArgs() {
    Node parameters = new Node(
        JSTypeNative.createParametersWithVarArgs(INT_TYPE),
        new Node[]{new Node(STRING_TYPE)});

    Node returnType = getNativeType(VOID_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Unexpected type", ARROW_TYPE, arrowType.getType());
    assertEquals("Unexpected number of parameters", 2, arrowType.getParameterCount());
    assertEquals("Unexpected parameter types", INT_TYPE, arrowType.getParameterType(0));
    assertEquals("Unexpected parameter types", STRING_TYPE.class, arrowType.getParameterType(1).getClass());
    assertEquals("Unexpected return type", VOID_TYPE, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeCreationWithOptionalParameter() {
    Node parameters = new Node(
        JSTypeNative.createParametersWithVarArgs(INT_TYPE),
        new Node[]{new Node(STRING_TYPE), new Node(OPTIONAL_STRING_TYPE)});

    Node returnType = getNativeType(VOID_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Unexpected type", ARROW_TYPE, arrowType.getType());
    assertEquals("Unexpected number of parameters", 3, arrowType.getParameterCount());
    assertEquals("Unexpected parameter types", INT_TYPE, arrowType.getParameterType(0));
    assertEquals("Unexpected parameter types", STRING_TYPE.class, arrowType.getParameterType(1).getClass());
    assertEquals("Unexpected return type", VOID_TYPE, arrowType.getReturnType());
    assertTrue("Unexpected value of optional parameter", arrowType.getParameter(1).isOptionalArg());
  }

  @Test
  public void testArrowTypeCreationWithMismatchedParameters() {
    Node parameters = new Node(
        JSTypeNative.createParametersWithVarArgs(INT_TYPE),
        new Node[]{new Node(STRING_TYPE), new Node(BOOLEAN_TYPE)});

    Node returnType = getNativeType(VOID_TYPE);

    try {
      new ArrowType(registry, parameters, returnType);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeCreationWithNullParameters() {
    Node returnType = getNativeType(VOID_TYPE);

    try {
      new ArrowType(registry, null, returnType);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeCreationWithNullReturnType() {
    Node parameters = new Node(JSTypeNative.VOID_TYPE, new Node[]{});

    try {
      new ArrowType(registry, parameters, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, null, getNativeType(INT_TYPE));
    ArrowType arrowType2 = new ArrowType(registry, null, getNativeType(VOID_TYPE));
    ArrowType arrowType3 = new ArrowType(registry, null, getNativeType(INT_TYPE));

    assertTrue("Unexpected subtype relationship: arrowType1 < arrowType3", arrowType1.isSubtype(arrowType3));
    assertFalse("Unexpected subtype relationship: arrowType1 < arrowType2", arrowType1.isSubtype(arrowType2));
    assertTrue("Unexpected subtype relationship: arrowType3 < arrowType3", arrowType3.isSubtype(arrowType3));
    assertTrue("Unexpected subtype relationship: arrowType2 < arrowType3 (due to covariance)", arrowType2.isSubtype(arrowType3));
    assertFalse("Unexpected subtype relationship: arrowType3 < arrowType2", arrowType3.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);
    ArrowType arrowType2 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);
    ArrowType arrowType3 = createArrowTypeWithParameters(INT_TYPE, STRING_TYPE);
    ArrowType arrowType4 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE, BOOLEAN_TYPE);

    assertTrue("Unexpected result of hasEqualParameters: arrowType1 and arrowType2", arrowType1.hasEqualParameters(arrowType2, false));
    assertFalse("Unexpected result of hasEqualParameters: arrowType1 and arrowType3", arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse("Unexpected result of hasEqualParameters: arrowType1 and arrowType4", arrowType1.hasEqualParameters(arrowType4, false));
    assertTrue("Unexpected result of hasEqualParameters: arrowType1 and arrowType1", arrowType1.hasEqualParameters(arrowType1, false));
    assertTrue("Unexpected result of hasEqualParameters: arrowType3 and arrowType4 (due to var_args)", arrowType3.hasEqualParameters(arrowType4, false));
    assertFalse("Unexpected result of hasEqualParameters: arrowType3 and arrowType4", arrowType3.hasEqualParameters(arrowType4, true));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);
    ArrowType arrowType2 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);
    ArrowType arrowType3 = createArrowTypeWithParameters(INT_TYPE, STRING_TYPE);
    ArrowType arrowType4 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE, BOOLEAN_TYPE);

    assertTrue("Unexpected result of checkArrowEquivalenceHelper: arrowType1 and arrowType2", arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertFalse("Unexpected result of checkArrowEquivalenceHelper: arrowType1 and arrowType3", arrowType1.checkArrowEquivalenceHelper(arrowType3, false));
    assertFalse("Unexpected result of checkArrowEquivalenceHelper: arrowType1 and arrowType4", arrowType1.checkArrowEquivalenceHelper(arrowType4, false));
    assertTrue("Unexpected result of checkArrowEquivalenceHelper: arrowType1 and arrowType1", arrowType1.checkArrowEquivalenceHelper(arrowType1, false));
    assertTrue("Unexpected result of checkArrowEquivalenceHelper: arrowType3 and arrowType4 (due to var_args)", arrowType3.checkArrowEquivalenceHelper(arrowType4, false));
    assertFalse("Unexpected result of checkArrowEquivalenceHelper: arrowType3 and arrowType4", arrowType3.checkArrowEquivalenceHelper(arrowType4, true));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType1 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);
    ArrowType arrowType2 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);
    ArrowType arrowType3 = createArrowTypeWithParameters(INT_TYPE, STRING_TYPE);
    ArrowType arrowType4 = createArrowTypeWithParameters(INT_TYPE, INT_TYPE, BOOLEAN_TYPE);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();
    int hashCode4 = arrowType4.hashCode();

    ArrowType arrowType5 = new ArrowType(registry, null, getNativeType(INT_TYPE));

    assertEquals("Unexpected hashcode for arrowType1", hashCode1, arrowType1.hashCode());
    assertEquals("Unexpected hashcode for arrowType2", hashCode1, arrowType2.hashCode());
    assertNotEquals("Unexpected hashcode for arrowType1 and arrowType3", hashCode1, hashCode3);
    assertNotEquals("Unexpected hashcode for arrowType1 and arrowType4", hashCode1, hashCode4);
    assertNotEquals("Unexpected hashcode for arrowType1 and arrowType5", hashCode1, arrowType5.hashCode());

    // Test with different return types
    ArrowType arrowType6 = new ArrowType(registry, null, getNativeType(VOID_TYPE));
    ArrowType arrowType7 = new ArrowType(registry, null, getNativeType(INT_TYPE));

    assertNotEquals("Unexpected hashcode for arrowType1 and arrowType6", hashCode1, arrowType6.hashCode());
    assertEquals("Unexpected hashcode for arrowType6 and arrowType6", hashCode1, arrowType6.hashCode());
    assertEquals("Unexpected hashcode for arrowType7 and arrowType7", hashCode1, arrowType7.hashCode());
  }

  @Test
  public void testToString() {
    ArrowType arrowType = createArrowTypeWithParameters(INT_TYPE, INT_TYPE);

    assertEquals("Unexpected string representation", "ArrowType[ArrowType[INT_TYPE, INT_TYPE]]", arrowType.toString());
  }

  private ArrowType createArrowTypeWithParameters(JSType... types) {
    Node parameters = new Node(JSTypeNative.createParametersWithVarArgs(NULL_TYPE), types);
    Node returnType = NULL_TYPE;

    return new ArrowType(registry, parameters, returnType);
  }
}