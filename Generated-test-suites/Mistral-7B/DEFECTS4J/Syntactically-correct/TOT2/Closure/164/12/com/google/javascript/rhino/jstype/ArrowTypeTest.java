import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createFunctionExpression;
import static com.google.javascript.rhino.NodeFactory.createIntLiteral;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createParameter;
import static com.google.javascript.rhino.NodeFactory.createTypeOf;
import static com.google.javascript.rhino.NodeFactory.createUndefined;
import static com.google.javascript.rhino.NodeFactory.createUnknown;
import static com.google.javascript.rhino.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.JSTypeFactory.createNativeType;
import static com.google.javascript.rhino.JSTypeFactory.createNullType;
import static com.google.javascript.rhino.JSTypeFactory.createNumberType;
import static com.google.javascript.rhino.JSTypeFactory.createStringType;
import static com.google.javascript.rhino.JSTypeFactory.createTypeError;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType.JSTypeRegistryFactory;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = createJSTypeRegistry();

  private static JSTypeRegistry createJSTypeRegistry() {
    return JSTypeRegistryFactory.createRegistry(registry);
  }

  @Test
  public void testSimpleArrowType() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x")}, createNumberType());
    JSType expectedType = new ArrowType(registry, createList(), createNumberType(), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createNumberType(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithReturnTypeInferred() {
    Node funcNode = createFunctionExpression(createName("f"), new Node[]{createParameter("x")}, createUndefined(), createList(), createList());
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createNativeType(JSTypeNative.VOID), true);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createNativeType(JSTypeNative.VOID), true).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithParameters() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x"), createParameter("y")}, createNumberType());
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createNumberType(), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createNumberType(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x"), createParameter(createVarName("args"))}, createNumberType());
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createNumberType(), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createNumberType(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithReturnTypeAndParameters() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x"), createParameter("y")}, createIntLiteral(3));
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createIntLiteral(3), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createIntLiteral(3), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithNullParameters() {
    Node funcNode = createArrowFunction(new Node[]{createParameter(createNullType())}, createStringType());
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createStringType(), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createStringType(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithUnknownReturnType() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x")}, createUnknown());
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createUnknown(), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createUnknown(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testArrowTypeWithUnknownParametersAndUnknownReturnType() {
    Node funcNode = createArrowFunction(new Node[]{createParameter(createUnknown()), createParameter(createUnknown())}, createUnknown());
    JSType expectedType = new ArrowType(registry, funcNode.parameters, createUnknown(), false);
    JSType actualType = new ArrowType(registry, funcNode.parameters, createUnknown(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
    assertEquals(expectedType, actualType);
  }

  @Test(expected = TypeError.class)
  public void testArrowTypeWithInvalidReturnType() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x")}, createTypeError());
    new ArrowType(registry, funcNode.parameters, createTypeError(), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
  }

  @Test(expected = TypeError.class)
  public void testArrowTypeWithInvalidParameterType() {
    Node funcNode = createArrowFunction(new Node[]{createParameter("x")}, createFunctionType(createList(), createList()));
    new ArrowType(registry, funcNode.parameters, createFunctionType(createList(), createList()), false).resolveInternal(new ErrorReporter(), registry.createEmptyScope());
  }

  // Additional edge cases and error scenarios can be added here

  @Test
  public void testArrowTypeComparison() {
    JSType arrowType1 = new ArrowType(registry, createList(), createNumberType(), false);
    JSType arrowType2 = new ArrowType(registry, createList(), createIntLiteral(3), false);
    JSType arrowType3 = new ArrowType(registry, createList(createParameter("x")), createNumberType(), false);
    JSType arrowType4 = new ArrowType(registry, createList(createParameter("x"), createParameter("y")), createNumberType(), false);
    JSType functionType = new FunctionType(registry, createList(), createList(), createNumberType(), false);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType1.isSubtype(functionType));

    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType2.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType4));
    assertFalse(arrowType2.isSubtype(functionType));

    assertTrue(arrowType3.isSubtype(arrowType1));
    assertTrue(arrowType3.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType2));
    assertFalse(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType3.isSubtype(functionType));

    assertTrue(arrowType4.isSubtype(arrowType1));
    assertTrue(arrowType4.isSubtype(arrowType4));
    assertTrue(arrowType4.isSubtype(arrowType3));
    assertFalse(arrowType4.isSubtype(functionType));

    assertFalse(functionType.isSubtype(arrowType1));
    assertFalse(functionType.isSubtype(arrowType2));
    assertFalse(functionType.isSubtype(arrowType3));
    assertFalse(functionType.isSubtype(arrowType4));
    assertTrue(functionType.isSubtype(functionType));
  }

  @Test
  public void testArrowTypeEquality() {
    JSType arrowType1 = new ArrowType(registry, createList(), createNumberType(), false);
    JSType arrowType2 = new ArrowType(registry, createList(), createIntLiteral(3), false);
    JSType arrowType3 = new ArrowType(registry, createList(createParameter("x")), createNumberType(), false);
    JSType arrowType4 = new ArrowType(registry, createList(createParameter("x"), createParameter("y")), createNumberType(), false);
    JSType functionType = new FunctionType(registry, createList(), createList(), createNumberType(), false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
    assertFalse(arrowType1.isEquivalentTo(arrowType4));
    assertFalse(arrowType1.isEquivalentTo(functionType));

    assertFalse(arrowType2.isEquivalentTo(arrowType1));
    assertFalse(arrowType2.isEquivalentTo(arrowType3));
    assertFalse(arrowType2.isEquivalentTo(arrowType4));
    assertFalse(arrowType2.isEquivalentTo(functionType));

    assertFalse(arrowType3.isEquivalentTo(arrowType1));
    assertFalse(arrowType3.isEquivalentTo(arrowType2));
    assertTrue(arrowType3.isEquivalentTo(arrowType3));
    assertFalse(arrowType3.isEquivalentTo(arrowType4));
    assertFalse(arrowType3.isEquivalentTo(functionType));

    assertFalse(arrowType4.isEquivalentTo(arrowType1));
    assertFalse(arrowType4.isEquivalentTo(arrowType2));
    assertFalse(arrowType4.isEquivalentTo(arrowType3));
    assertTrue(arrowType4.isEquivalentTo(arrowType4));
    assertFalse(arrowType4.isEquivalentTo(functionType));

    assertFalse(functionType.isEquivalentTo(arrowType1));
    assertFalse(functionType.isEquivalentTo(arrowType2));
    assertFalse(functionType.isEquivalentTo(arrowType3));
    assertFalse(functionType.isEquivalentTo(arrowType4));
    assertTrue(functionType.isEquivalentTo(functionType));
  }
}