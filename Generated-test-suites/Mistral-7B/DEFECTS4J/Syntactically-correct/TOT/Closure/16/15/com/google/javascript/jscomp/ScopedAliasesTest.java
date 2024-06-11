import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.RENAME_ALIASED_NAMES;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {
  @Mock private AbstractCompiler compiler;
  @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock private AliasTransformationHandler transformationHandler;

  private HotSwapCompilerPass scopedAliases;
  private Traversal traversal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    traversal = new Traversal();
  }

  @Test
  public void testSimpleAlias() {
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = goog.x; });");

    scopedAliases.process(null, rootNode);

    Node expectedNode = Node.newTextNode("goog.x;");
    Traversal expectedTraversal = new Traversal();

    traversal.applyAliasTransformation(RootNode -> {
      Node n = RootNode.getFirstChild();
      Node aliasDefinition = Node.newNameNode("x");
      Node aliasUsage = Node.newNameNode("goog.x");

      return new AliasedNode(aliasUsage, aliasDefinition);
    });

    assertEquals(expectedTraversal.getAliasDefinitionsInOrder().get(0).getNode(),
        traversal.getAliasDefinitionsInOrder().get(0).getNode());
    assertEquals(expectedTraversal.getAliasUsages().get(0).getNode(),
        traversal.getAliasUsages().get(0).getNode());
    assertEquals(expectedNode.getNodeType(), traversal.getLastNode().getNodeType());
    assertEquals(expectedNode.getNodeType(), rootNode.getLastChild().getNodeType());
  }

  @Test
  public void testMultipleAliases() {
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = goog.x; var y = goog.y; });");

    scopedAliases.process(null, rootNode);

    Node expectedNode = Node.newTextNode("goog.x;");
    Traversal expectedTraversal = new Traversal();

    traversal.applyAliasTransformation(RootNode -> {
      Node n = RootNode.getFirstChild();
      Node aliasDefinitionX = Node.newNameNode("x");
      Node aliasUsageX = Node.newNameNode("goog.x");
      Node aliasDefinitionY = Node.newNameNode("y");
      Node aliasUsageY = Node.newNameNode("goog.y");

      return new AliasedNode(aliasUsageX, aliasDefinitionX) {
        @Override
        public void applyAlias() {
          super.applyAlias();
          ((Node) this.aliasDefinitionY.getParent()).getParent().replaceChild(
              this.aliasDefinitionY, Node.newNameNode("y"));
        }
      };
    });

    assertEquals(expectedTraversal.getAliasDefinitionsInOrder().size(),
        traversal.getAliasDefinitionsInOrder().size());
    assertEquals(expectedTraversal.getAliasUsages().size(),
        traversal.getAliasUsages().size());
    assertEquals(expectedNode.getNodeType(), traversal.getLastNode().getNodeType());
    assertEquals(expectedNode.getNodeType(), rootNode.getLastChild().getNodeType());
  }

  @Test
  public void testAliasesWithTypeNode() {
    Node rootNode = Node.newTextNode("goog.scope(function() { var x: number = goog.x; });");

    scopedAliases.process(null, rootNode);

    Node expectedNode = Node.newTextNode("goog.x: number;");
    Traversal expectedTraversal = new Traversal();

    traversal.applyAliasTransformation(RootNode -> {
      Node n = RootNode.getFirstChild();
      Node aliasDefinition = Node.newNameNode("x");
      Node aliasUsage = Node.newNameNode("goog.x");

      return new AliasedTypeNode(aliasUsage, "x") {
        @Override
        public void applyAlias() {
          super.applyAlias();
          aliasUsage.setString("goog.x: number");
        }
      };
    });

    assertEquals(expectedTraversal.getAliasDefinitionsInOrder().get(0).getNode(),
        traversal.getAliasDefinitionsInOrder().get(0).getNode());
    assertEquals(expectedTraversal.getAliasUsages().get(0).getNode(),
        traversal.getAliasUsages().get(0).getNode());
    assertEquals(expectedNode.getNodeType(), traversal.getLastNode().getNodeType());
    assertEquals(expectedNode.getNodeType(), rootNode.getLastChild().getNodeType());
  }

  @Test(expected = AssertionError.class)
  public void testMultipleStatementsInScope() {
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = goog.x; var y = goog.y; goog.dom.createElement('div'); });");

    scopedAliases.process(null, rootNode);

    fail("Expected an error: JSC_GOOG_SCOPE_USED_IMPROPERLY");
  }

  @Test(expected = AssertionError.class)
  public void testNonFunctionCallToGoogScope() {
    Node rootNode = Node.newTextNameNode("goog.scope");

    scopedAliases.process(null, rootNode);

    fail("Expected an error: JSC_GOOG_SCOPE_USED_IMPROPERLY");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithNonAnonymousFunction() {
    Node rootNode = Node.newTextNode("goog.scope(function googScope() {});");

    scopedAliases.process(null, rootNode);

    fail("Expected an error: JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithParameters() {
    Node rootNode = Node.newTextNode("goog.scope(function(a) {});");

    scopedAliases.process(null, rootNode);

    fail("Expected an error: JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
  }

  @Test
  public void testGoogScopeWithThis() {
    Node rootNode = Node.newTextNode("goog.scope(function() { this.x = 42; });");

    scopedAliases.process(null, rootNode);

    SourcePosition position = new SourcePosition();
    Node lastNode = traversal.getLastNode();
    compiler.report(new DiagnosticPosition(position, rootNode, GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test
  public void testGoogScopeWithReturn() {
    Node rootNode = Node.newTextNode("goog.scope(function() { return 42; });");

    scopedAliases.process(null, rootNode);

    SourcePosition position = new SourcePosition();
    Node lastNode = traversal.getLastNode();
    compiler.report(new DiagnosticPosition(position, rootNode, GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testGoogScopeWithThrow() {
    Node rootNode = Node.newTextNode("goog.scope(function() { throw new Error(); });");

    scopedAliases.process(null, rootNode);

    SourcePosition position = new SourcePosition();
    Node lastNode = traversal.getLastNode();
    compiler.report(new DiagnosticPosition(position, rootNode, GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testGlobalScopeAlias() {
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = goog.x; });");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(RENAME_ALIASED_NAMES);

    AbstractCompiler compiler = new AbstractCompiler(options);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node result = Node.newTextNode("");
    scopedAliases.process(null, rootNode);
    compiler.process(null, rootNode, result);

    Node expectedNode = Node.newTextNode("goog.x;");

    assertEquals(expectedNode.getNodeType(), result.getLastChild().getNodeType());
  }

  @Test
  public void testGlobalScopeAliasWithTypeNode() {
    Node rootNode = Node.newTextNode("goog.scope(function() { var x: number = goog.x; });");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(RENAME_ALIASED_NAMES);

    AbstractCompiler compiler = new AbstractCompiler(options);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node result = Node.newTextNode("");
    scopedAliases.process(null, rootNode);
    compiler.process(null, rootNode, result);

    Node expectedNode = Node.newTextNode("goog.x: number;");

    assertEquals(expectedNode.getNodeType(), result.getLastChild().getNodeType());
  }

  @Test
  public void testLocalScopeAlias() {
    Node rootNode = Node.newTextNode("(function() { goog.scope(function() { var x = goog.x; }); })();");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(RENAME_ALIASED_NAMES);

    AbstractCompiler compiler = new AbstractCompiler(options);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node result = Node.newTextNode("");
    scopedAliases.process(null, rootNode);
    compiler.process(null, rootNode, result);

    Node expectedNode = Node.newTextNode("goog.x;");

    assertEquals(expectedNode.getNodeType(), result.getLastChild().getNodeType());
  }

  @Test
  public void testLocalScopeAliasWithTypeNode() {
    Node rootNode = Node.newTextNode("(function() { goog.scope(function() { var x: number = goog.x; }); })();");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(RENAME_ALIASED_NAMES);

    AbstractCompiler compiler = new AbstractCompiler(options);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node result = Node.newTextNode("");
    scopedAliases.process(null, rootNode);
    compiler.process(null, rootNode, result);

    Node expectedNode = Node.newTextNode("goog.x: number;");

    assertEquals(expectedNode.getNodeType(), result.getLastChild().getNodeType());
  }
}