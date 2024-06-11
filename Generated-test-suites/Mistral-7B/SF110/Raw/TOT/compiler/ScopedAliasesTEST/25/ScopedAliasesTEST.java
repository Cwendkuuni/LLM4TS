import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.LogLevel;

import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private HotSwapCompilerPass compilerPass;
  private AliasTransformationHandler handler;
  private Traversal traversal;

  @Before
  public void setup() {
    handler = Mockito.mock(AliasTransformationHandler.class);
    Mockito.when(handler.logAliasTransformation(Mockito.anyString(), Mockito.any(SourcePosition.class)))
        .thenReturn(new AliasTransformation(LogLevel.ERROR, "testAlias"));

    compilerPass = new ScopedAliases(Mockito.mock(AbstractCompiler.class), null, handler);
    traversal = new Traversal();
  }

  @Test
  public void testValidScopedAliases() {
    SourcePosition<AliasTransformation> position = new SourcePosition<AliasTransformation>() {};
    Node root = new Node("goog.scope(function() {" +
        "  var goog = goog || {};" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "})");

    NodeTraversal.traverse(compilerPass, null, root, traversal);
    assertEquals(3, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("dom", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.dom", traversal.getAliasDefinitionsInOrder().get(1).getString());
    assertEquals("DIV", traversal.getAliasDefinitionsInOrder().get(2).getString());

    assertEquals(1, traversal.getScopeCalls().size());
    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    Node anonymousFunction = scopeCall.getLastChild();
    assertEquals(1, anonymousFunction.getChildCount());
    Node body = anonymousFunction.getChildAtIndex(0);
    assertEquals(4, body.getChildCount());

    Node variableDeclaration = body.getChildAtIndex(0);
    assertEquals("var", variableDeclaration.getToken().toString());
    assertEquals("goog", variableDeclaration.getChildAtIndex(1).getString());

    Node assignment = body.getChildAtIndex(1);
    assertEquals("=", assignment.getToken().toString());
    assertEquals("goog || {}", assignment.getChildAtIndex(2).getString());

    Node variableDeclaration2 = body.getChildAtIndex(2);
    assertEquals("var", variableDeclaration2.getToken().toString());
    assertEquals("dom", variableDeclaration2.getChildAtIndex(1).getString());

    Node assignment2 = body.getChildAtIndex(3);
    assertEquals("=", assignment2.getToken().toString());
    Node rightHandSide = assignment2.getChildAtIndex(1);
    assertEquals("goog.dom", rightHandSide.getQualifiedName());

    Node methodCall = rightHandSide.getChildren().get(0);
    assertEquals("TagName", methodCall.getQualifiedName());

    Node methodCallArgument = methodCall.getLastChild();
    assertEquals("DIV", methodCallArgument.getString());
  }

  @Test
  public void testInvalidScopedAliases() {
    SourcePosition<AliasTransformation> position = new SourcePosition<AliasTransformation>() {};
    Node root = new Node("goog.scope(function() {" +
        "  var goog = goog || {};" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var d = goog.dom.createElement(DIV);" +
        "})");

    NodeTraversal.traverse(compilerPass, null, root, traversal);

    assertEquals(4, traversal.getErrors().size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_THROW",
        traversal.getErrors().get(0).getType().getName());
    assertEquals(
        "The body of a goog.scope function cannot use 'throw'.",
        traversal.getErrors().get(0).getMessage());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN",
        traversal.getErrors().get(1).getType().getName());
    assertEquals(
        "The body of a goog.scope function cannot use 'return'.",
        traversal.getErrors().get(1).getMessage());

    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS",
        traversal.getErrors().get(2).getType().getName());
    assertEquals(
        "The body of a goog.scope function cannot reference 'this'.",
        traversal.getErrors().get(2).getMessage());

    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        traversal.getErrors().get(3).getType().getName());
    assertEquals(
        "The alias DIV is assigned a value more than once.",
        traversal.getErrors().get(3).getMessage());
  }

  @Test
  public void testScopedAliasesWithNestedFunctions() {
    SourcePosition<AliasTransformation> position = new SourcePosition<AliasTransformation>() {};
    Node root = new Node("goog.scope(function() {" +
        "  function foo() {" +
        "    goog.scope(function() {" +
        "      var bar = goog.dom.createElement('DIV');" +
        "    })();" +
        "  }" +
        "})");

    NodeTraversal.traverse(compilerPass, null, root, traversal);

    assertEquals(1, traversal.getErrors().size());
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        traversal.getErrors().get(0).getType().getName());
    assertEquals(
        "The call to goog.scope must be alone in a single statement.",
        traversal.getErrors().get(0).getMessage());

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("DIV", traversal.getAliasDefinitionsInOrder().get(0).getString());

    assertEquals(3, traversal.getScopeCalls().size());
    Node outerScopeCall = traversal.getScopeCalls().get(0);
    Node outerBody = outerScopeCall.getLastChild();

    Node innerScopeCall = traversal.getScopeCalls().get(1);
    Node innerBody = innerScopeCall.getLastChild();

    assertEquals("function", innerBody.getToken().toString());

    Node fooFunctionDeclaration = traversal.getScopeCalls().get(2);
    Node fooFunctionBody = fooFunctionDeclaration.getLastChild();

    assertEquals("function", fooFunctionBody.getChildAtIndex(0).getToken().toString());
    assertEquals("foo", fooFunctionDeclaration.getString());
  }

  @Test
  public void testScopedAliasesWithTopLevelVariables() {
    SourcePosition<AliasTransformation> position = new SourcePosition<AliasTransformation>() {};
    Node root = new Node("goog.scope(function() {" +
        "  var goog = goog || {};" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var d = goog.dom.createElement(DIV);" +
        "  goog.dom.appendChild(document.body, d);" +
        "})");

    NodeTraversal.traverse(compilerPass, null, root, traversal);

    assertEquals(1, traversal.getErrors().size());
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        traversal.getErrors().get(0).getType().getName());
    assertEquals(
        "The call to goog.scope must be alone in a single statement.",
        traversal.getErrors().get(0).getMessage());

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("DIV", traversal.getAliasDefinitionsInOrder().get(0).getString());

    assertEquals(1, traversal.getScopeCalls().size());
    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    Node anonymousFunction = scopeCall.getLastChild();
    assertEquals(1, anonymousFunction.getChildCount());
    Node body = anonymousFunction.getChildAtIndex(0);
    assertEquals(6, body.getChildCount());

    Node variableDeclaration = body.getChildAtIndex(0);
    assertEquals("var", variableDeclaration.getToken().toString());
    assertEquals("goog", variableDeclaration.getChildAtIndex(1).getString());

    Node assignment = body.getChildAtIndex(1);
    assertEquals("=", assignment.getToken().toString());
    Node rightHandSide = assignment.getChildAtIndex(1);
    assertEquals("goog || {}", rightHandSide.getString());

    Node variableDeclaration2 = body.getChildAtIndex(2);
    assertEquals("var", variableDeclaration2.getToken().toString());
    assertEquals("dom", variableDeclaration2.getChildAtIndex(1).getString());

    Node assignment2 = body.getChildAtIndex(3);
    assertEquals("=", assignment2.getToken().toString());
    Node rightHandSide2 = assignment2.getChildAtIndex(1);
    assertEquals("goog.dom", rightHandSide2.getQualifiedName());

    Node methodCall = rightHandSide2.getChildren().get(0);
    assertEquals("TagName", methodCall.getQualifiedName());

    Node methodCallArgument = methodCall.getLastChild();
    assertEquals("DIV", methodCallArgument.getString());

    Node variableDeclaration3 = body.getChildAtIndex(4);
    assertEquals("var", variableDeclaration3.getToken().toString());
    assertEquals("d", variableDeclaration3.getChildAtIndex(1).getString());

    Node assignment3 = body.getChildAtIndex(5);
    assertEquals("=", assignment3.getToken().toString());
    Node rightHandSide3 = assignment3.getChildAtIndex(1);
    assertEquals("goog.dom.createElement(DIV)", rightHandSide3.getString());

    Node appendChild = rightHandSide3.getChildren().get(0);
    assertEquals("appendChild", appendChild.getQualifiedName());

    Node argument = appendChild.getLastChild();
    assertEquals("d", argument.getString());
  }
}