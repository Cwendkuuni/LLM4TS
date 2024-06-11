import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VarCheck;
import com.google.javascript.jscomp.CompilerPass;
import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private NodeTraversal nodeTraversal;
  private Scope scope;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    scope = mock(Scope.class);
    varCheck = new VarCheck(compiler);
    nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(scope);
  }

  @Test
  public void testValidVariableDeclaration() {
    // Basic test case
    Node root = Node.newObject("root");
    Node validVarDeclaration = Node.newVar("validVar");
    root.addChildToBack(validVarDeclaration);
    nodeTraversal.traverse(compiler, root, null);
    assertNull(varCheck.varsToDeclareInExterns.size());
  }

  @Test
  public void testUndefinedVariable() {
    // Basic test case for undefined variable
    Node root = Node.newObject("root");
    Node undefinedVar = Node.newName("undefinedVar");
    root.addChildToBack(undefinedVar);
    nodeTraversal.traverse(compiler, root, null);

    // Test error handling
    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
    String firstUndefinedVar = varCheck.varsToDeclareInExterns.iterator().next();
    when(compiler.getErrorLevel(UNDEFINED_EXTERN_VAR_ERROR)).thenReturn(CheckLevel.ERROR);
    varCheck.process(null, root);
  }

  @Test
  public void testModuleDependency() {
    // Basic test case for module dependency
    CompilerInput moduleA = new CompilerInput("moduleA.js", null);
    CompilerInput moduleB = new CompilerInput("moduleB.js", null);
    JSModule moduleAGraph = mock(JSModule.class);
    JSModule moduleBGraph = mock(JSModule.class);
    when(moduleAGraph.getName()).thenReturn("moduleA");
    when(moduleBGraph.getName()).thenReturn("moduleB");
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph() {{
      addModule(moduleA);
      addModule(moduleB);
    }});

    Node rootA = Node.newObject("moduleA");
    Node varA = Node.newVar("varA");
    rootA.addChildToBack(varA);
    Node rootB = Node.newObject("moduleB");
    Node varB = Node.newVar("varB");
    rootB.addChildToBack(varB);

    NodeTraversal nodeTraversalA = mock(NodeTraversal.class);
    NodeTraversal nodeTraversalB = mock(NodeTraversal.class);
    when(nodeTraversalA.getInput()).thenReturn(moduleA);
    when(nodeTraversalB.getInput()).thenReturn(moduleB);
    when(nodeTraversalA.getScope()).thenReturn(scope);
    when(nodeTraversalB.getScope()).thenReturn(scope);

    when(compiler.newExternInput("{SyntheticVarsDecl}"))
        .thenReturn(new CompilerInput("syntheticExterns.js", null));

    varCheck = new VarCheck(compiler);

    when(nodeTraversalA.process(null, rootA)).thenCallRealMethod();
    when(nodeTraversalB.process(null, rootB)).thenCallRealMethod();

    nodeTraversalA.traverseRoots(compiler, Lists.newArrayList(null, rootA), varCheck);
    nodeTraversalB.traverseRoots(compiler, Lists.newArrayList(null, rootB), varCheck);

    // Test error handling
    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
    String firstUndefinedVar = varCheck.varsToDeclareInExterns.iterator().next();
    when(compiler.getErrorLevel(VIOLATED_MODULE_DEP_ERROR)).thenReturn(CheckLevel.ERROR);
    varCheck.process(null, null);
  }
}