import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.SourceFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

 private Compiler compiler;
 private SourceFile originalSourceFile;

 @Before
 public void setup() {
 compiler = new Compiler();
 compiler.setOptions(new CompilerOptions());
 originalSourceFile = new SourceFile("CollapseVariableDeclarationsTest.js");
 }

 @Test
 public void testCollapsingSimpleVariables() {
 Node rootNode = createNode("var a; var b = 1; var c = 2;");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("var a, b = 1, c = 2;", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test
 public void testCollapsingVariablesWithInitializers() {
 Node rootNode = createNode("var a = 1; var b = 2; var c = 3;");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("var a = 1, b = 2, c = 3;", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test
 public void testCollapsingVariablesWithAssignments() {
 Node rootNode = createNode("var a = 1; a = 2; var b = 3;");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("var a = 1, b = 3; a = 2;", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test
 public void testCollapsingVariablesInsideFunction() {
 Node rootNode = createNode("function foo() {\n" +
 "  var a; \n" +
 "  var b = 1; \n" +
 "  var c = 2;\n" +
 "}");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("function foo() {\n" +
 "  var a, b = 1, c = 2;\n" +
 "}", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test
 public void testCollapsingVariablesInsideIfStatement() {
 Node rootNode = createNode("if (true) {\n" +
 "  var a; \n" +
 "  var b = 1; \n" +
 "} else {\n" +
 "  var c = 1;\n" +
 "}");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("if (true) {\n" +
 "  var a, b = 1;\n" +
 "} else {\n" +
 "  var c = 1;\n" +
 "}", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test
 public void testCollapsingVariablesWithConflictingNames() {
 Node rootNode = createNode("var a; var b = function a() {};");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("var a, b = function a();", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test
 public void testCollapsingVariablesWithConflictingNames2() {
 Node rootNode = createNode("var a = function a() {}; var a = 1;");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);

 checkResult("var a = function a() {}; var a = 1;", getCode(collapseVariableDeclarations.getResultNode()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollapsingVariablesInsideVariableDeclarationList() {
 Node rootNode = createNode("var a, b = 1, var c = 2;");

 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(null, rootNode);
 }

 private Node createNode(String code) {
 try {
 return compiler.parse(originalSourceFile, code, null, null);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 private String getCode(Node node) {
 return node.toString();
 }
}