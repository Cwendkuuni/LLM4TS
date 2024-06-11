import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Kind.TRAVERSE;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CollapseVariableDeclarationsTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private CollapseVariableDeclarations pass;

  @Before
  public void setUp() {
    options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
    pass = new CollapseVariableDeclarations(compiler);
  }

  @Test
  public void testCollapseSingleVariableDeclarations() {
    String code = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a, b = 1, c = 2");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }

  @Test
  public void testCollapseTwoVariableDeclarations() {
    String code = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a, b = 1, c = 2, d = 3");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }

  @Test
  public void testCollapseVariableAssignments() {
    String code = "function f() {\n" +
        "  var a = 1;\n" +
        "  var b = 1;\n" +
        "  var c = 1;\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR, "a = b = c = 1");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }

  @Test
  public void testCollapseVariableAssignmentsWithInitializers() {
    String code = "function f() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = {a: 3};\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR,
        "a = 1, b = 2, c = {a: 3}");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }

  @Test
  public void testCollapseVariableDeclarationsInFunction() {
    String code = "function f() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  function g() {\n" +
        "    var c = 3;\n" +
        "    var d = 4;\n" +
        "  }\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR,
        "a = 1, b = 2, g: function() {c = d = undefined}");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }

  @Test
  public void testCollapseVariableDeclarationsInConditional() {
    String code = "function f() {\n" +
        "  if (true) {\n" +
        "    var a = 1;\n" +
        "    var b = 2;\n" +
        "  } else {\n" +
        "    var c = 3;\n" +
        "    var d = 4;\n" +
        "  }\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR,
        "a = 1, b = 2, c = 3, d = 4");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }

  @Test
  public void testCollapseVariableDeclarationsWithJSDocs() {
    String code = "function f() {\n" +
        "  /**\n" +
        "   * @var {number} a\n" +
        "   */\n" +
        "  var a = 1;\n" +
        "  /**\n" +
        "   * @var {number} b\n" +
        "   */\n" +
        "  var b = 2;\n" +
        "  /**\n" +
        "   * @var {Object} c\n" +
        "   */\n" +
        "  var c = {a: 3};\n" +
        "}";

    Node root = ParserTestUtil.parseAndCompileToRoot(code, compiler);

    pass.process(null, root);

    Node expected = new Node(Token.VAR,
        "/** @var {number} a */ a = 1," +
        "/** @var {number} b */ b = 2," +
        "/** @var {Object} c */ c = {a: 3}");

    Node actual = root.getFirstChild();
    Node current = actual;

    while (current.getNext() != null) {
      current = current.getNext();
    }

    assertEquals(expected, current);
  }
}

class ParserTestUtil {
  public static Node parseAndCompileToRoot(String code, AbstractCompiler compiler) {
    Node externs = new Node(Token.PROGRAM);
    Node root = new Node(Token.PROGRAM);
    Node script = new Node(Token.SCRIPT, null, null, code, null, null);

    NodeTraversal.traverse(compiler, externs, new TraverseAndCompileScript(compiler, root, script));

    return root;
  }
}

class TraverseAndCompileScript extends NodeTraversal.AbstractPostOrderCallback {
  private final AbstractCompiler compiler;
  private final Node root;
  private final Node script;

  public TraverseAndCompileScript(AbstractCompiler compiler, Node root, Node script) {
    this.compiler = compiler;
    this.root = root;
    this.script = script;
  }

  @Override
  public Kind getKind() {
    return TRAVERSE;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isProgram()) {
      n.setFirstChild(script);
      root.setFirstChild(n);
      compiler.process(null, root, null);
    }
  }
}