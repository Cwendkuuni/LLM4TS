import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.TokenStream;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    String input =
        "function Foo() {\n" +
        "  this.prop = 1;\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.getProp = function() {\n" +
        "  return this.prop;\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    TokenStream source = new TokenStream(new ByteArrayInputStream(input.getBytes()));
    renamePrototypes.process(source.getRoot(), source.getRoot());

    Node output = compiler.getIntermediateRepresentation();

    // Check that the property 'prop' was renamed
    Property propProp = renamePrototypes.properties.get("prop");
    assertTrue(propProp != null);
    assertNotNull(propProp.newName);
    assertEquals("prop_", propProp.newName);

    // Check that the getter method 'getProp' was not renamed
    Property getProp = renamePrototypes.properties.get("getProp");
    assertTrue(getProp != null);
    assertNull(getProp.newName);

    String outputCode = compiler.getProgramText();
    // Check that the renamed property 'prop_' is used correctly in the getter method
    assertTrue(outputCode.contains("this.prop_"));
  }

  @Test
  public void testDontRenamePrototypePropertiesWithLeadingUnderscore() {
    String input =
        "function Foo() {\n" +
        "  this._prop = 1;\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.getProp = function() {\n" +
        "  return this._prop;\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    TokenStream source = new TokenStream(new ByteArrayInputStream(input.getBytes()));
    renamePrototypes.process(source.getRoot(), source.getRoot());

    Node output = compiler.getIntermediateRepresentation();

    // Check that the property '_prop' was not renamed
    Property propProp = renamePrototypes.properties.get("_prop");
    assertTrue(propProp != null);
    assertNull(propProp.newName);

    String outputCode = compiler.getProgramText();
    // Check that the unmolested property '_prop' is used correctly in the getter method
    assertTrue(outputCode.contains("this._prop"));
  }

  @Test
  public void testRenameObjLitProperties() {
    String input =
        "var obj = {\n" +
        "  prop: 1,\n" +
        "  getProp: function() {\n" +
        "    return this.prop;\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    TokenStream source = new TokenStream(new ByteArrayInputStream(input.getBytes()));
    renamePrototypes.process(source.getRoot(), source.getRoot());

    Node output = compiler.getIntermediateRepresentation();

    // Check that the property 'prop' was renamed
    Property propProp = renamePrototypes.properties.get("prop");
    assertTrue(propProp != null);
    assertNotNull(propProp.newName);
    assertEquals("prop_", propProp.newName);

    // Check that the getter method 'getProp' was not renamed
    Property getProp = renamePrototypes.properties.get("getProp");
    assertTrue(getProp != null);
    assertNull(getProp.newName);

    String outputCode = compiler.getProgramText();
    // Check that the renamed property 'prop_' is used correctly in the getter method
    assertTrue(outputCode.contains("this.prop_"));
  }

  @Test
  public void testAggressiveRenaming() {
    String input =
        "function Foo() {\n" +
        "  this.getName = function() {\n" +
        "    return 'bar';\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    TokenStream source = new TokenStream(new ByteArrayInputStream(input.getBytes()));
    renamePrototypes.process(source.getRoot(), source.getRoot());

    Node output = compiler.getIntermediateRepresentation();

    // Check that the method 'getName' was renamed
    Property getNameProp = renamePrototypes.properties.get("getName");
    assertTrue(getNameProp != null);
    assertNotNull(getNameProp.newName);
    assertEquals("getName_", getNameProp.newName);

    String outputCode = compiler.getProgramText();
    // Check that the renamed method 'getName_' is used correctly in the getter method
    assertTrue(outputCode.contains("this.getName_"));
  }

  @Test(expected = AssertionError.class)
  public void testDontRenameExternedProperties() {
    String input =
        "function Foo() {\n" +
        "  this.IndexOf = Array.prototype.indexOf;\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler();

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    TokenStream source = new TokenStream(new ByteArrayInputStream(input.getBytes()));
    renamePrototypes.process(source.getRoot(), source.getRoot());

    Node output = compiler.getIntermediateRepresentation();

    // Check that the externed property 'IndexOf' was not renamed
    Property indexOfProp = renamePrototypes.properties.get("IndexOf");
    assertTrue(indexOfProp != null);
    assertNull(indexOfProp.newName);
  }
}