import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testRenamePrototypes() throws IOException {
    CompilerOptions options = new CompilerOptions(new CompilerOptions());
    options.setAssumeFunctionTypes(true);
    options.setWarningLevel(0);

    Compiler compiler = new Compiler(options);
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    String sourceCode =
        "function Foo() {}\n" +
        "Foo.prototype.getBar = function() { return 'original'; };\n" +
        "Foo.prototype.getBaz = function() { return 'original'; };\n" +
        "Foo.prototype.setBar = function(x) { this.bar = x; };\n" +
        "Foo.prototype = {\n" +
        "  getFoo: function() { return 'original'; },\n" +
        "  getQuux: function() { return 'original'; },\n" +
        "  bar: 42\n" +
        "};\n" +
        "var obj = new Foo();\n" +
        "var x = obj.getBar();\n" +
        "obj.setBar(17);\n" +
        "print(obj.getBar());";

    TokenStream input = new TokenStream(new StringBuilder(sourceCode));
    CompilerResult result = compiler.compile(input);

    VariableMap prevUsedRenameMap = result.getVariableMap();
    NodeAST ast = result.getRootNode();

    renamePrototypes.process(result.getExterns(), ast.getBody());
    String renamedSourceCode = compiler.getDebugLog().toString();

    // Reset the compiler options to their defaults, so that we don't get
    // any interference from the previous compilation.
    options = new CompilerOptions(new CompilerOptions());
    compiler = new Compiler(options);

    // Compile the renamed source code
    NodeAST renamedAst = compileAndParse(compiler, renamedSourceCode);

    // Check that the renaming actually happened
    Map<String, String> expectedRenamings = new HashMap<>();
    expectedRenamings.put("getBar", "getRenamedBar");
    expectedRenamings.put("getBaz", "getRenamedBaz");
    expectedRenamings.put("setBar", "setRenamedBar");
    expectedRenamings.put("getFoo", "getRenamedFoo");
    expectedRenamings.put("getQuux", "getRenamedQuux");

    VariableMap actualRenamings = getPropertyMap(renamedAst);
    assertEquals(expectedRenamings, actualRenamings);

    // Check that the code still works after the rename
    NodeAST executedAst = execute(compiler, renamedAst);
    assertEquals("17", executedAst.getChild(1).getString());
    assertEquals("17", executedAst.getChild(7).getString());
  }

  private NodeAST compileAndParse(Compiler compiler, String sourceCode) throws IOException {
    TokenStream input = new TokenStream(new StringBuilder(sourceCode));
    CompilerResult result = compiler.compile(input);
    return result.getRootNode();
  }

  private NodeAST execute(Compiler compiler, NodeAST ast) throws IOException {
    return compiler.process(ast, null);
  }

  private VariableMap getPropertyMap(NodeAST ast) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, ast);
    return renamePrototypes.getPropertyMap();
  }
}