import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

 private static final String TEST_CODE =
         "function testFunction(a, b) {" +
         "  var x = a;" +
         "  var y = b;" +
         "  var c = \"test\";" +
         "  var d = function() {}" +
         "  var e = function testFunction2(x, y) {}" +
         "  var f = {g: 1};" +
         "  var g = {h: 1, i: {j: 1}};" +
         "}" +
         "testFunction(1, 2);";

 @Test
 public void testMakeDeclaredNamesUnique() {
 Node rootNode = new TokenStream(TEST_CODE).parseProgram();

 // Test with empty MakeDeclaredNamesUnique instance
 NodeTraversal traversal = new NodeTraversal(rootNode);
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.traverse(traversal);
 assertEquals(TEST_CODE, traversal.getSourceCode());

 // Test with MakeDeclaredNamesUnique instance with ContextualRenamer
 MakeDeclaredNamesUnique makeDeclaredNamesUniqueContextual =
         new MakeDeclaredNamesUnique(new ContextualRenamer());
 makeDeclaredNamesUniqueContextual.traverse(traversal);
 assertEquals(getExpectedRenamedCode(), traversal.getSourceCode());

 // Test with MakeDeclaredNamesUnique instance with InlineRenamer
 MakeDeclaredNamesUnique makeDeclaredNamesUniqueInline =
         new MakeDeclaredNamesUnique(
             new InlineRenamer(() -> String.valueOf(System.nanoTime()), "", true));
 makeDeclaredNamesUniqueInline.traverse(traversal);
 assertEquals(getExpectedRenamedCodeWithInlineRenamer(), traversal.getSourceCode());
 }

 private String getExpectedRenamedCode() {
 // Expected output of ContextualRenamer
 return "function testFunction(a, b) {" +
         "  var x_0 = a;" +
         "  var y_1 = b;" +
         "  var c_2 = \"test\";" +
         "  var d_3 = function() {};" +
         "  var e_4 = function testFunction2(x_5, y_6) {}" +
         "  var f_7 = {g: 1};" +
         "  var g_8 = {h: 1, i: {j: 1}};" +
         "}";
 }

 private String getExpectedRenamedCodeWithInlineRenamer() {
 // Expected output of InlineRenamer
 return "function testFunction(a, b) {" +
         "  var x__Google_jscomp_0 = a;" +
         "  var y__Google_jscomp_1 = b;" +
         "  var c__Google_jscomp_2 = \"test\";" +
         "  var d__Google_jscomp_3 = function() {};" +
         "  var e__Google_jscomp_4 = function testFunction2(x__Google_jscomp_5, y__Google_jscomp_6) {}" +
         "  var f__Google_jscomp_7 = {g: 1};" +
         "  var g__Google_jscomp_8 = {h: 1, i: {j: 1}};" +
         "}" +
         "testFunction(1, 2);";
 }

}