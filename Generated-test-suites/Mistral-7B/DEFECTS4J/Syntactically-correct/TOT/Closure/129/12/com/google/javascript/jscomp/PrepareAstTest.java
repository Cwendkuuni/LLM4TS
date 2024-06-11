import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Test with empty AST
    Node emptyAst = IR.program();
    prepareAst.process(null, emptyAst);

    // Test with simple AST
    Node simpleAst = IR.program(
        IR.declareVar("x", IR.numberLiteral(10)),
        IR.expressionStatement(IR.assign(IR.name("x"), IR.numberLiteral(20)))
    );
    prepareAst.process(null, simpleAst);

    // Test with control structures
    Node controlStructuresAst = IR.program(
        IR.ifStatement(
            IR.expressionStatement(IR.assign(IR.name("x"), IR.numberLiteral(1))),
            IR.expressionStatement(IR.assign(IR.name("y"), IR.numberLiteral(2))),
            IR.expressionStatement(IR.assign(IR.name("z"), IR.numberLiteral(3)))
        ),
        IR.expressionStatement(IR.assign(IR.name("x"), IR.numberLiteral(4)))
    );
    prepareAst.process(null, controlStructuresAst);

    // Test with object literals
    Node objectLiteralsAst = IR.program(
        IR.declareVar("obj", IR.objectLiteral(
            IR.property(IR.name("a"), IR.numberLiteral(1)),
            IR.property(IR.name("b"), IR.numberLiteral(2)),
            IR.property(IR.name("c"), IR.numberLiteral(3))
        )),
        IR.expressionStatement(IR.assign(IR.name("obj.a"), IR.numberLiteral(4)))
    );
    prepareAst.process(null, objectLiteralsAst);

    // Test with function literals
    Node functionLiteralsAst = IR.program(
        IR.declareVar("x", IR.numberLiteral(1)),
        IR.expressionStatement(IR.assign(IR.name("y"), IR.function(
            null,
            null,
            IR.block(
                IR.expressionStatement(IR.assign(IR.name("z"), IR.numberLiteral(2))),
                IR.returnStatement(IR.numberLiteral(3))
            )
        )))
    );
    prepareAst.process(null, functionLiteralsAst);

    // Test with error scenarios
    Node errorScenariosAst = IR.program(
        IR.expressionStatement(IR.assign(IR.name("x"), IR.undefined())),
        IR.expressionStatement(IR.assign(IR.name("y"), IR.nullLiteral()))
    );
    try {
      prepareAst.process(null, errorScenariosAst);
      fail("Expected Preconditions check to fail");
    } catch (IllegalArgumentException e) {
      // expected
    }

    // Test with checkOnly flag
    prepareAst = new PrepareAst(compiler, true);
    Node checkOnlyAst = IR.program(
        IR.expressionStatement(IR.assign(IR.name("x"), IR.numberLiteral(1)))
    );
    prepareAst.process(null, checkOnlyAst);
  }
}