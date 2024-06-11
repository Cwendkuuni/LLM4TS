import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.test_util.AbstractCompilerTestCase;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FunctionRewriterTest extends AbstractCompilerTestCase {

  private Compiler compiler;
  private CompilerPass functionRewriter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(Compiler.class);
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer() {
    CompilerOptions options = new CompilerOptions();
    Node root = createNodeFromString(
        "function Test() {}\n" +
        "Test.prototype.testFunction = function() {};");

    when(compiler.parseSyntheticCode(anyString(), anyInt(), anyInt()))
        .thenReturn(null);

    NodeTraversal traversal = new NodeTraversal(root, options, null);
    functionRewriter.process(null, root);
    traversal.traverse(functionRewriter);

    Node testFunction = findNodeInTree("Test.prototype.testFunction", root);
    assertEquals("emptyFn()", testFunction.toString());
  }

  @Test
  public void testIdentityFunctionReducer() {
    CompilerOptions options = new CompilerOptions();
    Node root = createNodeFromString(
        "function Test() {}\n" +
        "Test.prototype.testFunction = function(x) { return x; }");

    when(compiler.parseSyntheticCode(anyString(), anyInt(), anyInt()))
        .thenReturn(null);

    NodeTraversal traversal = new NodeTraversal(root, options, null);
    functionRewriter.process(null, root);
    traversal.traverse(functionRewriter);

    Node testFunction = findNodeInTree("Test.prototype.testFunction", root);
    assertEquals("identityFn(_)()", testFunction.toString());
  }

  @Test
  public void testGettersReducer() {
    CompilerOptions options = new CompilerOptions();
    Node root = createNodeFromString(
        "function Test() {}\n" +
        "Test.prototype.testFunction = function() { return this.a; }");

    Node expectedGetCall = createNodeFromString(
        "getter(\"a\")");

    when(compiler.parseSyntheticCode(anyString(), anyInt(), anyInt()))
        .thenReturn(expectedGetCall);

    NodeTraversal traversal = new NodeTraversal(root, options, null);
    functionRewriter.process(null, root);
    traversal.traverse(functionRewriter);

    Node testFunction = findNodeInTree("Test.prototype.testFunction", root);
    Node getCall = findNodeInTree("getter(\"a\")", root);
    assertEquals(testFunction, getCall);
  }

  @Test
  public void testSettersReducer() {
    CompilerOptions options = new CompilerOptions();
    Node root = createNodeFromString(
        "function Test() {}\n" +
        "Test.prototype.testFunction = function(value) { this.a = value; }");

    Node expectedSetCall = createNodeFromString(
        "getter(\"a\")");

    Node expectedAssignment = createNodeFromString(
        "function(value) { this.a = value; }");

    when(compiler.parseSyntheticCode(anyString(), anyInt(), anyInt()))
        .thenReturn(expectedSetCall);

    NodeTraversal traversal = new NodeTraversal(root, options, null);
    functionRewriter.process(null, root);
    traversal.traverse(functionRewriter);

    Node testFunction = findNodeInTree("Test.prototype.testFunction", root);
    Node setCall = findNodeInTree("getter(\"a\")", root);
    Node assignment = findNodeInTree(expectedAssignment, root);
    assertEquals(testFunction, assignment);
  }

  @Test
  public void testNoReduction() {
    CompilerOptions options = new CompilerOptions();
    Node root = createNodeFromString(
        "function Test() {}\n" +
        "Test.prototype.testFunction = function() { return 1 + 2; }");

    when(compiler.parseSyntheticCode(anyString(), anyInt(), anyInt()))
        .thenReturn(null);

    NodeTraversal traversal = new NodeTraversal(root, options, null);
    functionRewriter.process(null, root);
    traversal.traverse(functionRewriter);

    Node testFunction = findNodeInTree("Test.prototype.testFunction", root);
    assertEquals("function() { return 1 + 2; }", testFunction.toString());
  }

  @Test
  public void testNoReductionWithConstantReturn() {
    CompilerOptions options = new CompilerOptions();
    Node root = createNodeFromString(
        "function Test() {}\n" +
        "Test.prototype.testFunction = function() { return 10; }");

    when(compiler.parseSyntheticCode(anyString(), anyInt(), anyInt()))
        .thenReturn(null);

    NodeTraversal traversal = new NodeTraversal(root, options, null);
    functionRewriter.process(null, root);
    traversal.traverse(functionRewriter);

    Node testFunction = findNodeInTree("Test.prototype.testFunction", root);
    assertEquals("function() { return 10; }", testFunction.toString());
  }

  private Node findNodeInTree(String nodeName, Node root) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.getString().equals(nodeName)) {
        return child;
      }
    }
    throw new RuntimeException("Node not found: " + nodeName);
  }

  private Node createNodeFromString(String jsCode) {
    return (Node) getJavascriptParser().parse(jsCode, null, null, null, null);
  }

  private boolean isReduceableFunctionExpression(Node node) {
    return NodeUtil.isFunctionExpression(node);
  }
}