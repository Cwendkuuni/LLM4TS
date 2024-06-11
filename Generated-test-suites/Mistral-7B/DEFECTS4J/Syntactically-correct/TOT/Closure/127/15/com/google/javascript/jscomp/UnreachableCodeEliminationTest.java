import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.test.BaseGeneratorTest;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UnreachableCodeEliminationTest extends BaseGeneratorTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = newCompiler();
    options = new CompilerOptions();
  }

  @Test
  public void testRemoveDeadCodeAfterReturn() {
    String testCode = "function foo() {" +
                      "  if (true) {" +
                      "    return;" +
                      "    alert('unreachable');" +
                      "  }" +
                      "  return 42;" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    pass.process(node, null);
    String expectedCode = "function foo() {" +
                         "  return 42;" +
                         "}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testRemoveDeadCodeWithoutSideEffects() {
    String testCode = "function foo() {" +
                      "  a.b.MyClass.prototype.propertyName;" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    // The test should not change the output, since there is no code change.
  }

  @Test
  public void testRemoveReturnStatementWithSideEffects() {
    String testCode = "function foo() {" +
                      "  if (true) {" +
                      "    return 42;" +
                      "  }" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {" +
                         "  return 42;" +
                         "}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testRemoveBreakStatement() {
    String testCode = "function foo() {" +
                      "  for (var i = 0; i < 5; i++) {" +
                      "    if (i === 3) {" +
                      "      break;" +
                      "    }" +
                      "  }" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {" +
                         "  for (var i = 0; i < 5; i++) {" +
                         "  }" +
                         "}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testRemoveContinueStatement() {
    String testCode = "function foo() {" +
                      "  for (var i = 0; i < 5; i++) {" +
                      "    if (i === 2) {" +
                      "      continue;" +
                      "    }" +
                      "    if (i === 3) {" +
                      "      return;" +
                      "    }" +
                      "  }" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {" +
                         "  for (var i = 0; i < 5; i++) {" +
                         "    if (i === 3) {" +
                         "      return;" +
                         "    }" +
                         "  }" +
                         "}" +
                         "function foo$1() {" +
                         "  for (var i = 0; i < 5; i++) {" +
                         "  }" +
                         "}";
    String actualCode = getSourceCode(node);
    String expectedCodeWithNewFunction = "function foo() {" +
                                        "  for (var i = 0; i < 5; i++) {" +
                                        "  }" +
                                        "}" +
                                        "function foo$1() {" +
                                        "  for (var i = 0; i < 5; i++) {" +
                                        "    if (i === 3) {" +
                                        "      return;" +
                                        "    }" +
                                        "  }" +
                                        "}";
    assertThat(actualCode).isEqualTo(expectedCodeWithNewFunction);
  }

  @Test
  public void testRemoveEmptyBlock() {
    String testCode = "function foo() {" +
                      "  {}" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testRemoveEmptyBlockWithSideEffects() {
    String testCode = "function foo() {" +
                      "  {}" +
                      "  return 42;";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {return 42;}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testRemoveDeadCodeInsideTryCatchBlock() {
    String testCode = "function foo() {" +
                      "  try {" +
                      "    throw new Error('test');" +
                      "  } catch (e) {" +
                      "    if (false) {" +
                      "      return;" +
                      "    }" +
                      "    throw e;" +
                      "  }" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {" +
                         "  try {" +
                         "    throw new Error('test');" +
                         "  } catch (e) {" +
                         "    throw e;" +
                         "  }" +
                         "}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  @Test
  public void testRemoveDeadCodeInsideTryFinallyBlock() {
    String testCode = "function foo() {" +
                      "  try {" +
                      "    return 42;" +
                      "  } finally {" +
                      "    if (false) {" +
                      "      return;" +
                      "    }" +
                      "  }" +
                      "}";
    Node node = parseTree(testCode, options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    pass.process(node, null);
    String expectedCode = "function foo() {" +
                         "  try {" +
                         "    return 42;" +
                         "  } finally {}" +
                         "}";
    String actualCode = getSourceCode(node);
    assertThat(actualCode).isEqualTo(expectedCode);
  }
}