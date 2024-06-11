package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 15, new ArrayList<>(), new ArrayList<>());
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassalsPresent_ValueGreaterThanZero_KingdomNotSet_RebelliousVassalsIncreased() {
 // Given: A fiefdom with loyal vassals and a value greater than zero
 // When: The VassalRebellion performs on the fiefdom
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: The fiefdom's rebellious vassals are increased based on the condition in the VassalRebellion class
 assertEquals(15, fiefdomObj.getLoyalVassals().size()); // The initial size of loyal vassals
 assertEquals(5, fiefdomObj.getValue()); // The initial value of the fiefdom
 assertEquals(false, fiefdomObj.isKingdom()); // The initial kingdom status
 assertEquals(10, fiefdomObj.getRebelliousVassals().size()); // The expected size of rebellious vassals based on the conditions
 }

 @Test
 public void testPerform_LoyalVassalsPresent_ValueZeroOrLess_RebelliousVassalsSetToSizeOfLoyalVassals() {
 // Given: A fiefdom with loyal vassals and a value equal or less than zero
 fiefdomObj.setValue(0);

 // When: The VassalRebellion performs on the fiefdom
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: All vassals are set as rebellious since the condition checking for personal castles and fiefdom castles is not met
 assertEquals(15, fiefdomObj.getLoyalVassals().size()); // The initial size of loyal vassals
 assertEquals(0, fiefdomObj.getValue()); // The initial value of the fiefdom is zero
 assertEquals(false, fiefdomObj.isKingdom()); // The initial kingdom status
 assertEquals(15, fiefdomObj.getRebelliousVassals().size()); // All vassals should be rebellious
 }

 @Test
 public void testPerform_NoLoyalVassalsPresent_RollEventIsCalled() {
 // Given: A fiefdom with no loyal vassals
 fiefdomObj.setLoyalVassals(Collections.emptyList());

 // When: The VassalRebellion performs on the fiefdom
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: A RollEvent is called since there are no loyal vassals to check the conditions
 assertTrue(RollEvent.isRolled); // A RollEvent is expected to be rolled when there are no loyal vassals
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerform_CollectionIsEmpty() {
 // Given: An empty collection

 // When: The VassalRebellion performs on the empty collection
 vassalRebellion.perform(Collections.emptyList());

 // Then: An UnsupportedOperationException should be thrown since an empty collection cannot be iterated
 }

 @Test(expected = NullPointerException.class)
 public void testPerform_FiefdomNameIsNull() {
 // Given: A null fiefdom name

 // When: The VassalRebellion performs on the null fiefdom name
 vassalRebellion.perform(null);

 // Then: A NullPointerException should be thrown since a null fiefdom name is not allowed
 }
}