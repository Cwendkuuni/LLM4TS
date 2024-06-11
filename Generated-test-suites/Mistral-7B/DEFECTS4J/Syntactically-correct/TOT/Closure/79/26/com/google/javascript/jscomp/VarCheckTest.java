import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Set;

public class VarCheckTest {

  private static final DiagnosticType UNDEFINED_VAR_ERROR =
      DiagnosticType.error("JSC_UNDEFINED_VARIABLE",
          "variable {0} is undefined");

  private static final DiagnosticType MISSING_MODULE_DEP_ERROR =
      DiagnosticType.warning("JSC_MISSING_MODULE_DEPENDENCY",
          "missing module dependency; module {0} should depend " +
          "on module {1} because it references {2}");

  private Compiler compiler;
  private VarCheck varCheck;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() throws Exception {
    this.compiler = mock(Compiler.class);
    this.nodeFactory = NodeFactory.getInitial();

    when(compiler.newExternInput("{SyntheticVarsDeclar}"))
        .thenReturn(new CompilerInput("test.js", null, null, null, null, null));

    this.varCheck = new VarCheck(this.compiler);
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    CompilerInput input = new CompilerInput("test.js", null, null, null, null, null);
    Node root = new Node(Token.PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(this.compiler, input, root);

    Node varNode = new Node(Token.NAME, "foo");
    Node declNode = new Node(Token.VAR, varNode);
    root.addChildToBack(declNode);
    traversal.traverse(this.varCheck, root, null);

    traversal.setInput(input);
    Node assignmentNode = new Node(Token.ASSIGN, null, varNode, new Node(Token.NUMBER, 42));
    root.addChildToBack(assignmentNode);
    traversal.traverse(this.varCheck, root, null);

    assertEquals(0, this.varCheck.varsToDeclareInExterns.size());

    Scope scope = traversal.getScope();
    Scope.Var var = scope.getVar("foo");
    assertEquals(var, scope.getVar("foo"));
    assertEquals(input, var.input);

    // Test error reporting.
    Node undeclaredVarNode = new Node(Token.NAME, "bar");
    traversal.traverse(this.varCheck, root, null);
    assertEquals(1, this.varCheck.varsToDeclareInExterns.size());
    assertEquals("bar", this.varCheck.varsToDeclareInExterns.iterator().next());

    Node undeclaredVarAssignmentNode = new Node(Token.ASSIGN, null, undeclaredVarNode, new Node(Token.NUMBER, 42));
    root.addChildToBack(undeclaredVarAssignmentNode);
    traversal.traverse(this.varCheck, root, null);

    assertEquals(1, this.varCheck.varsToDeclareInExterns.size());
    assertEquals("bar", this.varCheck.varsToDeclareInExterns.iterator().next());

    NodeTraversal.traverseRoots(
        this.compiler, Lists.newArrayList(input.getAstRoot(this.compiler)), this.varCheck);

    assertEquals(0, this.varCheck.varsToDeclareInExterns.size());

    String log = this.compiler.getDiagnostics().getLog();
    assertEquals(
        "test.js:1:13: " + UNDEFINED_VAR_ERROR.getMessage(nodeFactory.makeText("bar")) + "\n",
        log.substring(0, log.indexOf("\n", log.indexOf(UNDEFINED_VAR_ERROR.getMessage(nodeFactory.makeText("bar"))))));
  }

  @org.junit.Test
  public void testDuplicateVariableDeclaration() {
    CompilerInput input = new CompilerInput("test.js", null, null, null, null, null);
    Node root = new Node(Token.PROGRAM, null);

    Node varNode1 = new Node(Token.NAME, "foo");
    Node declNode1 = new Node(Token.VAR, varNode1);
    root.addChildToBack(declNode1);

    Node varNode2 = new Node(Token.NAME, "foo");
    Node declNode2 = new Node(Token.VAR, varNode2);
    root.addChildToBack(declNode2);

    NodeTraversal traversal = new NodeTraversal(this.compiler, input, root);
    traversal.traverse(this.varCheck, root, null);

    assertEquals(0, this.varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test
  public void testModuleDependency() {
    CompilerInput moduleAInput = new CompilerInput("moduleA.js", null, null, null, null, null);
    Node moduleARoot = new Node(Token.PROGRAM, null);
    Node moduleAMainVarNode = new Node(Token.NAME, "main");
    Node moduleAMainVarDeclNode = new Node(Token.VAR, moduleAMainVarNode);
    moduleARoot.addChildToBack(moduleAMainVarDeclNode);

    Node moduleBInput = new CompilerInput("moduleB.js", null, null, null, null, null);
    Node moduleBRoot = new Node(Token.PROGRAM, null);
    Node moduleBVarNode = new Node(Token.NAME, "moduleA");
    Node modulBMainVarNode = new Node(Token.NAME, "main");
    Node moduleBMainVarDeclNode = new Node(Token.VAR, modulBMainVarNode);
    moduleBMainVarDeclNode.putStringProp("init", "require('moduleA')");
    Node moduleBMainCallNode = new Node(Token.CALL, null, moduleBMainVarDeclNode, moduleBVarNode);
    moduleBRoot.addChildToBack(moduleBMainVarDeclNode);
    moduleBRoot.addChildToBack(moduleBMainCallNode);

    JSModule moduleAGraph = mock(JSModule.class);
    when(this.compiler.getModuleGraph()).thenReturn(moduleAGraph);
    when(moduleAGraph.dependsOn(moduleAGraph, moduleBInput.getModule())).thenReturn(true);

    JSModule moduleBGraph = mock(JSModule.class);
    when(moduleAGraph.dependsOn(moduleBInput.getModule(), moduleAGraph)).thenReturn(false);

    when(moduleBInput.getModule()).thenReturn(moduleBGraph);

    CompilerInput input = new CompilerInput("test.js", moduleAInput, moduleBInput, null, null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node testMainVarNode = new Node(Token.NAME, "main");
    Node testMainVarDeclNode = new Node(Token.VAR, testMainVarNode);
    root.addChildToBack(testMainVarDeclNode);
    Node testMainCallNode = new Node(Token.CALL, null, testMainVarDeclNode, new Node(Token.NAME, "require"));
    Node testMainArgsNode = new Node(Token.LIST, null);
    testMainArgsNode.addChildToBack(moduleBVarNode);
    testMainCallNode.addChildToBack(testMainArgsNode);
    root.addChildToBack(testMainCallNode);

    NodeTraversal traversal = new NodeTraversal(this.compiler, input, root);
    traversal.traverse(this.varCheck, root, null);

    assertEquals(0, this.varCheck.varsToDeclareInExterns.size());

    String log = this.compiler.getDiagnostics().getLog();
    assertEquals(
        "test.js:1:19: " + MISSING_MODULE_DEP_ERROR.getMessage(moduleAInput.getName(), moduleBInput.getName(), "require") + "\n",
        log.substring(0, log.indexOf("\n", log.indexOf(MISSING_MODULE_DEP_ERROR.getMessage(moduleAInput.getName(), moduleBInput.getName(), "require")))));
  }

  @org.junit.Test
  public void testModuleDependencyNoError() {
    CompilerInput moduleAInput = new CompilerInput("moduleA.js", null, null, null, null, null);
    Node moduleARoot = new Node(Token.PROGRAM, null);
    Node moduleAMainVarNode = new Node(Token.NAME, "main");
    Node moduleAMainVarDeclNode = new Node(Token.VAR, moduleAMainVarNode);
    moduleARoot.addChildToBack(moduleAMainVarDeclNode);

    CompilerInput moduleBInput = new CompilerInput("moduleB.js", null, null, null, null, null);
    Node moduleBRoot = new Node(Token.PROGRAM, null);
    Node moduleBVarNode = new Node(Token.NAME, "moduleA");
    Node moduleBMainVarNode = new Node(Token.NAME, "main");
    Node moduleBMainVarDeclNode = new Node(Token.VAR, moduleBMainVarNode);
    moduleBMainVarDeclNode.putStringProp("init", "require('moduleA')");
    Node moduleBMainCallNode = new Node(Token.CALL, null, moduleBMainVarDeclNode, moduleBVarNode);
    moduleBRoot.addChildToBack(moduleBMainVarDeclNode);
    moduleBRoot.addChildToBack(moduleBMainCallNode);

    JSModule moduleAGraph = mock(JSModule.class);
    when(this.compiler.getModuleGraph()).thenReturn(moduleAGraph);
    when(moduleAGraph.dependsOn(moduleAGraph, moduleBInput.getModule())).thenReturn(false);

    when(moduleBInput.getModule()).thenReturn(moduleAGraph);

    CompilerInput input = new CompilerInput("test.js", moduleAInput, moduleBInput, null, null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node testMainVarNode = new Node(Token.NAME, "main");
    Node testMainVarDeclNode = new Node(Token.VAR, testMainVarNode);
    root.addChildToBack(testMainVarDeclNode);
    Node testMainCallNode = new Node(Token.CALL, null, testMainVarDeclNode, new Node(Token.NAME, "require"));
    Node testMainArgsNode = new Node(Token.LIST, null);
    testMainArgsNode.addChildToBack(moduleBVarNode);
    testMainCallNode.addChildToBack(testMainArgsNode);
    root.addChildToBack(testMainCallNode);

    NodeTraversal traversal = new NodeTraversal(this.compiler, input, root);
    traversal.traverse(this.varCheck, root, null);

    assertEquals(0, this.varCheck.varsToDeclareInExterns.size());
  }
}