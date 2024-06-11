import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassWithoutSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

 private Compiler passCompiler;
 private DiagnosticGroup diagnostics;

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up the compiler
 AbstractCompiler compiler = new Compiler();
 compiler.addPasses(new CompilerPassWithoutSideEffects[] {
 new VarCheck(compiler)
 });
 passCompiler = compiler;
 }

 @Before
 public void setUp() {
 // Clear any previous diagnostics
 diagnostics = new DiagnosticGroup();
 }

 @Test
 public void testUndefinedVariable() {
 Node ast = NodeFactory.createIntLiteral(1);
 CompilerInput input = new CompilerInput("test.js", "test", null, null, ast);

 NodeTraversal traversal = NodeTraversal.create(passCompiler, input, null);
 VarCheck varCheck = (VarCheck) traversal.getCallback();
 traversal.traverse(null, ast, null);

 assertEquals(1, varCheck.getDiagnostics().size());
 DiagnosticType diagnosticType = varCheck.getDiagnostics().get(0).getType();
 String message = varCheck.getDiagnostics().get(0).getMessage(null);
 assertEquals(DiagnosticType.error, diagnosticType);
 assertEquals("variable undefinedVariable is undefined", message);
 }

 @Test
 public void testFunctionDeclarationWithEmptyName() {
 Node functionNode = NodeFactory.createFunctionDeclaration(
 "anonymousFunction", NodeFactory.createList(NodeFactory.createName("arg0")),
 NodeFactory.createBlock(NodeFactory.createEmptyStatementList()));
 Node inputRoot = NodeFactory.createProgram(NodeFactory.createList(functionNode), null);

 CompilerInput input = new CompilerInput("test.js", "test", null, null, inputRoot);
 NodeTraversal traversal = NodeTraversal.create(passCompiler, input, null);
 VarCheck varCheck = (VarCheck) traversal.getCallback();
 traversal.traverse(null, inputRoot, null);

 assertTrue(varCheck.getDiagnostics().isEmpty());
 }

 @Test
 public void testVariableInDifferentFile() {
 String inputFile1 = "file1.js";
 String inputFile2 = "file2.js";

 Node file1Ast = NodeFactory.createIntLiteral(1);
 CompilerInput file1Input = new CompilerInput(inputFile1, inputFile1, null, null, file1Ast);
 Node file2Ast = NodeFactory.createIntLiteral(2);
 CompilerInput file2Input = new CompilerInput(inputFile2, inputFile2, null, null, file2Ast);

 CompilerInput rootInput = new CompilerInput("test.js", "test", null, null, null);
 CompilerInput rootCompilerInput = NodeFactory.createProgram(
 NodeFactory.createList(file1Input.getAstRoot(passCompiler), file2Input.getAstRoot(passCompiler)), null);

 NodeTraversal traversal = NodeTraversal.create(passCompiler, rootCompilerInput, null);
 VarCheck varCheck = (VarCheck) traversal.getCallback();
 traversal.traverse(null, rootInput.getAstRoot(passCompiler), null);

 assertEquals(1, varCheck.getDiagnostics().size());
 DiagnosticType diagnosticType = varCheck.getDiagnostics().get(0).getType();
 String message = varCheck.getDiagnostics().get(0).getMessage(null);
 assertEquals(DiagnosticType.error, diagnosticType);
 assertEquals("module file1.js cannot reference file2.js, defined in module test.js", message);
 }

 @Test
 @Ignore("TODO: figure out how to test externs properly")
 public void testModuleDependency() {
 // TODO: write test for module dependency
 }

 @Test
 public void testDuplicateVariableDeclaration() {
 Node varNode1 = NodeFactory.createName("myVar");
 Node varNode2 = NodeFactory.createName("myVar");
 Node inputRoot = NodeFactory.createProgram(NodeFactory.createList(varNode1, varNode2), null);

 CompilerInput input = new CompilerInput("test.js", "test", null, null, inputRoot);
 NodeTraversal traversal = NodeTraversal.create(passCompiler, input, null);
 VarCheck varCheck = (VarCheck) traversal.getCallback();
 traversal.traverse(null, inputRoot, null);

 assertFalse(varCheck.getDiagnostics().isEmpty());
 }

 @Test
 public void testUndefinedExternVariable() {
 Node externsAst = NodeFactory.createProgram(NodeFactory.createList(
 NodeFactory.createVarDeclList(NodeFactory.createList(
 NodeFactory.createName("undefinedExternVariable")))), null);
 CompilerInput input = new CompilerInput("test.js", "test", null, null, NodeFactory.createProgram(NodeFactory.createList(
 NodeFactory.createImportDeclaration(null, NodeFactory.createName("test"), externsAst),
 NodeFactory.createIntLiteral(1)), null));
 NodeTraversal traversal = NodeTraversal.create(passCompiler, input, null);
 VarCheck varCheck = (VarCheck) traversal.getCallback();
 traversal.traverse(null, input.getAstRoot(passCompiler), null);

 assertEquals(1, varCheck.getDiagnostics().size());
 DiagnosticType diagnosticType = varCheck.getDiagnostics().get(0).getType();
 String message = varCheck.getDiagnostics().get(0).getMessage(null);
 assertEquals(DiagnosticType.warning, diagnosticType);
 assertEquals("name undefinedExternVariable is not undefined in the externs.", message);
 }

 @Test
 public void testVariableNameReferenceInExterns() {
 Node externsAst = NodeFactory.createProgram(NodeFactory.createList(
 NodeFactory.createVarDeclList(NodeFactory.createList(
 NodeFactory.createName("myVar")))), null);
 Node inputAst = NodeFactory.createProgram(NodeFactory.createList(
 NodeFactory.createImportDeclaration(null, NodeFactory.createName("test"), externsAst),
 NodeFactory.createGetProp(NodeFactory.createName("myVar"), null)), null);

 CompilerInput input = new CompilerInput("test.js", "test", null, null, inputAst);
 NodeTraversal traversal = NodeTraversal.create(passCompiler, input, null);
 VarCheck varCheck = (VarCheck) traversal.getCallback();
 traversal.traverse(null, input.getAstRoot(passCompiler), null);

 assertTrue(varCheck.getDiagnostics().isEmpty());
 }
}