import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.Player;

import java.util.*;

public class PurchaseTest {

    @InjectMocks
    private Purchase purchase;
    private Actions actions;
    private GameAutoActions autoActions;
    private Fiefdoms fiefdoms;
    private Map map;
    private List<Player> currentPlayers;

    @Before
    public void setup() {
        actions = Mockito.mock(Actions.class);
        autoActions = Mockito.mock(GameAutoActions.class);
        fiefdoms = Mockito.mock(Fiefdoms.class);
        map = Mockito.mock(Map.class);
        currentPlayers = new ArrayList<>();

        Mockito.when(map.get(Mockito.anyString())).then(arg -> fiefdoms);
        Mockito.when(fiefdoms.getOwner()).thenReturn(null);
        Mockito.when(currentPlayers.get(Mockito.anyString())).then(arg -> getKnight("Alice"));

        purchase = new Purchase(actions, autoActions);
    }

    @Test
    public void testVassalValidScenario() {
        Iterator<Object> arg = Mockito.mock(Iterator.class);
        Mockito.when(arg.next()).thenReturn("fiefdom1");
        Mockito.when(arg.next()).thenReturn("Alice");
        Mockito.when(arg.next()).thenReturn("fiefdom1");

        purchase.perform(Collections.singletonList(arg));
        Mockito.verify(fiefdoms).setLoyalVassals(1, true);
        Mockito.verifyNoMoreInteractions(fiefdoms); // No more interactions after the first one, as the test should end here
    }

    @Test(expected = IllegalStateException.class)
    public void testVassalInvalidScenario1() {
        Iterator<Object> arg = Mockito.mock(Iterator.class);
        Mockito.when(arg.next()).thenReturn("fiefdom1");
        Mockito.when(arg.next()).thenReturn("Alice");
        Mockito.when(arg.next()).thenReturn("Alice");

        purchase.perform(Collections.singletonList(arg));
        Mockito.verifyNoInteractions(fiefdoms); // No interactions as the test should throw an exception
        Mockito.verify(System.out, Mockito.times(1)).println("You can only buy a vassal for a fiefdom that doesn't belong to you or you are the owner of it");
    }

    @Test(expected = IllegalStateException.class)
    public void testVassalInvalidScenario2() {
        Iterator<Object> arg = Mockito.mock(Iterator.class);
        Mockito.when(arg.next()).thenReturn("fiefdom1");
        Mockito.when(arg.next()).thenReturn("Alice");
        Mockito.when(arg.next()).thenReturn("king");

        purchase.perform(Collections.singletonList(arg));
        Mockito.verifyNoInteractions(fiefdoms); // No interactions as the test should throw an exception
        Mockito.verify(System.out, Mockito.times(1)).println("You can only buy a vassal for a fiefdom that doesn't belong to you or you are the owner of it");
    }

    // Add similar test cases for other methods like 'castleFiefdom', 'castlePersonal', 'mercenary'

    // Add test cases for the error handling scenarios
    // (SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException)

    private Knight getKnight(String name) {
        Player player = new Player(name, "knight");
        currentPlayers.add(player);
        return player;
    }
}