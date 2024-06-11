import junit.framework.TestCase;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @org.junit.Test
    public void testConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(5, 10);
        assertEquals("int_min should be set correctly", 5, clip.int_min);
        assertEquals("int_max should be set correctly", 10, clip.int_max);
    }

    @org.junit.Test
    public void testConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertEquals("float_min should be set correctly", 2.5f, clip.float_min);
        assertEquals("float_max should be set correctly", 7.5f, clip.float_max);
    }

    @org.junit.Test
    public void testApplyGrayImageWithIntMinAndIntMax() {
        GrayImage image = new GrayImage(new byte[10][10]);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (byte) (x + y));
            }
        }

        Clip clip = new Clip(5, 10);
        Image result = clip.apply(image);

        // Check that the pixel values are within the specified range
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int value = ((GrayImage) result).get(x, y).intValue();
                assertTrue("Pixel value should be within the range: " + value, value >= 5 && value <= 10);
            }
        }
    }

    @org.junit.Test
    public void testApplyGrayImageWithFloatMinAndFloatMax() {
        GrayImage image = new GrayImage(new byte[10][10]);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (byte) (x + y));
            }
        }

        Clip clip = new Clip(2.5f, 7.5f);
        Image result = clip.apply(image);

        // Check that the pixel values are within the specified range
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = ((GrayImage) result).get(x, y).floatValue();
                assertTrue("Pixel value should be within the range: " + value, value >= 2.5f && value <= 7.5f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageWithInvalidMin() {
        GrayImage image = new GrayImage(new byte[10][10]);
        Clip clip = new Clip(0, 10);
        clip.apply(image);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageWithInvalidMax() {
        GrayImage image = new GrayImage(new byte[10][10]);
        Clip clip = new Clip(10, 0);
        clip.apply(image);
    }

    @org.junit.Test
    public void testApplyRealGrayImageWithIntMinAndIntMax() {
        RealGrayImage image = new RealGrayImage(new float[10][10]);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, x + y * 0.5f);
            }
        }

        Clip clip = new Clip(5, 10);
        Image result = clip.apply(image);

        // Check that the pixel values are within the specified range
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = ((RealGrayImage) result).get(x, y).floatValue();
                assertTrue("Pixel value should be within the range: " + value, value >= 5 && value <= 10);
            }
        }
    }

    @org.junit.Test
    public void testApplyRealGrayImageWithFloatMinAndFloatMax() {
        RealGrayImage image = new RealGrayImage(new float[10][10]);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, x + y * 0.5f);
            }
        }

        Clip clip = new Clip(2.5f, 7.5f);
        Image result = clip.apply(image);

        // Check that the pixel values are within the specified range
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = ((RealGrayImage) result).get(x, y).floatValue();
                assertTrue("Pixel value should be within the range: " + value, value >= 2.5f && value <= 7.5f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyRealGrayImageWithInvalidMin() {
        RealGrayImage image = new RealGrayImage(new float[10][10]);
        Clip clip = new Clip(0, 10);
        clip.apply(image);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyRealGrayImageWithInvalidMax() {
        RealGrayImage image = new RealGrayImage(new float[10][10]);
        Clip clip = new Clip(10, 0);
        clip.apply(image);
    }

    @org.junit.Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(new byte[10][10][3], new ImageMetaInfo());
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                for (int c = 0; c < 3; c++) {
                    image.set(x, y, c, (byte) (x + y * 0.1f));
                }
            }
        }

        Clip clip = new Clip(2.5f, 7.5f);
        Image result = clip.apply(image);

        // Check that each plane's pixel values are within the specified range
        for (int plane = 0; plane < 3; plane++) {
            for (int y = 0; y < result.Y(); y++) {
                for (int x = 0; x < result.X(); x++) {
                    float value = ((GrayImage) result.plane(plane)).get(x, y).floatValue();
                    assertTrue("Pixel value should be within the range: " + value, value >= 2.5f && value <= 7.5f);
                }
            }
        }
    }

    @org.junit.Test
    public void testMain() {
        try {
            String inputFile = "input.jpg";
            Float minValue = Float.valueOf(0.5f);
            Float maxValue = Float.valueOf(2.5f);
            String outputFile = "output.jpg";

            Clip clip = new Clip(minValue, maxValue);

            Image image = new ImageIOUtils().read(inputFile);
            Image result = clip.apply(image);

            ImageIOUtils.write(result, image.fileFormat(), outputFile);

        } catch (Exception e) {
            fail("Main method should not throw an exception: " + e.getMessage());
        }
    }
}