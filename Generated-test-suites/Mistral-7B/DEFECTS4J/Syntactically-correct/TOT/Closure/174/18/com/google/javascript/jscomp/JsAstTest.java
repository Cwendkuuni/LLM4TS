import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsCompiler.CompilerFlags.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.util.ArrayList;
import java.util.List;

public class JsAstTest {

  private static final String TEST_FILE = "Test.js";
  private static final String VALID_CODE = "function test() { return 1; }";;
  private static final String INVALID_CODE = "function test() { return 1; } function test2() {}";

  @org.junit.Test
  public void testValidJsCode() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, VALID_CODE);
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING_CODE, Code.UNDEFINED, 0);
    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);
    Node functionNode = findFunctionNodeByName(root, "test");
    assertEquals(1, functionNode.getNumChildren());
    assertEquals(RETURN_STATEMENT, functionNode.getFirstChild().getType());
    assertEquals(1, functionNode.getFirstChild().getFirstChild().getInt32());

    assertEquals(ast.getSourceFile(), sourceFile);
    assertEquals(ast.getInputId(), sourceFile.getName());
  }

  @org.junit.Test(expected = JSError.class)
  public void testInvalidJsCode() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, INVALID_CODE);
    Compiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);
    ast.getAstRoot(compiler);
  }

  private Node findFunctionNodeByName(Node node, String name) {
    if (node.getType() == FUNCTION_DECLARATION) {
      Node functionNode = node;
      if (functionNode.getName().equals(name)) {
        return functionNode;
      }
    }

    for (int i = 0; i < node.getNumChildren(); i++) {
      Node childNode = node.getChild(i);
      Node result = findFunctionNodeByName(childNode, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @org.junit.Test
  public void testClearAst() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, VALID_CODE);
    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(new Compiler());
    ast.clearAst();
    assertNull(ast.getAstRoot(new Compiler()));
  }

  @org.junit.Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new TestSourceFile(TEST_FILE, VALID_CODE);
    SourceFile sourceFile2 = new TestSourceFile("Test2.js", VALID_CODE);
    JsAst ast = new JsAst(sourceFile1);
    ast.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, ast.getSourceFile());
  }

  @org.junit.Test
  public void testParseError() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, INVALID_CODE);
    Compiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);
    assertNull(ast.getAstRoot(compiler));
  }

  @org.junit.Test
  public void testIOException() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, null);
    Compiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);
    assertNull(ast.getAstRoot(compiler));
  }

  @org.junit.Test
  public void testOldParseTree() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, VALID_CODE);
    Compiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);
    String oldCode = "function test() { return 2; }";
    sourceFile.setCode(oldCode);
    Node oldRoot = ast.getAstRoot(compiler);
    List<Node> rootChildren = Lists.newArrayList(root.getChildren());
    List<Node> oldRootChildren = Lists.newArrayList(oldRoot.getChildren());
    List<String> rootNames = Lists.newArrayList();
    List<String> oldRootNames = Lists.newArrayList();
    for (Node node : rootChildren) {
      rootNames.add(node.getStaticSourceName().getName());
    }
    for (Node node : oldRootChildren) {
      oldRootNames.add(node.getStaticSourceName().getName());
    }
    Joiner joiner = Joiner.on("\n");
    assertEquals(joiner.join(rootNames), joiner.join(oldRootNames));
  }

  @org.junit.Test
  public void testParserRunnerParse() {
    SourceFile sourceFile = new TestSourceFile(TEST_FILE, VALID_CODE);
    Compiler compiler = new Compiler();
    ParserRunner.ParseResult result = ParserRunner.parse(sourceFile, sourceFile.getCode(),
        compiler.getParserConfig(), compiler.getDefaultErrorReporter(), logger_);
    assertTrue(result.ast != null);
  }
}