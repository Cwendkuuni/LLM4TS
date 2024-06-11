import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RenamePrototypesTest {

  private Compiler compiler;
  private TokenStream input;
  private TokenStream expectedOutput;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Info(null, null, null));
    compiler = new Compiler(options);
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype.bar = function() {};\n" +
            "Foo.prototype.baz = 42;\n" +
            "var x = { foo : 1, bar : 2 };\n" +
            "Foo.prototype = x;"));
    expectedOutput = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype.x_$1$ = function() {};\n" +
            "Foo.prototype.x_$2$ = 42;\n" +
            "var x = { foo : 1, bar : 2 };\n" +
            "Foo.prototype = x;"));
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
  }

  @Test
  public void testRenamePrototypes() {
    renamePrototypes.process(input, null);
    assertEquals(expectedOutput.toString(), compiler.getDebugLog());
    assertArrayEquals(new Node[]{input.readToken(Token.EOF)}, compiler.getDiagnostics());
  }

  @Test
  public void testRenamePrototypeProperty() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype.bar = function() {}\n" +
            "Foo.prototype.getBar = function() { return this.bar; };"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getFirstChild().getString().equals("prototype")) {
          Node prototype = n.getLastChild();
          if (prototype.getType() == OBJECTLIT) {
            Node bar = null;
            for (Node child = prototype.getFirstChild(); child != null; child = child.getNext()) {
              if (child.getType() == GETPROP && child.getFirstChild().getString().equals("bar")) {
                bar = child.getLastChild();
                break;
              }
            }
            assertNotNull(bar);
            assertEquals("x_$1_$", bar.getString());
          }
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRenameObjLitProperty() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "var x = { foo : 1, bar : 2 };\n" +
            "Foo.prototype = x;"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == OBJECTLIT && n.hasProperty("foo")) {
          Node foo = n.getProperty("foo");
          assertEquals("x_$2_$", foo.getString());
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDoNotRenameExportedProperty() {
    input = new TokenStream(new TokenInputStream(
        "var bar = 42;\n" +
            "Foo.prototype.bar = bar;"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {}).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDoNotRenamePrivateProperty() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "var _bar = 42; // private\n" +
            "Foo.prototype._bar = _bar;"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {}).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test(expected = AssertionError.class)
  public void testRenamePublicPropertyWithUppercaseLetter() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype.MyName = function() {}\n"));
    renamePrototypes.process(input, null);
    // The test will fail as expected since the test case is designed to check for an AssertionError
  }

  @Test
  public void testRenamePrototypeParentWithObjLit() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype = { getBar : function() {}}\n"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getFirstChild().getString().equals("prototype")) {
          Node prototype = n.getLastChild();
          if (prototype.getType() == OBJECTLIT) {
            Node getBar = null;
            for (Node child = prototype.getFirstChild(); child != null; child = child.getNext()) {
              if (child.getType() == GETPROP && child.getFirstChild().getString().equals("getBar")) {
                getBar = child.getLastChild();
                break;
              }
            }
            assertNotNull(getBar);
            assertEquals("x_$1_$", getBar.getString());
          }
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRenamePrototypeParentWithPrototype() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype = Foo.prototype;"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getFirstChild().getString().equals("prototype")) {
          Node prototype = n.getLastChild();
          if (prototype.getType() == GETPROP && prototype.getFirstChild().getString().equals("prototype")) {
            Node prototypeOfPrototype = prototype.getLastChild();
            assertEquals("x_$1_$prototype_", prototypeOfPrototype.getString());
          }
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRenamePrototypeParentWithPrototypeAndObjLit() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype = Foo.prototype;\n" +
            "Foo.prototype = { getBar : function() {}}\n"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getFirstChild().getString().equals("prototype")) {
          Node prototype = n.getLastChild();
          if (prototype.getType() == GETPROP && prototype.getFirstChild().getString().equals("prototype")) {
            Node prototypeOfPrototype = prototype.getLastChild();
            assertEquals("x_$1_$prototype_", prototypeOfPrototype.getString());
            Node getBar = null;
            if (prototype.getType() == OBJECTLIT) {
              for (Node child = prototype.getFirstChild(); child != null; child = child.getNext()) {
                if (child.getType() == GETPROP && child.getFirstChild().getString().equals("getBar")) {
                  getBar = child.getLastChild();
                  break;
                }
              }
            }
            assertNotNull(getBar);
            assertEquals("x_$1_$getBar_", getBar.getString());
          }
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRenamePrototypeParentWithPrototypeAndPrototype() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype = Foo.prototype;\n" +
            "Foo.prototype = Foo.prototype;"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getFirstChild().getString().equals("prototype")) {
          Node prototype = n.getLastChild();
          if (prototype.getType() == GETPROP && prototype.getFirstChild().getString().equals("prototype")) {
            Node prototypeOfPrototype = prototype.getLastChild();
            assertEquals("x_$1_$prototype_", prototypeOfPrototype.getString());
            Node prototypeOfPrototypeOfPrototype = null;
            if (prototype.getType() == GETPROP) {
              prototypeOfPrototypeOfPrototype = prototype.getLastChild();
            }
            assertEquals("x_$1_$prototype_", prototypeOfPrototypeOfPrototype.getString());
          }
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRenamePrototypeParentWithPrototypeAndObjLitAndPrototype() {
    input = new TokenStream(new TokenInputStream(
        "function Foo() {}\n" +
            "Foo.prototype = Foo.prototype;\n" +
            "Foo.prototype = Foo.prototype;\n" +
            "Foo.prototype = { getBar : function() {}};"));
    renamePrototypes.process(input, null);
    Node actualOutput = compiler.getAST();
    Node expectedOutput = new NodeTraversal(compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getFirstChild().getString().equals("prototype")) {
          Node prototype = n.getLastChild();
          if (prototype.getType() == GETPROP && prototype.getFirstChild().getString().equals("prototype")) {
            Node prototypeOfPrototype = prototype.getLastChild();
            assertEquals("x_$1_$prototype_", prototypeOfPrototype.getString());
            Node prototypeOfPrototypeOfPrototype = null;
            if (prototype.getType() == GETPROP) {
              prototypeOfPrototypeOfPrototype = prototype.getLastChild();
            }
            assertEquals("x_$1_$prototype_", prototypeOfPrototypeOfPrototype.getString());
            Node getBar = null;
            if (prototype.getType() == OBJECTLIT) {
              for (Node child = prototype.getFirstChild(); child != null; child = child.getNext()) {
                if (child.getType() == GETPROP && child.getFirstChild().getString().equals("getBar")) {
                  getBar = child.getLastChild();
                  break;
                }
              }
            }
            assertNotNull(getBar);
            assertEquals("x_$1_$getBar_", getBar.getString());
          }
        }
      }
    }).process(compiler.getAST(), null).getAST();
    assertEquals(expectedOutput, actualOutput);
  }
}