import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String testSource = "function Foo() {}";
    CompilerInput input = new CompilerInput(null, "test.js", testSource);
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RuntimeTypeCheck(compiler, null);

    Node root = NodeFactory.createProgram();
    NodeTraversal.traverse(compiler, root, NodeUtil.parseAndNormalizeCode(
        compiler, testSource, "test_"));

    pass.process(input.getExterns(), root);
    compiler.compile(input, root);

    Node functionNode = NodeUtil.getFunctionDeclarations(root)[0];
    Node constructorNode = functionNode.getFirstChild();
    Node instanceCheck = NodeUtil.getFirstChildByType(constructorNode, Token.GETPROP)
        .getFirstChild();

    Node factory = NodeFactory.getFactory();
    Node testInstance = factory.newObject("Foo");

    Node checkTypeCall = (Node) NodeUtil.getFirstChildByType(instanceCheck, Token.CALL);
    Node expression = checkTypeCall.getArgument(0);

    Node result = NodeUtil.runCode(compiler.getContext(), expression, testInstance, null);

    assertEquals(Node.NEW_BOOL(true), result);

    // Testing with a non-instance object
    Node nonInstance = factory.newObject("String");
    result = NodeUtil.runCode(compiler.getContext(), expression, nonInstance, null);

    assertEquals(Node.NEW_BOOL(false), result);
  }
}