package Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.*;

public class ProcessorTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File file;

    @Before
    public void setUp() throws Exception {
        file = folder.newFile();
    }

    @Test
    public void testProcess() throws Exception {
        Processor processor = Processor.getInstance();
        assertFalse(processor.process("x = abc | pwd | echo | exit"));
        assertTrue(processor.process("x = cde"));
        assertFalse(processor.process("exit"));
        assertTrue(processor.process("echo $x"));
        assertTrue(processor.process("cat " + file.getPath()));
        assertTrue(processor.process("wc " + file.getPath()));
    }

}