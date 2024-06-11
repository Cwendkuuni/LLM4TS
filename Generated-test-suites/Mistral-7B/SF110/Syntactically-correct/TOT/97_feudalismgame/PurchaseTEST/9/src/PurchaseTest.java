import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import src.*;
import src.util.Fiefdoms;
import src.util.GameAutoActions;
import src.util.Map;
import src.util.CurrentPlayers;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private CurrentPlayers currentPlayers;
 private Map map;
 private Fiefdoms fiefdoms;
 private GameAutoActions gameAutoActions;

 @Before
 public void init() {
 currentPlayers = new CurrentPlayers();
 map = new Map();
 fiefdoms = new Fiefdoms();
 purchase = new Purchase();
 gameAutoActions = Mockito.spy(new GameAutoActions());
 }

 @Test
 public void testPerform_ValidArguments() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("fiefdom1");

 // When
 purchase.perform(args);

 // Then
 ArgumentCaptor<Object> methodArgCaptor = ArgumentCaptor.forClass(Object.class);
 Mockito.verify(purchase).vassal(methodArgCaptor.capture());
 Iterator capturedIterator = (Iterator) methodArgCaptor.getValue();
 assertEquals("Alfred", capturedIterator.next());
 assertEquals("fiefdom1", capturedIterator.next());
 assertEquals("Alfred", capturedIterator.next());

 // When
 purchase.perform(args);

 // Then
 assertEquals(0, fiefdoms.get("fiefdom1").getVassals().size());
 assertEquals(1, currentPlayers.get("Alfred").getVassals().size());
 }

 @Test
 public void testPerform_InvalidArguments() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("castleFiefdom");
 args.add(Mockito.mock(Iterator.class));

 // When and Then
 assertThrows(NoSuchMethodException.class, () -> purchase.perform(args));
 }

 @Test
 public void testPerform_InsufficientFunds() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("fiefdom1");
 currentPlayers.get("Alfred").setTotalMoney(0);

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testPerform_FiefdomDoesntBelongToKing() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("fiefdom2");
 fiefdoms.put("fiefdom2", new Fiefdom("fiefdom2", null, null, null, null, null, null, null, null, null));

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testPerform_FiefdomDoesntBelongToPlayer() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(Mockito.mock(Iterator.class));
 args.add("JohnDoe");
 args.add("fiefdom1");

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testCastleFiefdom() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("castleFiefdom");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("fiefdom1");
 fiefdoms.put("fiefdom1", new Fiefdom("fiefdom1", null, null, null, null, null, null, null, null, null));
 CurrentPlayers.currentPlayers.put("Alfred", new Knight("Alfred", "knight", 1000, new ArrayList<>()));

 // When
 purchase.perform(args);

 // Then
 assertEquals(1, fiefdoms.get("fiefdom1").getCastle().size());
 assertEquals(1, currentPlayers.get("Alfred").getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("castlePersonal");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("fiefdom1");
 fiefdoms.put("fiefdom1", new Fiefdom("fiefdom1", null, null, null, null, null, null, null, null, null));
 CurrentPlayers.currentPlayers.put("Alfred", new Knight("Alfred", "knight", 5000, new ArrayList<>()));

 // When
 purchase.perform(args);

 // Then
 assertEquals(1, fiefdoms.get("fiefdom1").getPersonalCastles().size());
 assertEquals(1, currentPlayers.get("Alfred").getPersonalCastles().size());
 }

 @Test
 public void testMercenary() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("mercenary");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("knight1");
 CurrentPlayers.currentPlayers.put("Alfred", new Knight("Alfred", "knight", 1500, new ArrayList<>()));
 CurrentPlayers.currentPlayers.put("knight1", new Knight("knight1", "knight", 0, new ArrayList<>()));

 // When
 purchase.perform(args);

 // Then
 assertEquals(2, currentPlayers.get("knight1").getMercenaries().size());
 assertEquals(1, currentPlayers.get("Alfred").getMercenaries().size());
 }

 @Test
 public void testMercenary_SamePlayer() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("mercenary");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("Alfred");
 CurrentPlayers.currentPlayers.put("Alfred", new Knight("Alfred", "knight", 1500, new ArrayList<>()));

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testMercenary_InsufficientFunds_ForPlayer() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("mercenary");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("knight1");
 currentPlayers.get("Alfred").setTotalMoney(0);

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testMercenary_InsufficientFunds_ForKnight() {
 // Given
 Collection<Object> args = new ArrayList<>();
 args.add("mercenary");
 args.add(Mockito.mock(Iterator.class));
 args.add("Alfred");
 args.add("knight1");
 CurrentPlayers.currentPlayers.put("Alfred", new Knight("Alfred", "knight", 1500, new ArrayList<>()));
 currentPlayers.get("knight1").setTotalMoney(0);

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testPerform_NullArgs() {
 // Given
 Collection<Object> args = null;

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testPerform_EmptyArgs() {
 // Given
 Collection<Object> args = new ArrayList<>();

 // When
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }
}