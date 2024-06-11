package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsAstTest {

  @Mock private AbstractCompiler compiler;
  @Mock private ParserConfig parserConfig;
  @Mock private SourceFile sourceFile;

  private JsAst jsAst;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node expectedRoot = IR.script();
    when(compiler.getParserConfig()).thenReturn(parserConfig);

    jsAst.parse(compiler);

    Node actualRoot = jsAst.getAstRoot(compiler);

    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testClearAst() {
    Node oldAst = IR.script();
    jsAst.setOldParseTree(sourceFile.getName(), oldAst);

    jsAst.clearAst();

    assertNull(jsAst.getOldParseTree(sourceFile.getName()));
  }

  @Test
  public void testGetInputId() {
    String expectedInputId = "test.js";
    when(sourceFile.getName()).thenReturn(expectedInputId);

    String actualInputId = jsAst.getInputId().getName();

    assertEquals(expectedInputId, actualInputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceName expectedSourceName = new SourceName("test.js");
    when(sourceFile.getName()).thenReturn("test.js");

    SourceFile actualSourceFile = jsAst.getSourceFile();

    assertEquals(expectedSourceName, actualSourceFile.getSourceName());
  }

  @Test
  public void testSetSourceFile() {
    String newFileName = "newTest.js";
    SourceFile newSourceFile = new SourceFile(newFileName);

    jsAst.setSourceFile(newSourceFile);

    assertEquals(newFileName, jsAst.getSourceFile().getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSourceFileWithDifferentFileName() {
    String newFileName = "newTest.js";
    SourceFile newSourceFile = new SourceFile("differentFileName.js");

    jsAst.setSourceFile(newSourceFile);

    fail("Should throw an IllegalArgumentException");
  }

  @Test
  public void testParseWithValidCode() {
    String code = "function test() {\n" +
        "  return 1;\n" +
        "}";

    when(sourceFile.getCode()).thenReturn(code);
    Node expectedRoot = IR.script().setBody(IR.block(IR.returnStatement(IR.intLiteral(1))));

    jsAst.parse(compiler);

    Node actualRoot = jsAst.getAstRoot(compiler);

    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testParseWithInvalidCode() {
    String code = "function test() {";

    when(sourceFile.getCode()).thenReturn(code);

    jsAst.parse(compiler);

    Node expectedRoot = IR.script();

    Node actualRoot = jsAst.getAstRoot(compiler);

    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testParseWithIOException() {
    when(sourceFile.getCode()).thenThrow(new IOException());

    jsAst.parse(compiler);

    Node expectedRoot = IR.script();

    Node actualRoot = jsAst.getAstRoot(compiler);

    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testSetOldParseTree() {
    Node oldAst = IR.script();

    jsAst.setOldParseTree(sourceFile.getName(), oldAst);

    Node actualOldAst = jsAst.getOldParseTree(sourceFile.getName());

    assertEquals(oldAst, actualOldAst);
  }
}