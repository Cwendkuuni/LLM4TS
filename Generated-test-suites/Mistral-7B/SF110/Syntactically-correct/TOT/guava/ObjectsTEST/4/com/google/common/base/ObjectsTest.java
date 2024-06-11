import static org.junit.Assert.assertEquals;

public class ObjectsTest {
    @org.junit.Test
    public void testEqual() {
        // Test with null objects
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal(null, new Object()));
        assertEquals(false, Objects.equal(new Object(), null));

        // Test with same object
        String str1 = new String("test");
        assertEquals(true, Objects.equal(str1, str1));

        // Test with equal objects of different classes
        Integer int1 = new Integer(1);
        Float float1 = new Float(1.0);
        assertEquals(true, Objects.equal(int1, float1));

        // Test with non-equal objects
        String str1_1 = new String("test1");
        String str2 = new String("test2");
        assertEquals(false, Objects.equal(str1, str2));

        // Test with different classes
        Integer int1_1 = new Integer(1);
        String str1_2 = new String("test");
        assertEquals(false, Objects.equal(int1, str1_2));
    }
}