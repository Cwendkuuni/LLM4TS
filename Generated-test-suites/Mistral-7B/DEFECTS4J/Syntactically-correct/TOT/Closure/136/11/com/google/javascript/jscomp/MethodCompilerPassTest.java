import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
  private MethodCompilerPass pass;
  private SignatureStore signatureStore;
  private Set<String> externMethods;
  private Set<String> externMethodsWithoutSignatures;
  private Set<String> nonMethodProperties;
  private Multimap<String, Node> methodDefinitions;

  @Before
  public void setup() {
    pass = new MyMethodCompilerPass(new Compiler());
    signatureStore = mock(SignatureStore.class);
    pass.setSignatureStore(signatureStore);
    externMethods = Sets.newHashSet();
    externMethodsWithoutSignatures = Sets.newHashSet();
    nonMethodProperties = Sets.newHashSet();
    methodDefinitions = HashMultimap.create();
  }

  @Test
  public void testProcessExterns() {
    Node externs = createNode("externs: {\n" +
        "  MyClass: {\n" +
        "    methods: {\n" +
        "      myMethod: function() {}\n" +
        "    }\n" +
        "  },\n" +
        "  AnotherClass: {\n" +
        "    methods: {\n" +
        "      anotherMethod: function(a, b) {}\n" +
        "    }\n" +
        "  }\n" +
        "}");

    pass.process(externs, null);

    // assert method definitions were collected correctly
    assertEquals(2, methodDefinitions.size());
    assertEquals("myMethod", methodDefinitions.keys().iterator().next());
    assertEquals("anotherMethod", methodDefinitions.keys().iterator().next().iterator().next().getString());

    // assert extern methods were collected correctly
    assertEquals(2, externMethods.size());
    assertEquals("MyClass.methods.myMethod", externMethods.iterator().next());
    assertEquals("AnotherClass.methods.anotherMethod", externMethods.iterator().next());

    // assert extern methods without signatures were collected correctly
    assertTrue(externMethodsWithoutSignatures.isEmpty());

    // assert non-method properties were collected correctly
    assertTrue(nonMethodProperties.isEmpty());
  }

  @Test
  public void testProcessExternsWithMissingSignatures() {
    Node externs = createNode("externs: {\n" +
        "  MyClass: {\n" +
        "    methods: {\n" +
        "      myMethod: function() {}\n" +
        "    }\n" +
        "  },\n" +
        "  AnotherClass: {\n" +
        "    methods: {\n" +
        "      anotherMethod: null\n" +
        "    }\n" +
        "  }\n" +
        "}");

    pass.process(externs, null);

    // assert method definitions were collected correctly
    assertEquals(1, methodDefinitions.size());

    // assert extern methods were collected correctly
    assertEquals(1, externMethods.size());
    assertEquals("MyClass.methods.myMethod", externMethods.iterator().next());

    // assert extern method without signature was ignored
    assertEquals(0, externMethodsWithoutSignatures.size());

    // assert non-method properties were collected correctly
    assertTrue(nonMethodProperties.isEmpty());
  }

  @Test
  public void testProcessRoots() {
    Node root = createNode("function MyClass() {\n" +
        "  this.myMethod = function() {}\n" +
        "  this.prototype.anotherMethod = function(a, b) {}\n" +
        "}");

    pass.process(null, root);

    // assert method definitions were collected correctly
    assertEquals(2, methodDefinitions.size());
    assertEquals("myMethod", methodDefinitions.keys().iterator().next());
    assertEquals("anotherMethod", methodDefinitions.keys().iterator().next().iterator().next().getString());

    // assert extern methods were not collected
    assertEquals(0, externMethods.size());

    // assert extern methods without signatures were not collected
    assertTrue(externMethodsWithoutSignatures.isEmpty());

    // assert non-method properties were collected correctly
    assertTrue(nonMethodProperties.isEmpty());
  }

  @Test
  public void testProcessRootsWithDuplicateFunctionName() {
    Node root = createNode("function MyClass() {\n" +
        "  this.myMethod = function() {}\n" +
        "  this.myMethod = function() {}\n" +
        "}");

    pass.process(null, root);

    // assert method definitions were collected correctly
    assertEquals(1, methodDefinitions.size());
    assertEquals("myMethod", methodDefinitions.keys().iterator().next());

    // assert extern methods were not collected
    assertEquals(0, externMethods.size());

    // assert extern methods without signatures were not collected
    assertTrue(externMethodsWithoutSignatures.isEmpty());

    // assert non-method properties were not collected
    assertTrue(nonMethodProperties.isEmpty());
  }

  @Test
  public void testProcessRootsWithPrototypeAssignment() {
    Node root = createNode("function MyClass() {}\n" +
        "MyClass.prototype = {\n" +
        "  myMethod: function() {}\n" +
        "}");

    pass.process(null, root);

    // assert method definitions were collected correctly
    assertEquals(1, methodDefinitions.size());
    assertEquals("myMethod", methodDefinitions.keys().iterator().next());

    // assert extern methods were not collected
    assertEquals(0, externMethods.size());

    // assert extern methods without signatures were not collected
    assertTrue(externMethodsWithoutSignatures.isEmpty());

    // assert non-method properties were not collected
    assertTrue(nonMethodProperties.isEmpty());
  }

  @Test
  public void testProcessRootsWithPrototypeFunction() {
    Node root = createNode("function MyClass() {}\n" +
        "MyClass.prototype = function() {}\n");

    pass.process(null, root);

    // assert method definitions were not collected
    assertEquals(0, methodDefinitions.size());

    // assert extern methods were not collected
    assertEquals(0, externMethods.size());

    // assert extern methods without signatures were not collected
    assertTrue(externMethodsWithoutSignatures.isEmpty());

    // assert non-method properties were not collected
    assertTrue(nonMethodProperties.isEmpty());
  }

  private Node createNode(String js) {
    return new RhinoCompiler().parse(js, null, null).getRootNode();
  }
}

class MyMethodCompilerPass extends MethodCompilerPass {
  // empty implementation for testing purposes
}