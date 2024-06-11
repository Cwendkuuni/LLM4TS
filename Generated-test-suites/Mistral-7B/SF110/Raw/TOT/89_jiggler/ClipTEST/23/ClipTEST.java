import static org.junit.Assert.assertEquals;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.ROI;
import jigl.image.types.GrayImage;

public class ClipTest {
    @org.junit.Test
    public void testApplyGrayImageInt() {
        // Given
        GrayImage originalImage = new GrayImage(new int[10][10], 10, 10);
        Clip clip = new Clip(5, 7);

        // When
        GrayImage resultImage = clip.apply(originalImage);

        // Then
        for (int y = 0; y < resultImage.Y(); ++y) {
            for (int x = 0; x < resultImage.X(); ++x) {
                int expectedValue = Math.min(Math.max(originalImage.get(x, y), 5), 7);
                int actualValue = resultImage.get(x, y).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }
}