import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.Scope.Var.Kind.VAR;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Transformation;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    CompilerOptions options = new CompilerOptions() {};
    options.setAliasTransformation(NEW_NAME);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, new AliasTransformationHandler() {
      @Override
      public Transformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new NewNameTransformation(fileName, position);
      }
    });

    String code = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node root = NodeUtil.parseCompilationUnit(code, null, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage = aliasUsages.get(0);
    Node scopeCall = scopeCalls.get(0);

    // The alias definition should be the first child of the anonymous function.
    assertEquals("dom", aliasDefinition.getString());
    assertEquals(Scope.Kind.ANONYMOUS, aliasDefinition.getScope().getKind());
    assertEquals(VAR, aliasDefinition.getInitialValue().getNode().getType());

    // The alias usage should be the assignment to DIV.
    assertEquals("DIV", aliasUsage.getAliasedNode().getString());
    assertEquals("dom.TagName.DIV", aliasUsage.getAliasedDefinition().getString());

    // The scope call should be the first child of the goog.scope call.
    assertEquals("function() {...}", scopeCall.getString());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesInvalidCall() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    CompilerOptions options = new CompilerOptions() {};
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);

    String code = "goog.scope({foo: function() {}});";

    Node root = NodeUtil.parseCompilationUnit(code, null, null, null);
    scopedAliases.process(null, root);

    fail(); // Should throw an AssertionError due to GOOG_SCOPE_USED_IMPROPERLY
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesInvalidParameters() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    CompilerOptions options = new CompilerOptions() {};
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);

    String code = "goog.scope(function(x) {});";

    Node root = NodeUtil.parseCompilationUnit(code, null, null, null);
    scopedAliases.process(null, root);

    fail(); // Should throw an AssertionError due to GOOG_SCOPE_HAS_BAD_PARAMETERS
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesRefersThis() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    CompilerOptions options = new CompilerOptions() {};
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);

    String code = "goog.scope(function() {this.x = 1;});";

    Node root = NodeUtil.parseCompilationUnit(code, null, null, null);
    scopedAliases.process(null, root);

    fail(); // Should throw an AssertionError due to GOOG_SCOPE_REFERENCES_THIS
  }
}

class NewNameTransformation implements Transformation {
  private String fileName;
  private SourcePosition position;

  NewNameTransformation(String fileName, SourcePosition position) {
    this.fileName = fileName;
    this.position = position;
  }

  @Override
  public Node transform(Node n, SourcePosition position) {
    Node newNode = n.cloneTree();
    newNode.setPositionInfo(position);
    String newName = "_new_" + fileName + "_" + position.getLine();
    newNode.setString(newName);
    return newNode;
  }

  @Override
  public Node transformCatchClause(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformFunctionDeclaration(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformFunctionExpression(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformModuleDeclaration(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformModuleExpression(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformStatement(Node n) {
    Node newNode = n.cloneTree();
    newNode.setPositionInfo(position);
    String newName = "_new_" + fileName + "_" + position.getLine();
    newNode.setString(newName);
    return newNode;
  }

  @Override
  public Node transformSwitchCase(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformSwitchDefault(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformSwitchStatement(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformTemplateExpression(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformTypeAnnotation(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformTypeLiteral(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformVariableDeclaration(Node n) {
    Node newNode = n.cloneTree();
    newNode.setPositionInfo(position);
    String newName = "_new_" + fileName + "_" + position.getLine();
    newNode.getFirstChild().setString(newName);
    return newNode;
  }

  @Override
  public Node transformWhileStatement(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformWithStatement(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformExpressionStatement(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public List<Node> transformStatements(List<Node> statements) {
    List<Node> newStatements = Lists.newArrayList();
    for (Node statement : statements) {
      newStatements.add(transformStatement(statement));
    }
    return newStatements;
  }

  @Override
  public Node transformStatementList(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformScript(Node n) {
    Node newNode = n.cloneTree();
    newNode.setPositionInfo(position);
    String newName = "_new_" + fileName + "_" + position.getLine();
    newNode.setString(newName);
    return newNode;
  }

  @Override
  public Node transformTopLevelDeclarationList(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }

  @Override
  public Node transformTryStatement(Node n) {
    throw new UnsupportedOperationException("Should not be reached.");
  }
}