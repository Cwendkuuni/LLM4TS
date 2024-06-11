import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

import java.util.*;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchaseTest {

 private Purchase purchase;
 private CurrentPlayers currentPlayers;
 private Map map;
 private GameAutoActions gameAutoActions;
 private Fiefdoms fiefdom1;
 private Fiefdoms fiefdom2;
 private Knight buyer1;
 private Knight buyer2;
 private Knight knight;

 @Before
 public void setUp() {
 purchase = new Purchase();
 currentPlayers = mock(CurrentPlayers.class);
 map = mock(Map.class);
 gameAutoActions = mock(GameAutoActions.class);
 fiefdom1 = mock(Fiefdoms.class);
 fiefdom2 = mock(Fiefdoms.class);
 buyer1 = mock(Knight.class);
 buyer2 = mock(Knight.class);
 knight = mock(Knight.class);

 when(map.get(anyString())).thenAnswer(invocation -> {
 if (invocation.getArgument(0).equals("fiefdom1")) {
 return fiefdom1;
 } else if (invocation.getArgument(0).equals("fiefdom2")) {
 return fiefdom2;
 }
 return null;
 });

 when(currentPlayers.get(anyString())).thenAnswer(invocation -> {
 if (invocation.getArgument(0).equals("buyer1")) {
 return buyer1;
 } else if (invocation.getArgument(0).equals("buyer2")) {
 return buyer2;
 }
 return null;
 });

 when(currentPlayers.currentPlayers).thenReturn(Arrays.asList(buyer1, buyer2));
 when(fiefdom1.getName()).thenReturn("fiefdom1");
 when(fiefdom1.getOwner()).thenReturn(null);
 when(fiefdom1.getViking().size()).thenReturn(0);
 when(fiefdom1.getMagyar().size()).thenReturn(0);
 when(fiefdom1.getMuslim().size()).thenReturn(0);
 when(fiefdom2.getName()).thenReturn("fiefdom2");
 when(fiefdom2.getOwner()).thenReturn("owner1");
 when(gameAutoActions.saveAll()).thenReturn(null);
 }

 @Test
 public void testVassal() {
 // Given: buyer has enough money
 when(buyer1.getRank()).thenReturn("duke");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);

 // When: buyer buys vassals for fiefdom1
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom1").iterator();
 purchase.perform(args);

 // Then: vassals are added to fiefdom1 and buyer's money is decreased
 assertEquals(3, fiefdom1.getLoyalVassals().size());
 assertEquals(buyer1.getName(), fiefdom1.getLastAddedVassal().getName());
 assertEquals(buyer1.getTotalMoney() - 3 * 10, buyer1.getTotalMoney());
 }

 @Test
 public void testVassalNoMoney() {
 // Given: buyer doesn't have enough money
 when(buyer1.getRank()).thenReturn("duke");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(false);

 // When: buyer tries to buy vassals for fiefdom1
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom1").iterator();
 purchase.perform(args);

 // Then: no vassals are added to fiefdom1 and buyer's money is unchanged
 assertEquals(0, fiefdom1.getLoyalVassals().size());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }

 @Test
 public void testVassalWrongFiefdom() {
 // Given: buyer tries to buy vassals for a fiefdom that doesn't belong to the kingdom
 when(buyer1.getRank()).thenReturn("duke");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
 when(fiefdom2.getOwner()).thenReturn("owner2");
 when(map.get(anyString())).thenReturn(fiefdom2);

 // When: buyer tries to buy vassals for fiefdom2
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom2").iterator();
 purchase.perform(args);

 // Then: no vassals are added to fiefdom2 and buyer's money is unchanged
 assertEquals(0, fiefdom2.getLoyalVassals().size());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }

 @Test
 public void testCastleFiefdom() {
 // Given: buyer has enough money
 when(buyer1.getRank()).thenReturn("duke");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
 when(fiefdom1.getOwner()).thenReturn(null);
 when(fiefdom1.getViking().size()).thenReturn(0);
 when(fiefdom1.getMagyar().size()).thenReturn(0);
 when(fiefdom1.getMuslim().size()).thenReturn(0);

 // When: buyer buys castles for fiefdom1
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom1").iterator();
 purchase.perform(args);

 // Then: castles are added to fiefdom1 and buyer's money is decreased
 assertEquals(3, fiefdom1.getCastles().size());
 assertEquals(buyer1.getName(), fiefdom1.getLastAddedCastle().getName());
 assertEquals(buyer1.getTotalMoney() - 3 * 25, buyer1.getTotalMoney());
 }

 @Test
 public void testCastleFiefdomNoMoney() {
 // Given: buyer doesn't have enough money
 when(buyer1.getRank()).thenReturn("duke");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(false);

 // When: buyer tries to buy castles for fiefdom1
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom1").iterator();
 purchase.perform(args);

 // Then: no castles are added to fiefdom1 and buyer's money is unchanged
 assertEquals(0, fiefdom1.getCastles().size());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }

 @Test
 public void testCastleFiefdomWrongFiefdom() {
 // Given: buyer tries to buy castles for a fiefdom that doesn't belong to the kingdom
 when(buyer1.getRank()).thenReturn("duke");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
 when(fiefdom2.getOwner()).thenReturn("owner2");
 when(map.get(anyString())).thenReturn(fiefdom2);

 // When: buyer tries to buy castles for fiefdom2
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom2").iterator();
 purchase.perform(args);

 // Then: no castles are added to fiefdom2 and buyer's money is unchanged
 assertEquals(0, fiefdom2.getCastles().size());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }

 @Test
 public void testCastlePersonal() {
 // Given: buyer and knight are the same person
 when(buyer1.getName()).thenReturn("buyer1");
 when(knight.getName()).thenReturn("buyer1");
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);

 // When: buyer buys personal castles
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom1").iterator();
 purchase.perform(args);

 // Then: castles are added to buyer1 and fiefdom1's money is unchanged
 assertEquals(3, buyer1.getPersonalCastles().size());
 assertEquals(0, fiefdom1.getCastles().size());
 assertEquals(buyer1.getTotalMoney() - 3 * 10, buyer1.getTotalMoney());
 }

 @Test
 public void testCastlePersonalNoMoney() {
 // Given: buyer doesn't have enough money
 when(buyer1.getName()).thenReturn("buyer1");
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(false);

 // When: buyer tries to buy personal castles
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom1").iterator();
 purchase.perform(args);

 // Then: no personal castles are added to buyer1 and his money is unchanged
 assertEquals(0, buyer1.getPersonalCastles().size());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }

 @Test
 public void testCastlePersonalWrongFiefdom() {
 // Given: buyer tries to buy personal castles for a fiefdom that doesn't belong to the kingdom
 when(buyer1.getName()).thenReturn("buyer1");
 when(buyer1.getRank()).thenReturn("king");
 when(knight.getName()).thenReturn("knight");
 when(map.get(anyString())).thenReturn(fiefdom2);
 when(fiefdom2.getOwner()).thenReturn("owner2");

 // When: buyer tries to buy personal castles for fiefdom2
 Iterator<Object> args = Arrays.asList("3", "buyer1", "fiefdom2").iterator();
 purchase.perform(args);

 // Then: no personal castles are added to buyer1 and fiefdom2's money is unchanged
 assertEquals(0, buyer1.getPersonalCastles().size());
 assertEquals(0, fiefdom2.getCastles().size());
 }

 @Test
 public void testMercenary() {
 // Given: buyer has enough money
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
 when(buyer1.getName()).thenReturn("buyer1");
 when(currentPlayers.get(anyString())).thenReturn(knight);

 // When: buyer buys mercenaries for knight
 Iterator<Object> args = Arrays.asList("3", "buyer1", "knight").iterator();
 purchase.perform(args);

 // Then: mercenaries are added to knight and buyer's money is decreased
 assertEquals(3, knight.getTotalMercenaries());
 assertEquals(buyer1.getTotalMoney() - 3 * 10, buyer1.getTotalMoney());
 }

 @Test
 public void testMercenaryNoMoney() {
 // Given: buyer doesn't have enough money
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(false);
 when(buyer1.getName()).thenReturn("buyer1");
 when(currentPlayers.get(anyString())).thenReturn(knight);

 // When: buyer tries to buy mercenaries for knight
 Iterator<Object> args = Arrays.asList("3", "buyer1", "knight").iterator();
 purchase.perform(args);

 // Then: no mercenaries are added to knight and buyer's money is unchanged
 assertEquals(0, knight.getTotalMercenaries());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }

 @Test
 public void testMercenaryWrongKnight() {
 // Given: buyer tries to buy mercenaries for an inexistent knight
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
 when(currentPlayers.get(anyString())).thenReturn(null);

 // When: buyer tries to buy mercenaries for an inexistent knight
 Iterator<Object> args = Arrays.asList("3", "buyer1", "inexistent").iterator();
 purchase.perform(args);

 // Then: no mercenaries are added to the knight and buyer's money is unchanged
 assertEquals(0, knight.getTotalMercenaries());
 assertEquals(buyer1.getTotalMoney(), buyer1.getTotalMoney());
 }
}