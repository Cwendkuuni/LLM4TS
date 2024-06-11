import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(new Object()));
    }
    
    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertFalse(predicate.apply(new Object()));
        assertTrue(predicate.apply(null));
    }
    
    @Test
    public void testNot() {
        Predicate<Object> predicateTrue = Predicates.alwaysTrue();
        Predicate<Object> predicateFalse = Predicates.alwaysFalse();
        
        Predicate<Object> notPredicateTrue = Predicates.not(predicateTrue);
        assertFalse(notPredicateTrue.apply(null));
        assertFalse(notPredicateTrue.apply(new Object()));
        
        Predicate<Object> notPredicateFalse = Predicates.not(predicateFalse);
        assertTrue(notPredicateFalse.apply(null));
        assertTrue(notPredicateFalse.apply(new Object()));
    }

    @Test
    public void testAnd() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
        
        assertFalse(andPredicate.apply(null));
        assertTrue(andPredicate.apply(new Object()));
    }

    @Test
    public void testOr() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
        
        assertTrue(orPredicate.apply(null));
        assertTrue(orPredicate.apply(new Object()));
    }

    @Test(expected = AssertionError.class)
    public void testAndWithNullComponents() {
        Predicate<Object> predicate1 = null;
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
        
        andPredicate.apply(null);
    }

    @Test(expected = AssertionError.class)
    public void testOrWithNullComponents() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = null;
        Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
        
        orPredicate.apply(null);
    }
}