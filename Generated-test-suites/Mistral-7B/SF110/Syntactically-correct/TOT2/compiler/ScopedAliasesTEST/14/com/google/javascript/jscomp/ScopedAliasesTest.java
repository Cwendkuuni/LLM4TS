import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  @SuppressWarnings("unused")
  private AbstractCompiler compiler;
  @SuppressWarnings("unused")
  private PreprocessorSymbolTable preprocessorSymbolTable;
  @SuppressWarnings("unused")
  private AliasTransformationHandler transformationHandler;

  @BeforeClass
  public static void setup() {
    // Initialize the test environment.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  /**
   * Test that the ScopedAliases class correctly processes a valid goog.scope call.
   */
  @Test
  public void testValidGoogScopeCall() {
    String sourceCode = "goog.scope(function() {" +
                           "  var dom = goog.dom;" +
                           "  var DIV = dom.TagName.DIV;" +
                           "  dom.createElement(DIV);" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the goog.scope call was processed correctly.
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertTrue(scopeCall.isCall());
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getQualifiedName());

    // Check that the alias definitions were applied in the correct order.
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    Node domAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(VarType.VAR, domAliasDefinition.getType());
    assertEquals("dom", domAliasDefinition.getString());

    Node tagNameAliasDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals(VarType.VAR, tagNameAliasDefinition.getType());
    assertEquals("TagName", tagNameAliasDefinition.getParent().getQualifiedName());
    assertEquals("DIV", tagNameAliasDefinition.getString());

    // Check that the alias usages were correctly identified.
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());

    Node domAliasUsage = ((AliasedNode) aliasUsages.get(0)).aliasReference;
    assertEquals("dom", domAliasUsage.getString());
    Node tagNameAliasUsage = ((AliasedTypeNode) aliasUsages.get(1)).typeReference;
    assertEquals("TagName", tagNameAliasUsage.getParent().getQualifiedName());
    assertEquals("DIV", tagNameAliasUsage.getString());
  }

  /**
   * Test that the ScopedAliases class correctly reports errors for invalid goog.scope calls.
   */
  @Test
  public void testInvalidGoogScopeCalls() {
    String sourceCode = "goog.scope(function() {" +
                           "  var dom = goog.dom;" +
                           "  var DIV = dom.TagName.DIV;" +
                           "});goog.scope(function() {" +
                           "  var dom = goog.dom;" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the first goog.scope call was processed correctly.
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node firstScopeCall = scopeCalls.get(0);
    assertTrue(firstScopeCall.isCall());
    assertEquals(SCOPING_METHOD_NAME, firstScopeCall.getFirstChild().getQualifiedName());

    // Check that the second goog.scope call caused an error.
    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(1, errors.size());
    Node error = errors.get(0);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, error.getDiagnosticType());
    assertTrue(error.getNext() == null);
  }

  /**
   * Test that the ScopedAliases class correctly reports errors for goog.scope calls
   * with improper parameters.
   */
  @Test
  public void testBadGoogScopeCalls() {
    String sourceCode = "goog.scope(function() {" +
                           "  var dom = goog.dom;" +
                           "});goog.scope(function() {" +
                           "  var dom = goog.dom;" +
                           "});goog.scope(function() {" +
                           "  var dom = goog.dom;" +
                           "  return;" +
                           "});goog.scope(function() {" +
                           "  this.dom = goog.dom;" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the first goog.scope call was processed correctly.
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node firstScopeCall = scopeCalls.get(0);
    assertTrue(firstScopeCall.isCall());
    assertEquals(SCOPING_METHOD_NAME, firstScopeCall.getFirstChild().getQualifiedName());

    // Check that the second goog.scope call caused an error.
    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(4, errors.size());

    // Check that the first error is for the second goog.scope call.
    Node secondError = errors.get(1);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, secondError.getDiagnosticType());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        secondError.getArguments()[0]);

    // Check that the third error is for the third goog.scope call.
    Node thirdError = errors.get(2);
    assertEquals(GOOG_SCOPE_USES_RETURN, thirdError.getDiagnosticType());

    // Check that the fourth error is for the fourth goog.scope call.
    Node fourthError = errors.get(3);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, fourthError.getDiagnosticType());
  }

  /**
   * Test that the ScopedAliases class correctly handles aliases that are re-assigned.
   */
  @Test
  public void testAliasReassignment() {
    String sourceCode = "goog.scope(function() {" +
                           "  var x = goog.x;" +
                           "  var y = goog.x;" +
                           "  x = 1;" +
                           "  y = 2;" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the alias definitions were applied in the correct order.
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node xAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("x", xAliasDefinition.getString());

    // Check that the alias usage for 'x' was updated.
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    Node xAliasUsage = ((AliasedNode) aliasUsages.get(0)).aliasReference;
    assertEquals("x", xAliasUsage.getString());
    assertEquals(1, xAliasUsage.getFirstChild().getDoubleValue());

    // Check that the alias usage for 'y' was not updated.
    Node yAliasUsage = ((AliasedNode) aliasUsages.get(1)).aliasReference;
    assertEquals("x", yAliasUsage.getString());
    assertEquals("x", yAliasUsage.getParent().getQualifiedName());
  }

  /**
   * Test that the ScopedAliases class correctly handles aliases that are redefined with the same name.
   */
  @Test
  public void testAliasRedefinition() {
    String sourceCode = "goog.scope(function() {" +
                           "  var x = goog.x;" +
                           "  var x = 1;" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the first error is for the second definition of 'x'.
    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrors();
    Node error = errors.get(0);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error.getDiagnosticType());
    assertEquals("The alias 'x' is assigned a value more than once.",
        error.getArguments()[0]);
  }

  /**
   * Test that the ScopedAliases class correctly handles aliases that are redefined with a different name.
   */
  @Test
  public void testAliasRenamed() {
    String sourceCode = "goog.scope(function() {" +
                           "  var x = goog.x;" +
                           "  var y = goog.x;" +
                           "  x = 1;" +
                           "  y = 2;" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the first error is for the second definition of 'x'.
    assertTrue(scopedAliases.hasErrors());
    List<Node> errors = scopedAliases.getErrors();
    Node error = errors.get(0);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error.getDiagnosticType());
    assertEquals("The alias 'x' is assigned a value more than once.",
        error.getArguments()[0]);

    // Check that the alias 'y' was updated correctly.
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    Node yAliasUsage = ((AliasedNode) aliasUsages.get(1)).aliasReference;
    assertEquals("x", yAliasUsage.getString());
    assertEquals("y", yAliasUsage.getParent().getQualifiedName());
    assertEquals(2, yAliasUsage.getFirstChild().getDoubleValue());
  }

  /**
   * Test that the ScopedAliases class correctly handles alias definitions that are not in a goog.scope block.
   */
  @Test
  public void testAliasOutsideOfScope() {
    String sourceCode = "var x = goog.x;goog.scope(function() {" +
                           "  var y = goog.x;" +
                           "});";

    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    ScopedAliases scopedAliases = new ScopedAliases(null, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that there are no errors.
    assertFalse(scopedAliases.hasErrors());

    // Check that the alias for 'x' was not updated.
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    Node xAliasUsage = ((AliasedNode) aliasUsages.get(0)).aliasReference;
    assertEquals("x", xAliasUsage.getString());
    assertEquals("x", xAliasUsage.getParent().getQualifiedName());
    assertEquals(1, xAliasUsage.getFirstChild().getDoubleValue());
  }
}