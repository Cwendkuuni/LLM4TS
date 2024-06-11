import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.node.FunctionNode;
import junit.framework.TestCase;

public class ArrowTypeTest extends TestCase {

  private static final ErrorReporter errorReporter = new ErrorReporter();
  private static final JSTypeRegistry registry = new JSTypeRegistry(errorReporter);

  @Test
  public void testArrowTypeWithSingleParameterAndVoidReturnType() {
    Node functionNode = new FunctionNode(
        new Node("function"),
        new Node("name"),
        new Node("parameters") {
          {
            this.setFirstChild(new Node("identifier", "a"));
          }
        },
        new Node("body"),
        new Node("source")
    );
    JSType arrowType = new ArrowType(registry, functionNode.getParameters(), null);

    // Test isSubtype with ArrowType as supertype
    JSType superType = new ArrowType(registry, null, null, false);
    assertTrue(arrowType.isSubtype(superType));

    // Test isSubtype with ArrowType as subtype
    JSType subType = new ArrowType(registry, new Node("identifier", "x"), null);
    assertTrue(subType.isSubtype(arrowType));

    // Test hasEqualParameters
    ArrowType arrowTypeWithSameParameters = new ArrowType(registry, functionNode.getParameters(), null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParameters));

    // Test equals
    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, functionNode.getParameters(), JSTypeNative.VOID_TYPE, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturnType));

    // Test hashCode
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry, new Node("identifier", "b"), null);
    assertFalse(arrowType.hashCode() == arrowTypeWithDifferentParameters.hashCode());
  }

  @Test
  public void testArrowTypeWithMultipleParametersAndNumberReturnType() {
    Node functionNode = new FunctionNode(
        new Node("function"),
        new Node("name"),
        new Node("parameters") {
          {
            this.setFirstChild(new Node("identifier", "a"));
            this.addChild(new Node("identifier", "b"));
          }
        },
        new Node("body"),
        new Node("source")
    );
    JSType arrowType = new ArrowType(registry, functionNode.getParameters(), JSTypeNative.NUMBER_TYPE, false);

    // Test isSubtype with ArrowType as supertype
    JSType superType = new ArrowType(registry, new Node("identifier", "a"), null, false);
    assertTrue(arrowType.isSubtype(superType));

    // Test isSubtype with ArrowType as subtype
    JSType subType = new ArrowType(registry, new Node("identifier", "a"), null, false);
    JSType subTypeWithBParameter = new ArrowType(registry, new Node("identifier", "a"), new Node("identifier", "b"), false);
    assertTrue(subType.isSubtype(subTypeWithBParameter));
    assertFalse(subTypeWithBParameter.isSubtype(subType));
    assertFalse(subType.isSubtype(arrowType));

    // Test hasEqualParameters
    ArrowType arrowTypeWithSameParameters = new ArrowType(registry, functionNode.getParameters(), JSTypeNative.NUMBER_TYPE, false);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParameters));

    // Test equals
    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, functionNode.getParameters(), JSTypeNative.BOOL_TYPE, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturnType));

    // Test hashCode
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry, new Node("identifier", "b"), null, false);
    assertFalse(arrowType.hashCode() == arrowTypeWithDifferentParameters.hashCode());
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node functionNode = new FunctionNode(
        new Node("function"),
        new Node("name"),
        new Node("parameters") {
          {
            this.setFirstChild(new Node("identifier", "a"));
            this.addChild(new Node("identifier", "..."));
          }
        },
        new Node("body"),
        new Node("source")
    );
    JSType arrowType = new ArrowType(registry, functionNode.getParameters(), null, false);

    // Test isSubtype with ArrowType as supertype
    JSType superType = new ArrowType(registry, null, null, false);
    assertTrue(arrowType.isSubtype(superType));

    // Test isSubtype with ArrowType as subtype
    JSType subType = new ArrowType(registry, new Node("identifier", "a"), null, false);
    JSType subTypeWithVarArgs = new ArrowType(registry, new Node("identifier", "a"), null, true);
    assertTrue(subType.isSubtype(subTypeWithVarArgs));
    assertFalse(subTypeWithVarArgs.isSubtype(subType));
    assertFalse(subType.isSubtype(arrowType));

    // Test hasEqualParameters
    ArrowType arrowTypeWithSameParameters = new ArrowType(registry, functionNode.getParameters(), null, false);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParameters));

    // Test equals
    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, functionNode.getParameters(), JSTypeNative.BOOL_TYPE, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturnType));

    // Test hashCode
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry, new Node("identifier", "b"), null, false);
    assertFalse(arrowType.hashCode() == arrowTypeWithDifferentParameters.hashCode());
  }

  @Test
  public void testArrowTypeWithUnknownTypes() {
    Node functionNode = new FunctionNode(
        new Node("function"),
        new Node("name"),
        new Node("parameters") {
          {
            this.setFirstChild(new Node("identifier", "a"));
            this.addChild(new Node("identifier", "b"));
          }
        },
        new Node("body"),
        new Node("source")
    );
    JSType arrowType = new ArrowType(registry, functionNode.getParameters(), null, false);

    // Test isSubtype with ArrowType as supertype
    JSType superType = new ArrowType(registry, null, null, false);
    assertTrue(arrowType.isSubtype(superType));

    // Test isSubtype with ArrowType as subtype
    JSType subType = new ArrowType(registry, new Node("identifier", "x"), null);
    JSType subTypeWithUnknownTypes = new ArrowType(registry, functionNode.getParameters(), null, false);
    assertFalse(subType.isSubtype(subTypeWithUnknownTypes));

    // Test hasEqualParameters
    ArrowType arrowTypeWithSameUnknownParameters = new ArrowType(registry, functionNode.getParameters(), null, false);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameUnknownParameters));

    // Test equals
    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, functionNode.getParameters(), JSTypeNative.BOOL_TYPE, false);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturnType));

    // Test hashCode
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry, new Node("identifier", "b"), null, false);
    assertFalse(arrowType.hashCode() == arrowTypeWithDifferentParameters.hashCode());

    // Test hasUnknownParamsOrReturn
    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
}