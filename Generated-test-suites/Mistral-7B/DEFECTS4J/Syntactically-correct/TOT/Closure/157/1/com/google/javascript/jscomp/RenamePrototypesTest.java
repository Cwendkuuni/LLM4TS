import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOption.aggressiveRenaming;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOption.allowAllExterns;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOption.forbidPropertyRenaming;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOption.processExternalResidues;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.util.Map;

public class RenamePrototypesTest extends CompilerTestBase {

  public void testRenamePrototypeProperties() throws Exception {
    String input = "function Foo() {} Foo.prototype.bar = function() { this.baz = 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, true);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), true, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype._bar = function() { this._baz = 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenameObjLitProperties() throws Exception {
    String input = "var obj = { foo: 1 }";;

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, false);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), false, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "var obj = { foo_ : 1 }";;

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithExportedNames() throws Exception {
    String input = "function Foo() {} Foo.prototype.Math = Math;";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, true);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), true, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype.Math = Math;";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithPrivateNames() throws Exception {
    String input = "function Foo() {} Foo.prototype._bar = function() { this._baz = 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, true);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), true, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype._bar = function() { this._baz_ = 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithBuiltInNames() throws Exception {
    String input = "function Foo() {} Foo.prototype.indexOf = function() { return 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, false);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), false, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype.indexOf = function() { return 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithMixedNames() throws Exception {
    String input = "function Foo() {} Foo.prototype._foo = function() { this.bar = 1; }; Foo.prototype.bar = function() { this._baz = 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, true);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), true, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype._foo = function() { this._bar_ = 1; }; Foo.prototype.bar = function() { this._baz__ = 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithMultipleInstances() throws Exception {
    String input = "function Foo() {} Foo.prototype.bar = function() { this.baz = 1; }; function Bar() { Foo.call(this); } Bar.prototype.bar = function() { this.baz = 2; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, true);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), true, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype._bar = function() { this._baz_ = 1; }; function Bar() { Foo.call(this); } Bar.prototype._bar = function() { this._baz__ = 2; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithMixedNamesAndMultipleInstances() throws Exception {
    String input = "function Foo() {} Foo.prototype._foo = function() { this.bar = 1; }; Foo.prototype.bar = function() { this._baz = 1; }; function Bar() { Foo.call(this); } Bar.prototype._foo = function() { this.bar = 2; }; Bar.prototype.bar = function() { this._baz__ = 2; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, true);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), true, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype._foo = function() { this._bar__ = 1; }; Foo.prototype._bar = function() { this._baz_ = 1; }; function Bar() { Foo.call(this); } Bar.prototype._foo = function() { this._bar___ = 2; }; Bar.prototype._bar = function() { this._baz___ = 2; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithReservedChars() throws Exception {
    String input = "function Foo() {} Foo.prototype.foo$bar = function() { this.baz = 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, false);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);
    options.setOption(allowAllExterns, true);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), false, "\\$".toCharArray(), null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype.foo$bar = function() { this.foo$bar_ = 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithReservedNames() throws Exception {
    String input = "function Foo() {} Foo.prototype.indexOf = function() { return 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, false);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), false, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype.indexOf = function() { return 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithMixedReservedNames() throws Exception {
    String input = "function Foo() {} Foo.prototype.indexOf = function() { return 1; }; Foo.prototype.foo_bar = function() { this.baz = 1; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, false);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), false, null, null);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype.indexOf = function() { return 1; }; Foo.prototype.foo__bar = function() { this.baz = 1; };";

    assertEquals(expectedOutput, output);
  }

  public void testRenamePrototypePropertiesWithPrevUsedNames() throws Exception {
    String input = "function Foo() {} Foo.prototype.bar = function() { this.baz = 1; }; function Bar() { Foo.call(this); } Bar.prototype.bar = function() { this.baz = 2; };";

    CompilerOptions options = new CompilerOptions(compiler());
    options.setOption(aggressiveRenaming, false);
    options.setOption(processExternalResidues, true);
    options.setOption(forbidPropertyRenaming, false);

    CompilerInput inputNode = parseAndCompile(input, options);

    VariableMap prevUsedRenameMap = Maps.newHashMap();
    prevUsedRenameMap.put("bar", "bar_");

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler(), false, null, prevUsedRenameMap);
    CompilerResult result = renamePrototypes.process(inputNode, inputNode);

    String output = getSource(result);
    String expectedOutput = "function Foo() {} Foo.prototype.bar_ = function() { this.baz = 1; }; function Bar() { Foo.call(this); } Bar.prototype.bar__ = function() { this.baz = 2; };";

    assertEquals(expectedOutput, output);
  }
}