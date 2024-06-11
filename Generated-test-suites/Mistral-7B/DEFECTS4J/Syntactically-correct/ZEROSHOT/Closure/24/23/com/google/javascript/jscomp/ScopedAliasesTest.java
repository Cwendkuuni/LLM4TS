import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.rhino.NodeFactory.createAliasTransformation;
import static com.google.javascript.rhino.NodeFactory.createAliasTransformationHandler;
import static com.google.javascript.rhino.NodeFactory.createDiagnosticType;
import static com.google.javascript.rhino.NodeFactory.createJsDocInfo;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.source.JSDocInfo.createJSDocInfo;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

/**
 * Test class for ScopedAliases.java
 */
public class ScopedAliasesTest {

  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = createAliasTransformationHandler();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testScopedAliases() {
    // Test case 1: No aliasing
    String input = "function foo() {}";
    Node root = createNode(input);
    scopedAliases.process(null, root);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    // Test case 2: Basic aliasing
    input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    root = createNode(input);
    scopedAliases.process(null, root);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node scopeCall = scopeCalls.get(0);
    Node aliasUsage = aliasUsages.get(0);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getString());
    assertEquals(aliasDefinition, aliasUsage.getAliasedNode().getNode());

    // Test case 3: Multiple aliases
    input = "goog.scope(function() {" +
        "  var a = goog.dom;" +
        "  var b = goog.dom;" +
        "  var c = goog.dom;" +
        "  var DIV = a.TagName.DIV;" +
        "  var DIV2 = b.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  goog.dom.createElement(DIV2);" +
        "});";
    root = createNode(input);
    scopedAliases.process(null, root);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(2, aliasUsages.size());
    Node aliasDefinition1 = aliasDefinitionsInOrder.get(0);
    Node aliasDefinition2 = aliasDefinitionsInOrder.get(1);
    Node aliasUsage1 = aliasUsages.get(0);
    Node aliasUsage2 = aliasUsages.get(1);
    assertEquals("a", aliasDefinition1.getString());
    assertEquals("b", aliasDefinition2.getString());
    assertEquals(aliasDefinition1, aliasUsage1.getAliasedNode().getNode());
    assertEquals(aliasDefinition2, aliasUsage2.getAliasedNode().getNode());

    // Test case 4: Nested scopes
    input = "goog.scope(function() {" +
        "  var a = goog.dom;" +
        "  goog.scope(function() {" +
        "    var b = goog.dom;" +
        "    var DIV = b.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.dom.createElement(a.TagName.DIV);" +
        "});";
    root = createNode(input);
    scopedAliases.process(null, root);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    scopeCalls = scopedAliases.getScopeCalls();
    aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(2, scopeCalls.size());
    assertEquals(2, aliasUsages.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage1 = aliasUsages.get(0);
    Node aliasUsage2 = aliasUsages.get(1);
    Node scopeCall1 = scopeCalls.get(0);
    Node scopeCall2 = scopeCalls.get(1);
    assertEquals("a", aliasDefinition.getString());
    assertEquals(SCOPING_METHOD_NAME, scopeCall1.getFirstChild().getString());
    assertEquals(SCOPING_METHOD_NAME, scopeCall2.getFirstChild().getString());
    assertEquals(aliasDefinition, aliasUsage1.getAliasedNode().getNode());
    assertEquals(aliasDefinition, aliasUsage2.getAliasedNode().getNode());

    // Test case 5: Alias redefinition
    input = "goog.scope(function() {" +
        "  var a = goog.dom;" +
        "  var a = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(a);" +
        "});";
    root = createNode(input);
    try {
      scopedAliases.process(null, root);
      fail("Expected an error for alias redefinition.");
    } catch (AssertionError e) {
      DiagnosticType error = e.getCause().getCause().getMessage();
      assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error);
    }

    // Test case 6: Alias using 'this'
    input = "goog.scope(function() {" +
        "  this.a = goog.dom;" +
        "  var DIV = this.a.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    root = createNode(input);
    try {
      scopedAliases.process(null, root);
      fail("Expected an error for alias using 'this'.");
    } catch (AssertionError e) {
      DiagnosticType error = e.getCause().getCause().getMessage();
      assertEquals(GOOG_SCOPE_REFERENCES_THIS, error);
    }

