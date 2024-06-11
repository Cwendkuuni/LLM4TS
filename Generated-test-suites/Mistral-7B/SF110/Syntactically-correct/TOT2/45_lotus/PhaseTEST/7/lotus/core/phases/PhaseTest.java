import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.throwsException;

public class PhaseTest {
    //...

    @Test(expected = AssertionError.class)
    public void testDoPhaseThrowsError() {
        Phase phase = new Phase() {
            @Override
            public void doPhase() {
                // This should not be called directly
            }
        };
        phase.doPhase();
    }
    //...
}