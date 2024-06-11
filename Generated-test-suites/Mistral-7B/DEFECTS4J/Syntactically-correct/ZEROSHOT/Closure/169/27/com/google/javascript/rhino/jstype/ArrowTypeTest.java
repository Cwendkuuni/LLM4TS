import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowTypeNode;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.JSType.BOOLEAN;
import static com.google.javascript.rhino.JSType.INT;
import static com.google.javascript.rhino.JSType.STRING;
import static com.google.javascript.rhino.JSType.UNDEFINED;
import static com.google.javascript.rhino.JSType.VOID;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeCreation() {
    Node arrowTypeNode = createArrowTypeNode(createJSType(INT), createJSType(STRING));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, intType, false);
    assertEquals(INT, arrowType.getReturnType());
    assertEquals(STRING, arrowType.parameters.getFirstChild().getJSType());
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    Node arrowTypeNode = createArrowTypeNode(createJSType(INT), null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, true);
    assertNull(arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeCreationWithEmptyParameters() {
    Node arrowTypeNode = createArrowTypeNode(null, null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, false);
    assertNull(arrowType.parameters);
  }

  @Test
  public void testArrowTypeCreationWithVarArgs() {
    Node arrowTypeNode = createArrowTypeNode(createJSType(INT), null);
    arrowTypeNode.setLastChild(createVarArgsNode());
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, false);
    assertEquals(INT.arrayType(), arrowType.parameters.getFirstChild().getJSType());
    Node lastParam = arrowType.parameters.getLastChild();
    assertNotNull(lastParam);
    assertEquals(JSTypeNative.VAR_ARGS_TYPE, lastParam.getJSType());
  }

  @Test
  public void testArrowTypeCreationWithOptionalArgs() {
    Node arrowTypeNode = createArrowTypeNode(
        createArrowTypeNode(createJSType(INT), null),
        createJSType(STRING));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, false);
    Node firstParam = arrowType.parameters.getFirstChild();
    Node secondParam = arrowType.parameters.getLastChild();
    assertEquals(INT.arrayType(), firstParam.getFirstChild().getJSType());
    assertEquals(JSTypeNative.OPTIONAL_ARG_TYPE, firstParam.getLastChild());
    assertEquals(STRING, secondParam.getJSType());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSType intType = createJSType(INT);
    JSType stringType = createJSType(STRING);
    JSType voidType = createJSType(VOID);
    JSType undefinedType = createJSType(UNDEFINED);
    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType, false);
    ArrowType arrowType4 = new ArrowType(registry, null, voidType, false);
    ArrowType arrowType5 = new ArrowType(registry, null, undefinedType, false);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType2.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertTrue(arrowType3.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType3.isSubtype(arrowType5));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType4.isSubtype(arrowType2));
    assertFalse(arrowType4.isSubtype(arrowType3));
    assertFalse(arrowType4.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType1));
    assertFalse(arrowType5.isSubtype(arrowType2));
    assertFalse(arrowType5.isSubtype(arrowType3));
    assertFalse(arrowType5.isSubtype(arrowType4));
    assertFalse(arrowType5.isSubtype(arrowType5));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSType intType = createJSType(INT);
    JSType stringType = createJSType(STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType, false);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSType intType = createJSType(INT);
    JSType stringType = createJSType(STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();

    ArrowType arrowType4 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType5 = new ArrowType(registry, null, intType, true);
    ArrowType arrowType6 = new ArrowType(registry, null, stringType, false);

    assertEquals(hashCode1, arrowType1.hashCode());
    assertEquals(hashCode1, arrowType4.hashCode());
    assertEquals(hashCode2, arrowType2.hashCode());
    assertEquals(hashCode2, arrowType4.hashCode());
    assertNotEquals(hashCode1, arrowType3.hashCode());
    assertNotEquals(hashCode1, arrowType5.hashCode());
    assertNotEquals(hashCode1, arrowType6.hashCode());
    assertNotEquals(hashCode3, arrowType1.hashCode());
    assertNotEquals(hashCode3, arrowType2.hashCode());
    assertNotEquals(hashCode3, arrowType4.hashCode());
    assertNotEquals(hashCode3, arrowType5.hashCode());
    assertNotEquals(hashCode3, arrowType6.hashCode());
    assertNotEquals(hashCode5, arrowType1.hashCode());
    assertNotEquals(hashCode5, arrowType2.hashCode());
    assertNotEquals(hashCode5, arrowType3.hashCode());
    assertNotEquals(hashCode5, arrowType4.hashCode());
    assertNotEquals(hashCode5, arrowType5.hashCode());
    assertNotEquals(hashCode5, arrowType6.hashCode());
  }

  @Test
  public void testArrowTypeCheckEquivalenceHelper() {
    JSType intType = createJSType(INT);
    JSType stringType = createJSType(STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType, false);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, true));
  }

  @Test
  public void testArrowTypeResolve() {
    JSType intType = createJSType(INT);
    JSType stringType = createJSType(STRING);
    JSType booleanType = createJSType(BOOLEAN);
    Node arrowTypeNode = createArrowTypeNode(intType, stringType);

    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, false);
    arrowType.resolve(errorReporter, new StaticScope<JSType>(registry));

    assertEquals(intType, arrowType.getReturnType());
    assertEquals(stringType, arrowType.parameters.getFirstChild().getJSType());
  }

  @Test
  public void testArrowTypeHasAnyTemplate() {
    JSType intType = createJSType(INT);
    JSType intArrayType = intType.arrayType();
    JSType stringType = createJSType(STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, intArrayType, false);
    ArrowType arrowType3 = new ArrowType(registry, null, stringType, false);

    assertFalse(arrowType1.hasAnyTemplateInternal());
    assertFalse(arrowType2.hasAnyTemplateInternal());
    assertFalse(arrowType3.hasAnyTemplateInternal());

    ArrowType arrowType4 = new ArrowType(registry, null, intType, true);
    assertTrue(arrowType4.hasAnyTemplateInternal());

    ArrowType arrowType5 = new ArrowType(registry, null, intType, false);
    ArrowType arrowType6 = new ArrowType(registry, null, arrowType5, false);
    assertTrue(arrowType6.hasAnyTemplateInternal());
  }

  private Node createVarArgsNode() {
    Node varArgsNode = NodeFactory.createNode(Node.VAR_ARGS_TOKEN);
    varArgsNode.setJSType(JSTypeNative.VAR_ARGS_TYPE);
    return varArgsNode;
  }

  private JSType createJSType(JSType type) {
    return type == null ? UNDEFINED : type;
  }

  private JSTypeRegistry createJSTypeRegistry() {
    return new JSTypeRegistry() {};
  }
}