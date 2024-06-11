import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestType.newType;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.ErrorType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIAS_REPLACE);
    transformationHandler = new TestAliasTransformationHandler();
    compiler = new TestAbstractCompiler(options, transformationHandler);
  }

  @Test
  public void testNoScope() {
    Node root = Node.rootNode();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);
    assertEquals(0, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getTraversal().getAliasUsages().size());
    assertEquals(0, scopedAliases.getTraversal().getScopeCalls().size());
  }

  @Test
  public void testSimpleScope() {
    Node root = Node.objectLiteral();
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.x; })", null);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("x", aliasDefinition.getString());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    Node aliasReference = aliasUsage.getNode();
    assertEquals("goog.x", aliasReference.getString());
    Node aliasDefinition2 = aliasDefinition.getParent();
    assertEquals("x", aliasDefinition2.getString());

    Node scopeCall = traversal.getScopeCalls().get(0);
    Node scopeFunction = scopeCall.getLastChild();
    assertEquals("goog.scope", scopeFunction.getQualifiedName());
  }

  @Test
  public void testInvalidScope() {
    Node root = Node.rootNode();
    Node body = Node.functionLiteral("goog.scope(function() { this.x = 42; })", null);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(0, traversal.getScopeCalls().size());

    // Check for errors.
    List<ErrorType> errors = traversal.getErrors();
    assertEquals(2, errors.size());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errors.get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USES_THIS", errors.get(1).getType());
  }

  @Test
  public void testInvalidScopeParameters() {
    Node root = Node.rootNode();
    Node body = Node.functionLiteral(
        "goog.scope(function(x) { this.x = x; })", null);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(0, traversal.getScopeCalls().size());

    // Check for errors.
    List<ErrorType> errors = traversal.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", errors.get(0).getType());
  }

  @Test
  public void testAliasRedefinition() {
    Node root = Node.rootNode();
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.x; var x = 'foo'; })", null);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    List<ErrorType> errors = traversal.getErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errors.get(0).getType());
  }

  @Test
  public void testNamespaceShadow() {
    Node root = Node.rootNode();
    Node googScope = Node.functionLiteral(
        "goog.scope(function() { var goog = { x: 1 }; })", null);
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.x; })", null);
    root.addChild(googScope);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    List<ErrorType> errors = traversal.getErrors();
    assertEquals(0, errors.size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("x", aliasDefinition.getString());
    Node aliasReference = traversal.getAliasUsages().get(0).getNode();
    assertEquals("goog.x", aliasReference.getString());

    // Check that the name "goog" was renamed.
    Node renamedGoog = traversal.getRenamedNames().get("goog");
    assertEquals("__goog__", renamedGoog.getString());
  }

  @Test
  public void testNamespaceShadowInScope() {
    Node root = Node.rootNode();
    Node googScope = Node.functionLiteral(
        "goog.scope(function() { var goog = { x: 1 }; goog.y = 2; })", null);
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.x; var y = goog.y; })", null);
    root.addChild(googScope);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    List<ErrorType> errors = traversal.getErrors();
    assertEquals(0, errors.size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("x", aliasDefinition.getString());
    Node aliasReference = traversal.getAliasUsages().get(0).getNode();
    assertEquals("goog.x", aliasReference.getString());

    // Check that the names "goog" and "goog.y" were renamed.
    Node renamedGoog = traversal.getRenamedNames().get("goog");
    assertEquals("__goog__", renamedGoog.getString());
    Node renamedGoogY = traversal.getRenamedNames().get("goog.y");
    assertEquals("__goog__.y", renamedGoogY.getString());
  }

  @Test
  public void testAliasType() {
    Node root = Node.rootNode();
    Node googScope = Node.functionLiteral(
        "goog.scope(function() { var goog = {}; })", null);
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.Object; })", null);
    root.addChild(googScope);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    List<ErrorType> errors = traversal.getErrors();
    assertEquals(0, errors.size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("Object", aliasDefinition.getString());

    Node aliasReference = traversal.getAliasUsages().get(0).getNode();
    assertEquals("goog.Object", aliasReference.getString());

    // Check that the alias type was renamed.
    Node renamedObject = traversal.getRenamedTypes().get("Object");
    assertEquals(newType("Object"), renamedObject.getType());
  }

  @Test
  public void testAliasFunction() {
    Node root = Node.rootNode();
    Node googScope = Node.functionLiteral(
        "goog.scope(function() { var goog = { MyFunction: function() {} }); })", null);
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.MyFunction; })", null);
    root.addChild(googScope);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    List<ErrorType> errors = traversal.getErrors();
    assertEquals(0, errors.size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("MyFunction", aliasDefinition.getString());

    Node aliasReference = traversal.getAliasUsages().get(0).getNode();
    assertEquals("goog.MyFunction", aliasReference.getString());

    // Check that the alias function was renamed.
    Node renamedMyFunction = traversal.getRenamedFunctions().get("MyFunction");
    assertEquals("MyFunction_", renamedMyFunction.getName());
  }

  @Test
  public void testAliasFunctionWithArguments() {
    Node root = Node.rootNode();
    Node googScope = Node.functionLiteral(
        "goog.scope(function() { var goog = { MyFunction: function(x, y) {} }); })", null);
    Node body = Node.functionLiteral(
        "goog.scope(function() { var x = goog.MyFunction; })", null);
    root.addChild(googScope);
    root.addChild(body);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());

    List<ErrorType> errors = traversal.getErrors();
    assertEquals(0, errors.size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("MyFunction", aliasDefinition.getString());

    Node aliasReference = traversal.getAliasUsages().get(0).getNode();
    assertEquals("goog.MyFunction", aliasReference.getString());

    // Check that the alias function was renamed and its arguments were renamed as well.
    Node renamedMyFunction = traversal.getRenamedFunctions().get("MyFunction");
    assertEquals("MyFunction_", renamedMyFunction.getName());
    Node renamedMyFunctionArgument0 = traversal.getRenamedNames().get("x");
    assertEquals("x_", renamedMyFunctionArgument0.getString());
    Node renamedMyFunctionArgument1 = traversal.getRenamedNames().get("y");
    assertEquals("y_", renamedMyFunctionArgument1.getString());
  }

  private static class TestAbstractCompiler extends AbstractCompiler {
    public TestAbstractCompiler(CompilerOptions options,
        AliasTransformationHandler transformationHandler) {
      super(options, transformationHandler);
    }

    @Override
    protected void reportCodeChange() {
      // Do nothing.
    }
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    private ImmutableMap<String, String> renamedNames =
        ImmutableMap.of();
    private ImmutableMap<String, Node> renamedFunctions =
        ImmutableMap.of();
    private ImmutableMap<String, Node> renamedTypes =
        ImmutableMap.of();

    @Override
    public void logAliasTransformation(String sourceFile, SourcePosition position,
        AliasTransformation aliasTransformation) {
      // Do nothing.
    }

    @Override
    public void addAlias(String oldName, String newName) {
      renamedNames = ImmutableMap.of(oldName, newName).putAll(renamedNames);
    }

    @Override
    public void addFunctionRenaming(String oldName, String newName) {
      renamedFunctions = ImmutableMap.of(oldName, Node.name(newName)).putAll(renamedFunctions);
    }

    @Override
    public void addTypeRenaming(String oldType, Node newTypeNode) {
      renamedTypes = ImmutableMap.of(oldType, newTypeNode).putAll(renamedTypes);
    }

    public Map<String, Node> getRenamedFunctions() {
      return renamedFunctions;
    }

    public Map<String, Node> getRenamedNames() {
      return renamedNames;
    }

    public Map<String, Node> getRenamedTypes() {
      return renamedTypes;
    }
  }
}