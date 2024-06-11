import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdoms;
 private Actions actions;

 @Before
 public void setup() {
 fiefdoms = Mockito.spy(new Fiefdoms());
 actions = Mockito.spy(new Actions());
 vassalRebellion = new VassalRebellion() {
 @Override
 public void perform(Collection<String> col) {
 super.perform(col);
 }
 };
 vassalRebellion.setActions(actions);
 vassalRebellion.setFiefdomObj(fiefdoms);
 }

 @Test
 public void testPerformWithLoyalVassalsAndPersonalCastle() {
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put("TestFiefdom", fiefdoms);
 Mockito.when(fiefdoms.getName()).thenReturn("TestFiefdom");
 Mockito.when(fiefdoms.getPersonalCastles()).thenReturn(1);
 Mockito.when(fiefdoms.getFiefdomCastles()).thenReturn(1);
 Mockito.when(fiefdoms.getLoyalVassals()).thenReturn(Collections.singletonList("TestVassal"));
 Mockito.when(fiefdoms.getValue()).thenReturn(1);
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertEquals(2, fiefdoms.getRebelliousVassals().size());
 }

 @Test
 public void testPerformWithLoyalVassalsAndNoCastles() {
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put("TestFiefdom", fiefdoms);
 Mockito.when(fiefdoms.getName()).thenReturn("TestFiefdom");
 Mockito.when(fiefdoms.getPersonalCastles()).thenReturn(0);
 Mockito.when(fiefdoms.getFiefdomCastles()).thenReturn(0);
 Mockito.when(fiefdoms.getLoyalVassals()).thenReturn(Collections.singletonList("TestVassal"));
 Mockito.when(fiefdoms.getValue()).thenReturn(1);
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertEquals(1, fiefdoms.getRebelliousVassals().size());
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put("TestFiefdom", fiefdoms);
 Mockito.when(fiefdoms.getName()).thenReturn("TestFiefdom");
 Mockito.when(fiefdoms.getPersonalCastles()).thenReturn(0);
 Mockito.when(fiefdoms.getFiefdomCastles()).thenReturn(0);
 Mockito.when(fiefdoms.getLoyalVassals()).thenReturn(Collections.emptyList());
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertTrue(new RollEvent("TestFiefdom").isTriggered());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyCollection() {
 vassalRebbellion.perform(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullCollection() {
 vassalRebbellion.perform(null);
 }
}