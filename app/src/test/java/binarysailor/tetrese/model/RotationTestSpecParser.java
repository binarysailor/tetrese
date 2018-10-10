package binarysailor.tetrese.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class RotationTestSpecParser {

    private static final char COMMENT_START = '#';
    private BlockMatrixParser parser = new BlockMatrixParser('.', '*');

    MatrixPair[] parse(InputStream is) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        List<String> lines = readNonEmptyLines(in);

        return toMatrixPairs(lines);
    }

    private List<String> readNonEmptyLines(BufferedReader in) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            String stripped = stripComment(line.trim());
            if (!stripped.isEmpty()) {
                lines.add(stripped);
            }
        }
        return lines;
    }

    private String stripComment(String line) {
        int index = line.indexOf(COMMENT_START);
        return index >= 0 ? line.substring(0, index) : line;
    }

    private MatrixPair[] toMatrixPairs(List<String> lines) throws ParseException {
        List<MatrixPair> result = new ArrayList<>();

        Iterator<String> ilines = lines.iterator();
        int counter = 0, rowCountForCurrentMatrix = 0;
        List<String> m1Rows = new ArrayList<>(), m2Rows = new ArrayList<>();

        while (ilines.hasNext()) {
            String[] rowPair = toRowPair(ilines.next());
            if (rowPair.length != 2 || rowPair[0].length() != rowPair[1].length()) {
                invalid();
            }

            m1Rows.add(rowPair[0]);
            m2Rows.add(rowPair[1]);

            counter++;

            if (counter == 1) {
                rowCountForCurrentMatrix = rowPair[0].length(); // the matrices are supposed to be square;
            }
            if (counter == rowCountForCurrentMatrix) {
                result.add(toMatrixPair(m1Rows, m2Rows));
                m1Rows = new ArrayList<>();
                m2Rows = new ArrayList<>();
                counter = 0;
            }
        }

        return result.toArray(new MatrixPair[0]);
    }

    private MatrixPair toMatrixPair(List<String> m1Rows, List<String> m2Rows) {
        return new MatrixPair(parser.parseList(m1Rows), parser.parseList(m2Rows));
    }

    private String[] toRowPair(String line) {
        return line.split("\\s+");
    }

    private void invalid() throws ParseException {
        throw new ParseException("Rotation test specification file is invalid", 0);
    }

    static class MatrixPair {
        short[][] first;
        short[][] second;

        MatrixPair(short[][] first, short[][] second) {
            this.first = first;
            this.second = second;
        }
    }
}
