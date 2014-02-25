package message;

/**
 * Created by isti on 2/24/14.
 */
public class MessageStore {
    public final static String LAST_MESSAGE = "last message";
    public final static String CLOSE_MESSAGE = "close message";
    public static final String MESSAGE_1_KB = createMessage(1);
    public static final String MESSAGE_10_KB = createMessage(10);
    public static final String MESSAGE_100_KB = createMessage(100);
    public static final String MESSAGE_1_MB = createMessage(1024);
    public static final int MESSAGE_10_KB_FREQUENCY = 5;
    public static final int MESSAGE_100_KB_FREQUENCY = 10;
    public static final int MESSAGE_1_MB_FREQUENCY = 50;
    public static final int ALPHABET_SIZE = 26;

    private MessageStore() {
    }

    private static String createMessage(final int sizeInKB) {
        int sizeInChars = sizeInKB * 512;
        StringBuilder sb = new StringBuilder(sizeInChars);
        for (int i = 0; i < sizeInChars; i++) {
            sb.append(CharacterGenerator.getRandomCharacter(ALPHABET_SIZE));
        }
        return sb.toString();
    }

    public static String getMessage(int i) {

        if (i % MESSAGE_100_KB_FREQUENCY == 0) {
            return MESSAGE_100_KB;
        }
        if (i % MESSAGE_10_KB_FREQUENCY == 0) {
            return MESSAGE_10_KB;
        }
        return MESSAGE_1_KB;
    }
}
