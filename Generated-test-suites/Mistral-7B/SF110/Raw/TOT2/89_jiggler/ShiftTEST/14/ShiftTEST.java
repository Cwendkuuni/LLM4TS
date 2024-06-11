import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift sh = new Shift(1, 1, 0);
        assertNotNull(sh);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeX() {
        Shift sh = new Shift(-1, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeY() {
        Shift sh = new Shift(1, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonIntX() {
        Shift sh = new Shift(1.5, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonIntY() {
        Shift sh = new Shift(1, 1.5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonIntWrap() {
        Shift sh = new Shift(1, 1, 2.5);
    }

    @Test
    public void testConstructorWithWrap() {
        Shift sh = new Shift(1, 1, 1);
        assertTrue(sh.isWrap());
    }

    @Test
    public void testApplyGrayImageWithNoWrap() {
        // test case for apply method in Shift class with noWrap
    }

    @Test
    public void testApplyGrayImageWithWrap() {
        // test case for apply method in Shift class with wrap
    }

    // test cases for apply methods in Shift class with RealGrayImage, ColorImage and RealColorImage
}