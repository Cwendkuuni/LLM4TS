import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.TestUtilities;

public class ArrowTypeTest {

  @Test
  public void testNewArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowNode = TestUtilities.parseNode("function(x) { return y; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), null);

    assertEquals("Expected ArrowType object", ArrowType.class, arrowType.getClass());
    assertEquals("Expected ArrowType instance", arrowType, registry.getType(arrowNode));

    assertEquals("Expected 1 parameter", 1, arrowType.parameters().getChildCount());
    assertEquals("Expected parameter type to be UNKNOWN_TYPE", UNKNOWN_TYPE,
        arrowType.parameters().getChild(0).getJSType());

    assertNull("Expected no explicit return type", arrowType.returnType);
    assertFalse("Expected return type to be inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testNewArrowTypeWithExplicitReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowNode = TestUtilities.parseNode("function(x) { return 5; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(),
        IntType.getInstance(registry));

    assertEquals("Expected ArrowType object", ArrowType.class, arrowType.getClass());
    assertEquals("Expected ArrowType instance", arrowType, registry.getType(arrowNode));

    assertEquals("Expected 1 parameter", 1, arrowType.parameters().getChildCount());
    assertEquals("Expected parameter type to be UNKNOWN_TYPE", UNKNOWN_TYPE,
        arrowType.parameters().getChild(0).getJSType());

    assertEquals("Expected IntType return type", IntType.class, arrowType.returnType.getClass());
    assertFalse("Expected return type to be inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testNewArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowNode = TestUtilities.parseNode("function(x) { return x * x; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), null);

    assertEquals("Expected ArrowType object", ArrowType.class, arrowType.getClass());
    assertEquals("Expected ArrowType instance", arrowType, registry.getType(arrowNode));

    assertEquals("Expected 1 parameter", 1, arrowType.parameters().getChildCount());
    assertEquals("Expected parameter type to be UNKNOWN_TYPE", UNKNOWN_TYPE,
        arrowType.parameters().getChild(0).getJSType());

    assertNull("Expected no explicit return type", arrowType.returnType);
    assertTrue("Expected return type to be inferred", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Simple ArrowType subtyping test
    ArrowType arrowType1 = new ArrowType(registry, null, IntType.getInstance(registry), false);
    ArrowType arrowType2 = new ArrowType(registry, null, IntType.getInstance(registry), false);
    assertTrue("Expected arrowType1 to be a subtype of arrowType2", arrowType1.isSubtype(arrowType2));

    // ArrowType with different return types
    ArrowType arrowType3 = new ArrowType(registry, null, IntType.getInstance(registry), false);
    ArrowType arrowType4 = new ArrowType(registry, null, StringType.getInstance(registry), false);
    assertFalse("Expected arrowType3 to not be a subtype of arrowType4", arrowType3.isSubtype(arrowType4));

    // ArrowType with different parameter types
    ArrowType arrowType5 = new ArrowType(registry, null, IntType.getInstance(registry), false);
    ArrowType arrowType6 = new ArrowType(registry, new Node("function(x, y) {}"), StringType.getInstance(registry), false);
    assertFalse("Expected arrowType5 to not be a subtype of arrowType6", arrowType5.isSubtype(arrowType6));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode1 = TestUtilities.parseNode("function(x, y) { return x + y; }");
    ArrowType arrowType1 = new ArrowType(registry, arrowNode1.parameters(), null, false);

    Node arrowNode2 = TestUtilities.parseNode("function(x, y) { return x + y; }");
    ArrowType arrowType2 = new ArrowType(registry, arrowNode2.parameters(), null, false);

    Node arrowNode3 = TestUtilities.parseNode("function(y, x) { return x + y; }");
    ArrowType arrowType3 = new ArrowType(registry, arrowNode3.parameters(), null, false);

    Node arrowNode4 = TestUtilities.parseNode("function(x, z) { return x + z; }");
    ArrowType arrowType4 = new ArrowType(registry, arrowNode4.parameters(), null, false);

    assertTrue("Expected arrowType1 and arrowType2 to have equal parameters", arrowType1.hasEqualParameters(arrowType2));
    assertFalse("Expected arrowType1 and arrowType3 to have unequal parameters", arrowType1.hasEqualParameters(arrowType3));
    assertFalse("Expected arrowType1 and arrowType4 to have unequal parameters", arrowType1.hasEqualParameters(arrowType4));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode1 = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType1 = new ArrowType(registry, arrowNode1.parameters(), IntType.getInstance(registry), false);

    Node arrowNode2 = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType2 = new ArrowType(registry, arrowNode2.parameters(), IntType.getInstance(registry), false);

    Node arrowNode3 = TestUtilities.parseNode("function(y) { return y + 5; }");
    ArrowType arrowType3 = new ArrowType(registry, arrowNode3.parameters(), IntType.getInstance(registry), false);

    assertTrue("Expected arrowType1 and arrowType2 to be equivalent", arrowType1.isEquivalentTo(arrowType2));
    assertFalse("Expected arrowType1 and arrowType3 to be unequivalent", arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode1 = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType1 = new ArrowType(registry, arrowNode1.parameters(), IntType.getInstance(registry), false);

    Node arrowNode2 = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType2 = new ArrowType(registry, arrowNode2.parameters(), IntType.getInstance(registry), false);

    Node arrowNode3 = TestUtilities.parseNode("function(y) { return y + 5; }");
    ArrowType arrowType3 = new ArrowType(registry, arrowNode3.parameters(), IntType.getInstance(registry), false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();

    assertEquals("Expected hashCode1 and hashCode2 to be equal", hashCode1, hashCode2);
    assertNotEquals("Expected hashCode1 and hashCode3 to be different", hashCode1, hashCode3);
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), IntType.getInstance(registry), false);

    String expected = "ArrowType(function(x) {return x}, Int)";
    String actual = arrowType.toString();

    assertEquals("Expected toString to return expected string", expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), IntType.getInstance(registry), false);

    arrowType.getLeastSupertype(ObjectType.getInstance(registry));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), IntType.getInstance(registry), false);

