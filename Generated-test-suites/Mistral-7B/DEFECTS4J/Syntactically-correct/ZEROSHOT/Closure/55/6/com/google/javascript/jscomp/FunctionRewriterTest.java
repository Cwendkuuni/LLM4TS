import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class FunctionRewriterTest {

  private static Compiler createCompiler() {
    Compiler compiler = new Compiler();
    compiler.init();
    return compiler;
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString("function a() {}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a() {emptyFn();}");
    assertEquals(expected, root);
  }

  @org.junit.Test
  public void testIdentityFunctionReducer() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a(x) {return x;}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a(x) {identityFn(x);}");
    assertEquals(expected, root);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a() {return 10;}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a() {returnconst(10);}");
    assertEquals(expected, root);
  }

  @org.junit.Test
  public void testGetterReducer() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a() {return this.a;}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a() {getter(\"a\");}");
    assertEquals(expected, root);
  }

  @org.junit.Test
  public void testSetterReducer() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a(x) {this.a = x;}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a(x) {setter(\"a\");getter(\"a\");}");
    assertEquals(expected, root);
  }

  @org.junit.Test
  public void testFunctionWithArguments() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a(x, y) {return x + y;}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a(x, y) {return add(x, y);}");
    assertEquals(expected, root);
  }

  @org.junit.Test
  public void testNoReduction() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a() {}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    assertTrue(root.equals(Parser.parseString("function a() {}")));
  }

  @org.junit.Test
  public void testReductionWithMultipleFunctions() {
    Compiler compiler = createCompiler();
    Node root = new Parser(compiler).parseString(
        "function a() {return 1;} " +
        "function b() {return 2;} " +
        "function c() {return a() + b();}");

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expected = new Parser(compiler).parseString(
        "function a() {returnconst(1);} " +
        "function b() {returnconst(2);} " +
        "function c() {add(add(returnconst(1), returnconst(2)), null);}");
    assertEquals(expected, root);
  }

  private Node getHelperCode(FunctionRewriter functionRewriter,
                            String reducerName) {
    Node root = new Node(Token.BLOCK, 0, 0);
    functionRewriter.process(null, root);
    Node helperCode = functionRewriter.parseHelperCode(
        functionRewriter.getClass().getDeclaredClass(reducerName));
    return helperCode;
  }

  @org.junit.Test
  public void testEmptyFunctionHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(createCompiler());
    Node helperCode = getHelperCode(functionRewriter, "EmptyFunctionReducer");

    String helperSource = functionRewriter.getClass()
        .getDeclaredClass("EmptyFunctionReducer")
        .getDeclaredMethod("getHelperSource")
        .invoke(functionRewriter)
        .toString();

    String expectedHelperSource = "function " +
        "JSCompiler_emptyFn() {" +
        "  return function() {" +
        "    // empty code" +
        "  }" +
        "}";
    assertEquals(expectedHelperSource, helperSource);

    String expectedCode = "emptyFn();";
    Node expected = new Parser(createCompiler()).parseString(expectedCode);
    assertEquals(expected, helperCode);
  }

  @org.junit.Test
  public void testIdentityFunctionHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(createCompiler());
    Node helperCode = getHelperCode(functionRewriter, "IdentityReducer");

    String helperSource = functionRewriter.getClass()
        .getDeclaredClass("IdentityReducer")
        .getDeclaredMethod("getHelperSource")
        .invoke(functionRewriter)
        .toString();

    String expectedHelperSource = "function " +
        "JSCompiler_identityFn() {" +
        "  return function(JSCompiler_identityFn_value) {" +
        "    return JSCompiler_identityFn_value;" +
        "  }" +
        "}";
    assertEquals(expectedHelperSource, helperSource);

    String expectedCode = "identityFn(identityFn_value);";
    Node expected = new Parser(createCompiler()).parseString(expectedCode);
    assertEquals(expected, helperCode);
  }

  @org.junit.Test
  public void testReturnConstantHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(createCompiler());
    Node helperCode = getHelperCode(functionRewriter, "ReturnConstantReducer");

    String helperSource = functionRewriter.getClass()
        .getDeclaredClass("ReturnConstantReducer")
        .getDeclaredMethod("getHelperSource")
        .invoke(functionRewriter)
        .toString();

    String expectedHelperSource = "function " +
        "JSCompiler_returnArg(JSCompiler_returnArg_value) {" +
        "  return function() {" +
        "    return JSCompiler_returnArg_value;" +
        "  }" +
        "}";
    assertEquals(expectedHelperSource, helperSource);

    String expectedCode = "returnarg(JSCompiler_returnArg_value);";
    Node expected = new Parser(createCompiler()).parseString(expectedCode);
    assertEquals(expected, helperCode);
  }

  @org.junit.Test
  public void testGetterHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(createCompiler());
    Node helperCode = getHelperCode(functionRewriter, "GetterReducer");

    String helperSource = functionRewriter.getClass()
        .getDeclaredClass("GetterReducer")
        .getDeclaredMethod("getHelperSource")
        .invoke(functionRewriter)
        .toString();

    String expectedHelperSource = "function " +
        "JSCompiler_get(JSCompiler_get_name) {" +
        "  return function() {" +
        "    return this[JSCompiler_get_name]" +
        "  }" +
        "}";
    assertEquals(expectedHelperSource, helperSource);

    String expectedCode = "getter(\"JSCompiler_get_name\");";
    Node expected = new Parser(createCompiler()).parseString(expectedCode);
    assertEquals(expected, helperCode);
  }

  @org.junit.Test
  public void testSetterHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(createCompiler());
    Node helperCode = getHelperCode(functionRewriter, "SetterReducer");

    String helperSource = functionRewriter.getClass()
        .getDeclaredClass("SetterReducer")
        .getDeclaredMethod("getHelperSource")
        .invoke(functionRewriter)
        .toString();

    String expectedHelperSource = "function " +
        "JSCompiler_set(JSCompiler_set_name) {" +
        "  return function(JSCompiler_set_value) {" +
        "    this[JSCompiler_set_name] = JSCompiler_set_value" +
        "  }" +
        "}";
    assertEquals(expectedHelperSource, helperSource);

    String expectedCode = "setter(\"JSCompiler_set_name\");";
    Node expected = new Parser(createCompiler()).parseString(expectedCode);
    assertEquals(expected, helperCode);
  }
}

