import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.type.FlowScope;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.Type;
import static com.google.javascript.rhino.NodeParser.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testConstructor() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.DEFAULT, null);
    assertNotNull(interpreter);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomePositive() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.DEFAULT, null);

    Node condition = new NodeParser().parseExpression("goog.isDef(myVar)");
    FlowScope blindScope = new FlowScope();

    Type type = new Type(OBJECT_TYPE);
    Node parameter = new NodeParser().parseExpression("myVar");
    JSType paramType = new JSType(OBJECT_TYPE);

    FlowScope informedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, interpreter.restrictors.get("isDef"), true);

    assertEquals(informedScope, interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeNegative() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.DEFAULT, null);

    Node condition = new NodeParser().parseExpression("goog.isDef(nullVar)");
    FlowScope blindScope = new FlowScope();

    Node parameter = new NodeParser().parseExpression("nullVar");

    FlowScope informedScope = interpreter.restrictParameter(
        parameter, null, blindScope, interpreter.restrictors.get("isDef"), false);

    assertEquals(blindScope, interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false));
  }

  @Test
  public void testRestrictParameterValidType() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.DEFAULT, null);

    Node parameter = new NodeParser().parseExpression("myVar");
    JSType type = new JSType(OBJECT_TYPE);
    FlowScope blindScope = new FlowScope();

    FlowScope informedScope = interpreter.restrictParameter(
        parameter, type, blindScope, interpreter.restrictors.get("isObject"), true);

    assertEquals(informedScope, interpreter.getPreciserScopeKnowingConditionOutcome(
        new NodeParser().parseExpression("goog.isObject(myVar)"), blindScope, true));
  }

  @Test
  public void testRestrictParameterNullType() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.DEFAULT, null);

    Node parameter = new NodeParser().parseExpression("myVar");
    FlowScope blindScope = new FlowScope();

    assertNull(interpreter.restrictParameter(
        parameter, null, blindScope, interpreter.restrictors.get("isObject"), false));
  }

  @Test
  public void testRestrictParameterNonObjectType() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.DEFAULT, null);

    Node parameter = new NodeParser().parseExpression("5");
    FlowScope blindScope = new FlowScope();

    assertNull(interpreter.restrictParameter(
        parameter, new JSType(NUMBER_TYPE), blindScope, interpreter.restrictors.get("isObject"), true));
  }
}