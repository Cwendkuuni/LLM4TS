import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerInput.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerResult.Type;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.RenamePrototypes.Property;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenType;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() throws IOException {
    String input = "function Foo() { } Foo.prototype.foo = function() { }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    Map<String, String> prevMap = Maps.newHashMap();

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.ERROR, result.getType());
    assertEquals(1, result.getDiagnostics().size());

    String expectedError =
        "ERROR: Renaming 'prototype' property 'foo' of constructor 'Foo' is not allowed";
    assertEquals(expectedError, result.getDiagnostics().get(0).getMessage());

    input = "function Foo() { } Foo.prototype._foo = function() { }";

    result = compiler.compile(pass, Arrays.asList(parseCompilationUnit(input, "input.js")), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("_foo");
    assertEquals("_foo_", foo.newName);
  }

  @Test
  public void testDontRenamePrototypePropertiesWithLeadingUnderscore()
      throws IOException {
    String input = "function Foo() { } Foo.prototype._foo = function() { }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("_foo");
    assertEquals("_foo", foo.newName);
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming()
      throws IOException {
    String input = "function Foo() { } Foo.prototype.bar = function() { }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property bar = getProperty("bar");
    assertEquals("bar_", bar.newName);
  }

  @Test
  public void testDontRenamePrototypePropertiesWithoutTrailingUnderscore()
      throws IOException {
    String input = "function Foo() { } Foo.prototype.bar = function() { }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property bar = getProperty("bar");
    assertNull(bar.newName);
  }

  @Test
  public void testRenameObjLitProperties() throws IOException {
    String input = "var obj = { foo: 1 };";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("foo");
    assertNull(foo.newName);
  }

  @Test
  public void testDontRenameObjLitPropertiesWithQuotes() throws IOException {
    String input = "var obj = { 'foo': 1 };";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("foo");
    assertNull(foo.newName);
  }

  @Test
  public void testRenameObjLitPropertiesWithLeadingUnderscore() throws IOException {
    String input = "var obj = { '_foo': 1 };";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("_foo");
    assertNull(foo.newName);
  }

  @Test
  public void testRenameObjLitPropertiesWithTrailingUnderscore() throws IOException {
    String input = "var obj = { foo_: 1 };";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("foo_");
    assertNull(foo.newName);
  }

  @Test
  public void testDontRenameObjLitPropertiesWithLeadingOrTrailingUnderscores()
      throws IOException {
    String input = "var obj = { '_foo_': 1 };";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property foo = getProperty("_foo_");
    assertNull(foo.newName);
  }

  @Test
  public void testRenamePrototypePropertiesWithReservedNames() throws IOException {
    String input = "function Foo() { } Foo.prototype.indexOf = function() { }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.ERROR, result.getType());
    assertEquals(1, result.getDiagnostics().size());

    String expectedError = "ERROR: Renaming property 'prototype.indexOf' is not allowed";
    assertEquals(expectedError, result.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testRenamePrototypePropertiesWithExternedNames() throws IOException {
    String input = "function Foo() { } Foo.prototype.indexOf = Array.prototype.indexOf;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property indexOf = getProperty("indexOf");
    assertEquals("indexOf", indexOf.oldName);
    assertEquals(null, indexOf.newName);
  }

  @Test
  public void testRenamePrototypePropertiesWithExternedNamesAndAggressiveRenaming()
      throws IOException {
    String input = "function Foo() { } Foo.prototype.indexOf = Array.prototype.indexOf;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput inputNode = parseCompilationUnit(input, "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), options);

    assertEquals(Type.NORMAL, result.getType());
    assertNull(result.getDiagnostics());

    Property indexOf = getProperty("indexOf");
    assertEquals("indexOf", indexOf.oldName);
    assertEquals(null, indexOf.newName);
  }

  private static Node parseCompilationUnit(String code, String fileName) {
    TestSourceFile sourceFile = new TestSourceFile(fileName);
    sourceFile.setCode(code);

    TokenStream ts = new TokenStream(code.toCharArray(), 0, code.length());
    AbstractCompiler.parseAndReturnRootNode(sourceFile, ts);
    return sourceFile.getRootNode();
  }

  private Property getProperty(String name) {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput inputNode = parseCompilationUnit("function Foo() { } Foo.prototype.bar = function() { };", "input.js");
    CompilerResult result = compiler.compile(pass, Arrays.asList(inputNode), new CompilerOptions());

    return result.getVariableMap().get(name);
  }
}