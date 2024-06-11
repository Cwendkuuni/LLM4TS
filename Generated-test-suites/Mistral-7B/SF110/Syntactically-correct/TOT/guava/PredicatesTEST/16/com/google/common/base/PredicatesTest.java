import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object obj = new Object();
 assertTrue(Predicates.alwaysTrue().apply(obj));
 }

 @Test
 public void testAlwaysFalse() {
 Object obj = new Object();
 assertFalse(Predicates.alwaysFalse().apply(obj));
 }

 @Test
 public void testIsNull() {
 Object obj1 = null;
 assertTrue(Predicates.isNull().apply(obj1));
 Object obj2 = new Object();
 assertFalse(Predicates.isNull().apply(obj2));
 }

 @Test
 public void testNotNull() {
 Object obj1 = null;
 assertFalse(Predicates.notNull().apply(obj1));
 Object obj2 = new Object();
 assertTrue(Predicates.notNull().apply(obj2));
 }

 @Test
 public void testNot() {
 Predicate<Object> predicate = Predicates.isNotNull();
 Predicate<Object> notPredicate = Predicates.not(predicate);
 Object obj1 = null;
 assertFalse(notPredicate.apply(obj1));
 Object obj2 = new Object();
 assertTrue(notPredicate.apply(obj2));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };
 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
 int number1 = 4;
 int number2 = -2;
 assertTrue(andPredicate.apply(number1));
 assertFalse(andPredicate.apply(number2));
 }

 @Test
 public void testAndMultipleInputs() {
 Predicate<String> isEmpty = Predicates.isNull();
 Predicate<String> startsWithA = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String input) {
 return input.charAt(0) == 'A';
 }
 };
 Predicate<String> andPredicate = Predicates.and(isEmpty, startsWithA);
 String emptyString = null;
 String string1 = "Apple";
 String string2 = "Banana";
 assertTrue(andPredicate.apply(string1));
 assertFalse(andPredicate.apply(emptyString));
 assertFalse(andPredicate.apply(string2));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };
 Predicate<Integer> isNegative = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input < 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(isPositive, isNegative);
 int number1 = 4;
 int number2 = -2;
 assertTrue(orPredicate.apply(number1));
 assertTrue(orPredicate.apply(number2));
 }

 @Test
 public void testOrMultipleInputs() {
 Predicate<String> isEmpty = Predicates.isNull();
 Predicate<String> startsWithA = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String input) {
 return input.charAt(0) == 'A';
 }
 };
 Predicate<String> isEmptyOrStartsWithA = Predicates.or(isEmpty, startsWithA);
 String emptyString = null;
 String string1 = "Apple";
 String string2 = "Banana";
 assertTrue(isEmptyOrStartsWithA.apply(string1));
 assertFalse(isEmptyOrStartsWithA.apply(emptyString));
 assertTrue(isEmptyOrStartsWithA.apply(string2));
 }

 @Test
 public void testEqualTo() {
 Predicate<String> isEqualToString = Predicates.equalTo("Google");
 String string1 = "Google";
 String string2 = "Apple";
 assertTrue(isEqualToString.apply(string1));
 assertFalse(isEqualToString.apply(string2));
 }

 @Test
 public void testInstanceOf() {
 Predicate<String> isInstanceOfString = Predicates.instanceOf(String.class);
 Predicate<Integer> isInstanceOfInteger = Predicates.instanceOf(Integer.class);
 String string1 = "Google";
 Integer number1 = 4;
 assertTrue(isInstanceOfString.apply(string1));
 assertFalse(isInstanceOfString.apply(number1));
 assertTrue(isInstanceOfInteger.apply(number1));
 assertFalse(isInstanceOfInteger.apply(string1));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromInt = Predicates.assignableFrom(Integer.class);
 Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
 Class<Number> numberClass = Number.class;
 Class<String> stringClass = String.class;
 assertTrue(isAssignableFromInt.apply(numberClass));
 assertTrue(isAssignableFromString.apply(String[].class));
 assertTrue(isAssignableFromInt.apply(numberClass));
 assertFalse(isAssignableFromInt.apply(stringClass));
 assertFalse(isAssignableFromString.apply(numberClass));
 }

 @Test
 public void testIn() {
 Predicate<Integer> inPredicate = Predicates.in(Arrays.asList(1, 2, 3, 4));
 int number1 = 2;
 int number2 = 5;
 assertTrue(inPredicate.apply(number1));
 assertFalse(inPredicate.apply(number2));
 }

 @Test
 public void testInMultipleCollections() {
 Predicate<Integer> inPredicate1 = Predicates.in(Arrays.asList(1, 2));
 Predicate<Integer> inPredicate2 = Predicates.in(Arrays.asList(2, 3, 4));
 int number1 = 2;
 int number2 = 5;
 assertTrue(Predicates.or(inPredicate1, inPredicate2).apply(number1));
 assertFalse(Predicates.or(inPredicate1, inPredicate2).apply(number2));
 }

 @Test
 public void testInNullCollection() {
 Predicate<String> inPredicate = Predicates.in(null);
 String emptyString = null;
 String string1 = "Apple";
 assertTrue(Predicates.or(Predicates.isNull(), inPredicate).apply(emptyString));
 assertFalse(inPredicate.apply(string1));
 }

 @Test(expected = NullPointerException.class)
 public void testInNullElement() {
 Predicate<String> inPredicate = Predicates.in(Arrays.asList("Apple", null));
 String nullString = null;
 assertFalse(inPredicate.apply(nullString));
 }

 @Test
 public void testCompose() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(@Nullable Integer input) {
 return input + 1;
 }
 };
 Predicate<Integer> composedPredicate = Predicates.compose(isEven, addOne);
 int number1 = 2;
 int number2 = 3;
 assertTrue(composedPredicate.apply(number1));
 assertFalse(composedPredicate.apply(number2));
 }
}