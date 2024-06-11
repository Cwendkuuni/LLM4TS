import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPassTestUtils.assertPasses;
import static com.google.javascript.jscomp.CompilerPassTestUtils.assertPassesWithErrors;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createGetProp;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createUndefined;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassCallback;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "t");

    String code = "function foo() {" +
        "  var a = {" +
        "    x: 1," +
        "    b: 2" +
        "  };" +
        "  var c = {m: a};" +
        "  var d = a;" +
        "  return [a, c, d];" +
        "}";

    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createFunction("foo", new Node[0], new Node[0]);
    pass.process(externs, root);

    assertPasses(compiler, code, "function foo() {" +
        "  var x_1 = 1;" +
        "  var b_2 = 2;" +
        "  var m_3 = x_1;" +
        "  var c_4 = {m: m_3};" +
        "  var d_5 = x_1;" +
        "  return [x_1, c_4, d_5];" +
        "}");
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValues() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "t");

    String code = "function foo() {" +
        "  var a = {" +
        "    x: 1," +
        "    b: 2" +
        "  };" +
        "  var c = {m: a};" +
        "  a.x = 3;" +
        "  return [a, c];" +
        "}";

    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createFunction("foo", new Node[0], new Node[0]);
    pass.process(externs, root);

    Map<String, Node> initVals = Maps.newHashMap();
    Map<String, String> varmap = Maps.newLinkedHashMap();

    for (Node child = root.getLastChild().getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isVar()) {
        Var v = (Var) child;
        String varName = v.name.getString();
        if (varmap.containsKey(varName)) {
          continue;
        }

        String var = "t_" + varName + "_" + System.identityHashCode(this);
        varmap.put(varName, var);
        initVals.put(varName, child);

        Node value = child.getLastChild();
        if (value != null && value.isObjectLit()) {
          fillInitialValues(value, initVals);
        }
      }
    }

    String expected = "function foo() {" +
        "  var x_1 = {" +
        "    x: 3," +
        "    b: 2" +
        "  };" +
        "  var m_3 = x_1;" +
        "  return [x_1, m_3];" +
        "}";

    assertPasses(compiler, code, expected);
  }

  @Test
  public void testInlineObjectLiteralsWithComplexReferences() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "t");

    String code = "function foo() {" +
        "  var a = {" +
        "    x: 1," +
        "    b: {y: 2}" +
        "  };" +
        "  var c = {m: a};" +
        "  a.b.y = 3;" +
        "  return [a, c];" +
        "}";

    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createFunction("foo", new Node[0], new Node[0]);
    pass.process(externs, root);

    Map<String, Node> initVals = Maps.newHashMap();
    Map<String, String> varmap = Maps.newLinkedHashMap();

    for (Node child = root.getLastChild().getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isVar()) {
        Var v = (Var) child;
        String varName = v.name.getString();
        if (varmap.containsKey(varName)) {
          continue;
        }

        String var = "t_" + varName + "_" + System.identityHashCode(this);
        varmap.put(varName, var);
        initVals.put(varName, child);

        Node value = child.getLastChild();
        if (value != null && value.isObjectLit()) {
          fillInitialValues(value, initVals);
        }
      }
    }

    String expected = "function foo() {" +
        "  var x_1 = {" +
        "    x: 3," +
        "    b: {" +
        "      y: 3" +
        "    }" +
        "  };" +
        "  var m_3 = x_1;" +
        "  return [x_1, m_3];" +
        "}";

    assertPasses(compiler, code, expected);
  }

  @Test
  public void testInlineObjectLiteralsWithCircularReference() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "t");

    String code = "function foo() {" +
        "  var a = {" +
        "    x: 1," +
        "    b: {y: a}" +
        "  };" +
        "  return [a];" +
        "}";

    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createFunction("foo", new Node[0], new Node[0]);
    pass.process(externs, root);

    Map<String, Node> initVals = Maps.newHashMap();
    Map<String, String> varmap = Maps.newLinkedHashMap();

    for (Node child = root.getLastChild().getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isVar()) {
        Var v = (Var) child;
        String varName = v.name.getString();
        if (varmap.containsKey(varName)) {
          continue;
        }

        String var = "t_" + varName + "_" + System.identityHashCode(this);
        varmap.put(varName, var);
        initVals.put(varName, child);

        Node value = child.getLastChild();
        if (value != null && value.isObjectLit()) {
          fillInitialValues(value, initVals);
        }
      }
    }

    String expected = "function foo() {" +
        "  var x_1 = {" +
        "    x: 1," +
        "    b: {" +
        "      y: null" + // This is supposed to be a circular reference
        "    }" +
        "  };" +
        "  return [x_1];" +
        "}";

    assertPassesWithErrors(compiler, code, expected, "Circular reference");
  }

  @Test
  public void testInlineObjectLiteralsWithLValueReference() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "t");

    String code = "function foo() {" +
        "  var a = {" +
        "    x: 1," +
        "    b: 2" +
        "  };" +
        "  var c = a;" +
        "  a.x = 3;" +
        "  return c;" +
        "}";

    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createFunction("foo", new Node[0], new Node[0]);
    pass.process(externs, root);

    Map<String, Node> initVals = Maps.newHashMap();
    Map<String, String> varmap = Maps.newLinkedHashMap();

    for (Node child = root.getLastChild().getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isVar()) {
        Var v = (Var) child;
        String varName = v.name.getString();
        if (varmap.containsKey(varName)) {
          continue;
        }

        String var = "t_" + varName + "_" + System.identityHashCode(this);
        varmap.put(varName, var);
        initVals.put(varName, child);

        Node value = child.getLastChild();
        if (value != null && value.isObjectLit()) {
          fillInitialValues(value, initVals);
        }
      }
    }

    String expected = "function foo() {" +
        "  var x_1 = 1;" +
        "  var b_2 = 2;" +
        "  var c_3 = null;" + // c was ref to a, now a is ref to a new var
        "  var x_4 = 3;" +
        "  c_3 = {" +
        "    x: x_4" +
        "  };" +
        "  return c_3;" +
        "}";

    assertPasses(compiler, code, expected);
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionDefinition() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "t");

    String code = "function foo() {" +
        "  var a = {" +
        "    x: 1," +
        "    b: {m: function bar() {}}," +
        "    c: []" +
        "  };" +
        "  return a;" +
        "}";

    Node externs = NodeFactory.createRootNode();
    Node root = NodeFactory.createFunction("foo", new Node[0], new Node[0]);
    pass.process(externs, root);

    Map<String, Node> initVals = Maps.newHashMap();
    Map<String, String> varmap = Maps.newLinkedHashMap();

    for (Node child = root.getLastChild().getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isVar()) {
        Var v = (Var) child;
        String varName = v.name.getString();
        if (varmap.containsKey(varName)) {
          continue;
        }

        String var = "t_" + varName + "_" + System.identityHashCode(this);
        varmap.put(varName, var);
        initVals.put(varName, child);

        Node value = child.getLastChild();
        if (value != null && (value.isObjectLit() || value.isFunction())) {
          fillInitialValues(value, initVals);
        }
      }
    }

    String expected = "function foo() {" +
        "  var x_1 = 1;" +
        "  var m_2 = function bar() {};" +
        "  var c_3 = [];" +
        "  var a_4 = {" +
        "    x: x_1," +
        "    b: {" +
        "      m: m_2" +
        "    }," +
        "    c: c_3" +
        "  };" +
        "  return a_4;" +
        "}";

    assertPasses(compiler, code, expected);
  }

  private void fillInitialValues(Node node, Map<String, Node> initVals) {
    if (node.isObjectLit()) {
      for (Node child = node.getFirstChild(); child != null;
           child = child.getNext()) {
        String key = child.getString();
        Node value = child.removeFirstChild();
        initVals.put(key, value);
      }
    }
  }
}