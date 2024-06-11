package src.test.java;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: A fiefdom with loyal vassals and castles
 List<String> fiefdoms = new ArrayList<>();
 fiefdoms.add("TestFiefdom");
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put("TestFiefdom", fiefdomObj);

 // When: VassalRebellion is performed on the fiefdom
 vassalRebellion.perform(fiefdoms);

 // Then: The fiefdom is set to rebellious and the kingdom is set to false
 assertTrue(fiefdomObj.isRebellious());
 assertFalse(fiefdomObj.isKingdom());

 // Given: The fiefdom has enough loyal vassals and castles to trigger a rebellion
 int personalCastles = 2;
 int fiefdomCastles = 3;
 fiefdomObj = new Fiefdoms("TestFiefdom", 15, personalCastles + fiefdomCastles);

 // When: VassalRebellion is performed on the fiefdom
 vassalRebellion.perform(fiefdoms);

 // Then: The number of loyal vassals is decreased and the number of rebellious vassals is increased
 int expectedLoyalVassals = fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25);
 int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() - expectedLoyalVassals;

 assertEquals(expectedLoyalVassals, fiefdomObj.getLoyalVassals().size());
 assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerformWithEmptyFiefdom() {
 // Given: A fiefdom with no loyal vassals
 fiefdomObj = new Fiefdoms("TestFiefdom", 0, 0);

 // When: VassalRebellion is performed on the fiefdom
 vassalRebellion.perform(new ArrayList<>());

 // Then: A new RollEvent is created for the fiefdom
 assertTrue(fiefdomObj.getRollEvents().size() > 0);
 }
}