package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {
    @Test
    public void testClipIntInt() {
        GrayImage inputImage = new ByteArrayGrayImage(new byte[]{}, 1, 1);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);

        Clip clip = new Clip(5, 10);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max(inputImage.get(x, y).intValue(), 5), 10);
                int actual = ((Integer) outputImage.get(x, y)).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testClipIntInt_negativeValues() {
        GrayImage inputImage = new ByteArrayGrayImage(new byte[]{-1, -2, -3}, 1, 1);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);

        Clip clip = new Clip(5, 10);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max(inputImage.get(x, y).intValue(), 5), 10);
                int actual = ((Integer) outputImage.get(x, y)).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage inputImage = new ByteArrayRealGrayImage(new byte[]{}, 1, 1);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);

        Clip clip = new Clip(5.5f, 10.5f);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(inputImage.get(x, y).floatValue(), 5.5f), 10.5f);
                float actual = ((Float) outputImage.get(x, y)).floatValue();
                assertEquals(expected, actual, 0.01f);
            }
        }
    }

    @Test
    public void testClipFloatFloat_negativeValues() {
        RealGrayImage inputImage = new ByteArrayRealGrayImage(new byte[]{-1.5f, -2.5f, -3.5f}, 1, 1);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);

        Clip clip = new Clip(5.5f, 10.5f);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(inputImage.get(x, y).floatValue(), 5.5f), 10.5f);
                float actual = ((Float) outputImage.get(x, y)).floatValue();
                assertEquals(expected, actual, 0.01f);
            }
        }
    }

    @Test
    public void testApplyToColorImage() {
        ColorImage inputImage = new ByteArrayColorImage(new byte[][]{
            {0, 0, 0},
            {255, 255, 255}
        }, 2, 1);

        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);

        Clip clip = new Clip(5, 10);
        Image outputImage = clip.apply(inputImage, roi);

        for (int plane = 0; plane < inputImage.numPlanes(); ++plane) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    int[] expected = new int[inputImage.numBands()];
                    int[] actual = new int[inputImage.numBands()];

                    inputImage.getPixel(x, y, expected);
                    outputImage.getPixel(x, y, actual);

                    for (int band = 0; band < inputImage.numBands(); ++band) {
                        int bandValue = expected[band];
                        int bandValueClipped = Math.min(Math.max(bandValue, 5), 10);
                        assertEquals(bandValueClipped, actual[band]);
                    }
                }
            }
        }
    }

    @Test
    public void testMain() {
        String inputFile = "testInput.jpg";
        String outputFile = "testOutput.jpg";

        Float f_val1 = Float.valueOf(3.5f);
        Float f_val2 = Float.valueOf(7.5f);

        String[] argv = new String[]{"3.5", "7.5", inputFile, outputFile};

        Clip clip = new Clip(f_val1, f_val2);
        clip.main(argv);

        Image inputImage = ImageIO.read(new File(inputFile));
        Image outputImage = ImageIO.read(new File(outputFile));

        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                float expected = Math.min(Math.max(inputImage.get(x, y).floatValue(), f_val1.floatValue()), f_val2.floatValue());
                float actual = outputImage.get(x, y).floatValue();
                assertEquals(expected, actual, 0.01f);
            }
        }

        new File(outputFile).delete();
    }
}