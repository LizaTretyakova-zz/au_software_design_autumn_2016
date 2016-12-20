package Command;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;

public class CommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File file;

    @Before
    public void setUp() throws Exception {
        file = folder.newFile();
    }

    @Test
    public void testCatExecute() throws Exception {
        new Cat().execute(Arrays.asList(file.getPath()));
    }

    @Test(expected = Command.CommandFailedException.class)
    public void testCatExecuteFails() throws Exception {
        new Cat().execute(Arrays.asList("definitely not a path"));
    }

    @Test(expected = Command.CommandFailedException.class)
    public void testCatExecuteFailsEmptyArgs() throws Exception {
        new Cat().execute(Arrays.asList());
    }

    // Ways for development: check the content.

    @Test
    public void testEchoExecute() throws Exception {
        new Echo().execute(Arrays.asList("Still alive!"));
    }

    @Test(expected = Command.CommandFailedException.class)
    public void testEchoFails() throws Exception {
        new Echo().execute(Arrays.asList());
    }

    @Test
    public void testExitExecute() throws Exception {
        new Exit().execute(Arrays.asList());
    }

    @Test
    public void testPwdExecute() throws Exception {
        new Pwd().execute(Arrays.asList());
    }

    @Test
    public void testWcExecute() throws Exception {
        new Wc().execute(Arrays.asList(file.getPath()));
    }

    @Test(expected = Command.CommandFailedException.class)
    public void testWcFails() throws Exception {
        new Wc().execute(Arrays.asList("Definitely not a path!"));
    }

    @Test(expected = Command.CommandFailedException.class)
    public void testWcFailsEmptyArgs() throws Exception {
        new Wc().execute(Arrays.asList());
    }
}