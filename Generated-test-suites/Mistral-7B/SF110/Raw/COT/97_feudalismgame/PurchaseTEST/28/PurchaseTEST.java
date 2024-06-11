package src.test;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.GameAutoActions;
import src.CurrentPlayers;
import src.Knight;
import src.Map;
import src.Purchase;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;
 private Map map;
 private GameAutoActions gameAutoActions;

 @Before
 public void setUp() {
 purchase = new Purchase();
 currentPlayers = new CurrentPlayers();
 fiefdom = new Fiefdoms();
 map = new Map();
 gameAutoActions = new GameAutoActions();
 }

 @Test
 public void testPerform() {
 // Testing the perform method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("feudalism.Purchase");
 args.add(this.args());
 purchase.perform(args);
 // Testing the perform method with invalid arguments
 Collection<Object> invalidArgs = new ArrayList<>();
 args.clear();
 args.add(null);
 args.add(this.args());
 purchase.perform(args);
 }

 @Test
 public void testVassal() {
 // Testing the vassal method with valid arguments
 Collection<Object> vassalArgs = new ArrayList<>();
 vassalArgs.add(this.iteratorWithValidArgumentsForVassal());
 purchase.vassal(vassalArgs.iterator());
 // Testing the vassal method with invalid arguments
 Collection<Object> invalidVassalArgs = new ArrayList<>();
 vassalArgs.clear();
 vassalArgs.add(null);
 vassalArgs.add(this.args());
 purchase.vassal(vassalArgs.iterator());
 // Testing the vassal method with negative quantity
 Collection<Object> negativeQuantityVassalArgs = new ArrayList<>();
 negativeQuantityVassalArgs.add(this.iteratorWithValidArgumentsForVassal());
 negativeQuantityVassalArgs.add(this.argWithNegativeQuantityForVassal());
 purchase.vassal(negativeQuantityVassalArgs.iterator());
 }

 @Test
 public void testCastleFiefdom() {
 // Testing the castleFiefdom method with valid arguments
 Collection<Object> castleFiefdomArgs = new ArrayList<>();
 castleFiefdomArgs.add(this.iteratorWithValidArgumentsForCastleFiefdom());
 purchase.castleFiefdom(castleFiefdomArgs.iterator());
 // Testing the castleFiefdom method with invalid arguments
 Collection<Object> invalidCastleFiefdomArgs = new ArrayList<>();
 invalidCastleFiefdomArgs.add(null);
 invalidCastleFiefdomArgs.add(this.args());
 purchase.castleFiefdom(invalidCastleFiefdomArgs.iterator());
 // Testing the castleFiefdom method with negative quantity
 Collection<Object> negativeQuantityCastleFiefdomArgs = new ArrayList<>();
 negativeQuantityCastleFiefdomArgs.add(this.iteratorWithValidArgumentsForCastleFiefdom());
 negativeQuantityCastleFiefdomArgs.add(this.argWithNegativeQuantityForCastleFiefdom());
 purchase.castleFiefdom(negativeQuantityCastleFiefdomArgs.iterator());
 }

 @Test
 public void testCastlePersonal() {
 // Testing the castlePersonal method with valid arguments
 Collection<Object> castlePersonalArgs = new ArrayList<>();
 castlePersonalArgs.add(this.iteratorWithValidArgumentsForCastlePersonal());
 purchase.castlePersonal(castlePersonalArgs.iterator());
 // Testing the castlePersonal method with invalid arguments
 Collection<Object> invalidCastlePersonalArgs = new ArrayList<>();
 invalidCastlePersonalArgs.add(null);
 invalidCastlePersonalArgs.add(this.args());
 purchase.castlePersonal(invalidCastlePersonalArgs.iterator());
 // Testing the castlePersonal method with negative quantity
 Collection<Object> negativeQuantityCastlePersonalArgs = new ArrayList<>();
 negativeQuantityCastlePersonalArgs.add(this.iteratorWithValidArgumentsForCastlePersonal());
 negativeQuantityCastlePersonalArgs.add(this.argWithNegativeQuantityForCastlePersonal());
 purchase.castlePersonal(negativeQuantityCastlePersonalArgs.iterator());
 }

 @Test
 public void testMercenary() {
 // Testing the mercenary method with valid arguments
 Collection<Object> mercenaryArgs = new ArrayList<>();
 mercenaryArgs.add(this.iteratorWithValidArgumentsForMercenary());
 purchase.mercenary(mercenaryArgs.iterator());
 // Testing the mercenary method with invalid arguments
 Collection<Object> invalidMercenaryArgs = new ArrayList<>();
 invalidMercenaryArgs.add(null);
 invalidMercenaryArgs.add(this.args());
 purchase.mercenary(invalidMercenaryArgs.iterator());
 // Testing the mercenary method with negative quantity
 Collection<Object> negativeQuantityMercenaryArgs = new ArrayList<>();
 negativeQuantityMercenaryArgs.add(this.iteratorWithValidArgumentsForMercenary());
 negativeQuantityMercenaryArgs.add(this.argWithNegativeQuantityForMercenary());
 purchase.mercenary(negativeQuantityMercenaryArgs.iterator());
 // Testing the mercenary method with knight trying to buy mercenaries for himself
 Collection<Object> sameKnightMercenaryArgs = new ArrayList<>();
 sameKnightMercenaryArgs.add(this.iteratorWithValidArgumentsForMercenary());
 sameKnightMercenaryArgs.add(this.knightWithSameNameAsBuyer());
 purchase.mercenary(sameKnightMercenaryArgs.iterator());
 }

 private Iterator args() {
 return this.iteratorWithValidArguments();
 }

 private Iterator iteratorWithValidArguments() {
 return new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 }.iterator();
 }

 private Iterator iteratorWithValidArgumentsForVassal() {
 Iterator arg = this.args().iterator();
 arg.next(); // arg.next() should return Class.forName("feudalism.Purchase")
 arg.next(); // arg
 arg.next(); // Iterator
 arg.next(); // Integer.valueOf(qty)
 arg.next(); // String.valueOf(buyerName)
 arg.next(); // String.valueOf(fiefdomName)
 return arg;
 }

 private Iterator iteratorWithValidArgumentsForCastleFiefdom() {
 Iterator arg = this.args().iterator();
 arg.next(); // arg.next() should return Class.forName("feudalism.Purchase")
 arg.next(); // Iterator
 arg.next(); // Integer.valueOf(qty)
 arg.next(); // String.valueOf(buyerName)
 arg.next(); // String.valueOf(fiefdomName)
 return arg;
 }

 private Iterator iteratorWithValidArgumentsForCastlePersonal() {
 Iterator arg = this.args().iterator();
 arg.next(); // arg.next() should return Class.forName("feudalism.Purchase")
 arg.next(); // Iterator
 arg.next(); // Integer.valueOf(qty)
 arg.next(); // String.valueOf(buyerName)
 arg.next(); // String.valueOf(fiefdomName)
 return arg;
 }

 private Iterator iteratorWithValidArgumentsForMercenary() {
 Iterator arg = this.args().iterator();
 arg.next(); // arg.next() should return Class.forName("feudalism.Purchase")
 arg.next(); // Iterator
 arg.next(); // Integer.valueOf(qty)
 arg.next(); // String.valueOf(buyerName)
 arg.next(); // String.valueOf(knightName)
 return arg;
 }

 private Object argWithNegativeQuantityForVassal() {
 return Integer.valueOf(-1);
 }

 private Object argWithNegativeQuantityForCastleFiefdom() {
 return Integer.valueOf(-1);
 }

 private Object argWithNegativeQuantityForCastlePersonal() {
 return Integer.valueOf(-1);
 }

 private Object argWithNegativeQuantityForMercenary() {
 return Integer.valueOf(-1);
 }

 private Knight knightWithSameNameAsBuyer() {
 Knight knight = new Knight();
 knight.setName(currentPlayers.getCurrentPlayer().getName());
 return knight;
 }

}