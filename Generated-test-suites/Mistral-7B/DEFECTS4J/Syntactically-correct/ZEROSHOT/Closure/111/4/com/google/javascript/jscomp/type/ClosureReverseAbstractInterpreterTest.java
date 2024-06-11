import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.NodeTraversingTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeVisitor;
import com.google.javascript.rhino.jstype.JSTypeRegistry.Type;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest extends NodeTraversingTestCase {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    codingConvention = mock(CodingConvention.class);
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Override
  protected JSTypeVisitor getTypeVisitor() {
    return interpreter;
  }

  public void testIsDefTrue() {
    JSType inputType = typeRegistry.createUnionType(typeRegistry.createStringType(), typeRegistry.createNumberStringBooleanType());
    TypeRestriction outputRestriction = new TypeRestriction(inputType, true);

    JSType expectedOutput = typeRegistry.createStringType();
    JSType actualOutput = interpreter.restrictParameter(
        createNameNode("param"), inputType, null, interpreter.restricters.get("isDef"), true).getDeclaredType();

    assertEquals(expectedOutput, actualOutput);
  }

  public void testIsDefFalse() {
    JSType inputType = typeRegistry.createVoidType();
    TypeRestriction outputRestriction = new TypeRestriction(inputType, false);

    JSType expectedOutput = null;
    JSType actualOutput = interpreter.restrictParameter(
        createNameNode("param"), inputType, null, interpreter.restricters.get("isDef"), false).getDeclaredType();

    assertEquals(expectedOutput, actualOutput);
  }

  // Add test cases for other functions in the class

  private static class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    private TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }
  }
}