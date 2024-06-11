import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    compiler.setPrintDiagnostics(System.err);
    compiler.setOutputFile("test.js");

    preprocessorSymbolTable = new PreprocessorSymbolTable();
    CompilerOptions options = new CompilerOptions();
    options.setPreprocessorSymbolTable(preprocessorSymbolTable);
    transformationHandler = new AliasTransformationHandler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    compiler.addPass(scopedAliases);
  }

  @After
  public void tearDown() throws Exception {
    compiler.dispose();
  }

  @Test
  public void testValidScopedAliases() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testEmptyScopedBlock() throws Exception {
    String sourceCode = "goog.scope(function() {});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testMultipleStatementsInScopedBlock() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  goog.dom.appendChild(document.body, goog.dom.createElement(DIV));" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testScopedBlockWithFunction() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  function foo() {" +
        "    return 42;" +
        "  }" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithNonFunction() throws Exception {
    String sourceCode = "goog.scope(42);";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithMultipleFunctions() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  function foo() {" +
        "    return 42;" +
        "  }" +
        "  function bar() {" +
        "    return 43;" +
        "  }" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithStatement() throws Exception {
    String sourceCode = "goog.scope(var foo = 42);";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithReturnStatement() throws Exception {
    String sourceCode = "goog.scope(return 42);";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithThisKeyword() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  this.foo = 42;" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithThrowStatement() throws Exception {
    String sourceCode = "goog.scope(throw new Error('test'));";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithAlias() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var googDom = goog.dom;" +
        "  googDom.createElement(DIV);" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testScopedBlockWithAliasRedefined() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var googDom = goog.dom;" +
        "  var DIV = googDom.TagName.DIV;" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0));
  }

  @Test
  public void testScopedBlockWithNonAliasLocal() throws Exception {
    String sourceCode =
        "goog.scope(function() {" +
        "  var foo = 42;" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlock() throws Exception {
    String sourceCode = "goog.scope()";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithParentheses() throws Exception {
    String sourceCode = "goog.scope()()";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithArguments() throws Exception {
    String sourceCode = "goog.scope(1, 2)";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithMultipleParameters() throws Exception {
    String sourceCode = "goog.scope(1, 2, 3)";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithReturnStatementInsideFunction() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  return 42;" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithFunctionInsideFunction() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  function foo() {" +
        "  }" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithThisKeywordInsideFunction() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  function foo() {" +
        "    this.foo = 42;" +
        "  }" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithThrowStatementInsideFunction() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithAliasRedefinedInsideFunction() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var googDom = goog.dom;" +
        "  var DIV = googDom.TagName.DIV;" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0));
  }

  @Test
  public void testInvalidScopedBlockWithNonAliasLocalInsideFunction() throws Exception {
    String sourceCode = "goog.scope(function() {" +
        "  var foo = 42;" +
        "});";

    Node root = compiler.parseTree(sourceCode);
    compiler.process(Collections.singletonList((Node) null), root);

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.get(0));
  }
}