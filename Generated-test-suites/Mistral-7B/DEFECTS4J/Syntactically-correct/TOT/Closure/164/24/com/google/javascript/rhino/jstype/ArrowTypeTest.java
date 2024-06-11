import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.Property.*;
import static com.google.javascript.rhino.jstype.JSType.getNativeType;
import static com.google.javascript.rhino.jstype.JSType.createParametersWithVarArgs;
import static com.google.javascript.rhino.jstype.JSType.createFunctionType;
import static com.google.javascript.rhino.jstype.JSType.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSType.INT_TYPE;
import static com.google.javascript.rhino.jstype.JSType.OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSType.getFunctionType;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testSimpleArrowType() {
    Node params = createParametersWithVarArgs(registry, getNativeType(INT_TYPE));
    JSType returnType = getNativeType(INT_TYPE);
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals(arrowType.toString(), "arrowType(int) -> int");

    FunctionType functionType = new FunctionType(registry, INT_TYPE, ARRAY_TYPE.of(INT_TYPE));
    assertEquals(arrowType.isSubtype(functionType), true);

    ArrowType arrowTypeWithBooleanReturn = new ArrowType(registry, params, getNativeType(BOOL_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithBooleanReturn));

    ArrowType arrowTypeWithMoreParams = new ArrowType(registry, createParametersWithVarArgs(registry, getNativeType(INT_TYPE), getNativeType(BOOL_TYPE)), getNativeType(INT_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithMoreParams));

    // Error cases
    ArrowType arrowTypeWithInvalidReturn = new ArrowType(registry, params, OBJECT_TYPE);
    try {
      arrowTypeWithInvalidReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullParams = new ArrowType(registry, null, getNativeType(INT_TYPE));
    try {
      arrowTypeWithNullParams.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, params, null);
    try {
      arrowTypeWithNullReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node params = createParametersWithVarArgs(registry, getNativeType(INT_TYPE));
    ArrowType arrowType = new ArrowType(registry, params, null, true);

    assertEquals(arrowType.toString(), "arrowType(int) -> Unknown");

    FunctionType functionType = new FunctionType(registry, INT_TYPE, ARRAY_TYPE.of(INT_TYPE));
    assertTrue(arrowType.isSubtype(functionType));

    ArrowType arrowTypeWithBooleanReturn = new ArrowType(registry, params, getNativeType(BOOL_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithBooleanReturn));

    ArrowType arrowTypeWithMoreParams = new ArrowType(registry,
        createParametersWithVarArgs(registry, getNativeType(INT_TYPE), getNativeType(BOOL_TYPE)), getNativeType(INT_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithMoreParams));

    // Error cases
    ArrowType arrowTypeWithInvalidReturn = new ArrowType(registry, params, OBJECT_TYPE);
    try {
      arrowTypeWithInvalidReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullParams = new ArrowType(registry, null, getNativeType(INT_TYPE));
    try {
      arrowTypeWithNullParams.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, params, null);
    try {
      arrowTypeWithNullReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithFunctionReturnType() {
    Node params = createParametersWithVarArgs(registry, getNativeType(INT_TYPE));
    JSType returnType = getFunctionType(registry, INT_TYPE, ARRAY_TYPE.of(INT_TYPE));
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals(arrowType.toString(), "arrowType(int) -> function(int[]):int");

    FunctionType functionType = new FunctionType(registry, INT_TYPE, ARRAY_TYPE.of(INT_TYPE));
    assertTrue(arrowType.isSubtype(functionType));

    ArrowType arrowTypeWithBooleanReturn = new ArrowType(registry, params, getNativeType(BOOL_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithBooleanReturn));

    ArrowType arrowTypeWithMoreParams = new ArrowType(registry,
        createParametersWithVarArgs(registry, getNativeType(INT_TYPE), getNativeType(BOOL_TYPE)), getNativeType(INT_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithMoreParams));

    // Error cases
    ArrowType arrowTypeWithInvalidReturn = new ArrowType(registry, params, OBJECT_TYPE);
    try {
      arrowTypeWithInvalidReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullParams = new ArrowType(registry, null, getNativeType(INT_TYPE));
    try {
      arrowTypeWithNullParams.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, params, null);
    try {
      arrowTypeWithNullReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node params = createParametersWithVarArgs(registry, getNativeType(INT_TYPE));
    JSType returnType = getNativeType(INT_TYPE);
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals(arrowType.toString(), "arrowType(int...) -> int");

    FunctionType functionType = new FunctionType(registry, createParametersWithVarArgs(registry, INT_TYPE), ARRAY_TYPE.of(INT_TYPE));
    assertEquals(arrowType.hasEqualParameters(functionType), true);

    ArrowType arrowTypeWithBooleanReturn = new ArrowType(registry, createParametersWithVarArgs(registry, INT_TYPE), getNativeType(BOOL_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithBooleanReturn));

    ArrowType arrowTypeWithMoreParams = new ArrowType(registry,
        createParametersWithVarArgs(registry, INT_TYPE, BOOL_TYPE), getNativeType(INT_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithMoreParams));

    // Error cases
    ArrowType arrowTypeWithInvalidReturn = new ArrowType(registry, params, OBJECT_TYPE);
    try {
      arrowTypeWithInvalidReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullParams = new ArrowType(registry, null, getNativeType(INT_TYPE));
    try {
      arrowTypeWithNullParams.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, params, null);
    try {
      arrowTypeWithNullReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithVarArgsAndInferredReturn() {
    Node params = createParametersWithVarArgs(registry, getNativeType(INT_TYPE));
    ArrowType arrowType = new ArrowType(registry, params, null, true);

    assertEquals(arrowType.toString(), "arrowType(int...) -> Unknown");

    FunctionType functionType = new FunctionType(registry, createParametersWithVarArgs(registry, INT_TYPE), ARRAY_TYPE.of(INT_TYPE));
    assertTrue(arrowType.isSubtype(functionType));

    ArrowType arrowTypeWithBooleanReturn = new ArrowType(registry, createParametersWithVarArgs(registry, INT_TYPE), getNativeType(BOOL_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithBooleanReturn));

    ArrowType arrowTypeWithMoreParams = new ArrowType(registry,
        createParametersWithVarArgs(registry, INT_TYPE, BOOL_TYPE), getNativeType(INT_TYPE));
    assertFalse(arrowType.isSubtype(arrowTypeWithMoreParams));

    // Error cases
    ArrowType arrowTypeWithInvalidReturn = new ArrowType(registry, params, OBJECT_TYPE);
    try {
      arrowTypeWithInvalidReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullParams = new ArrowType(registry, null, getNativeType(INT_TYPE));
    try {
      arrowTypeWithNullParams.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }

    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, params, null);
    try {
      arrowTypeWithNullReturn.resolveInternal(registry.getErrorReporter(), registry.createEmptyScope());
      fail("Expected TypeError");
    } catch (TypeError e) {
      // Expected
    }
  }
}