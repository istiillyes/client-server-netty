package message;

import java.util.Random;

/**
 * Created by isti on 2/24/14.
 */
public class CharacterGenerator {

    public static final int A_ASCII_CODE = 65;
    public static final Random RANDOM = new Random(23);

    private CharacterGenerator() {
    }

    /**
     * Returns a random uppercase character from the first {@code limit} characters of the english alphabet.
     *
     * @param limit index of last character that is used for generation
     * @return
     */
    public static char getRandomCharacter(int limit) {
        return Character.toChars(A_ASCII_CODE + RANDOM.nextInt(limit))[0];
    }
}