class NodeUtil {
  public static boolean isFunctionExpression(Node node) {
    return node.getType() == Token.FUNCTION &&
        node.getLastChild().hasOneChild() &&
        node.getLastChild().getFirstChild().getType() == Token.BLOCK;
  }

  public static boolean isIdentityFunction(Node functionNode) {
    return NodeUtil.isFunctionExpression(functionNode) &&
        NodeUtil.isSingleReturnStatement(functionNode) &&
        NodeUtil.isReturnValueEqualsArgument(functionNode);
  }

  public static boolean isSingleReturnStatement(Node node) {
    Node body = node.getLastChild();
    return body.getType() == Token.BLOCK &&
           body.getFirstChild().getType() == Token.RETURN;
  }

  public static boolean isExprAssign(Node node) {
    Node left = node.getFirstChild();
    Node op = node.getFirstChild().getNext();
    Node right = node.getLastChild();

    return left.getType() == Token.GETPROP &&
           left.getFirstChild().getType() == Token.NAME &&
           op.getType() == Token.ASSIGN &&
           right != null;
  }

  public static boolean isImmutableValue(Node node) {
    Node firstChild = node.getFirstChild();
    return firstChild.getType() == Token.NUMBER ||
           firstChild.getType() == Token.STRING ||
           firstChild.getType() == Token.BOOLEAN ||
           firstChild.getType() == Token.NULL ||
           firstChild.getType() == Token.NAME &&
           firstChild.getString().startsWith("true") ||
           firstChild.getString().startsWith("false");
  }

  public static boolean isGetProp(Node node) {
    return node.getType() == Token.GETPROP;
  }

  public static boolean isThis(Node node) {
    return node.getType() == Token.THIS;
  }

  public static Node getLastChild(Node node) {
    if (node.hasOneChild()) {
      return node.getFirstChild();
    }
    Node parent = node.getParent();
    int index = parent.getChildrenList().indexOf(node);
    return parent.getChildren().get(index - 1);
  }

  public static Node getFirstChild(Node node) {
    return node.getFirstChild();
  }

  public static Node getValueNode(Node functionNode) {
    Node valueNode = maybeGetSingleReturnRValue(functionNode);
    return valueNode != null ? valueNode : null;
  }

  public static Node maybeGetSingleReturnRValue(Node functionNode) {
    Node body = functionNode.getLastChild();
    if (!body.hasOneChild()) {
      return null;
    }

    Node statement = body.getFirstChild();
    if (statement.getType() != Token.RETURN) {
      return null;
    }

    Node value = statement.getFirstChild();
    if (value != null) {
      if (NodeUtil.isImmutableValue(value)) {
        return value;
      }
    }

    return null;
  }

  public static Node isName(Node node) {
    return node.getType() == Token.NAME;
  }

  public static String getString(Node node) {
    return node.getString();
  }
}