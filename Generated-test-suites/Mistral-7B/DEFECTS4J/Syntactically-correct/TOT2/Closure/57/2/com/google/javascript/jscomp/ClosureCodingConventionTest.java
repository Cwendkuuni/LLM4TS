import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Test;

public class ClosureCodingConventionTest {

  private static final ClosureCodingConvention convention = new ClosureCodingConvention();

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("Parent", ImmutableList.of(), new ObjectType("Object"));
    FunctionType childCtor = new FunctionType("Child", ImmutableList.of(), new ObjectType("Object"));

    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals("Parent", childCtor.getPrototype().getClass().getSimpleName());
    assertEquals("constructor", childCtor.getPrototype().getDeclaredPropertyNames()[0]);
    assertEquals(parentCtor, childCtor.getPrototype().getDeclaredProperty("constructor"));

    // Test that superclass_ property is defined
    assertEquals("Parent", childCtor.getDeclaredProperty("superClass_").getValue().getClassName());
  }

  @Test(expected = AssertionError.class)
  public void testApplySubclassRelationshipWithInvalidCall() {
    FunctionType parentCtor = new FunctionType("Parent", ImmutableList.of(), new ObjectType("Object"));
    FunctionType childCtor = new FunctionType("Child", ImmutableList.of(), new ObjectType("Object"));

    convention.applySubclassRelationship(parentCtor, childCtor, null);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    // Test with correct call
    Node callNode = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog"), "inherits"),
        Node.newNode(Token.NAME, "Child"),
        Node.newNode(Token.NAME, "Parent")
    );
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode, null);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("Child", relationship.getSubclass().getQualifiedName());
    assertEquals("Parent", relationship.getSuperclass().getQualifiedName());

    // Test with call to mixin
    callNode = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog"), "mixin"),
        Node.newNode(Token.NAME, "Child"),
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "Parent"), "prototype")
    );
    relationship = convention.getclasssDefinedByCall(callNode, null);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("Child", relationship.getSubclass().getQualifiedName());
    assertEquals("Parent", relationship.getSuperclass().getQualifiedName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog"), "provide"),
        Node.newNode(Token.NAME, "X")
    );
    assertEquals("X", convention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog"), "require"),
        Node.newNode(Token.NAME, "X")
    );
    assertEquals("X", convention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", convention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.NAME, "goog.addDependency"),
        Node.newNode(Token.NAME, "goog.ArrayType"),
        Node.newNode(Token.NAME, "MyClass"),
        Node.newNode(Token.ARRAYLIT, Node.newNode(Token.STRING, "Array"))
    );
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);
    assertEquals(1, typeNames.size());
    assertEquals("ArrayType", typeNames.get(0));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "a"), "b.c.prototype");
    assertTrue(convention.endsWithPrototype(qualifiedName));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType fn = new FunctionType("MyFunction", ImmutableList.of(), new ObjectType("Object"));
    ObjectType objectType = new ObjectType("ObjectWithInstance");

    convention.applySingletonGetter(fn, fn, objectType);

    assertEquals("getInstance", fn.getDeclaredPropertyNames()[0]);
    assertEquals("instance_", fn.getDeclaredPropertyNames()[1]);
    assertEquals("ObjectWithInstance", fn.getDeclaredProperty("instance_").getType().getName());
  }

  @Test
  public void testSingletonGetterClassName() {
    Node callNode = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.addSingletonGetter"), null),
        Node.newNode(Token.NAME, "MySingletonGetter"),
        Node.newNode(Token.NAME, "MyClass")
    );

    String className = convention.getSingletonGetterClassName(callNode);
    assertEquals("MyClass", className);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog"), "isString"),
        Node.newNode(Token.NUMBER_LITERAL, 123)
    );
    assertTrue(convention.isPropertyTestFunction(call));
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newName("goog.reflect"), "object"),
        Node.newNode(Token.OBJECTLIT, Node.newNode(Token.STRING, "MyType"), Node.newNode(Token.NULL))
    );
    ObjectLiteralCast cast = convention.getObjectLiteralCast(NodeTraversal.emptyTraversal(), callNode);
    assertNotNull(cast);
    assertEquals("MyType", cast.getTypeName());
    assertEquals("Object", cast.getType().getName());
  }

  @Test(expected = AssertionError.class)
  public void testObjectLiteralCastMissingObjectLit() {
    Node callNode = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newName("goog.reflect"), "object"),
        Node.newNode(Token.NAME, "MyType"),
        Node.newNode(Token.NUMBER_LITERAL, 123)
    );
    convention.getObjectLiteralCast(NodeTraversal.emptyTraversal(), callNode);
  }
}