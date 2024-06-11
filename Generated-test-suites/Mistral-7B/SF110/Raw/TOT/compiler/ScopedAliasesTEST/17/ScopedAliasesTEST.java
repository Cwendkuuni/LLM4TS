import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.ALIAS_TRANSFORMATION_ENABLED;
import static com.google.javascript.jscomp.DiagnosticType.error;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testSimpleScopedAlias() {
    SourceFile sourceFile = new SourceFile("test.js", "test");

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createTree(sourceFile, "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
    Node aliasNode = ((AliasedNode) aliasUsage).aliasDefinition;

    assertEquals("goog.dom", aliasReference.getString());
    assertEquals("dom", aliasNode.getString());
  }

  @Test
  public void testMalformedScopedAlias() {
    SourceFile sourceFile = new SourceFile("test.js", "test");

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createTree(sourceFile, "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "  goog.scope(function() {});" +
        "});");

    scopedAliases.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();

    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_USED_IMPROPERLY), diagnostics.get(0));
  }

  @Test
  public void testAliasRedefinition() {
    SourceFile sourceFile = new SourceFile("test.js", "test");

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createTree(sourceFile, "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  var DIV = 42;" +
        "  dom.createElement(DIV); " +
        "});");

    scopedAliases.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();

    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_ALIAS_REDEFINED, "DIV"), diagnostics.get(0));
  }

  @Test
  public void testAliasUsageInFunction() {
    SourceFile sourceFile = new SourceFile("test.js", "test");

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createTree(sourceFile, "function test() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = dom.TagName.DIV;" +
        "    dom.createElement(DIV); " +
        "  });" +
        "  dom.createElement(DIV); " +
        "}");

    scopedAliases.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();

    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testAliasUsageInGlobalScope() {
    SourceFile sourceFile = new SourceFile("test.js", "test");

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createTree(sourceFile, "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});" +
        "dom.createElement(DIV);");

    scopedAliases.process(null, root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();

    assertEquals(1, diagnostics.size());
    assertEquals(error(GOOG_SCOPE_USED_IMPROPERLY), diagnostics.get(0));
  }

  @Test
  public void testAliasUsageInScopeWithAliases() {
    SourceFile sourceFile = new SourceFile("test.js", "test");

    Compiler compilerMock = mock(Compiler.class);
    PreprocessorSymbolTable preprocessorSymbolTableMock = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    when(compilerMock.getDiagnostics()).thenReturn(ImmutableList.of());
    when(preprocessorSymbolTableMock.addReference(Node.assertString("dom"))).thenReturn(null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compilerMock, preprocessorSymbolTableMock, transformationHandler);

    Node root = Node.createTree(sourceFile, "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var googScope = goog.scope;" +
        "  googScope(function() {" +
        "    var dom2 = goog.dom;" +
        "    var DIV2 = dom2.TagName.DIV;" +
        "    dom.createElement(DIV2); " +
        "  });" +
        "  dom.createElement(DIV); " +
        "});");

    scopedAliases.process(null, root);

    List<DiagnosticType> diagnostics = compilerMock.getDiagnostics();

    assertEquals(0, diagnostics.size());
  }
}