import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerOptions.CodingConvention;
import com.google.javascript.rhino.TokenStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private TokenStream input;
  private Node root;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setCodingConvention(CodingConvention.STRICT);
    compiler = new Compiler(options);

    input = new TokenStream(
      "function Foo() {}\n" +
      "Foo.prototype.bar = function() {};\n" +
      "Foo.prototype = {'getBar': function() {}};");
    root = compiler.parse(input, null, null);
  }

  @Test
  public void testNoRenaming() {
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Map<String, String> map = pass.getPropertyMap();
    assertEquals(2, map.size());
    assertEquals("bar", map.get("bar"));
    assertEquals("getBar", map.get("getBar"));

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }

  @Test
  public void testRenaming() {
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    Map<String, String> map = pass.getPropertyMap();
    assertEquals(2, map.size());
    Map<String, String> expected = Maps.newHashMap();
    expected.put("_bar", "bar");
    expected.put("_getBar", "getBar");

    assertEquals(expected, map);

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }

  @Test
  public void testReservedNames() {
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Map<String, String> map = pass.getPropertyMap();
    assertEquals(2, map.size());
    assertEquals("bar", map.get("bar"));
    assertEquals("getBar", map.get("getBar"));

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }

  @Test(expected = AssertionError.class)
  public void testAggressiveRenamingIndexOf() {
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }

  @Test(expected = AssertionError.class)
  public void testAggressiveRenamingToString() {
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }

  @Test(expected = AssertionError.class)
  public void testAggressiveRenamingValueOf() {
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }

  @Test
  public void testReusedNames() {
    RenamePrototypes prevPass = new RenamePrototypes(compiler, false, null, null);
    prevPass.process(null, root);

    Map<String, String> prevMap = prevPass.getPropertyMap();
    assertEquals(2, prevMap.size());
    assertEquals("_bar", prevMap.get("bar"));
    assertEquals("_getBar", prevMap.get("getBar"));

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, prevMap);
    pass.process(null, root);

    Map<String, String> map = pass.getPropertyMap();
    assertEquals(2, map.size());
    Map<String, String> expected = Maps.newHashMap();
    expected.put("_bar", "_bar");
    expected.put("_getBar", "_getBar");

    assertEquals(expected, map);

    NodeTraversal.traverse(compiler, root, new CheckPropertyNames());
  }
}

class CheckPropertyNames extends AbstractPostOrderCallback {
  private int count = 0;

  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.GETPROP && n.getFirstChild().getType() == Token.STRING) {
      Node str = n.getFirstChild().getNext();
      String name = str.getString();
      if (name.equals("prototype")) {
        // Skip prototype properties
        return;
      }

      Property prop = getProperty(name);
      String expected = prop.newName;
      String actual = str.getString();
      assertEquals("Unexpected name for property: " + name, expected, actual);
      count++;
    }
  }

  private Property getProperty(String name) {
    for (Map.Entry<String, Property> entry : RenamePrototypes.properties.entrySet()) {
      if (entry.getKey().equals(name)) {
        return entry.getValue();
      }
    }
    fail("Unknown property: " + name);
    return null;
  }
}