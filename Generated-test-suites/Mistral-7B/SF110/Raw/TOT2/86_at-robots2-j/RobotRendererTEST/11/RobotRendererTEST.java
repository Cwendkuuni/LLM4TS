import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static net.virtualinfinity.atrobots.gui.renderers.RobotRenderer.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RobotRendererTest {

    private RobotRenderer robotRenderer;
    private Graphics2D mockGraphics2D;
    private RobotSnapshot robotSnapshot;

    @BeforeAll
    public void setup() {
        robotRenderer = new RobotRenderer();
        mockGraphics2D = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot();
    }

    @Test
    public void testRender_RobotIsAlive() {
        when(mockGraphics2D.getFontMetrics()).thenReturn(mock(FontMetrics.class));
        robotRenderer.render(mockGraphics2D, robotSnapshot, Collections.emptySet());
        verify(mockGraphics2D, times(6)).fill(any(Shape.class));
        verify(mockGraphics2D, times(6)).draw(any(Shape.class));
    }

    // Test other traditional use cases and edge cases here
}