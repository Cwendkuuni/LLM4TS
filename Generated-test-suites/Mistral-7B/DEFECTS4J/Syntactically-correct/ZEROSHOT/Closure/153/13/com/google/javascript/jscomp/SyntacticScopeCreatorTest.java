import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SpyScope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope result = creator.createScope(createFunctionNode("function test() {}"), null);
    assertEquals(result.getVariables().size(), 0);
  }

  @Test
  public void testCreateScopeWithArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    Scope result = creator.createScope(createFunctionNode("function test(a) {}"), null);
    assertEquals(result.getVariables().size(), 1);
    assertEquals(result.getVariables().get(0).getName(), "a");
  }

  @Test
  public void testCreateScopeWithDuplicateDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    SpyScope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionNode("function test() { var x = 1; var x = 2; }"), parentScope);
    assertEquals(result.getVariables().size(), 1);
    assertEquals(result.getVariables().get(0).getName(), "x");
    assertEquals(result.getDiagnostics().size(), 1);
    assertEquals(result.getDiagnostics().get(0).getMessage(), "Variable x first declared in test");
  }

  @Test
  public void testCreateScopeWithArgumentsAndDuplicateDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionNode("function test(x) { var x = 1; var x = 2; }"), parentScope);
    assertEquals(result.getVariables().size(), 1);
    assertEquals(result.getVariables().get(0).getName(), "x");
    assertEquals(result.getDiagnostics().size(), 1);
    assertEquals(result.getDiagnostics().get(0).getMessage(), "Variable x first declared in test");
  }

  @Test
  public void testCreateScopeWithGlobalDuplicateDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
    SpyScope globalScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(globalScope))).thenReturn(globalScope);

    Scope result = creator.createScope(createFunctionNode("function test() { var x = 1; var x = 2; }"), globalScope);
    assertEquals(result.getVariables().size(), 1);
    assertEquals(result.getVariables().get(0).getName(), "x");
    assertEquals(globalScope.getVariables().size(), 1);
    assertEquals(globalScope.getVariables().get(0).getName(), "x");
    assertEquals(globalScope.getDiagnostics().size(), 1);
    assertEquals(globalScope.getDiagnostics().get(0).getMessage(), "Variable x first declared in test");
  }

  @Test
  public void testCreateScopeWithArgumentShadowing() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionNode("function test(x) { var arguments = x; }"), parentScope);
    assertEquals(result.getDiagnostics().size(), 1);
    assertEquals(result.getDiagnostics().get(0).getMessage(), "Shadowing \"arguments\" is not allowed");
  }

  @Test
  public void testCreateScopeWithMultipleVariables() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionNode("function test() { var x = 1; var y = 2; }"), parentScope);
    assertEquals(result.getVariables().size(), 2);
    assertEquals(result.getVariables().get(0).getName(), "x");
    assertEquals(result.getVariables().get(1).getName(), "y");
  }

  @Test
  public void testCreateScopeWithFunctionExpression() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionExpressionNode("function test() {}"), parentScope);
    assertEquals(result.getVariables().size(), 0);
  }

  @Test
  public void testCreateScopeWithFunctionDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionDeclarationNode("function test() {}"), parentScope);
    assertEquals(result.getVariables().size(), 0);
  }

  @Test
  public void testCreateScopeWithFunctionWithArgumentsAndLocalVariables() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createFunctionNode("function test(x) { var y = 1; }"), parentScope);
    assertEquals(result.getVariables().size(), 2);
    assertEquals(result.getVariables().get(0).getName(), "x");
    assertEquals(result.getVariables().get(1).getName(), "y");
  }

  @Test
  public void testCreateScopeWithCatchBlock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createCatchBlockNode("try { } catch (x) {}"), parentScope);
    assertEquals(result.getVariables().size(), 1);
    assertEquals(result.getVariables().get(0).getName(), "x");
  }

  @Test
  public void testCreateScopeWithScriptNode() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);

    Scope parentScope = new SpyScope();
    when(compiler.createScope(Mockito.any(), Mockito.eq(parentScope))).thenReturn(parentScope);

    Scope result = creator.createScope(createScriptNode("var x = 1;"), parentScope);
    assertEquals(result.getVariables().size(), 1);
    assertEquals(result.getVariables().get(0).getName(), "x");
  }

  private Node createFunctionNode(String code) {
    return NodeFactory.createFunctionDeclaration(code);
  }

  private Node createFunctionExpressionNode(String code) {
    return NodeFactory.createFunctionExpression(code);
  }

  private Node createFunctionDeclarationNode(String code) {
    return NodeFactory.createFunctionDeclaration(code);
  }

  private Node createCatchBlockNode(String code) {
    return NodeFactory.createCatch(code);
  }

  private Node createScriptNode(String code) {
    return NodeFactory.createScript(code);
  }

  private static NodeFactory NodeFactory = new NodeFactory() {
    @Override
    public Node createFunctionDeclaration(String code) {
      Node n = new Node();
      n.setType(Token.FUNCTION);
      n.setSourcePosition(0, 0);
      n.setEndPosition(code.length(), code.length());
      n.setProp(Node.SOURCENAME_PROP, code.substring(0, code.indexOf("{")).replaceAll("/", ""));
      Node nameNode = new Node();
      nameNode.setType(Token.NAME);
      nameNode.setString(code.substring(code.indexOf("function") + 7, code.indexOf("(")).trim());
      n.setFirstChild(nameNode);
      Node args = new Node();
      args.setType(Token.LP);
      n.setLastChild(args);
      Node body = NodeFactory.createScript(code.substring(code.indexOf("{"), code.lastIndexOf("}") + 1));
      n.setNext(body);
      return n;
    }

    @Override
    public Node createFunctionExpression(String code) {
      Node n = new Node();
      n.setType(Token.FUNCTION);
      n.setSourcePosition(0, 0);
      n.setEndPosition(code.length(), code.length());
      n.setProp(Node.SOURCENAME_PROP, code.substring(0, code.indexOf("(")).replaceAll("/", ""));
      Node fnNameNode = new Node();
      fnNameNode.setType(Token.NAME);
      fnNameNode.setString(code.substring(0, code.indexOf("(")).trim());
      n.setFirstChild(fnNameNode);
      Node args = new Node();
      args.setType(Token.LP);
      n.setLastChild(args);
      Node body = NodeFactory.createScript(code.substring(code.indexOf("{"), code.lastIndexOf("}") + 1));
      n.setNext(body);
      return n;
    }

    @Override
    public Node createCatch(String code) {
      Node n = new Node();
      n.setType(Token.CATCH);
      n.setSourcePosition(0, 0);
      n.setEndPosition(code.length(), code.length());
      Node nameNode = new Node();
      nameNode.setType(Token.NAME);
      nameNode.setString(code.substring(code.indexOf("(") + 1, code.indexOf(")")).trim());
      Node body = NodeFactory.createScript(code.substring(code.indexOf("{"), code.lastIndexOf("}") + 1));
      n.setFirstChild(nameNode);
      n.setLastChild(body);
      return n;
    }

    @Override
    public Node createScript(String code) {
      Node n = new Node();
      n.setType(Token.SCRIPT);
      n.setSourcePosition(0, 0);
      n.setEndPosition(code.length(), code.length());
      n.setProp(Node.SOURCENAME_PROP, code.substring(0, code.indexOf("(")).replaceAll("/", ""));
      n.setCode(code);
      return n;
    }

  };
}