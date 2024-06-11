import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.jscomp.Traverse.NodeTraversal;
import com.google.javascript.rhino.Token;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private VarCheck varCheck;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    compiler.setErrorReporter(new TestErrorReporter());
    input = new CompilerInput("test.js", "test.js", true, null, null);
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testUndefinedVar() {
    Node rootNode = Node.newText("var x; x;");
    NodeTraversal traversal = Traverse.create(compiler, rootNode, varCheck);
    traversal.traverse();

    Scope scope = traversal.getScope();
    assertFalse(scope.getVar("x") instanceof Scope.Var);
    assertTrue(varCheck.varsToDeclareInExterns.contains("x"));
  }

  @Test
  public void testDefinedVar() {
    Node rootNode = Node.newText("var x; x = 1;");
    NodeTraversal traversal = Traverse.create(compiler, rootNode, varCheck);
    traversal.traverse();

    Scope scope = traversal.getScope();
    assertNotNull(scope.getVar("x"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVarInFunction() {
    Node rootNode = Node.newText("function foo() { var x; }");
    NodeTraversal traversal = Traverse.create(compiler, rootNode, varCheck);
    traversal.traverse();

    Scope functionScope = traversal.getScope().getFunctionScope();
    assertNotNull(functionScope.getVar("x"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVarInExterns() {
    Node externs = Node.newText("{var x;}");
    Node rootNode = Node.newText("var y = x;");
    NodeTraversal traversal = Traverse.create(compiler, Node.newList(externs, rootNode), varCheck);
    traversal.traverse();

    Scope scope = traversal.getScope();
    assertNotNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVarInExternsWithDuplicateDeclaration() {
    Node externs = Node.newText("{var x;}");
    Node rootNode = Node.newText("var x; x = 1;");
    NodeTraversal traversal = Traverse.create(compiler, Node.newList(externs, rootNode), varCheck);
    traversal.traverse();

    Scope scope = traversal.getScope();
    assertNotNull(scope.getVar("x"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVariInModuleWithDependency() {
    Node externs = Node.newText("{var x;}");
    Node module1 = Node.newText("var y = x;");
    Node module2 = Node.newText("(function() {var x;})();");
    Node rootNode = Node.newList(externs, module1, module2);

    NodeTraversal traversal = Traverse.create(compiler, rootNode, varCheck);
    traversal.traverse();

    JSModule module1Module = input.getModule("Module1.js");
    JSModule module2Module = input.getModule("Module2.js");
    assertTrue(moduleGraph.dependsOn(module1Module, module2Module));

    Scope scope1 = traversal.getScopeForNode(module1);
    assertNotNull(scope1.getVar("x"));
    assertNotNull(scope1.getVar("y"));

    Scope scope2 = traversal.getScopeForNode(module2);
    assertNull(scope2.getVar("x"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVarInModuleWithMissingDependency() {
    Node externs = Node.newText("{var x;}");
    Node module1 = Node.newText("var y = x;");
    Node module2 = Node.newText("(function() {var x;})();");
    Node rootNode = Node.newList(externs, module1, module2);

    NodeTraversal traversal = Traverse.create(compiler, rootNode, varCheck);
    traversal.traverse();

    JSModule module1Module = input.getModule("Module1.js");
    JSModule module2Module = input.getModule("Module2.js");
    assertFalse(moduleGraph.dependsOn(module1Module, module2Module));

    Scope scope1 = traversal.getScopeForNode(module1);
    assertNotNull(scope1.getVar("x"));
    assertNotNull(scope1.getVar("y"));

    Scope scope2 = traversal.getScopeForNode(module2);
    assertNull(scope2.getVar("x"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testVarInModuleWithStrictDependency() {
    Node externs = Node.newText("{var x;}");
    Node module1 = Node.newText("var y = x;");
    Node module2 = Node.newText("(function() {var x;})();");
    Node rootNode = Node.newList(externs, module1, module2);

    NodeTraversal traversal = Traverse.create(compiler, rootNode, varCheck);
    traversal.traverse();

    JSModule module1Module = input.getModule("Module1.js");
    JSModule module2Module = input.getModule("Module2.js");
    assertTrue(moduleGraph.dependsOn(module2Module, module1Module));

    Scope scope1 = traversal.getScopeForNode(module1);
    assertNotNull(scope1.getVar("x"));
    assertNotNull(scope1.getVar("y"));

    Scope scope2 = traversal.getScopeForNode(module2);
    assertNull(scope2.getVar("x"));
    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }
}