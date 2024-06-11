import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.traverse.AbstractPostOrderCallback;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Set;

public class VarCheckTest {

  @SuppressWarnings("unchecked")
  private static final DiagnosticType[] DIAGNOSTIC_TYPES = new DiagnosticType[] {
      VarCheck.UNDEFINED_VAR_ERROR,
      VarCheck.VIOLATED_MODULE_DEP_ERROR,
      VarCheck.MISSING_MODULE_DEP_ERROR,
      VarCheck.STRICT_MODULE_DEP_ERROR,
      VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
      VarCheck.UNDEFINED_EXTERN_VAR_ERROR,
      VarCheck.INVALID_FUNCTION_DECL
  };

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;
  private Node root;

  @org.junit.Before
  public void setUp() throws Exception {
    compiler = mock(AbstractCompiler.class);
    input = mock(CompilerInput.class);
    root = mock(Node.class);
    varCheck = new VarCheck(compiler);
  }

  @org.junit.Test
  public void testUndefinedVarError() {
    when(input.getName()).thenReturn("TestInput.js");
    when(input.isExtern()).thenReturn(false);

    Node varNode = Node.newName("testVar");
    Node parentNode = Node.newName("testFn");
    parentNode.addChildToBack(varNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getInput()).thenReturn(input);
    when(traversal.getScope()).thenReturn(mock(Scope.class));

    varCheck.process(input, root);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.UNDEFINED_VAR_ERROR, varCheck.getDiagnostics().get(0).getType());
    assertEquals("testVar is undefined", varCheck.getDiagnostics().get(0).getMessage(null));
  }

  @org.junit.Test
  public void testViolatedModuleDependency() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleA.getName()).thenReturn("ModuleA");
    when(moduleB.getName()).thenReturn("ModuleB");

    when(input.getModule()).thenReturn(moduleA);
    when(input.getSyntheticModuleName()).thenReturn("ModuleB");

    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
    when(compiler.getModuleGraph().dependsOn(moduleA, moduleB)).thenReturn(true);

    Node varNode = Node.newName("testVar");
    Node parentNode = Node.newName("testFn");
    parentNode.addChildToBack(varNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    varCheck.process(input, root);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, varCheck.getDiagnostics().get(0).getType());
    assertEquals("Module B cannot reference testVar, defined in ModuleA, since ModuleA loads after ModuleB",
                 varCheck.getDiagnostics().get(0).getMessage(null));
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleA.getName()).thenReturn("ModuleA");
    when(moduleB.getName()).thenReturn("ModuleB");

    when(input.getModule()).thenReturn(moduleB);
    when(input.getSyntheticModuleName()).thenReturn("ModuleA");

    Node varNode = Node.newName("testVar");
    Node parentNode = Node.newName("testFn");
    parentNode.addChildToBack(varNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    varCheck.process(input, root);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.MISSING_MODULE_DEP_ERROR, varCheck.getDiagnostics().get(0).getType());
    assertEquals("missing module dependency; ModuleA should depend on ModuleB because it references testVar",
                 varCheck.getDiagnostics().get(0).getMessage(null));
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleA.getName()).thenReturn("ModuleA");
    when(moduleB.getName()).thenReturn("ModuleB");

    when(input.getModule()).thenReturn(moduleB);
    when(input.getSyntheticModuleName()).thenReturn("ModuleA");

    Node varNode = Node.newName("testVar");
    Node parentNode = Node.newName("testFn");
    parentNode.addChildToBack(varNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    varCheck.process(input, root);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, varCheck.getDiagnostics().get(0).getType());
    assertEquals("Module A cannot reference testVar, defined in ModuleB",
                 varCheck.getDiagnostics().get(0).getMessage(null));
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    CompilerInput externsInput = mock(CompilerInput.class);
    Node externsRoot = Node.newName("{SyntheticVarsDeclar}");
    when(externsInput.getAstRoot(compiler)).thenReturn(externsRoot);

    when(input.getModule()).thenReturn(null);

    Node varNode = Node.newName("testVar");
    Node parentNode = Node.newName("testFn");
    parentNode.addChildToBack(varNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    varCheck.process(externsInput, root);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
                 varCheck.getDiagnostics().get(0).getType());
    assertEquals("accessing name testVar in externs has no effect",
                 varCheck.getDiagnostics().get(0).getMessage(null));
  }

  @org.junit.Test
  public void testUndefinedExternalVar() {
    CompilerInput externsInput = mock(CompilerInput.class);
    Node externsRoot = Node.newName("{SyntheticVarsDeclar}");
    when(externsInput.getAstRoot(compiler)).thenReturn(externsRoot);

    when(input.getModule()).thenReturn(null);

    Node varNode = Node.newName("testVar");
    Node parentNode = Node.newName("testFn");
    parentNode.addChildToBack(varNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    varCheck.process(externsInput, root);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR,
                 varCheck.getDiagnostics().get(0).getType());
    assertEquals("name testVar is not undefined in the externs.",
                 varCheck.getDiagnostics().get(0).getMessage(null));
  }

  @org.junit.Test
  public void testInvalidFunctionDeclaration() {
    Node functionNode = Node.newExpressionStatement(
        Node.newName("testFn"),
        Node.newCall(null, Node.newName("function"), null));

    Node parentNode = Node.newName("testFnContainer");
    parentNode.addChildToBack(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    varCheck.visit(NodeTraversal.TRAVERSAL, null, parentNode, traversal);

    assertEquals(1, varCheck.getDiagnostics().size());
    assertEquals(VarCheck.INVALID_FUNCTION_DECL, varCheck.getDiagnostics().get(0).getType());
    assertEquals("Syntax error: function declaration must have a name",
                 varCheck.getDiagnostics().get(0).getMessage(null));
  }
}