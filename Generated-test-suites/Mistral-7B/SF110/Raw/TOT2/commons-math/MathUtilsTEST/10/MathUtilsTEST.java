import org.apache.commons.math3.util.MathUtils; import org.junit.Test; import static org.junit.Assert.assertEquals;

public class MathUtilsTest {

 @Test public void testHashDouble() {
 double value = 1.2;
 int expected = 123; // Placeholder for actual hashcode
 int result = MathUtils.hash(value);
 assertEquals(expected, result);
 }

 // Add test cases for different input values and negative/large doubles
}