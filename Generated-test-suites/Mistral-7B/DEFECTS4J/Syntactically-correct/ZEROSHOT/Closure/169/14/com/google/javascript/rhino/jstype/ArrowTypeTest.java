import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void error(String message, int lineNumber, int lineOffset) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        System.out.println(message);
      }

      @Override
      public void warning(String message, int lineNumber, int lineOffset) {
        System.out.println(message);
      }
    };
  }

  @Test
  public void testCreation() {
    Node parameters = NodeFactory.createList();
    Node functionTypeNode = NodeFactory.createFunctionType(parameters,
        registry.getType(Number.class), registry.getType(Number.class));
    ArrowType arrowType = new ArrowType(registry, parameters, registry.getType(String.class));
    ArrowType arrowTypeFromFunctionType = new ArrowType(registry, parameters,
        functionTypeNode);

    assertEquals(arrowType, arrowTypeFromFunctionType);
  }

  @Test
  public void testSubtype() {
    JSType type1 = new ArrowType(registry, NodeFactory.createList(),
        registry.getType(Number.class), true);
    JSType type2 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(String.class), false);

    assertFalse(type1.isSubtype(type2));

    Node param1 = NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
        type1);
    Node param2 = NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
        type2);

    Node paramList1 = NodeFactory.createList(param1);
    Node paramList2 = NodeFactory.createList(param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList1,
        registry.getType(Number.class), true);
    ArrowType arrowType2 = new ArrowType(registry, paramList2,
        registry.getType(String.class), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testEqualParameters() {
    JSType type1 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), true);

    JSType type2 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(String.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), true);

    assertTrue(type1.hasEqualParameters(type2, true));

    Node param1 = NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
        registry.createTypeAnnotation(registry.getType(Number.class),
            UNKNOWN_TYPE));
    Node param2 = NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
        registry.createTypeAnnotation(registry.getType(String.class),
            UNKNOWN_TYPE));

    Node paramList1 = NodeFactory.createList(param1);
    Node paramList2 = NodeFactory.createList(param2);

    ArrowType arrowType1 = new ArrowType(registry, paramList1,
        registry.getType(Number.class), true);
    ArrowType arrowType2 = new ArrowType(registry, paramList2,
        registry.getType(Number.class), true);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));

    ArrowType arrowType3 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(String.class), true);

    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
  }

  @Test
  public void testHashCode() {
    JSType type1 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), true);

    JSType type2 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(String.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), true);

    assertEquals(type1.hashCode(), type2.hashCode());

    JSType type3 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(String.class), false);

    assertNotEquals(type1.hashCode(), type3.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException("Method not supported yet.");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException("Method not supported yet.");
  }

  @Test
  public void testToString() {
    JSType type = new ArrowType(registry, NodeFactory.createList(),
        registry.getType(Number.class), false);

    assertEquals("[ArrowType]", type.toStringHelper(false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    JSType type1 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), true);

    JSType type2 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(String.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), true);

    assertTrue(type1.checkArrowEquivalenceHelper(type2, true));

    JSType type3 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(String.class), true);

    assertFalse(type1.checkArrowEquivalenceHelper(type3, true));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    JSType type1 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            registry.createTypeAnnotation(registry.getType(Number.class),
                UNKNOWN_TYPE))),
        registry.getType(Number.class), false);

    assertFalse(type1.hasAnyTemplateInternal());

    JSType type2 = new ArrowType(registry, NodeFactory.createList(
        NodeFactory.createTypeAnnotation(NodeFactory.createIdentifier("a"),
            NodeFactory.createTemplateAnnotation(
                NodeFactory.createList(NodeFactory.createIdentifier("T"))))),
        registry.getType(Number.class), false);

    assertTrue(type2.hasAnyTemplateInternal());
  }
}