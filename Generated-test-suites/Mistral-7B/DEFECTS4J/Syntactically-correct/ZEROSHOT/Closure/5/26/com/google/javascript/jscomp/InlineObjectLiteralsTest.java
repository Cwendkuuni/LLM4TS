package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ConsoleLogger;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObject() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {a: 1, b: 2};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);
    String expectedCode = "var a = 1, b = 2, tempVar_a = null, tempVar_b = null;" +
        "obj = {tempVar_a: tempVar_a, tempVar_b: tempVar_b}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineNestedObject() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {a: {b: 1, c: 2}};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);
    String expectedCode = "var a = {b: 1, c: 2};" +
        "var tempVar_a = null;" +
        "obj = {tempVar_a: tempVar_a}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {a: 1, getB: function() { return 2; }};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);
    String expectedCode = "var a = 1;" +
        "var getB = function() {" +
        "  return 2;" +
        "}" +
        "var tempVar_a = null;" +
        "obj = {a: a, getB: getB, tempVar_a: tempVar_a}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithGetterAndSetter() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  get x() { return 1; }," +
        "  set x(val) { this.x = val; }," +
        "  a: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);

    String expectedCode = "var a = 1;" +
        "var x = function() {" +
        "  return 1;" +
        "}" +
        "var setx = function(val) {" +
        "  a = val;" +
        "}" +
        "var tempVar_a = null;" +
        "obj = {a: a, x: x, setx: setx, tempVar_a: tempVar_a}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithFunctionProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  foo: function() { return 1; }," +
        "  a: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);
    String expectedCode = "var foo = function() {" +
        "  return 1;" +
        "}" +
        "var a = 1;" +
        "var tempVar_foo = null;" +
        "obj = {foo: foo, a: a, tempVar_foo: tempVar_foo}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithArrayProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  a: [1, 2, 3]," +
        "  b: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);

    String expectedCode = "var a = [1, 2, 3];" +
        "var b = 1;" +
        "var tempVar_a = null;" +
        "obj = {a: a, b: b, tempVar_a: tempVar_a}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithFunctionPropertyAndArrayProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  foo: function() { return 1; }," +
        "  a: [1, 2, 3]," +
        "  b: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);

    String expectedCode = "var foo = function() {" +
        "  return 1;" +
        "}" +
        "var a = [1, 2, 3]" +
        "var b = 1;" +
        "var tempVar_foo = null;" +
        "obj = {foo: foo, a: a, b: b, tempVar_foo: tempVar_foo}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithFunctionPropertyAndGetterSetter() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  get getX() { return 1; }," +
        "  set setX(val) { this.x = val; }," +
        "  foo: function() { return 1; }," +
        "  a: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);

    String expectedCode = "var foo = function() {" +
        "  return 1;" +
        "}" +
        "var getX = function() {" +
        "  return 1;" +
        "}" +
        "var setX = function(val) {" +
        "  a = val;" +
        "}" +
        "var a = 1;" +
        "var tempVar_foo = null;" +
        "obj = {foo: foo, getX: getX, setX: setX, a: a, tempVar_foo: tempVar_foo}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithFunctionPropertyAndGetterSetterAndArrayProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  get getX() { return 1; }," +
        "  set setX(val) { this.x = val; }," +
        "  foo: function() { return 1; }," +
        "  a: [1, 2]," +
        "  b: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);

    String expectedCode = "var foo = function() {" +
        "  return 1;" +
        "}" +
        "var getX = function() {" +
        "  return 1;" +
        "}" +
        "var setX = function(val) {" +
        "  a = val;" +
        "}" +
        "var a = [1, 2]" +
        "var b = 1;" +
        "var tempVar_foo = null;" +
        "obj = {foo: foo, getX: getX, setX: setX, a: a, b: b, tempVar_foo: tempVar_foo}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }

  @Test
  public void testInlineObjectWithFunctionPropertyAndGetterSetterAndNestedObjectProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.CHEAP);
    Compiler compiler = new Compiler(new ConsoleLogger());
    compiler.init(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tempVar");
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, null, null, pass);

    String jsCode = "var obj = {" +
        "  get getX() { return 1; }," +
        "  set setX(val) { this.x = val; }," +
        "  foo: function() { return 1; }," +
        "  nested: {" +
        "    a: 2," +
        "    b: 3" +
        "  }," +
        "  a: 1" +
        "};";
    Node node = abstractCompiler.parseTree(jsCode);
    pass.process(node, null);

    String expectedCode = "var foo = function() {" +
        "  return 1;" +
        "}" +
        "var getX = function() {" +
        "  return 1;" +
        "}" +
        "var setX = function(val) {" +
        "  a = val;" +
        "}" +
        "var nested = {" +
        "  a: 2," +
        "  b: 3" +
        "}" +
        "var a = 1;" +
        "var tempVar_foo = null;" +
        "obj = {foo: foo, getX: getX, setX: setX, a: a, nested: nested, tempVar_foo: tempVar_foo}";
    String actualCode = abstractCompiler.getSource();
    assertThat(actualCode, is(expectedCode));
  }
}