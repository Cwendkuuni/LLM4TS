import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.List;

public class InlineObjectLiteralsTest {

  @org.junit.Test
  public void testInlineObjectLiteralWithSingleProperty() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function f() { var x = {a: 1}; }";
    Node rootNode = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(rootNode, null);
    t.parseAndTraverse(sourceCode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);
    inlineObjectLiterals.process(null, rootNode);

    Node expectedResult = new Node(Token.BLOCK, Lists.newArrayList(
        IR.var("x", NodeUtil.newUndefinedNode(null)),
        IR.assign(IR.name("x"), IR.literal(1)),
        IR.returnNode(null)
    ));

    assertEquals(expectedResult.toSource(), rootNode.toSource());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithMultipleProperties() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function f() { var x = {a: 1, b: 2}; }";
    Node rootNode = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(rootNode, null);
    t.parseAndTraverse(sourceCode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);
    inlineObjectLiterals.process(null, rootNode);

    Map<String, String> expectedVarMap = Maps.newLinkedHashMap();
    expectedVarMap.put("a", "JSCompiler_object_inline_a_");
    expectedVarMap.put("b", "JSCompiler_object_inline_b_");

    Node expectedResult = new Node(Token.BLOCK, Lists.newArrayList(
        IR.var("x", NodeUtil.newUndefinedNode(null)),
        IR.assign(IR.name("x.a"), IR.literal(1)),
        IR.assign(IR.name("x.b"), IR.literal(2)),
        IR.returnNode(null)
    ));

    assertEquals(expectedResult.toSource(), rootNode.toSource());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithNestedProperties() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function f() { var x = {a: {b: 1, c: 2}}; }";
    Node rootNode = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(rootNode, null);
    t.parseAndTraverse(sourceCode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);
    inlineObjectLiterals.process(null, rootNode);

    Map<String, String> expectedVarMap = Maps.newLinkedHashMap();
    expectedVarMap.put("a.b", "JSCompiler_object_inline_a_b_");
    expectedVarMap.put("a.c", "JSCompiler_object_inline_a_c_");

    Node expectedResult = new Node(Token.BLOCK, Lists.newArrayList(
        IR.var("x", NodeUtil.newUndefinedNode(null)),
        IR.assign(IR.name("x.a.b"), IR.literal(1)),
        IR.assign(IR.name("x.a.c"), IR.literal(2)),
        IR.returnNode(null)
    ));

    assertEquals(expectedResult.toSource(), rootNode.toSource());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithCircularReference() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function f() { var x = {a: {b: x}}; }";
    Node rootNode = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(rootNode, null);
    t.parseAndTraverse(sourceCode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);
    inlineObjectLiterals.process(null, rootNode);

    // This test case should fail as the InlineObjectLiterals Pass does
    // not support circular object references.
    assertThrows(RuntimeException.class, () -> {
      Node expectedResult = new Node(Token.BLOCK);
      assertEquals(expectedResult.toSource(), rootNode.toSource());
    });
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithEmptyObject() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function f() { var x = {}; }";
    Node rootNode = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(rootNode, null);
    t.parseAndTraverse(sourceCode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);
    inlineObjectLiterals.process(null, rootNode);

    Map<String, String> expectedVarMap = Maps.newLinkedHashMap();

    Node expectedResult = new Node(Token.BLOCK, Lists.newArrayList(
        IR.var("x", NodeUtil.newUndefinedNode(null)),
        IR.returnNode(null)
    ));

    assertEquals(expectedResult.toSource(), rootNode.toSource());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithUnassignedProperties() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    String sourceCode = "function f() { var x = {a: 1, b: 2, c: undefined}; }";
    Node rootNode = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(rootNode, null);
    t.parseAndTraverse(sourceCode);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, null);
    inlineObjectLiterals.process(null, rootNode);

    Map<String, String> expectedVarMap = Maps.newLinkedHashMap();
    expectedVarMap.put("a", "JSCompiler_object_inline_a_");
    expectedVarMap.put("b", "JSCompiler_object_inline_b_");

    Node expectedResult = new Node(Token.BLOCK, Lists.newArrayList(
        IR.var("x", NodeUtil.newUndefinedNode(null)),
        IR.assign(IR.name("x.a"), IR.literal(1)),
        IR.assign(IR.name("x.b"), IR.literal(2)),
        IR.returnNode(null)
    ));

    assertEquals(expectedResult.toSource(), rootNode.toSource());
  }
}