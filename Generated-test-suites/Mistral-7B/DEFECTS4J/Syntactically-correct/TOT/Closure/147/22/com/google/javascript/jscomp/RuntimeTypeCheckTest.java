package com.google.javascript.jscomp;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  private static final Comparator<JSType> ALPHA = new Comparator<JSType>() {
    public int compare(JSType t1, JSType t2) {
      return getName(t1).compareTo(getName(t2));
    }

    private String getName(JSType type) {
      if (type.isInstanceType()) {
        return ((ObjectType) type).getReferenceName();
      } else if (type.isNullType()
          || type.isBooleanValueType()
          || type.isNumberValueType()
          || type.isStringValueType()
          || type.isVoidType()) {
        return type.toString();
      } else {
        // Type unchecked at runtime, so we don't care about the sorting order.
        return "";
      }
    }
  };

  private RuntimeTypeCheck runtimeTypeCheck;
  private Node rootNode;

  @Before
  public void setUp() throws IOException {
    rootNode = NodePrinter.parseJavaScript("" +
        "function add(x, y) { return x + y; }" +
        "function main() { return add(1, 2); }", null);

    AbstractCompiler compiler = new CompilerOptions().setTarget(CompilerOptions.JsOutput.JS)
        .setCompilerFlags(ImmutableList.of("-Xjscomp:checkTypes"))
        .setExterns(NodePrinter.parseJavaScript(
            "var add: Function;", null))
        .compile(rootNode);

    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testAddFunctionType() {
    FunctionType addType = (FunctionType) rootNode.getChildAt(0).getJSType();
    Node marker = getMarkerNode("add", addType);
    assertEquals("function add(x: number, y: number)", getFunctionTypeString(addType));
    assertEquals("add.prototype.instance_of__add", marker.getFirstChild().getFirstChild().getFirstChild().getStringValue());

    Node checkTypeCall = marker.getLastChild();
    Function checkTypeFunction = (FunctionType) checkTypeCall.getChildAt(0).getJSType();
    assertEquals(3, checkTypeFunction.getParameters().size());
    assertEquals("Function", checkTypeFunction.getReturnType().toString());

    assertEquals("add", checkTypeCall.getChildAt(1).getFirstChild().getStringValue());
    assertEquals("add", checkTypeCall.getChildAt(2).getFirstChild().getStringValue());

    Set<JSType> expectedTypes = Sets.newTreeSet(ALPHA);
    expectedTypes.add(NodeType.NUMBER);
    expectedTypes.add(NodeType.NUMBER);
    expectedTypes.add(NodeType.NUMBER);

    List<JSType> checkerTypes = getCheckerTypes(checkTypeCall.getChildAt(2));
    assertEquals(expectedTypes, checkerTypes);

    // This call should not trigger any type checks
    assertEquals(3, add(1, 2));
  }

  @Test
  public void testAddStringFunctionType() {
    rootNode = NodePrinter.parseJavaScript("" +
        "function add(x, y) { return x + y; }" +
        "function main() { return add('1', '2'); }", null);

    AbstractCompiler compiler = new CompilerOptions().setTarget(CompilerOptions.JsOutput.JS)
        .setCompilerFlags(ImmutableList.of("-Xjscomp:checkTypes"))
        .compile(rootNode);

    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    FunctionType addType = (FunctionType) rootNode.getChildAt(0).getJSType();
    Node marker = getMarkerNode("add", addType);
    assertNull(marker);
  }

  @Test
  public void testAddNumberAndStringFunctionType() {
    rootNode = NodePrinter.parseJavaScript("" +
        "function add(x, y) { return x + y; }" +
        "function main() { return add('1', 2); }", null);

    AbstractCompiler compiler = new CompilerOptions().setTarget(CompilerOptions.JsOutput.JS)
        .setCompilerFlags(ImmutableList.of("-Xjscomp:checkTypes"))
        .compile(rootNode);

    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    FunctionType addType = (FunctionType) rootNode.getChildAt(0).getJSType();
    Node marker = getMarkerNode("add", addType);
    assertNull(marker);
  }

  @Test
  public void testAddFunctionWithExternType() {
    rootNode = NodePrinter.parseJavaScript("" +
        "/** @type {Function} */" +
        "var add; " +
        "function main() { return add(1, 2); }", null);

    AbstractCompiler compiler = new CompilerOptions().setTarget(CompilerOptions.JsOutput.JS)
        .setCompilerFlags(ImmutableList.of("-Xjscomp:checkTypes"))
        .compile(rootNode);

    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    FunctionType addType = (FunctionType) rootNode.getChildAt(0).getJSType();
    Node marker = getMarkerNode("add", addType);
    assertNull(marker);
  }

  @Test
  public void testAddFunctionWithInterfaceImplementation() {
    rootNode = NodePrinter.parseJavaScript("" +
        "/** @interface Add */" +
        "/** @type {Function} */" +
        "var add; " +
        "function main() { return add(1, 2); }", null);

    AbstractCompiler compiler = new CompilerOptions().setTarget(CompilerOptions.JsOutput.JS)
        .setCompilerFlags(ImmutableList.of("-Xjscomp:checkTypes"))
        .compile(rootNode);

    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    FunctionType addType = (FunctionType) rootNode.getChildAt(0).getJSType();
    Node marker = getMarkerNode("add", addType);
    assertNotNull(marker);
    assertEquals("add.prototype.instance_of__Add", marker.getFirstChild().getFirstChild().getFirstChild().getStringValue());
  }

  @Test
  public void testAddFunctionWithInterfaceImplementationAndExternType() {
    rootNode = NodePrinter.parseJavaScript("" +
        "/** @interface Add */" +
        "/** @type {Function} */" +
        "var add; " +
        "/** @type {Function} */" +
        "function add() {}" +
        "function main() { return add(1, 2); }", null);

    AbstractCompiler compiler = new CompilerOptions().setTarget(CompilerOptions.JsOutput.JS)
        .setCompilerFlags(ImmutableList.of("-Xjscomp:checkTypes"))
        .compile(rootNode);

    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    FunctionType addType = (FunctionType) rootNode.getChildAt(1).getJSType();
    Node marker = getMarkerNode("add", addType);
    assertNotNull(marker);
    assertEquals("add.prototype.instance_of__Add", marker.getFirstChild().getFirstChild().getFirstChild().getStringValue());
  }

  private Node getMarkerNode(String name, FunctionType type) {
    for (Node child = rootNode; child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP &&
          child.getFirstChild().getStringValue().equals("prototype")) {
        for (Node grandChild = child.getNext(); grandChild != null;
             grandChild = grandChild.getNext()) {
          if (grandChild.getType() == Token.GETPROP &&
              grandChild.getFirstChild().getStringValue().equals(name) &&
              grandChild.getNext() != null &&
              grandChild.getNext().getType() == Token.GETELEM) {
            Node marker = grandChild.getNext();
            if (marker.getStringValue().startsWith("instance_of__")) {
              return marker;
            }
          }
        }
      }
    }
    return null;
  }

  private List<JSType> getCheckerTypes(Node checkTypeCall) {
    Node checkTypeFunctionCall = checkTypeCall.getChildAt(1);
    Node arrayLiteral = checkTypeFunctionCall.getLastChild();
    List<JSType> checkerTypes = Lists.newArrayList();
    for (Node checkerNode = arrayLiteral.getFirstChild();
         checkerNode != null; checkerNode = checkerNode.getNext()) {
      checkerTypes.add(checkerNode.getFirstChild().getJSType());
    }
    return checkerTypes;
  }

  private String getFunctionTypeString(FunctionType type) {
    StringBuilder sb = new StringBuilder();
    sb.append("function ");
    if (type.isConstructor()) {
      sb.append("constructor ");
    }
    sb.append(type.getName()).append("(");
    for (Node paramNode = type.getParameters().getFirstChild();
         paramNode != null; paramNode = paramNode.getNext()) {
      sb.append(getTypeNameString(paramNode.getJSType()));
      if (paramNode.getNext() != null) {
        sb.append(", ");
      }
    }
    sb.append(") { return ");
    sb.append(getTypeNameString(type.getReturnType()));
    sb.append("; }");
    return sb.toString();
  }

  private String getTypeNameString(JSType type) {
    if (type.isInstanceType()) {
      return "{" + type.getReferenceName() + "}";
    } else if (type.isNullType()) {
      return "null";
    } else if (type.isBooleanValueType()) {
      return "boolean";
    } else if (type.isNumberValueType()) {
      return "number";
    } else if (type.isStringValueType()) {
      return "string";
    } else if (type.isVoidType()) {
      return "void";
    } else {
      return "";
    }
  }

  private int add(int x, int y) {
    return x + y;
  }
}