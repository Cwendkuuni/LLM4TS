import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String inputCode = "// Input code\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    return 'Hello World';\n" +
        "  }\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(null);
    Processor processor = new Processor(options);
    CompilerInput input = new CompilerInput(SourceName.get("<test>"), inputCode, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(processor, "");

    Node ast = NodeFactory.compile(input.getSourceText());
    pass.process(input.getExterns(), ast);

    JSModule module = pass.getModule();
    Node moduleExports = module.getVariable("module$exports");
    assertEquals("function() {\n" +
        "  return 'Hello World';\n" +
        "}", moduleExports.getChildAtIndex(0).toString());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequires() {
    String inputCode = "// Input code\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "var a = require('./b');\n" +
        "module.exports = {\n" +
        "  b: function() {\n" +
        "    return 'B';\n" +
        "  }\n" +
        "};\n" +
        "module.exports.a = function() {\n" +
        "  return 'A';\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(null);
    Processor processor = new Processor(options);
    CompilerInput input = new CompilerInput(SourceName.get("<test>"), inputCode, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(processor, "");

    Node ast = NodeFactory.compile(input.getSourceText());
    pass.process(input.getExterns(), ast);

    JSModule module = pass.getModule();
    Node aNode = module.getVariable("a");
    Node aFunction = aNode.getChildAtIndex(0);
    assertEquals("function() {\n" +
        "  return 'A';\n" +
        "}", aFunction.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithReportDependencies() {
    String inputCode = "// Input code\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    return 'Hello World';\n" +
        "  }\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(null);
    options.setGenerator(CompilerOptions.GeneratorType.ES5);
    options.setWarningLevel(CompilerOptions.WarningLevel.ERRORS);
    Processor processor = new Processor(options);
    CompilerInput input = new CompilerInput(SourceName.get("<test>"), inputCode, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(processor, "");
    pass.setReportDependencies(true);

    Node ast = NodeFactory.compile(input.getSourceText());
    assertThrows(RuntimeException.class, () -> pass.process(input.getExterns(), ast));
    assertEquals("[ERROR] Unknown module '.'\n", processor.getDiagnostics().getError(0).getMessage());
  }

  @Test
  public void testProcessCommonJSModulesWithEmptyModule() {
    String inputCode = "// Input code\n" +
        "module.exports = {};\n";

    CompilerOptions options = new CompilerOptions(null);
    Processor processor = new Processor(options);
    CompilerInput input = new CompilerInput(SourceName.get("<test>"), inputCode, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(processor, "");

    Node ast = NodeFactory.compile(input.getSourceText());
    pass.process(input.getExterns(), ast);

    JSModule module = pass.getModule();
    assertNull(module);
  }

  @Test
  public void testProcessCommonJSModulesWithCycle() {
    String inputCodeA = "// Input code for module A\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    return 'A';\n" +
        "  },\n" +
        "  require: function(moduleName) {\n" +
        "    return require('./b');\n" +
        "  }\n" +
        "};\n";

    String inputCodeB = "// Input code for module B\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    return 'B';\n" +
        "  },\n" +
        "  require: function(moduleName) {\n" +
        "    return require('./a');\n" +
        "  }\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(null);
    Processor processor = new Processor(options);
    CompilerInput inputA = new CompilerInput(SourceName.get("moduleA.js"), inputCodeA, null);
    CompilerInput inputB = new CompilerInput(SourceName.get("moduleB.js"), inputCodeB, null);
    inputA.addDependency(inputB);
    inputB.addDependency(inputA);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(processor, "");

    Node astA = NodeFactory.compile(inputA.getSourceText());
    Node astB = NodeFactory.compile(inputB.getSourceText());

    pass.process(inputA.getExterns(), astA);
    pass.process(inputB.getExterns(), astB);

    JSModule moduleA = pass.getModule("moduleA.js");
    JSModule moduleB = pass.getModule("moduleB.js");
    assertEquals("module$exports", moduleA.getVariable("require").getChildAtIndex(0).getName().getString());
    assertEquals("module$exports", moduleB.getVariable("require").getChildAtIndex(0).getName().getString());
    assertEquals("moduleA.js", moduleB.getVariable("require").getChildAtIndex(1).getString());
    assertEquals("moduleB.js", moduleA.getVariable("require").getChildAtIndex(1).getString());
  }
}