import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntegerConstructor() {
        int min = 10;
        int max = 20;
        Clip clip = new Clip(min, max);
        SimpleOperator simpleOperator = new SimpleOperator();
        GrayImage inputImage = new GrayImage(5, 5);
        assertThat(clip, is(instanceOf(Clip.class)));
        assertThat(clip.int_min, is(min));
        assertThat(clip.int_max, is(max));
        clip.apply(inputImage); // Testing apply method with this constructor
    }

    @Test
    public void testClipFloatConstructor() {
        float min = 1.5f;
        float max = 3.5f;
        Clip clip = new Clip(min, max);
        SimpleOperator simpleOperator = new SimpleOperator();
        GrayImage inputImage = new GrayImage(5, 5);
        clip.apply(inputImage); // Testing apply method with this constructor
        for (int y = 0; y < inputImage.Y(); ++y) {
            for (int x = 0; x < inputImage.X(); ++x) {
                float expected = Math.min(Math.max(inputImage.get(x, y), min), max);
                float actual = ((Float) inputImage.get(x, y)).floatValue();
                assertThat(actual, is(closeTo(expected, 0.001f)));
            }
        }
    }

    @Test
    public void testApplyGrayImage() {
        int min = 0;
        int max = 100;
        Clip clip = new Clip(min, max);
        GrayImage inputImage = new GrayImage(5, 5);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);
        clip.apply(inputImage, roi);
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max(inputImage.get(x, y), min), max);
                int actual = ((Integer) inputImage.get(x, y)).intValue();
                assertThat(actual, is(equalTo(expected)));
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        float min = 0.5f;
        float max = 1.5f;
        Clip clip = new Clip(min, max);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);
        clip.apply(inputImage, roi);
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(inputImage.get(x, y), min), max);
                float actual = ((Float) inputImage.get(x, y)).floatValue();
                assertThat(actual, is(closeTo(expected, 0.001f)));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithInvalidArguments() {
        Clip invalidClip = new Clip(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithInvalidArguments() {
        GrayImage inputImage = new GrayImage(5, 5);
        Clip clip = new Clip(1, 1);
        ROI invalidRoi = new ROI(-1, -1, inputImage.X() - 1, inputImage.Y() - 1);
        clip.apply(inputImage, invalidRio);
    }
}