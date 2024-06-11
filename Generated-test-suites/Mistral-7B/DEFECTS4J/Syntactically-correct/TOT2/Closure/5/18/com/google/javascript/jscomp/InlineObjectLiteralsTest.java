import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    String sourceCode = "function main() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: {\n" +
        "      d: 3,\n" +
        "      e: 4\n" +
        "    },\n" +
        "    f: function() {\n" +
        "      return this.a;\n" +
        "    }\n" +
        "  };\n" +
        "  var objRef = obj;\n" +
        "  var a = obj.a;\n" +
        "  var b = obj.b;\n" +
        "  var c = obj.c;\n" +
        "  var d = c.d;\n" +
        "  var e = c.e;\n" +
        "  var f = obj.f;\n" +
        "  var g = obj.g;\n" +
        "  \n" +
        "  // Use the object and its properties directly\n" +
        "  var result1 = obj.a + obj.b;\n" +
        "  var result2 = obj.c.d + obj.c.e;\n" +
        "  var result3 = obj.f();\n" +
        "  var result4 = obj.g();\n" +
        "  \n" +
        "  // Use the object and its properties as references\n" +
        "  var objRefResult1 = objRef.a + objRef.b;\n" +
        "  var objRefResult2 = c.d + c.e;\n" +
        "  var objRefResult3 = f();\n" +
        "  var objRefResult4 = g();\n" +
        "  \n" +
        "  // Check that the object and its properties have been inlined\n" +
        "  var inlineObj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: {\n" +
        "      d: 3,\n" +
        "      e: 4\n" +
        "    },\n" +
        "    f: function() {\n" +
        "      return this.a;\n" +
        "    },\n" +
        "    g: null\n" +
        "  };\n" +
        "  var inlineA = inlineObj.a;\n" +
        "  var inlineB = inlineObj.b;\n" +
        "  var inlineC = inlineObj.c;\n" +
        "  var inlineD = inlineC.d;\n" +
        "  var inlineE = inlineC.e;\n" +
        "  var inlineF = inlineObj.f;\n" +
        "  var inlineG = null;\n" +
        "  \n" +
        "  // Use the inlined object and its properties directly\n" +
        "  var inlineResult1 = inlineA + inlineB;\n" +
        "  var inlineResult2 = inlineD + inlineE;\n" +
        "  var inlineResult3 = inlineF();\n" +
        "  \n" +
        "  // Use the inlined object and its properties as references\n" +
        "  var inlineObjRef = inlineObj;\n" +
        "  var inlineObjRefResult1 = inlineObjRef.a + inlineObjRef.b;\n" +
        "  var inlineObjRefResult2 = inlineC.d + inlineC.e;\n" +
        "  var inlineObjRefResult3 = inlineF();\n" +
        "  \n" +
        "  // Check that the results of using the object and its properties\n" +
        "  // are the same whether they are inlined or not\n" +
        "  assertEquals(result1, inlineResult1);\n" +
        "  assertEquals(result2, inlineResult2);\n" +
        "  assertEquals(result3, inlineResult3);\n" +
        "  assertEquals(objRefResult1, inlineObjRefResult1);\n" +
        "  assertEquals(objRefResult2, inlineObjRefResult2);\n" +
        "  assertEquals(objRefResult3, inlineObjRefResult3);\n" +
        "}";

    compiler.compile(sourceCode, "InlineObjectLiteralsTest", null);

    inlineObjectLiterals.process(compiler.getExterns(), compiler.getRootNode());
    compiler.compile(compiler.getSourceCode(), "InlineObjectLiteralsTest", null);

    String expectedResult = "function main() {\n" +
      "  var a = 1;\n" +
      "  var b = 2;\n" +
      "  var c = {\n" +
      "    d: 3,\n" +
      "    e: 4\n" +
      "  };\n" +
      "  var f = function() {\n" +
      "    return this.a;\n" +
      "  };\n" +
      "  var g = null;\n" +
      "  \n" +
      "  // Use the variables and their values directly\n" +
      "  var result1 = a + b;\n" +
      "  var result2 = c.d + c.e;\n" +
      "  var result3 = f();\n" +
      "  var result4 = g;\n" +
      "  \n" +
      "  // Use the variables and their values as references\n" +
      "  var objRef = a;\n" +
      "  var objRefResult1 = objRef + b;\n" +
      "  var objRefResult2 = c.d + c.e;\n" +
      "  var objRefResult3 = f();\n" +
      "  var objRefResult4 = g;\n" +
      "  \n" +
      "  // Check that the variables and their values have not changed\n" +
      "  assertEquals(a, result1);\n" +
      "  assertEquals(b, result2);\n" +
      "  assertEquals(1, result3);\n" +
      "  assertEquals(null, result4);\n" +
      "  assertEquals(objRef, objRefResult1);\n" +
      "  assertEquals(2, objRefResult2);\n" +
      "  assertEquals(1, objRefResult3);\n" +
      "  assertEquals(null, objRefResult4);\n" +
      "}";

    assertEquals(expectedResult, compiler.getSourceCode());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithInvalidObjectLiteral() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    String sourceCode = "function main() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 3,\n" +
        "    },\n" +
        "  };\n" +
        "  var a = obj.a;\n" +
        "  var b = obj.b.c;\n" +
        "}\n";

    compiler.compile(sourceCode, "InlineObjectLiteralsTest", null);

    inlineObjectLiterals.process(compiler.getExterns(), compiler.getRootNode());
    compiler.compile(compiler.getSourceCode(), "InlineObjectLiteralsTest", null);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithInvalidReference() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    String sourceCode = "function main() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "  };\n" +
        "  var a = obj.a;\n" +
        "  var b = obj.x;\n" +
        "}\n";

    compiler.compile(sourceCode, "InlineObjectLiteralsTest", null);

    inlineObjectLiterals.process(compiler.getExterns(), compiler.getRootNode());
    compiler.compile(compiler.getSourceCode(), "InlineObjectLiteralsTest", null);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithInvalidAssignment() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");

    String sourceCode = "function main() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "  };\n" +
        "  obj.a = obj;\n" +
        "}\n";

    compiler.compile(sourceCode, "InlineObjectLiteralsTest", null);

    inlineObjectLiterals.process(compiler.getExterns(), compiler.getRootNode());
    compiler.compile(compiler.getSourceCode(), "InlineObjectLiteralsTest", null);
  }
}