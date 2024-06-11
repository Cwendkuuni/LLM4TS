import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Nullable;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testScopedAliasWithSingleUsage() {
    String sourceCode =
        "goog.scope(function() {" +
            "var x = goog.foo;" +
            "goog.bar = goog.baz;" +
            "var y = x;" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    Node fooAliasDefinition = aliasDefinitions.get(0);
    Node bazAliasDefinition = aliasDefinitions.get(1);

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    Node xUsage = aliasUsages.get(0);
    Node yUsage = aliasUsages.get(1);

    assertEquals(fooAliasDefinition, xUsage.applyAlias());
    assertEquals(bazAliasDefinition, yUsage.applyAlias());

    Node expectedRoot = new Node(Token.BLOCK, null);
    expectedRoot.setJSDocInfo(JSDocInfo.createFromRoot(sourceCode));
    expectedRoot.addChild(new Node(Token.GOOG_SCOPE, new Node("goog.scope")));
    expectedRoot.addChild(new Node(Token.FUNCTION, null));
    expectedRoot.addChild(new Node(Token.LP, null));
    expectedRoot.addChild(new Node(Token.VAR, null, new Node("x"), null));
    expectedRoot.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.NAME, null, "goog.foo"), null));
    expectedRoot.addChild(
        new Node(Token.VAR, null, new Node("foo"), null));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.NAME, null, "goog.bar"), null));
    expectedRoot.addChild(new Node(Token.NAME, null, "baz"));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(new Node(Token.VAR, null, new Node("y"), null));
    expectedRoot.addChild(new Node(Token.ASSIGN, null, new Node("x"), null));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(Token.RP, null);

    Node traversedRoot = NodeUtil.deepClone(root);
    scopedAliases.hotSwapScript(root, null);

    assertEquals(expectedRoot, traversedRoot);
  }

  @Test
  public void testScopedAliasWithMultiUsage() {
    String sourceCode =
        "goog.scope(function() {" +
            "var x = goog.foo;" +
            "var y = x;" +
            "goog.bar = goog.baz;" +
            "var z = y;" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    Node fooAliasDefinition = aliasDefinitions.get(0);
    Node bazAliasDefinition = aliasDefinitions.get(1);

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(4, aliasUsages.size());
    Node xUsage = aliasUsages.get(0);
    Node yUsage1 = aliasUsages.get(1);
    Node yUsage2 = aliasUsages.get(2);
    Node zUsage = aliasUsages.get(3);

    assertEquals(fooAliasDefinition, xUsage.applyAlias());
    assertEquals(fooAliasDefinition, yUsage1.applyAlias());
    assertEquals(fooAliasDefinition, yUsage2.applyAlias());
    assertEquals(bazAliasDefinition, zUsage.applyAlias());

    Node expectedRoot = new Node(Token.BLOCK, null);
    expectedRoot.setJSDocInfo(JSDocInfo.createFromRoot(sourceCode));
    expectedRoot.addChild(new Node(Token.GOOG_SCOPE, new Node("goog.scope")));
    expectedRoot.addChild(new Node(Token.FUNCTION, null));
    expectedRoot.addChild(new Node(Token.LP, null));
    expectedRoot.addChild(new Node(Token.VAR, null, new Node("x"), null));
    expectedRoot.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.NAME, null, "goog.foo"), null));
    expectedRoot.addChild(new Node("foo", null));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(new Node(Token.VAR, null, new Node("y"), null));
    expectedRoot.addChild(new Node(Token.ASSIGN, null, new Node("x"), null));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.NAME, null, "goog.bar"), null));
    expectedRoot.addChild(new Node(Token.NAME, null, "baz"));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(new Node(Token.VAR, null, new Node("z"), null));
    expectedRoot.addChild(new Node(Token.ASSIGN, null, new Node("y"), null));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(Token.RP, null);

    Node traversedRoot = NodeUtil.deepClone(root);
    scopedAliases.hotSwapScript(root, null);

    assertEquals(expectedRoot, traversedRoot);
  }

  @Test
  public void testScopedAliasWithBadParameters() {
    String sourceCode =
        "goog.scope(function(foo) {" +
            "goog.bar = goog.baz;" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node expectedRoot = new Node(Token.BLOCK, null);
    expectedRoot.setJSDocInfo(JSDocInfo.createFromRoot(sourceCode));
    expectedRoot.addChild(new Node(Token.GOOG_SCOPE, new Node("goog.scope")));
    expectedRoot.addChild(new Node(Token.FUNCTION, null, new Node("foo"), null));
    expectedRoot.addChild(new Node(Token.LP, null));
    expectedRoot.addChild(Token.RP, null);
    expectedRoot.addChild(Token.RP, null);

    Node traversedRoot = NodeUtil.deepClone(root);
    scopedAliases.hotSwapScript(root, null);

    assertEquals(expectedRoot, traversedRoot);

    assertThrows(RuntimeException.class, () -> {
      scopedAliases.hotSwapScript(root, null);
    });

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0));
  }

  @Test
  public void testScopedAliasWithThisKeyword() {
    String sourceCode =
        "goog.scope(function() {" +
            "this.x = goog.foo;" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThrows(RuntimeException.class, () -> {
      scopedAliases.hotSwapScript(root, null);
    });

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));
  }

  @Test
  public void testScopedAliasWithReturnKeyword() {
    String sourceCode =
        "goog.scope(function() {" +
            "return goog.foo;" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThrows(RuntimeException.class, () -> {
      scopedAliases.hotSwapScript(root, null);
    });

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0));
  }

  @Test
  public void testScopedAliasWithThrowKeyword() {
    String sourceCode =
        "goog.scope(function() {" +
            "throw new Error('Test error');" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThrows(RuntimeException.class, () -> {
      scopedAliases.hotSwapScript(root, null);
    });

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0));
  }

  @Test
  public void testScopedAliasWithRedefinedAlias() {
    String sourceCode =
        "goog.scope(function() {" +
            "var goog = {" +
            "  foo: 'bar'," +
            "  foo: 'baz'" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertThrows(RuntimeException.class, () -> {
      scopedAliases.hotSwapScript(root, null);
    });

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0));
  }

  @Test
  public void testScopedAliasWithUnusedLocal() {
    String sourceCode =
        "goog.scope(function() {" +
            "var unused = goog.foo;" +
            "});";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node expectedRoot = new Node(Token.BLOCK, null);
    expectedRoot.setJSDocInfo(JSDocInfo.createFromRoot(sourceCode));
    expectedRoot.addChild(new Node(Token.GOOG_SCOPE, new Node("goog.scope")));
    expectedRoot.addChild(new Node(Token.FUNCTION, null));
    expectedRoot.addChild(new Node(Token.LP, null));
    expectedRoot.addChild(new Node(Token.VAR, null, new Node("unused"), null));
    expectedRoot.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.NAME, null, "goog.foo"), null));
    expectedRoot.addChild(new Node("foo", null));
    expectedRoot.addChild(Token.SEMICOLON);
    expectedRoot.addChild(Token.RP, null);
    expectedRoot.addChild(Token.RP, null);

    Node traversedRoot = NodeUtil.deepClone(root);
    scopedAliases.hotSwapScript(root, null);

    assertEquals(expectedRoot, traversedRoot);
  }

  @Test
  public void testScopedAliasWithFunctionReturnType() {
    String sourceCode =
        "function myFunction() {" +
            "goog.scope(function() {" +
                "return goog.foo;" +
            "});" +
            "}";

    Node root = Node.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    Node myFunction = root.getLastChild();
    assertEquals(Token.FUNCTION, myFunction.getType());
    assertEquals("myFunction", myFunction.getJSDocInfo().getName());

    Node expectedMyFunction = new Node(Token.FUNCTION, null, null, null);
    expectedMyFunction.setJSDocInfo(JSDocInfo.createFromRoot(sourceCode));
    expectedMyFunction.addChild(new Node(Token.FUNCTION, null));
    expectedMyFunction.addChild(new Node(Token.LP, null));
    expectedMyFunction.addChild(new Node(Token.RP, null));
    expectedMyFunction.addChild(new Node(Token.RP, null));

    Node traversedMyFunction = NodeUtil.deepClone(myFunction);
    scopedAliases.hotSwapScript(root, null);

    assertEquals(expectedMyFunction, traversedMyFunction);
  }
}