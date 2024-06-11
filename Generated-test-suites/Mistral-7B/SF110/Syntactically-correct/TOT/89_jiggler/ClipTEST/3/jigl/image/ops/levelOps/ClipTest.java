import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import org.junit.Assert;
import org.junit.Test;

public class ClipTest {
    @Test
    public void testClipConstructorIntInt() {
        // Test valid constructor with positive values
        Clip clip = new Clip(5, 10);
        Assert.assertNotNull(clip);
        Assert.assertEquals(SimpleOperator.class, clip.getClass());
        Assert.assertEquals(5, clip.int_min);
        Assert.assertEquals(10, clip.int_max);
        Assert.assertNull(clip.float_min);
        Assert.assertNull(clip.float_max);

        // Test valid constructor with zero values
        clip = new Clip(0, 0);
        Assert.assertNotNull(clip);
        Assert.assertEquals(SimpleOperator.class, clip.getClass());
        Assert.assertEquals(0, clip.int_min);
        Assert.assertEquals(0, clip.int_max);
        Assert.assertNull(clip.float_min);
        Assert.assertNull(clip.float_max);

        // Test invalid constructor with negative values
        try {
            clip = new Clip(-5, 10);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            clip = new Clip(5, -10);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}