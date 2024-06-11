import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for the RenamePrototypes class
 */
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CharSequence sourceCode = "function Foo() {} \nFoo.prototype.x = 1; \nFoo.prototype.y = 2;";
    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x", "x_1_");
    expectedMap.put("y", "y_1_");

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected number of properties", 2, actualMap.size());
    assertEquals("Unexpected property mapping", expectedMap, actualMap);
  }

  @Test
  public void testNoRenamePrototypeProperties() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CharSequence sourceCode = "function Foo() {} \nFoo.prototype.x = 1; \nFoo.prototype.foo = 2;";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected number of properties", 1, actualMap.size());
    assertEquals("Unexpected property mapping", "foo", actualMap.lookup("foo"));
  }

  @Test
  public void testAggressiveRenamePrototypeProperties() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CharSequence sourceCode = "function Foo() {} \nFoo.prototype.x = 1; \nFoo.prototype.y = 2;";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x", "_x_1_");
    expectedMap.put("y", "_y_1_");

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected number of properties", 2, actualMap.size());
    assertEquals("Unexpected property mapping", expectedMap, actualMap);
  }

  @Test
  public void testRenameObjLitProperties() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CharSequence sourceCode = "var o = { x: 1 };";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x", "_x_1_");

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected number of properties", 1, actualMap.size());
    assertEquals("Unexpected property mapping", expectedMap, actualMap);
  }

  @Test
  public void testNoRenameObjLitProperties() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "var o = { x: 1, foo: 2 };";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected number of properties", 2, actualMap.size());
    assertEquals("Unexpected property mapping", "foo", actualMap.lookup("foo"));
  }

  @Test
  public void testAggressiveRenameObjLitProperties() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "var o = { x: 1 };";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x", "_x_1_");

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected number of properties", 1, actualMap.size());
    assertEquals("Unexpected property mapping", expectedMap, actualMap);
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedNames() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.indexOf = 1; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertNull("Unexpected property mapping", actualMap.lookup("indexOf"));
  }

  @Test
  public void testNoRenamePrototypePropertiesWithReservedNames() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.toString = 1; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected property mapping", "toString", actualMap.lookup("toString"));
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedNamesAggressive() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.indexOf = 1; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertNull("Unexpected property mapping", actualMap.lookup("indexOf"));
  }

  @Test
  public void testProcessPrototypeAssignment() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { x: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertNull("Unexpected property mapping", actualMap.lookup("prototype"));
  }

  @Test
  public void testProcessPrototypeObjectLitAssignment() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { x: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    Node prototypeObjLit = findPrototypeObjLit(root, "prototype");
    assertNotNull("Unexpected null prototype object literal", prototypeObjLit);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x", "_x_1_");

    Map<String, String> actualProperties = getProperties(prototypeObjLit);
    assertEquals("Unexpected number of properties", 1, actualProperties.size());
    assertEquals("Unexpected property mapping", expectedMap, actualProperties);
  }

  @Test
  public void testProcessPrototypeObjectLitAssignmentAggressive() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { x: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Node prototypeObjLit = findPrototypeObjLit(root, "prototype");
    assertNotNull("Unexpected null prototype object literal", prototypeObjLit);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x", "_x_1_");

    Map<String, String> actualProperties = getProperties(prototypeObjLit);
    assertEquals("Unexpected number of properties", 1, actualProperties.size());
    assertEquals("Unexpected property mapping", expectedMap, actualProperties);
  }

  @Test
  public void testProcessPrototypeObjectLitAssignmentWithSameName() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { x: 1, x: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    Node prototypeObjLit = findPrototypeObjLit(root, "prototype");
    assertNotNull("Unexpected null prototype object literal", prototypeObjLit);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x_1_", "_x_2_");

    Map<String, String> actualProperties = getProperties(prototypeObjLit);
    assertEquals("Unexpected number of properties", 1, actualProperties.size());
    assertEquals("Unexpected property mapping", expectedMap, actualProperties);
  }

  @Test
  public void testProcessPrototypeObjectLitAssignmentWithSameNameAggressive() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { x: 1, x: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Node prototypeObjLit = findPrototypeObjLit(root, "prototype");
    assertNotNull("Unexpected null prototype object literal", prototypeObjLit);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("x_1_", "_x_2_");

    Map<String, String> actualProperties = getProperties(prototypeObjLit);
    assertEquals("Unexpected number of properties", 1, actualProperties.size());
    assertEquals("Unexpected property mapping", expectedMap, actualProperties);
  }

  @Test
  public void testProcessPrototypeObjectLitAssignmentWithExternalName() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { toString: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected property mapping", "toString", actualMap.lookup("toString"));
  }

  @Test
  public void testProcessPrototypeObjectLitAssignmentWithExternalNameAggressive() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Charsequence sourceCode = "function Foo() { this.prototype = { toString: 1 }; }";

    Node root = new TokenStream(sourceCode).parseProgram();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    VariableMap actualMap = renamePrototypes.getPropertyMap();
    assertEquals("Unexpected property mapping", "toString", actualMap.lookup("toString"));
  }

  private Node findPrototypeObjLit(Node root, String propertyName) {
    NodeTraversal traversal = new NodeTraversal(root) {};

    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.GETPROP) {
        if (currentNode.getFirstChild().getNodeValue().equals(propertyName)) {
          if (currentNode.getLastChild().getType() == Token.OBJECTLIT) {
            return currentNode.getLastChild();
          }
        }
      }

      currentNode = traversal.getParentOrNull();
    }

    return null;
  }

  private Map<String, String> getProperties(Node node) {
    Map<String, String> properties = new HashMap<>();

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.STRING) {
        String key = child.getString();
        if (node.getLastChild().getType() == Token.ASSIGN) {
          properties.put(key, getValueAsString(node.getLastChild().getFirstChild()));
        }
      }
    }

    return properties;
  }

  private String getValueAsString(Node node) {
    if (node.getType() == Token.STRING) {
      return node.getString();
    } else if (node.getType() == Token.NUMBER) {
      return node.getIntValue() + "";
    } else if (node.getType() == Token.NAME) {
      return node.getName();
    }

    throw new IllegalStateException("Node is not a string, number or name");
  }
}