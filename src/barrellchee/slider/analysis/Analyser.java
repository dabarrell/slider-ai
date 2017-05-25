package barrellchee.slider.analysis;

import aiproj.slider.Move;
import barrellchee.slider.ArrayListSliderBoard;
import barrellchee.slider.ai.SliderAlphaBetaSearch;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;

import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * ai-partB
 * Created by David Barrell on 14/5/17.
 */
public class Analyser {
    private static final String HEADER
            = "dimension,movesToEnd,oppBlocked,removed,unblocked,moves,oppPathBlocked,utility";

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        Path gamesFolder = Paths.get("games");
        Path dataFile = Paths.get("data.csv");
        System.out.println(dataFile.toAbsolutePath().toString());

        try(Stream<Path> paths = Files.list(gamesFolder)) {
            if (!Files.exists(dataFile)) {
                List<String> header = new ArrayList<>();
                header.add(HEADER);
                Files.write(dataFile, header, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
            }

            long numOfFiles = Files.list(gamesFolder).count();
            System.out.println("Number of files to analyse: " + numOfFiles);

            List<List<String>> results = new ArrayList<>();
            paths.forEach((path) -> {
                if (!Files.isRegularFile(path)) {
                    System.err.println("Path not file");
                    System.exit(1);
                }
                if (!path.endsWith(".DS_Store")) {
                    try {
                        Files.write(dataFile, analyse(path), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error printing analysis of file: " + path);
                    }
                }
            });

            System.out.println("Finished");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to print to file");
        }
    }

    private static List<String> analyse(Path file) {
        List<String> ret = new ArrayList<>();
        try {
            List<String> input = Files.readAllLines(file, Charset.forName("UTF-8"));
            double utility;
            int dimension;

            if (input.get(0).equals("vertical!")) {
                utility = -1D;
            } else if (input.get(0).equals("horizontal!")) {
                utility = 1D;
            } else if (input.get(0).equals("nobody! (tie)")) {
                utility = 0D;
            } else {
                throw new Exception("Invalid first line");
            }

            dimension = Integer.valueOf(input.get(1));

            StringBuilder sbBoard = new StringBuilder();
            int i;
            for (i = 2; i < dimension + 2; i++) {
                sbBoard.append(input.get(i)).append("\n");
            }
            i++;
            String strBoard = sbBoard.toString();

            SliderGame game = new SliderGame(dimension, strBoard, ArrayListSliderBoard.class);
            SliderState state = game.getInitialState();

            state.setPlayerToMove('H');
            int moveCount = 0;
            do {
                String line = constructLine(dimension, utility, state, moveCount);
                ret.add(line);

                Move nextMove = parseMove(input.get(i));
                state = game.getResult(state, nextMove);
                i++;
                moveCount++;
            } while (i < input.size());

            // Add last line
            String line = constructLine(dimension, utility, state, moveCount);
            ret.add(line);

            System.out.println(file.toAbsolutePath().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String constructLine(int dimension, double utility, SliderState state, int moveCount) {
        // HEADER -> "dimension,movesToEnd,oppBlocked,removed,unblocked,moves,oppPathBlocked,utility";

        SliderGame game = new SliderGame(dimension, state.toString(), ArrayListSliderBoard.class);
        SliderAlphaBetaSearch search = new SliderAlphaBetaSearch(game,-1D,1D,2);
        List<SliderGame.WeightFeature> evalList = game.evalFeatures(state, 'H');
        List<String> values = new ArrayList<>();
        for (SliderGame.WeightFeature wf : evalList) {
            values.add(String.valueOf(wf.getValue()));
        }
        values.add(String.valueOf(search.eval(state,'H')));
        return String.join(",", values);
    }

    private static Move parseMove(String strMove) {
        int i;
        int j;
        Move.Direction d;

        String pattern = ".*?([0-9]).*?([0-9]).*?";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(strMove);
        if (m.find()) {
            i = Integer.parseInt(m.group(1));
            j = Integer.parseInt(m.group(2));

            if (strMove.contains("UP"))
                d = Move.Direction.UP;
            else if (strMove.contains("DOWN"))
                d = Move.Direction.DOWN;
            else if (strMove.contains("LEFT"))
                d = Move.Direction.LEFT;
            else
                d = Move.Direction.RIGHT;

            Move res = new Move(i, j, d);

            if (!strMove.equals(res.toString())) {
                System.err.println("Moves don't match");
                System.err.println("String: " + strMove + ", move:" + res);
                System.exit(1);
            }

            return res;
        } else {
            return null;
        }
    }
}
