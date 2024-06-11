import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    // Test case 1: Safe usage of 'this' inside a constructor
    String code1 = "function MyConstructor() { this.x = 1; }";
    SourceFile sourceFile1 = Parser.parseJavaScript(code1, "MyConstructor.js");
    CompilerOptions options1 = new CompilerOptions();
    options1.setWarningLevel(CHECK_GLOBAL_THIS, WARNING);
    AbstractCompiler compiler1 = new AbstractCompiler(options1);
    Checker checker1 = new Checker(compiler1);
    checker1.checkSourceBase(sourceFile1, null, null);
    assertFalse(compiler1.getDiagnostics().hasError());

    // Test case 2: Unsafe usage of 'this' outside a constructor or a function
    String code2 = "function evil() { this.x = 1; }";
    SourceFile sourceFile2 = Parser.parseJavaScript(code2, "evil.js");
    CompilerOptions options2 = new CompilerOptions();
    options2.setWarningLevel(CHECK_GLOBAL_THIS, WARNING);
    AbstractCompiler compiler2 = new AbstractCompiler(options2);
    Checker checker2 = new Checker(compiler2);
    checker2.checkSourceBase(sourceFile2, null, null);
    assertTrue(compiler2.getDiagnostics().hasError());

    // Test case 3: Unsafe usage of 'this' in a function without a logical place to put a 'this' annotation
    String code3 = "function unsafeFunction() { this.x = 1; }";
    SourceFile sourceFile3 = Parser.parseJavaScript(code3, "unsafeFunction.js");
    CompilerOptions options3 = new CompilerOptions();
    options3.setWarningLevel(CHECK_GLOBAL_THIS, WARNING);
    AbstractCompiler compiler3 = new AbstractCompiler(options3);
    Checker checker3 = new Checker(compiler3);
    checker3.checkSourceBase(sourceFile3, null, null);
    assertTrue(compiler3.getDiagnostics().hasError());

    // Test case 4: Safe usage of 'this' inside a prototype method
    String code4 = "function Parent() {} Parent.prototype.method = function() { this.x = 1; }";
    SourceFile sourceFile4 = Parser.parseJavaScript(code4, "Parent.js");
    CompilerOptions options4 = new CompilerOptions();
    options4.setWarningLevel(CHECK_GLOBAL_THIS, WARNING);
    AbstractCompiler compiler4 = new AbstractCompiler(options4);
    Checker checker4 = new Checker(compiler4);
    checker4.checkSourceBase(sourceFile4, null, null);
    assertFalse(compiler4.getDiagnostics().hasError());

    // Test case 5: Safe usage of 'this' inside a compiler-generated constructor
    String code5 = "class MyClass { constructor() {} }";
    SourceFile sourceFile5 = Parser.parseJavaScript(code5, "MyClass.js");
    CompilerOptions options5 = new CompilerOptions();
    options5.setWarningLevel(CHECK_GLOBAL_THIS, WARNING);
    AbstractCompiler compiler5 = new AbstractCompiler(options5);
    Checker checker5 = new Checker(compiler5);
    checker5.checkSourceBase(sourceFile5, null, null);
    assertFalse(compiler5.getDiagnostics().hasError());

    // Test case 6: Safe usage of 'this' inside a factory function annotated with @constructor
    String code6 = "function MyFactory() { return { x: 1 }; }";
    SourceFile sourceFile6 = Parser.parseJavaScript(code6, "MyFactory.js");
    CompilerOptions options6 = new CompilerOptions();
    options6.setWarningLevel(CHECK_GlobalThis, WARNING);
    AbstractCompiler compiler6 = new AbstractCompiler(options6);
    Checker checker6 = new Checker(compiler6);
    JSDocInfo jsdoc6 = new JSDocInfo();
    jsdoc6.setConstructor(true);
    sourceFile6.setJSDocInfo(jsdoc6);
    checker6.checkSourceBase(sourceFile6, null, null);
    assertFalse(compiler6.getDiagnostics().hasError());

    // Test case 7: Safe usage of 'this' inside a function annotated with @this
    String code7 = "/** @this {Number} */ function MyFunction() { this.x = 1; }";
    SourceFile sourceFile7 = Parser.parseJavaScript(code7, "MyFunction.js");
    CompilerOptions options7 = new CompilerOptions();
    options7.setWarningLevel(CHECK_GlobalThis, WARNING);
    AbstractCompiler compiler7 = new AbstractCompiler(options7);
    Checker checker7 = new Checker(compiler7);
    JSDocInfo jsdoc7 = new JSDocInfo();
    jsdoc7.setThisType("Number");
    sourceFile7.setJSDocInfo(jsdoc7);
    checker7.checkSourceBase(sourceFile7, null, null);
    assertFalse(compiler7.getDiagnostics().hasError());

    // Test case 8: Safe usage of 'this' inside a function annotated with @constructor and @this
    String code8 = "/** @constructor @this {String} */ function MyConstructor() { this.x = 1; }";
    SourceFile sourceFile8 = Parser.parseJavaScript(code8, "MyConstructor.js");
    CompilerOptions options8 = new CompilerOptions();
    options8.setWarningLevel(CHECK_GlobalThis, WARNING);
    AbstractCompiler compiler8 = new AbstractCompiler(options8);
    Checker checker8 = new Checker(compiler8);
    JSDocInfo jsdoc8 = new JSDocInfo();
    jsdoc8.setConstructor(true);
    jsdoc8.setThisType("String");
    sourceFile8.setJSDocInfo(jsdoc8);
    checker8.checkSourceBase(sourceFile8, null, null);
    assertFalse(compiler8.getDiagnostics().hasError());
  }

  private static final String CHECK_GLOBAL_THIS = "CheckGlobalThis";
}