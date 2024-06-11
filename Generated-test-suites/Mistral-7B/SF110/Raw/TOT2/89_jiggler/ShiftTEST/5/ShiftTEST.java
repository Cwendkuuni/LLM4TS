import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        Assert.assertEquals(2, shift.getXoffset());
        Assert.assertEquals(3, shift.getYoffset());
        Assert.assertTrue(shift.isWrap());
    }

    // Methods for other constructors and methods can be added here
}