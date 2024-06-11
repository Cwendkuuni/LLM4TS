import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable_TypicalUseCase() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = true;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_EdgeCase_FullVerificationResult_UNCHECK() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
boolean manualSwapTableStarted = false;

boolean expectedValue = true;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_ExceptionCase_NonManualSwapTableType() {
SwapTableType swapTableType = SwapTableType.AUTO;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_ExceptionCase_ManualSwapTableStarted() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = true;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_ExceptionCase_NonRunningTaskStatus() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.FINISHED;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_ExceptionCase_FullTransferProgressPercentage_Not100() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 99.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}
}