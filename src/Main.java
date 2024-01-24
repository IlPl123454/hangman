import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.nio.file.Files;


public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Path path = Paths.get("src", "words.txt");
    static String GAME_STATE_WIN = "Вы выиграли! Поздравляем!";
    static String GAME_STATE_LOSE = "Вы проиграли! Допущено слишком много ошибок.";
    static String GAME_STATE_NOT_FINISHED = "Игра не окончена";
    static int POSSIBLE_MISTAKES = 6;


    public static void main(String[] args) {
        startNewGame();
    }

    public static void startNewGame(){
        System.out.println("Добро пожаловать в игру Виселица!");

        if (startNewGameCheck()) {
            ArrayList<String> words = getWordList(path);
            startNewRound(words);
        }

    }
    public static void startNewRound(ArrayList<String > words){

        do {
            String word = getRandomWord(words);
            word = word.toLowerCase();
            gameLoop(word);
        } while (startNewGameCheck());

    }

    public static void gameLoop(String word) {

        String hiddenWord = makeWordHidden(word);

        HashSet<Character> usedLetters2 = new HashSet<>();

        int mistakes = 0;

        char letter;

        while (checkGameStatus(word, hiddenWord, mistakes).equals(GAME_STATE_NOT_FINISHED)) {

            System.out.println("Слово: " + hiddenWord);

            letter = inputLetterFromUser(usedLetters2);

            if (isLetterInWord(word, letter)) {
                System.out.println("Верно!");
            } else {
                System.out.println("Ошибка!");
                mistakes++;
            }
            usedLetters2.add(letter);

            printHangman(mistakes);

            hiddenWord = insertRightLetterInHiddenWord(word, hiddenWord, letter);


            System.out.println("Использованные буквы: " + usedLetters2);
            printUsedLetters(usedLetters2);

            System.out.println("Количество возможных ошибок - " + (POSSIBLE_MISTAKES - mistakes) + "\n");
        }

        System.out.println(checkGameStatus(word, hiddenWord, mistakes));
        System.out.println("Загаданное слово - " + word + "\n");
    }


    public static ArrayList<String> getWordList(Path path) {
        // открыть файл со словами
        ArrayList<String> words = new ArrayList<>();

        BufferedReader reader = null;

        try {
            reader = Files.newBufferedReader(path);
            String word;
            while ((word = reader.readLine()) != null){
                words.add(word);
            }

        } catch (IOException e) {
            System.out.println("Couldn't open the file!");
        } finally {
            try {
                if (reader != null){
                    reader.close();
                }
            }
            catch (IOException ex){
                System.out.println("Не удалось найти закрываемый файл");
            }
            }
        return words;

    }

    public  static String getRandomWord(ArrayList<String> words){
        Random random = new Random();
        int randomNumber = random.nextInt(words.size());

        String word = words.get(randomNumber);
        words.remove(randomNumber);

        return word.toLowerCase();
    }

    public static void printHangman(int mistakes) {
        String[] hangman = {
                "",
                "",
                ""
        };

        if (mistakes > 0) hangman[0] = "O";
        if (mistakes > 1) hangman[1] = " □";
        if (mistakes > 2) hangman[1] = "/□";
        if (mistakes > 3) hangman[1] = "/□\\";
        if (mistakes > 4) hangman[2] = "/";
        if (mistakes > 5) hangman[2] = "/ \\";

        String top = "_____________\n" + "|           |\n";
        String head = "|           " + hangman[0] + "\n";
        String body = "|          " + hangman[1] + "\n";
        String legs = "|          " + hangman[2] + "\n";
        String bottom = "|\n" + "‾‾‾‾‾‾‾‾‾‾‾‾‾\n";

        System.out.print("\n" + top + head + body + legs + bottom);
        }

    public static String  makeWordHidden(String word){
        return "_".repeat(word.length());
    }

    public static boolean  isLetterInWord(String word, char letter) {

        letter = Character.toLowerCase(letter);

        for (int i = 0; i < word.length(); i++){
            if (word.charAt(i) == letter){
                return true;
            }
        }
        return false;
    }


    public static String  insertRightLetterInHiddenWord(String word, String hiddenWord, char letter) {

        StringBuilder sb = new StringBuilder(hiddenWord);

        for (int i = 0; i < word.length(); i++){
            if (word.charAt(i) == letter){
                sb.replace(i, i + 1, String.valueOf(letter));
            }
        }

        hiddenWord = sb.toString();

        return hiddenWord;
        }

    public static char inputLetterFromUser(HashSet<Character> usedLetters){
        char letter;
        do {
            System.out.print("Введите букву: ");
            letter = scanner.next().charAt(0);
            letter = Character.toLowerCase(letter);
            if (usedLetters.contains(letter)){
                System.out.println("Вы уже использовали эту букву. Попробуйте другую.");
            } else if (letter < 'а' || letter > 'я') {
                System.out.println("Введите букву русского алфавита.");
            } else {
                break;
            }
        } while (true);

        return letter;
    }


    public  static  void printUsedLetters (HashSet<Character> usedLetters){
        System.out.print("Использованные буквы:");

        for (char usedLetter : usedLetters) {
            System.out.print(usedLetter + ", ");
        }
        System.out.println();
    }

    public static String  checkGameStatus(String word, String hiddenWord, int mistakes){
        if (mistakes == POSSIBLE_MISTAKES) {
            return GAME_STATE_LOSE;
        } else if (word.equals(hiddenWord)) {
            return GAME_STATE_WIN;
        } else {
            return GAME_STATE_NOT_FINISHED;
        }

    }

    public static boolean startNewGameCheck() {
        System.out.print("Начать новую игру? \nВведите Д/Н: ");
        char letter;
        while (true) {
            letter = scanner.next().charAt(0);
            letter = Character.toLowerCase(letter);
            if (letter == 'д') {
                System.out.println();
                return true;
            } else if (letter == 'н') {
                System.out.println("Спасибо за игру!");
                return false;
            } else {
                System.out.print("Введите 'Д' для начала новой игры или 'Н' для выхода из игры: ");
            }
        }
    }
}

