import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testValidGoogScope() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function() { var dom = goog.dom; }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).hasSize(1);
    assertThat(aliasUsages).isEmpty();
  }

  @Test
  public void testInvalidGoogScopeNoFunction() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("goog.scope");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_USED_IMPROPERLY, root);
  }

  @Test
  public void testInvalidGoogScopeFunctionNoAnonymousParam() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() {}");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_HAS_BAD_PARAMETERS, googScope);
  }

  @Test
  public void testInvalidGoogScopeFunctionWithParam() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope(x) {}");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_HAS_BAD_PARAMETERS, googScope);
  }

  @Test
  public void testInvalidGoogScopeFunctionWithReturn() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() { return 1; }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_USES_RETURN, googScope);
  }

  @Test
  public void testInvalidGoogScopeFunctionWithThis() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() { this.x = 1; }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_REFERENCES_THIS, googScope);
  }

  @Test
  public void testInvalidGoogScopeFunctionWithThrow() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() { throw new Error(); }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_USES_THROW, googScope);
  }

  @Test
  public void testInvalidGoogScopeFunctionWithAliasDefinition() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() { var x = goog.dom; }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).hasSize(1);
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage = aliasDefinition.getFirstChild();

    verifyAliasDefinition(aliasDefinition, "x", "goog.dom");
    verifyAliasUsage(aliasUsage, aliasDefinition);
  }

  @Test
  public void testInvalidGoogScopeFunctionWithAliasRedefinition() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() { var x = goog.dom; var x = goog.html; }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).hasSize(1);
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage1 = aliasDefinition.getFirstChild();
    Node aliasUsage2 = aliasDefinition.getLastChild();

    verifyAliasDefinition(aliasDefinition, "x", "goog.dom");
    verifyAliasUsage(aliasUsage1, aliasDefinition);
    verifyAliasUsage(aliasUsage2, aliasDefinition);

    verifyErrors(GOOG_SCOPE_ALIAS_REDEFINED, aliasDefinition, "x");
  }

  @Test
  public void testInvalidGoogScopeFunctionWithNonAliasVariable() {
    Node root = new Node();
    Node googScope = createGoogScopeNode("function googScope() { var x = 1; }");
    root.appendChild(googScope);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(aliasUsages).isEmpty();

    verifyErrors(GOOG_SCOPE_NON_ALIAS_LOCAL, googScope, "x");
  }

  private Node createGoogScopeNode(String code) {
    Node root = new Node();
    Node googScope = new Node(Token.CALL, "goog.scope");
    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node body = new Node(Token.BLOCK);
    Node parameter = null;

    Node[] tokens = JSCompiler.parseJS(code, null, null).getTokens();
    NodeUtil.parseTree(tokens, functionNode, 0, tokens.length, false);

    if (functionNode.getParameters().hasChildren()) {
      parameter = functionNode.getParameters().getFirstChild();
      googScope.getLastChild().appendChild(parameter);
    }

    googScope.getLastChild().appendChild(functionNode);
    root.appendChild(googScope);
    body.appendChild(functionNode.getBody());
    functionNode.setBody(body);

    return root;
  }

  private void verifyAliasDefinition(Node aliasDefinition, String name, String value) {
    Node aliasNode = aliasDefinition.getLastChild();
    Node nameNode = aliasNode.getFirstChild();
    Node initialValueNode = aliasNode.getLastChild();

    assertThat(nameNode.getString()).isEqualTo(name);
    assertThat(initialValueNode.getQualifiedName()).isEqualTo(value);
  }

  private void verifyAliasUsage(Node aliasUsage, Node aliasDefinition) {
    Node aliasNode = aliasUsage.getParent();
    Node nameNode = aliasNode.getFirstChild();
    Node aliasReferenceNode = aliasNode.getLastChild();

    Node aliasDefinitionNameNode = aliasDefinition.getFirstChild();
    Node aliasDefinitionValueNode = aliasDefinition.getLastChild();

    assertThat(nameNode).isSameInstanceAs(aliasDefinitionNameNode);
    assertThat(aliasReferenceNode).isSameInstanceAs(aliasDefinitionValueNode);
  }

  private void verifyErrors(DiagnosticType errorType, Node node) {
    verifyErrors(errorType, node, (String[]) new Object[0]);
  }

  private void verifyErrors(DiagnosticType errorType, Node node, String... arguments) {
    AliasTransformation transformation = Mockito.spy(new AliasTransformation());
    when(transformationHandler.logAliasTransformation(Mockito.anyString(), Mockito.any(SourcePosition.class)))
        .thenReturn(transformation);

    scopedAliases.process(null, null);
    scopedAliases.hotSwapScript(null, null);

    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, node, traversal);

    List<Node> errors = traversal.getErrors();

    assertThat(errors).hasSize(1);
    Node error = errors.get(0);
    DiagnosticType error = error.getDiagnosticType();

    assertThat(error).isEqualTo(errorType);
    if (arguments != null && arguments.length > 0) {
      String[] errorArguments = error.getArguments();
      for (int i = 0; i < arguments.length; i++) {
        assertThat(errorArguments[i]).isEqualTo(arguments[i]);
      }
    }
  }
}