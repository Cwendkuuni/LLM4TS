import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testSameObjects() {
        Same sameObject = new Same(new Object());
        Object actualObject = new Object();

        Assert.assertTrue("should match same object", sameObject.matches(actualObject));
        Assert.assertTrue("describeTo should return correct string", sameObject.describeTo(mock(Description.class)).toString().equals("same(" + actualObject.toString() + ")"));
    }

    @Test
    public void testDifferentObjects() {
        Same sameObject = new Same(new Object());
        Object differentObject = new Object() {
            @Override
            public String toString() {
                return "different object";
            }
        };

        Assert.assertFalse("should not match different object", sameObject.matches(differentObject));
    }

    @Test
    public void testSameStrings() {
        Same sameString = new Same("test string");
        String actualString = "test string";

        Assert.assertTrue("should match same string", sameString.matches(actualString));
        Assert.assertTrue("describeTo should return correct string for strings", sameString.describeTo(mock(Description.class)).toString().equals("same(\"test string\")"));
    }

    @Test
    public void testDifferentStrings() {
        Same sameString = new Same("test string");
        String differentString = "different string";

        Assert.assertFalse("should not match different string", sameString.matches(differentString));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacter = new Same('a');
        Character actualCharacter = 'a';

        Assert.assertTrue("should match same character", sameCharacter.matches(actualCharacter));
        Assert.assertTrue("describeTo should return correct string for characters", sameCharacter.describeTo(mock(Description.class)).toString().equals("same('a')"));
    }

    @Test
    public void testDifferentCharacter() {
        Same sameCharacter = new Same('a');
        Character differentCharacter = 'b';

        Assert.assertFalse("should not match different character", sameCharacter.matches(differentCharacter));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWantedObject() {
        new Same(null);
    }
}