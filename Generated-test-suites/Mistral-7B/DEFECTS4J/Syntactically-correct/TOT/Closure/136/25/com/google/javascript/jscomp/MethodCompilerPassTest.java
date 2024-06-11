import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import java.util.List;

public class MethodCompilerPassTest {

  private static final String TEST_SOURCE = "testSource.js";

  @org.junit.Test
  public void testSimpleMethod() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new SimpleMethodCompilerPass(compiler);

    Node root = createTestNode("function foo() {}");

    pass.process(null, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    List<Node> methodNames = Lists.newArrayList(methodDefinitions.keySet().toArray(new String[0]));

    assertEquals(1, methodDefinitions.size());
    assertEquals("foo", methodNames.get(0));
    assertNull(signatureStore.getSignatureFor("nonExistentFunction"));
    assertSame(methodDefinitions.get("foo").get(0), getFunctionNode("foo"));
  }

  @org.junit.Test
  public void testMultipleMethods() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MultipleMethodsCompilerPass(compiler);

    Node root = createTestNode("function foo() {} function bar() {}");

    pass.process(null, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    List<Node> methodNames = Lists.newArrayList(methodDefinitions.keySet().toArray(new String[0]));

    assertEquals(2, methodDefinitions.size());
    assertEquals("foo", methodNames.get(0));
    assertEquals("bar", methodNames.get(1));
    assertNull(signatureStore.getSignatureFor("nonExistentFunction"));
    assertSame(methodDefinitions.get("foo").get(0), getFunctionNode("foo"));
    assertSame(methodDefinitions.get("bar").get(0), getFunctionNode("bar"));
  }

  @org.junit.Test
  public void testExternMethod() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new ExternMethodCompilerPass(compiler);

    Node externs = createTestNode("function foo() {}");
    Node root = createTestNode("function bar() {}");

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    List<Node> methodNames = Lists.newArrayList(methodDefinitions.keySet().toArray(new String[0]));

    assertEquals(1, methodDefinitions.size());
    assertEquals("foo", methodNames.get(0));
    assertNull(signatureStore.getSignatureFor("bar"));
    assertSame(methodDefinitions.get("foo").get(0), getFunctionNode("foo"));
  }

  @org.junit.Test
  public void testExternMethodWithoutSignature() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new ExternMethodWithoutSignaturePass(compiler);

    Node externs = createTestNode("function foo() {}");
    Node root = createTestNode("function bar() {}");

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    List<Node> methodNames = Lists.newArrayList(methodDefinitions.keySet().toArray(new String[0]));

    assertEquals(1, methodDefinitions.size());
    assertEquals("foo", methodNames.get(0));
    assertNull(signatureStore.getSignatureFor("foo"));
    assertSame(methodDefinitions.get("foo").get(0), getFunctionNode("foo"));
    assertFalse(methodDefinitions.containsKey("bar"));
  }

  @org.junit.Test
  public void testNonMethodProperty() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    Node root = createTestNode("var foo = 42;");

    pass.process(null, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    List<String> nonMethodProperties = Lists.newArrayList(pass.nonMethodProperties);

    assertEquals(0, methodDefinitions.size());
    assertEquals("foo", nonMethodProperties.get(0));
    assertNull(signatureStore.getSignatureFor("nonExistentFunction"));
  }

  @org.junit.Test
  public void testPrototype() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new PrototypeMethodCompilerPass(compiler);

    Node root = createTestNode("function Foo() {} Foo.prototype.bar = function() {}");

    pass.process(null, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    List<Node> methodNames = Lists.newArrayList(methodDefinitions.keySet().toArray(new String[0]));

    assertEquals(1, methodDefinitions.size());
    assertEquals("bar", methodNames.get(0));
    assertNull(signatureStore.getSignatureFor("nonExistentFunction"));
    assertSame(methodDefinitions.get("bar").get(0), getFunctionNode("Foo.prototype.bar"));
  }

  private Node getFunctionNode(String name) {
    return new Node() {
      @Override
      public Token getType() {
        return Token.FUNCTION;
      }

      @Override
      public String getString() {
        return name;
      }
    };
  }

  private Node createTestNode(String jsCode) {
    AbstractCompiler compiler = new Compiler();
    NodeAST nodeAST = compiler.parse(jsCode, TEST_SOURCE, null);
    return nodeAST.getRootNode();
  }
}

class SimpleMethodCompilerPass extends MethodCompilerPass {
  SimpleMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  SignatureStore getSignatureStore() {
    return new SignatureStore() {
      private Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void reset() {
      }

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
      }

      public Node getSignatureFor(String functionName) {
        return signatures.get(functionName).get(0);
      }
    };
  }

  @Override
  Callback getActingCallback() {
    return new Callback() {
      @Override
      public void call(NodeTraversal t, Node n, Node parent) {
        // no-op
      }
    };
  }
}

class MultipleMethodsCompilerPass extends MethodCompilerPass {
  MultipleMethodsCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    return new Callback() {
      @Override
      public void call(NodeTraversal t, Node n, Node parent) {
        // no-op
      }
    };
  }
}

class ExternMethodCompilerPass extends MethodCompilerPass {
  ExternMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    return new Callback() {
      @Override
      public void call(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION && parent != null) {
          addPossibleSignature(n.getString(), n, t);
        }
      }
    };
  }
}

class ExternMethodWithoutSignaturePass extends MethodCompilerPass {
  ExternMethodWithoutSignaturePass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    return new Callback() {
      @Override
      public void call(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION && parent != null) {
          String name = n.getString();
          if (!externMethods.contains(name)) {
            externMethodsWithoutSignatures.add(name);
          }
        }
      }
    };
  }
}

class PrototypeMethodCompilerPass extends MethodCompilerPass {
  PrototypeMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    return new Callback() {
      @Override
      public void call(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.GETPROP && parent != null) {
          String name = n.getString();
          if (name.equals("prototype")) {
            processPrototypeParent(t, parent);
          }
        }
      }
    };
  }

  private void processPrototypeParent(NodeTraversal t, Node n) {
    if (n.getType() == Token.GETPROP || n.getType() == Token.GETELEM) {
      Node dest = n.getFirstChild().getNext();

      if (dest.getType() == Token.STRING && n.getParent().getType() == Token.ASSIGN) {
        Node assignee = n.getParent().getFirstChild().getNext();
        addPossibleSignature(dest.getString(), assignee, t);
      }
    }
  }
}