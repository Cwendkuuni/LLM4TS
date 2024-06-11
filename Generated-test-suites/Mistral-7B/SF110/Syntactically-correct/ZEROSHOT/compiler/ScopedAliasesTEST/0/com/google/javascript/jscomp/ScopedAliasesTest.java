import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  var dom = goog.dom;\n" +
                          "  var DIV = dom.TagName.DIV;\n" +
                          "  dom.createElement(DIV);\n" +
                          "  function foo() {\n" +
                          "    var x = goog.x;\n" +
                          "    goog.x = 'newX';\n" +
                          "    assert(x == 'goog.x'); // old value\n" +
                          "    assert(x == 'newX'); // new value\n" +
                          "  }\n" +
                          "  foo();\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    List<String> expectedAliases = ImmutableList.of("goog.dom", "DIV");
    assertEquals(expectedAliases, aliases);

    Node transformedRoot = compiler.getOptimizedCode(root, null);
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(root.getFirstChild(), scopeCalls.get(0));

    List<Node> aliasDefinitions = NodeUtil.findNodes(transformedRoot, n -> n.isName() && n.getString().startsWith("goog."));
    assertEquals(2, aliasDefinitions.size());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      Node expectedAliasDefinition = transformedRoot.getFirstChild().getLastChild()
                                                      .getLastChild()
                                                      .getLastChild();
      assertEquals(aliasedNode.aliasDefinition, expectedAliasDefinition);
    } else if (aliasUsage instanceof AliasedTypeNode) {
      AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
      Node expectedAliasDefinition = NodeUtil.findSingleChildWithName(
          transformedRoot, "goog." + aliasedTypeNode.aliasName);
      assertEquals(aliasedTypeNode.aliasDefinition, expectedAliasDefinition);
    }

    Node transformedNode = NodeUtil.findSingleChildWithName(transformedRoot, "foo");
    Node expectedNode = new Node("function foo() {\n" +
                                 "  var x = " + expectedAliases.get(0) + ";\n" +
                                 "  " + expectedAliases.get(1) + ".createElement('DIV');\n" +
                                 "  assert(x == 'goog.x'); // old value\n" +
                                 "  assert(x == 'newX'); // new value\n" +
                                 "}");
    assertEquals(expectedNode, transformedNode);
  }

  @Test
  public void testScopedAliasesWithError() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  goog.scope(function() {\n" +
                          "  });\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_USED_IMPROPERLY.getMessage()));
  }

  @Test
  public void testScopedAliasesWithInvalidParameter() {
    Node root = new Node("goog.scope(function(x, y) {\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_HAS_BAD_PARAMETERS.getMessage()));
  }

  @Test
  public void testScopedAliasesWithThis() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  var x = this;\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_REFERENCES_THIS.getMessage()));
  }

  @Test
  public void testScopedAliasesWithReturn() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  return 'foo';\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_USES_RETURN.getMessage()));
  }

  @Test
  public void testScopedAliasesWithThrow() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  throw new Error('foo');\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_USES_THROW.getMessage()));
  }

  @Test
  public void testScopedAliasesWithAliasRedefined() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  var x = goog.x;\n" +
                          "  goog.x = 'newX';\n" +
                          "  var x = goog.x;\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_ALIAS_REDEFINED.getMessage()));
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  function x() {} // not an alias\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_NON_ALIAS_LOCAL.getMessage()));
  }

  @Test
  public void testScopedAliasesWithNamespaceShadows() {
    Node root = new Node("goog.scope(function() {\n" +
                          "  var goog = {};\n" +
                          "  var x = goog.x;\n" +
                          "});");

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(null, root);

    AliasTransformation transformation = scopedAliases.getTransformation();
    List<String> aliases = transformation.getAliases();
    assertTrue(aliases.isEmpty());

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0).getMessage();
    assertTrue(errorMessage.contains(GOOG_SCOPE_NON_ALIAS_LOCAL.getMessage()));

    List<String> forbiddenLocals = scopedAliases.getForbiddenLocals();
    assertEquals(1, forbiddenLocals.size());
    assertEquals("goog", forbiddenLocals.get(0));
  }
}