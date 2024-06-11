import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParameters(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y")
    );
    JSType returnType = registry.getFactory().createStringType();

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    // Test isSubtype with equal ArrowType
    ArrowType arrowTypeEqual = new ArrowType(registry, parametersNode, returnType);
    assertEquals(arrowType, arrowTypeEqual);
    assertEquals(arrowType.isSubtype(arrowTypeEqual), true);

    // Test isSubtype with ArrowType with less parameters
    ArrowType arrowTypeLess = new ArrowType(registry, registry.getFactory().createParameters(
        registry.getFactory().createIdentifier("x")
    ), returnType);
    assertEquals(arrowTypeLess.isSubtype(arrowType), true);

    // Test isSubtype with ArrowType with more parameters
    ArrowType arrowTypeMore = new ArrowType(registry, registry.createParametersWithVarArgs(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y"),
        registry.getFactory().createIdentifier("z")
    ), returnType);
    assertEquals(arrowType.isSubtype(arrowTypeMore), false);

    // Test isSubtype with ArrowType with different return type
    ArrowType arrowTypeDifferentReturn = new ArrowType(registry, parametersNode,
        registry.getFactory().createNumberType());
    assertEquals(arrowType.isSubtype(arrowTypeDifferentReturn), false);

    // Test isSubtype with ArrowType with inferred return type
    ArrowType arrowTypeInferredReturn = new ArrowType(registry, parametersNode, null, false);
    ArrowType arrowTypeInferredReturnType = new ArrowType(registry, parametersNode, returnType, true);
    assertEquals(arrowTypeInferredReturn.isSubtype(arrowTypeInferredReturnType), true);

    // Test isSubtype with FunctionType
    JSType functionType = registry.getFactory().createFunctionType(
        registry.getFactory().createParametersWithVarArgs(
            registry.getFactory().createIdentifier("x"),
            registry.getFactory().createIdentifier("y")
        ), registry.getFactory().createVoidType());
    assertEquals(arrowType.isSubtype(functionType), false);

    // Test isSubtype with ObjectType
    JSType objectType = registry.getFactory().createObjectType();
    assertEquals(arrowType.isSubtype(objectType), false);

    // Test isSubtype with VoidType
    JSType voidType = registry.getFactory().createVoidType();
    assertEquals(arrowType.isSubtype(voidType), false);

    // Test hasEqualParameters
    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, parametersNode, null);
    ArrowType arrowTypeWithNullReturnType = new ArrowType(registry, null, returnType);
    assertEquals(arrowType.hasEqualParameters(arrowTypeWithNullReturn), false);
    assertEquals(arrowType.hasEqualParameters(arrowTypeWithNullReturnType), false);
    ArrowType arrowTypeWithNullParameters = new ArrowType(registry, null, returnType);
    ArrowType arrowTypeWithNullParametersType = new ArrowType(registry, parametersNode, null);
    assertEquals(arrowType.hasEqualParameters(arrowTypeWithNullParameters), false);
    assertEquals(arrowType.hasEqualParameters(arrowTypeWithNullParametersType), false);
    ArrowType arrowTypeWithEqualsParameters = new ArrowType(registry, parametersNode, returnType);
    ArrowType arrowTypeWithEqualsParametersType = new ArrowType(registry, parametersNode, returnType);
    assertEquals(arrowType.hasEqualParameters(arrowTypeWithEqualsParameters), true);
    assertEquals(arrowType.hasEqualParameters(arrowTypeWithEqualsParametersType), true);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y")
    );
    JSType returnType = registry.getFactory().createNumberType();

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    // Test isSubtype with ArrowType with less parameters
    ArrowType arrowTypeLess = new ArrowType(registry, registry.getFactory().createParameters(
        registry.getFactory().createIdentifier("x")
    ), returnType);
    assertEquals(arrowType.isSubtype(arrowTypeLess), true);

    // Test isSubtype with ArrowType with more parameters
    ArrowType arrowTypeMore = new ArrowType(registry, registry.createParametersWithVarArgs(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y"),
        registry.getFactory().createIdentifier("z")
    ), returnType);
    assertEquals(arrowType.isSubtype(arrowTypeMore), false);

    // Test isSubtype with ArrowType with different return type
    ArrowType arrowTypeDifferentReturn = new ArrowType(registry, parametersNode,
        registry.getFactory().createStringType());
    assertEquals(arrowType.isSubtype(arrowTypeDifferentReturn), false);

    // Test isSubtype with FunctionType
    JSType functionType = registry.getFactory().createFunctionType(
        registry.getFactory().createParametersWithVarArgs(
            registry.getFactory().createIdentifier("x"),
            registry.getFactory().createIdentifier("y")
        ), registry.getFactory().createVoidType());
    assertEquals(arrowType.isSubtype(functionType), false);

    // Test isSubtype with ObjectType
    JSType objectType = registry.getFactory().createObjectType();
    assertEquals(arrowType.isSubtype(objectType), false);

    // Test isSubtype with VoidType
    JSType voidType = registry.getFactory().createVoidType();
    assertEquals(arrowType.isSubtype(voidType), false);

    // Test isSubtype with ArrowType with inferred return type
    ArrowType arrowTypeInferredReturn = new ArrowType(registry, parametersNode, null, false);
    ArrowType arrowTypeInferredReturnType = new ArrowType(registry, parametersNode, returnType, true);
    assertEquals(arrowType.isSubtype(arrowTypeInferredReturnType), true);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, registry.getFactory().createNumberType());
    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, registry.getFactory().createNumberType());
    arrowType.getGreatestSubtype(null);
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y")
    );
    JSType returnType = registry.getFactory().createStringType();

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);
    assertEquals(arrowType.toString(), "-> " + parametersNode.toString() + " " + returnType);
  }

  @Test
  public void testToStringForAnnotations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y")
    );
    JSType returnType = registry.getFactory().createStringType();

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);
    assertEquals(arrowType.toStringForAnnotations(), "-> " + parametersNode.toStringForAnnotations() + " " + returnType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertTrue(arrowType.hasUnknownParamsOrReturn());

    Node parametersNode = registry.createParametersWithVarArgs(
        registry.getFactory().createIdentifier("x"),
        registry.getFactory().createIdentifier("y")
    );
    JSType returnType = registry.getFactory().createStringType();
    ArrowType arrowTypeWithKnownParamsAndReturn = new ArrowType(registry, parametersNode, returnType);
    assertFalse(arrowTypeWithKnownParamsAndReturn.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, parametersNode, null);
    assertTrue(arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownParams = new ArrowType(registry, null, registry.getFactory().createNumberType());
    assertTrue(arrowTypeWithUnknownParams.hasUnknownParamsOrReturn());
  }
}