package binarysailor.tetrese.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unused")
class BlockMatrixParser {

    private final char blankChar;
    private final char occupiedChar;

    public BlockMatrixParser(char blankChar, char occupiedChar) {
        this.blankChar = blankChar;
        this.occupiedChar = occupiedChar;
    }

    public short[][] parseArray(String[] rows) {
        return parseStream(Arrays.stream(rows));
    }

    public short[][] parseList(List<String> rows) {
        return parseStream(rows.stream());
    }

    private short[][] parseStream(Stream<String> rows) {
        return rows.map(this::mapRow)
                .collect(toList())
                .toArray(new short[0][]);
    }

    private short[] mapRow(String strRow) {
        short[] aRow = new short[strRow.length()];
        for (int i = 0; i < aRow.length; i++) {
            aRow[i] = (short) (strRow.charAt(i) == blankChar ? 0 : 1);
        }

        return aRow;
    }
}