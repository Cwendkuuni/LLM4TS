import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.parser.ParseTree;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = new CompilerInput(new File("test.js"));
    processor = new ProcessCommonJSModules(compiler, ".");
  }

  @Test
  public void testProcess_noRequireCalls() {
    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);

    processor.process(input, root);

    assertNotNull(processor.getModule());
  }

  @Test
  public void testProcess_requireCallWithoutModuleNameArgument() {
    String testCode = "require('');";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    Node requireCall = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);
    when(root.getFirstChild()).thenReturn(requireCall);

    processor.process(input, root);

    Set<String> requires = processor.getInput().getRequires();
    assertEquals("", requires.iterator().next());
  }

  @Test
  public void testProcess_requireCallWithModuleNameArgument() {
    String testCode = "require('test');";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    Node requireCall = mock(Node.class);
    Node moduleName = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);
    when(root.getFirstChild()).thenReturn(requireCall);
    when(requireCall.getChildCount()).thenReturn(2);
    when(requireCall.getChildAtIndex(0)).thenReturn(IR.name("require"));
    when(requireCall.getChildAtIndex(1)).thenReturn(moduleName);

    processor.process(input, root);

    Set<String> requires = processor.getInput().getRequires();
    assertEquals("test", requires.iterator().next());
  }

  @Test
  public void testProcess_moduleExports() {
    String testCode = "module.exports = 123;";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);

    Node moduleExports = mock(Node.class);
    when(root.getFirstChild()).thenReturn(
        IR.getprop(IR.name("module"), IR.string("exports"))).thenReturn(moduleExports);

    processor.process(input, root);

    assertEquals("123", processor.getModule().getModuleExports().getValue());
  }

  @Test
  public void testProcess_moduleExportsWithDefault() {
    String testCode = "module.exports = function() {};";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);

    Node moduleExports = mock(Node.class);
    when(root.getFirstChild())
        .thenReturn(IR.getprop(IR.name("module"), IR.string("exports")))
        .thenReturn(moduleExports);

    processor.process(input, root);

    assertEquals("function () {}\n", processor.getModule().getSourceCode());
  }

  @Test
  public void testProcess_scriptWithMultipleRequireCalls() {
    String testCode = "require('test1');\n" +
                      "require('test2');\n";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);

    Node requireCall1 = mock(Node.class);
    Node moduleName1 = mock(Node.class);
    when(requireCall1.getChildCount()).thenReturn(2);
    when(requireCall1.getChildAtIndex(0)).thenReturn(IR.name("require"));
    when(requireCall1.getChildAtIndex(1)).thenReturn(moduleName1);
    when(moduleName1.getString()).thenReturn("test1");

    Node requireCall2 = mock(Node.class);
    Node moduleName2 = mock(Node.class);
    when(requireCall2.getChildCount()).thenReturn(2);
    when(requireCall2.getChildAtIndex(0)).thenReturn(IR.name("require"));
    when(requireCall2.getChildAtIndex(1)).thenReturn(moduleName2);
    when(moduleName2.getString()).thenReturn("test2");

    when(root.getChildCount()).thenReturn(2);
    when(root.getChildAtIndex(0)).thenReturn(requireCall1);
    when(root.getChildAtIndex(1)).thenReturn(requireCall2);

    processor.process(input, root);

    Set<String> requires = processor.getInput().getRequires();
    assertEquals("test1", requires.iterator().next());
    assertEquals("test2", requires.iterator().next());
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_requireCallWithInvalidURI() {
    String testCode = "require('../test/');";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);

    Node requireCall = mock(Node.class);
    Node moduleName = mock(Node.class);
    when(requireCall.getChildCount()).thenReturn(2);
    when(requireCall.getChildAtIndex(0)).thenReturn(IR.name("require"));
    when(requireCall.getChildAtIndex(1)).thenReturn(moduleName);
    when(moduleName.getString()).thenReturn("../test/");

    processor.process(input, root);
  }

  @Test
  public void testProcess_moduleWithProvideStatement() {
    String testCode = "goog.provide('example');\n" +
                      "module.exports = 123;";

    Parser parser = mock(Parser.class);
    when(compiler.getParserForInput(input)).thenReturn(parser);
    ParseTree tree = mock(ParseTree.class);
    when(parser.parseCompilationUnit(input)).thenReturn(tree);

    Node root = mock(Node.class);
    when(tree.getRootNode()).thenReturn(root);

    Node provideStatement = mock(Node.class);
    when(root.getFirstChild()).thenReturn(provideStatement);
    when(provideStatement.getQualifiedName()).thenReturn("goog.provide");
    when(provideStatement.getFirstChild()).thenReturn(IR.string("example"));

    Node moduleExports = mock(Node.class);
    when(root.getChildAtIndex(1)).thenReturn(moduleExports);

    processor.process(input, root);

    String expectedModuleCode = "goog.provide('example');" +
                                "\nvar module$exports = module.exports;" +
                                "\nmodule.exports = null;" +
                                "\nexport module$exports;";

    String actualModuleCode = processor.getModule().getSourceCode();
    assertEquals(expectedModuleCode, actualModuleCode);
  }
}