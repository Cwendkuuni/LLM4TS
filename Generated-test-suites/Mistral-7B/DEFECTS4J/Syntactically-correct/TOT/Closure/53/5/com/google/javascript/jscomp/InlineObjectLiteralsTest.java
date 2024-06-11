import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String jsCode = "function Foo() {" +
                    "  this.obj = {" +
                    "    a: 1," +
                    "    b: 2," +
                    "    c: function() {}" +
                    "  };" +
                    "}" +
                    "Foo.prototype.bar = function() {" +
                    "  return this.obj.a + this.obj.b;" +
                    "};";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(jsCode);

    // Check that the object literal has been inlined and converted into individual variables.
    Node foobar = NodeUtil.findFunctionByName(root, "Foo");
    Node objVar = null;
    for (Node statement : foobar.getBody()) {
      if (statement.getType() == Token.VAR) {
        for (Node child : statement.getChildren()) {
          if (child.getType() == Token.NAME && child.getString().startsWith("JSCompiler_object_inline_")) {
            objVar = child;
            break;
          }
        }
        if (objVar != null) {
          break;
        }
      }
    }
    assertEquals("a should be inlined as JSCompiler_object_inline_a", 3, objVar.getChildren().size());
    for (int i = 0; i < objVar.getChildren().size(); i++) {
      Node keyNode = objVar.getChildren().get(i).getFirstChild();
      assertEquals("Key name should match", String.format("JSCompiler_object_inline_a_%d", i), keyNode.getString());
    }

    // Check that the method bar() still works as expected.
    Node bar = NodeUtil.findMethodByName(foobar, "bar");
    Node call = new Node(Token.CALL, bar, NodeUtil.newThisNode(null));
    Node result = NodeUtil.runScript(NodeUtil.createRootNode(call), null);
    assertEquals("The sum of a and b should be 3", 3, result.getNumber().intValue());
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    String jsCode = "function Foo() {" +
                    "  this.obj = {" +
                    "    a: 1," +
                    "    b: this.obj" +
                    "  };" +
                    "}" +
                    "Foo.prototype.bar = function() {" +
                    "  return this.obj.a + this.obj.b;" +
                    "};";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(jsCode);

    // This test case should fail due to the detection of a circular object reference.
    try {
      Node foobar = NodeUtil.findFunctionByName(root, "Foo");
      Node objVar = null;
      for (Node statement : foobar.getBody()) {
        if (statement.getType() == Token.VAR) {
          for (Node child : statement.getChildren()) {
            if (child.getType() == Token.NAME && child.getString().startsWith("JSCompiler_object_inline_")) {
              objVar = child;
              break;
            }
          }
          if (objVar != null) {
            break;
          }
        }
      }
      assertEquals("Object should not be inlined due to a cycle", null, objVar);
      fail("Test should fail due to a cycle");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    String jsCode = "var x = {" +
                    "  a: 1," +
                    "  b: 2," +
                    "};" +
                    "export function Foo() {" +
                    "  this.obj = x;" +
                    "}" +
                    "Foo.prototype.bar = function() {" +
                    "  return this.obj.a + this.obj.b;" +
                    "};";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(jsCode);

    // The variable x is exported and should not be inlined.
    Node foobar = NodeUtil.findFunctionByName(root, "Foo");
    Node objVar = null;
    for (Node statement : foobar.getBody()) {
      if (statement.getType() == Token.VAR) {
        for (Node child : statement.getChildren()) {
          if (child.getType() == Token.NAME && child.getString().startsWith("JSCompiler_object_inline_")) {
            objVar = child;
            break;
          }
        }
        if (objVar != null) {
          break;
        }
      }
    }
    assertEquals("Object should not be inlined as it's an exported variable", null, objVar);
  }

  @Test
  public void testInlineObjectLiteralsWithExportedFunction() {
    String jsCode = "var x = {" +
                    "  a: 1," +
                    "  b: function() { return 2; }" +
                    "};" +
                    "export function Foo() {" +
                    "  this.obj = x;" +
                    "}" +
                    "Foo.prototype.bar = function() {" +
                    "  return this.obj.a + this.obj.b();" +
                    "};";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(jsCode);

    // The function x.b() is an exported function and should not be inlined.
    Node foobar = NodeUtil.findFunctionByName(root, "Foo");
    Node objVar = null;
    for (Node statement : foobar.getBody()) {
      if (statement.getType() == Token.VAR) {
        for (Node child : statement.getChildren()) {
          if (child.getType() == Token.NAME && child.getString().startsWith("JSCompiler_object_inline_")) {
            objVar = child;
            break;
          }
        }
        if (objVar != null) {
          break;
        }
      }
    }
    assertEquals("Function should not be inlined as it's an exported function", null, objVar);
  }
}