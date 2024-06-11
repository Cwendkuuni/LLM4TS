import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(inlineObjectLiterals);

    // Provide a simple node tree for testing
    Node root = Node.newObjectLiteral();

    Node object1 = new Node(Token.OBJECTLIT);
    object1.addChild(new Node(Token.STRING_LITERAL, "name1"));
    object1.addChild(new Node(Token.STRING_LITERAL, "value1"));
    root.addChild(object1);

    Node object2 = new Node(Token.OBJECTLIT);
    object2.addChild(new Node(Token.STRING_LITERAL, "name2"));
    object2.addChild(new Node(Token.STRING_LITERAL, "value2"));
    root.addChild(object2);

    Node assign = new Node(Token.ASSIGN, Node.newName("testObj"), root);
    root.addChild(assign);

    // Run the pass
    Node actual = passManager.process(Node.newRootNode(externs -> null), root);

    // Verify that the object literals have been expanded into individual variables
    Node testObj = actual.getFirstChild();
    assertEquals(4, testObj.getNumChildren());

    Node name1 = testObj.getChildAtIndex(0);
    assertEquals(Token.VAR, name1.getType());
    assertEquals("JSCompiler_object_inline_name1_test", name1.getString());

    Node value1 = testObj.getChildAtIndex(1);
    assertEquals(Token.VAR, value1.getType());
    assertEquals("JSCompiler_object_inline_value1_test", value1.getString());

    Node name2 = testObj.getChildAtIndex(2);
    assertEquals(Token.VAR, name2.getType());
    assertEquals("JSCompiler_object_inline_name2_test", name2.getString());

    Node value2 = testObj.getChildAtIndex(3);
    assertEquals(Token.VAR, value2.getType());
    assertEquals("JSCompiler_object_inline_value2_test", value2.getString());

    // Verify that the assignments have been updated accordingly
    Node assignNode = testObj.getLastChild();
    assertEquals(Token.ASSIGN, assignNode.getType());
    assertEquals(4, assignNode.getNumChildren());

    Node name1Assign = assignNode.getChildAtIndex(0);
    assertEquals(Token.ASSIGN, name1Assign.getType());
    assertEquals(name1, name1Assign.getFirstChild());
    assertEquals(value1, name1Assign.getLastChild());

    Node name2Assign = assignNode.getChildAtIndex(1);
    assertEquals(Token.ASSIGN, name2Assign.getType());
    assertEquals(name2, name2Assign.getFirstChild());
    assertEquals(value2, name2Assign.getLastChild());

    Node trueNode = assignNode.getChildAtIndex(2);
    assertEquals(Token.TRUE, trueNode.getType());
  }
}