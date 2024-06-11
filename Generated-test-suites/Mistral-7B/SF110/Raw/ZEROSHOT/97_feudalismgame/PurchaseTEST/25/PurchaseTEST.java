package src.test.java;

import static org.junit.Assert.*;
import static src.Purchase.*;
import src.Actions;
import src.CurrentPlayers;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;

import java.util.*;

public class PurchaseTest {

 private static final String BUYER_NAME_1 = "buyer1";
 private static final String BUYER_NAME_2 = "buyer2";
 private static final String FIELDOM_NAME = "fiefdom1";
 private static final String KNIGHT_NAME_1 = "knight1";
 private static final String KNIGHT_NAME_2 = "knight2";
 private static final int QTY = 5;

 @BeforeClass
 public static void setupClass() {
 CurrentPlayers.currentPlayers.clear();
 Fiefdoms.map.clear();
 }

 @Before
 public void setup() {
 CurrentPlayers.currentPlayers.put(BUYER_NAME_1, new Knight(BUYER_NAME_1));
 CurrentPlayers.currentPlayers.put(BUYER_NAME_2, new Knight(BUYER_NAME_2));
 Fiefdoms.map.put(FIELDOM_NAME, new Fiefdoms(FIELDOM_NAME, null));
 }

 @Test
 public void testPerform_vassal() {
 List<Object> args = new ArrayList();
 args.add("vassal");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get(FIELDOM_NAME).getLoyalVassals());
 }

 @Test
 public void testPerform_vassal_insufficientFunds() {
 List<Object> args = new ArrayList();
 args.add("vassal");
 args.add(Collections.singletonList(new Object[]{"10", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(0, Fiefdoms.map.get(FIELDOM_NAME).getLoyalVassals());
 }

 @Test
 public void testPerform_vassal_notOwnedByBuyer() {
 List<Object> args = new ArrayList();
 args.add("vassal");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, "fiefdom2"}).iterator());
 new Purchase().perform(args);
 assertEquals(0, Fiefdoms.map.get("fiefdom2").getLoyalVassals());
 }

 @Test
 public void testPerform_vassal_buyersRankIsKing() {
 List<Object> args = new ArrayList();
 args.add("vassal");
 args.add(Collections.singletonList(new Object[]{"5", "king", FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get(FIELDOM_NAME).getLoyalVassals());
 }

 @Test
 public void testPerform_vassal_buyerOwnsFiefdom() {
 List<Object> args = new ArrayList();
 args.add("vassal");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 CurrentPlayers.currentPlayers.get(BUYER_NAME_1).setFiefdom(FIELDOM_NAME);
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get(FIELDOM_NAME).getLoyalVassals());
 }

 @Test
 public void testPerform_castleFiefdom() {
 List<Object> args = new ArrayList();
 args.add("castleFiefdom");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get(FIELDOM_NAME).getCastles().size());
 }

 @Test
 public void testPerform_castleFiefdom_insufficientFunds() {
 List<Object> args = new ArrayList();
 args.add("castleFiefdom");
 args.add(Collections.singletonList(new Object[]{"10", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get(FIELDOM_NAME).getCastles().size());
 }

 @Test
 public void testPerform_castleFiefdom_notOwnedByBuyer() {
 List<Object> args = new ArrayList();
 args.add("castleFiefdom");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, "fiefdom2"}).iterator());
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get("fiefdom2").getCastles().size());
 }

 @Test
 public void testPerform_castleFiefdom_fiefdomHasCitizens() {
 List<Object> args = new ArrayList();
 args.add("castleFiefdom");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 Fiefdoms.map.get(FIELDOM_NAME).setViking(Collections.singletonList(new Citizen()));
 Fiefdoms.map.get(FIELDOM_NAME).setMagyar(Collections.singletonList(new Citizen()));
 Fiefdoms.map.get(FIELDOM_NAME).setMuslim(Collections.singletonList(new Citizen()));
 new Purchase().perform(args);
 assertEquals(5, Fiefdoms.map.get(FIELDOM_NAME).getCastles().size());
 }

 @Test
 public void testPerform_castlePersonal() {
 List<Object> args = new ArrayList();
 args.add("castlePersonal");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(BUYER_NAME_1).getCastles().size());
 }

 @Test
 public void testPerform_castlePersonal_insufficientFunds() {
 List<Object> args = new ArrayList();
 args.add("castlePersonal");
 args.add(Collections.singletonList(new Object[]{"10", BUYER_NAME_1, FIELDOM_NAME}).iterator());
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(BUYER_NAME_1).getCastles().size());
 }

 @Test
 public void testPerform_castlePersonal_notOwnedByBuyer() {
 List<Object> args = new ArrayList();
 args.add("castlePersonal");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, "fiefdom2"}).iterator());
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get("fiefdom2").getCastles().size());
 }

 @Test
 public void testPerform_mercenary() {
 List<Object> args = new ArrayList();
 args.add("mercenary");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, KNIGHT_NAME_1}).iterator());
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(BUYER_NAME_1).getMercenaries().size());
 }

 @Test
 public void testPerform_mercenary_notBuyer() {
 List<Object> args = new ArrayList();
 args.add("mercenary");
 args.add(Collections.singletonList(new Object[]{"5", "buyer2", KNIGHT_NAME_1}).iterator());
 new Purchase().perform(args);
 assertEquals(0, CurrentPlayers.currentPlayers.get("buyer2").getMercenaries().size());
 }

 @Test
 public void testPerform_mercenary_knightAlreadyBought() {
 List<Object> args = new ArrayList();
 args.add("mercenary");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, KNIGHT_NAME_1}).iterator());
 CurrentPlayers.currentPlayers.get(BUYER_NAME_1).addMercenaries(5);
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(BUYER_NAME_1).getMercenaries().size());
 }

 @Test
 public void testPerform_mercenary_knightIsKing() {
 List<Object> args = new ArrayList();
 args.add("mercenary");
 args.add(Collections.singletonList(new Object[]{"5", "king", KNIGHT_NAME_1}).iterator());
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(KNIGHT_NAME_1).getMercenaries().size());
 }

 @Test
 public void testPerform_mercenary_buyerIsKing() {
 List<Object> args = new ArrayList();
 args.add("mercenary");
 args.add(Collections.singletonList(new Object[]{"5", BUYER_NAME_1, KNIGHT_NAME_1}).iterator());
 CurrentPlayers.currentPlayers.get(BUYER_NAME_1).setRank("king");
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(BUYER_NAME_1).getMercenaries().size());
 }

 @Test
 public void testPerform_mercenary_knightIsBuyer() {
 List<Object> args = new ArrayList();
 args.add("mercenary");
 args.add(Collections.singletonList(new Object[]{"5", KNIGHT_NAME_1, BUYER_NAME_1}).iterator());
 new Purchase().perform(args);
 assertEquals(5, CurrentPlayers.currentPlayers.get(KNIGHT_NAME_1).getMercenaries().size());
 }
}