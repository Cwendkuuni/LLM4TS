import static org.junit.Assert.*;
import static com.google.common.collect.Lists.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    Node parameterNode = registry.createIdentifier("param");
    Node returnNode = registry.createIdentifier("return");
    ArrowType arrowType = new ArrowType(registry, parameterNode, returnNode);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(JSTypeNative.ARROW_TYPE, arrowType.getTypeID());

    assertTrue(arrowType.isFunctionType());

    Node noParamsParameterNode = registry.createNode(null);
    ArrowType arrowTypeWithNoParams = new ArrowType(registry, noParamsParameterNode, null);
    assertNull(arrowTypeWithNoParams.parameters);
  }

  @Test
  public void testArrowTypeSubtyping() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType stringType = registry.createPrimitiveType(JSTypeNative.STRING);
    JSType functionType = new FunctionType(registry, intType, stringType);
    JSType arrowType = new ArrowType(registry, null, intType);

    assertTrue(arrowType.isSubtype(intType));
    assertTrue(intType.isSubtype(arrowType));

    assertTrue(arrowType.isSubtype(functionType));
    assertFalse(functionType.isSubtype(arrowType));

    assertFalse(intType.isSubtype(functionType));
    assertFalse(functionType.isSubtype(intType));

    // FunctionType with var_args should be a supertype of ArrowType with var_args
    Node varArgsParameterNode = registry.createIdentifier("param");
    Node varArgsParameterNodeWithEllipsis = registry.createVarArgsNode(varArgsParameterNode);
    FunctionType functionWithVarArgs = new FunctionType(registry, intType, varArgsParameterNodeWithEllipsis);
    ArrowType arrowWithVarArgs = new ArrowType(registry, null, intType, true);
    assertTrue(functionWithVarArgs.isSubtype(arrowWithVarArgs));
  }

  @Test
  public void testArrowTypeEquality() {
    Node paramIdentifier1 = registry.createIdentifier("param1");
    Node paramIdentifier2 = registry.createIdentifier("param2");
    Node returnIdentifier = registry.createIdentifier("return");

    ArrowType arrowType1 = new ArrowType(registry, paramIdentifier1, returnIdentifier);
    ArrowType arrowType2 = new ArrowType(registry, paramIdentifier2, returnIdentifier);
    ArrowType arrowType3 = new ArrowType(registry, paramIdentifier1, returnIdentifier);

    assertTrue(arrowType1.equals(arrowType1));
    assertTrue(arrowType1.equals(arrowType3));
    assertFalse(arrowType1.equals(arrowType2));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node paramIdentifier1 = registry.createIdentifier("param1");
    Node paramIdentifier2 = registry.createIdentifier("param2");
    Node returnIdentifier = registry.createIdentifier("return");

    ArrowType arrowType1 = new ArrowType(registry, paramIdentifier1, returnIdentifier);
    ArrowType arrowType2 = new ArrowType(registry, paramIdentifier2, returnIdentifier);
    ArrowType arrowType3 = new ArrowType(registry, paramIdentifier1, returnIdentifier);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();

    // identical types should have identical hash codes
    assertEquals(hashCode1, hashCode3);
    assertNotSame(hashCode1, hashCode2);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node paramIdentifier = registry.createIdentifier("param");
    ArrowType arrowType = new ArrowType(registry, paramIdentifier, null, true);

    assertFalse(arrowType.returnTypeInferred = false);
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testArrowTypeWithExplicitReturnType() {
    Node paramIdentifier = registry.createIdentifier("param");
    JSType returnType = registry.createPrimitiveType(JSTypeNative.BOOL);
    ArrowType arrowType = new ArrowType(registry, paramIdentifier, returnType);

    assertTrue(arrowType.returnTypeInferred = false);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node paramIdentifier = registry.createIdentifier("param");
    ArrowType arrowType1 = new ArrowType(registry, paramIdentifier, null);
    ArrowType arrowType2 = new ArrowType(registry, paramIdentifier, null, true);

    assertFalse(arrowType1.hasVarArgs());
    assertTrue(arrowType2.hasVarArgs());
  }

  @Test
  public void testArrowTypeWithFunctionParameters() {
    Node intTypeNode = registry.createPrimitiveType(JSTypeNative.INT);
    Node stringTypeNode = registry.createPrimitiveType(JSTypeNative.STRING);
    Node paramIdentifier = registry.createIdentifier("param");

    FunctionType functionType = new FunctionType(registry, intTypeNode, stringTypeNode);
    ArrowType arrowType = new ArrowType(registry, paramIdentifier, intTypeNode);

    assertFalse(arrowType.isEquivalentTo(functionType));
    assertTrue(arrowType.isSubtype(functionType));

    Node varArgsParamIdentifier = registry.createVarArgsNode(paramIdentifier);
    FunctionType functionTypeWithVarArgs = new FunctionType(registry, intTypeNode, varArgsParamIdentifier);
    ArrowType arrowTypeWithVarArgs = new ArrowType(registry, paramIdentifier, intTypeNode, true);

    // FunctionType with var_args should be a supertype of ArrowType with var_args
    assertTrue(functionTypeWithVarArgs.isSubtype(arrowTypeWithVarArgs));
    assertFalse(arrowTypeWithVarArgs.isSubtype(functionTypeWithVarArgs));
  }

  @Test
  public void testArrowTypeWithLocalFunction() {
    Node intTypeNode = registry.createPrimitiveType(JSTypeNative.INT);
    Node stringTypeNode = registry.createPrimitiveType(JSTypeNative.STRING);
    Node paramIdentifier = registry.createIdentifier("param");

    JSType functionType = new FunctionType(registry, intTypeNode, stringTypeNode);
    JSType arrowType = new ArrowType(registry, paramIdentifier, intTypeNode);

    assertTrue(arrowType.isSubtype(functionType));
  }

  @Test
  public void testArrowTypeWithMultipleParameters() {
    Node intTypeNode = registry.createPrimitiveType(JSTypeNative.INT);
    Node stringTypeNode = registry.createPrimitiveType(JSTypeNative.STRING);
    Node paramIdentifier1 = registry.createIdentifier("param1");
    Node paramIdentifier2 = registry.createIdentifier("param2");
    Node paramIdentifier3 = registry.createIdentifier("param3");

    ArrowType arrowType = new ArrowType(registry, list(paramIdentifier1, paramIdentifier2, paramIdentifier3), intTypeNode);

    // Create a FunctionType with the same parameter list, but with different return types
    FunctionType functionType1 = new FunctionType(registry, list(paramIdentifier1, paramIdentifier2, paramIdentifier3), intTypeNode);
    FunctionType functionType2 = new FunctionType(registry, list(paramIdentifier1, paramIdentifier2, paramIdentifier3), stringTypeNode);

    // ArrowType should be a subtype of FunctionType with the same parameter list
    assertTrue(arrowType.isSubtype(functionType1));
    assertTrue(arrowType.isSubtype(functionType2));

    // FunctionType with different return types should not be a subtype of each other
    assertFalse(functionType1.isSubtype(functionType2));
    assertFalse(functionType2.isSubtype(functionType1));
  }

  @Test
  public void testArrowTypeWithNoReturnType() {
    Node noParamsParameterNode = registry.createNode(null);
    ArrowType arrowType = new ArrowType(registry, noParamsParameterNode, null, false);
    assertNull(arrowType.returnType);
  }

  @Test
  public void testArrowTypeToString() {
    Node intTypeNode = registry.createPrimitiveType(JSTypeNative.INT);
    Node stringTypeNode = registry.createPrimitiveType(JSTypeNative.STRING);
    Node paramIdentifier1 = registry.createIdentifier("param1");
    Node paramIdentifier2 = registry.createIdentifier("param2");

    ArrowType arrowType = new ArrowType(registry, list(paramIdentifier1, paramIdentifier2), intTypeNode);
    String expectedArrowTypeString = "ArrowType(FunctionType(Array[Object, Object], Int32NumberType))";
    assertEquals(expectedArrowTypeString, arrowType.toString());

    ArrowType arrowTypeWithNoReturn = new ArrowType(registry, null, null);
    String expectedArrowTypeWithNoReturnString = "ArrowType()";
    assertEquals(expectedArrowTypeWithNoReturnString, arrowTypeWithNoReturn.toString());
  }
}