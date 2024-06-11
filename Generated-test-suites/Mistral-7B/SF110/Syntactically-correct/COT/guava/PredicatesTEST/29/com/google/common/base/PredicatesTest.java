import static org.junit.Assert.*;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 private static <T> Predicate<T> createConstantPredicate(boolean constantValue) {
 return (Predicate<T>) (constantValue ? Predicates.alwaysTrue() : Predicates.alwaysFalse());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAlwaysTrue() {
 Predicate<Object> predicate = Predicates.alwaysTrue();
 assertTrue(predicate.apply(null));
 assertTrue(predicate.apply(""));
 assertTrue(predicate.apply(new Object()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAlwaysFalse() {
 Predicate<Object> predicate = Predicates.alwaysFalse();
 assertFalse(predicate.apply(null));
 assertFalse(predicate.apply(""));
 assertFalse(predicate.apply(new Object()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testIsNull() {
 Predicate<Object> predicate = Predicates.isNull();
 assertFalse(predicate.apply(new Object()));
 assertTrue(predicate.apply(null));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNotNull() {
 Predicate<Object> predicate = Predicates.notNull();
 assertFalse(predicate.apply(null));
 assertTrue(predicate.apply(new Object()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNot() {
 Predicate<Object> originalPredicate = createConstantPredicate(true);
 Predicate<Object> negatedPredicate = Predicates.not(originalPredicate);
 assertFalse(originalPredicate.apply(null));
 assertTrue(negatedPredicate.apply(null));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAnd() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysFalse());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 assertFalse(andPredicate.apply(null));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAndWithTwoValidPredicates() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(new Object()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAndWithTwoValidPredicatesOnSameObject() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 Object obj = new Object();
 assertFalse(andPredicate.apply(obj));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAndWithTwoValidPredicatesOnDifferentObjects() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertFalse(andPredicate.apply(obj1));
 assertFalse(andPredicate.apply(obj2));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAndWithMixedValidAndInvalidPredicates() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull(), Predicates.alwaysFalse());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 Object obj = new Object();
 assertFalse(andPredicate.apply(obj));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NullPointerException.class)
 public void testAndWithNullPredicate() {
 List<Predicate<Object>> predicates = Arrays.asList(null, Predicates.notNull());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testOr() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysTrue());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 assertTrue(orPredicate.apply(null));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testOrWithTwoValidPredicates() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 assertFalse(orPredicate.apply(null));
 assertTrue(orPredicate.apply(new Object()));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testOrWithTwoValidPredicatesOnSameObject() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 Object obj = new Object();
 assertTrue(orPredicate.apply(obj));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testOrWithTwoValidPredicatesOnDifferentObjects() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertTrue(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testOrWithMixedValidAndInvalidPredicates() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull(), Predicates.alwaysFalse());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 Object obj = new Object();
 assertTrue(orPredicate.apply(obj));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NullPointerException.class)
 public void testOrWithNullPredicate() {
 List<Predicate<Object>> predicates = Arrays.asList(null, Predicates.notNull());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testEqualTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Predicate<Object> equalPredicate = Predicates.equalTo(obj1);
 assertTrue(equalPredicate.apply(obj1));
 assertTrue(equalPredicate.apply(obj2));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testEqualToWithDifferentObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Predicate<Object> equalPredicate = Predicates.equalTo(obj1);
 assertFalse(equalPredicate.apply(obj2));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testInstanceOf() {
 @SuppressWarnings("rawtypes")
 Class clazz = String.class;
 Predicate<Object> instanceOfPredicate = Predicates.instanceOf(clazz);
 assertFalse(instanceOfPredicate.apply(new Object()));
 assertTrue(instanceOfPredicate.apply(""));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAssignableFrom() {
 @SuppressWarnings("rawtypes")
 Class clazz1 = String.class;
 @SuppressWarnings("rawtypes")
 Class clazz2 = Object.class;
 Predicate<Class> assignableFromPredicate = Predicates.assignableFrom(clazz1);
 assertFalse(assignableFromPredicate.apply(clazz2));
 assertTrue(clazz2.isAssignableFrom(clazz1));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testIn() {
 List<Object> list = Arrays.asList(new Object(), new Object());
 Predicate<Object> inPredicate = Predicates.in(list);
 assertFalse(inPredicate.apply(new Object()));
 assertTrue(inPredicate.apply(list.get(0)));
 assertTrue(inPredicate.apply(list.get(1)));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCompose() {
 Function<Integer, Integer> function = i -> i * 2;
 Predicate<Integer> predicate = Predicates.alwaysFalse();
 Predicate<Integer> composedPredicate = Predicates.compose(predicate, function);
 assertFalse(composedPredicate.apply(0));
 assertFalse(composedPredicate.apply(1));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testContainsPattern() {
 String pattern = ".*ab.*";
 Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern(pattern);
 assertFalse(containsPatternPredicate.apply("abc"));
 assertTrue(containsPatternPredicate.apply("abab"));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testContainsPatternFromString() {
 String pattern = "ab";
 Predicate<CharSequence> containsPatternPredicate = Predicates.containsPatternFromString(pattern);
 assertFalse(containsPatternPredicate.apply("abc"));
 assertTrue(containsPatternPredicate.apply("abab"));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testEmptyCollection() {
 Predicate<Object> inPredicate = Predicates.in(Collections.emptyList());
 assertFalse(inPredicate.apply(new Object()));
 }
}