    arrowType.getGreatestSubtype(IntType.getInstance(registry));
  }

  @Test
  public void testArrowTypeTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode1 = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType1 = new ArrowType(registry, arrowNode1.parameters(), IntType.getInstance(registry), false);

    Node arrowNode2 = TestUtilities.parseNode("function(y) { return y + 5; }");
    ArrowType arrowType2 = new ArrowType(registry, arrowNode2.parameters(), IntType.getInstance(registry), false);

    TernaryValue result = arrowType1.testForEquality(arrowType2);

    assertFalse("Expected testForEquality to return false", result.getValue());
  }

  @Test
  public void testArrowTypeVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), IntType.getInstance(registry), false);

    JSType expected = VoidType.VOID_TYPE;
    JSType actual = arrowType.visit(new TestVisitor());

    assertEquals("Expected visit to return expected type", expected, actual);
  }

  @Test
  public void testArrowTypeGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x) { return x > 0 ? 1 : 0; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), BoolType.BOOLEAN_TYPE, false);

    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();

    assertEquals("Expected getPossibleToBooleanOutcomes to return BooleanLiteralSet containing 'true'", BooleanLiteralSet.TRUE, result);
  }

  @Test
  public void testArrowTypeResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x) { return x + 5; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), IntType.getInstance(registry), false);

    assertNull("Expected returnType to be null before resolution", arrowType.returnType);

    Node functionNode = TestUtilities.parseNode("function foo() { return 5; }");
    JSType fooType = registry.getType(functionNode);

    assertFalse("Expected returnTypeInferred to be false before resolution", arrowType.returnTypeInferred);

    arrowType.resolveInternal(TestUtilities.getErrorReporter(), null);

    assertEquals("Expected IntType as resolved return type", IntType.class, arrowType.returnType.getClass());
    assertTrue("Expected returnTypeInferred to be true after resolution", arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowNode = TestUtilities.parseNode("function(x, y) { return x + y; }");
    ArrowType arrowType = new ArrowType(registry, arrowNode.parameters(), null, false);

    assertTrue("Expected arrowType to have unknown parameters or return type", arrowType.hasUnknownParamsOrReturn());

    Node arrowNode2 = TestUtilities.parseNode("function foo() { return 5; }");
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);

    assertTrue("Expected arrowType2 to have unknown parameters and return type", arrowType2.hasUnknownParamsOrReturn());
  }
}

class TestVisitor implements Visitor<JSType> {
  @Override
  public JSType visitArrowType(ArrowType arrowType) {
    return VoidType.VOID_TYPE;
  }
}