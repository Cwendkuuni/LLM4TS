import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerTestCase {

  private AbstractCompiler compiler;

  @Override
  public void setUp() throws Exception {
    compiler = newAbstractCompilerWithSourceFiles(
        "InlineObjectLiteralsTest.java",
        "InlineObjectLiterals.java");
  }

  /**
   * Test a simple object literal assignment.
   */
  public void testSimpleObjectLiteralAssignment() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test1.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test1_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment that uses properties as keys.
   */
  public void testObjectLiteralAssignmentWithPropertiesAsKeys() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test2.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test2_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with multiple properties with the
   * same key name.
   */
  public void testObjectLiteralAssignmentWithMultiplePropertiesWithSameKeyName()
      throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test3.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test3_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a circular reference.
   */
  public void testObjectLiteralAssignmentWithCircularReference() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test4.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test4_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a circular reference that is
   * within an object property.
   */
  public void testObjectLiteralAssignmentWithCircularReferenceInProperty()
      throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test5.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test5_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with an unreachable property.
   */
  public void testObjectLiteralAssignmentWithUnreachableProperty() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test6.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test6_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a getter property.
   */
  public void testObjectLiteralAssignmentWithGetterProperty() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test7.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test7_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a setter property.
   */
  public void testObjectLiteralAssignmentWithSetterProperty() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test8.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test8_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a deleted property.
   */
  public void testObjectLiteralAssignmentWithDeletedProperty() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test9.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test9_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a property alias.
   */
  public void testObjectLiteralAssignmentWithPropertyAlias() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test10.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test10_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a property alias that leads to a
   * circular reference.
   */
  public void testObjectLiteralAssignmentWithCircularReferenceViaPropertyAlias()
      throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test11.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test11_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a property alias that leads to a
   * circular reference, but the alias is not used directly.
   */
  public void testObjectLiteralAssignmentWithCircularReferenceViaPropertyAliasIndirectly()
      throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test12.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test12_out.js"), getSourceCode(root));
  }

  /**
   * Test an object literal assignment with a variable that has been renamed
   * by the {@code RenameProperties} pass.
   */
  public void testObjectLiteralAssignmentWithRenamedVariable() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test13.js");
    CompilerPass pass1 = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass1.process(null, root);

    Node root2 = compileTestFile("InlineObjectLiteralsTest.test13_1.js");
    CompilerPass pass2 = new RenameProperties(compiler, newSafeNameIdSupplier());
    pass2.process(null, root2);
    pass1.process(null, root2);
    assertEquals(expectedTree("test13_out.js"), getSourceCode(root2));
  }

  /**
   * Test an object literal assignment with a variable that has been renamed
   * by the {@code RenameProperties} pass and is used within an object
   * initializer.
   */
  public void testObjectLiteralAssignmentWithRenamedVariableInObjectInitializer()
      throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test14.js");
    CompilerPass pass1 = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass1.process(null, root);

    Node root2 = compileTestFile("InlineObjectLiteralsTest.test14_1.js");
    CompilerPass pass2 = new RenameProperties(compiler, newSafeNameIdSupplier());
    pass2.process(null, root2);
    pass1.process(null, root2);
    assertEquals(expectedTree("test14_out.js"), getSourceCode(root2));
  }

  /**
   * Test a complex object literal assignment with multiple nested objects and
   * properties that are used as keys.
   */
  public void testComplexObjectLiteralAssignment() throws Exception {
    Node root = compileTestFile("InlineObjectLiteralsTest.test15.js");
    CompilerPass pass = new InlineObjectLiterals(compiler, newSafeNameIdSupplier());
    pass.process(null, root);
    assertEquals(expectedTree("test15_out.js"), getSourceCode(root));
  }

  private String getSourceCode(Node node) {
    SourceFile sourceFile = getSourceFile(node);
    return sourceFile.getSource();
  }

  private Node compileTestFile(String fileName) throws Exception {
    String[] files = {fileName};
    Node root = compileModules(files);
    return root;
  }

  private static Node newAbstractCompilerWithSourceFiles(
      String... sourceFiles) {
    return new CompilerTestCase().newCompiler().compile(sourceFiles);
  }

  private static Node newSafeNameIdSupplier() {
    return new Node() {};
  }

  private static ImmutableMap<String, String> expectedTree(String fileName) {
    String input = readInputFromResource("expect/" + fileName + ".js");
    return ImmutableMap.of("sourceCode", input);
  }
}