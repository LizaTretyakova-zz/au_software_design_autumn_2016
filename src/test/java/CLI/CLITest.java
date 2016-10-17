package CLI;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static org.junit.Assert.*;

public class CLITest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File file;

    @Before
    public void setUp() throws Exception {
        file = folder.newFile();
    }

    @Test
    public void run() throws Exception {
        String input = "x = abc\ny = cde\ncat " + file.getPath()
                + "\nwc " + file.getPath() + "\nx = abcd | pwd | echo | exit\n"
                + "x = \"Should not be here!\" | echo";
        CLI.run(new BufferedReader(new StringReader(input)));
    }

}