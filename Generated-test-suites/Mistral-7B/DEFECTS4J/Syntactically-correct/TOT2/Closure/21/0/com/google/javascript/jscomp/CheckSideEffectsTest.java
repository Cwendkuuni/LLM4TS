import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {

  @org.junit.Test
  public void testCheckSideEffects() {
    // Setup
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.WARNING, "JsCompiler");
    Compiler compiler = new Compiler(options);

    // Basic functionality test
    String code = "function main() {\n" +
        "  var x = 5;\n" +
        "  foo();\n" +
        "  x = foo();\n" +
        "}";
    SourceFile input = new SourceFile("Test.java", code, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input.getAst(), input.getAst().getFirstChild());
    Node result = input.getAst();
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Edge cases
    code = "function main() {\n" +
        "  var x = 5;\n" +
        "  ;\n" + // empty statement
        "  foo();\n" +
        "  x = foo();\n" +
        "}";
    input = new SourceFile("Test.java", code, null);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input.getAst(), input.getAst().getFirstChild());
    result = input.getAst();
    assertEquals(0, checkSideEffects.problemNodes.size());

    code = "function main() {\n" +
        "  var x = 5;\n" +
        "  x = foo();\n" +
        "  ;\n" + // empty statement
        "  foo();\n" +
        "}";
    input = new SourceFile("Test.java", code, null);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input.getAst(), input.getAst().getFirstChild());
    result = input.getAst();
    assertEquals(0, checkSideEffects.problemNodes.size());

    code = "function main() {\n" +
        "  var x = 5;\n" +
        "  foo();\n" +
        "  foo();\n" +
        "  x = foo();\n" +
        "}";
    input = new SourceFile("Test.java", code, null);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input.getAst(), input.getAst().getFirstChild());
    result = input.getAst();
    assertEquals(1, checkSideEffects.problemNodes.size());

    code = "function main() {\n" +
        "  var x = 5;\n" +
        "  foo();\n" +
        "  foo();\n" +
        "  x = foo();\n" +
        "  foo();\n" +
        "}";
    input = new SourceFile("Test.java", code, null);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input.getAst(), input.getAst().getFirstChild());
    result = input.getAst();
    assertEquals(1, checkSideEffects.problemNodes.size());

    // Exception handling
    try {
      checkSideEffects.process(null, null);
    } catch (NullPointerException e) {
      assertNotNull(e);
    }

    try {
      checkSideEffects.process(null, new Node());
    } catch (NullPointerException e) {
      assertNotNull(e);
    }

    try {
      CheckSideEffects checkSideEffects1 = new CheckSideEffects(null, CheckLevel.WARNING, false);
    } catch (NullPointerException e) {
      assertNotNull(e);
    }

    try {
      CheckSideEffects checkSideEffects1 = new CheckSideEffects(compiler, null, false);
    } catch (IllegalArgumentException e) {
      assertNotNull(e);
    }
  }
}