package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        try {
            final String newzini = "test_newzini.ini";
            final String batchfile = "test_batchfile.ini";
            final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            assertNull(batchDriver);
        } catch (NullPointerException e) {
            // Expected
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        try {
            final String newzini = "test_newzini.ini";
            final String batchfile = "test_batchfile.ini";
            final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            final String[] groups = batchDriver.getGroups();
            assertNull(groups);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            final String newzini = new File("test_newzini.ini").getAbsolutePath();
            final String batchfile = new File("test_batchfile.ini").getAbsolutePath();
            final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            final String[] groups = batchDriver.getGroups();
            assertNotNull(groups);
            assertEquals(2, groups.length);
            assertTrue(groups[0].equals("group1"));
            assertTrue(groups[1].equals("group2"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetBatchJob() {
        try {
            final String newzini = new File("test_newzini.ini").getAbsolutePath();
            final String batchfile = new File("test_batchfile.ini").getAbsolutePath();
            final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            final BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNull(batchJob);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        try {
            final String newzini = new File("test_newzini.ini").getAbsolutePath();
            final String batchfile = new File("test_batchfile.ini").getAbsolutePath();
            final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            final String server = "server1.nntp";
            final String group = "group1";
            final String directory = "directory1";
            final int firstId = 1;
            final int lastId = 10;
            final String username = "testUser";
            final String password = "testPassword";

            final Ini ini = new Ini(newzini, "Messageids");
            ini.setProperty("group1", "server1.nntp:directory1");
            ini.setProperty("group1.retrieve", "true");
            ini.setProperty("group1.options", "server=" + server + ";directory=" + directory);
            ini.setProperty("group1.options.username", username);
            ini.setProperty("group1.options.password", password);

            final Ini batchGroups = new Ini(batchfile, "");
            batchGroups.setProperty("Newsgroups:group1", "Retrieve=true;Options=server=server1.nntp;options=directory=directory1;options=threads=2;options=timeout=30000");

            batchDriver.getGroups(); // Initialize the BatchDriver

            final BatchJob batchJob = batchDriver.getBatchJob(group);
            assertNotNull(batchJob);
            assertEquals(server, batchJob.getServer());
            assertEquals(group, batchJob.getGroup());
            assertEquals(directory, batchJob.getDirectory());
            assertEquals(firstId, batchJob.getFirstId());
            assertEquals(lastId, batchJob.getLastId());
            assertEquals(username, batchJob.getUsername());
            assertEquals(password, batchJob.getPassword());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetSubjectLines() {
        try {
            final String newzini = new File("test_newzini.ini").getAbsolutePath();
            final String batchfile = new File("test_batchfile.ini").getAbsolutePath();
            final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            final String[] subjectLines = batchDriver.getSubjectLines();
            assertNull(subjectLines);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}