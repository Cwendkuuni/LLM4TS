import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.testing.BaseGeneratorTest;
import static com.google.common.truth.Truth.assertThat;

public class VarCheckTest extends BaseGeneratorTest {

 private Compiler compiler;
 private VarCheck varCheck;

 @Before
 public void setUp() {
 compiler = new TestCompiler(new CompilerOptions());
 compiler.addPass(new VarCheck(compiler));
 }

 @Test
 public void testValidVariableDeclaration() {
 Node root = createSource("function foo() { var x; }");
 NodeTraversal traversal = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 assertThat(compiler.getErrors()).isEmpty();
 }

 @Test
 public void testUndefinedVariable() {
 Node root = createSource("function foo() { var x; console.log(x); }");
 NodeTraversal traversal = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 assertThat(compiler.getErrors()).hasSize(1);
 }

 @Test
 public void testModuleDependency() {
 String module1 = "function foo() {}";
 String module2 = "function bar() { \n" +
     "  import { foo } from './module1'; \n" +
     "  function baz() { console.log(foo); } \n" +
     "}";
 Node root = createSource(module1 + "\n" + module2);
 NodeTraversal traversal = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 assertThat(compiler.getErrors()).isEmpty();

 root = createSource(module2);
 NodeTraversal traversal2 = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 assertThat(compiler.getErrors()).hasSize(1);
 }

 @Test
 public void testUndefinedExternVariable() {
 Node root = createSource("/** @suppress {unused} */\n" +
     "function foo() { console.log(EXPORTED_VAR); }");
 NodeTraversal traversal = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 assertThat(compiler.getErrors()).hasSize(1);
 }

 @Test
 public void testStrictModuleDependency() {
 String module1 = "function foo() {}";
 String module2 = "function bar() { \n" +
     "  import { foo } from './module1'; \n" +
     "}";
 Node root = createSource(module1 + "\n" + module2);
 NodeTraversal traversal = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 varCheck.strictExternCheck = true;
 varCheck.process(null, root);
 assertThat(compiler.getErrors()).isEmpty();

 root = createSource(module2);
 NodeTraversal traversal2 = NodeTraversal.create(compiler, root);
 varCheck.process(null, root);
 varCheck.strictExternCheck = true;
 assertThat(compiler.getErrors()).isEmpty();
 }
}