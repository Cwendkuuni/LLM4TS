import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.JSTypeFactory.createUnknownType;
import static com.google.javascript.rhino.JSTypeFactory.createVarArgs;
import static com.google.javascript.rhino.JSTypeFactory.createVoidType;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
  }

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    Node parameters = registry.createList(
        registry.createIdentifier("x"),
        registry.createIdentifier("y"));
    JSType returnType = createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowTypeExpected = new ArrowType(registry, parameters, returnType, false);
    assertEquals(arrowTypeExpected, arrowType);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node parameters = registry.createList(registry.createIdentifier("x"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    ArrowType arrowTypeExpected = new ArrowType(registry, parameters, createUnknownType(), true);
    assertEquals(arrowTypeExpected, arrowType);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node parameters = registry.createList(registry.createIdentifier("x"), createVarArgs());
    JSType returnType = createVoidType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowTypeExpected = new ArrowType(registry, parameters, returnType, false);
    assertEquals(arrowTypeExpected, arrowType);
  }

  @Test
  public void testArrowTypeEquivalence() {
    Node parameters = registry.createList(
        registry.createIdentifier("x"),
        registry.createIdentifier("y"));
    JSType returnType = createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeWithDifferentParameters() {
    Node parameters1 = registry.createList(
        registry.createIdentifier("x"),
        registry.createIdentifier("y"));
    Node parameters2 = registry.createList(
        registry.createIdentifier("x"),
        registry.createIdentifier("z"));
    JSType returnType = createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeWithMissingParameters() {
    Node parameters1 = registry.createList(
        registry.createIdentifier("x"),
        registry.createIdentifier("y"));
    Node parameters2 = registry.createList(registry.createIdentifier("x"));
    JSType returnType = createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidParameters() {
    Node parameters = registry.createList(
        registry.createIdentifier("x"),
        registry.createNumberLiteral(1));
    JSType returnType = createNumberType();

    new ArrowType(registry, parameters, returnType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidReturnType() {
    Node parameters = registry.createList(registry.createIdentifier("x"));
    JSType returnType = createFunctionType(createUnknownType(), createUnknownType());

    new ArrowType(registry, parameters, returnType);
  }

  @Test
  public void testArrowTypeToString() {
    Node parameters = registry.createList(
        registry.createIdentifier("x"),
        registry.createIdentifier("y"));
    JSType returnType = createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    String expected = "[ArrowType]";
    String actual = arrowType.toStringHelper(false);
    assertEquals(expected, actual);
  }
}