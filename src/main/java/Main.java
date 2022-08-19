import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int MAX_WORDS = 100_000;
    private static final int WORD_MIN_LENGTH = 3;
    private static AtomicInteger countOf3 = new AtomicInteger(0);
    private static AtomicInteger countOf4 = new AtomicInteger(0);
    private static AtomicInteger countOf5 = new AtomicInteger(0);
    private static final int LENGTH_3 = 3;
    private static final int LENGTH_4 = 4;
    private static final int LENGTH_5 = 5;

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static String getPalindrome(String word) {
        String palindrome = new StringBuilder(word).reverse().toString();
        return palindrome;
    }

    private static boolean isPalindrome(String word1) {
        String palindrome1 = getPalindrome(word1);
        return (word1.equalsIgnoreCase(palindrome1));
    }

    private static boolean hasEqualChars(String word) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length-1; i++) {
            if (i < chars.length && chars[i] != chars[i+1]) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasAscOrder(String word) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length-1; i++) {
            if (chars[i] != chars[i+1] && chars[i] > chars[i+1]) {
                return false;
            }
        }
        return true;
    };

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[MAX_WORDS];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", WORD_MIN_LENGTH + random.nextInt(WORD_MIN_LENGTH));
        }

        Thread palindromeThread = new Thread(() -> {
            for (String str : texts) {
                if (str.length() == LENGTH_3 && isPalindrome(str)) {
                    countOf3.getAndAdd(1);
                }
                if (str.length() == LENGTH_4 && isPalindrome(str)) {
                    countOf4.getAndAdd(1);
                }
                if (str.length() == LENGTH_5 && isPalindrome(str)) {
                    countOf5.getAndAdd(1);
                }
            }
        });

        Thread equalsThread = new Thread(() -> {
            for (String str : texts) {
                if (str.length() == LENGTH_3 && hasEqualChars(str)) {
                    countOf3.getAndAdd(1);
                }
                if (str.length() == LENGTH_4 && hasEqualChars(str)) {
                    countOf4.getAndAdd(1);
                }
                if (str.length() == LENGTH_5 && hasEqualChars(str)) {
                    countOf5.getAndAdd(1);
                }
            }
        });

        Thread ascOrderThread = new Thread(() -> {
            for (String str : texts) {
                if (str.length() == LENGTH_3 && hasAscOrder(str)) {
                    countOf3.getAndAdd(1);
                }
                if (str.length() == LENGTH_4 && hasAscOrder(str)) {
                    countOf4.getAndAdd(1);
                }
                if (str.length() == LENGTH_5 && hasAscOrder(str)) {
                    countOf5.getAndAdd(1);
                }
            }
        });

        palindromeThread.start();
        equalsThread.start();
        ascOrderThread.start();
        try {
            palindromeThread.join();
            equalsThread.join();
            ascOrderThread.join();
            System.out.println("Красивых слов с длиной 3: " + countOf3.get() + " шт.");
            System.out.println("Красивых слов с длиной 4: " + countOf4.get() + " шт.");
            System.out.println("Красивых слов с длиной 5: " + countOf5.get() + " шт.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
