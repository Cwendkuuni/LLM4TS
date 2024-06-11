import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JSEmitter.*;
import static com.google.javascript.jscomp.CompilerOptions.Option.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private Compiler compiler;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOption(DECLARE_EXPLICIT_VARS, true);
    compiler.setOption(CHECK_TYPES, false);
    compiler.setOption(CHECK_ARITY, false);
    compiler.setOption(GENERATE_SOURCE_MAP, false);

    String input =
        "var x;\n" +
        "function foo() {\n" +
        "  var y = x;\n" +
        "}\n" +
        "var z = 1;\n" +
        "module foo {\n" +
        "  export var x = 5;\n" +
        "  export function bar() {\n" +
        "    var y = x;\n" +
        "  }\n" +
        "}";

    CompilerInput inputNode = compiler.compile(input, "test.js");
    root = inputNode.getAstRoot(compiler);
  }

  @Test
  public void testUndefinedVar() {
    VarCheck check = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(root, check);
    NodeTraversal.traverse(compiler, root, check);

    Scope scope = traversal.getScope();
    Node xNode = scope.getVar("x").getDeclaration();
    assertNull(xNode);

    DiagnosticMessage message = check.getLastDiagnosticMessage();
    assertEquals(DiagnosticType.UNDEFINED_VAR_ERROR, message.getType());
    assertEquals("variable x is undefined", message.getMessage());
  }

  @Test
  public void testModuleDependency() {
    VarCheck check = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(root, check);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root), check);

    Node fooModule = getChildByProperty(root, "foo");
    Node fooBarFunction = getChildByProperty(fooModule, "bar");

    Node yNode = getChildByProperty(fooBarFunction, "y");
    assertEquals(TOKEN_VAR, yNode.getType());

    DiagnosticMessage message = check.getLastDiagnosticMessage();
    assertNull(message);
  }

  @Test
  public void testStrictModuleDependency() {
    VarCheck check = new VarCheck(compiler, true);
    NodeTraversal traversal = new NodeTraversal(root, check);

    Node fooModule = getChildByProperty(root, "foo");
    Node fooBarFunction = getChildByProperty(fooModule, "bar");

    Node yNode = getChildByProperty(fooBarFunction, "y");
    assertEquals(TOKEN_VAR, yNode.getType());

    DiagnosticMessage message = check.getLastDiagnosticMessage();
    assertNull(message);

    compiler.setOption(STRICT_MODE, true);
    check = new VarCheck(compiler, true);

    NodeTraversal traversalStrict = new NodeTraversal(root, check);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root), check);

    message = check.getLastDiagnosticMessage();
    assertNotNull(message);
    assertEquals(DiagnosticType.STRICT_MODULE_DEPENDENCY_ERROR, message.getType());
    assertTrue(message.getMessage().contains("module foo cannot reference x,"));
  }

  @Test
  public void testFunctionDeclarationNameMissing() {
    String input = "function foo() {}";

    VarCheck check = new VarCheck(compiler);
    CompilerInput inputNode = compiler.compile(input, "test.js");
    root = inputNode.getAstRoot(compiler);

    Node functionNode = getChildByProperty(root, "foo");
    assertNotNull(functionNode);
    assertEquals(TOKEN_FUNCTION, functionNode.getType());
    assertEquals(functionNode.getString(), "foo");

    DiagnosticMessage message = check.getLastDiagnosticMessage();
    assertNotNull(message);
    assertEquals(DiagnosticType.INVALID_FUNCTION_DECL, message.getType());
    assertEquals("Syntax error: function declaration must have a name", message.getMessage());
  }

  private Node getChildByProperty(Node parent, String name) {
    for (Node child : parent.getChildren()) {
      if (child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }

  private DiagnosticMessage getLastDiagnosticMessage() {
    VarCheck check = (VarCheck) NodeTraversal.getCurrentCallback();
    return check.getLastDiagnosticMessage();
  }
}