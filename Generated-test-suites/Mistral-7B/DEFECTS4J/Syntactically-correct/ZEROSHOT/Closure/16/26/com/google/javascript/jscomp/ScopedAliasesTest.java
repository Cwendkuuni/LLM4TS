import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.SourcePosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);

    Node root = Node.createTree("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // Verify we have the correct number of alias definitions, alias usages, and scope calls.
    assertThat(aliasDefinitionsInOrder).hasSize(1);
    assertThat(aliasUsages).hasSize(1);
    assertThat(scopeCalls).hasSize(1);

    // Verify the alias definition is correct.
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    String aliasName = "dom";
    String qualifiedAliasName = "goog.dom";
    Node aliasNode = Node.createString(aliasName);
    Node aliasValueNode = Node.createQualifiedName(qualifiedAliasName);
    Node aliasAssignment = Node.createAssignment(aliasNode, aliasValueNode);

    assertThat(aliasDefinition.getChildAtIndex(0)).isEqualTo(aliasNode);
    assertThat(aliasDefinition.getChildAtIndex(1)).isEqualTo(aliasAssignment);

    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasedNode = aliasUsage.getNode();
    Node aliasDefinitionNode = ((AliasedNode) aliasUsage).getAliasDefinition();

    // Verify the alias usage is correct.
    assertThat(aliasedNode).isEqualTo(Node.createName("dom"));
    assertThat(aliasDefinitionNode).isEqualTo(aliasDefinition);

    // Verify the scope call is correct.
    Node scopeCall = scopeCalls.get(0);
    Node scopeCallFunction = scopeCall.getChildAtIndex(0);
    Node scopeCallParameter = scopeCall.getChildAtIndex(1);

    // The scope call should have exactly 1 parameter.
    assertThat(scopeCallParameter).isNotNull();
    assertThat(scopeCallParameter.isFunction()).isFalse();
    assertThat(scopeCallParameter.getChildCount()).isZero();

    // The scope call function should be an anonymous function with no parameters.
    Node scopeCallFunctionBody = scopeCallFunction.getLastChild();
    Node scopeCallFunctionParameters = scopeCallFunction.getChildAtIndex(1);
    assertThat(scopeCallFunctionBody).isNotNull();
    assertThat(scopeCallFunctionBody.isBlock()).isTrue();
    assertThat(scopeCallFunctionParameters).isNull();
  }

  @Test
  public void testScopedAliasesWithAliasRedefinition() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);

    Node root = Node.createTree("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom.Element;" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // Verify we have the correct number of alias definitions, alias usages, and scope calls.
    assertThat(aliasDefinitionsInOrder).hasSize(2);
    assertThat(aliasUsages).hasSize(1);
    assertThat(scopeCalls).hasSize(1);

    // Verify the first alias definition is correct.
    Node firstAliasDefinition = aliasDefinitionsInOrder.get(0);
    String firstAliasName = "dom";
    String firstQualifiedAliasName = "goog.dom";
    Node firstAliasNode = Node.createString(firstAliasName);
    Node firstAliasValueNode = Node.createQualifiedName(firstQualifiedAliasName);
    Node firstAliasAssignment = Node.createAssignment(firstAliasNode, firstAliasValueNode);

    assertThat(firstAliasDefinition.getChildAtIndex(0)).isEqualTo(firstAliasNode);
    assertThat(firstAliasDefinition.getChildAtIndex(1)).isEqualTo(firstAliasAssignment);

    // Verify the second alias definition is incorrect (due to alias redefinition).
    Node secondAliasDefinition = aliasDefinitionsInOrder.get(1);
    String secondAliasName = "dom";
    String secondQualifiedAliasName = "goog.dom.Element";
    Node secondAliasNode = Node.createString(secondAliasName);
    Node secondAliasValueNode = Node.createQualifiedName(secondQualifiedAliasName);
    Node secondAliasAssignment = Node.createAssignment(secondAliasNode, secondAliasValueNode);

    // Verify there is an error for the second alias definition.
    DiagnosticType error = new DiagnosticType("JSC_GOOG_SCOPE_ALIAS_REDEFINED");
    List<String> errorArguments = Collections.singletonList(secondAliasName);
    Node secondAliasError = Node.createError(error, errorArguments, null);
    Node secondAliasDefinitionErrorNode = secondAliasDefinition.getLastChild();
    assertThat(secondAliasDefinitionErrorNode).isEqualTo(secondAliasError);

    // Verify the alias usage is correct.
    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasedNode = aliasUsage.getNode();
    Node aliasDefinitionNode = ((AliasedNode) aliasUsage).getAliasDefinition();

    // Verify the alias usage is correct and uses the first alias definition.
    assertThat(aliasedNode).isEqualTo(Node.createName("dom"));
    assertThat(aliasDefinitionNode).isEqualTo(firstAliasDefinition);
  }
}