import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineObjectLiteralsTest {

 private Compiler compiler;
 private CompilerPass inlineObjectLiterals;

 @Before
 public void setup() {
 compiler = new Compiler();
 inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tempVar");
 }

 /**
 * Test for basic functionality of InlineObjectLiterals class
 */
 @Test
 public void testInlineObjectLiteralsBasic() {
 String input = "var obj = {a: 1, b: 2};";
 String expectedOutput = "var a = 1,\n" +
 "         b = 2,\n" +
 "         tempVar_a = 1,\n" +
 "         tempVar_b = 2,\n" +
 "         tempVar_obj = {\n" +
 "           a: tempVar_a,\n" +
 "           b: tempVar_b\n" +
 "         }";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with only one property
 */
 @Test
 public void testInlineObjectLiteralsOneProperty() {
 String input = "var obj = {a: 1};";
 String expectedOutput = "var a = 1,\n" +
 "         tempVar_a = 1,\n" +
 "         tempVar_obj = {a: tempVar_a}";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with initial value assignment
 */
 @Test
 public void testInlineObjectLiteralsInitialValueAssignment() {
 String input = "var obj = {a: 1, b: function() {}}";
 String expectedOutput = "var a = 1,\n" +
 "         b = function () {},\n" +
 "         tempVar_a = 1,\n" +
 "         tempVar_b = function () {},\n" +
 "         tempVar_obj = {\n" +
 "           a: tempVar_a,\n" +
 "           b: tempVar_b\n" +
 "         }";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with getter and setter properties
 */
 @Test
 public void testInlineObjectLiteralsGetterSetterProperties() {
 String input = "var obj = {\n" +
 "  get getA() { return 1; },\n" +
 "  set setA(value) { console.log('setA: ', value); },\n" +
 "  get getB() { return 2; },\n" +
 "  set setB(value) { console.log('setB: ', value); },\n" +
 "  a: 3,\n" +
 "  b: 4\n" +
 "};";
 String expectedOutput = "var getA = function () {\n" +
 "  return 1;\n" +
 "},\n" +
 "setA = function (value) {\n" +
 "  console.log('setA: ', value);\n" +
 "},\n" +
 "var getB = function () {\n" +
 "  return 2;\n" +
 "},\n" +
 "setB = function (value) {\n" +
 "  console.log('setB: ', value);\n" +
 "},\n" +
 "var a = 3,\n" +
 "var b = 4,\n" +
 "tempVar_getA = function () {\n" +
 "  return 1;\n" +
 "},\n" +
 "tempVar_setA = function (value) {\n" +
 "  console.log('setA: ', value);\n" +
 "},\n" +
 "tempVar_getB = function () {\n" +
 "  return 2;\n" +
 "},\n" +
 "tempVar_setB = function (value) {\n" +
 "  console.log('setB: ', value);\n" +
 "},\n" +
 "tempVar_obj = {\n" +
 "  get getA: tempVar_getA,\n" +
 "  set setA: tempVar_setA,\n" +
 "  get getB: tempVar_getB,\n" +
 "  set setB: tempVar_setB,\n" +
 "  a: a,\n" +
 "  b: b\n" +
 "}";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with nested object literal
 */
 @Test
 public void testInlineObjectLiteralsNestedObjectLiteral() {
 String input = "var obj = {a: {b: 1}};";
 String expectedOutput = "var tempVar_obj = {\n" +
 "  a: {\n" +
 "    b: 1\n" +
 "  }\n" +
 "};\n" +
 "var a = tempVar_obj;\n" +
 "tempVar_obj = {\n" +
 "  a: {\n" +
 "    b: 1\n" +
 "  }\n" +
 "};";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with multiple assignments
 */
 @Test
 public void testInlineObjectLiteralsMultipleAssignments() {
 String input = "var obj = {\n" +
 "  a: 1,\n" +
 "  b: 2,\n" +
 "  c: 3,\n" +
 "  d: 4\n" +
 "};\n" +
 "var obj2 = {\n" +
 "  a: 5,\n" +
 "  b: 6,\n" +
 "  c: 7,\n" +
 "  d: 8\n" +
 "};\n" +
 "obj = obj2;";
 String expectedOutput = "var a_obj = {\n" +
 "  a: 5,\n" +
 "  b: 6,\n" +
 "  c: 7,\n" +
 "  d: 8\n" +
 "};\n" +
 "var a = a_obj.a,\n" +
 "b = a_obj.b,\n" +
 "c = a_obj.c,\n" +
 "d = a_obj.d,\n" +
 "tempVar_a_obj = a_obj,\n" +
 "a = a_obj.a,\n" +
 "b = a_obj.b,\n" +
 "c = a_obj.c,\n" +
 "d = a_obj.d,\n" +
 "obj = tempVar_a_obj";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with variable name collision
 */
 @Test
 public void testInlineObjectLiteralsVariableNameCollision() {
 String input = "var obj = {a: 1, a: 2};";
 String expectedOutput = "var a_1 = 1,\n" +
 "         a_2 = 2,\n" +
 "         tempVar_a_1 = a_1,\n" +
 "         tempVar_a_2 = a_2,\n" +
 "         tempVar_obj = {\n" +
 "           a_1: tempVar_a_1,\n" +
 "           a_2: tempVar_a_2\n" +
 "         }";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with property name collision
 */
 @Test
 public void testInlineObjectLiteralsPropertyNameCollision() {
 String input = "var obj = {a: {b: 1, b: 2}};";
 String expectedOutput = "var tempVar_obj = {\n" +
 "  a: {\n" +
 "    b_1: 1,\n" +
 "    b_2: 2\n" +
 "  }\n" +
 "};\n" +
 "var a = tempVar_obj.a;\n" +
 "tempVar_obj = {\n" +
 "  a: {\n" +
 "    b_1: 1,\n" +
 "    b_2: 2\n" +
 "  }\n" +
 "};";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 String output = NodePrinter.printNodeToSource(root);

 assertEquals(expectedOutput, output);
 }

 /**
 * Test for object literal with missing property
 */
 @Test(expected = java.lang.RuntimeException.class)
 public void testInlineObjectLiteralsMissingProperty() {
 String input = "var obj = {a: 1};";
 String expectedErrorMessage = "Reference 'b' is not defined.";

 Node root = NodePrinter.parseJs(input);
 inlineObjectLiterals.process(NodePrinter.getExterns(root), root);
 inlineObjectLiterals.process(root, root); // Force error to be thrown

 assertEquals(expectedErrorMessage,
 compiler.getCodingConvention().getLastReportedErrorMessage());
 }
}