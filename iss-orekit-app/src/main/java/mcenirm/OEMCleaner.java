package mcenirm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class OEMCleaner extends BufferedReader {

    public OEMCleaner(final String fileName) throws FileNotFoundException {
        this(new FileInputStream(fileName));
    }

    public OEMCleaner(final InputStream stream) {
        super(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
    }

    public OEMCleaner(final Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        String line = super.readLine();
        if (line != null && line.startsWith("USABLE_")) {
            line = line.substring(0, 2) + "E" + line.substring(2);
        }
        return line;
    }
}
