import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @org.junit.Test
  public void testInlineObjectLiterals() {
    // Set up the test compiler
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, null);

    // Create a sample NodeTree for testing
    Node externs = null;
    Node root = createSampleNodeTree();

    // Run the test
    inlineObjectLiterals.process(externs, root);

    // Verify the output
    assertObjectInline(root, "obj1", "name1", "value1");
    assertObjectInline(root, "obj2", "name2", "value2");
  }

  private Node createSampleNodeTree() {
    Node root = new Node(Token.SCRIPTLET);

    Node obj1 = new Node(Token.OBJECT_LITERAL);
    root.addChildToFront(obj1);

    Node name1 = new Node(Token.STRING, "name1");
    new Node(Token.PROPS, name1, new Node(Token.STRING, "value1")).addChildToFront(obj1);

    Node obj2 = new Node(Token.OBJECT_LITERAL);
    root.addChildToFront(obj2);

    new Node(Token.PROPS, new Node(Token.STRING, "name2"), new Node(Token.STRING, "value2")).addChildToFront(obj2);

    Node assignment = new Node(Token.ASSIGN,
        new Node(Token.NAME, "obj1"),
        new Node(Token.OBJECT_LITERAL,
          new Node(Token.STRING, "name3"),
          new Node(Token.STRING, "value3")
        )
    );
    root.addChildToFront(assignment);

    return root;
  }

  private void assertObjectInline(Node root, String objName, String keyName, String value) {
    Node obj = findObjectByName(root, objName);
    assertNotNull(obj);

    Node key = findKeyByName(obj, keyName);
    assertNotNull(key);

    Node valueNode = findValueByName(obj, keyName);
    assertNotNull(valueNode);
    assertEquals(value, valueNode.getString());

    Node variable = findVariableByName(root, VAR_PREFIX + keyName);
    assertNotNull(variable);
    assertEquals(value, variable.getFirstChild().getString());
  }

  private Node findObjectByName(Node root, String name) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.isObjectLit()) {
        String objName = child.getFirstChild().getString();
        if (objName.equals(name)) {
          return child;
        }
      }
    }

    return null;
  }

  private Node findKeyByName(Node obj, String name) {
    for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isName()) {
        String keyName = child.getString();
        if (keyName.equals(name)) {
          return child.getNext();
        }
      } else if (child.isObjectLit()) {
        Node key = findKeyByName(child, name);
        if (key != null) {
          return key;
        }
      }
    }

    return null;
  }

  private Node findValueByName(Node obj, String name) {
    for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetProp() && child.getLastChild().isName() && child.getLastChild().getString().equals(name)) {
        return child.getFirstChild();
      }
      if (child.isObjectLit()) {
        Node value = findValueByName(child, name);
        if (value != null) {
          return value;
        }
      }
    }

    return null;
  }

  private Node findVariableByName(Node root, String name) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.isVar()) {
        String varName = child.getString();
        if (varName.startsWith(VAR_PREFIX) && varName.length() > VAR_PREFIX.length()
            && varName.substring(VAR_PREFIX.length()).equals(name)) {
          return child;
        }
      }
    }

    return null;
  }
}