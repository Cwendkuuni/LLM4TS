import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeOf;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("function(...) -> string", arrowType.toString());

    assertNotNull(arrowType.parameters);
    assertEquals(0, arrowType.parameters.getChildCount());
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList(Node.newIdent("x"), Node.newIdent("y"));
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("function(x, y) -> number", arrowType.toString());

    assertNotNull(arrowType.parameters);
    assertEquals(2, arrowType.parameters.getChildCount());

    Node param1 = arrowType.parameters.getFirstChild();
    assertEquals("x", param1.toString());
    JSType param1Type = param1.getJSType();
    assertNotNull(param1Type);
    assertEquals(JSType.IDENTIFIER, param1Type.getClass().getSimpleName());

    Node param2 = arrowType.parameters.getChild(1);
    assertEquals("y", param2.toString());
    JSType param2Type = param2.getJSType();
    assertNotNull(param2Type);
    assertEquals(JSType.IDENTIFIER, param2Type.getClass().getSimpleName());

    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertEquals("function(...) -> any", arrowType.toString());

    assertNotNull(arrowType.parameters);
    assertEquals(0, arrowType.parameters.getChildCount());
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList(Node.newIdent("x"), Node.newVarArgs());
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("function(x, ...) -> number", arrowType.toString());

    assertNotNull(arrowType.parameters);
    assertEquals(1, arrowType.parameters.getChildCount());

    Node param1 = arrowType.parameters.getFirstChild();
    assertEquals("x", param1.toString());
    JSType param1Type = param1.getJSType();
    assertNotNull(param1Type);
    assertEquals(JSType.IDENTIFIER, param1Type.getClass().getSimpleName());

    Node varArgs = arrowType.parameters.getLastChild();
    assertEquals("...", varArgs.toString());
    assertNull(varArgs.getJSType());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with the same ArrowType
    ArrowType arrowType1 = new ArrowType(registry, null, null);
    ArrowType arrowType2 = new ArrowType(registry, null, null);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    // Test with different ArrowTypes
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("y")), null);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    // Test with ArrowType and FunctionType
    FunctionType functionType = new FunctionType( registry, null, null, null );
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x")), null);
    ArrowType arrowType2 = new FunctionType(registry, Node.newList(Node.newIdent("x")), functionType);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    // Test with ArrowType and a native type
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x")), null);
    assertFalse(arrowType1.isSubtype(JSTypeNative.getBooleanType()));
    assertFalse(JSTypeNative.getBooleanType().isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("z")), null);
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("z")), null);
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("z")), null);
    assertNotSame(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    assertEquals("function(x, y) -> ", arrowType1.toString());

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    assertEquals("function(x, y) -> ", arrowType1.toStringHelper(false).toString());
    assertEquals("Function(x, y)", arrowType1.toStringHelper(true).toString());
  }

  @Test
  public void testArrowTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    assertEquals(arrowType1, arrowType2);
    assertEquals(arrowType2, arrowType1);

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("z")), null);
    assertNotEquals(arrowType1, arrowType2);
    assertNotEquals(arrowType2, arrowType1);
  }

  @Test
  public void testArrowTypeGetLeastSupertype() {
    throw new UnsupportedOperationException("ArrowType.getLeastSupertype() is not implemented.");
  }

  @Test
  public void testArrowTypeGetGreatestSubtype() {
    throw new UnsupportedOperationException("ArrowType.getGreatestSubtype() is not implemented.");
  }

  @Test
  public void testArrowTypeTestForEquality() {
    throw new UnsupportedOperationException("ArrowType.testForEquality() is not implemented.");
  }

  @Test
  public void testArrowTypeVisit() {
    throw new UnsupportedOperationException("ArrowType.visit() is not implemented.");
  }

  @Test
  public void testArrowTypeGetPossibleToBooleanOutcomes() {
    throw new UnsupportedOperationException("ArrowType.getPossibleToBooleanOutcomes() is not implemented.");
  }

  @Test
  public void testArrowTypeResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType intType = registry.createNumberType();
    JSType stringType = registry.createStringType();

    ArrowType arrowType = new ArrowType(registry, Node.newList(), null);

    arrowType.resolveInternal(new ErrorReporter() {
      @Override
      public void error(String message, int line, int offsetInLine) {
        fail("Error encountered during resolution: " + message);
      }
    }, new StaticScope<JSType>(registry));

    assertNotNull(arrowType.returnType);
    assertEquals(JSTypeNative.getAnyType(), arrowType.returnType);

    ArrowType arrowTypeWithReturnType = new ArrowType(registry, Node.newList(), intType);
    arrowTypeWithReturnType.resolveInternal(new ErrorReporter() {
      @Override
      public void error(String message, int line, int offsetInLine) {
        fail("Error encountered during resolution: " + message);
      }
    }, new StaticScope<JSType>(registry));

    assertNotNull(arrowTypeWithReturnType.returnType);
    assertEquals(intType, arrowTypeWithReturnType.returnType);

    ArrowType arrowTypeWithParams = new ArrowType(registry, Node.newList(Node.newIdent("x"), Node.newIdent("y")), null);
    arrowTypeWithParams.resolveInternal(new ErrorReporter() {
      @Override
      public void error(String message, int line, int offsetInLine) {
        fail("Error encountered during resolution: " + message);
      }
    }, new StaticScope<JSType>(registry));

    Node param1 = arrowTypeWithParams.parameters.getFirstChild();
    JSType param1Type = param1.getJSType();
    assertNotNull(param1Type);
    assertEquals(JSType.IDENTIFIER, param1Type.getClass().getSimpleName());

    Node param2 = arrowTypeWithParams.parameters.getChild(1);
    JSType param2Type = param2.getJSType();
    assertNotNull(param2Type);
    assertEquals(JSType.IDENTIFIER, param2Type.getClass().getSimpleName());
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, null);
    assertFalse(arrowType1.hasUnknownParamsOrReturn());

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newIdent("x")), null);
    assertFalse(arrowType2.hasUnknownParamsOrReturn());

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(), null);
    arrowType3.returnType = null;
    assertTrue(arrowType3.hasUnknownParamsOrReturn());

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(), null);
    arrowType4.parameters = null;
    assertTrue(arrowType4.hasUnknownParamsOrReturn());
  }
}