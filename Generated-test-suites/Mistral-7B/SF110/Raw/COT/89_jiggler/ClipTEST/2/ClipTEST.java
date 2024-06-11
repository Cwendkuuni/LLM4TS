import junit.framework.TestCase;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @org.junit.Test
    public void testConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(1, 5);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(2.5f, 6.5f);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testApplyWithGrayImageAndROI_intMinAndIntMax() {
        GrayImage image = new BufferedImageGray(5, 5);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                image.set(x, y, (byte)x);
            }
        }

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max(x, y), 3);
                int actual = image.get(x, y).intValue();
                assertEquals("Pixel value is not correct", expected, actual);
            }
        }
    }

    @org.junit.Test
    public void testApplyWithGrayImageAndROI_floatMinAndFloatMax() {
        GrayImage image = new BufferedImageGray(5, 5);
        Clip clip = new Clip(2.5f, 6.5f);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                image.set(x, y, (byte)x);
            }
        }

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(x + 0.5f, y + 0.5f), 6.5f);
                float actual = image.get(x, y).floatValue();
                assertEquals("Pixel value is not correct", expected, actual, 0.001f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithGrayImageAndNegativeROI() {
        GrayImage image = new BufferedImageGray(5, 5);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(-1, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithGrayImageAndROIOutOfImageBounds() {
        GrayImage image = new BufferedImageGray(5, 5);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(5, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);
    }

    @org.junit.Test
    public void testApplyWithRealGrayImageAndROI_intMinAndIntMax() {
        RealGrayImage image = new BufferedRealGray(5, 5);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                image.set(x, y, (float)x / 255.0f);
            }
        }

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(x / 255.0f, y / 255.0f), 3.0f / 255.0f);
                float actual = image.get(x, y).floatValue();
                assertEquals("Pixel value is not correct", expected, actual, 0.001f);
            }
        }
    }

    @org.junit.Test
    public void testApplyWithRealGrayImageAndROI_floatMinAndFloatMax() {
        RealGrayImage image = new BufferedRealGray(5, 5);
        Clip clip = new Clip(2.5f, 6.5f);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                image.set(x, y, (float)x / 255.0f);
            }
        }

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(x / 255.0f + 0.001f, y / 255.0f + 0.001f), 6.5f / 255.0f);
                float actual = image.get(x, y).floatValue();
                assertEquals("Pixel value is not correct", expected, actual, 0.001f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithRealGrayImageAndNegativeROI() {
        RealGrayImage image = new BufferedRealGray(5, 5);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(-1, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithRealGrayImageAndROIOutOfImageBounds() {
        RealGrayImage image = new BufferedRealGray(5, 5);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(5, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);
    }

    @org.junit.Test
    public void testApplyWithColorImageAndROI_intMinAndIntMax() {
        ColorImage image = new RGBImage(5, 5, new int[5][5], new int[5][5], new int[5][5]);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                image.setPixel(x, y, new RGB(x, y, x));
            }
        }

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int[] rgb = image.getPixel(x, y);
                int r = Math.min(Math.max(rgb[0], x), 3);
                int g = Math.min(Math.max(rgb[1], y), 3);
                int b = Math.min(Math.max(rgb[2], x), 3);
                image.setPixel(x, y, new RGB(r, g, b));
            }
        }

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int[] rgbExpected = new int[]{Math.min(Math.max(x, y), 3), Math.min(Math.max(x, y), 3), Math.min(Math.max(x, y), 3)};
                int[] rgbActual = image.getPixel(x, y);
                assertArrayEquals("Pixel values are not correct", rgbExpected, rgbActual);
            }
        }
    }

    @org.junit.Test
    public void testApplyWithColorImageAndROI_floatMinAndFloatMax() {
        ColorImage image = new RGBImage(5, 5, new int[5][5], new int[5][5], new int[5][5]);
        Clip clip = new Clip(2.5f, 7.5f);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                image.setPixel(x, y, new RGB(x, y, x));
            }
        }

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float xf = x + 0.5f;
                float yf = y + 0.5f;
                int[] rgbExpected = new int[]{Math.min(Math.max((int)(xf), (int)(yf)), (int)(7.5f)), Math.min(Math.max((int)(xf), (int)(yf)), (int)(7.5f)), Math.min(Math.max((int)(xf), (int)(yf)), (int)(7.5f))};
                int[] rgbActual = image.getPixel(x, y);
                assertArrayEquals("Pixel values are not correct", rgbExpected, rgbActual);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithColorImageAndNegativeROI() {
        ColorImage image = new RGBImage(5, 5, new int[5][5], new int[5][5], new int[5][5]);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(-1, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithColorImageAndROIOutOfImageBounds() {
        ColorImage image = new RGBImage(5, 5, new int[5][5], new int[5][5], new int[5][5]);
        Clip clip = new Clip(1, 3);
        ROI roi = new ROI(5, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);
    }

    @org.junit.Test
    public void testMainMethod() throws Exception {
        String inputFile = "test_image.jpg";
        Float minValue = Float.valueOf(1.5f);
        Float maxValue = Float.valueOf(4.5f);
        String outputFile = "test_output.jpg";

        Clip clip = new Clip(minValue, maxValue);

        Image image = ImageIO.read(new File(inputFile));
        ImageOutputStream os = ImageIO.createImageOutputStream(new File(outputFile));

        clip.main(new String[]{inputFile.toString(), minValue.toString(), maxValue.toString(), outputFile});

        Image readOutput = ImageIO.read(new File(outputFile));
        assertEquals("Output image is not the same as expected", image, readOutput);

        ImageIO.close(image);
        ImageIO.close(readOutput);
        os.close();
    }
}