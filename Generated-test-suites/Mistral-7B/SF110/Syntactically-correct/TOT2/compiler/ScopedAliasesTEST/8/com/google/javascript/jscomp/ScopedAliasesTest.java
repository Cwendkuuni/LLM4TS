import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;

public class ScopedAliasesTest extends CompilationTestBase {

  @Test
  public void testScopedAliases() {
    String jsCode = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  var div = goog.dom.createElement(DIV);"
        + "  assertEquals('div', div.tagName);"
        + "});"
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  var div = goog.dom.createElement(goog.dom.TagName.DIV);"
        + "  assertEquals('div', div.tagName);"
        + "});";

    Node root = parseJs(jsCode);
    compile(root);

    Node googScopeFunction = findNode("goog.scope", root);
    Scope scope = getScope(googScopeFunction);

    // Check that the alias 'dom' is defined and used correctly in the goog.scope block
    assertEquals(1, scope.getVarCount());
    Var domVar = scope.getVarAt(0);
    Node domAlias = findNode("dom", root);
    assertEquals(domAlias, domVar.getNode());

    // Check that the alias 'DIV' is defined and used correctly in the goog.scope block
    assertEquals(2, scope.getVarCount());
    Var divVar = scope.getVarAt(1);
    Node divAlias = findNode("DIV", root);
    assertEquals(divAlias, divVar.getNode());

    // Check that the alias 'goog.dom.createElement' is defined and used correctly in the goog.scope block
    assertEquals(3, scope.getVarCount());
    Var createElementVar = scope.getVarAt(2);
    Node createElementFunction = findNode("goog.dom.createElement", root);
    assertEquals(createElementFunction, createElementVar.getNode());

    // Check that the alias 'div' is defined and used correctly in the goog.scope block
    assertEquals(4, scope.getVarCount());
    Var divVar = scope.getVarAt(3);
    Node divAssignment = findNode("div = goog.dom.createElement(DIV)", root);
    assertEquals(divAssignment, divVar.getNode());

    // Check that the alias 'DIV' is used correctly in the test assertion
    Node tagNameCheck = findNode("assertEquals('div', div.tagName)", root);
    Node divNode = findNode("div", root);
    Node divTagName = findNode("tagName", divNode);
    Node tagNameValue = findNode("'div'", root);
    assertEquals(tagNameCheck, findNode("assertEquals", root));
    assertEquals(tagNameValue, findNode("div.tagName", root));
    assertEquals(divTagName, findNode("div.tagName", root));

    // Test that an error is thrown when 'goog.scope' is called with invalid parameters
    jsCode = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "});"
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "});";

    root = parseJs(jsCode);
    compile(root);

    Node firstScopeCall = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_USED_IMPROPERLY, firstScopeCall);

    Node secondScopeCall = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_HAS_BAD_PARAMETERS, secondScopeCall);

    // Test that an error is thrown when the body of a 'goog.scope' function references 'this'
    jsCode = ""
        + "goog.scope(function() {"
        + "  this.test = 'hello';"
        + "});";

    root = parseJs(jsCode);
    compile(root);

    Node scopeFunction = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_REFERENCES_THIS, scopeFunction);

    // Test that an error is thrown when the body of a 'goog.scope' function uses 'return'
    jsCode = ""
        + "goog.scope(function() {"
        + "  return 'hello';"
        + "});";

    root = parseJs(jsCode);
    compile(root);

    scopeFunction = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_USES_RETURN, scopeFunction);

    // Test that an error is thrown when the body of a 'goog.scope' function uses 'throw'
    jsCode = ""
        + "goog.scope(function() {"
        + "  throw new Error('hello');"
        + "});";

    root = parseJs(jsCode);
    compile(root);

    scopeFunction = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_USES_THROW, scopeFunction);

    // Test that an error is thrown when an alias is redefined
    jsCode = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  var dom = 'hello';"
        + "});";

    root = parseJs(jsCode);
    compile(root);

    scopeFunction = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_ALIAS_REDEFINED, scopeFunction);

    // Test that an error is thrown when a non-alias local variable is used in a 'goog.scope' block
    jsCode = ""
        + "goog.scope(function() {"
        + "  var x = 'hello';"
        + "});";

    root = parseJs(jsCode);
    compile(root);

    scopeFunction = findNode("goog.scope", root);
    assertErrorMessage(GOOG_SCOPE_NON_ALIAS_LOCAL, scopeFunction);
  }

  private static Node findNode(String nodeName, Node root) {
    for (Node child = root.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isName() && child.getString().equals(nodeName)) {
        return child;
      }
      if (child.getChildCount() > 0) {
        Node foundNode = findNode(nodeName, child);
        if (foundNode != null) {
          return foundNode;
        }
      }
    }
    return null;
  }

  private static Scope getScope(Node node) {
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, node, traversal);
    return traversal.getScope();
  }
}