import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node root;

  @org.junit.Before
  public void setup() {
    compiler = new AbstractCompiler();
    input = new CompilerInput("", "test.js", null);
    root = Node.newProgram(Token.PROGRAM);
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    Scope scope = NodeTraversal.traverse(compiler, root).getScope();

    // Check that an undefined variable is reported as an error.
    Node undeclaredVar = Node.newString(Token.NAME, "foo");
    scope.getGlobalScope().declare("foo", undeclaredVar, null, null);
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == undeclaredVar) {
          assertTrue(scope.getVar("foo") == null);
          assertTrue(t.getDiagnostics().hasError());
        }
      }
    });

    // Check that a duplicate variable is not reported as an error.
    Node duplicateVar = Node.newString(Token.NAME, "foo");
    scope.declare("foo", duplicateVar, null, null);
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == duplicateVar) {
          assertFalse(scope.getVar("foo") == null);
          assertFalse(t.getDiagnostics().hasError());
        }
      }
    });
  }

  @org.junit.Test
  public void testModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(input, root);

    // Check that a valid module dependency is not reported as an error.
    Node moduleA = Node.newProgram(Token.PROGRAM);
    Node varA = Node.newVar(Token.VAR, Node.newName(Token.NAME, "a"));
    Node assignA = Node.newAssign(Token.ASSIGN, varA, Node.newInt(Token.INT, 1));
    moduleA.addChildToBack(varA);
    moduleA.addChildToBack(assignA);

    Node moduleB = Node.newProgram(Token.PROGRAM);
    Node varB = Node.newVar(Token.VAR, Node.newName(Token.NAME, "a"));
    Node assignB = Node.newAssign(Token.ASSIGN, varB, Node.newInt(Token.INT, 2));
    Node getModuleA = Node.newGetProp(Token.GETPROP, Node.newName(Token.NAME, "require"), Node.newString(Token.STRING_LITERAL, "moduleA"));
    Node assignC = Node.newAssign(Token.ASSIGN, varB, Node.getOrCreateBindNode(getModuleA, "a"));
    moduleB.addChildToBack(getModuleA);
    moduleB.addChildToBack(varB);
    moduleB.addChildToBack(assignB);
    moduleB.addChildToBack(assignC);

    Node rootWithModules = Node.newBlock(Token.BLOCK, root, moduleA, moduleB);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input, rootWithModules), varCheck);

    Scope scopeA = NodeTraversal.traverse(compiler, moduleA).getScope();
    Scope scopeB = NodeTraversal.traverse(compiler, moduleB).getScope();

    assertTrue(scopeA.getVar("a") != null);
    assertTrue(scopeB.getVar("a") != null);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());

    // Check that a missing module dependency is reported as a warning.
    Node moduleC = Node.newProgram(Token.PROGRAM);
    Node varC = Node.newVar(Token.VAR, Node.newName(Token.NAME, "a"));
    Node assignC = Node.newAssign(Token.ASSIGN, varC, Node.newInt(Token.INT, 3));
    moduleC.addChildToBack(varC);
    moduleC.addChildToBack(assignC);

    Node moduleD = Node.newProgram(Token.PROGRAM);
    Node getModuleC = Node.newGetProp(Token.GETPROP, Node.newName(Token.NAME, "require"), Node.newString(Token.STRING_LITERAL, "moduleC"));
    Node assignD = Node.newAssign(Token.ASSIGN, varC, Node.getOrCreateBindNode(getModuleC, "a"));
    moduleD.addChildToBack(getModuleC);
    moduleD.addChildToBack(varC);
    moduleD.addChildToBack(assignD);

    Node rootWithMissingModule = Node.newBlock(Token.BLOCK, root, moduleC, moduleD);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input, rootWithMissingModule), varCheck);

    assertTrue(scopeA.getVar("a") != null);
    assertTrue(scopeB.getVar("a") != null);
    assertTrue(scopeB.getVar("a") == scopeC.getVar("a"));
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(varCheck.getDiagnostics().hasWarning());

    // Check that a strict module dependency is reported as an error.
    input = new CompilerInput("", "moduleA.js", null);
    root = Node.newProgram(Token.PROGRAM);
    Node moduleAA = Node.newProgram(Token.PROGRAM);
    Node varAA = Node.newVar(Token.VAR, Node.newName(Token.NAME, "a"));
    Node assignAA = Node.newAssign(Token.ASSIGN, varAA, Node.newInt(Token.INT, 1));
    moduleAA.addChildToBack(varAA);
    moduleAA.addChildToBack(assignAA);

    input.setModule(new JSModule("moduleA"));
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input, root), varCheck);

    assertTrue(scopeA.getVar("a") != null);
    assertTrue(scopeB.getVar("a") == null);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(varCheck.getDiagnostics().hasError());

    // Check that a non-strict module dependency is reported as a warning.
    input = new CompilerInput("", "moduleA.js", null);
    root = Node.newProgram(Token.PROGRAM);
    Node moduleAB = Node.newProgram(Token.PROGRAM);
    Node varAB = Node.newVar(Token.VAR, Node.newName(Token.NAME, "a"));
    Node assignAB = Node.newAssign(Token.ASSIGN, varAB, Node.newInt(Token.INT, 1));
    moduleAB.addChildToBack(varAB);
    moduleAB.addChildToBack(assignAB);

    input.setModule(new JSModule("moduleA"));
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input, root), varCheck);

    assertTrue(scopeA.getVar("a") != null);
    assertTrue(scopeB.getVar("a") == null);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
    assertTrue(varCheck.getDiagnostics().hasWarning());
  }

  @org.junit.Test
  public void testUndefinedExternVariable() {
    VarCheck varCheck = new VarCheck(compiler, true);
    varCheck.process(input, root);

    Node undeclaredExternVar = Node.newVar(Token.VAR, Node.newName(Token.NAME, "foo"));
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == undeclaredExternVar) {
          assertNull(scope.getVar("foo"));
          assertTrue(t.getDiagnostics().hasWarning());
        }
      }

      private Scope scope = NodeTraversal.traverse(compiler, root).getScope();
    });
  }

  @org.junit.Test
  public void testDuplicateExternVariable() {
    VarCheck varCheck = new VarCheck(compiler, true);
    varCheck.process(input, root);

    Node declaringExternVar = Node.newVar(Token.VAR, Node.newName(Token.NAME, "foo"));
    Node referencedExternVar = Node.newVar(Token.VAR, Node.newName(Token.NAME, "foo"));

    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == declaringExternVar || n == referencedExternVar) {
          assertTrue(scope.getVar("foo") != null);
          assertTrue(t.getDiagnostics().isEmpty());
        }
      }

      private Scope scope = NodeTraversal.traverse(compiler, root).getScope();
    });
  }
}