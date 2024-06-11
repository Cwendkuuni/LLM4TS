import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class VarCheckTest {

  @Test
  public void testVarCheck() {
    String validCode = "function foo() {" +
                       "var x = 1;" +
                       "var y = 2;" +
                       "}";

    CompilerInput input = new CompilerInput("Test", validCode, null, null, null);
    Node root = new Node(Token.PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), false).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertEquals(0, traversal.getDiagnostics().size());

    String invalidCode1 = "function foo() {" +
                          "var x;" + // missing initialization
                          "}";

    input = new CompilerInput("Test", invalidCode1, null, null, null);
    root = new Node(Token.PROGRAM, null);
    traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), false).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("variable x is undefined", traversal.getDiagnostics().get(0).getMessage());

    String invalidCode2 = "function foo() {" +
                          "var x = y;" + // reference undefined variable
                          "}";

    input = new CompilerInput("Test", invalidCode2, null, null, null);
    root = new Node(Token.PROGRAM, null);
    traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), false).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("variable y is undefined", traversal.getDiagnostics().get(0).getMessage());

    String invalidCode3 = "function foo() {" +
                          "var x = 1;" +
                          "var y = x;" + // assign value from variable
                          "y = 2;" + // re-assign value
                          "}";

    input = new CompilerInput("Test", invalidCode3, null, null, null);
    root = new Node(Token.PROGRAM, null);
    traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), false).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("variable y is undefined", traversal.getDiagnostics().get(0).getMessage());

    String invalidCode4 = "function foo() {" +
                          "var x = 1;" +
                          "function bar() {" +
                          "var y = x;" + // reference local variable
                          "}" +
                          "}" +
                          "bar();";

    input = new CompilerInput("Test", invalidCode4, null, null, null);
    root = new Node(Token.PROGRAM, null);
    traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), false).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("module Test cannot reference bar, defined in module Test, since Test " +
        "loads after bar", traversal.getDiagnostics().get(0).getMessage());

    String invalidCode5 = "function foo() {" +
                          "function bar() {}" + // module declaration with empty name
                          "}" +
                          "bar();";

    input = new CompilerInput("Test", invalidCode5, null, null, null);
    root = new Node(Token.PROGRAM, null);
    traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), false).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("function declaration must have a name", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testVarCheckSanityCheck() {
    String code = "function foo() {" +
                  "var x = 1;" +
                  "function bar() {" +
                  "var y = x;" +
                  "}" +
                  "}";

    CompilerInput input = new CompilerInput("Test", code, null, null, null);
    Node root = new Node(Token.PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(new Compiler(), root, input);
    new VarCheck(new Compiler(), true).process(input.getExterns(), root);
    traversal.traverseRoots(input, Lists.newArrayList(input.getExterns(), root), null);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }
}