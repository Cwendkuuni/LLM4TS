import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.AssertTypeEquality;
import com.google.javascript.rhino.testing.RuntimeTest;
import com.google.javascript.rhino.testing.RuntimeTest.GlobalScope;

public class ArrowTypeTest {

  @Test
  public void testArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test an arrow type with no parameters and an inferred return type.
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    assertEquals(arrowType1.getClass(), ArrowType.class);
    assertTrue(arrowType1.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType1.returnType.getType());

    // Test an arrow type with no parameters and an explicitly specified return type.
    JSType intType = registry.getBuiltins().getIntegerType();
    ArrowType arrowType2 = new ArrowType(registry, null, intType);
    assertFalse(arrowType2.returnTypeInferred);
    assertEquals(intType.getType(), arrowType2.returnType.getType());

    // Test an arrow type with one parameter and an inferred return type.
    Node paramNode = createNode("x");
    JSType paramType = registry.getParameterType(paramNode);
    ArrowType arrowType3 = new ArrowType(registry, paramNode, null);
    assertEquals(paramType, arrowType3.parameters.getFirstChild().getJSType());
    assertTrue(arrowType3.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType3.returnType.getType());

    // Test an arrow type with one parameter and an explicitly specified return type.
    JSType stringType = registry.getStringType();
    ArrowType arrowType4 = new ArrowType(registry, paramNode, stringType);
    assertFalse(arrowType4.returnTypeInferred);
    assertEquals(stringType.getType(), arrowType4.returnType.getType());

    // Test an arrow type with multiple parameters and an inferred return type.
    Node paramNodes = createNode("x, y");
    JSType parameters = registry.createParametersWithVarArgs(UNKNOWN_TYPE);
    parameters.setChild(0, paramNodes.getFirstChild());
    ArrowType arrowType5 = new ArrowType(registry, paramNodes, null);
    assertEquals(parameters, arrowType5.parameters);
    assertTrue(arrowType5.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType5.returnType.getType());

    // Test an arrow type with multiple parameters and an explicitly specified return type.
    ArrowType arrowType6 = new ArrowType(registry, paramNodes, intType);
    assertFalse(arrowType6.returnTypeInferred);
    assertEquals(intType, arrowType6.returnType);

