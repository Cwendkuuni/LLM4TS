
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueBoolBool() {
        Validate.isTrue(true, "Expected: true", (Object[]) null);
        Validate.isTrue(false, "Expected: false", (Object[]) null);
        Validate.isTrue(true, "Expected: true", new Object[]{"Value is true"});
        Validate.isTrue(false, "Expected: false", new Object[]{"Value is false"});
        Validate.isTrue(true, "Expected: true", new Object[]{"Value is true", 10});
        Validate.isTrue(false, "Expected: false", new Object[]{"Value is false", 20});
        Validate.isTrue(true, "Expected: true", new Object[]{"Value is true", 3.14});
        Validate.isTrue(false, "Expected: false", new Object[]{"Value is false", 4.16});
    }

    @Test
    public void testIsTrueEdgeCases() {
        Validate.isTrue(Boolean.FALSE, "Expected: false", (Object[]) null);
        Validate.isTrue(Boolean.FALSE, "Expected: false", new Object[]{"Value is false"});
        Validate.isTrue(Boolean.FALSE, "Expected: false", new Object[]{"Value is false", 10});
        Validate.isTrue(Boolean.FALSE, "Expected: false", new Object[]{"Value is false", 2.0});

        Validate.isTrue(Boolean.TRUE, "Expected: true", (Object[]) null);
        Validate.isTrue(Boolean.TRUE, "Expected: true", new Object[]{"Value is true"});
        Validate.isTrue(Boolean.TRUE, "Expected: true", new Object[]{"Value is true", 10});
        Validate.isTrue(Boolean.TRUE, "Expected: true", new Object[]{"Value is true", 2.0});
    }

    @Test
    public void testNotNullObject() {
        Object obj = new Object();
        Validate.notNull(obj, "Expected: non-null object", (Object[]) null);

        Object nullObj = null;
        try {
            Validate.notNull(nullObj, "Expected: non-null object", (Object[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Validate.notNull(obj, "Expected: non-null object", new Object[]{"Validated object is non-null"});
    }

    @Test
    public void testNotNullEdgeCases() {
        Validate.notNull(null, "Expected: non-null object", (Object[]) null);
        Validate.notNull(new String(""), "Expected: non-empty string", (Object[]) null);
        Validate.notNull(new ArrayList<>(), "Expected: non-empty collection", (Object[]) null);
        Validate.notNull(new HashMap<>(), "Expected: non-empty map", (Object[]) null);

        Validate.notNull(null, "Expected: non-null object", new Object[]{"Validated object is non-null"});
        Validate.notNull(new String(""), "Expected: non-empty string", new Object[]{"Validated string is non-empty"});
        Validate.notNull(new ArrayList<>(), "Expected: non-empty collection", new Object[]{"Validated collection is non-empty"});
        Validate.notNull(new HashMap<>(), "Expected: non-empty map", new Object[]{"Validated map is non-empty"});
    }

    // Test other methods and test cases if necessary
}