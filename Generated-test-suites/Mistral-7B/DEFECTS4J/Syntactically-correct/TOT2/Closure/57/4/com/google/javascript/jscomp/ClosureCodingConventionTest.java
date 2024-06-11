import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.JSType.OBJECT;
import static com.google.javascript.rhino.JSType.FUNCTION;
import static com.google.javascript.rhino.JSType.NUMBER;
import static com.google.javascript.rhino.JSType.STRING;
import static com.google.javascript.rhino.JSType.BOOLEAN;
import static com.google.javascript.rhino.JSType.ARRAY;
import static com.google.javascript.rhino.JSType.OBJECT_INSTANCE;
import static com.google.javascript.rhino.JSType.NULL;
import static com.google.javascript.rhino.JSType.UNDEFINED;
import static com.google.javascript.jscomp.ClosureCodingConvention.OBJECTLIT_EXPECTED;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("Parent", VOID_TYPE, ImmutableList.of());
    FunctionType childCtor = new FunctionType("Child", VOID_TYPE, ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);
    // Test for adding superClass_ and constructor properties.
    assertTrue(childCtor.hasOwnProperty("superClass_"));
    assertTrue(childCtor.getPrototype().hasOwnProperty("constructor"));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.newInstance(Token.CALL, 3);
    Node parent = Node.newInstance(Token.NAME, "Parent");
    Node child = Node.newInstance(Token.NAME, "Child");
    callNode.setChild(0, Node.newInstance(Token.GETPROP, parent));
    callNode.setChild(1, child);
    String extractedClassName = ClosureCodingConvention.extractClassNameIfCall(callNode, null);
    assertTrue(extractedClassName.equals("Child"));
  }

  @Test
  public void testIsSuperClassReference() {
    String propertyName = "superClass_";
    assertTrue(ClosureCodingConvention.isSuperClassReference(propertyName));
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = ClosureCodingConvention.getExportPropertyFunction();
    assertTrue(exportPropertyFunction.equals("goog.exportProperty"));
  }

  @Test(expected = AssertionError.class)
  public void testObjectLiteralCast() {
    NodeTraversal traversal = new NodeTraversal();
    Node callNode = Node.newInstance(Token.CALL, 3);
    Node objectNode = Node.newInstance(Token.OBJECTLIT, ImmutableList.of());
    callNode.setChild(0, Node.newInstance(Token.GETPROP, Node.newInstance(Token.STRING, "goog.reflect.object")));
    callNode.setChild(1, Node.newInstance(Token.STRING, "StringType"));
    callNode.setChild(2, objectNode);
    ObjectLiteralCast cast = ClosureCodingConvention.getObjectLiteralCast(traversal, callNode);
    assert cast != null;
    // Test that the cast type is valid.
    assertTrue(cast.getType().equals("StringType"));
    // Test that the cast value is valid.
    Node valueNode = Object.create(cast.getType().getJsType());
    assertTrue(valueNode.getClass().isInstance(cast.getValue()));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.newInstance(Token.CALL, 2);
    Node callName = Node.newInstance(Token.GETPROP, Node.newInstance(Token.STRING, "goog.isDef"));
    callNode.setChild(0, callName);
    assertTrue(ClosureCodingConvention.isPropertyTestFunction(callNode));
  }
}