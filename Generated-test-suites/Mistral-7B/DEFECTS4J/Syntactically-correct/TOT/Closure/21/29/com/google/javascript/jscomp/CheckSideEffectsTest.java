import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CompilerInput input;
 private NodeFactory factory;

 @Before
 public void setUp() throws Exception {
 compiler = new AbstractCompiler();
 input = new CompilerInput();
 factory = new NodeFactory(input);
 }

 @Test
 public void testCheckSideEffects() throws Exception {
 // Test case 1: Simple expression without side effects
 String code1 = "function test1() {}";
 compileAndTest(code1);

 // Test case 2: Simple statement without side effects
 String code2 = "var x; x = 1;";
 compileAndTest(code2);

 // Test case 3: Statement with side effects
 String code3 = "var x = document.createElement('div');";
 compileAndTest(code3, WARNING);

 // Test case 4: Expression with side effects
 String code4 = "document.getElementById('test').innerHTML += 'Hello World';";
 compileAndTest(code4, WARNING);

 // Test case 5: Assignment with side effects
 String code5 = "var x = document.getElementById('test').innerHTML += 'Hello World';";
 compileAndTest(code5, WARNING);

 // Test case 6: Call to a function with side effects
 String code6 = "function test() { document.write('Hello World'); } test();";
 compileAndTest(code6, WARNING);

 // Test case 7: Call to a function without side effects
 String code7 = "function test() {} test();";
 compileAndTest(code7);

 // Test case 8: Assignment to a variable with JSDoc info
 String code8 = "/** @type {number} */ var x; x = 1;";
 compileAndTest(code8);

 // Test case 9: Complex expression tree without side effects
 String code9 = "var sum = (x = 1, y = 2, x + y);";
 compileAndTest(code9);

 // Test case 10: Expression tree with side effects
 String code10 = "var sum = (x = document.createElement('div'), y = 2, x.innerHTML += 'Hello World', x + y);";
 compileAndTest(code10, WARNING);
 }

 private void compileAndTest(String code, CheckLevel levelOptional) throws Exception {
 sourceToAST(code);
 compiler.setOptions(new CompilerOptions().setWarningLevel(levelOptional));
 Node root = compiler.process(input, null);
 NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, levelOptional, false));
 checkForWarnings(root);
 }

 private void sourceToAST(String code) throws Exception {
 SourceFile source = new SourceFile("test.js", code);
 input.setSourceRoot(".");
 input.setSourceFile(source);
 }

 private void checkForWarnings(Node node) {
 List<Node> warnings = node.getChildren();
 for (Node warning : warnings) {
 JSDocInfo warningInfo = warning.getJSDocInfo();
 if (warningInfo != null && warningInfo.getTagName().equals("JSC_USELESS_CODE")) {
 assertTrue(warning.isWarning());
 }
 }
 }
}