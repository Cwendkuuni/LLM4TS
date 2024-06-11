import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  /**
   * Tests that the {@code ScopedAliases} class properly processes a simple
   * goog.scope block.
   */
  public void testSimpleGoogScopeBlock() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);

    SourceFile source = new SourceFile("test.js", "goog.scope test", null);
    Node root = source.getTree();

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    List<AliasUsage> aliasUsages = ((Traversal) pass.getTraversal()).getAliasUsages();
    List<Node> aliasDefinitionsInOrder =
        ((Traversal) pass.getTraversal()).getAliasDefinitionsInOrder();
    List<Node> scopeCalls = ((Traversal) pass.getTraversal()).getScopeCalls();

    // Check that the number of alias definitions, uses, and scope calls
    // matches our expectations.
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    // Check that the alias definition is correctly applied.
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals(4, aliasDefinitionsInOrder.get(0).getChildCount());

    // Check that the alias usage is correctly replaced with the definition.
    assertEquals("goog.dom.createElement", aliasUsages.get(0).applyAlias().getString());

    // Check that the goog.scope call is correctly transformed.
    assertEquals("goog.dom.createElement", scopeCalls.get(0).getLastChild().getLastChild().getString());
  }

  /**
   * Tests that the {@code ScopedAliases} class properly handles errors when
   * processing a goog.scope block.
   */
  public void testGoogScopeErrors() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);

    SourceFile source = new SourceFile("test.js", "goog.scope error test", null);
    Node root = source.getTree();

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    // Test improper usage of goog.scope.
    Node improperUsage = root.getChildAtIndex(0);
    Node scopeCall = improperUsage.getLastChild();
    Node anonymousFn = scopeCall.getLastChild();
    anonymousFn.addChild(new Node(Token.RETURN, null, null));
    pass.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        errors.get(0).getErrorType().getName());

    // Test goog.scope with bad parameters.
    source = new SourceFile("test.js", "goog.scope bad parameters test", null);
    root = source.getTree();

    improperUsage = root.getChildAtIndex(0);
    anonymousFn = improperUsage.getLastChild();
    anonymousFn.addChild(new Node(Token.LP, null, null));
    anonymousFn.addChild(new Node(Token.ID, "x", null));
    anonymousFn.addChild(new Node(Token.RP, null, null));
    pass.process(null, root);

    errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        errors.get(0).getErrorType().getName());

    // Test goog.scope that references 'this'.
    source = new SourceFile("test.js", "goog.scope this test", null);
    root = source.getTree();

    improperUsage = root.getChildAtIndex(0);
    anonymousFn = improperUsage.getLastChild();
    anonymousFn.addChild(new Node(Token.THIS, null, null));
    pass.process(null, root);

    errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS",
        errors.get(0).getErrorType().getName());

    // Test goog.scope that uses 'return'.
    source = new SourceFile("test.js", "goog.scope return test", null);
    root = source.getTree();

    improperUsage = root.getChildAtIndex(0);
    anonymousFn = improperUsage.getLastChild();
    anonymousFn.addChild(new Node(Token.RETURN, null, null));
    pass.process(null, root);

    errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN",
        errors.get(0).getErrorType().getName());

    // Test goog.scope that uses 'throw'.
    source = new SourceFile("test.js", "goog.scope throw test", null);
    root = source.getTree();

    improperUsage = root.getChildAtIndex(0);
    anonymousFn = improperUsage.getLastChild();
    anonymousFn.addChild(new Node(Token.THROW, null, null));
    pass.process(null, root);

    errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_THROW",
        errors.get(0).getErrorType().getName());
  }

  /**
   * Tests that the {@code ScopedAliases} class properly handles alias
   * redefinition errors.
   */
  public void testGoogScopeAliasRedefinition() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);

    SourceFile source = new SourceFile("test.js", "goog.scope alias redefinition test", null);
    Node root = source.getTree();

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    Node googScope = root.getChildAtIndex(0);
    Node aliasDef = googScope.getChildAtIndex(1);

    Node aliasDefinition1 = aliasDef.getLastChild();
    aliasDefinition1.setString("goog.dom");
    Node aliasUsage1 = root.getChildAtIndex(1);
    aliasUsage1.replaceChild(aliasUsage1.getFirstChild(), aliasDefinition1);

    Node aliasDefinition2 = new Node(Token.ASSIGN, null, null);
    aliasDefinition2.setChild(0, aliasDefinition1);
    aliasDefinition2.setChild(1, new Node(Token.ID, "goog.dom", null));
    aliasDef.setLastChild(aliasDefinition2);

    pass.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        errors.get(0).getErrorType().getName());
  }

  /**
   * Tests that the {@code ScopedAliases} class properly handles alias usage
   * errors for non-alias local variables.
   */
  public void testGoogScopeNonAliasLocal() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);

    SourceFile source = new SourceFile("test.js", "goog.scope non-alias local test", null);
    Node root = source.getTree();

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    Node googScope = root.getChildAtIndex(0);
    Node aliasDef = googScope.getChildAtIndex(1);

    Node aliasDefinition1 = aliasDef.getLastChild();
    aliasDefinition1.setString("goog.dom");
    Node aliasUsage1 = root.getChildAtIndex(1);
    aliasUsage1.replaceChild(aliasUsage1.getFirstChild(), aliasDefinition1);

    Node localVar = new Node(Token.VAR, null, null);
    localVar.setChild(0, new Node(Token.ID, "x", null));
    localVar.setChild(1, new Node(Token.ASSIGN, null, null));
    localVar.setChild(1, aliasDefinition1);

    aliasDef.addChild(1, localVar);

    pass.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        errors.get(0).getErrorType().getName());
  }

  /**
   * Tests that the {@code ScopedAliases} class properly handles alias usage
   * errors for local variables in a loop.
   */
  public void testGoogScopeLocalVariableInLoop() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);

    SourceFile source = new SourceFile("test.js", "goog.scope local variable in loop test", null);
    Node root = source.getTree();

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    Node googScope = root.getChildAtIndex(0);
    Node aliasDef = googScope.getChildAtIndex(1);

    Node aliasDefinition1 = aliasDef.getLastChild();
    aliasDefinition1.setString("goog.dom");
    Node aliasUsage1 = root.getChildAtIndex(1);
    aliasUsage1.replaceChild(aliasUsage1.getFirstChild(), aliasDefinition1);

    Node loop = new Node(Token.FOR, null, null);
    Node initializer = new Node(Token.ASSIGN, null, null);
    initializer.setChild(0, new Node(Token.ID, "x", null));
    initializer.setChild(1, aliasDefinition1);
    loop.setChild(0, initializer);

    Node body = new Node(Token.BLOCK, null, null);
    Node statement = new Node(Token.ASSIGN, null, null);
    statement.setChild(0, new Node(Token.ID, "x", null));
    statement.setChild(1, new Node(Token.INTEGER, 1, null));
    body.addChild(statement);

    loop.setChild(1, body);
    loop.setChild(2, new Node(Token.SEMICOLON, null, null));

    aliasDef.addChild(1, loop);

    pass.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        errors.get(0).getErrorType().getName());
  }

  /**
   * Tests that the {@code ScopedAliases} class properly handles alias
   * definitions in loop initialization.
   */
  public void testGoogScopeAliasDefinitionInLoopInitializer() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);

    SourceFile source = new SourceFile("test.js", "goog.scope alias definition in loop initializer test", null);
    Node root = source.getTree();

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);

    Node googScope = root.getChildAtIndex(0);
    Node aliasDef = googScope.getChildAtIndex(1);

    Node aliasDefinition1 = aliasDef.getLastChild();
    aliasDefinition1.setString("goog.dom");
    Node aliasUsage1 = root.getChildAtIndex(1);
    aliasUsage1.replaceChild(aliasUsage1.getFirstChild(), aliasDefinition1);

    Node loop = new Node(Token.FOR, null, null);
    Node initializer = new Node(Token.ASSIGN, null, null);
    Node aliasDefinition2 = new Node(Token.ASSIGN, null, null);
    aliasDefinition2.setChild(0, new Node(Token.ID, "goog.dom", null));
    aliasDefinition2.setChild(1, new Node(Token.ID, "y", null));
    initializer.setChild(0, aliasDefinition2);
    initializer.setChild(1, aliasDefinition1);
    loop.setChild(0, initializer);

    Node body = new Node(Token.BLOCK, null, null);
    Node statement = new Node(Token.ASSIGN, null, null);
    statement.setChild(0, new Node(Token.ID, "x", null));
    statement.setChild(1, new Node(Token.INTEGER, 1, null));
    body.addChild(statement);

    loop.setChild(1, body);
    loop.setChild(2, new Node(Token.SEMICOLON, null, null));

    aliasDef.addChild(1, loop);

    pass.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(0, errors.size());
  }
}