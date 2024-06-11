import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.HashMultimap;
import com.google.javascript.jscomp.Lists;
import com.google.javascript.jscomp.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

 private static class TestCompiler extends AbstractCompiler {
  @Override
  public SignatureStore getSignatureStore() {
    return new SignatureStore() {
      Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void reset() {
      }

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
        signatures.remove(functionName);
      }

      public Node getFunctionByName(String functionName) {
        return signatures.get(functionName).get(0);
      }
    };
  }

  @Override
  public MethodCompilerPass createPass(Node externs, Node root) {
    return new TestMethodCompilerPass(this);
  }
 }

 @Test
 public void testMethodCompilerPass() {
   Node externs = null;
   Node root = Node.newJsonObject();
   TestCompiler compiler = new TestCompiler();
   MethodCompilerPass pass = compiler.createPass(externs, root);
   pass.process(externs, root);

   // test that extern methods without signatures are ignored
   assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
   assertEquals(2, pass.externMethodsWithoutSignatures.size());
   assertFalse(pass.externMethods.isEmpty());
   assertEquals(3, pass.externMethods.size());

   Node foo = Node.newFunction("foo", null);
   assertNull(pass.getSignatureStore().getFunctionByName("foo"));

   Node externFoo = Node.newName("Foo");
   Node externMethodsNode = Node.newObjectLiteral();
   Node fooSignature = Node.newObjectLiteral();
   fooSignature.setProperty("name", Node.newName("foo"));
   fooSignature.setProperty("value", foo);
   externMethodsNode.setProperty("foo", fooSignature);
   externFoo.setProperty("methods", externMethodsNode);
   externs = Node.newObjectLiteral();
   externs.setProperty("Foo", externFoo);

   pass.process(externs, root);

   assertTrue(pass.externMethods.contains("Foo.foo"));
   assertEquals(1, pass.methodDefinitions.get("Foo.foo").size());
   Node fooMethod = pass.methodDefinitions.get("Foo.foo").get(0);
   assertEquals(foo, fooMethod);

   // test that methods are added to methodDefinitions
   Node bar = Node.newFunction("bar", null);
   root.setProperty("bar", bar);
   pass.process(externs, root);

   assertTrue(pass.methodDefinitions.get("bar").isEmpty());

   // test that getters are handled correctly
   Node getter = Node.newFunction("getter", new Node[]{Node.newName("obj")}, Node.newName("value"));
   root.setProperty("getter", getter);
   Node obj = Node.newObjectLiteral();
   obj.setProperty("value", Node.newNumber(42));
   root.setProperty("obj", obj);
   pass.process(externs, root);

   assertEquals(1, pass.methodDefinitions.get("getter").size());
   Node getterMethod = pass.methodDefinitions.get("getter").get(0);
   assertEquals(Node.newName("obj"), getterMethod.getFirstChild());
   assertEquals(Token.GETPROP, getterMethod.getFirstChild().getType());

   // test that setters are handled correctly
   Node setter = Node.newFunction("setter", new Node[]{Node.newName("obj"), Node.newName("value")});
   root.setProperty("setter", setter);
   pass.process(externs, root);

   assertEquals(1, pass.methodDefinitions.get("setter").size());
   Node setterMethod = pass.methodDefinitions.get("setter").get(0);
   assertEquals(Node.newName("obj"), setterMethod.getFirstChild());
   assertEquals(Token.GETPROP, setterMethod.getFirstChild().getType());
   assertEquals(Node.newName("value"), setterMethod.getLastChild());
   assertEquals(Token.ASSIGN, setterMethod.getLastChild().getType());

   // test that methods on prototype are handled correctly
   Node prototype = Node.newObjectLiteral();
   prototype.setProperty("getSomeMethod", Node.newName("getSomeMethod"));
   root.setProperty("prototype", prototype);
   pass.process(externs, root);

   assertEquals(1, pass.methodDefinitions.get("prototype.getSomeMethod").size());
   Node prototypeMethod = pass.methodDefinitions.get("prototype.getSomeMethod").get(0);
   assertEquals(Node.newName("prototype"), prototypeMethod.getFirstChild());
   assertEquals(Token.GETPROP, prototypeMethod.getFirstChild().getType());
 }
}

class TestMethodCompilerPass extends MethodCompilerPass {
  TestMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  SignatureStore getSignatureStore() {
    return compiler.getSignatureStore();
  }

  @Override
  Callback getActingCallback() {
    // no-op
    return null;
  }
}