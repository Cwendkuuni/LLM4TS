import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockValveContext;

public class ScheduleCheckOmsProjectValveTest {
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ValveContext valveContext;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        valveContext = new MockValveContext();
    }

    @Test
    public void testInvoke() {
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(valveContext).invokeNext(valveContext);
    }
}