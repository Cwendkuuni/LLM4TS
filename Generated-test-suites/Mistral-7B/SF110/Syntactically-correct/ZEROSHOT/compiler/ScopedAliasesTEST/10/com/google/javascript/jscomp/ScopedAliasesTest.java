import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createCallExpr;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNameExpr;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createLiteral;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.NodeFactory.createReturnStmt;
import static com.google.javascript.rhino.NodeFactory.createThrowStmt;
import static com.google.javascript.rhino.NodeFactory.createThrowStmt;
import static com.google.javascript.rhino.NodeFactory.createFunctionCall;
import static com.google.javascript.rhino.NodeFactory.createQualifiedName;
import static com.google.javascript.rhino.NodeFactory.createQualifiedName;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createVarScope;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createScript;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopeCall;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.NodeFactory;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private Traversal traversal;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {};
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
    traversal = new Traversal();
  }

  @Test
  public void testValidScopedAliases() {
    Node root = createScript(
        "goog.scope(function() {" +
            "var x = goog.x;" +
            "goog.scope(function() {" +
            "  var y = goog.y;" +
            "});" +
            "goog.scope(function() {" +
            "  var z = goog.z;" +
            "});" +
            "goog.scope(function() {" +
            "  var w = goog.w;" +
            "});" +
            "goog.scope(function() {" +
            "  var v = goog.v;" +
            "});" +
            "goog.scope(function() {" +
            "  var u = goog.u;" +
            "});" +
            "goog.scope(function() {" +
            "  var t = goog.t;" +
            "});" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertEquals(5, scopeCalls.size());

    Node firstScopeCall = scopeCalls.get(0);
    assertEquals(createCallExpr(createIdentifier("goog"), createFunctionCall(createName("scope"))), firstScopeCall);

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(5, aliasUsages.size());

    AliasUsage xAliasUsage = aliasUsages.get(0);
    assertEquals(createName("x"), xAliasUsage.getAliasReference());
    assertEquals(createName("x"), xAliasUsage.getAliasDefinition());

    AliasUsage yAliasUsage = aliasUsages.get(1);
    assertEquals(createName("y"), yAliasUsage.getAliasReference());
    assertEquals(createName("y"), yAliasUsage.getAliasDefinition());

    AliasUsage zAliasUsage = aliasUsages.get(2);
    assertEquals(createName("z"), zAliasUsage.getAliasReference());
    assertEquals(createName("z"), zAliasUsage.getAliasDefinition());

    AliasUsage wAliasUsage = aliasUsages.get(3);
    assertEquals(createName("w"), wAliasUsage.getAliasReference());
    assertEquals(createName("w"), wAliasUsage.getAliasDefinition());

    AliasUsage vAliasUsage = aliasUsages.get(4);
    assertEquals(createName("v"), vAliasUsage.getAliasReference());
    assertEquals(createName("v"), vAliasUsage.getAliasDefinition());
  }

  @Test
  public void testInvalidScopedAliases_MissingFunction() {
    Node root = createScript(
        "goog.scope(function() {" +
            "var x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getTypeName());
    assertEquals("The call to goog.scope must be alone in a single statement.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidScopedAliases_FunctionWithParameters() {
    Node root = createScript(
        "goog.scope(function(a, b) {" +
            "var x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostic.getTypeName());
    assertEquals("The call to goog.scope must take only a single parameter.  It must be an anonymous function that itself takes no parameters.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidScopedAliases_FunctionWithReturnStatement() {
    Node root = createScript(
        "goog.scope(function() {" +
            "return 5;" +
            "var x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getTypeName());
    assertEquals("The body of a goog.scope function cannot use 'return'.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidScopedAliases_FunctionWithThrowStatement() {
    Node root = createScript(
        "goog.scope(function() {" +
            "throw new Error();" +
            "var x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getTypeName());
    assertEquals("The body of a goog.scope function cannot use 'throw'.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidScopedAliases_FunctionWithThisKeyword() {
    Node root = createScript(
        "goog.scope(function() {" +
            "this.x = 5;" +
            "var x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostic.getTypeName());
    assertEquals("The body of a goog.scope function cannot reference 'this'.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidScopedAliases_MultipleDefinitions() {
    Node root = createScript(
        "goog.scope(function() {" +
            "var x = goog.x;" +
            "var x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    assertEquals(createName("x"), aliasUsage.getAliasReference());
    assertEquals(createName("x"), aliasUsage.getAliasDefinition());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getTypeName());
    assertEquals("The alias x is assigned a value more than once.", diagnostic.getMessage());
  }

  @Test
  public void testInvalidScopedAliases_NonAliasLocalVariable() {
    Node root = createScript(
        "goog.scope(function() {" +
            "var x = 5;" +
            "var goog_x = goog.x;" +
            "});");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostic.getTypeName());
    assertEquals("The local variable x is in a goog.scope and is not an alias.", diagnostic.getMessage());
  }

  @Test
  public void testAliasTransformation() {
    Node root = createScript(
        "goog.scope(function() {" +
            "var goog_dom = goog.dom;" +
            "goog_dom.createElement('div');" +
            "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    AliasUsage googDomAliasUsage = aliasUsages.get(0);
    assertEquals(createName("goog_dom"), googDomAliasUsage.getAliasReference());
    assertEquals(createQualifiedName(createIdentifier("dom"), createIdentifier("goog")),
        googDomAliasUsage.getAliasDefinition());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    Node transformedRoot = traversal.getTransformedRoot();
    assertEquals(createFunctionCall(createIdentifier("goog_dom"),
        createQualifiedName(createIdentifier("createElement"), createIdentifier("goog_dom"))),
        transformedRoot);
  }
}