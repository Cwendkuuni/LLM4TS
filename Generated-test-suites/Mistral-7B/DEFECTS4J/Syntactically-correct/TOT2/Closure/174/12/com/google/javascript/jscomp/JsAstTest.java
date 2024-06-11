// JUnit 4 test file for JsAst class
package com.google.javascript.jscomp;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

 private SourceFile sourceFile;
 private JsAst jsAst;
 private StubAbstractCompiler abstractCompiler;

 @Before
 public void setUp() {
 sourceFile = new SourceFile("test.js");
 jsAst = new JsAst(sourceFile);
 abstractCompiler = new StubAbstractCompiler();
 }

 @Test
 public void testGetAstRoot() {
 // Test basic functionality
 Node expectedAstRoot = IR.script();
 Node actualAstRoot = jsAst.getAstRoot(abstractCompiler);
 assertEquals(expectedAstRoot, actualAstRoot);

 // Test for null AbstractCompiler
 jsAst = new JsAst(sourceFile);
 try {
 jsAst.parse(null);
 } catch (NullPointerException e) {
 // Expected
 }

 // Test for empty SourceFile
 SourceFile emptySourceFile = new SourceFile("");
 JsAst jsAstWithEmptySourceFile = new JsAst(emptySourceFile);
 assertNull(jsAstWithEmptySourceFile.getAstRoot(abstractCompiler));

 // Test for invalid SourceFile
 SourceFile invalidSourceFile = new SourceFile("{invalid: json}");
 JsAst jsAstWithInvalidSourceFile = new JsAst(invalidSourceFile);
 assertThrows(IllegalArgumentException.class, () -> jsAstWithInvalidSourceFile.getAstRoot(abstractCompiler));

 // Test for IOException during parsing
 doReturn(new File("test.js")).when(jsAst).getSourceFile();
 doThrow(new IOException("Test IOException")).when(sourceFile).getCode();
 try {
 jsAst.parse(abstractCompiler);
 } catch (IOException e) {
 // Expected
 }
 assertNull(jsAst.getAstRoot(abstractCompiler));

 // Test for halting errors during parsing
 abstractCompiler.setHaltingErrors(true);
 jsAst.parse(abstractCompiler);
 assertNull(jsAst.getAstRoot(abstractCompiler));
 }

 @After
 public void tearDown() {
 jsAst = null;
 abstractCompiler = null;
 sourceFile = null;
 }

 static class StubAbstractCompiler extends AbstractCompiler {
 boolean haltingErrors = false;

 @Override
 public boolean hasHaltingErrors() {
 return haltingErrors;
 }

 @Override
 public void setHaltingErrors(boolean haltingErrors) {
 this.haltingErrors = haltingErrors;
 }
 }
}