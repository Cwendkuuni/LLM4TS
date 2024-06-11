import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Test;

public class ArrowTypeTest {

  private static ErrorReporter t = new ErrorReporter();
  private static JSTypeRegistry registry = new JSTypeRegistry(t);

  @Test
  public void testArrowTypeConstruction() {
    Node parameters = Node.newList();
    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(returnType, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeWithNullParameterAndReturnTypes() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.getReturnType());
    assertNull(arrowType.getParameters());
  }

  @Test
  public void testArrowTypeWithEmptyParameterList() {
    Node parameters = Node.newList();
    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(returnType, arrowType.getReturnType());
    assertNull(arrowType.getParameters());
  }

  @Test
  public void testArrowTypeWithParameterList() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"),
        Node.newIdentifier("p3"));

    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(returnType, arrowType.getReturnType());
    assertNotNull(arrowType.getParameters());
    assertEquals(3, arrowType.getParameters().getChildCount());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"));

    ArrowType arrowType = new ArrowType(registry, parameters, null);
    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());

    Node returnNode = Node.newReturnStatement(Node.newStringLiteral(""));
    JSType returnType = returnNode.getJSType(registry, t, new StaticScope<JSType>(registry));
    arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(returnType, arrowType.getReturnType());
    assertTrue(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeIsSubtypeOfArrowType() {
    Node parameters1 = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType1 = registry.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"),
        Node.newIdentifier("p3"));

    JSType returnType2 = registry.createBooleanType();
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeIsSubtypeOfFunctionType() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType functionType = registry.createFunctionType(
        registry.createParameters(Node.newList(), false),
        registry.createStringType());
    assertFalse(arrowType.isSubtype(functionType));
    assertFalse(functionType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node parameters1 = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType1 = registry.createNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType2 = registry.createBooleanType();
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType4 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType4, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
  }

  @Test
  public void testGetHashCode() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType1 = registry.createNumberType();
    JSType returnType2 = registry.createBooleanType();

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parameters, returnType1);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();

    assertEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testTernaryValue() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testToString() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    String toString = arrowType.toStringHelper(false);
    assertEquals("[ArrowType]", toString);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"));

    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasAnyTemplateInternal());

    JSType templatedReturnType = registry.createTemplateType(
        Node.newIdentifier("T"),
        registry.createFunctionType(
            Node.newList(),
            registry.createStringType()));

    arrowType = new ArrowType(registry, parameters, templatedReturnType);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node parameters = Node.newList(
        Node.newIdentifier("p1"),
        Node.newIdentifier("p2"));

    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    JSType unknownType = registry.getFactory().getUnknownType();

    arrowType = new ArrowType(registry, parameters, unknownType);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
}