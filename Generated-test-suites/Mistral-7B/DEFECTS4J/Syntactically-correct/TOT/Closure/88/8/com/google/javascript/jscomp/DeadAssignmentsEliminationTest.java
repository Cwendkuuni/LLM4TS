import org.junit.Test;
import static com.google.common.truth.Truth.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.rhino.Node;

public class DeadAssignmentsEliminationTest extends CompilerPassTestCase {

  public DeadAssignmentsEliminationTest() {
    super("--jsCompiler:dead_assignments_elimination");
  }

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = ""
        + "function main(x, y) {"
        + "  x = y;" // Dead assignment
        + "  y = 1;"
        + "  return x + y;"
        + "}"
        + "main(1, 2);";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main(x, y) {"
        + "  y = 1;"
        + "  return x + y;"
        + "}", root);

    jsCode = ""
        + "function main() {"
        + "  var x = 1;"
        + "  if (x = 2) {"
        + "    x = 3;" // Dead assignment
        + "  } else {"
        + "    x = 4;"
        + "  }"
        + "  return x;"
        + "}"
        + "main();";

    root = parse(jsCode);
    compiler = newAbstractCompiler();
    pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  if (true) {"
        + "  } else {"
        + "    var x = 4;"
        + "  }"
        + "  return x;"
        + "}", root);

    jsCode = ""
        + "function main() {"
        + "  var x = 1;"
        + "  if (x = 2) {"
        + "    return 2;" // Dead assignment
        + "  }"
        + "  return x + 1;"
        + "}"
        + "main();";

    root = parse(jsCode);
    compiler = newAbstractCompiler();
    pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  if (true) {"
        + "    return 2;"
        + "  }"
        + "  return 1;"
        + "}", root);

    jsCode = ""
        + "function main() {"
        + "  var x = 1;"
        + "  x = x;" // Identity assignment, doesn't change the value
        + "  return x;"
        + "}"
        + "main();";

    root = parse(jsCode);
    compiler = newAbstractCompiler();
    pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  return 1;"
        + "}", root);

    jsCode = ""
        + "function main(x) {"
        + "  var y = x;"
        + "  x = 1;" // Dead assignment
        + "  return x + y;"
        + "}"
        + "main(2);";

    root = parse(jsCode);
    compiler = newAbstractCompiler();
    pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main(x) {"
        + "  return 1 + x;"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationInsideFunction() {
    String jsCode = ""
        + "function outerFunction() {"
        + "  function innerFunction(x) {"
        + "    x = 1;" // Dead assignment
        + "    return x;"
        + "  }"
        + "  return innerFunction(1);"
        + "}"
        + "outerFunction();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function outerFunction() {"
        + "  function innerFunction(x) {"
        + "    return x + 0;"
        + "  }"
        + "  return innerFunction(1);"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationInsideIf() {
    String jsCode = ""
        + "function main() {"
        + "  if (true) {"
        + "    var x = 1;"
        + "    x = 2;" // Dead assignment
        + "  }"
        + "  return 1;"
        + "}"
        + "main();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  return 1;"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationInsideFor() {
    String jsCode = ""
        + "function main() {"
        + "  for (var i = 0; i < 10; i = i + 1) {"
        + "    var j = i;"
        + "    j = j + 1;" // Dead assignment
        + "  }"
        + "  return 1;"
        + "}"
        + "main();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "  }"
        + "  return 1;"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationInsideIfElse() {
    String jsCode = ""
        + "function main() {"
        + "  var x = 1;"
        + "  if (x > 0) {"
        + "    x = 2;" // Dead assignment
        + "  } else {"
        + "    x = 3;"
        + "  }"
        + "  return x;"
        + "}"
        + "main();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  var x = 3;"
        + "  return x;"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationInsideSwitch() {
    String jsCode = ""
        + "function main() {"
        + "  var x = 1;"
        + "  switch (x) {"
        + "    case 1:"
        + "      x = 2;" // Dead assignment
        + "      break;"
        + "    case 2:"
        + "      break;"
        + "  }"
        + "  return x;"
        + "}"
        + "main();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  return 1;"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithSideEffect() {
    String jsCode = ""
        + "function main() {"
        + "  var x = 1;"
        + "  var y = 1;"
        + "  x = y;" // Dead assignment
        + "  y++;"
        + "  return x + y;"
        + "}"
        + "main();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  var y = 1;"
        + "  return 1 + y;"
        + "}", root);
  }

  @Test
  public void testDeadAssignmentsEliminationInsideVariableDeclaration() {
    String jsCode = ""
        + "function main() {"
        + "  var x, y;"
        + "  x = 1;" // Dead assignment
        + "  y = 2;"
        + "  return x + y;"
        + "}"
        + "main();";

    Node root = parse(jsCode);
    AbstractCompiler compiler = newAbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    pass.process(null, root);
    assertCompilesTo("function main() {"
        + "  var y = 2;"
        + "  return 1 + y;"
        + "}", root);
  }
}