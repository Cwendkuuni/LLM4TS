import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.testing.AssertTypes;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithSingleParamAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node("(x) -> x");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), JSTypeNative.NUMBER);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSType.FUNCTION, arrowType.getJSType(registry).getType());
    assertEquals(JSTypeNative.NUMBER, arrowType.returnType);
  }

  @Test
  public void testArrowTypeWithSingleParamAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node("(x) -> x");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), null, true);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSType.FUNCTION, arrowType.getJSType(registry).getType());
    assertNull(arrowType.returnType);
  }

  @Test
  public void testArrowTypeWithMultipleParamsAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node("(x, y) -> x + y");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), JSTypeNative.NUMBER);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSType.FUNCTION, arrowType.getJSType(registry).getType());
    assertEquals(JSTypeNative.NUMBER, arrowType.returnType);
  }

  @Test
  public void testArrowTypeWithMultipleParamsAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node("(x, y) -> x + y");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), null, true);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSType.FUNCTION, arrowType.getJSType(registry).getType());
    assertNull(arrowType.returnType);
  }

  @Test(expected = AssertionError.class)
  public void testArrowTypeWithInvalidParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node("(x) -> y");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), JSTypeNative.NUMBER);

    fail("Expected an error when creating an ArrowType with an invalid parameter type.");
  }

  @Test
  public void testArrowTypeEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node1 = new Node("(x) -> x");
    Node node2 = new Node("(x) -> x");
    ArrowType arrowType1 = new ArrowType(registry, node1.getFirstChild(), JSTypeNative.NUMBER);
    ArrowType arrowType2 = new ArrowType(registry, node2.getFirstChild(), JSTypeNative.NUMBER);

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeInequality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node1 = new Node("(x) -> x");
    Node node2 = new Node("(y) -> y");
    ArrowType arrowType1 = new ArrowType(registry, node1.getFirstChild(), JSTypeNative.NUMBER);
    ArrowType arrowType2 = new ArrowType(registry, node2.getFirstChild(), JSTypeNative.NUMBER);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node1 = new Node("(x) -> x");
    Node node2 = new Node("(x, y) -> x");
    ArrowType arrowType1 = new ArrowType(registry, node1.getFirstChild(), JSTypeNative.NUMBER);
    ArrowType arrowType2 = new ArrowType(registry, node2.getFirstChild(), JSTypeNative.NUMBER, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeSubtypingWithIncompatibleReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node1 = new Node("(x) -> x");
    Node node2 = new Node("(x) -> y");
    ArrowType arrowType1 = new ArrowType(registry, node1.getFirstChild(), JSTypeNative.NUMBER);
    ArrowType arrowType2 = new ArrowType(registry, node2.getFirstChild(), JSTypeNative.STRING);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node("(...args) -> args.join('')");
    ArrowType arrowType = new ArrowType(registry, node.getFirstChild(), JSTypeNative.STRING);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
    assertEquals(JSType.FUNCTION, arrowType.getJSType(registry).getType());
    assertEquals(JSTypeNative.ARRAY, arrowType.parameters.getType());
    assertEquals(JSTypeNative.STRING, arrowType.returnType);
  }
}