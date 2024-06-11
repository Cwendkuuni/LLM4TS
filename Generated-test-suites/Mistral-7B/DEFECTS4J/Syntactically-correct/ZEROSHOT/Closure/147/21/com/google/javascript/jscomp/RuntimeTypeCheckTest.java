import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.RuntimeTypeCheck.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {
  private AbstractCompiler compiler;
  private String logFunction;

  @Before
  public void setUp() {
    compiler = new Compiler(TreeCompilationUnit.EMPTY_TREE);
    logFunction = "function(warning, expr) { print('Warning: ' + expr); }";
  }

  @Test
  public void testAddMarkers() {
    Node root = Node.newBlock(
        new Node(Token.RETURN, new Node(Token.INT_LITERAL, 42)));

    new RuntimeTypeCheck(compiler, logFunction).process(null, root);

    Node functionNode = getFunctionNodeByName(root, "add");
    FunctionType functionType = (FunctionType) functionNode.getJSType();

    Node prototypeNode = getPrototypeNode(functionNode);
    Set<String> markers = new HashSet<>();
    markers.add("instance_of__Add");
    Iterables.addAll(markers, functionType.getAllImplementedInterfaces());

    for (ObjectType interfaceType : functionType.getAllImplementedInterfaces()) {
      markers.add("implements__" + interfaceType.getReferenceName());
    }

    for (Node markerNode : prototypeNode.getChildren()) {
      assertEquals(markerNode.getString(), markers.iterator().next(),
          "Unexpected marker: " + markerNode.getString());
      markers.remove(markerNode.getString());
    }

    assertTrue("Missing markers: " + markers, markers.isEmpty());
  }

  @Test
  public void testAddChecks() {
    String jsCode =
        "function add(x, y) {" +
            "return x + y;" +
            "}";

    Node root = Node.newScript(jsCode);

    new RuntimeTypeCheck(compiler, logFunction).process(null, root);

    Node addFunctionNode = getFunctionNodeByName(root, "add");
    FunctionType addFunctionType = (FunctionType) addFunctionNode.getJSType();

    Node blockNode = addFunctionNode.getLastChild();

    int parameterCount = addFunctionType.getParameterCount();
    for (int i = 0; i < parameterCount; i++) {
      Node parameterNode = blockNode.getChildAtIndex(i);
      Node checkNode = getFirstChildOfType(parameterNode, Token.CALL);

      assertEquals(checkNode.getFirstChild().getString(), "checkType");

      Node checkerArrayNode = checkNode.getLastChild();
      int checkerCount = checkerArrayNode.getNumberOfChildren();
      for (int j = 0; j < checkerCount; j++) {
        Node checkerNode = checkerArrayNode.getChildAtIndex(j);
        Node checkerTypeNode = getFirstChildOfType(checkerNode, Token.GETPROP);
        String checkerTypeString = checkerTypeNode.getString();

        if (i == 0) {
          assertTrue("Unexpected checker type for parameter 0: " + checkerTypeString,
              checkerTypeString.startsWith("numberValueType"));
        } else {
          assertTrue("Unexpected checker type for parameter 1: " + checkerTypeString,
              checkerTypeString.startsWith("instanceType__"));
        }
      }
    }

    Node returnNode = getFirstChildOfType(blockNode, Token.RETURN);
    Node checkNode = getFirstChildOfType(returnNode, Token.CALL);

    assertEquals(checkNode.getFirstChild().getString(), "checkType");

    Node checkerArrayNode = checkNode.getLastChild();
    Node classCheckerNode = getFirstChildOfType(checkerArrayNode, Token.GETPROP);
    String className = classCheckerNode.getString();

    assertEquals("Unexpected return type checker class: " + className,
        "Int", className);
  }

  @Test
  public void testBoilerplateCodeInsertion() {
    String jsCode =
        "function add(x, y) {" +
            "return x + y;" +
            "}";

    Node root = Node.newScript(jsCode);

    new RuntimeTypeCheck(compiler, logFunction).process(null, root);

    Node boilerplateCodeNode = getFirstChildOfType(root, jsCode("checkType"));
    assertNotNull(boilerplateCodeNode);
  }

  @Test
  public void testLogFunctionCall() {
    String jsCode =
        "function add(x, y) {" +
            "if (x instanceof String) {" +
            "  checkType(x, [valueChecker('string')]);" +
            "}" +
            "return x + y;" +
            "}";

    Node root = Node.newScript(jsCode);

    new RuntimeTypeCheck(compiler, logFunction).process(null, root);

    Node addFunctionNode = getFunctionNodeByName(root, "add");
    FunctionType addFunctionType = (FunctionType) addFunctionNode.getJSType();

    Node blockNode = addFunctionNode.getLastChild();

    Node ifStatementNode = getFirstChildOfType(blockNode, Token.IF);
    Node checkNode = getFirstChildOfType(ifStatementNode, Token.CALL);

    assertEquals(checkNode.getFirstChild().getString(), "checkType");

    Node checkerArrayNode = checkNode.getLastChild();
    Node valueCheckerNode = getFirstChildOfType(checkerArrayNode, Token.GETPROP);

    assertEquals(valueCheckerNode.getString(), "valueChecker");

    Node argumentNode = getFirstChildOfType(checkerArrayNode, Token.ARRAYLIT)
        .getChildAtIndex(0);

    assertEquals(argumentNode.getType(), Token.STRING_LITERAL);
    assertEquals(argumentNode.getString(), "string");

    Node logFunctionCallNode = getFirstChildOfType(root, jsCode("function(warning, expr)"));
    assertNotNull(logFunctionCallNode);
  }
}