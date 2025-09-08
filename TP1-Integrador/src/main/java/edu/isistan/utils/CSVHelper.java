package edu.isistan.utils;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVHelper {
    private Path path;

    public CSVHelper(String archivo) {
        this.path = Path.of("TP1-Integrador", "src", "main", "resources", archivo);
    }

    public Path getPath() {
        return path;
    }
    public void setPath(Path path) {
        this.path = path;
    }

    public Iterable<CSVRecord> getData() throws Exception {
        Reader in = new FileReader(this.path.toString());
        return CSVFormat.DEFAULT.builder()
          .setHeader()
          .get()
          .parse(in);
    }
}