import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.UnknownType;

import java.util.Arrays;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.Test
  public void testArrowType() {
    JSType intType = registry.createPrimitiveType(JSTypeNative.INT);
    JSType intFuncType = new FunctionType(registry, intType, intType);

    Node intParamNode = registry.createIdentifier("p");
    Node intParamList = registry.createParametersWithVarArgs(intParamNode);

    ArrowType arrowType1 = new ArrowType(registry, intParamList, intType);
    ArrowType arrowType2 = new ArrowType(registry, intParamList, intType, true);

    // isSubtype() test
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    // hasEqualParameters() test
    ArrowType arrowType3 = new ArrowType(registry, intParamList, intType);
    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    ArrowType arrowType4 = new ArrowType(registry, registry.createParametersWithVarArgs(intParamNode), intType);
    assertFalse(arrowType1.hasEqualParameters(arrowType4));

    // isEquivalentTo() test
    JSType intArrowType1 = registry.createArrowType(intParamList, intType);
    JSType intArrowType2 = registry.createArrowType(registry.createParametersWithVarArgs(intParamNode), intType);
    assertEquals(intArrowType1, intArrowType1);
    assertEquals(intArrowType1, intArrowType2);
    assertNotSame(intArrowType1, intArrowType2);

    // hashCode() test
    intArrowType1 = registry.createArrowType(intParamList, intType);
    intArrowType2 = registry.createArrowType(registry.createParametersWithVarArgs(intParamNode), intType);
    assertEquals(intArrowType1.hashCode(), intArrowType1.hashCode());
    assertEquals(intArrowType1.hashCode(), intArrowType2.hashCode());
    assertNotEquals(intArrowType1.hashCode(), intType.hashCode());

    // getLeastSupertype() test (not supported)

    // getGreatestSubtype() test (not supported)

    // testForEquality() test (not supported)

    // visit() test
    ArrowType arrowType5 = new ArrowType(registry, intParamList, intType);
    JSType visitorResult = registry.createArrowType(intParamList, intType);
    assertEquals(arrowType5.accept(registry.createArrowTypeVisitor(), null), visitorResult);

    // isSubtype() test with null types
    ArrowType nullArrowType1 = new ArrowType(registry, null, null);
    ArrowType nullArrowType2 = new ArrowType(registry, null, null, true);
    assertTrue(nullArrowType1.isSubtype(nullArrowType2));
    assertFalse(nullArrowType2.isSubtype(nullArrowType1));

    // isSubtype() test with different number of parameters
    ArrowType intArrowType3 = new ArrowType(registry, intParamList, intType);
    ArrowType voidArrowType = new ArrowType(registry, registry.createEmptyParameterList(), UnknownType.VOID);
    assertFalse(intArrowType3.isSubtype(voidArrowType));

    // isSubtype() test with different return types
    ArrowType intArrowType4 = new ArrowType(registry, intParamList, intType);
    ArrowType stringArrowType = new ArrowType(registry, intParamList, registry.createStringType());
    assertFalse(intArrowType4.isSubtype(stringArrowType));

    // isSubtype() test with different parameter types
    ArrowType intArrowType5 = new ArrowType(registry, intParamList, intType);
    ArrowType numberArrowType = new ArrowType(registry, intParamList, registry.createNumberType());
    assertFalse(intArrowType5.isSubtype(numberArrowType));

    // isSubtype() test with null parameter lists
    ArrowType nullArrowType3 = new ArrowType(registry, null, intType);
    ArrowType nullArrowType4 = new ArrowType(registry, null, intType, true);
    assertTrue(nullArrowType3.isSubtype(nullArrowType4));
    assertFalse(nullArrowType4.isSubtype(nullArrowType3));

    // isSubtype() test with var_args
    ArrowType intVarArgsArrowType = new ArrowType(registry, intParamList, intType);
    ArrowType varArgsIntArrowType = new ArrowType(registry, registry.createParametersWithVarArgs(intParamNode), intType);
    assertTrue(intVarArgsArrowType.isSubtype(varArgsIntArrowType));
    assertFalse(varArgsIntArrowType.isSubtype(intVarArgsArrowType));

    // test resolveInternal()
    ArrowType resolvedArrowType = registry.createArrowType(intParamList, intType);
    resolvedArrowType.resolveInternal(registry.createErrorReporter(), null);
    assertEquals(intType, resolvedArrowType.returnType);
  }
}