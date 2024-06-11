import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import org.junit.Test;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() {
    // Create a mock AbstractCompiler for testing
    AbstractCompiler mockCompiler = new AbstractCompiler() {
      @Override
      public String getSourceName() {
        return "testFile.js";
      }
    };

    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(mockCompiler);

    // Test with empty Node (externs = null)
    methodCompilerPass.process(null, null);
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());

    // Test with simple externs file
    Node externsNode = parseJs("externs = {" +
        "  'Foo.prototype.bar = function() {}'," +
        "  'Foo.prototype.baz = function() {}'," +
        "  'Foo.prototype.qux = function() {}'" +
        "};");
    methodCompilerPass.process(externsNode, null);
    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(3, methodCompilerPass.methodDefinitions.size());

    // Test with source file containing static methods
    Node sourceNode = parseJs("" +
        "Foo.bar = function() {" +
        "  // implementation of bar" +
        "}" +
        "Foo.baz = function(x) {" +
        "  // implementation of baz" +
        "}" +
        "Foo.qux = function(x, y) {" +
        "  // implementation of qux" +
        "}");
    List<Node> roots = Lists.newArrayList(externsNode, sourceNode);
    methodCompilerPass.process(null, roots.toArray(new Node[0]));
    assertEquals(6, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(6, methodCompilerPass.methodDefinitions.size());

    // Test with source file defining prototype methods
    sourceNode = parseJs("" +
        "Foo.prototype.bar = function() {" +
        "  // implementation of bar" +
        "}" +
        "Foo.prototype.baz = function(x) {" +
        "  // implementation of baz" +
        "}" +
        "Foo.prototype.qux = function(x, y) {" +
        "  // implementation of qux" +
        "}");
    roots = Lists.newArrayList(externsNode, sourceNode);
    methodCompilerPass.process(null, roots.toArray(new Node[0]));
    assertEquals(6, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(6, methodCompilerPass.methodDefinitions.size());
  }
}