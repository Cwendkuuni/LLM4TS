import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowTypeNode;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createParameterListNode;
import static com.google.javascript.rhino.NodeFactory.createTypeLiteralNode;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    typeRegistry = createJSTypeRegistry(errorReporter);
  }

  @Test
  public void testArrowTypeCreation() {
    Node functionNode = createArrowTypeNode(createParameterListNode(),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType = new ArrowType(typeRegistry, null, null, false);
    arrowType.resolveInternal(errorReporter, null);
    arrowType.setNode(functionNode);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithParameters() {
    Node functionNode = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType = new ArrowType(typeRegistry, null, null, false);
    arrowType.resolveInternal(errorReporter, null);
    arrowType.setNode(functionNode);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNotNull(arrowType.parameters);
    assertEquals(2, arrowType.parameters.getChildCount());
    assertNotNull(arrowType.returnType);
    assertEquals("String", arrowType.returnType.getClass().getSimpleName());
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    Node functionNode = createArrowTypeNode(createParameterListNode(), null);
    ArrowType arrowType = new ArrowType(typeRegistry, null, null, true);
    arrowType.resolveInternal(errorReporter, null);
    arrowType.setNode(functionNode);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeCreationWithMultipleParameters() {
    Node functionNode = createArrowTypeNode(createParameterListNode(
        createTypeLiteralNode(Integer.TYPE),
        createTypeLiteralNode(Float.TYPE),
        createTypeLiteralNode(Boolean.TYPE),
        createTypeLiteralNode(createArrayTypeNode(Boolean.TYPE, 5))),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType = new ArrowType(typeRegistry, null, null, false);
    arrowType.resolveInternal(errorReporter, null);
    arrowType.setNode(functionNode);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNotNull(arrowType.parameters);
    assertEquals(4, arrowType.parameters.getChildCount());
    assertNotNull(arrowType.returnType);
    assertEquals("String", arrowType.returnType.getClass().getSimpleName());
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeEquality() {
    Node functionNode1 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    Node functionNode2 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType1 = new ArrowType(typeRegistry, null, null, false);
    ArrowType arrowType2 = new ArrowType(typeRegistry, null, null, false);

    arrowType1.resolveInternal(errorReporter, null);
    arrowType1.setNode(functionNode1);
    arrowType2.resolveInternal(errorReporter, null);
    arrowType2.setNode(functionNode2);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertTrue(arrowType2.checkArrowEquivalenceHelper(arrowType1, false));
  }

  @Test
  public void testArrowTypeParameterTypeEquality() {
    Node functionNode1 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class), createTypeLiteralNode(Double.TYPE)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    Node functionNode2 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class), createTypeLiteralNode(Float.TYPE)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType1 = new ArrowType(typeRegistry, null, null, false);
    ArrowType arrowType2 = new ArrowType(typeRegistry, null, null, false);

    arrowType1.resolveInternal(errorReporter, null);
    arrowType1.setNode(functionNode1);
    arrowType2.resolveInternal(errorReporter, null);
    arrowType2.setNode(functionNode2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertFalse(arrowType2.checkArrowEquivalenceHelper(arrowType1, false));
  }

  @Test
  public void testArrowTypeParameterTypeEqualityWithUnknownTypes() {
    Node functionNode1 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(Node.UNKNOWN_TYPE), createTypeLiteralNode(Double.TYPE)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    Node functionNode2 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(Node.UNKNOWN_TYPE), createTypeLiteralNode(Float.TYPE)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType1 = new ArrowType(typeRegistry, null, null, false);
    ArrowType arrowType2 = new ArrowType(typeRegistry, null, null, false);

    arrowType1.resolveInternal(errorReporter, null);
    arrowType1.setNode(functionNode1);
    arrowType2.resolveInternal(errorReporter, null);
    arrowType2.setNode(functionNode2);

    // Tolerating unknown types should make both arrow types equal
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    assertTrue(arrowType2.checkArrowEquivalenceHelper(arrowType1, true));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node functionNode = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createParameterListNode(createTypeLiteralNode(Integer.TYPE))),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType = new ArrowType(typeRegistry, null, null, false);
    arrowType.resolveInternal(errorReporter, null);
    arrowType.setNode(functionNode);

    assertEquals(2, arrowType.parameters.getChildCount());
    assertFalse(arrowType.parameters.getLastChild().isOptionalArg());
    assertTrue(arrowType.parameters.getLastChild().isVarArgs());

    Node varArgsParam = arrowType.parameters.getLastChild();
    Node firstParam = arrowType.parameters.getFirstChild();

    assertEquals(Integer.TYPE, firstParam.getJSType().getType());
    assertNull(firstParam.getJSType().getBoundName());
    assertEquals("...", varArgsParam.getString());
    assertNull(varArgsParam.getJSType());
  }

  @Test
  public void testArrowTypeWithOptionalParams() {
    Node functionNode = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class), createTypeLiteralNode(Boolean.TYPE),
            createParameterListNode(createTypeLiteralNode(Node.UNKNOWN_TYPE))),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType = new ArrowType(typeRegistry, null, null, false);
    arrowType.resolveInternal(errorReporter, null);
    arrowType.setNode(functionNode);

    assertEquals(4, arrowType.parameters.getChildCount());
    Node firstParam = arrowType.parameters.getFirstChild();
    Node lastParam = arrowType.parameters.getLastChild();
    assertEquals(Integer.TYPE, firstParam.getJSType().getType());
    assertEquals("param1", firstParam.getJSType().getBoundName());
    assertNull(lastParam.getJSType());
    assertTrue(lastParam.isOptionalArg());
  }

  @Test
  public void testArrowTypeHashCode() {
    Node functionNode1 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    Node functionNode2 = createArrowTypeNode(
        createParameterListNode(createTypeLiteralNode(Integer.TYPE),
            createTypeLiteralNode(String.class)),
        createTypeLiteralNode(new JSTypeNative.ObjectType("String")));
    ArrowType arrowType1 = new ArrowType(typeRegistry, null, null, false);
    ArrowType arrowType2 = new ArrowType(typeRegistry, null, null, false);

    arrowType1.resolveInternal(errorReporter, null);
    arrowType1.setNode(functionNode1);
    arrowType2.resolveInternal(errorReporter, null);
    arrowType2.setNode(functionNode2);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }
}