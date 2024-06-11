import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testNewArrowType() {
    Node arrowFunctionNode = registry.parseJsFunctionType("function(a) { return b; }");
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getParameters(), UNKNOWN_TYPE);
    assertEquals("function(a) { return b; }", arrowType.toString());

    // Test isSubtype with simple cases
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, INT, false);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    ArrowType arrowType3 = new ArrowType(registry, null, INT, true);
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));

    ArrowType arrowType4 = new ArrowType(registry, null, STRING, false);
    ArrowType arrowType5 = new ArrowType(registry, null, INT, false);
    assertFalse(arrowType4.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType4));

    // Test isSubtype with complex cases
    ArrowType arrowType6 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType7 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number, b: number) { return b; }"),
        INT, false);
    assertTrue(arrowType6.isSubtype(arrowType7));

    ArrowType arrowType8 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number) { return b: number; }"),
        INT, false);
    ArrowType arrowType9 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number, b: number) { return b; }"),
        INT, false);
    assertFalse(arrowType8.isSubtype(arrowType9));
    assertFalse(arrowType9.isSubtype(arrowType8));

    ArrowType arrowType10 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType11 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number, ...b: number[]) { return b[0]; }"),
        INT, false);
    assertTrue(arrowType10.isSubtype(arrowType11));

    ArrowType arrowType12 = new ArrowType(registry, null, INT, true);
    ArrowType arrowType13 = new ArrowType(registry,
        registry.parseJsFunctionType("function() { return 1; }"),
        INT, true);
    assertTrue(arrowType12.isSubtype(arrowType13));

    // Test hasEqualParameters
    ArrowType arrowType14 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number, b: number) { return b; }"),
        INT, false);
    ArrowType arrowType15 = new ArrowType(registry,
        registry.parseJsFunctionType("function(b: number, a: number) { return b; }"),
        INT, false);
    assertFalse(arrowType14.hasEqualParameters(arrowType15));

    ArrowType arrowType16 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number, b: number) { return b; }"),
        INT, false);
    ArrowType arrowType17 = new ArrowType(registry,
        registry.parseJsFunctionType("function(a: number, b: number) { return b; }"),
        INT, false);
    assertTrue(arrowType16.hasEqualParameters(arrowType17));
  }

  @Test
  public void testNewArrowTypeWithVarArgs() {
    Node arrowFunctionNode = registry.parseJsFunctionType(
        "function(a, ...b) { return b[0]; }");
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getParameters(),
        INT, false);

    assertEquals("function(a, ...b) { return b[0]; }", arrowType.toString());
  }

  @Test
  public void testNewArrowTypeWithInferredReturnType() {
    Node arrowFunctionNode = registry.parseJsFunctionType(
        "function(a) { return a; }");
    ArrowType arrowType = new ArrowType(registry, arrowFunctionNode.getParameters(),
        null, true);

    assertEquals("function(a) { return a; }", arrowType.toString());
  }

  @Test
  public void testNewArrowTypeWithMismatchedParameters() {
    Node arrowFunctionNode = registry.parseJsFunctionType(
        "function(a: number, b: string) { return b; }");
    try {
      new ArrowType(registry, arrowFunctionNode.getParameters(), INT, false);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Error creating arrow type: Types mismatch", e.getMessage());
    }
  }

  @Test
  public void testNewArrowTypeWithInvalidParameters() {
    Node arrowFunctionNode = registry.parseJsFunctionType("function() { }");
    try {
      new ArrowType(registry, arrowFunctionNode.getParameters(), INT, false);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Function type has no parameters", e.getMessage());
    }

    Node arrowFunctionNode2 = registry.parseJsFunctionType(
        "function(x: number, y: number) { return x + y; }");
    try {
      new ArrowType(registry, arrowFunctionNode2.getParameters(), BOOL, false);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Function type returns a boolean, but the arrow type is inferred to have an int return type", e.getMessage());
    }
  }
}