import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        ObjectType.INSTANCE);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        ObjectType.INSTANCE);
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newNode(Token.CALL, "goog.inherits",
        Node.newNode(Token.GETIDENT, "SubClass"),
        Node.newNode(Token.GETIDENT, "SuperClass"));
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(Token.CALL, "goog.addDependency",
        Node.newNode(Token.GETIDENT, "MyClass"),
        Node.newNode(Token.ARRAYLIT,
            Node.newNode(Token.STRING, "type1"),
            Node.newNode(Token.STRING, "type2")));

    List<String> types = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(2, types.size());
    assertEquals("type1", types.get(0));
    assertEquals("type2", types.get(1));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, "goog.require",
        Node.newNode(Token.STRING, "MyModule"));
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("MyModule", className);
  }
}