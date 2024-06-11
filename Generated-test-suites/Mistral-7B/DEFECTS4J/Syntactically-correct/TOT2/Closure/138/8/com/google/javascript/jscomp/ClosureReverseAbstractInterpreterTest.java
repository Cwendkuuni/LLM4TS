import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TYPE_RESTRICTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Node condition = createNode("goog.isDef(null)");
    JSType type = interpreter.getType(condition, null);
    assertEquals(interpreter.getNativeType(JSTypeNative.NULL_TYPE), type);

    condition = createNode("goog.isDef(true)");
    type = interpreter.getType(condition, null);
    assertNull(type);

    condition = createNode("goog.isDef(1)");
    type = interpreter.getType(condition, null);
    assertNull(type);

    condition = createNode("goog.isDef(1)");
    TypeRestriction typeRestriction = new TypeRestriction(JSTypeNative.NUMBER_TYPE, true);
    type = interpreter.restrictParameter(createNode("x"), typeRestriction, null, interpreter.restricters.get("isDef"), true).getScope().getVariable("x").getType();
    assertEquals(JSTypeNative.NUMBER_TYPE, type);
  }

  // Add other test cases for other methods here

  private Node createNode(String expression) {
    // Implementation for creating Node object
  }
}