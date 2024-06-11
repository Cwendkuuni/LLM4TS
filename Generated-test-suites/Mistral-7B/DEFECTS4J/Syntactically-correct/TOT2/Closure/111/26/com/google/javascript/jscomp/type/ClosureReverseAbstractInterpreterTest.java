import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Visitor;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType type = JSType.STRING;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = interpreter.getRestrictedWithoutUndefined(type);
    assertEquals(expected, interpreter.restricters.get("isDef").apply(restriction));
  }

  @Test
  public void testIsDefWithNullType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    TypeRestriction restriction = new TypeRestriction(null, true);
    JSType expected = interpreter.getNativeType(VOID_TYPE);
    assertEquals(expected, interpreter.restricters.get("isDef").apply(restriction));
  }

  @Test
  public void testIsDefWithFalse() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType type = JSType.STRING;
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    assertEquals(expected, interpreter.restricters.get("isDef").apply(restriction));
  }

  // Add test cases for other methods in the ClosureReverseAbstractInterpreter class
}