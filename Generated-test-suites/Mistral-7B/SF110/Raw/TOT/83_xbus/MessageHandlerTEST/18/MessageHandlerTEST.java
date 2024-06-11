package net.sf.xbus.base.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private static final String BASENAME = "testMessageHandler";

    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance(BASENAME);
        messageHandler.addMessages(BASENAME, "src/test/resources");
    }

    // ... (Expert 1, Expert 2, and Expert 3's test cases)

}