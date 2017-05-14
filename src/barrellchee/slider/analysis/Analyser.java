package barrellchee.slider.analysis;

import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ai-partB
 * Created by David Barrell on 14/5/17.
 */
public class Analyser {

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        Path gamesFolder = Paths.get("games");
        Path dataFile = Paths.get("data.csv");

        try(Stream<Path> paths = Files.list(gamesFolder)) {
            OpenOption mode;
            if (!Files.exists(dataFile)) {
                lines.add("HEADER");
                mode = StandardOpenOption.CREATE;
            } else {
                mode = StandardOpenOption.APPEND;
            }

            long numOfFiles = Files.list(gamesFolder).count();
            System.out.println("Number of files to analyse: " + numOfFiles);

            List<List<String>> results = new ArrayList<>();
            paths.forEach((path) -> {
                if (!Files.isRegularFile(path)) {
                    System.err.println("Path not file");
                    System.exit(1);
                }
                results.add(analyse(path));
            });

            for (List<String> res : results) {
                lines = Stream.concat(lines.stream(), res.stream()).collect(Collectors.toList());
            }

            System.out.println(dataFile.toAbsolutePath().toString());
            Files.write(dataFile, lines, Charset.forName("UTF-8"), mode);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to print to file");
        }
    }

    private static List<String> analyse(Path file) {
        List<String> ret = new ArrayList<>();
        ret.add(file.toAbsolutePath().toString());
        return ret;
    }
}
