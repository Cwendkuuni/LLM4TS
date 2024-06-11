package com.mentorgen.tools.profile.runtime;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ProfileTest {
    // 

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
    }

    // 
}