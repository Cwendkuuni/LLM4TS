package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private List<Knight> currentPlayers;
 private List<Fiefdoms> map;
 private Knight buyer;
 private Fiefdoms fiefdom;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 actions = mock(Actions.class);
 purchase = new Purchase(actions);
 currentPlayers = new ArrayList<>();
 map = new ArrayList<>();

 buyer = new Knight("John", "knight", 100);
 currentPlayers.add(buyer);

 fiefdom = new Fiefdoms("Fiefdom1", null);
 map.add(fiefdom);
 }

 @Test
 public void testPerform_vassal() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 verify(actions).perform(Collections.singletonList(args));
 verify(fiefdom).setLoyalVassals(1, true);
 assertEquals(99, buyer.getTotalMoney());
 }

 @Test
 public void testPerform_vassal_noMoney() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty2", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }

 @Test
 public void testPerform_castleFiefdom() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 verify(actions).perform(Collections.singletonList(args));
 verify(fiefdom).buyFiefdomCastles(1);
 assertEquals(98, buyer.getTotalMoney());
 }

 @Test
 public void testPerform_castleFiefdom_noMoney() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }

 @Test
 public void testPerform_castlePersonal() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 verify(actions).perform(Collections.singletonList(args));
 verify(fiefdom).buyPersonalCastles(1);
 assertEquals(97, buyer.getTotalMoney());
 }

 @Test
 public void testPerform_castlePersonal_noMoney() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }

 @Test
 public void testPerform_mercenary() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Knight1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 verify(actions).perform(Collections.singletonList(args));
 assertEquals(99 - (1 * 10), buyer.getTotalMoney());
 assertEquals(1 + knight.getMercenaries(), knight.getMercenaries());
 }

 @Test
 public void testPerform_mercenary_noMoney() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Knight1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }

 @Test
 public void testPerform_fiefdomDoesntBelongToKingdom() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom2")).iterator();
 Fiefdoms fiefdom2 = new Fiefdoms("Fiefdom2", null);
 map.add(fiefdom2);
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }

 @Test
 public void testPerform_buyerIsKing() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "King1", "Fiefdom1")).iterator();
 Knight king = new Knight("King1", "king", 1000);
 currentPlayers.set(0, king);
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 verify(actions).perform(Collections.singletonList(args));
 verify(fiefdom).setLoyalVassals(1, true);
 assertEquals(999, king.getTotalMoney());
 }

 @Test
 public void testPerform_buyerIsNotOwner() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "Buyer1", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }

 @Test
 public void testPerform_countOrDuke() {
 // Given
 final Iterator<Object> args = new LinkedList<Object>(Arrays.asList("Qty1", "CountOrDuke", "Fiefdom1")).iterator();
 when(actions.getClass()).thenReturn(Purchase.class);
 when(actions.perform(Collections.singletonList(args))).thenCallRealMethod();

 // When
 purchase.perform(Collections.singletonList(args));

 // Then
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getOut().toString());
 verify(actions).perform(Collections.singletonList(args));
 }
}