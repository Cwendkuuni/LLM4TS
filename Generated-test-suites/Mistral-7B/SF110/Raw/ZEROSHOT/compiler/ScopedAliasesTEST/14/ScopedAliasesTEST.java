import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAliasTransformation(RENAME_ALL);
    transformationHandler = new AliasTransformationHandler(options);
  }

  @Test
  public void testScopeAliases() {
    Node rootNode = Node.makeRoot();
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopeRoot(rootNode);

    ScopedAliases scopedAliases = new ScopedAliases(
        new Compiler(options, transformationHandler),
        preprocessorSymbolTable, transformationHandler);

    HotSwapCompilerPass.ProcessResult result =
        scopedAliases.process(null, rootNode);

    Traversal traversal = (Traversal) result.getTraversal();

    // Check that there is only one scope call.
    assertEquals(1, traversal.getScopeCalls().size());

    // Check that there are no alias definitions in the wrong order.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    assertEquals(
        "goog.dom.createElement",
        aliasDefinitions.get(1).getQualifiedName());
    assertEquals(
        "goog.dom.TagName.DIV",
        aliasDefinitions.get(0).getQualifiedName());

    // Check that there are no errors.
    assertFalse(traversal.hasErrors());

    // Check that the alias definitions have been applied.
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    AliasedNode aliasUsage1 = (AliasedNode) aliasUsages.get(0);
    assertEquals("goog.dom.createElement", aliasUsage1.aliasDefinition.getQualifiedName());
    AliasedNode aliasUsage2 = (AliasedNode) aliasUsages.get(1);
    assertEquals("goog.dom", aliasUsage2.aliasDefinition.getQualifiedName());

    // Check that the scope call has been collapsed.
    Node scopeCall = traversal.getScopeCalls().get(0);
    assertNull(scopeCall.getLastChild().getLastChild());

    // Check that the alias references have been replaced.
    Node googScopeCall = scopeCall.getFirstChild().getLastChild();
    Node googScopeFunction = googScopeCall.getLastChild();
    Node googScopeBlock = googScopeFunction.getLastChild();
    assertEquals(2, googScopeBlock.getChildCount());
    Node createElementNode = googScopeBlock.getChildAtIndex(0);
    assertEquals("goog.dom.createElement", createElementNode.getQualifiedName());
    Node tagNameNode = createElementNode.getLastChild();
    assertEquals("DIV", tagNameNode.getQualifiedName());
  }
}