import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String source = "function foo() {"
        + "  var obj = {a: 1, b: 2};"
        + "  var x = obj.a;"
        + "  var y = obj.b;"
        + "  obj = {c: 3};"
        + "  x = obj.a;"
        + "  y = obj.b;"
        + "}";

    Node externs = Node.externalSource("foo", source);
    Node root = Node.parse(source);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, new IdentityNameIdSupplier());
    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    traversal.traverse();

    Map<String, Node> expectedVars = ImmutableMap.of(
        "JSCompiler_object_inline_a_", root.getFirstChild(),
        "JSCompiler_object_inline_b_", root.getFirstChild().getNext(),
        "JSCompiler_object_inline_c_", root.getFirstChild().getNext().getNext());

    for (Map.Entry<String, Node> entry : expectedVars.entrySet()) {
      Node actual = traversal.getScope().getVar(entry.getKey());
      assertEquals(entry.getValue(), actual);
    }

    Node expectedX = root.getFirstChild().getNext().getNext();
    Node expectedY = expectedX.getNext();
    Node expectedZ = root.getFirstChild().getNext().getNext().getNext();

    Node actualX = traversal.getScope().getVar("x");
    Node actualY = traversal.getScope().getVar("y");
    Node actualZ = traversal.getScope().getVar("z");

    assertEquals(expectedX, actualX);
    assertEquals(expectedY, actualY);
    assertEquals(expectedZ, actualZ);
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String source = "function foo() {"
        + "  var obj = {};"
        + "  var x = obj.a;"
        + "}";

    Node externs = Node.externalSource("foo", source);
    Node root = Node.parse(source);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, new IdentityNameIdSupplier());
    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    traversal.traverse();

    Node expectedVar = root.getFirstChild();
    Node actualVar = traversal.getScope().getVar("JSCompiler_object_inline_");

    assertEquals(expectedVar, actualVar);

    Node expectedX = expectedVar.getNext();
    Node actualX = traversal.getScope().getVar("x");

    assertEquals(expectedX, actualX);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialObject() {
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String source = "function foo() {"
        + "  var obj = {a: {b: obj}};"
        + "}";

    Node externs = Node.externalSource("foo", source);
    Node root = Node.parse(source);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, new IdentityNameIdSupplier());
    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    traversal.traverse();

    Node expectedVar = root.getFirstChild();
    Node actualVar = traversal.getScope().getVar("JSCompiler_object_inline_");

    assertEquals(expectedVar, actualVar);

    Node expectedAB = expectedVar.getFirstChild();
    Node expectedB = expectedAB.getNext();
    Node expectedC = expectedB.getNext();

    Node actualAB = traversal.getScope().getVar("JSCompiler_object_inline_a_");
    Node actualB = traversal.getScope().getVar("JSCompiler_object_inline_b_");
    Node actualC = traversal.getScope().getVar("JSCompiler_object_inline_c_");

    assertEquals(expectedAB, actualAB);
    assertEquals(expectedB, actualB);
    assertEquals(expectedC, actualC);
  }

  @Test
  public void testInlineObjectLiteralsWithEarlyReturn() {
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String source = "function foo() {"
        + "  if (true) {"
        + "    var obj = {a: 1};"
        + "  }"
        + "  return obj.a;"
        + "}";

    Node externs = Node.externalSource("foo", source);
    Node root = Node.parse(source);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, new IdentityNameIdSupplier());
    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    traversal.traverse();

    Node expectedVar = root.getFirstChild();
    Node actualVar = traversal.getScope().getVar("JSCompiler_object_inline_a_");

    assertEquals(expectedVar, actualVar);

    Node expectedA = expectedVar.getFirstChild();
    Node actualA = traversal.getScope().getVar("JSCompiler_object_inline_a_");

    assertEquals(expectedA, actualA);
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    String source = "function foo() {"
        + "  var objA = {a: {b: objB}};"
        + "  var objB = {b: objA};"
        + "}";

    Node externs = Node.externalSource("foo", source);
    Node root = Node.parse(source);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        compiler, new IdentityNameIdSupplier());
    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    traversal.traverse();

    Node expectedVarA = root.getFirstChild();
    Node actualVarA = traversal.getScope().getVar("JSCompiler_object_inline_a_");

    Node expectedVarB = expectedVarA.getNext();
    Node actualVarB = traversal.getScope().getVar("JSCompiler_object_inline_b_");

    assertEquals(expectedVarA, actualVarA);
    assertEquals(expectedVarB, actualVarB);

    Node expectedA = expectedVarA.getFirstChild();
    Node expectedBB = expectedVarB.getFirstChild();

    Node actualA = traversal.getScope().getVar("JSCompiler_object_inline_a_");
    Node actualBB = traversal.getScope().getVar("JSCompiler_object_inline_b_");

    Node expectedAB = expectedA.getNext();
    Node expectedB = expectedBB.getNext();

    Node actualAB = traversal.getScope().getVar("JSCompiler_object_inline_a_b_");
    Node actualB = traversal.getScope().getVar("JSCompiler_object_inline_b_");

    assertEquals(expectedA, actualA);
    assertEquals(expectedBB, actualBB);
    assertEquals(expectedAB, actualAB);
    assertEquals(expectedB, actualB);
  }

  private static class IdentityNameIdSupplier implements Supplier<String> {
    private int counter = 0;

    @Override
    public String get() {
      return "JSCompiler_object_inline_" + counter++;
    }
  }
}