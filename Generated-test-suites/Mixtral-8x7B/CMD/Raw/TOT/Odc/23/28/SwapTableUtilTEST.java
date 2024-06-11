import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnableWithManualSwapTypeAndRunningTaskAndFullTransferProgressAndUncheckedVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnableWithManualSwapTypeAndRunningTaskAndFullTransferProgressAndConsistentVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnableWithManualSwapTypeAndNotRunningTask() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.STOPPED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }
}