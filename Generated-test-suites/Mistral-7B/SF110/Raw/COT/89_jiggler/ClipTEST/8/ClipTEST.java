import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.ImageType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

    @Test
    public void testClipIntConstructor() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        assertNotNull(clip);
    }

    @Test
    public void testClipFloatConstructor() {
        float min = 5.5f;
        float max = 10.5f;
        Clip clip = new Clip(min, max);
        assertNotNull(clip);
    }

    @Test
    public void testApplyGrayImageIntMinIntMax() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        GrayImage image = new GrayImage(new int[5][5], ImageType.BYTE_GRAY);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        int expectedMin = Math.min(5, min);
        int expectedMax = Math.min(5, max);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int actualValue = image.get(x, y).intValue();
                int expectedValue = Math.min(Math.max(actualValue, expectedMin), expectedMax);
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageFloatMinFloatMax() {
        float min = 5.5f;
        float max = 10.5f;
        Clip clip = new Clip(min, max);
        RealGrayImage image = new RealGrayImage(new float[5][5], ImageType.FLOAT_GRAY);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        float expectedMin = Math.min(5.5f, min);
        float expectedMax = Math.min(5.5f, max);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float actualValue = image.get(x, y).floatValue();
                float expectedValue = Math.min(Math.max(actualValue, expectedMin), expectedMax);
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageNullImage() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        ROI roi = new ROI(0, 0, 5, 5);
        clip.apply(null, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyRealGrayImageNullImage() {
        float min = 5.5f;
        float max = 10.5f;
        Clip clip = new Clip(min, max);
        ROI roi = new ROI(0, 0, 5, 5);
        clip.apply(null, roi);
    }

    @Test
    public void testApplyColorImage() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        ColorImage image = new ColorImage(new int[5][5][3], ImageType.BYTE_RGB);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image);

        for (int plane = 0; plane < 3; plane++) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    int[] actualValues = image.get(x, y);
                    for (int i = 0; i < 3; i++) {
                        int expectedMin = Math.min(Math.min(actualValues[i], min), 0);
                        int expectedMax = Math.min(Math.min(actualValues[i], max), 255);
                        assertEquals(expectedMin, actualValues[i]);
                    }
                }
            }
        }
    }

    @Test
    public void testMainMethodValidInput() {
        float min = 5.5f;
        float max = 10.5f;
        String inputFile = "input.jpg";
        String outputFile = "output.jpg";
        String[] argv = {"5.5", "10.5", inputFile, outputFile};
        Clip.main(argv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainMethodNullInputImageStream() {
        float min = 5.5f;
        float max = 10.5f;
        String inputFile = null;
        String outputFile = "output.jpg";
        String[] argv = {"5.5", "10.5", inputFile, outputFile};
        Clip.main(argv);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMainMethodInvalidInputImageFile() {
        float min = 5.5f;
        float max = 10.5f;
        String inputFile = "invalid_input.jpg";
        String outputFile = "output.jpg";
        String[] argv = {"5.5", "10.5", inputFile, outputFile};
        Clip.main(argv);
    }
}