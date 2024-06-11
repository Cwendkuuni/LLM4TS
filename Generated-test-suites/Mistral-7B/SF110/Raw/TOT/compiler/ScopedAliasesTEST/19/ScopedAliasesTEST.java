import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock private AbstractCompiler compiler;
  @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
  @InjectMocks private ScopedAliases scopedAliases;

  @Test
  public void testSimpleAlias() {
    Node root = Node.createString("goog.scope(function() { var x = goog.x; });");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    // There should be one definition and one usage.
    Preconditions.checkState(aliasDefinitions.size() == 1);
    Preconditions.checkState(aliasUsages.size() == 1);

    // The definition should be "goog.x" and the usage should be "x".
    Node aliasDefinition = aliasDefinitions.get(0);
    Node aliasUsage = aliasUsages.get(0);
    assertEquals(TOKEN_IDENT, aliasDefinition.getType());
    assertEquals("goog.x", aliasDefinition.getString());
    assertEquals(TOKEN_NAME, aliasUsage.getType());
    assertEquals("x", aliasUsage.getString());

    // Verify that the alias was applied.
    Node rootAfterProcessing = scopedAliases.compiler.getRootNode();
    Node xNodeBeforeAliasApplication = Mockito.spy(rootAfterProcessing.getChildAtIndex(1));
    Node xNodeAfterAliasApplication = aliasUsage.getNode();
    assertEquals(xNodeBeforeAliasApplication, xNodeAfterAliasApplication.getParent());
    assertEquals(aliasDefinition, xNodeAfterAliasApplication.getParent());
  }

  @Test
  public void testMultipleAliases() {
    Node root =
        Node.createString("goog.scope(function() {" +
            "var x = goog.x, y = goog.y, z = goog.z;" +
            "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    // There should be three definitions and three usages.
    Preconditions.checkState(aliasDefinitions.size() == 3);
    Preconditions.checkState(aliasUsages.size() == 3);

    // Verify that the aliases were applied in the correct order.
    Node xDefinition = aliasDefinitions.get(0);
    Node xUsage = aliasUsages.get(0);
    assertEquals("goog.x", xDefinition.getString());
    assertEquals("x", xUsage.getNode().getString());

    Node yDefinition = aliasDefinitions.get(1);
    Node yUsage = aliasUsages.get(1);
    assertEquals("goog.y", yDefinition.getString());
    assertEquals("y", yUsage.getNode().getString());

    Node zDefinition = aliasDefinitions.get(2);
    Node zUsage = aliasUsages.get(2);
    assertEquals("goog.z", zDefinition.getString());
    assertEquals("z", zUsage.getNode().getString());
  }

  @Test
  public void testAliasWithTypeNode() {
    Node root =
        Node.createString("goog.scope(function() {" +
            "var x: string = goog.x;" +
            "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    // There should be one definition and one usage.
    Preconditions.checkState(aliasDefinitions.size() == 1);
    Preconditions.checkState(aliasUsages.size() == 1);

    // The definition should be "goog.x" and the usage should be "x".
    Node aliasDefinition = aliasDefinitions.get(0);
    Node aliasUsage = aliasUsages.get(0);
    assertEquals(TOKEN_IDENT, aliasDefinition.getType());
    assertEquals("goog.x", aliasDefinition.getString());

    // The type node should have been updated to reference the alias.
    Node typeNode = aliasUsage.getNode().getChildAtIndex(1);
    assertEquals(TOKEN_IDENT, typeNode.getType());
    assertEquals("x", typeNode.getString());
  }

  @Test
  public void testInvalidAliasDefinition() {
    Node root = Node.createString("goog.scope(function() {" +
        "goog.scope(function() {});" +
        "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that an error was reported.
    assertEquals(1, scopedAliases.compiler.getNumDiagnostics());
    DiagnosticType errorType = scopedAliases.compiler.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errorType.getName());
  }

  @Test
  public void testInvalidAliasDefinitionWithParameters() {
    Node root = Node.createString("goog.scope(function() {" +
        "goog.scope(function(a, b) {});" +
        "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that an error was reported.
    assertEquals(1, scopedAliases.compiler.getNumDiagnostics());
    DiagnosticType errorType = scopedAliases.compiler.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", errorType.getName());
  }

  @Test
  public void testInvalidAliasReferenceToThis() {
    Node root = Node.createString("goog.scope(function() {" +
        "this.x = goog.x;" +
        "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that an error was reported.
    assertEquals(1, scopedAliases.compiler.getNumDiagnostics());
    DiagnosticType errorType = scopedAliases.compiler.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errorType.getName());
  }

  @Test
  public void testInvalidAliasReturnsValue() {
    Node root = Node.createString("goog.scope(function() {" +
        "return goog.x;" +
        "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that an error was reported.
    assertEquals(1, scopedAliases.compiler.getNumDiagnostics());
    DiagnosticType errorType = scopedAliases.compiler.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errorType.getName());
  }

  @Test
  public void testInvalidAliasThrowsException() {
    Node root = Node.createString("goog.scope(function() {" +
        "throw new Error('hello');" +
        "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that an error was reported.
    assertEquals(1, scopedAliases.compiler.getNumDiagnostics());
    DiagnosticType errorType = scopedAliases.compiler.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", errorType.getName());
  }

  @Test
  public void testAliasRedefinition() {
    Node root = Node.createString("goog.scope(function() {" +
        "var x = goog.x;" +
        "var x = 42;" +
        "});");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be one alias definition and one usage.
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    // Verify that an error was reported.
    Preconditions.checkState(aliasDefinitions.size() == 1);
    Preconditions.checkState(aliasUsages.size() == 1);
    assertEquals(1, scopedAliases.compiler.getNumDiagnostics());
    DiagnosticType errorType = scopedAliases.compiler.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errorType.getName());
  }

  @Test
  public void testAliasInsideFunction() {
    Node root = Node.createString("function foo() {" +
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "});" +
        "return 42;" +
        "}");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that no errors were reported.
    assertEquals(0, scopedAliases.compiler.getNumDiagnostics());
  }

  @Test
  public void testAliasInsideLoop() {
    Node root = Node.createString("for (var i = 0; i < 10; i++) {" +
        "  goog.scope(function() {" +
        "    var x = goog.x;" +
        "  });" +
        "}");

    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    scopedAliases.process(null, root);

    // There should be no alias definitions or usages.
    Preconditions.checkState(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    Preconditions.checkState(scopedAliases.getAliasUsages().isEmpty());

    // Verify that no errors were reported.
    assertEquals(0, scopedAliases.compiler.getNumDiagnostics());
  }
}