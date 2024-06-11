import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.RollEvent;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

 private Actions actions;
 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 actions = Mockito.spy(new Actions());
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true, true);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setActions(actions);
 Map.map.put("TestFiefdom", fiefdomObj);
 }

 @Test
 public void testPerformEmptyList() {
 vassalRebellion.perform(Collections.emptyList());
 Mockito.verify(actions).newRollEvent(fiefdomObj.getName());
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 List<String> input = new ArrayList<>();
 input.add("TestFiefdom");
 fiefdomObj.setLoyalVassals(10);
 fiefdomObj.setValue(5);
 fiefdomObj.setPersonalCastles(2);
 fiefdomObj.setFiefdomCastles(3);
 vassalRebellion.perform(input);
 assertEquals(15, fiefdomObj.getRebelliousVassals().size());
 Mockito.verify(actions, times(2)).newRollEvent(fiefdomObj.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testPerformWithNegativeLoyalVassals() {
 fiefdomObj.setLoyalVassals(-1);
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 }

 @Test(expected = IllegalStateException.class)
 public void testPerformWithNegativeDifference() {
 fiefdomObj.setLoyalVassals(5);
 fiefdomObj.setValue(3);
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 }

 @Test(expected = IllegalStateException.class)
 public void testPerformWithNegativePersonalCastlesAndFiefdomCastles() {
 fiefdomObj.setPersonalCastles(-1);
 fiefdomObj.setFiefdomCastles(-1);
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 List<String> input = new ArrayList<>();
 input.add("TestFiefdom");
 fiefdomObj.setLoyalVassals(0);
 vassalRebellion.perform(input);
 Mockito.verify(actions).newRollEvent(fiefdomObj.getName());
 }

 @Test
 public void testPerformWithSameCastles() {
 List<String> input = new ArrayList<>();
 input.add("TestFiefdom");
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);
 vassalRebellion.perform(input);
 assertEquals(12, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerformWithZeroCastles() {
 List<String> input = new ArrayList<>();
 input.add("TestFiefdom");
 fiefdomObj.setPersonalCastles(0);
 fiefdomObj.setFiefdomCastles(0);
 vassalRebellion.perform(input);
 assertEquals(10, fiefdomObj.getRebelliousVassals().size());
 }
}