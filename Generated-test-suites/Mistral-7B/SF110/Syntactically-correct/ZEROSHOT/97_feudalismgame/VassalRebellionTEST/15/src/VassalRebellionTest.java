package src.test;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomObj = new Fiefdoms("TestFiefdom");
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);
 fiefdomObj.setValue(5);
 Map.map.put("TestFiefdom", fiefdomObj);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassalsPresent_MinimumConditionMet() {
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertEquals(5, fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_LoyalVassalsPresent_ConditionNotMet() {
 fiefdomObj.setValue(4);
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertEquals(fiefdomObj.getLoyalVassals().size(), fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_LoyalVassalsPresent_CastlesPresent() {
 fiefdomObj.setPersonalCastles(2);
 fiefdomObj.setFiefdomCastles(2);
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertEquals(fiefdomObj.getLoyalVassals().size() - (2 * 10 + 2 * 25), fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_NoLoyalVassalsPresent() {
 fiefdomObj.getLoyalVassals().clear();
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertTrue(new RollEvent("TestFiefdom").isRolled());
 }

}