    // Test isSubtype with arrow types with the same parameters and return types.
    ArrowType arrowType7 = new ArrowType(registry, paramNodes, intType);
    assertTrue(arrowType5.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType5));

    // Test isSubtype with arrow types with different return types.
    JSType doubleType = registry.getBuiltins().getDoubleType();
    ArrowType arrowType8 = new ArrowType(registry, paramNodes, doubleType);
    assertFalse(arrowType5.isSubtype(arrowType8));
    assertFalse(arrowType8.isSubtype(arrowType5));

    // Test isSubtype with arrow types with different numbers of parameters.
    Node paramNodes2 = createNode("x, y, z");
    ArrowType arrowType9 = new ArrowType(registry, paramNodes2, intType);
    assertFalse(arrowType5.isSubtype(arrowType9));
    assertFalse(arrowType9.isSubtype(arrowType5));

    // Test isSubtype with an arrow type and a function type.
    Node functionNode = createNode("function(x) { return 42; }");
    FunctionType functionType = registry.getFunctionType(functionNode);
    assertFalse(arrowType5.isSubtype(functionType));
    assertFalse(functionType.isSubtype(arrowType5));

    // Test isSubtype with an arrow type and an object type.
    JSType objectType = registry.getObjectType();
    assertFalse(arrowType5.isSubtype(objectType));
    assertFalse(objectType.isSubtype(arrowType5));

    // Test hasEqualParameters.
    ArrowType arrowType10 = new ArrowType(registry, paramNodes, intType);
    ArrowType arrowType11 = new ArrowType(registry, paramNodes, intType);
    assertTrue(arrowType10.hasEqualParameters(arrowType11));
    assertTrue(arrowType11.hasEqualParameters(arrowType10));

    // Test equals and hashCode.
    ArrowType arrowType12 = new ArrowType(registry, paramNodes, intType);
    ArrowType arrowType13 = new ArrowType(registry, paramNodes, intType);
    assertTrue(arrowType12.equals(arrowType13));
    assertTrue(arrowType12.hashCode() == arrowType13.hashCode());
    ArrowType arrowType14 = new ArrowType(registry, paramNodes2, intType);
    assertFalse(arrowType12.equals(arrowType14));
    assertFalse(arrowType12.hashCode() == arrowType14.hashCode());

    // Test toString.
    String expected = "function(x): number";
    String actual = arrowType5.toString();
    assertEquals(expected, actual);

    // Test resolveInternal.
    ArrowType arrowType15 = new ArrowType(registry, null, null);
    ErrorReporter errorReporter = new ErrorReporter();
    GlobalScope globalScope = new GlobalScope(registry);
    arrowType15.resolveInternal(errorReporter, globalScope);
    assertTrue(arrowType15.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType15.returnType.getType());

    // Test resolveInternal with an explicitly specified return type.
    JSType boolType = registry.getBooleanType();
    ArrowType arrowType16 = new ArrowType(registry, null, boolType);
    arrowType16.resolveInternal(errorReporter, globalScope);
    assertFalse(arrowType16.returnTypeInferred);
    assertEquals(boolType, arrowType16.returnType);

    // Test resolveInternal with a single required parameter.
    Node paramNode1 = createNode("x");
    ArrowType arrowType17 = new ArrowType(registry, paramNode1, null);
    arrowType17.resolveInternal(errorReporter, globalScope);
    JSType paramType1 = paramNode1.getJSType();
    assertEquals(paramType1, arrowType17.parameters.getFirstChild().getJSType());
    assertTrue(arrowType17.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType17.returnType.getType());

    // Test resolveInternal with multiple required parameters.
    Node paramNodes3 = createNode("x, y");
    ArrowType arrowType18 = new ArrowType(registry, paramNodes3, null);
    arrowType18.resolveInternal(errorReporter, globalScope);
    JSType paramType2 = paramNodes3.getFirstChild().getNext().getJSType();
    assertEquals(paramType2, arrowType18.parameters.getFirstChild().getNext().getJSType());
    assertTrue(arrowType18.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType18.returnType.getType());

    // Test resolveInternal with a variable number of parameters.
    Node paramNodes4 = createNode("x, ...args");
    ArrowType arrowType19 = new ArrowType(registry, paramNodes4, null);
    arrowType19.resolveInternal(errorReporter, globalScope);
    JSType paramType3 = paramNodes4.getFirstChild().getNext();
    assertEquals(registry.createParametersWithVarArgs(UNKNOWN_TYPE), arrowType19.parameters);
    assertTrue(arrowType19.returnTypeInferred);
    assertEquals(UNKNOWN_Type, arrowType19.returnType.getType());

    // Test resolveInternal with an error.
    Node functionNode2 = createNode("function(x) { throw new Error('Test'); }");
    FunctionType functionType2 = registry.getFunctionType(functionNode2);
    ArrowType arrowType20 = new ArrowType(registry, paramNodes, functionType2);
    arrowType20.resolveInternal(errorReporter, globalScope);
    assertFalse(arrowType20.returnTypeInferred);
    assertNull(arrowType20.returnType);

    // Test hasUnknownParamsOrReturn.
    ArrowType arrowType21 = new ArrowType(registry, paramNodes, null);
    assertTrue(arrowType21.hasUnknownParamsOrReturn());

    ArrowType arrowType22 = new ArrowType(registry, paramNodes, intType);
    assertFalse(arrowType22.hasUnknownParamsOrReturn());

    // Test isEquivalentTo.
    ArrowType arrowType23 = new ArrowType(registry, paramNodes, intType);
    ArrowType arrowType24 = new ArrowType(registry, paramNodes, intType);
    assertTrue(arrowType23.isEquivalentTo(arrowType24));
    assertTrue(arrowType24.isEquivalentTo(arrowType23));

    ArrowType arrowType25 = new ArrowType(registry, paramNodes2, intType);
    assertFalse(arrowType23.isEquivalentTo(arrowType25));
    assertFalse(arrowType25.isEquivalentTo(arrowType23));

    ArrowType arrowType26 = new ArrowType(registry, paramNodes, stringType);
    assertFalse(arrowType23.isEquivalentTo(arrowType26));
    assertFalse(arrowType26.isEquivalentTo(arrowType23));

    ArrowType arrowType27 = new ArrowType(registry, null, null);
    assertFalse(arrowType23.isEquivalentTo(arrowType27));
    assertFalse(arrowType27.isEquivalentTo(arrowType23));

    // Test getPossibleToBooleanOutcomes.
    ArrowType arrowType28 = new ArrowType(registry, null, boolType);
    BooleanLiteralSet booleanLiteralSet = arrowType28.getPossibleToBooleanOutcomes();
    assertTrue(booleanLiteralSet.contains(BooleanLiteralSet.TRUE));

    // Test getLeastSupertype and getGreatestSubtype.
    ArrowType arrowType29 = new ArrowType(registry, paramNodes, intType);
    ArrowType arrowType30 = new ArrowType(registry, paramNodes, stringType);
    ArrowType arrowType31 = new ArrowType(registry, paramNodes, objectType);
    ArrowType arrowType32 = new ArrowType(registry, paramNodes, UNKNOWN_TYPE);

    JSType leastSupertype = arrowType29.getLeastSupertype(arrowType30);
    assertEquals(arrowType29, leastSupertype);

    JSType greatestSubtype = arrowType30.getGreatestSubtype(arrowType29);
    assertEquals(arrowType30, greatestSubtype);

    JSType leastSupertype2 = arrowType31.getLeastSupertype(arrowType32);
    assertEquals(arrowType31, leastSupertype2);

    JSType greatestSubtype2 = arrowType32.getGreatestSubtype(arrowType31);
    assertEquals(arrowType32, greatestSubtype2);

    JSType leastSupertype3 = arrowType32.getLeastSupertype(arrowType29);
    assertEquals(arrowType29, leastSupertype3);

    JSType greatestSubtype3 = arrowType29.getGreatestSubtype(arrowType32);
    assertEquals(arrowType32, greatestSubtype3);

    // Test visit.
    ArrowType arrowType33 = new ArrowType(registry, paramNodes, intType);
    Node result = new AssertTypeEquality().visit(arrowType33);
    assertEquals(arrowType33, result);
  }

  private Node createNode(String nodeValue) {
    ReturnNode returnNode = new ReturnNode();
    FunctionNode functionNode = new FunctionNode();
    functionNode.setParameters(new ParametersNode(nodeValue));
    functionNode.setBody(returnNode);
    Node node = new Node(Node.CALL, functionNode);
    node.setFirstChild(Node.createNul());
    node.setLineNumber(1);
    node.setColumnNumber(1);
    return node;
  }
}