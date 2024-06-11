import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockA;
 private Object mockB;
 private Object mockC;
 private Object mockD;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();

 mockA = mock(Map.class, returnsDeepStubs);
 mockB = mock(Number.class);
 mockC = mock(Number.class);
 mockD = mock(Number[].class);
 }

 @Test
 public void testDeepStubbingWithMap() {
 Map<String, Number> map = (Map<String, Number>) mockA;
 map.put("keyA", mockB);
 map.put("keyB", mockC);

 verify(mockB).equals(map.get("keyA"));
 verify(mockC).equals(map.get("keyB"));

 Map<String, Map<String, Number>> nestedMap = (Map<String, Map<String, Number>>) mockA;
 nestedMap.put("keyX", map);

 Map<String, Number> nestedMapValue = nestedMap.get("keyX");
 verify(mockB).equals(nestedMapValue.get("keyA"));
 verify(mockC).equals(nestedMapValue.get("keyB"));
 }

 @Test(expected = IllegalAccessError.class)
 public void testDeepStubbingWithNonMockableType() {
 Object nonMockableType = mock(String[].class, returnsDeepStubs);
 // This test case will fail with an IllegalAccessError as String[] is not mockable with Mockito
 }

 @Test
 public void testDeepStubbingWithListAndNestedMap() {
 List<Map<String, Number>> list = new ArrayList<>();
 list.add((Map<String, Number>) mockA);

 Map<String, Number> nestedMap = new HashMap<>();
 nestedMap.put("keyX", mockB);
 nestedMap.put("keyY", mockC);
 list.set(0, nestedMap);

 Object deepStubbedList = (List<Map<String, Number>>) mockA;
 deepStubbedList.set(0, mockB);

 verify(mockB).equals(list.get(0).get("keyX"));
 verify(mockC).equals(list.get(0).get("keyY"));
 }
}