package barrellchee.slider;

import aiproj.slider.Referee;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ai-partB
 * Created by David Barrell on 14/5/17.
 */
public class Generator {
    private static int count = 1;

    public static void main(String[] args) {
        while (count < 10001) {
            int dimension = ThreadLocalRandom.current().nextInt(5, 7 + 1);
            System.out.println("Starting game number " + count + " with dimension " + dimension);
            Referee.main(new String[]{
                    String.valueOf(dimension)
                    , "barrellchee.slider.RandomPlayer"
                    , "barrellchee.slider.RandomPlayer"
            });
            count++;
        }
    }
}
