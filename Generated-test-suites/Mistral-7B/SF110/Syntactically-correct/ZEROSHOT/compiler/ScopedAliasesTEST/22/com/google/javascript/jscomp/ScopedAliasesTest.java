import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new CompilerOptions() {
      @Override
      public void setAliasTransformationHandler(
          AliasTransformationHandler handler) {
        // Do nothing.
      }
    }.createCompiler();

    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be no errors reported.
    assertEquals(0, traversal.getErrorCount());

    // The scoped alias for 'dom' should be defined.
    Node domDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", domDefinition.getString());

    // The call to 'dom.createElement' should use the alias.
    Node domElementCreation = traversal.getScopeCalls().get(0).getLastChild();
    assertEquals("goog.dom.createElement", domElementCreation.getString());

    // The call to 'TagName.DIV' should be unchanged.
    Node tagNameDiv = traversal.getCurrentNode();
    assertEquals("TagName.DIV", tagNameDiv.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    String jsCode = "goog.scope(function() {" +
        "function goo() {" +
        "  goo.x = 1;" +
        "  goog.scope(function() {" +
        "    goo.x = 2;" +
        "  });" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be an error reported for the alias redefinition.
    assertEquals(1, traversal.getErrorCount());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getLastErrorType());
    assertEquals("goo.x", traversal.getLastErrorMessage());

    // The alias for 'goo' should not be defined.
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
  }

  @Test
  public void testScopedAliasesWithBadScopeCall() {
    String jsCode = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be an error reported for the improperly used goog.scope.
    assertEquals(1, traversal.getErrorCount());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, traversal.getLastErrorType());
  }

  @Test
  public void testScopedAliasesWithBadParameters() {
    String jsCode = "goog.scope(function(foo) {" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be an error reported for the improperly used goog.scope.
    assertEquals(1, traversal.getErrorCount());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, traversal.getLastErrorType());
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    String jsCode = "goog.scope(function() {" +
        "return 1;" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be an error reported for the return statement.
    assertEquals(1, traversal.getErrorCount());
    assertEquals(GOOG_SCOPE_USES_RETURN, traversal.getLastErrorType());
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    String jsCode = "goog.scope(function() {" +
        "throw new Error('test');" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be an error reported for the throw statement.
    assertEquals(1, traversal.getErrorCount());
    assertEquals(GOOG_SCOPE_USES_THROW, traversal.getLastErrorType());
  }

  @Test
  public void testScopedAliasesWithImproperlyNamedLocalVariable() {
    String jsCode = "goog.scope(function() {" +
        "var goog = {};" +
        "goog.scope(function() {" +
        "  var goog = 1;" +
        "});" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be an error reported for the non-alias local variable.
    assertEquals(1, traversal.getErrorCount());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, traversal.getLastErrorType());
    assertEquals("goog", traversal.getLastErrorMessage());
  }

  @Test
  public void testScopedAliasesWithTwoShorterScopes() {
    String jsCode = "goog.scope(function() {" +
        "var x = 1;" +
        "goog.scope(function() {" +
        "  var x = 2;" +
        "});" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be no errors reported.
    assertEquals(0, traversal.getErrorCount());

    // The alias for 'x' in the outer scope should not be applied to the inner scope.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertNotNull(aliasDefinitions.get(0));
    assertEquals("x", aliasDefinitions.get(0).getString());
    Node outerX = aliasDefinitions.get(0).getParent();
    assertEquals(1, outerX.getChildCount());
    Node innerX = outerX.getFirstChild();
    Node innerXAssignment = innerX.getLastChild();
    assertNotNull(innerXAssignment);
    assertEquals("=", innerXAssignment.getNodeType());
    assertEquals(2, innerXAssignment.getChildCount());
    Node innerXValue = innerXAssignment.getChild(1);
    assertNotNull(innerXValue);
    assertEquals(Token.NUMBER, innerXValue.getNodeType());
    assertEquals("2", innerXValue.getString());
  }

  @Test
  public void testScopedAliasesWithLongerInnerScope() {
    String jsCode = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "  var x = 1;" +
        "});" +
        "var x = 2;" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be no errors reported.
    assertEquals(0, traversal.getErrorCount());

    // The alias for 'x' in the outer scope should be applied to the inner scope.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertNotNull(aliasDefinitions.get(0));
    assertEquals("x", aliasDefinitions.get(0).getString());
    Node outerX = aliasDefinitions.get(0).getParent();
    Node innerScope = outerX.getParent();
    Node innerX = innerScope.getFirstChild();
    Node innerXAssignment = innerX.getLastChild();
    assertNotNull(innerXAssignment);
    assertEquals("=", innerXAssignment.getNodeType());
    assertEquals(2, innerXAssignment.getChildCount());
    Node innerXValue = innerXAssignment.getChild(1);
    assertNotNull(innerXValue);
    assertEquals(Token.NAME, innerXValue.getNodeType());
    assertEquals("x", innerXValue.getString());
  }

  @Test
  public void testScopedAliasesWithNamespaceShadows() {
    String jsCode = "goog.scope(function() {" +
        "var goog_foo = 1;" +
        "goog.scope(function() {" +
        "  var goog_foo = 2;" +
        "});" +
        "});";
    rootNode = NodePrinter.parseJs(jsCode).getFirstChild();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();

    // There should be no errors reported.
    assertEquals(0, traversal.getErrorCount());

    // The alias for 'goog_foo' in the outer scope should be applied to the inner scope.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertNotNull(aliasDefinitions.get(0));
    assertEquals("goog_foo", aliasDefinitions.get(0).getString());
    Node outerGoogFoo = aliasDefinitions.get(0).getParent();
    Node innerScope = outerGoogFoo.getParent();
    Node innerGoogFoo = innerScope.getFirstChild();
    Node innerGoogFooAssignment = innerGoogFoo.getLastChild();
    assertNotNull(innerGoogFooAssignment);
    assertEquals("=", innerGoogFooAssignment.getNodeType());
    assertEquals(2, innerGoogFooAssignment.getChildCount());
    Node innerGoogFooValue = innerGoogFooAssignment.getChild(1);
    assertNotNull(innerGoogFooValue);
    assertEquals(Token.NAME, innerGoogFooValue.getNodeType());
    assertEquals("goog_foo", innerGoogFooValue.getString());

    // The 'goog_foo' in the inner scope should be renamed.
    String renamedJsCode = "goog.scope(function() {" +
        "var goog__ScopedAliasesTest_foo__1 = 1;" +
        "goog.scope(function() {" +
        "  var goog__ScopedAliasesTest_foo__2 = 2;" +
        "});" +
        "});";
    Node renamedRootNode = NodePrinter.parseJs(renamedJsCode).getFirstChild();
    Traversal renamedTraversal = new ScopedAliases(compiler, null, null).getTraversal();
    Node renamedInnerGoogFoo = renamedTraversal.getCurrentNode();
    assertEquals("goog__ScopedAliasesTest_foo__2", renamedInnerGoogFoo.getString());
  }
}