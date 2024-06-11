import org.junit.Test;
import org.apache.commons.math3.fraction.Fraction;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testDoubleConstructor() {
        Fraction fraction = new Fraction(1.5);
        assertEquals("1/2", fraction.toString());
    }
}