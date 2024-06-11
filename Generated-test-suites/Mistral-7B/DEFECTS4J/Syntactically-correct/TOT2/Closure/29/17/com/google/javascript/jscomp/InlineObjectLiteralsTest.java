import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {
  private Compiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testInlineObjectLiterals() {
    String source = "function Test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: function() { return 2; },\n" +
        "    c: { d: 3 },\n" +
        "    e: null,\n" +
        "    f: true,\n" +
        "    g: [1, 2],\n" +
        "  };\n" +
        "}\n";

    Node root = new Node(Token.DUMMY);
    Node externs = new Node(Token.DUMMY);

    compiler.compile(source, externs, root);

    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, new DummyNameIdSupplier());

    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    inlineObjectLiterals.getInliningBehavior().blacklistVarReferencesInTree(
        root, traversal.getScope());

    // Check that all object literals have been inlined
    for (Var v : traversal.getScope().getVars()) {
      if (v.getName().startsWith(VAR_PREFIX)) {
        Node value = v.getInitialValue();
        assertTrue(value.isObjectLit());
        for (Node c = value.getFirstChild(); c != null;
             c = c.getNext()) {
          if (c.isName()) {
            String key = c.getString();
            Node keyNode = traversal.getScope().getVar(key);
            assertEquals(v, keyNode.getParent());
          }
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String source = "function Test() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: obj\n" +
        "    },\n" +
        "  };\n" +
        "}\n";

    Node root = new Node(Token.DUMMY);
    Node externs = new Node(Token.DUMMY);

    compiler.compile(source, externs, root);

    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, new DummyNameIdSupplier());

    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    inlineObjectLiterals.getInliningBehavior().blacklistVarReferencesInTree(
        root, traversal.getScope());

    // Check that the object literal hasn't been inlined due to the self-reference
    for (Var v : traversal.getScope().getVars()) {
      if (v.getName().startsWith(VAR_PREFIX)) {
        Node value = v.getInitialValue();
        assertNotNull(value);
        assertFalse(value.isObjectLit());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithPropertyReference() {
    String source = "function Test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: obj.a\n" +
        "    },\n" +
        "  };\n" +
        "}\n";

    Node root = new Node(Token.DUMMY);
    Node externs = new Node(Token.DUMMY);

    compiler.compile(source, externs, root);

    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, new DummyNameIdSupplier());

    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    inlineObjectLiterals.getInliningBehavior().blacklistVarReferencesInTree(
        root, traversal.getScope());

    // Check that the object literal has been inlined but the reference to 'a' hasn't been changed
    for (Var v : traversal.getScope().getVars()) {
      if (v.getName().startsWith(VAR_PREFIX)) {
        Node value = v.getInitialValue();
        assertNotNull(value);
        assertFalse(value.isObjectLit());

        for (Node child = value.getFirstChild(); child != null;
             child = child.getNext()) {
          if (child.isName() && child.getString().equals("b")) {
            Node b = traversal.getScope().getVar("b");
            Node bValue = b.getInitialValue();
            assertNotNull(bValue);
            assertTrue(bValue.isObjectLit());
            Node a = bValue.getFirstChild();
            assertEquals(traversal.getScope().getVar("a"), a);
          }
        }
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicReference() {
    String source = "function Test() {\n" +
        "  var obj1 = {\n" +
        "    a: {\n" +
        "      b: obj1\n" +
        "    },\n" +
        "  };\n" +
        "  var obj2 = {\n" +
        "    a: obj1,\n" +
        "  };\n" +
        "}\n";

    Node root = new Node(Token.DUMMY);
    Node externs = new Node(Token.DUMMY);

    compiler.compile(source, externs, root);

    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, new DummyNameIdSupplier());

    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    inlineObjectLiterals.getInliningBehavior().blacklistVarReferencesInTree(
        root, traversal.getScope());

    // Check that the object literals haven't been inlined due to the cyclic reference
    for (Var v : traversal.getScope().getVars()) {
      if (v.getName().startsWith(VAR_PREFIX)) {
        Node value = v.getInitialValue();
        assertNotNull(value);
        assertFalse(value.isObjectLit());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithComplexObject() {
    String source = "function Test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: function() { return 2; },\n" +
        "    c: {\n" +
        "      d: {\n" +
        "        e: 3,\n" +
        "        f: function() { return 4; },\n" +
        "        g: {\n" +
        "          h: 5,\n" +
        "          i: null,\n" +
        "          j: true,\n" +
        "          k: [1, 2, 3],\n" +
        "        },\n" +
        "      },\n" +
        "      h: {\n" +
        "        i: 6,\n" +
        "      },\n" +
        "    },\n" +
        "  };\n" +
        "}\n";

    Node root = new Node(Token.DUMMY);
    Node externs = new Node(Token.DUMMY);

    compiler.compile(source, externs, root);

    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, new DummyNameIdSupplier());

    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root);
    inlineObjectLiterals.getInliningBehavior().blacklistVarReferencesInTree(
        root, traversal.getScope());

    // Check that all object literals have been inlined
    for (Var v : traversal.getScope().getVars()) {
      if (v.getName().startsWith(VAR_PREFIX)) {
        Node value = v.getInitialValue();
        assertTrue(value.isObjectLit());

        Map<String, Node> expectedMapping =
            ImmutableMap.<String, Node>builder()
            .put("h.i", traversal.getScope().getVar("h.i"))
            .put("h.j", traversal.getScope().getVar("h.j"))
            .put("g.k[0]", traversal.getScope().getVar("g.k[0]"))
            .put("g.k[1]", traversal.getScope().getVar("g.k[1]"))
            .build();

        for (Node child = value.getFirstChild(); child != null;
             child = child.getNext()) {
          if (child.isObjectLit()) {
            Node innerValue = child;
            Map<String, Node> innerMapping = Maps.newHashMap();

            for (Node grandChild = innerValue.getFirstChild();
                 grandChild != null;
                 grandChild = grandChild.getNext()) {
              if (grandChild.isName()) {
                String key = grandChild.getString();
                Node innerKey = traversal.getScope().getVar(key);
                innerMapping.put(key, innerKey);
                assertEquals(innerKey, grandChild);
              } else if (grandChild.isObjectLit()) {
                Map<String, Node> innerInnerMapping = Maps.newHashMap();
                innerMapping.putAll(inlineObjectLiterals.getInliningBehavior()
                    .computeVarList(traversal.getScope().getVar("g"),
                        traversal.getScope().getVariableInfoForNode(grandChild)));
                for (Map.Entry<String, Node> entry : innerInnerMapping.entrySet()) {
                  Node innerInnerNode = entry.getValue();
                  assertEquals(innerInnerNode, grandChild.getFirstChild());
                  assertEquals(expectedMapping.get(entry.getKey()),
                      innerInnerMapping.get(entry.getKey()));
                }
              }
            }
          }
        }
      }
    }
  }
}

class DummyNameIdSupplier implements Supplier<String> {
  private int counter = 0;

  @Override
  public String get() {
    return "dummy_" + (counter++);
  }
}