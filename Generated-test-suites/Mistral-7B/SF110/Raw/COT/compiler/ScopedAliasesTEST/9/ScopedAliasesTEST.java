import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    // Test the process method with a simple root node
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createTestNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); });");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(NodeType.CALL, aliasUsage.getType());
    assertEquals("goog.dom.createElement", aliasUsage.getLastChild().getString());
  }

  @Test
  public void testProcessWithAliasDefinitions() {
    // Test the process method with alias definitions in root node
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createTestNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; var DOM = goog.DOM; DOM.createElement(DOM.DIV); });");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals(NodeType.VAR, aliasDefinition.getType());
    assertEquals("DOM", aliasDefinition.getString());

    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals(NodeType.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(NodeType.CALL, aliasUsage.getType());
    assertEquals("DOM.createElement", aliasUsage.getLastChild().getString());
  }

  @Test
  public void testProcessWithInvalidScopeCall() {
    // Test the process method with an invalid goog.scope call
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createTestNode("goog.scope function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); }");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnosticType);
  }

  @Test
  public void testProcessWithInvalidAliasDefinition() {
    // Test the process method with an invalid alias definition
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createTestNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; var DOM = goog.DOM; DOM = goog.DOM; });");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversassignable(traversal, "getScopeCalls").size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnosticType);
  }

  @Test
  public void testProcessWithInvalidGoogScopeFunction() {
    // Test the process method with an invalid goog.scope function
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createTestNode("goog.scope function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); } function() { }");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnosticType);
  }

  @Test
  public void testProcessWithInvalidFunctionInsideScope() {
    // Test the process method with an invalid function inside the goog.scope
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = createTestNode("goog.scope(function() { var dom = goog.dom; function invalidFunction() { } dom.createElement(goog.TagName.DIV); });");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnosticType);
  }

  private Node createTestNode(String code) {
    // Create a Node from the given code
    RhinoCompiler compiler = new RhinoCompiler();
    Node root = compiler.compile(code);
    return root;
  }

  private static <T> List<T> traversableList(Object o, String methodName) {
    return (List<T>) ReflectionUtils.invokeMethod(o, methodName);
  }

  private static <T> T getLastTraversal(Object o) {
    return (T) ReflectionUtils.getFieldValue(o, "lastTraversal");
  }
}