import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;

public class ClosureReverseAbstractInterpreterTest {

 private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
 private static final CodingConvention convention = new CodingConvention();

 @Test
 public void testIsDefRestrictedToBoolean() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newQualifiedName("myVariable"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedWithoutUndefined(expectedType);

 assertTrue(actualType.isSubtype(JSType.getBooleanType()));
 }

 @Test
 public void testIsDefRestrictedToNull() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newQualifiedName("myVariable"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedWithoutNull(expectedType);

 assertNull(actualType);
 }

 @Test
 public void testIsDefAndNotNullRestrictedToVoid() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newQualifiedName("myVariable"));
 Node notNullCondition = Node.newNot(condition);
 Node andCondition = Node.newBinaryOp(Node.OP_AND, condition, notNullCondition);

 JSType expectedType = interpreter.getType(andCondition, null);
 JSType actualType = interpreter.getRestrictedWithoutNull(
 interpreter.getRestrictedWithoutUndefined(expectedType));

 assertEquals(JSType.getNullVoidType(), actualType);
 }

 @Test
 public void testIsStringRestrictedToString() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isString"),
 Node.newString("myString"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "string", true);

 assertEquals(JSType.getNativeStringType(), actualType);
 }

 @Test
 public void testIsStringRestrictedToBoolean() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isString"),
 Node.newString("myString"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "string", false);

 assertFalse(actualType.isBoolean());
 }

 @Test
 public void testIsFunctionRestrictedToFunction() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isFunction"),
 Node.newQualifiedName("myFunction"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "function", true);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsFunctionRestrictedToBoolean() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isFunction"),
 Node.newQualifiedName("myFunction"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "function", false);

 assertFalse(actualType.isFunction());
 }

 @Test
 public void testIsArrayRestrictedToArray() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isArray"),
 Node.newQualifiedName("myArray"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "array", true);

 assertEquals(JSType.getArrayType(JSType.getNativeType(Number.class), null),
 actualType);
 }

 @Test
 public void testIsArrayRestrictedToBoolean() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isArray"),
 Node.newQualifiedName("myArray"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "array", false);

 assertFalse(actualType.isArray());
 }

 @Test
 public void testIsObjectRestrictedToObject() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isObject"),
 Node.newQualifiedName("myObject"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "object", true);

 assertEquals(JSType.getObjectType(), actualType);
 }

 @Test
 public void testIsObjectRestrictedToBoolean() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 convention, typeRegistry);

 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isObject"),
 Node.newQualifiedName("myObject"));

 JSType expectedType = interpreter.getType(condition, null);
 JSType actualType = interpreter.getRestrictedByTypeOfResult(expectedType,
 "object", false);

 assertFalse(actualType.isObject());
 }
}