import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.JSTestCase;

public class ArrowTypeTest extends JSTestCase {

  private static final Node PARAMETERS_WITH_VAR_ARGS = createNode(
      "function(a, b, c) { }", 0);
  private static final Node PARAMETERS = createNode(
      "function(a, b) { }", 0);

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, null);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, null);

    assertEquals(arrowType1.getClass(), ArrowType.class);
    assertEquals(arrowType1.toString(), "[ArrowType]");
    assertEquals(arrowType1.getParameters().length(), 2);
    assertNull(arrowType1.getReturnType());

    assertEquals(arrowType2.getClass(), ArrowType.class);
    assertEquals(arrowType2.toString(), "[ArrowType]");
    assertEquals(arrowType2.getParameters().length(), 3);
    assertNull(arrowType2.getReturnType());
  }

  @Test
  public void testArrowTypeCreationWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType numberType = registry.getTypeOrCreate(Int.class);
    JSType arrowType = new ArrowType(registry, PARAMETERS, numberType);

    assertEquals(arrowType.getClass(), ArrowType.class);
    assertEquals(arrowType.toString(), "[ArrowType]");
    assertEquals(arrowType.getParameters().length(), 2);
    assertEquals(arrowType.getReturnType().getClass(), numberType.getClass());
  }

  @Test
  public void testArrowTypeCreationWithReturnTypeInferred() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType arrowType = new ArrowType(registry, PARAMETERS, null, true);

    assertEquals(arrowType.getClass(), ArrowType.class);
    assertEquals(arrowType.toString(), "[ArrowType]");
    assertEquals(arrowType.getParameters().length(), 2);
    assertNull(arrowType.getReturnType());
  }

  @Test
  public void testIsSubtypeBasic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithSameParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, intType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, intType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithLessRequiredParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, null, intType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithMoreRequiredParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, intType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithOptionalParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, createNode("function(a?) { }", 0), intType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, intType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithVoidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType voidType = registry.getTypeOrCreate(Void.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, null);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, voidType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithNumberReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType numberType = registry.getTypeOrCreate(Number.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, numberType);

    assertTrue(arrowType1.isSubtype(arrowType2));
    // Number can't be a subtype of Int, because of the loss of precision
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithArrayReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrayType = registry.getTypeOrCreate(Int[].class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, arrayType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    // Array can't be a subtype of Int, because they are not related types
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithFunctionReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType functionType = new FunctionType(registry, null, null, null, null);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, functionType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    // Function can't be a subtype of Int, because they are not related types
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, null);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getTypeOrCreate(String.class);
    JSType intType = registry.getTypeOrCreate(Int.class);
    ArrowType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    ArrowType arrowType2 = new ArrowType(registry, PARAMETERS, intType);
    ArrowType arrowType3 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, intType);
    ArrowType arrowType4 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, stringType);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType3.hasEqualParameters(arrowType1, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, false));
    assertFalse(arrowType4.hasEqualParameters(arrowType1, false));

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType3, true));
    assertTrue(arrowType3.hasEqualParameters(arrowType1, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType4.hasEqualParameters(arrowType1, true));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType = new ArrowType(registry, PARAMETERS, intType);

    assertEquals("function(a, b) => Int", arrowType.toString());
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType1 = new ArrowType(registry, PARAMETERS, intType);
    JSType arrowType2 = new ArrowType(registry, PARAMETERS, intType);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);

    JSType arrowType3 = new ArrowType(registry, PARAMETERS_WITH_VAR_ARGS, intType);
    int hashCode3 = arrowType3.hashCode();

    assertNotEquals(hashCode1, hashCode3);
  }

  @Test
  public void testHasAnyTemplate() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType intType = registry.getTypeOrCreate(Int.class);
    JSType arrowType = new ArrowType(registry, PARAMETERS, intType);

    assertFalse(arrowType.hasAnyTemplate());

    JSType arrowTypeWithTemplate = new ArrowType(registry, PARAMETERS,
        new TemplateType(registry, intType, null));

    assertTrue(arrowTypeWithTemplate.hasAnyTemplate());
  }
}