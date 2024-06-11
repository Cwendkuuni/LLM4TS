import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.aliasHandlers.AliasTransformationHandler;
import com.google.javascript.jscomp.errors.DiagnosticTypeError;
import com.google.javascript.rhino.NodeTraversal;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Override
  public void setUp() throws Exception {
    // Initialize the test compiler and options
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();

    AliasTransformationHandler aliasTransformationHandler = new AliasTransformationHandler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, aliasTransformationHandler);

    // Set up the test case
    TestCase testCase = new TestCase(compiler, options);
    testCase.setPass(scopedAliases);
  }

  @org.junit.Test
  public void testScopedAliases_Simple() {
    String sourceCode = "goog.scope(function() {\n" +
        "var dom = goog.dom;\n" +
        "var DIV = dom.TagName.DIV;\n" +
        "dom.createElement(DIV);\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(NodeUtil.getFunctionName(aliasDefinitionsInOrder.get(0)), "dom");
    assertEquals(aliasDefinitionsInOrder.get(0).getParent().toString(),
        "goog.dom");
    assertEquals(aliasCalls(scopeCalls.get(0)), "goog.dom");
  }

  @org.junit.Test
  public void testScopedAliases_Invalid_ScopeCall() {
    String sourceCode = "goog.scope(function() {\n" +
        "goog.scope(function() {});\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    assertEquals(diagnosticMessage(traversal.getError(0)),
        "JSC_GOOG_SCOPE_USED_IMPROPERLY: The call to goog.scope must be alone in a single statement.");
  }

  @org.junit.Test
  public void testScopedAliases_Invalid_AliasDefinition() {
    String sourceCode = "goog.scope(function() {\n" +
        "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "});\n" +
        "var y = goog.y;\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    assertEquals(diagnosticMessage(traversal.getError(0)),
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias x is assigned a value more than once.");
  }

  @org.junit.Test
  public void testScopedAliases_Invalid_AliasUsage() {
    String sourceCode = "goog.scope(function() {\n" +
        "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  return x;\n" +
        "});\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    assertEquals(diagnosticMessage(traversal.getError(0)),
        "JSC_GOOG_SCOPE_USES_RETURN: The body of a goog.scope function cannot use 'return'.");
  }

  @org.junit.Test
  public void testScopedAliases_Invalid_ThrownException() {
    String sourceCode = "goog.scope(function() {\n" +
        "goog.scope(function() {\n" +
        "  throw new Error('TestError');\n" +
        "});\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    assertEquals(diagnosticMessage(traversal.getError(0)),
        "JSC_GOOG_SCOPE_USES_THROW: The body of a goog.scope function cannot use 'throw'.");
  }

  @org.junit.Test
  public void testScopedAliases_Nested_Scope() {
    String sourceCode = "goog.scope(function() {\n" +
        "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  return x;\n" +
        "});\n" +
        "var y = goog.x;\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    assertEquals(diagnosticMessage(traversal.getError(0)),
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias x is assigned a value more than once.");

    // Check the aliases
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionsInOrder.get(0).getParent().toString(),
        "goog.x");

    // Check the scope calls
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(2, scopeCalls.size());
    assertEquals(aliasCalls(scopeCalls.get(0)), "goog.x");
    assertEquals(aliasCalls(scopeCalls.get(1)), "goog.x");
  }

  @org.junit.Test
  public void testScopedAliases_Multiple_Aliases() {
    String sourceCode = "goog.scope(function() {\n" +
        "var goog = { dom: 1, x: 2 };\n" +
        "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var dom = goog.dom;\n" +
        "  return x + dom.createElement('DIV').nodeValue;\n" +
        "});\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertFalse(traversal.hasErrors());

    // Check the aliases
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionsInOrder.get(0).getParent().toString(),
        "goog.dom");
    assertEquals(aliasDefinitionsInOrder.get(1).getParent().toString(),
        "goog.x");

    // Check the scope calls
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(aliasCalls(scopeCalls.get(0)), "goog.dom, goog.x");
  }

  @org.junit.Test
  public void testScopedAliases_Scope_With_Multiple_Statements() {
    String sourceCode = "goog.scope(function() {\n" +
        "var goog = { dom: 1, x: 2 };\n" +
        "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var y = goog.dom.createElement('DIV').nodeValue;\n" +
        "  goog.dom.createElement('DIV').nodeValue = 'Test';\n" +
        "  return x + y;\n" +
        "});\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertFalse(traversal.hasErrors());

    // Check the aliases
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionsInOrder.get(0).getParent().toString(),
        "goog.dom");
    assertEquals(aliasDefinitionsInOrder.get(1).getParent().toString(),
        "goog.x");

    // Check the scope calls
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(aliasCalls(scopeCalls.get(0)), "goog.dom, goog.x");
  }

  @org.junit.Test
  public void testScopedAliases_Namespace_Shadowing() {
    String sourceCode = "goog.scope(function() {\n" +
        "var goog = { dom: 1, x: 2 };\n" +
        "goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var goog = { dom: 3 };\n" +
        "  goog.dom.createElement('DIV').nodeValue = 'Test';\n" +
        "  return x;\n" +
        "});\n" +
        "})";

    List<Node> ast = TestHelper.parseJavascript(sourceCode);

    // Test the ScopedAliases processor
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);

    // Check for errors
    assertFalse(traversal.hasErrors());

    // Check the aliases
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(aliasDefinitionsInOrder.get(0).getParent().toString(),
        "goog.dom");
    assertEquals(aliasDefinitionsInOrder.get(1).getParent().toString(),
        "goog.x");

    // Check the scope calls
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(aliasCalls(scopeCalls.get(0)), "goog.dom, goog.x");

    // Check the namespace shadows
    Set<String> forbiddenLocals = traversal.getForbiddenLocals();
    assertEquals(1, forbiddenLocals.size());
    assertEquals(forbiddenLocals.iterator().next(), "goog");
  }

  private String aliasCalls(Node node) {
    String aliasCalls = "";

    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isQualifiedName()) {
        aliasCalls += child.getString() + ", ";
      } else {
        aliasCalls += aliasCalls(child);
      }
    }

    return aliasCalls.substring(0, aliasCalls.length() - 2);
  }

  private String diagnosticMessage(Node node) {
    return node.getClass().getSimpleName() + ":\n" +
        "  Line: " + node.getLineno() + "\n" +
        "  Character: " + node.getCharno() + "\n" +
        "  Message: ";
  }

  private static class TestCase extends AbstractCompilerTestCase {
    private HotSwapCompilerPass pass;

    public TestCase(AbstractCompiler compiler, CompilerOptions options) {
      super(compiler, options);
    }

    public void setPass(HotSwapCompilerPass pass) {
      this.pass = pass;
    }

    @Override
    public void process(List<Node> externs, Node root) throws Exception {
      pass.process(externs, root);
    }
  }
}