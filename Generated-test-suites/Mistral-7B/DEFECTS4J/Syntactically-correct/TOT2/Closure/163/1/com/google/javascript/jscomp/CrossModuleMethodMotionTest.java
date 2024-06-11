import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethod() {
    // Given: A class with a prototype method
    CrossModuleMethodMotion compilerPass = new CrossModuleMethodMotion(null, null, true);
    Node rootNode = Node.objectLiteral();
    Node prototype = Node.objectLiteral();
    prototype.addChild(Node.property("method", Node.functionLiteral()));
    rootNode.addChild(Node.property("prototype", prototype));

    // When: The method is moved to a deeper module
    compilerPass.process(null, rootNode);

    // Then: The method is replaced with a stub in the original module
    Node methodInOriginalModule = rootNode.getFirstChild().getLastChild();
    assertInstanceOf(Node.Call.class, methodInOriginalModule);
    Node stubCall = methodInOriginalModule.getFirstChild();
    assertEquals("JSCompiler_stubMethod", stubCall.getFirstChild().getNodeType(), Node.NAME);
    assertEquals(1, stubCall.getArguments().length);

    // Given: A call to the method in the original module
    Node callToMethod = Node.call(methodInOriginalModule, null);

    // When: The method is called
    Node result = compilerPass.compiler.run(callToMethod, null, null);

    // Then: The stub method is called instead
    assertTrue(result instanceof Node.Undefined);
  }

  @Test
  public void testMoveMethodInDeepestCommonModule() {
    // Given: A class with a prototype method in the deepest common module
    CrossModuleMethodMotion compilerPass = new CrossModuleMethodMotion(null, null, true);
    Node rootNode = Node.objectLiteral();
    Node moduleA = Node.moduleLiteral("A");
    Node prototype = Node.objectLiteral();
    prototype.addChild(Node.property("method", Node.functionLiteral()));
    moduleA.addChild(Node.property("prototype", prototype));
    rootNode.addChild(moduleA);

    // When: The method is moved to a deeper module
    compilerPass.process(null, rootNode);

    // Then: The method is replaced with a stub in the original module
    Node methodInOriginalModule = moduleA.getFirstChild().getLastChild().getLastChild();
    assertInstanceOf(Node.Call.class, methodInOriginalModule);
    Node stubCall = methodInOriginalModule.getFirstChild();
    assertEquals("JSCompiler_stubMethod", stubCall.getFirstChild().getNodeType(), Node.NAME);
    assertEquals(1, stubCall.getArguments().length);

    // Given: A call to the method in the original module
    Node callToMethod = Node.call(methodInOriginalModule, null);

    // When: The method is called
    Node result = compilerPass.compiler.run(callToMethod, null, null);

    // Then: The stub method is called instead
    assertTrue(result instanceof Node.Undefined);
  }

  @Test
  public void testMoveMethodWithException() {
    // Given: A class with a prototype method that throws an exception
    CrossModuleMethodMotion compilerPass = new CrossModuleMethodMotion(null, null, true);
    Node rootNode = Node.objectLiteral();
    Node prototype = Node.objectLiteral();
    prototype.addChild(Node.property("method", Node.functionLiteral()
        .setBody(Node.throwStatement(Node.error("TestException")))));
    rootNode.addChild(Node.property("prototype", prototype));

    // When: The method is moved to a deeper module
    compilerPass.process(null, rootNode);

    // Given: A call to the method in the original module
    Node callToMethod = Node.call(Node.getprop(rootNode.getFirstChild().getLastChild(), "prototype"), "method");

    // Then: The exception is caught and logged
    Logger logger = Logger.getLogger(CrossModuleMethodMotion.class.getName());
    assertEquals(1, logger.getLevel().intValue()); // Warnings are logged as level 999
    assertTrue(logger.getMessages().get(0).getLevel().intValue() >= 900);
    assertTrue(logger.getMessages().get(0).getMessage().contains("TestException"));
  }
}