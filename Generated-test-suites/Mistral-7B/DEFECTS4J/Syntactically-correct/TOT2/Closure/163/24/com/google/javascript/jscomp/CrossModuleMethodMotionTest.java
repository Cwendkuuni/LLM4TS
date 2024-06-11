import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;

public class CrossModuleMethodMotionTest {
  @Test
  public void testMoveMethods() {
    // Test setup
    Compiler compiler = new Compiler();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    Node root = IR.program();

    // Basic functionality test
    Node originalClass = IR.classDef("OriginalClass", IR.nullLiteral(), root);
    Node originalMethod = IR.methodDef("originalMethod", IR.nullLiteral(), IR.nullLiteral(), IR.nullLiteral(), IR.block(
        IR.returnValue(IR.stringLiteral("Original method")))
        .copyInformationFromForTree(originalClass), originalClass);

    Node deepClass = IR.classDef("DeepClass", IR.nullLiteral(), root);
    Node deepMethod = IR.methodDef("deepMethod", IR.nullLiteral(), IR.nullLiteral(), IR.nullLiteral(), IR.block(
        IR.returnValue(IR.stringLiteral("Deep method")))
        .copyInformationFromForTree(deepClass), deepClass);

    IR.setProperty(originalClass, IR.string("prototype"), IR.getprop(deepClass, IR.string("prototype")));
    IR.setProperty(originalClass, IR.string("prototype"), IR.getprop(originalClass, IR.string("prototype")));

    IR.assign(IR.getprop(originalClass, IR.string("prototype")), originalMethod);
    IR.assign(IR.getprop(deepClass, IR.string("prototype")), deepMethod);

    Node topLevel = IR.exprResult(IR.assign(root, pass.process(IR.nullNode(), root)))
        .copyInformationFromForTree(root);

    // Test that methods are moved correctly
    Node movedMethod = IR.getprop(topLevel, IR.string("OriginalClass.prototype.originalMethod"));
    assertEquals("DeepClass.prototype.deepMethod", movedMethod.toString());

    // Edge cases and error scenarios testing will be added in the next steps
  }
}