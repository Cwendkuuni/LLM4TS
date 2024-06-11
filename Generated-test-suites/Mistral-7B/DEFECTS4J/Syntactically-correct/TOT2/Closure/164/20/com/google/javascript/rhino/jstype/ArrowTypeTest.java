import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestSuite.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.UnknownType;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter errorReporter;
  JSTypeRegistry registry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeBasic() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), new Node("a"), new Node("int"));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), new IntType(registry));

    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeSubtype() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("a"), new Node("number"));

    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new NumberType(registry));

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeNotSubtype() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("b"), new Node("number"));

    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new NumberType(registry));

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), new Node("a"), null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), null, true);

    Node functionNode = createFunctionTypeNode(new Node("function"), new Node("a"), new Node("int"), null);
    JSType functionType = new FunctionType(registry, functionNode.getFirstChild(), new IntType(registry), null);

    assertTrue(arrowType.isSubtype(functionType));
  }

  @Test
  public void testArrowTypeWithUnknownReturnType() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), new Node("a"), null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), null, true);

    assertTrue(arrowType.isSubtype(new UnknownType(registry)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), new Node("a"), new Node("int"));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), new IntType(registry));

    arrowType.getLeastSupertype(new IntType(registry));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), new Node("a"), new Node("int"));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), new IntType(registry));

    arrowType.getGreatestSubtype(new FunctionType(registry, null, new IntType(registry), null));
  }

  @Test
  public void testEquality() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("a"), new Node("int"));
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new IntType(registry));

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testInequality() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("b"), new Node("int"));
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new IntType(registry));

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCode() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("a"), new Node("int"));
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new IntType(registry));

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testEqualityWithUnknownReturnType() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), null);
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("a"), null);
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), null, true);
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), null, true);

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testInequalityWithUnknownReturnType() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), null);
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("b"), null);
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), null, true);
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), null, true);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("a"), new Node("int"));
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new IntType(registry));

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testNoEqualParameters() {
    Node arrowTypeNode1 = createArrowTypeNode(new Node("arrowType1"), new Node("a"), new Node("int"));
    Node arrowTypeNode2 = createArrowTypeNode(new Node("arrowType2"), new Node("b"), new Node("int"));
    ArrowType arrowType1 = new ArrowType(registry, arrowTypeNode1.getFirstChild().getNext(), new IntType(registry));
    ArrowType arrowType2 = new ArrowType(registry, arrowTypeNode2.getFirstChild().getNext(), new IntType(registry));

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeWithUnknownParameters() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), null, null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), null, true);

    assertTrue(arrowType.isSubtype(new UnknownType(registry)));
  }

  @Test
  public void testArrowTypeWithUnknownReturnAndParameters() {
    Node arrowTypeNode = createArrowTypeNode(new Node("arrowType"), null, null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.getFirstChild().getNext(), null, true);

    assertTrue(arrowType.isSubtype(new UnknownType(registry)));
  }

  private Node createArrowTypeNode(Node name, Node parameters, Node returnType) {
    Node arrowTypeNode = new Node("arrowType");
    arrowTypeNode.setJSType(new ArrowType(registry, null, parameters, returnType));
    arrowTypeNode.setProperty("name", name);
    return arrowTypeNode;
  }

  private Node createFunctionTypeNode(Node name, Node parameters, Node returnType, Node varargs) {
    Node functionTypeNode = new Node("function");
    Node functionType = new FunctionType(registry, parameters, returnType, varargs);
    functionTypeNode.setJSType(functionType);
    functionTypeNode.setProperty("name", name);
    return functionTypeNode;
  }
}