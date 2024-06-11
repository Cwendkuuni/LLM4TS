import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private static final Node EMPTY_NODE_LIST = NodeFactory.emptyList();

  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    registry = new JSTypeRegistry();
    nodeFactory = new NodeFactory(registry, errorReporter);
  }

  @Test
  public void testArrowTypeCreation() {
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    Node parameters = nodeFactory.createList(nodeFactory.createIdentifier("a"));
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("ArrowType[Number]", arrowType.toString());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, EMPTY_NODE_LIST, returnType, true);

    assertEquals("ArrowType[?]", arrowType.toString());
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithEmptyParameters() {
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType = new ArrowType(registry, EMPTY_NODE_LIST, returnType);

    assertEquals("ArrowType[Number]", arrowType.toString());
    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithVarArgs() {
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    Node parameters = nodeFactory.createList(
        nodeFactory.createIdentifier("a"),
        nodeFactory.createList(nodeFactory.createIdentifier("...")));

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("ArrowType[Number, ...]", arrowType.toString());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype() {
    JSType returnType1 = getNativeType(JSTypeNative.STRING);
    JSType returnType2 = getNativeType(JSTypeNative.NUMBER);

    Node parameter1 = nodeFactory.createIdentifier("p1");
    Node parameter2 = nodeFactory.createIdentifier("p2");

    ArrowType arrowType1 = new ArrowType(registry, nodeFactory.createList(parameter1), returnType1);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(parameter2), returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(parameter1), returnType2);

    assertTrue(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));

    ArrowType arrowType4 = new ArrowType(registry, EMPTY_NODE_LIST, returnType1);

    assertTrue(arrowType1.isSubtype(arrowType4));
    assertTrue(arrowType4.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    Node parameter1 = nodeFactory.createIdentifier("p1");
    Node parameter2 = nodeFactory.createIdentifier("p2");

    ArrowType arrowType1 = new ArrowType(registry, nodeFactory.createList(parameter1), getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(parameter2), getNativeType(JSTypeNative.NUMBER));
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(parameter1), getNativeType(JSTypeNative.NUMBER));

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));

    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertTrue(arrowType3.hasEqualParameters(arrowType1));
  }

  @Test
  public void testIsEquivalentTo() {
    JSType returnType1 = getNativeType(JSTypeNative.STRING);
    JSType returnType2 = getNativeType(JSTypeNative.NUMBER);

    Node parameter1 = nodeFactory.createIdentifier("p1");
    Node parameter2 = nodeFactory.createIdentifier("p2");

    ArrowType arrowType1 = new ArrowType(registry, nodeFactory.createList(parameter1), returnType1);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(parameter2), returnType2);
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(parameter1), returnType1);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));

    assertTrue(arrowType1.isEquivalentTo(arrowType3));
    assertTrue(arrowType3.isEquivalentTo(arrowType1));
  }

  @Test
  public void testHashCode() {
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    Node parameter1 = nodeFactory.createIdentifier("p1");
    Node parameter2 = nodeFactory.createIdentifier("p2");

    ArrowType arrowType1 = new ArrowType(registry, nodeFactory.createList(parameter1), returnType);
    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(parameter2), returnType);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    // The return type and the inferred flag should be different.
    assertNotEquals(hashCode1, hashCode2);

    // Changing the order of the parameter types should result in a different hash code.
    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(parameter2, parameter1), returnType);
    assertNotEquals(hashCode1, arrowType3.hashCode());

    // Adding an extra parameter should result in a different hash code.
    ArrowType arrowType4 = new ArrowType(registry, nodeFactory.createList(parameter1, parameter2, nodeFactory.createIdentifier("p3")), returnType);
    assertNotEquals(hashCode1, arrowType4.hashCode());

    // Changing the return type should result in a different hash code.
    ArrowType arrowType5 = new ArrowType(registry, nodeFactory.createList(parameter1), getNativeType(JSTypeNative.STRING));
    assertNotEquals(hashCode1, arrowType5.hashCode());

    // Changing the inferred flag should result in a different hash code.
    ArrowType arrowType6 = new ArrowType(registry, nodeFactory.createList(parameter1), returnType, true);
    assertNotEquals(hashCode1, arrowType6.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @Test
  public void testVisit() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @Test
  public void testToString() {
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    Node parameter1 = nodeFactory.createIdentifier("p1");
    Node parameter2 = nodeFactory.createIdentifier("p2");

    ArrowType arrowType = new ArrowType(registry, nodeFactory.createList(parameter1, parameter2), returnType);

    assertEquals("ArrowType[Number, Number]", arrowType.toString());
  }

  @Test
  public void testResolveInternal() {
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    Node parameter1 = nodeFactory.createIdentifier("p1");

    StaticScope<JSType> scope = mock(StaticScope.class);
    when(scope.lookupType(parameter1)).thenReturn(getNativeType(JSTypeNative.STRING));

    ArrowType arrowType = new ArrowType(registry, nodeFactory.createList(parameter1), returnType);

    // The return type should be resolved.
    assertSame(getNativeType(JSTypeNative.NUMBER), arrowType.resolveInternal(errorReporter, scope).returnType);

    // The parameter type should be resolved.
    assertSame(getNativeType(JSTypeNative.STRING), arrowType.parameters.getFirstChild().getJSType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSType returnType = null;

    ArrowType arrowType1 = new ArrowType(registry, EMPTY_NODE_LIST, returnType);
    assertFalse(arrowType1.hasUnknownParamsOrReturn());

    ArrowType arrowType2 = new ArrowType(registry, nodeFactory.createList(nodeFactory.createIdentifier("p1")), returnType);
    assertFalse(arrowType2.hasUnknownParamsOrReturn());

    ArrowType arrowType3 = new ArrowType(registry, nodeFactory.createList(nodeFactory.createIdentifier("p1")), getNativeType(JSTypeNative.NUMBER));
    assertFalse(arrowType3.hasUnknownParamsOrReturn());

    ArrowType arrowType4 = new ArrowType(registry, nodeFactory.createList(nodeFactory.createIdentifier("p1")), null);
    assertFalse(arrowType4.hasUnknownParamsOrReturn());

    ArrowType arrowType5 = new ArrowType(registry, nodeFactory.createList(nodeFactory.createIdentifier("p1")), getNativeType(JSTypeNative.NUMBER), true);
    assertFalse(arrowType5.hasUnknownParamsOrReturn());

    ArrowType arrowType6 = new ArrowType(registry, nodeFactory.createList(nodeFactory.createIdentifier("p1"), nodeFactory.createIdentifier("p2")), null);
    assertTrue(arrowType6.hasUnknownParamsOrReturn());
  }
}