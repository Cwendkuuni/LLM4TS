import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class SyntacticScopeCreatorTest {

  private static final String SOURCE_NAME = "testSource";

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testFunctionScope() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createFunctionNode(), parent);

    Node functionNameNode = nodeFactory.createName("testFunction");
    Node args = nodeFactory.createList(nodeFactory.createName("arg1"), nodeFactory.createName("arg2"));
    Node body = nodeFactory.createBlock();

    creator.scanRoot(createFunctionNode(), null);

    assertEquals(1, scope.getVariables().size());
    Var functionVariable = scope.getVariables().get(0);
    assertVarDeclared(functionVariable, functionNameNode, JSType.FUNCTION, SOURCE_NAME, null);

    assertEquals(2, scope.getArguments().size());
    Var arg1Variable = scope.getArguments().get(0);
    assertVarDeclared(arg1Variable, nodeFactory.createName("arg1"), JSType.ANY, SOURCE_NAME, null);
    Var arg2Variable = scope.getArguments().get(1);
    assertVarDeclared(arg2Variable, nodeFactory.createName("arg2"), JSType.ANY, SOURCE_NAME, null);

    assertNull(scope.getParent());
  }

  @Test
  public void testVarScope() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createVariableNode("x"), parent);

    assertEquals(1, scope.getVariables().size());
    Var xVariable = scope.getVariables().get(0);
    assertVarDeclared(xVariable, nodeFactory.createName("x"), JSType.ANY, SOURCE_NAME, null);

    assertNull(scope.getParent());
  }

  @Test
  public void testVarScopeWithArguments() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createVariableNode("x"), createFunctionNode(), parent);

    assertEquals(1, scope.getVariables().size());
    Var xVariable = scope.getVariables().get(0);
    assertVarDeclared(xVariable, nodeFactory.createName("x"), JSType.ANY, SOURCE_NAME, null);

    assertEquals("arguments", scope.getArgumentName());
  }

  @Test
  public void testDuplicateDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createVariableNode("x"), createVariableNode("x"), parent);

    assertEquals(1, scope.getVariables().size());
    Var xVariable = scope.getVariables().get(0);
    assertVarDeclared(xVariable, nodeFactory.createName("x"), JSType.ANY, SOURCE_NAME, null);

    assertEquals("Variable x first declared in testSource",
        scope.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testDuplicateArgumentDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(
        createFunctionNode(),
        createVariableNode("arg1"),
        createVariableNode("arg1"),
        parent
    );

    assertEquals(1, scope.getVariables().size());
    Var arg1Variable = scope.getVariables().get(0);
    assertVarDeclared(arg1Variable, nodeFactory.createName("arg1"), JSType.ANY, SOURCE_NAME, null);

    assertEquals("Shadowing \"arguments\" is not allowed",
        scope.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testGlobalScope() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(createScriptNode(), null);

    assertEquals(0, scope.getVariables().size());
    assertEquals(0, scope.getArguments().size());
  }

  @Test
  public void testGlobalScopeWithDuplicateDeclaration() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(createVariableNode("x"), createVariableNode("x"), null);

    assertEquals(1, scope.getVariables().size());
    Var xVariable = scope.getVariables().get(0);
    assertVarDeclared(xVariable, nodeFactory.createName("x"), JSType.ANY, SOURCE_NAME, null);

    assertEquals("Variable x first declared in testSource",
        scope.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithJSDoc() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createFunctionNodeWithJSDoc(), parent);

    assertEquals(1, scope.getVariables().size());
    Var functionVariable = scope.getVariables().get(0);
    assertVarDeclared(functionVariable, nodeFactory.createName("testFunction"), JSType.FUNCTION, SOURCE_NAME, null);

    assertEquals(0, scope.getArguments().size());
  }

  @Test
  public void testFunctionWithJSDocAndArguments() {
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createFunctionNodeWithJSDocAndArguments(), parent);

    assertEquals(1, scope.getVariables().size());
    Var functionVariable = scope.getVariables().get(0);
    assertVarDeclared(functionVariable, nodeFactory.createName("testFunction"), JSType.FUNCTION, SOURCE_NAME, null);

    assertEquals(2, scope.getArguments().size());
    Var arg1Variable = scope.getArguments().get(0);
    assertVarDeclared(arg1Variable, nodeFactory.createName("arg1"), JSType.ANY, SOURCE_NAME, null);
    Var arg2Variable = scope.getArguments().get(1);
    assertVarDeclared(arg2Variable, nodeFactory.createName("arg2"), JSType.ANY, SOURCE_NAME, null);
  }

  @Test
  public void testErrorReporting() {
    RedeclarationHandler redeclarationHandler = new ErrorReportingRedeclarationHandler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler, redeclarationHandler);
    Scope parent = new Scope(nodeFactory.createNull(), null);
    Scope scope = creator.createScope(createVariableNode("x"), createVariableNode("x"), parent);

    assertEquals(1, scope.getDiagnostics().size());
    DiagnosticType diagnosticType = scope.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.ERROR, diagnosticType);

    assertEquals("Variable x first declared in testSource",
        scope.getDiagnostics().get(0).getMessage());
  }

  private Node createFunctionNode() {
    return nodeFactory.createBlock(
        nodeFactory.createToken(Token.FUNCTION, null, SOURCE_NAME),
        nodeFactory.createList(nodeFactory.createName("testFunction"), createListNode()),
        createBlockNode()
    );
  }

  private Node createFunctionNodeWithJSDoc() {
    JSDocInfo jsdocInfo = new JSDocInfo(nodeFactory.createToken(Token.JSDOC, null, SOURCE_NAME), ImmutableList.of());
    return nodeFactory.createBlock(
        nodeFactory.createToken(Token.FUNCTION, null, SOURCE_NAME),
        createListNode(),
        nodeFactory.createList(nodeFactory.createToken(Token.JSDOC, null, SOURCE_NAME), jsdocInfo),
        createBlockNode()
    );
  }

  private Node createFunctionNodeWithJSDocAndArguments() {
    JSDocInfo jsdocInfo = new JSDocInfo(nodeFactory.createToken(Token.JSDOC, null, SOURCE_NAME), ImmutableList.of("arg1", "arg2"));
    return nodeFactory.createBlock(
        nodeFactory.createToken(Token.FUNCTION, null, SOURCE_NAME),
        createListNode(nodeFactory.createName("arg1"), nodeFactory.createName("arg2")),
        nodeFactory.createList(nodeFactory.createToken(Token.JSDOC, null, SOURCE_NAME), jsdocInfo),
        createBlockNode()
    );
  }

  private Node createVariableNode(String name) {
    return nodeFactory.createVar(nodeFactory.createName(name), null, SOURCE_NAME);
  }

  private Node createScriptNode() {
    return nodeFactory.createScript(null, null, null, null, null, null);
  }

  private Node createBlockNode() {
    return nodeFactory.createBlock();
  }

  private Node createListNode() {
    return nodeFactory.createList();
  }

  private Node createListNode(Node... nodes) {
    ImmutableList<Node> nodeList = ImmutableList.copyOf(nodes);
    return nodeFactory.createList(nodeList.toArray(new Node[0]));
  }

  private void assertVarDeclared(Var var, Node node, JSType type, String sourceName, Node gramps) {
    assertTrue("Variable " + var.getName() + " should be declared", var != null);
    assertEquals("Node", node, var.getNode());
    assertEquals("JSType", type, var.getType());
    assertEquals("SourceName", sourceName, var.getSourceName());
    assertEquals("Gramps", gramps, var.getGramps());
  }
}

class ErrorReportingRedeclarationHandler implements RedeclarationHandler {
  @Override
  public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
    throw new RuntimeException("Redeclaration detected: " + name);
  }
}