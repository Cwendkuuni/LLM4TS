import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {
  @org.junit.Test
  public void testArrowTypeWithParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowTypeNode = new Node("ArrowType", new Node("Parameters", null),
        new Node("ReturnType", new Node("IntType")));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild(),
        arrowTypeNode.getLastChild().getFirstChild());

    JSType expectedType = new ArrowType(registry, null, new IntType(), false);
    assertEquals(expectedType, arrowType);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowTypeNode = new Node("ArrowType", new Node("Parameters", null), null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild(), null);
  }

  @org.junit.Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowTypeNode = new Node("ArrowType", new Node("Parameters", null), null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild(), null, true);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowTypeNode = new Node("ArrowType", null, new Node("ReturnType", null));
    ArrowType arrowType = new ArrowType(registry, null, arrowTypeNode.getFirstChild(), false);
  }

  @org.junit.Test
  public void testArrowTypeWithEmptyParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowTypeNode = new Node("ArrowType", new Node("Parameters", new Node("")), new Node("VoidType"));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild(), null, false);
  }

  @org.junit.Test
  public void testArrowTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node arrowTypeNode1 = new Node("ArrowType",
        new Node("Parameters", new Node("IntType"), new Node("StringType")),
        new Node("ReturnType", new Node("IntType")));
    Node arrowTypeNode2 = new Node("ArrowType",
        new Node("Parameters", new Node("StringType"), new Node("StringType")),
        new Node("ReturnType", new Node("IntType")));

    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild(),
        arrowTypeNode1.getLastChild().getFirstChild());
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild(),
        arrowTypeNode2.getLastChild().getFirstChild());

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertEquals(arrowType1, arrowType2);
  }
}