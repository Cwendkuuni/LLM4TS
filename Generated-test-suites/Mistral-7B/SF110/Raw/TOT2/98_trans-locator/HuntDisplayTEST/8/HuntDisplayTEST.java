import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.HuntDisplay;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }

    // Test getPreferredSize() and getMinimumSize() methods here.

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(huntDisplay.points.size(), 1);
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        huntDisplay.addLandMark(landmark);
        assertEquals(huntDisplay.landmarks.size(), 1);
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(trigPoint);
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        huntDisplay.addLandMark(landmark);

        HuntDisplay spy = spy(huntDisplay);
        MockGraphics graphics = powerMock.mock(Graphics.class);
        when(graphics.drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Component.class), any(Graphics2D.class)))
                .thenReturn(true);
        huntDisplay.paintComponent(graphics.getMock());

        verify(huntDisplay).repaint();
        verify(huntDisplay, times(trigPoints.length)).getWidth();
        verify(huntDisplay, times(trigPoints.length)).getHeight();
    }
}