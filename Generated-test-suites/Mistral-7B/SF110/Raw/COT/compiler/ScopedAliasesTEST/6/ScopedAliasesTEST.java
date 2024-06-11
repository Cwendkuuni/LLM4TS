import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private static final String CLASS_NAME = "com.google.javascript.jscomp.ScopedAliases";
  private static final String ROOT_NODE_NAME = "root";
  private static final String SCOPE_FUNCTION_NAME = "goog.scope";

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions compilerOptions;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.RENAME);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {}) {
      @Override
      public void process(Node externs, Node root) {}

      @Override
      public void hotSwapScript(Node root, Node originalRoot) {}
    };

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testScopedAliases_BasicFunctionality() {
    Node root = createNode("function foo() {}");
    Node scopeCall = createNode(SCOPE_FUNCTION_NAME + "(" + "function bar() {}" + ")");
    Node aliasDefinition = createNode("var x = goog.x;");

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();

    assertThat(scopeCalls).hasSize(1);
    assertThat(aliasDefinitionsInOrder).hasSize(1);

    Node scopeClosureBlock = scopeCalls.get(0).getLastChild();
    Node aliasNode = aliasDefinitionsInOrder.get(0).getFirstChild();

    verify(compiler).reportCodeChange();

    NodeUtil.tryMergeBlock(scopeClosureBlock);

    NodeUtil.tryMergeBlock(aliasNode.getParent());

    NodeUtil.tryMergeBlock(root);

    Node newScopeCall = scopeCalls.get(0).cloneTree();
    Node newAliasDefinition = aliasDefinitionsInOrder.get(0).cloneTree();

    Node newScopeRoot = createNode("function foo() {" + newScopeCall.toString() +
                                   " var x = " + newAliasDefinition.toString() + "}");

    Traversal newTraversal = new Traversal();

    NodeTraversal.traverse(compiler, newScopeRoot, newTraversal);

    Node newScopeClosureBlock = newTraversal.getScopeCalls().get(0).getLastChild();
    Node newAliasNode = newTraversal.getAliasDefinitionsInOrder().get(0).getFirstChild();

    assertThat(newScopeClosureBlock.toString()).isEqualTo(scopeClosureBlock.toString());
    assertThat(newAliasNode.toString()).isEqualTo(aliasNode.toString());
  }

  @Test
  public void testScopedAliases_MultipleAliases() {
    Node root = createNode(
        "function foo() {" +
            "var x = goog.x;" +
            "var y = goog.y;" +
            "var z = goog.z;" +
            "}"
    );

    Node scopeCall = createNode(SCOPE_FUNCTION_NAME + "(" + "function bar() {}" + ")");
    Node aliasDefinitionX = createNode("var x = goog.x;");
    Node aliasDefinitionY = createNode("var y = goog.y;");
    Node aliasDefinitionZ = createNode("var z = goog.z;");

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();

    assertThat(scopeCalls).hasSize(1);
    assertThat(aliasDefinitionsInOrder).hasSize(3);

    Node scopeClosureBlock = scopeCalls.get(0).getLastChild();
    Node aliasNodeX = aliasDefinitionsInOrder.get(0).getFirstChild();
    Node aliasNodeY = aliasDefinitionsInOrder.get(1).getFirstChild();
    Node aliasNodeZ = aliasDefinitionsInOrder.get(2).getFirstChild();

    verify(compiler).reportCodeChange();

    NodeUtil.tryMergeBlock(scopeClosureBlock);

    NodeUtil.tryMergeBlock(aliasNodeX.getParent());
    NodeUtil.tryMergeBlock(aliasNodeY.getParent());
    NodeUtil.tryMergeBlock(aliasNodeZ.getParent());

    NodeUtil.tryMergeBlock(root);

    Node newScopeRoot = createNode(
        "function foo() {" +
            "var x = " + aliasDefinitionX.toString() + ";" +
            "var y = " + aliasDefinitionY.toString() + ";" +
            "var z = " + aliasDefinitionZ.toString() + "}"
    );

    Traversal newTraversal = new Traversal();

    NodeTraversal.traverse(compiler, newScopeRoot, newTraversal);

    List<Node> newScopeCalls = newTraversal.getScopeCalls();
    List<Node> newAliasDefinitionsInOrder = newTraversal.getAliasDefinitionsInOrder();

    Node newScopeClosureBlock = newScopeCalls.get(0).getLastChild();
    Node newAliasNodeX = newAliasDefinitionsInOrder.get(0).getFirstChild();
    Node newAliasNodeY = newAliasDefinitionsInOrder.get(1).getFirstChild();
    Node newAliasNodeZ = newAliasDefinitionsInOrder.get(2).getFirstChild();

    assertThat(newScopeClosureBlock.toString()).isEqualTo(scopeClosureBlock.toString());
    assertThat(newAliasNodeX.toString()).isEqualTo(aliasNodeX.toString());
    assertThat(newAliasNodeY.toString()).isEqualTo(aliasNodeY.toString());
    assertThat(newAliasNodeZ.toString()).isEqualTo(aliasNodeZ.toString());
  }

  @Test
  public void testScopedAliases_AliasRedefined() {
    Node root = createNode("function foo() {" +
                           "var x = goog.x;" +
                           "var x = goog.x;" +
                           "}"
    );

    Node scopeCall = createNode(SCOPE_FUNCTION_NAME + "(" + "function bar() {}" + ")");
    Node aliasDefinitionX = createNode("var x = goog.x;");

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();

    assertThat(scopeCalls).hasSize(1);
    assertThat(aliasDefinitionsInOrder).hasSize(1);

    verify(compiler).reportError(any(), any(), "The alias x is assigned a value more than once.");

    NodeUtil.tryMergeBlock(root);

    Traversal newTraversal = new Traversal();

    NodeTraversal.traverse(compiler, root, newTraversal);

    List<Node> newScopeCalls = newTraversal.getScopeCalls();
    List<Node> newAliasDefinitionsInOrder = newTraversal.getAliasDefinitionsInOrder();

    assertThat(newScopeCalls).hasSize(0);
    assertThat(newAliasDefinitionsInOrder).hasSize(0);
  }

  @Test
  public void testScopedAliases_InvalidScopeCall() {
    Node root = createNode("function foo() {" +
                           "goog.scope(" +
                           "function bar() {" +
                           "}" +
                           ")" +
                           "}"
    );

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();

    assertThat(scopeCalls).hasSize(0);
  }

  @Test
  public void testScopedAliases_ScopeCallWithParameters() {
    Node root = createNode("function foo() {" +
                           "goog.scope(" +
                           "function bar(x) {" +
                           "}" +
                           ")" +
                           "}"
    );

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();

    assertThat(scopeCalls).hasSize(0);
  }

  @Test
  public void testScopedAliases_ScopeCallWithReturnStatement() {
    Node root = createNode("function foo() {" +
                           "goog.scope(" +
                           "function bar() {" +
                           "return 42;" +
                           "}" +
                           ")" +
                           "}"
    );

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();

    assertThat(scopeCalls).hasSize(0);
  }

  @Test
  public void testScopedAliases_ScopeCallWithThrowStatement() {
    Node root = createNode("function foo() {" +
                           "goog.scope(" +
                           "function bar() {" +
                           "throw new Error('Test Error');" +
                           "}" +
                           ")" +
                           "}"
    );

    Traversal traversal = new Traversal();

    NodeTraversal.traverse(compiler, root, traversal);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = traversal.getScopeCalls();

    assertThat(scopeCalls).hasSize(0);
  }

  private Node createNode(String nodeText) {
    return NodeFactory.createNodeFromPrintable(
        new Printable() {
          @Override
          public Node toNode(Node root, int pos) {
            return NodeFactory.createPrintableNode(root, new SourcePosition(ROOT_NODE_NAME, pos), null, nodeText);
          }

          @Override
          public String toString() {
            return nodeText;
          }
        });
  }

  private static class Printable {
    @Override
    public Node toNode(Node root, int pos) {
      return null;
    }

    @Override
    public String toString() {
      return "";
    }
  }
}