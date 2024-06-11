
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(new Compiler(null), CheckLevel.WARNING, false);
    Node rootNode = Node.objectLiteral();

    // Test method without side effect
    rootNode.addChildToBack(Node.stringLiteral("Hello World"));
    checkSideEffects.process(null, rootNode);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Test method with side effect
    rootNode.setType(Token.BLOCK);
    rootNode.addChildToBack(Node.stringLiteral("var s = "));
    rootNode.addChildToBack(Node.stringLiteral("\"this string is "));
    rootNode.addChildToBack(Node.stringLiteral("continued on the next line but you forgot the +"));
    rootNode.addChildToBack(Node.stringLiteral("\";"));
    rootNode.addChildToBack(Node.numberLiteral(1));
    checkSideEffects.process(null, rootNode);
    assertEquals(1, checkSideEffects.problemNodes.size());

    // Test method with unexpected type
    rootNode.setType(Token.IDENTIFIER);
    checkSideEffects.process(null, rootNode);
    assertNotNull(checkSideEffects.problemNodes.isEmpty());

    // Test method with protected side effect free code
    rootNode.setType(Token.BLOCK);
    rootNode.addChildToBack(Node.stringLiteral("JSCOMPILER_PRESERVE "));
    rootNode.addChildToBack(Node.stringLiteral("var s = "));
    rootNode.addChildToBack(Node.stringLiteral("\"this string is "));
    rootNode.addChildToBack(Node.stringLiteral("continued on the next line but you forgot the +"));
    rootNode.addChildToBack(Node.stringLiteral("\";"));
    CheckSideEffects checkSideEffectsProtected = new CheckSideEffects(new Compiler(null), CheckLevel.WARNING, true);
    checkSideEffectsProtected.process(null, rootNode);
    assertEquals(0, checkSideEffectsProtected.problemNodes.size());

    // Test method with side effect in for loop
    rootNode.setType(Token.BLOCK);
    rootNode.addChildToBack(Node.stringLiteral("for (var i = 0; i < 10; i++) {"));
    rootNode.addChildToBack(Node.numberLiteral(1));
    rootNode.addChildToBack(Node.stringLiteral("}"));
    checkSideEffects.process(null, rootNode);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }
}

// StripProtection class test
public class StripProtectionTest {

  @Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(new Compiler(null), CheckLevel.WARNING, false);
    Node rootNode = Node.objectLiteral();

    // Test method with protected side effect free code
    rootNode.setType(Token.BLOCK);
    rootNode.addChildToBack(Node.stringLiteral("JSCOMPILER_PRESERVE "));
    rootNode.addChildToBack(Node.stringLiteral("var s = "));
    rootNode.addChildToBack(Node.stringLiteral("\"this string is "));
    rootNode.addChildToBack(Node.stringLiteral("continued on the next line but you forgot the +"));
    rootNode.addChildToBack(Node.stringLiteral("\";"));
    checkSideEffects.process(null, rootNode);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Test method after StripProtection pass
    new StripProtection(new Compiler(null)).process(null, rootNode);
    checkSideEffects = new CheckSideEffects(new Compiler(null), CheckLevel.WARNING, false);
    checkSideEffects.process(null, rootNode);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }
}