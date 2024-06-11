import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "test";
            }
        };

        when(execution.getProcessDefinitionId()).thenReturn("testProcess");
        when(execution.getCurrentActivityId()).thenReturn("testActivity");
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);

        assertEquals("test", delegate.get());
    }
}