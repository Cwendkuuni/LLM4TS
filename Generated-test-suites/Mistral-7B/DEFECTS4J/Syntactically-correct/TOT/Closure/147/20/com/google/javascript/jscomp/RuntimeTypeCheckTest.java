import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  public RuntimeTypeCheckTest() {
    compiler = new Compiler();
    nodeFactory = compiler.getNodeFactory();
  }

  @Test
  public void testRuntimeTypeCheck() {
    String sourceCode = "function Foo() {}";

    Node externs = NodeUtil.parseJavaScript(sourceCode, "Foo");
    Node root = nodeFactory.parseJavaScript("function bar(x) { return x; }", null);

    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    compiler.process(externs, root, null, new ImmutableList.Builder<CompilerPass>()
        .add(runtimeTypeCheck).build());

    Node functionNode = NodeUtil.getFunctionNode(root, "bar");
    FunctionType functionType = (FunctionType) functionNode.getJSType();

    // Test type checking for a valid argument.
    Node argNode = NodeUtil.newLiteralNode(1);
    Node callNode = NodeUtil.newExprCallNode(functionNode, argNode);
    Node resultNode = NodeUtil.evaluateToGlobal(compiler, callNode);
    assertEquals(1, resultNode.getNumber().intValueExact());

    // Test type checking for an invalid argument.
    Node invalidArgNode = NodeUtil.newNullNode();
    callNode = NodeUtil.newExprCallNode(functionNode, invalidArgNode);
    resultNode = NodeUtil.evaluateToGlobal(compiler, callNode);
    assertNull(resultNode);

    // Test that the function returns the correct type.
    Node returnNode = functionNode.getLastChild();
    JSType returnType = returnNode.getJSType();
    assertEquals(JSType.NUMBER_TYPE, returnType);

    // Test that a constructor function is marked with the correct interface.
    sourceCode = "function Foo() {}" +
        "function FooBar() implements Foo {}";
    externs = NodeUtil.parseJavaScript(sourceCode, "FooBar");
    root = nodeFactory.parseJavaScript("function bar(x) { return new FooBar(); }", null);

    compiler.process(externs, root, null, new ImmutableList.Builder<CompilerPass>()
        .add(runtimeTypeCheck).build());

    functionNode = NodeUtil.getFunctionNode(root, "bar");
    assertNotNull(functionNode);

    Node constructorNode = NodeUtil.getFunctionNode(
        NodeUtil.getFirstChild(functionNode), "FooBar");
    assertEquals(FunctionType.constructorFunctionType(
        new ObjectType("FooBar"), null), constructorNode.getJSType());

    Node markerNode = NodeUtil.getFirstChild(
        NodeUtil.getFirstChild(constructorNode));
    assertEquals("implements__Foo", markerNode.getString());
  }
}