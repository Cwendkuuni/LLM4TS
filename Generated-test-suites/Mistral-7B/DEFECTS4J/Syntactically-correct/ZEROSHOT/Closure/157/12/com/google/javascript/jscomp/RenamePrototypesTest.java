import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {};";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    assertEquals("bar", variableMap.lookupOldName("prototype.bar"));
    assertNull(variableMap.lookupOldName("prototype.baz"));
  }

  @Test
  public void testDontRenameExportedProperties() {
    String sourceCode = "var exports = {}; exports.bar = function() {}; function Foo() {} Foo.prototype.bar = exports.bar;";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    assertEquals("bar", variableMap.lookupOldName("exports.bar"));
    assertEquals("bar", variableMap.lookupOldName("prototype.bar"));
  }

  @Test
  public void testDontRenamePrivateProperties() {
    String sourceCode = "function Foo() { var _bar = function() {}; } Foo.prototype._bar = _bar;";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    assertEquals("_bar", variableMap.lookupOldName("prototype._bar"));
  }

  @Test
  public void testDontRenameObjLitProperties() {
    String sourceCode = "var obj = { foo: function() {}, bar: function() {} };";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    assertEquals("foo", variableMap.lookupOldName("obj.foo"));
    assertEquals("bar", variableMap.lookupOldName("obj.bar"));
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedNames() {
    String sourceCode = "function Foo() {} Foo.prototype.indexOf = function() {}; Foo.prototype.toString = function() {}";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    assertEquals("indexOf", variableMap.lookupOldName("prototype.indexOf"));
    assertEquals("toString", variableMap.lookupOldName("prototype.toString"));
    assertNull(variableMap.lookupOldName("prototype.length"));
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedCharacters() {
    char[] reservedCharacters = {'$'};
    String sourceCode = "function Foo() {} Foo.prototype.$bar = function() {};";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, reservedCharacters, null);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    // '$' is a reserved character, so the property should be renamed.
    assertEquals("$bar", variableMap.lookupOldName("prototype.$bar"));
  }

  @Test
  public void testRenamePrototypePropertiesWithPrevUsedRenameMap() {
    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {};";
    AbstractCompiler compiler = new AbstractCompiler();
    VariableMap prevUsedRenameMap = new VariableMap();
    prevUsedRenameMap.put("prototype.bar", "baz");
    CompilerPass pass = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    Node root = new TokenStream(new ByteArrayInputStream(sourceCode.getBytes())).parse();
    pass.process(null, root);

    VariableMap variableMap = pass.getPropertyMap();
    assertEquals("baz", variableMap.lookupOldName("prototype.bar"));
  }
}