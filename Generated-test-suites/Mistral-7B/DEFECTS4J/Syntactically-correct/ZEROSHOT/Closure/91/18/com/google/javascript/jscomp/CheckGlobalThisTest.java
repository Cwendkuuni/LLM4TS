import static org.junit.Assert.assertThat;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.isEmpty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.TypeChecker;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    SourceFile source = new SourceFile("CheckGlobalThisTest.js", "function evil() {\n" +
        "  this.useful = undefined;\n" +
        "}");

    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler() {};
    TypeChecker typeChecker = new TypeChecker(options, compiler);

    Node ast = new Parser(options, compiler).parse(source.getCode());

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    new NodeTraversal(ast, checker).traverse();

    DiagnosticType globalThisDiagnostic = checker.GLOBAL_THIS;
    int numberOfDiagnostics = compiler.getDiagnostics().size();

    // No diagnostics for this test case.
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a valid use of 'this'.
    source = new SourceFile("CheckGlobalThisTest_Valid.js", "function foo() {\n" +
        "  this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // No diagnostics for this test case.
    new NodeTraversal(ast, checker).traverse();

    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a global 'this' usage.
    source = new SourceFile("CheckGlobalThisTest_GlobalThis.js", "this.a = 1;");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // One warning diagnostic.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    Node diagnostic = getFirst(compiler.getDiagnostics());

    assertThat(diagnostic.getMessage(), is(globalThisDiagnostic.getMessage()));
    assertThat(diagnostic.getSourceName(), is(source.getName()));
    assertThat(diagnostic.getSourceCodeStart(), is(0));
    assertThat(diagnostic.getSourceCodeEnd(), is(4));

    // Test case with a 'this' usage inside a function.
    source = new SourceFile("CheckGlobalThisTest_FunctionThis.js", "function evil() {\n" +
        "  this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a constructor.
    source = new SourceFile("CheckGlobalThisTest_ConstructorThis.js", "function Evil() {\n" +
        "  this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a method.
    source = new SourceFile("CheckGlobalThisTest_MethodThis.js", "var obj = {\n" +
        "  method: function() {\n" +
        "    this.a = 1;\n" +
        "  }\n" +
        "};");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a prototype method.
    source = new SourceFile("CheckGlobalThisTest_PrototypeMethodThis.js", "var obj = {\n" +
        "  __proto__: {\n" +
        "    method: function() {\n" +
        "      this.a = 1;\n" +
        "    }\n" +
        "  }\n" +
        "};");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a function annotated with @constructor.
    source = new SourceFile("CheckGlobalThisTest_ConstructorAnnotatedFunctionThis.js", "function Evil() {\n" +
        "@constructor\n" +
        "this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a function annotated with @this.
    source = new SourceFile("CheckGlobalThisTest_ThisAnnotatedFunctionThis.js", "function Evil() {\n" +
        "@this\n" +
        "this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a function where there's no logical place to put a @this annotation.
    source = new SourceFile("CheckGlobalThisTest_FunctionNoThisAnnotation.js", "function evil(x) {\n" +
        "  this.a = x;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a function that is getting lent to a prototype property.
    source = new SourceFile("CheckGlobalThisTest_FunctionLentToPrototypeThis.js", "function Evil() {\n" +
        "  this.a = 1;\n" +
        "}\n" +
        "var obj = {\n" +
        "  __proto__: {\n" +
        "    a: Evil\n" +
        "  }\n" +
        "};");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a function that is getting lent to a prototype subproperty.
    source = new SourceFile("CheckGlobalThisTest_FunctionLentToPrototypeSubPropertyThis.js", "function Evil() {\n" +
        "  this.a = {\n" +
        "    b: 1\n" +
        "  };\n" +
        "}\n" +
        "var obj = {\n" +
        "  __proto__: {\n" +
        "    a: Evil\n" +
        "  }\n" +
        "};");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    new NodeTraversal(ast, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a constructor that is getting lent to a prototype property.
    source = new SourceFile("CheckGlobalThisTest_ConstructorLentToPrototypeThis.js", "function Evil() {\n" +
        "this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Create a prototype object.
    Node prototype = NodeUtil.createObjectLiteral(new Node("prototype"), new Node("a"), new Node(1));

    Node obj = NodeUtil.createObjectLiteral(new Node("__proto__"), new Node("prototype"));

    Node constructor = NodeUtil.createFunction(new Node("Evil"), null, null, null, null);

    Node constructorAssignment = NodeUtil.createAssign(new Node("obj"), constructor, null);

    Node root = new Node("(function() {\n" +
        "  var obj = " + constructorAssignment.toString() + ";\n" +
        "})()");

    new NodeTraversal(root, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a constructor that is getting lent to a prototype subproperty.
    source = new SourceFile("CheckGlobalThisTest_ConstructorLentToPrototypeSubPropertyThis.js", "function Evil() {\n" +
        "this.a = {\n" +
        "  b: 1\n" +
        "};\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Create a prototype object.
    prototype = NodeUtil.createObjectLiteral(new Node("prototype"), new Node("a"), NodeUtil.createObjectLiteral(new Node("b"), NodeUtil.createNumberLiteral(1)));

    obj = NodeUtil.createObjectLiteral(new Node("__proto__"), new Node("prototype"));

    constructor = NodeUtil.createFunction(new Node("Evil"), null, null, null, null);

    constructorAssignment = NodeUtil.createAssign(new Node("obj"), constructor, null);

    root = new Node("(function() {\n" +
        "  var obj = " + constructorAssignment.toString() + ";\n" +
        "})()");

    new NodeTraversal(root, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a function that is getting lent to a non-prototype property.
    source = new SourceFile("CheckGlobalThisTest_FunctionLentToNonPrototypeThis.js", "function Evil() {\n" +
        "this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node obj = NodeUtil.createObjectLiteral(new Node("a"), new Node("b"), new Node("c"));

    Node functionAssignment = NodeUtil.createAssign(new Node("obj"), NodeUtil.createFunction(new Node("Evil"), null, null, null, null), null);

    Node root = new Node("(function() {\n" +
        "  var obj = " + functionAssignment.toString() + ";\n" +
        "})()");

    new NodeTraversal(root, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());

    // Test case with a 'this' usage inside a constructor that is getting lent to a non-prototype property.
    source = new SourceFile("CheckGlobalThisTest_ConstructorLentToNonPrototypeThis.js", "function Evil() {\n" +
        "this.a = 1;\n" +
        "}");

    ast = new Parser(options, compiler).parse(source.getCode());
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Create a prototype object.
    prototype = NodeUtil.createObjectLiteral(new Node("prototype"));

    obj = NodeUtil.createObjectLiteral(new Node("a"), new Node("b"), new Node("c"));

    constructor = NodeUtil.createFunction(new Node("Evil"), null, null, null, null);

    constructorAssignment = NodeUtil.createAssign(new Node("obj"), constructor, null);

    root = new Node("(function() {\n" +
        "  var obj = " + constructorAssignment.toString() + ";\n" +
        "})()");

    new NodeTraversal(root, checker).traverse();

    // No diagnostics for this test case.
    numberOfDiagnostics = compiler.getDiagnostics().size();
    assertThat(numberOfDiagnostics, isEmpty());
  }
}