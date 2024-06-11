import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ColorImage;
import jigl.image.ops.RealColorImage;
import jigl.image.types.ROI;

public class ClipTest {

    @Test
    public void testConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(5, 10);
        assertNotNull(clip);
    }

    @Test
    public void testConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertNotNull(clip);
    }

    @Test
    public void testApplyGrayImageWithIntMinAndIntMax() {
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(5, 10);

        image.set(0, 0, 15);
        image.set(0, 1, 8);
        image.set(1, 0, 2);
        image.set(1, 1, 12);

        GrayImage expected = new GrayImage(5, 5);
        expected.set(0, 0, 10);
        expected.set(0, 1, 10);
        expected.set(1, 0, 5);
        expected.set(1, 1, 5);

        GrayImage actual = (GrayImage) clip.apply(image);
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testApplyGrayImageWithFloatMinAndFloatMax() {
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(2.5f, 7.5f);

        image.set(0, 0, 15.5f);
        image.set(0, 1, 8.5f);
        image.set(1, 0, 2.5f);
        image.set(1, 1, 12.5f);

        GrayImage expected = new GrayImage(5, 5);
        expected.set(0, 0, 7.5f);
        expected.set(0, 1, 7.5f);
        expected.set(1, 0, 2.5f);
        expected.set(1, 1, 2.5f);

        GrayImage actual = (GrayImage) clip.apply(image);
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testApplyRealGrayImageWithIntMinAndIntMax() {
        RealGrayImage image = new RealGrayImage(10, 10);
        Clip clip = new Clip(5, 10);

        image.set(0, 0, 15.5);
        image.set(0, 1, 8.5);
        image.set(1, 0, 2.5);
        image.set(1, 1, 12.5);

        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(0, 0, 10);
        expected.set(0, 1, 10);
        expected.set(1, 0, 5);
        expected.set(1, 1, 5);

        RealGrayImage actual = (RealGrayImage) clip.apply(image);
        assertArrayEquals(expected.getData(), actual.getData(), 0.01);
    }

    @Test
    public void testApplyRealGrayImageWithFloatMinAndFloatMax() {
        RealGrayImage image = new RealGrayImage(10, 10);
        Clip clip = new Clip(2.5f, 7.5f);

        image.set(0, 0, 15.5f);
        image.set(0, 1, 8.5f);
        image.set(1, 0, 2.5f);
        image.set(1, 1, 12.5f);

        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(0, 0, 7.5f);
        expected.set(0, 1, 7.5f);
        expected.set(1, 0, 2.5f);
        expected.set(1, 1, 2.5f);

        RealGrayImage actual = (RealGrayImage) clip.apply(image);
        assertArrayEquals(expected.getData(), actual.getData(), 0.01);
    }

    @Test
    public void testApplyColorImageWithMinIntAndMaxInt() {
        ColorImage image = new ColorImage(10, 10, 3);
        Clip clip = new Clip(5, 10);

        image.setPlane(0, new GrayImage(10, 10) {{
            set(0, 0, 15);
            set(0, 1, 8);
            set(1, 0, 2);
            set(1, 1, 12);
        }});
        image.setPlane(1, new GrayImage(10, 10) {{
            set(0, 0, 25);
            set(0, 1, 18);
            set(1, 0, 3);
            set(
1, 1, 15);
        }});
        image.setPlane(2, new GrayImage(10, 10) {{
            set(0, 0, 10);
            set(0, 1, 6);
            set(1, 0, 15);
            set(1, 1, 11);
        }});

        ColorImage expected = new ColorImage(5, 5, 3);
        expected.setPlane(0, new GrayImage(5, 5) {{
            set(0, 0, 10);
            set(0, 1, 10);
            set(1, 0, 5);
            set(1, 1, 5);
        }});
        expected.setPlane(1, new GrayImage(5, 5) {{
            set(0, 0, 10);
            set(0, 1, 10);
            set(1, 0, 5);
            set(1, 1, 5);
        }});
        expected.setPlane(2, new GrayImage(5, 5) {{
            set(0, 0, 5);
            set(0, 1, 5);
            set(1, 0, 5);
            set(1, 1, 5);
        }});

        ColorImage actual = clip.apply(image);
        assertArrayEquals(expected.getData(0), actual.getData(0));
        assertArrayEquals(expected.getData(1), actual.getData(1));
        assertArrayEquals(expected.getData(2), actual.getData(2));
    }

    @Test
    public void testApplyColorImageWithMinFloatAndMaxFloat() {
        ColorImage image = new ColorImage(10, 10, 3);
        Clip clip = new Clip(2.5f, 7.5f);

        image.setPlane(0, new GrayImage(10, 10) {{
            set(0, 0, 15.5f);
            set(0, 1, 8.5f);
            set(1, 0, 2.5f);
            set(1, 1, 12.5f);
        }});
        image.setPlane(1, new GrayImage(10, 10) {{
            set(0, 0, 25.5f);
            set(0, 1, 18.5f);
            set(1, 0, 3.5f);
            set(1, 1, 15.5f);
        }});
        image.setPlane(2, new GrayImage(10, 10) {{
            set(0, 0, 10.5f);
            set(0, 1, 6.5f);
            set(1, 0, 15.5f);
            set(1, 1, 11.5f);
        }});

        ColorImage expected = new ColorImage(5, 5, 3);
        expected.setPlane(0, new GrayImage(5, 5) {{
            set(0, 0, 7.5f);
            set(0, 1, 7.5f);
            set(1, 0, 2.5f);
            set(1, 1, 2.5f);
        }});
        expected.setPlane(1, new GrayImage(5, 5) {{
            set(0, 0, 7.5f);
            set(0, 1, 7.5f);
            set(1, 0, 2.5f);
            set(1, 1, 2.5f);
        }});
        expected.setPlane(2, new GrayImage(5, 5) {{
            set(0, 0, 2.5f);
            set(0, 1, 2.5f);
            set(1, 0, 2.5f);
            set(1, 1, 2.5f);
        }});

        ColorImage actual = clip.apply(image);
        assertArrayEquals(expected.getData(0), actual.getData(0), 0.01);
        assertArrayEquals(expected.getData(1), actual.getData(1), 0.01);
        assertArrayEquals(expected.getData(2), actual.getData(2), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        Clip clip = new Clip(2.5f, 7.5f);
        clip.apply(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() {
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(2.5f, 7.5f);
        clip.apply(image, null);
    }
}