import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

    @Test
    public void testRenderRobot() {
        RobotRenderer renderer = new RobotRenderer();
        RobotSnapshot robotSnapshot = new RobotSnapshot(new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new RelativeAngle(0), new RelativeAngle(0), new RobotPosition(1, 1), "TestRobot", 50.0f, 100.0f, 50.0f, false, false, true, true, true, false, false);
        Graphics2D g2d = mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();

        renderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Add assertions here to check if the robot is being rendered correctly
        // when it is not dead
    }

    @Test
    public void testRenderDeadRobot() {
        RobotRenderer renderer = new RobotRenderer();
        RobotSnapshot robotSnapshot = new RobotSnapshot(new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new Length(10.0), new RelativeAngle(0), new RelativeAngle(0), new RobotPosition(1, 1), "TestRobot", 50.0f, 100.0f, 50.0f, true, false, true, true, true, false, false);
        Graphics2D g2d = mock(Graphics2D.class);
        Set<Integer> selectedRobotIds = new HashSet<>();

        renderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Add assertions here to check if the dead robot is not being rendered
    }
}