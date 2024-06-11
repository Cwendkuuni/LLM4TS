import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @org.junit.Test
  public void testInlineSimpleObject() throws Exception {
    String source = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "  };" +
        "  return obj.a;";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, new StubNameIdSupplier());
    pass.process(null, null);
    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    assertEquals(3, countVars(root));

    Node expected = IR.returnNode(IR.getInt(1));
    Node actual = getReturnNode(root);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testInlineComplexObject() throws Exception {
    String source = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: {" +
        "        d: 2," +
        "        e: {" +
        "          f: 3," +
        "          g: 4," +
        "        }" +
        "      }" +
        "    }," +
        "    b: 2," +
        "  };" +
        "  return obj.a.c.e.f;";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, new StubNameIdSupplier());
    pass.process(null, null);
    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    assertEquals(11, countVars(root));

    Node expected = IR.returnNode(IR.getInt(3));
    Node actual = getReturnNode(root);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testInlineObjectWithFunction() throws Exception {
    String source = "function foo() {" +
        "  var obj = {" +
        "    bar: function() {" +
        "      return 1;" +
        "    }" +
        "  };" +
        "  return obj.bar();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, new StubNameIdSupplier());
    pass.process(null, null);
    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    assertEquals(4, countVars(root));

    Node expected = IR.returnNode(IR.getInt(1));
    Node actual = getReturnNode(root);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testInlineObjectWithCycle() throws Exception {
    String source = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: obj2" +
        "  };" +
        "  var obj2 = {" +
        "    a: 2," +
        "    b: obj1" +
        "  };" +
        "  return obj1.a;";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, new StubNameIdSupplier());
    pass.process(null, null);
    Node root = compiler.parseTree(source, null, null);
    try {
      pass.process(null, root);
      fail("Expected an error.");
    } catch (RuntimeException e) {
      // Expected exception
    }

    source = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: {" +
        "      c: obj2" +
        "    }" +
        "  };" +
        "  var obj2 = {" +
        "    a: 2," +
        "    c: obj1" +
        "  };" +
        "  return obj1.a;";

    root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    assertEquals(5, countVars(root));

    Node expected = IR.returnNode(IR.getInt(1));
    Node actual = getReturnNode(root);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testInlineObjectWithConcat() throws Exception {
    String source = "function foo() {" +
        "  var obj = {" +
        "    a: 'abc'," +
        "    b: ''" +
        "  };" +
        "  return obj.a.concat(obj.b);";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, new StubNameIdSupplier());
    pass.process(null, null);
    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    assertEquals(3, countVars(root));

    Node expected = IR.returnNode(IR.string("abc"));
    Node actual = getReturnNode(root);
    assertEquals(expected, actual);
  }

  private static int countVars(Node root) {
    int count = 0;
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isVar()) {
          count++;
        }
      }
    });
    return count;
  }

  private static Node getReturnNode(Node root) {
    Node expectedNode = root;
    while (expectedNode != null && expectedNode.getType() != Token.RET) {
      expectedNode = expectedNode.getParent();
    }
    return expectedNode;
  }

  private static class StubNameIdSupplier implements Supplier<String> {
    private int id = 0;

    @Override
    public String get() {
      return "stub_" + (++id);
    }
  }
}