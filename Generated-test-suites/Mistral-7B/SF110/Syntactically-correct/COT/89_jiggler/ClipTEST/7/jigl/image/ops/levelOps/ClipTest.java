import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.IntegerImageType;
import jigl.image.types.RealGrayImageType;

public class ClipTest {

    @org.junit.Test
    public void testClipIntConstructor() {
        Clip clip = new Clip(5, 10);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testClipFloatConstructor() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testApplyGrayImageIntMinIntMax() {
        Clip clip = new Clip(0, 10);
        GrayImage image = new GrayImage(10, 10, new IntegerImageType());
        image.setPixel(0, 0, 15);
        image.setPixel(0, 1, 7);
        image.setPixel(0, 2, 12);

        GrayImage result = clip.apply(image);

        assertEquals(10, result.getPixel(0, 0).getValue());
        assertEquals(7, result.getPixel(0, 1).getValue());
        assertEquals(10, result.getPixel(0, 2).getValue());
    }

    @org.junit.Test
    public void testApplyGrayImageNegativeMinIntMax() {
        Clip clip = new Clip(-5, 10);
        GrayImage image = new GrayImage(10, 10, new IntegerImageType());
        image.setPixel(0, 0, -15);
        image.setPixel(0, 1, 7);
        image.setPixel(0, 2, 12);

        GrayImage result = clip.apply(image);

        assertEquals(0, result.getPixel(0, 0).getValue());
        assertEquals(7, result.getPixel(0, 1).getValue());
        assertEquals(10, result.getPixel(0, 2).getValue());
    }

    @org.junit.Test
    public void testApplyGrayImageZeroMinZeroMax() {
        Clip clip = new Clip(0, 0);
        GrayImage image = new GrayImage(10, 10, new IntegerImageType());
        image.setPixel(0, 0, 15);
        image.setPixel(0, 1, 7);
        image.setPixel(0, 2, 12);

        GrayImage result = clip.apply(image);

        assertEquals(15, result.getPixel(0, 0).getValue());
        assertEquals(7, result.getPixel(0, 1).getValue());
        assertEquals(12, result.getPixel(0, 2).getValue());
    }

    @org.junit.Test
    public void testApplyGrayImageNoROI() {
        Clip clip = new Clip(0, 10);
        GrayImage image = new GrayImage(10, 10, new IntegerImageType());
        image.setPixel(0, 0, 15);
        image.setPixel(0, 1, 7);
        image.setPixel(0, 2, 12);

        try {
            clip.apply(image);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testApplyRealGrayImageFloatMinFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        RealGrayImage image = new RealGrayImage(10, 10, new RealGrayImageType());
        image.setPixel(0, 0, 15.5f);
        image.setPixel(0, 1, 7.2f);
        image.setPixel(0, 2, 12.8f);

        RealGrayImage result = clip.apply(image);

        assertEquals(7.5f, result.getPixel(0, 0).getValue(), 0.01f);
        assertEquals(7.2f, result.getPixel(0, 1).getValue(), 0.01f);
        assertEquals(10.0f, result.getPixel(0, 2).getValue(), 0.01f);
    }

    @org.junit.Test
    public void testApplyRealGrayImageNegativeMinFloatMax() {
        Clip clip = new Clip(-2.5f, 7.5f);
        RealGrayImage image = new RealGrayImage(10, 10, new RealGrayImageType());
        image.setPixel(0, 0, -15.5f);
        image.setPixel(0, 1, 7.2f);
        image.setPixel(0, 2, 12.8f);

        RealGrayImage result = clip.apply(image);

        assertEquals(2.5f, result.getPixel(0, 0).getValue(), 0.01f);
        assertEquals(7.2f, result.getPixel(0, 1).getValue(), 0.01f);
        assertEquals(10.0f, result.getPixel(0, 2).getValue(), 0.01f);
    }

    @org.junit.Test
    public void testApplyRealGrayImageZeroMinZeroMax() {
        Clip clip = new Clip(0.0f, 0.0f);
        RealGrayImage image = new RealGrayImage(10, 10, new RealGrayImageType());
        image.setPixel(0, 0, 15.5f);
        image.setPixel(0, 1, 7.2f);
        image.setPixel(0, 2, 12.8f);

        RealGrayImage result = clip.apply(image);

        assertEquals(15.5f, result.getPixel(0, 0).getValue(), 0.01f);
        assertEquals(7.2f, result.getPixel(0, 1).getValue(), 0.01f);
        assertEquals(12.8f, result.getPixel(0, 2).getValue(), 0.01f);
    }

    @org.junit.Test
    public void testApplyRealGrayImageNoROI() {
        Clip clip = new Clip(2.5f, 7.5f);
        RealGrayImage image = new RealGrayImage(10, 10, new RealGrayImageType());
        image.setPixel(0, 0, 15.5f);
        image.setPixel(0, 1, 7.2f);
        image.setPixel(0, 2, 12.8f);

        try {
            clip.apply(image);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 10);
        ColorImage image = new ColorImage(10, 10, new IntegerImageType(), new IntegerImageType(), new IntegerImageType());
        image.setPlane(0, new GrayImage(10, 10, new IntegerImageType()) {{
            setPixel(0, 0, 15);
            setPixel(0, 1, 7);
            setPixel(0, 2, 12);
        }});
        image.setPlane(1, new GrayImage(10, 10, new IntegerImageType()) {{
            setPixel(0, 0, 15);
            setPixel(0, 1, 7);
            setPixel(0, 2, 12);
        }});
        image.setPlane(2, new GrayImage(10, 10, new IntegerImageType()) {{
            setPixel(0, 0, 15);
            setPixel(0, 1, 7);
            setPixel(0, 2, 12);
        }});

        ColorImage result = clip.apply(image);

        assertEquals(10, result.getPlane(0).getPixel(0, 0).getValue());
        assertEquals(7, result.getPlane(0).getPixel(0, 1).getValue());
        assertEquals(10, result.getPlane(0).getPixel(0, 2).getValue());
        assertEquals(10, result.getPlane(1).getPixel(0, 0).getValue());
        assertEquals(7, result.getPlane(1).getPixel(0, 1).getValue());
        assertEquals(10, result.getPlane(1).getPixel(0, 2).getValue());
        assertEquals(10, result.getPlane(2).getPixel(0, 0).getValue());
        assertEquals(7, result.getPlane(2).getPixel(0, 1).getValue());
        assertEquals(10, result.getPlane(2).getPixel(0, 2).getValue());
    }

    @org.junit.Test
    public void testApplyColorImageNoROI() {
        Clip clip = new Clip(0, 10);
        ColorImage image = new ColorImage(10, 10, new IntegerImageType(), new IntegerImageType(), new IntegerImageType());
        image.setPlane(0, new GrayImage(10, 10, new IntegerImageType()) {{
            setPixel(0, 0, 15);
            setPixel(0, 1, 7);
            setPixel(0, 2, 12);
        }});
        image.setPlane(1, new GrayImage(10, 10, new IntegerImageType()) {{
            setPixel(0, 0, 15);
            setPixel(0, 1, 7);
            setPixel(0, 2, 12);
        }});
        image.setPlane(2, new GrayImage(10, 10, new IntegerImageType()) {{
            setPixel(0, 0, 15);
            setPixel(0, 1, 7);
            setPixel(0, 2, 12);
        }});

        try {
            clip.apply(image);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(2.5f, 7.5f);
        RealColorImage image = new RealColorImage(10, 10, new RealGrayImageType(), new RealGrayImageType(), new RealGrayImageType());
        image.setPlane(0, new RealGrayImage(10, 10, new RealGrayImageType()) {{
            setPixel(0, 0, 15.5f);
            setPixel(0, 1, 7.2f);
            setPixel(0, 2, 12.8f);
        }});
        image.setPlane(1, new RealGrayImage(10, 10, new RealGrayImageType()) {{
            setPixel(0, 0, 15.5f);
            setPixel(0, 1, 7.2f);
            setPixel(0, 2, 12.8f);
        }});
        image.setPlane(2, new RealGrayImage(10, 10, new RealGrayImageType()) {{
            setPixel(0, 0, 15.5f);
            setPixel(0, 1, 7.2f);
            setPixel(0, 2, 12.8f);
        }});

        RealColorImage result = clip.apply(image);

        assertEquals(7.5f, result.getPlane(0).getPixel(0, 0).getValue(), 0.01f);
        assertEquals(7.2f, result.getPlane(0).getPixel(0, 1).getValue(), 0.01f);
        assertEquals(10.0f, result.getPlane(0).getPixel(0, 2).getValue(), 0.01f);
        assertEquals(7.5f, result.getPlane(1).getPixel(0, 0).getValue(), 0.01f);
        assertEquals(7.2f, result.getPlane(1).getPixel(0, 1).getValue(), 0.01f);
        assertEquals(10.0f, result.getPlane(1).getPixel(0, 2).getValue(), 0.01f);
        assertEquals(10.0f, result.getPlane(2).getPixel(0, 0).getValue(), 0.01f);
        assertEquals(7.2f, result.getPlane(2).getPixel(0, 1).getValue(), 0.01f);
        assertEquals(10.0f, result.getPlane(2).getPixel(0, 2).getValue(), 0.01f);
    }

    @org.junit.Test
    public void testApplyRealColorImageNoROI() {
        Clip clip = new Clip(2.5f, 7.5f);
        RealColorImage image = new RealColorImage(10, 10, new RealGrayImageType(), new RealGrayImageType(), new RealGrayImageType());
        image.setPlane(0, new RealGrayImage(10, 10, new RealGrayImageType()) {{
            setPixel(0, 0, 15.5f);
            setPixel(0, 1, 7.2f);
            setPixel(0, 2, 12.8f);
        }});
        image.setPlane(1, new RealGrayImage(10, 10, new RealGrayImageType()) {{
            setPixel(0, 0, 15.5f);
            setPixel(0, 1, 7.2f);
            setPixel(0, 2, 12.8f);
        }});
        image.setPlane(2, new RealGrayImage(10, 10, new RealGrayImageType()) {{
            setPixel(0, 0, 15.5f);
            setPixel(0, 1, 7.2f);
            setPixel(0, 2, 12.8f);
        }});

        try {
            clip.apply(image);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}