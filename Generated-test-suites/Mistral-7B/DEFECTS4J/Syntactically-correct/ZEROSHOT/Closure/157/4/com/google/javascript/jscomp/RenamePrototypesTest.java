import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  @Nullable
  private static CompilerInput createInput(String code) {
    return new CompilerInput(code, "test.js");
  }

  @Nullable
  private static Node createNode(AbstractCompiler compiler, CompilerInput input) {
    return compiler.parseTree(input, null);
  }

  /**
   * Tests that a simple script with no prototype properties or object literals
   * doesn't change the code.
   */
  @Test
  public void testSimple() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);
    CompilerInput input = createInput("function Foo() {}");

    Node root = createNode(compiler, input);
    pass.process(null, root);

    assertEquals(input.getSourceCode(), compiler.getSourceCode());
  }

  /**
   * Tests that a script with prototype properties and object literals is
   * correctly renamed.
   */
  @Test
  public void testRename() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerInput input = createInput(
        "function Foo() {}" +
        "Foo.prototype.oldProp = 42;" +
        "var o = { oldProp : 42 };");

    Node root = createNode(compiler, input);
    pass.process(null, root);

    Map<String, String> expected = Maps.newHashMap();
    expected.put("oldProp", "oldProp__");

    VariableMap actual = pass.getPropertyMap();
    assertEquals(expected, actual);

    assertEquals(input.getSourceCode(), compiler.getSourceCode());
  }

  /**
   * Tests that a script with conflicting prototype and object literal properties
   * behaves as expected.
   */
  @Test
  public void testConflict() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerInput input = createInput(
        "function Foo() {}" +
        "Foo.prototype.oldProp = 42;" +
        "var o = { oldProp : 42 };");

    Node root = createNode(compiler, input);
    pass.process(null, root);

    Map<String, String> expected = Maps.newHashMap();
    expected.put("oldProp", "oldProp__");

    VariableMap actual = pass.getPropertyMap();
    assertEquals(expected, actual);

    assertEquals(input.getSourceCode(), compiler.getSourceCode());
  }

  /**
   * Tests that a script with prototype properties that cannot be renamed as
   * they conflict with built-in property names behaves as expected.
   */
  @Test
  public void testExternals() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerInput input = createInput(
        "function Foo() {" +
        "  Foo.prototype.indexOf = Array.prototype.indexOf;" +
        "  Foo.prototype.toString = Object.prototype.toString;" +
        "}" +
        "var o = { toString : function() {} };");

    Node root = createNode(compiler, input);
    pass.process(null, root);

    Map<String, String> expected = Maps.newHashMap();
    // The renaming pass should not have touched the prototype properties.
    expected.putAll(Maps.newHashMap(
        "indexOf", "indexOf",
        "toString", "toString"));

    VariableMap actual = pass.getPropertyMap();
    assertEquals(expected, actual);

    assertEquals(input.getSourceCode(), compiler.getSourceCode());
  }
}