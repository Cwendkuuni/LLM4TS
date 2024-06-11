package com.example.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.TypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

 @Test
 public void testIsDef() {
 ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
 new CodingConvention(), new TypeRegistry());
 JSType type = JSType.NULL;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.STRING;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.BOOLEAN;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.NUMBER;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.ARRAY;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.OBJECT;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.FUNCTION;
 assertTrue(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.VOID;
 assertFalse(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.NULL_VOID;
 assertFalse(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);

 type = JSType.UNDEFINED;
 assertFalse(interpreter.isDef(new TypeRestriction(type, true)).outcome);
 assertFalse(interpreter.isDef(new TypeRestriction(type, false)).outcome);
 }

 // Add the remaining test cases for isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, and isObject here

 static class TypeRestriction {
 private final JSType type;
 private final boolean outcome;

 public TypeRestriction(JSType type, boolean outcome) {
 this.type = type;
 this.outcome = outcome;
 }
 }
}