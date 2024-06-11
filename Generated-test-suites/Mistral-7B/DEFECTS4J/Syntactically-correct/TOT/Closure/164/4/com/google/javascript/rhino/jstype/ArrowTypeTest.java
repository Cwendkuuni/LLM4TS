import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTypeFactory.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

  private static ErrorReporter t = new ErrorReporter();
  private static JSTypeRegistry typeRegistry = new JSTypeRegistry();

  @org.junit.Test
  public void testArrowTypeWithNoParametersAndUnknownReturnType() {
    Node arrowTypeNode = makeArrowTypeNode(null, null);
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, null, false);
    assertEquals(UNKNOWN_TYPE, arrowType.getReturnType());
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test
  public void testArrowTypeWithParametersAndUnknownReturnType() {
    Node arrowTypeNode = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, null, false);
    assertEquals(UNKNOWN_TYPE, arrowType.getReturnType());
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test
  public void testArrowTypeWithNoParametersAndKnownReturnType() {
    Node arrowTypeNode = makeArrowTypeNode(null, makeNamedTypeNode("String"));
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, getStringType(), false);
    assertEquals(getStringType(), arrowType.getReturnType());
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test
  public void testArrowTypeWithParametersAndKnownReturnType() {
    Node arrowTypeNode = makeArrowTypeNode(makeNamedTypeNode("Number"), makeNamedTypeNode("String"));
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, getBooleanType(), false);
    assertEquals(getBooleanType(), arrowType.getReturnType());
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test
  public void testArrowTypeWithNoParametersAndInferredReturnType() {
    Node arrowTypeNode = makeArrowTypeNode(null, null);
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, null, true);
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test
  public void testArrowTypeWithParametersAndInferredReturnType() {
    Node arrowTypeNode = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, null, true);
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test
  public void testArrowTypeSubtyping() {
    Node arrowTypeNode1 = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType1 = new ArrowType(typeRegistry, arrowTypeNode1, getBooleanType(), false);

    Node arrowTypeNode2 = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType2 = new ArrowType(typeRegistry, arrowTypeNode2, getStringType(), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @org.junit.Test
  public void testArrowTypeSubtypingWithDifferentParameters() {
    Node arrowTypeNode1 = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType1 = new ArrowType(typeRegistry, arrowTypeNode1, getBooleanType(), false);

    Node arrowTypeNode2 = makeArrowTypeNode(makeNamedTypeNode("String"), null);
    ArrowType arrowType2 = new ArrowType(typeRegistry, arrowTypeNode2, getStringType(), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @org.junit.Test
  public void testArrowTypeSubtypingWithDifferentReturnTypes() {
    Node arrowTypeNode1 = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType1 = new ArrowType(typeRegistry, arrowTypeNode1, getBooleanType(), false);

    Node arrowTypeNode2 = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType2 = new ArrowType(typeRegistry, arrowTypeNode2, getStringType(), false);

    Node arrowTypeNode3 = makeArrowTypeNode(makeNamedTypeNode("Number"), null);
    ArrowType arrowType3 = new ArrowType(typeRegistry, arrowTypeNode3, getNumberType(), false);

    assertTrue(arrowType1.isSubtype(arrowType3));
    assertTrue(arrowType3.isSubtype(arrowType1));

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  private Node makeArrowTypeNode(Node... types) {
    Node arrowTypeNode = new Node_Call(typeRegistry.getJSType(NativeType.FUNCTION), 2);
    int i = 0;
    for (Node typeNode : types) {
      if (typeNode != null) {
        arrowTypeNode.setChild(i++, typeNode);
      } else {
        arrowTypeNode.setChild(i++, typeRegistry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE)));
      }
    }
    return arrowTypeNode;
  }

  private void resolveTypes(ArrowType arrowType) {
    arrowType.resolveInternal(t, new StaticScope<JSType>(typeRegistry));
  }

  @org.junit.Test
  public void testArrowTypeResolving() {
    Node arrowTypeNode = makeArrowTypeNode(makeNamedTypeNode("Number"), makeNamedTypeNode("String"));
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, getBooleanType(), false);
    resolveTypes(arrowType);
    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    Node arrowTypeNode = makeArrowTypeNode(makeNamedTypeNode("Number"), makeNamedTypeNode("String"));
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, getBooleanType(), false);
    arrowType.getLeastSupertype(getBooleanType());
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    Node arrowTypeNode = makeArrowTypeNode(makeNamedTypeNode("Number"), makeNamedTypeNode("String"));
    ArrowType arrowType = new ArrowType(typeRegistry, arrowTypeNode, getBooleanType(), false);
    arrowType.getGreatestSubtype(getBooleanType());
  }
}