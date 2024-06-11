import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.testing.Assert;
import org.junit.Test;

public class ArrowTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();
  private static ErrorReporter errorReporter = new ErrorReporter();
  private static StaticScope<JSType> scope = new StaticScope<>(registry);

  @Test
  public void testArrowTypeCreation() {
    Node functionNode = registry.parseFunctionExpression("function(x) { return x; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    Assert.assertEquals("Expected correct ArrowType creation", arrowType,
        arrowType.resolveInternal(errorReporter, scope));

    Assert.assertTrue("Expected return type to be Number", arrowType.returnType
        .isSubtype(registry.getNativeType(JSType.NUMBER)));
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node functionNode = registry.parseFunctionExpression("function(x) { return x; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(),
        null, true);

    Assert.assertEquals("Expected correct ArrowType creation with inferred return type", arrowType,
        arrowType.resolveInternal(errorReporter, scope));

    Assert.assertNull("Expected return type to be inferred", arrowType.returnType);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node functionNode = registry.parseFunctionExpression("function(x, ...y) { return x; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    Assert.assertEquals("Expected correct ArrowType creation with var args", arrowType,
        arrowType.resolveInternal(errorReporter, scope));

    Node lastParamNode = arrowType.parameters.getLastChild();
    Assert.assertTrue("Expected last parameter to be var args",
        lastParamNode.isVarArgs());
  }

  @Test
  public void testIsSubtypeWithSameTypes() {
    Node functionNode1 = registry.parseFunctionExpression("function(x) { return x; }");
    Node functionNode2 = registry.parseFunctionExpression("function(x) { return x; }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    Assert.assertTrue("Expected both ArrowTypes to be subtypes of each other",
        arrowType1.isSubtype(arrowType2) && arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentReturnTypes() {
    Node functionNode1 = registry.parseFunctionExpression("function(x) { return x; }");
    Node functionNode2 = registry.parseFunctionExpression("function(x) { return x + 1; }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    Assert.assertFalse("Expected different ArrowTypes not to be subtypes of each other",
        arrowType1.isSubtype(arrowType2) && arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentParameterTypes() {
    Node functionNode1 = registry.parseFunctionExpression("function(x) { return x; }");
    Node functionNode2 = registry.parseFunctionExpression("function(y) { return y; }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        registry.getNativeType(JSType.STRING));

    Assert.assertFalse("Expected different ArrowTypes not to be subtypes of each other",
        arrowType1.isSubtype(arrowType2) && arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    Node functionNode1 = registry.parseFunctionExpression("function(x, y) { }");
    Node functionNode2 = registry.parseFunctionExpression("function(y, x) { }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        null);
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        null);

    Assert.assertFalse("Expected different ArrowTypes not to have equal parameters",
        arrowType1.hasEqualParameters(arrowType2));

    Node firstParamNode1 = functionNode1.getFirstChild();
    Node firstParamNode2 = functionNode2.getFirstChild();

    Assert.assertEquals("Expected first parameters to have equal types",
        firstParamNode1.getJSType(), firstParamNode2.getJSType());

    ArrowType arrowType3 = new ArrowType(registry, functionNode1.getParameters(),
        null);
    ArrowType arrowType4 = new ArrowType(registry, functionNode1.getParameters(),
        null);

    Assert.assertTrue("Expected the same ArrowTypes to have equal parameters",
        arrowType3.hasEqualParameters(arrowType4));
  }

  @Test
  public void testIsEquivalentTo() {
    Node functionNode1 = registry.parseFunctionExpression("function(x) { return x; }");
    Node functionNode2 = registry.parseFunctionExpression("function(x, y) { return x; }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    ArrowType arrowType3 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    Assert.assertFalse("Expected different ArrowTypes not to be equivalent",
        arrowType1.isEquivalentTo(arrowType2));

    Assert.assertTrue("Expected the same ArrowTypes to be equivalent",
        arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testGetLeastSupertype() {
    Node functionNode1 = registry.parseFunctionExpression("function(x) { return x; }");
    Node functionNode2 = registry.parseFunctionExpression("function(x, y) { return x + y; }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    ArrowType leastSupertype = arrowType1.getLeastSupertype(arrowType2);

    // A function that returns a number can be considered a supertype of a function that returns a number or a number and a number
    ArrowType expectedLeastSupertype = new ArrowType(registry, null,
        registry.getNativeType(JSType.NUMBER));

    Assert.assertEquals("Expected the least supertype to be the correct ArrowType", expectedLeastSupertype, leastSupertype);
  }

  @Test
  public void testGetGreatestSubtype() {
    Node functionNode1 = registry.parseFunctionExpression("function(x) { return x; }");
    Node functionNode2 = registry.parseFunctionExpression("function(x, y) { return x + y; }");

    ArrowType arrowType1 = new ArrowType(registry, functionNode1.getParameters(),
        registry.getNativeType(JSType.NUMBER));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    ArrowType greatestSubtype = arrowType2.getGreatestSubtype(arrowType1);

    // A function that returns a number and a number cannot be a subtype of a function that returns a single number
    Assert.assertNull("Expected the greatest subtype to be null", greatestSubtype);
  }

  @Test
  public void testToString() {
    Node functionNode = registry.parseFunctionExpression("function(x) { return x; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(),
        registry.getNativeType(JSType.NUMBER));

    String expectedToString = "ArrowType[number]";
    String actualToString = arrowType.toString();

    Assert.assertEquals("Expected correct ArrowType toString", expectedToString, actualToString);
  }
}