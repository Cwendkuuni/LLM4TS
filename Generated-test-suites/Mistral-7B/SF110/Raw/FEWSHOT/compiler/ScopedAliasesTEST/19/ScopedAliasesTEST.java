package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Util;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    traversal = new Traversal();
  }

  @Test
  public void testProcess() {
    Node root = createTestNode();
    scopedAliases.process(null, root);
    verifyAliasDefinitionsAppliedInOrder();
    verifyAliasUsagesRemoved();
    verifyScopeCallsCollapsed();
  }

  @Test
  public void testHotSwapScript() {
    Node root = createTestNode();
    scopedAliases.hotSwapScript(root, null);
    verifyAliasDefinitionsAppliedInOrder();
    verifyAliasUsagesRemoved();
    verifyScopeCallsCollapsed();
  }

  private Node createTestNode() {
    Node scopeCall = createScopeCallNode();
    Node root = new Node(new Node(new Node(Token.NAME, "goog"), Token.DOT, Token.NAME, "scope"),
        scopeCall);
    return root;
  }

  private Node createScopeCallNode() {
    Node function = createFunctionNode("function() { var x = goog.x; }");
    Node scopeCall = new Node(Token.NAME, "goog.scope");
    scopeCall.appendChild(function);
    return scopeCall;
  }

  private Node createFunctionNode(String functionCode) {
    Node function = new Node(Token.LPAREN, null);
    function.setEndPosition(createSourcePosition(1, 1, 1, 1));
    function = new Node(
        new Node(Token.FUNCTION, null, functionCode, "anonymous", null, null), function);
    function.setEndPosition(createSourcePosition(1, 1, functionCode.length() + 1, 1));
    return function;
  }

  private SourcePosition<AliasTransformation> createSourcePosition(int line, int charPosition, int endLine, int endCharPosition) {
    SourcePosition<AliasTransformation> pos = new SourcePosition<AliasTransformation>() {};
    pos.setPositionInformation(line, charPosition, endLine, endCharPosition);
    return pos;
  }

  private void verifyAliasDefinitionsAppliedInOrder() {
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    Node xDefinition = aliasDefinitionsInOrder.get(0);
    Node googDefinition = aliasDefinitionsInOrder.get(1);

    assertEquals("x", xDefinition.getString());
    assertEquals("goog", googDefinition.getString());
  }

  private void verifyAliasUsagesRemoved() {
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
  }

  private void verifyScopeCallsCollapsed() {
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node collapsedScopeCall = scopeCalls.get(0);
    assertEquals("goog.scope(function() { var x = goog.x; })",
        collapsedScopeCall.getString());
  }
}

class TraversalTest extends Traversal {
  @Override
  public void enterScope(NodeTraversal t) {
    // Do nothing in tests.
  }

  @Override
  public void exitScope(NodeTraversal t) {
    // Do nothing in tests.
  }
}

class AliasUsageTest implements ScopedAliases.AliasUsage {
  private final Node aliasReference;

  private final Node aliasDefinition;

  AliasUsageTest(Node aliasReference, Node aliasDefinition) {
    this.aliasReference = aliasReference;
    this.aliasDefinition = aliasDefinition;
  }

  @Override
  public void applyAlias() {
    // Do nothing in tests.
  }
}

class AliasedNodeTest implements ScopedAliases.AliasUsage, ScopedAliases.AliasedNode {
  private final Node aliasReference;

  private final Node aliasDefinition;

  AliasedNodeTest(Node aliasReference, Node aliasDefinition) {
    super();
    this.aliasReference = aliasReference;
    this.aliasDefinition = aliasDefinition;
  }

  @Override
  public void applyAlias() {
    // Do nothing in tests.
  }

  @Override
  public Node getAliasDefinition() {
    return aliasDefinition;
  }

  @Override
  public Node getAliasReference() {
    return aliasReference;
  }
}

class AliasedTypeNodeTest implements ScopedAliases.AliasUsage, ScopedAliases.AliasedTypeNode {
  private final Node typeReference;
  private final Node aliasDefinition;
  private final String aliasName;

  AliasedTypeNodeTest(Node typeReference, Node aliasDefinition, String aliasName) {
    super();
    this.typeReference = typeReference;
    this.aliasDefinition = aliasDefinition;
    this.aliasName = aliasName;
  }

  @Override
  public void applyAlias() {
    // Do nothing in tests.
  }

  @Override
  public Node getAliasDefinition() {
    return aliasDefinition;
  }

  @Override
  public Node getAliasReference() {
    return typeReference;
  }

  @Override
  public String getAliasName() {
    return aliasName;
  }
}

class AliasTransformationHandlerTest implements ScopedAliases.AliasTransformationHandler {
  @Override
  public AliasTransformation logAliasTransformation(String sourceFileName,
      SourcePosition<AliasTransformation> position) {
    AliasTransformation aliasTransformation = new AliasTransformation();
    aliasTransformation.setSourceFileName(sourceFileName);
    aliasTransformation.setSourcePosition(position);
    return aliasTransformation;
  }
}

class PreprocessorSymbolTableTest {
  // PreprocessorSymbolTable does not have any methods that can be easily tested.
}

class AbstractCompilerTest {
  // AbstractCompiler does not have any methods that can be easily tested.
}

class NodeTest {
  // Node does not have any methods that can be easily tested.
}

class NodeTraversalTest {
  // NodeTraversal does not have any methods that can be easily tested.
}

class ScopeTest {
  // Scope does not have any methods that can be easily tested.
}

class VarTest {
  // Var does not have any methods that can be easily tested.
}

class SourcePositionTest {
  // SourcePosition does not have any methods that can be easily tested.
}