package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdom;

 @Before
 public void setUp() {
 // Create a Fiefdom object with some initial values
 fiefdom = new Fiefdom("TestFiefdom");
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(2);
 fiefdom.setValue(10);
 fiefdom.setLoyalVassals(new ArrayList<>());
 fiefdom.getLoyalVassals().add(new Vassal("TestVassal1"));
 fiefdom.getLoyalVassals().add(new Vassal("TestVassal2"));
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), fiefdom);

 // Create a VassalRebellion object and assign the Fiefdom object to it
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdoms(map);
 }

 @Test
 public void testPerform_LoyalVassalsWithinLimit_CastlesDestroyed() {
 // Test case for when there are loyal vassals and the number of rebellious vassals created is within the limit
 vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));
 int loyalVassalsSizeBefore = fiefdom.getLoyalVassals().size();
 int rebelliousVassalsSizeAfter = fiefdom.getRebelliousVassals().size();

 // Check if the number of loyal vassals decreases and the number of rebellious vassals increases
 assertEquals(loyalVassalsSizeBefore - (1 * 10 + 2 * 25), fiefdom.getLoyalVassals().size());
 assertEquals(rebelliousVassalsSizeAfter, loyalVassalsSizeBefore - (1 * 10 + 2 * 25));
 }

 @Test
 public void testPerform_LoyalVassalsExceedLimit_AllVassalsBecomeRebellious() {
 // Test case for when there are loyal vassals but the number of rebellious vassals created exceeds the limit
 fiefdom.setLoyalVassals(new ArrayList<>());
 fiefdom.getLoyalVassals().addAll(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2"), new Vassal("TestVassal3"), new Vassal("TestVassal4"), new Vassal("TestVassal5"), new Vassal("TestVassal6"), new Vassal("TestVassal7"), new Vassal("TestVassal8"), new Vassal("TestVassal9"), new Vassal("TestVassal10")));

 vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));
 int loyalVassalsSizeBefore = fiefdom.getLoyalVassals().size();
 int rebelliousVassalsSizeAfter = fiefdom.getRebelliousVassals().size();

 // Check if all vassals have become rebellious
 assertEquals(0, fiefdom.getLoyalVassals().size());
 assertEquals(rebelliousVassalsSizeAfter, loyalVassalsSizeBefore);
 }

 @Test
 public void testPerform_NoLoyalVassals_RollEventThrown() {
 // Test case for when there are no loyal vassals
 fiefdom.setLoyalVassals(new ArrayList<>());

 vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));

 // Check if a RollEvent is thrown
 assertTrue(fiefdom.getRollEventList().size() > 0);
 }
}