    // Test case 7: Alias using 'return'
    input = "goog.scope(function() {" +
        "  return goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";
    root = createNode(input);
    try {
      scopedAliases.process(null, root);
      fail("Expected an error for alias using 'return'.");
    } catch (AssertionError e) {
      DiagnosticType error = e.getCause().getCause().getMessage();
      assertEquals(GOOG_SCOPE_USES_RETURN, error);
    }

    // Test case 8: Alias using 'throw'
    input = "goog.scope(function() {" +
        "  try {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  } catch (e) {" +
        "    throw e;" +
        "  }" +
        "});";
    root = createNode(input);
    try {
      scopedAliases.process(null, root);
      fail("Expected an error for alias using 'throw'.");
    } catch (AssertionError e) {
      DiagnosticType error = e.getCause().getCause().getMessage();
      assertEquals(GOOG_SCOPE_USES_THROW, error);
    }
  }

  private AliasTransformationHandler createAliasTransformationHandler() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(createAliasTransformation());
    return new AliasTransformationHandler(options);
  }

  private PreprocessorSymbolTable newPreprocessorSymbolTable() {
    PreprocessorSymbolTable symbolTable = new PreprocessorSymbolTable();
    symbolTable.setCompiler(compiler);
    return symbolTable;
  }

  private Scope newScope() {
    Scope scope = createScope();
    scope.setParent(null);
    return scope;
  }

  private Node createScope(int depth) {
    Node scopeNode = createNode(Token.CALL, SCOPING_METHOD_NAME, null);
    Node scopeBlock = createNode(Token.BLOCK, null, null);
    Node anonymousFunction = createNode(Token.FUNCTION, null, null);
    Node body = createNode(Token.BLOCK, null, null);
    Node thisVar = createVar("this", null, null, null, null, null, null, null,
        null);
    Node varNode = null;
    if (depth > 0) {
      varNode = createVar(null, null, null, null, null, null, null, null, null);
      body.addChild(thisVar);
      body.addChild(varNode);
      anonymousFunction.setBody(body);
    }
    scopeBlock.addChild(anonymousFunction);
    scopeNode.setLastChild(scopeBlock);
    Scope scope = newScope();
    scope.setParent(scopeNode);
    scope.setVariables(Lists.newArrayList(thisVar, varNode));
    return scopeNode;
  }

  private Node createVar(String name, Node initialValue, Node aliasName,
      Node aliasValue, Node aliasType, Node aliasJSDocs,
      Node aliasSourcePosition, Node aliasEndPosition, Node aliasParent) {
    Var var = createVar(name, initialValue, null, null);
    Var aliasVar = createVar(aliasName, null, aliasValue, aliasType, aliasJSDocs,
        aliasSourcePosition, aliasEndPosition, aliasParent, var);
    return aliasVar.getNode();
  }

  private Var createVar(String name, Node initialValue, Node parent) {
    Var var = new Var();
    var.setName(name);
    var.setNode(createNode(Token.VAR, null, null));
    if (initialValue != null) {
      var.setInitialValue(initialValue);
    }
    var.setParent(parent);
    return var;
  }

  private Node createJsDocInfo() {
    return createJSDocInfo(null, null, null, null, null, null);
  }

  private Node createJSDocInfo(String tag, Node tagValue, Node name,
      Node description, Node type, Node source) {
    JSDocInfo jsDocInfo = createJsDocInfo();
    jsDocInfo.setTag(tag);
    jsDocInfo.setTagValue(tagValue);
    jsDocInfo.setName(name);
    jsDocInfo.setDescription(description);
    jsDocInfo.setType(type);
    jsDocInfo.setSource(source);
    return jsDocInfo.getNode();
  }

  private Node createNode(int type, Node... children) {
    return createNode(type, null, children);
  }

  private Node createNode(int type, @Nullable Node parent, Node... children) {
    Node node = createNode(type);
    if (parent != null) {
      parent.addChild(node);
    }
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private Node createNode(int type) {
    return createNode(type, null);
  }

  private Node createNode(int type, @Nullable Node parent) {
    return createNode(type, parent, null);
  }
}