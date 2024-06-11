import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RuntimeTypeCheck.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.TreeSet;

public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private CompilerPass runtimeTypeCheck;

  @Before
  public void setUp() {
    compiler = new Compiler();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testAddMarkers() {
    String code = "function Foo() {}";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    TreeSet<String> markerNames = new TreeSet<>();
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.GETPROP && "prototype".equals(n.getFirstChild().getStringValue())) {
          Node marker = n.getNext();
          if (marker.getType() == Token.IDENT) {
            markerNames.add(marker.getStringValue());
          }
        }
      }
    });

    assertEquals(Collections.singletonList("instance_of__Foo"), markerNames);
  }

  @Test
  public void testAddChecks() {
    String code = "function Foo(x) { return x; }";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node checkNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getType() == Token.IDENT &&
            "checkType".equals(n.getFirstChild().getStringValue())) {
          checkNode = n;
        }
      }
    });

    assertNotNull(checkNode);

    Node functionNode = NodeUtil.getFunctionDeclaration(root, 0);
    Node parameterNode = functionNode.getFirstChild().getNext();

    Node expectedCheckNode = new Node(Token.CALL, jsCode("valueChecker"),
        new Node(Token.STRING, "number"));
    Node expectedFunctionCall = new Node(Token.CALL, jsCode("checkType"),
        checkNode, new Node(Token.ARRAYLIT), expectedCheckNode);

    assertEquals(expectedFunctionCall, checkNode);
  }

  @Test
  public void testAddChecksForUnionType() {
    String code = "function Foo(x) { if (x instanceof Number || x instanceof String) { return x; } }";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node checkNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getType() == Token.IDENT &&
            "checkType".equals(n.getFirstChild().getStringValue())) {
          checkNode = n;
        }
      }
    });

    assertNotNull(checkNode);

    Node functionNode = NodeUtil.getFunctionDeclaration(root, 0);
    Node parameterNode = functionNode.getFirstChild().getNext();

    Node expectedCheckNode1 = new Node(Token.CALL, jsCode("valueChecker"),
        new Node(Token.STRING, "number"));
    Node expectedCheckNode2 = new Node(Token.CALL, jsCode("valueChecker"),
        new Node(Token.STRING, "string"));
    Node expectedArray = new Node(Token.ARRAYLIT);
    expectedArray.addChildToBack(expectedCheckNode1);
    expectedArray.addChildToBack(expectedCheckNode2);

    Node expectedFunctionCall = new Node(Token.CALL, jsCode("checkType"),
        checkNode, new Node(Token.ARRAYLIT), expectedArray);

    assertEquals(expectedFunctionCall, checkNode);
  }

  @Test
  public void testAddChecksForClassType() {
    String code = "class Foo { constructor(x) { this.x = x; } }";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node checkNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getType() == Token.IDENT &&
            "checkType".equals(n.getFirstChild().getStringValue())) {
          checkNode = n;
        }
      }
    });

    assertNotNull(checkNode);

    Node constructorNode = NodeUtil.getFunctionDeclaration(root, 0);
    Node thisNode = constructorNode.getLastChild();

    Node expectedCheckNode = new Node(Token.CALL, jsCode("classChecker"),
        new Node(Token.STRING, "Foo"));

    Node expectedFunctionCall = new Node(Token.CALL, jsCode("checkType"),
        checkNode, new Node(Token.ARRAYLIT), expectedCheckNode);

    assertEquals(expectedFunctionCall, checkNode);
  }

  @Test
  public void testAddChecksForInterfaceType() {
    String code = "interface Foo { x: number; }";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node checkNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getType() == Token.IDENT &&
            "checkType".equals(n.getFirstChild().getStringValue())) {
          checkNode = n;
        }
      }
    });

    assertNotNull(checkNode);

    Node expectedCheckNode = new Node(Token.CALL, jsCode("interfaceChecker"),
        new Node(Token.STRING, "Foo"));

    Node expectedFunctionCall = new Node(Token.CALL, jsCode("checkType"),
        checkNode, new Node(Token.ARRAYLIT), expectedCheckNode);

    assertEquals(expectedFunctionCall, checkNode);
  }

  @Test
  public void testAddChecksForNullType() {
    String code = "function Foo() { return null; }";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node checkNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getType() == Token.IDENT &&
            "checkType".equals(n.getFirstChild().getStringValue())) {
          checkNode = n;
        }
      }
    });

    assertNotNull(checkNode);

    Node expectedCheckNode = NodeUtil.newNullLiteral();

    Node expectedFunctionCall = new Node(Token.CALL, jsCode("checkType"),
        checkNode, new Node(Token.ARRAYLIT), expectedCheckNode);

    assertEquals(expectedFunctionCall, checkNode);
  }

  @Test
  public void testAddChecksForVoidType() {
    String code = "function Foo() { return; }";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node checkNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getType() == Token.IDENT &&
            "checkType".equals(n.getFirstChild().getStringValue())) {
          checkNode = n;
        }
      }
    });

    assertNotNull(checkNode);

    Node expectedFunctionCall = new Node(Token.CALL, jsCode("checkType"),
        checkNode, new Node(Token.ARRAYLIT), NodeUtil.newUndefinedLiteral());

    assertEquals(expectedFunctionCall, checkNode);
  }

  @Test
  public void testAddBoilerplateCode() {
    String code = "function Foo() {}";
    Node root = NodeUtil.parseJs(code);
    runtimeTypeCheck.process(null, root);

    Node boilerplateCodeNode = null;
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (isJsCompRuntimeTypeCheckBoilerplateCode(n)) {
          boilerplateCodeNode = n;
        }
      }
    });

    assertNotNull(boilerplateCodeNode);

    Node expectedBoilerplateCode = NodeUtil.parseJs("js/runtime_type_check.js");

    assertEquals(expectedBoilerplateCode, boilerplateCodeNode);
  }

  private boolean isJsCompRuntimeTypeCheckBoilerplateCode(Node node) {
    if (node.getType() != Token.SCRIPT) {
      return false;
    }

    String code = NodeUtil.getScriptCode(node);
    return code.contains("function checkType");
  }
}