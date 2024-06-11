import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunctionType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createParametersWithVarArgs;
import static com.google.javascript.rhino.NodeFactory.createTypeName;
import static com.google.javascript.rhino.NodeFactory.createUnknownType;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TypeChecker;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node functionNode = createArrowFunctionType(createNode(createTypeName("number")),
        createUnknownType(), false);
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    ArrowType arrowTypeExpected = new ArrowType(registry, null, createTypeName("number"), false);
    assertEquals("Arrow types should be equal", arrowTypeExpected, arrowType);

    Node functionNodeWithParameters = createArrowFunctionType(
        createParametersWithVarArgs(createUnknownType()), createUnknownType(), false);
    ArrowType arrowTypeWithParameters = new ArrowType(registry, createParametersWithVarArgs(createUnknownType()),
        null, false);
    assertEquals("Arrow types with varargs should be equal", arrowTypeWithParameters, arrowType);

    Node functionNodeWithInferredReturnType = createArrowFunctionType(
        createTypeName("number"), createUnknownType(), true);
    ArrowType arrowTypeWithInferredReturnType = new ArrowType(registry, null, null, true);
    assertEquals("Arrow types with inferred return type should be equal", arrowTypeWithInferredReturnType,
        arrowType);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = createJSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, createTypeName("number"), false);
    ArrowType arrowType2 = new ArrowType(registry, null, createTypeName("number"), false);
    ArrowType arrowType3 = new ArrowType(registry, null, createTypeName("string"), false);
    ArrowType arrowType4 = new ArrowType(registry, null, createTypeName("boolean"), false);
    ArrowType arrowType5 = new ArrowType(registry, null, null, false);
    ArrowType arrowType6 = new ArrowType(registry, null, null, true);

    assertTrue("Arrow type with number return type is a subtype of arrow type with number return type",
        arrowType1.isSubtype(arrowType1));
    assertTrue("Arrow type with number return type is a subtype of arrow type with same parameters and inferred return type",
        arrowType1.isSubtype(arrowType5));
    assertTrue("Arrow type with number return type is a subtype of arrow type with number return type and inferred return type",
        arrowType1.isSubtype(arrowType6));
    assertFalse("Arrow type with number return type is not a subtype of arrow type with string return type",
        arrowType1.isSubtype(arrowType3));
    assertFalse("Arrow type with number return type is not a subtype of arrow type with boolean return type",
        arrowType1.isSubtype(arrowType4));
    assertFalse("Arrow type with number return type is not a subtype of arrow type with no return type", arrowType1.isSubtype(arrowType5));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node func1 = createArrowFunctionType(createTypeName("number"), createTypeName("number"), false);
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    arrowType1.resolveInternal(TypeChecker.NULL_ERROR_REPORTER, TypeChecker.NULL_SCOPE);
    arrowType1.parameters = createParametersWithVarArgs(createNode(createTypeName("number")));

    Node func2 = createArrowFunctionType(createParametersWithVarArgs(createTypeName("number")), createTypeName("number"), false);
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);
    arrowType2.resolveInternal(TypeChecker.NULL_ERROR_REPORTER, TypeChecker.NULL_SCOPE);
    arrowType2.parameters = createParametersWithVarArgs(createNode(createTypeName("number")));

    Node func3 = createArrowFunctionType(createTypeName("number"), createTypeName("number"), false);
    ArrowType arrowType3 = new ArrowType(registry, null, null, false);
    arrowType3.resolveInternal(TypeChecker.NULL_ERROR_REPORTER, TypeChecker.NULL_SCOPE);
    arrowType3.parameters = createParametersWithVarArgs(createNode(createTypeName("number")));

    Node func4 = createArrowFunctionType(createParametersWithVarArgs(createTypeName("number")), createTypeName("number"), false);
    ArrowType arrowType4 = new ArrowType(registry, null, null, false);
    arrowType4.resolveInternal(TypeChecker.NULL_ERROR_REPORTER, TypeChecker.NULL_SCOPE);
    arrowType4.parameters = createParametersWithVarArgs(createNode(createTypeName("number")));

    Node func5 = createArrowFunctionType(createTypeName("number"), createTypeName("number"), false);
    ArrowType arrowType5 = new ArrowType(registry, null, null, false);
    arrowType5.resolveInternal(TypeChecker.NULL_ERROR_REPORTER, TypeChecker.NULL_SCOPE);
    arrowType5.parameters = null;

    assertTrue("Arrow types with equal parameters should be considered equal", arrowType1.hasEqualParameters(arrowType2));
    assertTrue("Arrow types with equal parameters should be considered equal", arrowType1.hasEqualParameters(arrowType3));
    assertFalse("Arrow types with different parameters should not be considered equal", arrowType1.hasEqualParameters(arrowType4));
    assertFalse("Arrow type with null parameters should not be considered equal to arrow type with non-null parameters",
        arrowType1.hasEqualParameters(arrowType5));
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = createJSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, createTypeName("number"), false);
    String expected = "ArrowType[number, null, false]";
    assertEquals("Arrow type toString should match expected", expected, arrowType.toString());
  }

  private JSTypeRegistry createJSTypeRegistry() {
    return new JSTypeRegistry(JSTypeNative.EMPTY_TYPE_REGISTRY, null);
  }
}