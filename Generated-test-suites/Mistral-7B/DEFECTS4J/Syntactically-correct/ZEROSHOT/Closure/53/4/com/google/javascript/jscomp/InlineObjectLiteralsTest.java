package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.AbstractCompiler.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.TestBase;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest extends TestBase {

  public static void main(String[] args) throws Exception {
    runTests(InlineObjectLiteralsTest.class);
  }

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(new AbstractCompiler(context), () -> "");
  }

  public void testInlineObjectLiterals() throws Exception {
    String js = ""
        + "function foo(obj) {\n"
        + "  obj.a = 1;\n"
        + "  obj.b = 2;\n"
        + "  obj.c = obj;\n"
        + "}\n"
        + "function bar(obj) {\n"
        + "  var x = obj.a;\n"
        + "  var y = obj.b;\n"
        + "  var z = obj.c;\n"
        + "  z.d = 4;\n"
        + "}\n"
        + "function baz() {\n"
        + "  var obj = {\n"
        + "    a: 1,\n"
        + "    b: 2,\n"
        + "    c: {\n"
        + "      d: 3\n"
        + "    }\n"
        + "  };\n"
        + "  bar(obj);\n"
        + "}\n";

    Node root = parse(js);
    CompilerContext ctxt = context.createCompilerContext("");
    getPass().process(ctxt.externs, root);

    Scope globalScope = root.getBody().getScope();
    ReferenceCollection refs = new ReferenceCollectingCallback(ctxt, null).process(
        ctxt.externs, root);

    ImmutableMap<String, Node> expected = ImmutableMap.<String, Node>builder()
        .put("obj.a", NodeUtil.newNumberLiteral(1))
        .put("obj.b", NodeUtil.newNumberLiteral(2))
        .put("obj.c", new Node(Token.VAR, Node.newString("obj_c_"), null))
        .put("obj.c.d", NodeUtil.newNumberLiteral(3))
        .put("obj", NodeUtil.newObjectLiteral(
            Lists.<Node>newArrayList(
                NodeUtil.newProperty(NodeUtil.newString("a"),
                    NodeUtil.newNumberLiteral(1)),
                NodeUtil.newProperty(NodeUtil.newString("b"),
                    NodeUtil.newNumberLiteral(2)),
                NodeUtil.newProperty(NodeUtil.newString("c"),
                    NodeUtil.newObjectLiteral(
                        Lists.<Node>newArrayList(
                            NodeUtil.newProperty(NodeUtil.newString("d"),
                                NodeUtil.newNumberLiteral(3))))))))
        .build();

    ImmutableSet<String> staleVars = ImmutableSet.of();
    for (Reference ref : refs.references) {
      Node node = ref.getNode();
      String varName = NodeUtil.getName(node);
      if (varName != null) {
        Node expectedNode = expected.get(varName);
        assertNotNull(expectedNode);
        assertEquals(expectedNode.toString(), node.toString());
      }
      if (ref.isStale()) {
        staleVars.add(varName);
      }
    }

    assertEquals(staleVars, ImmutableSet.of());
  }

  public void testInlineObjectLiteralsWithSelfReference() throws Exception {
    String js = ""
        + "function foo(obj) {\n"
        + "  obj.a = obj;\n"
        + "}\n";

    Node root = parse(js);
    CompilerContext ctxt = context.createCompilerContext("");
    getPass().process(ctxt.externs, root);

    Scope globalScope = root.getBody().getScope();
    ReferenceCollection refs = new ReferenceCollectingCallback(ctxt, null).process(
        ctxt.externs, root);

    assertEquals(refs.references.size(), 0);
  }

  public void testInlineObjectLiteralsWithNamedProperties() throws Exception {
    String js = ""
        + "function foo(obj) {\n"
        + "  obj.foo = {\n"
        + "    bar: 1,\n"
        + "    baz: 2\n"
        + "  };\n"
        + "}\n";

    Node root = parse(js);
    CompilerContext ctxt = context.createCompilerContext("");
    getPass().process(ctxt.externs, root);

    Scope globalScope = root.getBody().getScope();
    ReferenceCollection refs = new ReferenceCollectingCallback(ctxt, null).process(
        ctxt.externs, root);

    ImmutableMap<String, Node> expected = ImmutableMap.<String, Node>builder()
        .put("obj.foo.bar", NodeUtil.newNumberLiteral(1))
        .put("obj.foo.baz", NodeUtil.newNumberLiteral(2))
        .put("obj.foo", new Node(Token.VAR, Node.newString("obj_foo_"), null))
        .build();

    ImmutableSet<String> staleVars = ImmutableSet.of();
    for (Reference ref : refs.references) {
      Node node = ref.getNode();
      String varName = NodeUtil.getName(node);
      if (varName != null) {
        Node expectedNode = expected.get(varName);
        assertNotNull(expectedNode);
        assertEquals(expectedNode.toString(), node.toString());
      }
      if (ref.isStale()) {
        staleVars.add(varName);
      }
    }

    assertEquals(staleVars, ImmutableSet.of());
  }

  public void testInlineObjectLiteralsWithFunctionProperty() throws Exception {
    String js = ""
        + "function foo(obj) {\n"
        + "  obj.foo = function() {\n"
        + "  };\n"
        + "}\n";

    Node root = parse(js);
    CompilerContext ctxt = context.createCompilerContext("");
    getPass().process(ctxt.externs, root);

    Scope globalScope = root.getBody().getScope();
    ReferenceCollection refs = new ReferenceCollectingCallback(ctxt, null).process(
        ctxt.externs, root);

    ImmutableMap<String, Node> expected = ImmutableMap.<String, Node>builder()
        .put("obj.foo", new Node(Token.FUNCTION, null,
            NodeUtil.newFunctionSignature(
                NodeUtil.newList(NodeUtil.newList(), NodeUtil.newList(),
                    NodeUtil.newList()), NodeUtil.newList(), null),
            null))
        .build();

    ImmutableSet<String> staleVars = ImmutableSet.of();
    for (Reference ref : refs.references) {
      Node node = ref.getNode();
      String varName = NodeUtil.getName(node);
      if (varName != null) {
        Node expectedNode = expected.get(varName);
        assertNotNull(expectedNode);
        assertEquals(expectedNode.toString(), node.toString());
      }
      if (ref.isStale()) {
        staleVars.add(varName);
      }
    }

    assertEquals(staleVars, ImmutableSet.of());
  }

  public void testInlineObjectLiteralsWithDuplicateKeys() throws Exception {
    String js = ""
        + "function foo(obj) {\n"
        + "  obj.a = 1;\n"
        + "  obj.a = 2;\n"
        + "}\n";

    Node root = parse(js);
    CompilerContext ctxt = context.createCompilerContext("");
    getPass().process(ctxt.externs, root);

    Scope globalScope = root.getBody().getScope();
    ReferenceCollection refs = new ReferenceCollectingCallback(ctxt, null).process(
        ctxt.externs, root);

    ImmutableMap<String, Node> expected = ImmutableMap.<String, Node>builder()
        .put("obj.a", NodeUtil.newNumberLiteral(1))
        .build();

    ImmutableSet<String> staleVars = ImmutableSet.of();
    for (Reference ref : refs.references) {
      Node node = ref.getNode();
      String varName = NodeUtil.getName(node);
      if (varName != null) {
        Node expectedNode = expected.get(varName);
        assertNotNull(expectedNode);
        assertEquals(expectedNode.toString(), node.toString());
      }
      if (ref.isStale()) {
        staleVars.add(varName);
      }
    }

    assertEquals(staleVars, ImmutableSet.of());
  }
}