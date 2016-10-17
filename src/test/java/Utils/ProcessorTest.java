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
        assertEquals(false, processor.process("x = abc | pwd | echo | exit"));
        assertEquals(true, processor.process("x = cde"));
        assertEquals(false, processor.process("exit"));
        assertEquals(true, processor.process("echo $x"));
        assertEquals(true, processor.process("cat " + file.getPath()));
        assertEquals(true, processor.process("wc " + file.getPath()));
    }

}