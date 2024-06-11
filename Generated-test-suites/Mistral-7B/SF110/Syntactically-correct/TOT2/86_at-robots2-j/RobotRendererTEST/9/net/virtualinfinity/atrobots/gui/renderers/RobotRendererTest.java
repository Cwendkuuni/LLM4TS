import org.junit.Test;
import static org.junit.Assert.*;

public class RobotRendererTest {
    @Test
    public void testIsRenderDead() {
        RobotRenderer renderer = new RobotRenderer();
        assertFalse(renderer.isRenderDead());

        RobotRenderer rendererDead = new RobotRenderer();
        rendererDead.setRenderDead(true);
        assertTrue(rendererDead.isRenderDead());
    }
}