import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.DefaultRedeclarationHandler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;

  @ org.junit.Before
  public void setup() {
    compiler = new AbstractCompiler() {
      @Override
      public RedeclarationHandler getRedeclarationHandler() {
        return new DefaultRedeclarationHandler();
      }
    };
  }

  @ org.junit.Test
  public void testDeclareSingleVariable() {
    Node functionNode = createFunctionNode("function foo() { var x = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareMultipleVariables() {
    Node functionNode = createFunctionNode("function foo() { var x = 1, y = 2; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(2, variables.size());
    Scope.Var xVariable = variables.get(0);
    assertEquals("x", xVariable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), xVariable.getParentScope());
    assertEquals(JSType.NUMBER, xVariable.getType());

    Scope.Var yVariable = variables.get(1);
    assertEquals("y", yVariable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), yVariable.getParentScope());
    assertEquals(JSType.NUMBER, yVariable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithType() {
    Node functionNode = createFunctionNode("function foo() { var x: string = 'bar'; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.STRING, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableInGlobalScope() {
    Node scriptNode = createScriptNode("var x = 1;");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(scriptNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(null, variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableInFunctionScopeWithSameNameAsParent() {
    Node functionNode = createFunctionNode("function foo() { var x = 1; function x() {} }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parentScope = new Scope(null, null);
    Scope scope = creator.createScope(functionNode, parentScope);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.FUNCTION, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithSameNameAsArgument() {
    Node functionNode = createFunctionNode("function foo(x) { var x = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithSameNameAsGlobal() {
    Node scriptNode = createScriptNode("var x = 1; function foo() { var x = 2; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(getFunctionNode(scriptNode, 0), null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(getFunctionNode(scriptNode, 0)), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithSameNameAsLocal() {
    Node functionNode = createFunctionNode("function foo() { var x = 1; var x = 2; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parentScope = new Scope(null, null);
    Scope scope = creator.createScope(functionNode, parentScope);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.newLocalScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithSameNameAsParameterAndWithTypeAnnotation() {
    Node functionNode = createFunctionNode("function foo(x: number) { var x: number = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithSameNameAsParameterAndWithoutTypeAnnotation() {
    Node functionNode = createFunctionNode("function foo(x) { var x = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.ANY, variable.getType());
  }

  @ org.junit.Test
  public void testRedeclareVariableWithSameNameAsGlobal() {
    Node scriptNode = createScriptNode("var x = 1; function foo() { var x = 2; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parentScope = new Scope(null, null);
    Scope scope = creator.createScope(getFunctionNode(scriptNode, 0), parentScope);
    RedeclarationHandler handler = new DefaultRedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name,
          Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        assertEquals(scriptNode, parent);
        assertEquals("x", name);
      }
    };
    creator.setRedeclarationHandler(handler);
    scope = creator.createScope(getFunctionNode(scriptNode, 0), parentScope);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(0, variables.size());
  }

  @ org.junit.Test
  public void testRedeclareVariableWithSameNameAsLocal() {
    Node functionNode = createFunctionNode("function foo() { var x = 1; var x = 2; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope parentScope = new Scope(null, null);
    Scope scope = creator.createScope(functionNode, parentScope);
    RedeclarationHandler handler = new DefaultRedeclarationHandler() {
      @Override
      public void onRedeclaration(
          Scope s, String name,
          Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        assertEquals(functionNode, parent);
        assertEquals("x", name);
      }
    };
    creator.setRedeclarationHandler(handler);
    scope = creator.createScope(functionNode, parentScope);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.newLocalScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithTypeAnnotationButNoJSDocInfo() {
    Node functionNode = createFunctionNode("function foo() { var x: number = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithTypeAnnotationButEmptyJSDocInfo() {
    Node functionNode = createFunctionNode("function foo() { /** @type {number} */ var x = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  @ org.junit.Test
  public void testDeclareVariableWithTypeAnnotationAndJSDocInfo() {
    Node functionNode = createFunctionNode("function foo() { /** @type {number} */ var x = 1; }");
    ScopeCreator creator = new SyntacticScopeCreator(compiler);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setSuppressions(ImmutableList.of("duplicate"));
    functionNode.setJSDocInfo(jsDocInfo);
    Scope scope = creator.createScope(functionNode, null);
    ImmutableList<Scope.Var> variables = scope.getVars();
    assertEquals(1, variables.size());
    Scope.Var variable = variables.get(0);
    assertEquals("x", variable.getName());
    assertEquals(NodeUtil.getFunctionScope(functionNode), variable.getParentScope());
    assertEquals(JSType.NUMBER, variable.getType());
  }

  private Node createFunctionNode(String code) {
    Node functionNode = JsParser.parseExpression(code, null);
    JsParser.setSourceInfo(functionNode, "test.js");
    return functionNode;
  }

  private Node getFunctionNode(Node node, int index) {
    Node functionNode = null;
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        if (index-- == 0) {
          functionNode = child;
          break;
        }
      }
    }
    return functionNode;
  }

  private Node createScriptNode(String code) {
    Node scriptNode = JsParser.parseExpression(code, null);
    JsParser.setSourceInfo(scriptNode, "test.js");
    return scriptNode;
  }
}