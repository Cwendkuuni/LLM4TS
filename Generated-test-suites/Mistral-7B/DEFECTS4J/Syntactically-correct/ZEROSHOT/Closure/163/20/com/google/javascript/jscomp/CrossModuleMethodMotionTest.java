import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.jscomp.CompilerOptions.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions.CheckType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.jscomp.QuirksMode;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    options.setCheckTypes(ImmutableList.of(CheckType.ERROR));
    options.setWarningsAsErrors(true);
    options.setQuirksMode(QuirksMode.ES5);
    compiler = new Compiler();
    passManager = new CompilerPassManager(compiler, options);
  }

  private Node createRootNode(String source) {
    return compiler.parseTree(source, null, null);
  }

  @Test
  public void testCrossModuleMethodMotion() {
    String source = "(" +
        "function Foo() {}" +
        "Foo.prototype.bar = function() {};" +
        "function Main() {" +
        "  Foo.prototype.bar();" +
        "}" +
        ")" +
        "(" +
        "function Bar() {}" +
        ")" +
        "function Main2() {" +
        "  Foo.prototype.bar();" +
        "}";

    Node root = createRootNode(source);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    passManager.addPass(pass);
    passManager.runPasses();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());

    Node mainNode = findNodeByKind(root, NodeKind.GLOBAL, "Main");
    Node mainFunction = (Node) mainNode.getFirstChild();
    Node mainBody = mainFunction.getBody();
    Node barCall = findNodeByKind(mainBody, NodeKind.CALL, "Foo.prototype.bar");
    assertNotNull(barCall);

    Node deepRoot = (Node) findNodeByKind(root, NodeKind.GLOBAL, "Main2");
    Node deepFunction = (Node) deepRoot.getFirstChild();
    Node deepBody = deepFunction.getBody();
    Node deepBarCall = findNodeByKind(deepBody, NodeKind.CALL, "Foo.prototype.bar");
    assertNotNull(deepBarCall);

    // Ensure that the method call in the second module still calls the
    // original method.
    Node barFunction = findNodeByKind(root, NodeKind.FUNCTION_DECLARATION, "Foo.prototype.bar");
    Node barFunctionBody = barFunction.getBody();
    Node barStubCall = findNodeByKind(barFunctionBody, NodeKind.CALL, STUB_METHOD_NAME);
    Node barUnstubCall = findNodeByKind(deepRoot, NodeKind.CALL, UNSTUB_METHOD_NAME);
    Node barFunctionBodyDeep = findNodeByKind(barUnstubCall, NodeKind.FUNCTION_BODY);
    Node barFunctionBodyDeepBody = barFunctionBodyDeep.getBody();
    Node barFunctionBodyDeepReturn = findNodeByKind(barFunctionBodyDeepBody, NodeKind.RETURN);
    Node barFunctionBodyDeepBodyCall = findNodeByKind(barFunctionBodyDeepBody, NodeKind.CALL, "Foo.prototype.bar");
    assertEquals(barFunction, barFunctionBodyCall.getParent());
  }

  private Node findNodeByKind(Node node, NodeKind kind, String name) {
    if (node == null) {
      return null;
    }
    if (node.getKind() == kind && node.getValue().toString().equals(name)) {
      return node;
    }
    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChild(i);
      Node result = findNodeByKind(child, kind, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static class NodeKind {
    NodeKind(int kind, String name) {
      this.kind = kind;
      this.name = name;
    }

    int kind;
    String name;
  }

  @SuppressWarnings("all")
  private static NodeKind GLOBAL = new NodeKind(Token.GLOBAL, "GLOBAL");
  @SuppressWarnings("all")
  private static NodeKind FUNCTION_BODY = new NodeKind(Token.FUNCTION_BODY, "FUNCTION_BODY");
  @SuppressWarnings("all")
  private static NodeKind FUNCTION_DECLARATION = new NodeKind(Token.FUNCTION_DECLARATION, "FUNCTION_DECLARATION");
  @SuppressWarnings("all")
  private static NodeKind CALL = new NodeKind(Token.CALL, "CALL");
}