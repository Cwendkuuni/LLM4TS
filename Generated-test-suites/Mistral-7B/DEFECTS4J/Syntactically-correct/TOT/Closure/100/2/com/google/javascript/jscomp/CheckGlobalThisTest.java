import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new Compiler();
    CheckerFactory checkerFactory = new CheckerFactory(compiler);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkerFactory.getChecks().addCheck(checker, CheckGlobalThis.GLOBAL_THIS);

    // Prototype method
    String prototypeCode = "function Person() {}\nPerson.prototype.getName = function() { return this.name; }";
    Node prototypeNode = RhinoLoader.compileToNode(prototypeCode);
    checker.traverse(prototypeNode, null);
    assertEquals(0, compiler.getNumErrors());

    // @constructor annotation
    String constructorCode = "function Person(name) { this.name = name; }";
    Node constructorNode = RhinoLoader.compileToNode(constructorCode);
    checker.traverse(constructorNode, null);
    assertEquals(0, compiler.getNumErrors());

    // @this annotation
    String thisAnnotationCode = "function Person() { @this Person.prototype;\n this.name = 'John'; }";
    Node thisAnnotationNode = RhinoLoader.compileToNode(thisAnnotationCode);
    checker.traverse(thisAnnotationNode, null);
    assertEquals(0, compiler.getNumErrors());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    AbstractCompiler compiler = new Compiler();
    CheckerFactory checkerFactory = new CheckerFactory(compiler);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkerFactory.getChecks().addCheck(checker, CheckGlobalThis.GLOBAL_THIS);

    // Global this
    String globalThisCode = "this.name = 'John'";
    Node globalThisNode = RhinoLoader.compileToNode(globalThisCode);
    checker.traverse(globalThisNode, null);
    assertTrue(compiler.hasError(CheckGlobalThis.GLOBAL_THIS));

    // Function with no logical place for @this
    String noThisAnnotationCode = "function Person() { this.name = 'John'; }";
    Node noThisAnnotationNode = RhinoLoader.compileToNode(noThisAnnotationCode);
    checker.traverse(noThisAnnotationNode, null);
    assertTrue(compiler.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testNestedAssignments() {
    AbstractCompiler compiler = new Compiler();
    CheckerFactory checkerFactory = new CheckerFactory(compiler);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkerFactory.getChecks().addCheck(checker, CheckGlobalThis.GLOBAL_THIS);

    // Nested assignments (a = this).property = c;
    String nestedAssignmentsCode = "var a = {\n  property: function() {\n    this.name = 'John';\n  }.bind(this)\n};\n" +
                                   "a = this;\n" +
                                   "a.property = 'John'";
    Node nestedAssignmentsNode = RhinoLoader.compileToNode(nestedAssignmentsCode);
    checker.traverse(nestedAssignmentsNode, null);
    assertTrue(compiler.hasError(CheckGlobalThis.GLOBAL_THIS));
  }
}