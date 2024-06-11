import static org.junit.Assert.assertEquals;

public class LagoonGUITest {

    @Test
    public void testMain() {
        String[] args = new String[]{"args.txt"};
        LagoonGUI.main(args);
        assertEquals("Lagoon", this.getClass().getSuperclass().getDeclaredField("title").get(null));
    }
}