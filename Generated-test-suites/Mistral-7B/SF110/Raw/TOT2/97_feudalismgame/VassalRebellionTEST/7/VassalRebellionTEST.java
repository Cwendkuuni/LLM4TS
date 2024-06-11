
package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    private Fiefdoms fiefdomMock;
    @Spy
    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        fiefdomMock = mock(Fiefdoms.class);
    }

    // Test cases go here

    // ...

}
