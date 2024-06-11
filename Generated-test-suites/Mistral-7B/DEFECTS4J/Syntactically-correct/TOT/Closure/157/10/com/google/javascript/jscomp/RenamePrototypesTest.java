import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOption.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class RenamePrototypesTest {

  @SuppressWarnings("unchecked")
  private static <T> T parseAndCompile(String jsCode, CompilerOptions options)
      throws IOException {
    TokenStream input = new TokenStream(jsCode);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    RenamePrototypes renamePrototypes =
        new RenamePrototypes(compiler, false, null, null);
    CompilerPass[] passes = {renamePrototypes};
    compiler.addPasses(passes);

    Node rootNode = compiler.parseInitialScriptContainingSourceInfo(input, "test");
    compiler.process(rootNode, null);

    return (T) renamePrototypes;
  }

  @Test
  public void testRenamePrototypeProperty() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "function Foo() {} Foo.prototype.bar = function() {};", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("bar");

    assertEquals("Should rename prototype property 'bar'", newName, "baz");
  }

  @Test
  public void testDontRenamePrototypePropertyWithLeadingUnderscore() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "function Foo() {} Foo.prototype._bar = function() {};", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("_bar");

    assertEquals("Should not rename prototype property '_bar'", newName, "_bar");
  }

  @Test
  public void testRenameObjLitProperty() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "var obj = { bar: function() {} };", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("bar");

    assertEquals("Should rename object literal property 'bar'", newName, "baz");
  }

  @Test
  public void testDontRenameObjLitPropertyWithLeadingUnderscore() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "var obj = { _bar: function() {} };", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("_bar");

    assertEquals("Should not rename object literal property '_bar'", newName, "_bar");
  }

  @Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    CompilerOptions options = new CompilerOptions();
    options.setOption(aggressiveRenaming, true);

    RenamePrototypes renamePrototypes = parseAndCompile(
        "function Foo() {} Foo.prototype.bar = function() {};", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("bar");

    assertNull("Should rename prototype property 'bar' aggressively", newName);
  }

  @Test
  public void testRenamePrototypePropertyWithSameNameAsBuiltInFunction() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "function Foo() {} Foo.prototype.index = function() {};", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("index");

    assertNull("Should not rename prototype property 'index' to a built-in function name", newName);
  }

  @Test
  public void testRenamePrototypePropertyWithUpperCaseLetter() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "function Foo() {} Foo.prototype.Baz = function() {};", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("Baz");

    assertNotNull("Should rename prototype property 'Baz'", newName);
  }

  @Test
  public void testRenameObjLitPropertyWithQuotedName() {
    CompilerOptions options = new CompilerOptions();
    RenamePrototypes renamePrototypes = parseAndCompile(
        "var obj = { \"bar\": function() {} };", options);

    VariableMap renameMap = renamePrototypes.getPropertyMap();
    String newName = renameMap.lookupNewName("bar");

    assertNull("Should not rename object literal property 'bar' with quoted name", newName);
  }

  @Test
  public void testRenamePrototypePropertyWithSameNameInPrototypeAndObjLit() {
    CompilerOptions options = new CompilerOptions();
    String jsCode =
        "function Foo() {} Foo.prototype.bar = function() {}; var obj = { bar: function() {} };";

    RenamePrototypes renamePrototypes = null;
    VariableMap renameMap = null;

    try {
      renamePrototypes = parseAndCompile(jsCode, options);
      renameMap = renamePrototypes.getPropertyMap();
    } catch (IOException e) {
      fail("Compilation failed: " + e.getMessage());
    }

    String newName = renameMap.lookupNewName("bar");

    assertNull("Should not rename prototype property 'bar' with the same name as an object literal property", newName);
  }

  @Test
  public void testRenamePrototypePropertyWithSameNameAsPrototypeProperty() {
    CompilerOptions options = new CompilerOptions();
    String jsCode =
        "function Foo() {} Foo.prototype.bar = function() {}; function Foo2() {} Foo2.prototype.bar = function() {};";

    RenamePrototypes renamePrototypes = null;
    VariableMap renameMap = null;

    try {
      renamePrototypes = parseAndCompile(jsCode, options);
      renameMap = renamePrototypes.getPropertyMap();
    } catch (IOException e) {
      fail("Compilation failed: " + e.getMessage());
    }

    String newName = renameMap.lookupNewName("bar");

    assertNotNull("Should rename the second prototype property 'bar'", newName);
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedCharacters() {
    CompilerOptions options = new CompilerOptions();
    options.setOption(charAtPos, 0, '!');

    String jsCode = "function Foo() {} Foo.prototype.! = function() {};";

    RenamePrototypes renamePrototypes = null;
    VariableMap renameMap = null;

    try {
      renamePrototypes = parseAndCompile(jsCode, options);
      renameMap = renamePrototypes.getPropertyMap();
    } catch (IOException e) {
      fail("Compilation failed: " + e.getMessage());
    }

    String newName = renameMap.lookupNewName("!");

    assertNull("Should not rename prototype property '!' with a reserved character", newName);
  }
}