import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestUtil.createNode;
import static com.google.javascript.jscomp.TestUtil.createNodeTraversal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALIASES);
    Compiler compiler = new Compiler(options);

    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node rootNode = createNode(sourceCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // There should be no errors.
    assertEquals(0, traversal.getErrors().size());

    // There should be one scope call.
    assertEquals(1, scopeCalls.size());

    // The scope call should be the first node in the tree.
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    // The scope function should be the last child of the scope call.
    Node scopeFunction = scopeCall.getLastChild();
    assertEquals(Token.FUNCTION, scopeFunction.getType());
    assertTrue(scopeFunction.isAnonymousFunction());

    // The scope function should have exactly one parameter.
    Node scopeFunctionParameter = scopeFunction.getFirstChild();
    assertEquals(Token.LPAREN, scopeFunctionParameter.getType());
    assertEquals(Token.RPAREN, scopeFunctionParameter.getNext().getType());

    // The body of the scope function should be empty.
    assertEquals(0, scopeFunction.getChildCount());

    // There should be one alias definition.
    assertEquals(1, aliasDefinitionsInOrder.size());

    // The alias definition should be the last child of the scope function.
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals("dom", aliasDefinition.getFirstChild().getString());

    // The alias definition should be assigned to a variable named "dom".
    Var domVariable = traversal.getScope().getVar("dom");
    assertEquals(aliasDefinition, domVariable.getNode());

    // The alias definition should be assigned to a local variable.
    assertTrue(domVariable.isLocal());

    // The alias definition should not be assigned to a function variable.
    assertFalse(domVariable.isFunction());

    // The alias definition should not be assigned to a parameter variable.
    assertFalse(domVariable.isParameter());

    // The alias definition should not be assigned to a BLEEDING variable.
    assertFalse(domVariable.isBleedingFunction());

    // There should be one alias usage.
    assertEquals(1, aliasUsages.size());

    // The alias usage should be the node that uses the alias "dom".
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    assertEquals(aliasDefinition, aliasedNode.aliasDefinition);

    // The alias usage should be assigned to a local variable.
    Var domAliasVariable = traversal.getScope().getVar("dom");
    assertEquals(aliasedNode.aliasReference, domAliasVariable.getNode());

    // The alias usage should not be assigned to a function variable.
    assertFalse(domAliasVariable.isFunction());

    // The alias usage should not be assigned to a parameter variable.
    assertFalse(domAliasVariable.isParameter());

    // The alias usage should not be assigned to a BLEEDING variable.
    assertFalse(domAliasVariable.isBleedingFunction());

    // The alias usage should be assigned to the same variable as the alias definition.
    assertEquals(domVariable, domAliasVariable);
  }

  @Test
  public void testScopedAliasesWithTypeNodes() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALIASES);
    Compiler compiler = new Compiler(options);

    String sourceCode = "goog.scope(function() {" +
        "  /** @type {goog.dom.TagName} */" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node rootNode = createNode(sourceCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // There should be no errors.
    assertEquals(0, traversal.getErrors().size());

    // There should be one scope call.
    assertEquals(1, scopeCalls.size());

    // The scope call should be the first node in the tree.
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    // The scope function should be the last child of the scope call.
    Node scopeFunction = scopeCall.getLastChild();
    assertEquals(Token.FUNCTION, scopeFunction.getType());
    assertTrue(scopeFunction.isAnonymousFunction());

    // The scope function should have exactly one parameter.
    Node scopeFunctionParameter = scopeFunction.getFirstChild();
    assertEquals(Token.LPAREN, scopeFunctionParameter.getType());
    assertEquals(Token.RPAREN, scopeFunctionParameter.getNext().getType());

    // The body of the scope function should be empty.
    assertEquals(0, scopeFunction.getChildCount());

    // There should be one alias definition.
    assertEquals(1, aliasDefinitionsInOrder.size());

    // The alias definition should be the last child of the scope function.
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals("DIV", aliasDefinition.getFirstChild().getString());

    // The alias definition should be assigned to a variable named "DIV".
    Var divVariable = traversal.getScope().getVar("DIV");
    assertEquals(aliasDefinition, divVariable.getNode());

    // The alias definition should not be assigned to a function variable.
    assertFalse(divVariable.isFunction());

    // The alias definition should not be assigned to a parameter variable.
    assertFalse(divVariable.isParameter());

    // The alias definition should not be assigned to a BLEEDING variable.
    assertFalse(divVariable.isBleedingFunction());

    // There should be one alias usage.
    assertEquals(1, aliasUsages.size());

    // The alias usage should be the node that uses the alias "DIV".
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedTypeNode);
    AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
    assertEquals(aliasDefinition, aliasedTypeNode.aliasDefinition);

    // The alias usage should be assigned to a type node for the variable "DIV".
    Node divTypeNode = traversal.getScope().getVar("DIV").getNode();
    assertEquals(aliasDefinition.getParent(), divTypeNode.getParent());
    assertEquals(Token.TYPEOF, divTypeNode.getType());
    assertEquals("goog.dom.TagName", aliasedTypeNode.aliasName);
  }

  @Test
  public void testScopedAliasesWithErrors() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_ALIASES);
    Compiler compiler = new Compiler(options);

    String sourceCode = "goog.scope(function() {" +
        "  /** @type {goog.dom.TagName} */" +
        "  function dupFunction(x) {" +
        "    return x;" +
        "  }" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  dupFunction(this);" +
        "});";

    Node rootNode = createNode(sourceCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> errors = traversal.getErrors();

    // There should be one error for "this" reference.
    assertEquals(1, errors.size());

    // The error should be for the "dupFunction" call.
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0).getType());
    assertEquals(2, errors.get(0).getChildren().size());
    assertEquals("dupFunction", errors.get(0).getChildren().get(0).getString());

    // There should be one scope call.
    assertEquals(1, scopeCalls.size());

    // The scope call should be the first node in the tree.
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    // The scope function should be the last child of the scope call.
    Node scopeFunction = scopeCall.getLastChild();
    assertEquals(Token.FUNCTION, scopeFunction.getType());
    assertTrue(scopeFunction.isAnonymousFunction());

    // The scope function should have exactly one parameter.
    Node scopeFunctionParameter = scopeFunction.getFirstChild();
    assertEquals(Token.LPAREN, scopeFunctionParameter.getType());
    assertEquals(Token.RPAREN, scopeFunctionParameter.getNext().getType());

    // The body of the scope function should be empty.
    assertEquals(0, scopeFunction.getChildCount());

    // There should be one alias definition.
    assertEquals(1, aliasDefinitionsInOrder.size());

    // The alias definition should be the last child of the scope function.
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals("DIV", aliasDefinition.getFirstChild().getString());

    // The alias definition should be assigned to a variable named "DIV".
    Var divVariable = traversal.getScope().getVar("DIV");
    assertEquals(aliasDefinition, divVariable.getNode());

    // The alias definition should not be assigned to a function variable.
    assertFalse(divVariable.isFunction());

    // The alias definition should not be assigned to a parameter variable.
    assertFalse(divVariable.isParameter());

    // The alias definition should not be assigned to a BLEEDING variable.
    assertFalse(divVariable.isBleedingFunction());

    // There should be no alias usage.
    assertEquals(0, aliasUsages.size());
  }
}