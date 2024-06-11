package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.GrayImage;
import jigl.image.ops.levelOps.Image;
import jigl.image.ops.levelOps.RealGrayImage;
import jigl.image.ops.levelOps.ROI;
import jigl.image.types.ByteType;
import jigl.image.types.FloatType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClipTest {

    private Image image;
    private Image image2;
    private ROI roi;
    private ImageInputStream is;
    private ImageOutputStream os;
    private Clip clip;

    @Before
    public void setup() {
        roi = new ROI(0, 0, 5, 5);
        image = mock(GrayImage.class);
        image2 = mock(GrayImage.class);
        when(image.get(ArgumentMatchers.intThat(i -> i >= 0 && i < 5 * 5))).thenReturn(0);
        is = mock(ImageInputStream.class);
        os = mock(ImageOutputStream.class);
        clip = new Clip(1.0f, 2.0f);
    }

    @Test
    public void testApplyGrayImage() {
        when(image.X()).thenReturn(5);
        when(image.Y()).thenReturn(5);

        when(image.getType()).thenReturn(ByteType.instance);

        clip.apply(image);

        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                int expectedValue = Math.min(Math.max(0, x + y), 4);
                int actualValue = ((Integer) image.get(x, y)).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        when(image.X()).thenReturn(5);
        when(image.Y()).thenReturn(5);

        when(image.getType()).thenReturn(FloatType.instance);

        clip.apply(image);

        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                float expectedValue = Math.min(Math.max(0.0f, x + y / 10.0f), 4.0f);
                float actualValue = ((Float) image.get(x, y)).floatValue();
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testApplyColorImage() {
        when(image.X()).thenReturn(5);
        when(image.Y()).thenReturn(5);

        clip = new Clip(1.0f, 2.0f);

        clip.apply(image);

        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                for (int channel = 0; channel < 3; ++channel) {
                    int expectedValue = Math.min(Math.max(0, x + y), 4);
                    int actualValue = ((Integer) image.get(x, y, channel)).intValue();
                    assertEquals(expectedValue, actualValue);
                }
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        when(image.X()).thenReturn(5);
        when(image.Y()).thenReturn(5);

        clip = new Clip(1.0f, 2.0f);

        clip.apply(image);

        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                for (int channel = 0; channel < 3; ++channel) {
                    float expectedValue = Math.min(Math.max(0.0f, x + y / 10.0f), 4.0f);
                    float actualValue = ((Float) image.get(x, y, channel)).floatValue();
                    assertEquals(expectedValue, actualValue, 0.001f);
                }
            }
        }
    }

    @Test
    public void testMain() throws Exception {
        when(is.read()).thenReturn(image);
        when(image.getType()).thenReturn(ByteType.instance);

        clip.main(new String[]{"1.0", "2.0", "input.jpg", "output.jpg"});

        assertNotNull(image2);
        assertNotNull(os);
    }
}