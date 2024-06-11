import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.UseSite;
import com.google.javascript.rhino.NodeUtil;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);

    Node root = Node.objectLiteral();
    Node prototype = Node.newObject("prototype");
    Node accumulateMethod = Node.function("accumulate", Node.EMPTY_LIST, Node.EMPTY_LIST,
        Node.block(Node.statement(Node.assignment(
            Node.getProp(prototype, "total"), Node.binaryOp("+=", Node.thisProperty(), Node.number(2))),
            Node.returnStatement(Node.getProp(thisProperty(), "total")))));
    Node object = Node.newObject("a", prototype);

    root.setJSType(Node.OBJECT_TYPE);
    prototype.setJSType(Node.OBJECT_TYPE);
    prototype.addChild(accumulateMethod);
    object.addChild(Node.getProp(prototype, "accumulate"));
    root.addChild(object);

    SimpleDefinitionFinder defFinder = mock(SimpleDefinitionFinder.class);
    when(defFinder.getDefinitionSites(any(Node.class))).thenReturn(Lists.newArrayList(
        new DefinitionSite(new Definition("accumulate", object, null), object, null, null)
    ));

    devirtualizer.process(Node.nullNode(), root);

    Node rewrittenMethod = findRewrittenMethod(root, "accumulate");
    Node expectedMethod = Node.functionWithThis("accumulate", Node.EMPTY_LIST, Node.EMPTY_LIST,
        Node.block(Node.statement(Node.assignment(Node.getProp(thisProperty(), "total"), Node.binaryOp("+=", thisProperty(), Node.number(2))),
            Node.returnStatement(Node.getProp(thisProperty(), "total")))));

    assertSame(expectedMethod, rewrittenMethod);
  }

  private Node findRewrittenMethod(Node root, String name) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child instanceof Node && child.getJSType() instanceof ObjectType) {
        Node method = findMethod((Node) child, name);
        if (method != null) {
          return method;
        }
      }
    }
    return null;
  }

  private Node findMethod(Node node, String name) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child instanceof Node && child.getType() == Token.FUNCTION) {
        Node nameNode = child.getFirstChild();
        if (nameNode != null && nameNode.getString().equals(name)) {
          return child;
        }
      } else if (child instanceof Node && child.getType() == Token.OBJECT) {
        Node prototype = child.getFirstChild();
        if (prototype != null) {
          Node method = findMethod(prototype, name);
          if (method != null) {
            return method;
          }
        }
      }
    }
    return null;
  }

  private static final class DefinitionSite {
    private final Definition definition;
    private final Node node;
    private final Node module;

    DefinitionSite(Definition definition, Node node, Node module, Object unused) {
      this.definition = definition;
      this.node = node;
      this.module = module;
    }
  }

  private static class Definition {
    private final String name;
    private final Node definitionNode;
    private final Node module;

    Definition(String name, Node definitionNode, Node module) {
      this.name = name;
      this.definitionNode = definitionNode;
      this.module = module;
    }
  }
}