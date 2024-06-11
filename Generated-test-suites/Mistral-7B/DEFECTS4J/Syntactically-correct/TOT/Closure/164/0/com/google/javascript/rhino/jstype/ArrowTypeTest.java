import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.JSTestCase;

public class ArrowTypeTest extends JSTestCase {

  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Override
  public void setUp() {
    super.setUp();
    errorReporter = getErrorReporter();
    scope = new StaticScope<JSType>(null);
  }

  /**
   * Test for ArrowType with known types for parameters and return type.
   */
  public void testArrowTypeKnownTypes() {
    Node functionNode = parse("function foo(number x, boolean y) { }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(NUMBER), false);
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(NUMBER), getNativeType(BOOLEAN), false).resolve(
            errorReporter, scope);
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for ArrowType with inferred return type.
   */
  public void testArrowTypeInferredReturnType() {
    Node functionNode = parse("function foo(number x) { return x * 2; }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        null, false).resolve(errorReporter, scope);
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        null, true).resolve(errorReporter, scope);
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for ArrowType with varargs.
   */
  public void testArrowTypeWithVarargs() {
    Node functionNode = parse("function foo(number... numbers) { }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(),
        getContext().getTypeRegistry().createParametersWithVarArgs(
            getNativeType(NUMBER)),
        getNativeType(UNKNOWN_TYPE), false).resolve(
            errorReporter, scope);
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(UNKNOWN_TYPE), false).resolve(errorReporter, scope);
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for ArrowType with different parameter types.
   */
  public void testArrowTypeWithDifferentParameterTypes() {
    Node functionNode = parse("function foo(number x, boolean y) { }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(NUMBER), false).resolve(errorReporter, scope);

    Node functionNodeDifferent = parse("function foo(boolean x, number y) { }");
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNodeDifferent.getParameters(),
        getNativeType(BOOLEAN), false).resolve(errorReporter, scope);

    assertFalse(expectedType.isSubtype(actualType));
  }

  /**
   * Test for ArrowType with the same parameter types but different return types.
   */
  public void testArrowTypeSameParametersDifferentReturnTypes() {
    Node functionNode = parse("function foo(number x) { return x * 2; }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(NUMBER), false).resolve(errorReporter, scope);

    Node functionNodeDifferentReturn = parse("function foo(number x) { return 'hello'; }");
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNodeDifferentReturn.getParameters(),
        getNativeType(STRING), false).resolve(errorReporter, scope);

    assertFalse(expectedType.isSubtype(actualType));
  }

  /**
   * Test for ArrowType with same parameters and return types but different
   * inferred return types.
   */
  public void testArrowTypeSameTypesDifferentReturnInferred() {
    Node functionNode = parse("function foo(number x) { return x * 2; }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(NUMBER), false).resolve(errorReporter, scope);

    Node functionNodeInferred = parse("function foo(number x) { }");
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNodeInferred.getParameters(),
        null, true).resolve(errorReporter, scope);

    assertFalse(expectedType.isSubtype(actualType));
  }

  /**
   * Test for ArrowType with same parameters and return types but different
   * inferred and known return types.
   */
  public void testArrowTypeSameTypesDifferentReturnTypesInferred() {
    Node functionNode = parse("function foo(number x) { return x * 2; }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), functionNode.getParameters(),
        getNativeType(NUMBER), false).resolve(errorReporter, scope);

    Node functionNodeInferred = parse("function foo(number x) { }");
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), functionNodeInferred.getParameters(),
        null, true).resolve(
            errorReporter, scope);

    Node functionNodeExplicit = parse("function foo(number x) { return x * 2; }");
    JSType expectedTypeExplicit = new ArrowType(
        getContext().getTypeRegistry(), functionNodeExplicit.getParameters(),
        getNativeType(NUMBER), false).resolve(errorReporter, scope);

    assertTrue(expectedType.isSubtype(expectedTypeExplicit));
    assertTrue(expectedTypeExplicit.isSubtype(expectedType));
    assertTrue(expectedType.isSubtype(actualType));
    assertTrue(actualType.isSubtype(expectedType));
  }

  /**
   * Test for ArrowType with empty parameter list.
   */
  public void testArrowTypeEmptyParameters() {
    Node functionNode = parse("function foo() { }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(), null,
        getNativeType(UNKNOWN_TYPE), false).resolve(errorReporter, scope);
    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), null,
        getNativeType(UNKNOWN_TYPE), false).resolve(errorReporter, scope);
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for ArrowType with illegal types (null and UNKNOWN_TYPE).
   */
  public void testArrowTypeIllegalTypes() {
    Node functionNode = parse("function foo() { }");
    JSType expectedType = new ArrowType(
        getContext().getTypeRegistry(),
        getContext().getTypeRegistry().createParametersWithVarArgs(NULL_TYPE),
        getNativeType(UNKNOWN_TYPE), false);

    try {
      new ArrowType(getContext().getTypeRegistry(), null, getNativeType(NUMBER), false);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }

    try {
      new ArrowType(getContext().getTypeRegistry(), null, NULL_TYPE, false);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }

    try {
      new ArrowType(getContext().getTypeRegistry(), null, getNativeType(UNKNOWN_TYPE), false);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      new ArrowType(getContext().getTypeRegistry(), null, getNativeType(NUMBER), true);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      new ArrowType(getContext().getTypeRegistry(), null, null, false);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      new ArrowType(getContext().getTypeRegistry(), null, null, true);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }

    JSType actualType = new ArrowType(
        getContext().getTypeRegistry(), null,
        getNativeType(UNKNOWN_TYPE), false).resolve(errorReporter, scope);

    assertEquals(expectedType, actualType);
  }
}