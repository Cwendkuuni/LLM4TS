import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.common.base.Functions;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.TypeRestriction;

public class ClosureReverseAbstractInterpreterTest {
  private static final ClosureReverseAbstractInterpreter interpreter =
      new ClosureReverseAbstractInterpreter(CodingConvention.UNSAFE, null);

  @org.junit.Test
  public void testIsDef() {
    JSType expectedType = VOID_TYPE;
    TypeRestriction typeRestriction = new TypeRestriction(NUMBER_STRING_BOOLEAN, true);
    JSType actualType = interpreter.restrictParameter(Node.newIdent("x"), typeRestriction.type, null, interpreter.restricters.get("isDef"), true);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testIsDef_null() {
    JSType expectedType = NULL_TYPE;
    TypeRestriction typeRestriction = new TypeRestriction(NUMBER_STRING_BOOLEAN, false);
    JSType actualType = interpreter.restrictParameter(Node.newIdent("x"), typeRestriction.type, null, interpreter.restricters.get("isDef"), false);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testIsDef_undefined() {
    JSType expectedType = VoidType.VOID;
    TypeRestriction typeRestriction = new TypeRestriction(null, true);
    JSType actualType = interpreter.restrictParameter(Node.newIdent("x"), typeRestriction.type, null, interpreter.restricters.get("isDef"), true);
    assertEquals(expectedType, actualType);
  }

  // Add test cases for other methods and edge cases here
}