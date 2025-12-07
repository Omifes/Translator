import java.io.*;
import java.nio.file.*;
import java.util.*;

class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}

class FileReadException extends Exception {
    public FileReadException(String message) {
        super(message);
    }
}

public class Translator {
    private final Map<String, String> dictionary = new HashMap<>();

    public static void main(String[] args) {
        Translator translator = new Translator();

        try {
            translator.readDictionary("dictionary.txt");

            String result = translator.translateFile("input.txt");

            System.out.println("Результат перевода:");
            System.out.println(result);

        } catch (InvalidFileFormatException e) {
            System.err.println("Ошибка формата файла: " + e.getMessage());
        } catch (FileReadException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    public void readDictionary(String filename) throws InvalidFileFormatException, FileReadException {
        dictionary.clear();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 2) {
                    throw new InvalidFileFormatException(
                            String.format("Некорректный формат в строке %d: %s", i + 1, line)
                    );
                }

                String word = parts[0].trim().toLowerCase();
                String translation = parts[1].trim();

                if (word.isEmpty() || translation.isEmpty()) {
                    throw new InvalidFileFormatException(
                            String.format("Пустое слово или перевод в строке %d", i + 1)
                    );
                }

                if (!dictionary.containsKey(word) ||
                        dictionary.get(word).length() < translation.length()) {
                    dictionary.put(word, translation);
                }
            }

            System.out.println("Словарь загружен. Записей: " + dictionary.size());

        } catch (NoSuchFileException e) {
            throw new FileReadException("Файл не найден: " + filename);
        } catch (AccessDeniedException e) {
            throw new FileReadException("Нет доступа к файлу: " + filename);
        } catch (IOException e) {
            throw new FileReadException("Ошибка ввода-вывода при чтении файла: " + filename);
        }
    }

    public String translateFile(String filename) throws FileReadException {
        try {
            String content = Files.readString(Paths.get(filename));
            return translateText(content);

        } catch (NoSuchFileException e) {
            throw new FileReadException("Файл не найден: " + filename);
        } catch (AccessDeniedException e) {
            throw new FileReadException("Нет доступа к файлу: " + filename);
        } catch (IOException e) {
            throw new FileReadException("Ошибка ввода-вывода при чтении файла: " + filename);
        }
    }

    public String translateText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        String[] words = text.split("(?<=\\b)|(?=\\b)");
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < words.length) {
            String token = words[i];

            if (!token.matches("\\w+")) {
                result.append(token);
                i++;
                continue;
            }

            String bestMatch = findBestMatch(words, i);

            if (bestMatch != null) {
                result.append(dictionary.get(bestMatch));

                int wordsInPhrase = bestMatch.split(" ").length;
                int tokensInPhrase = wordsInPhrase * 2 - 1;

                i += tokensInPhrase;

            } else {
                String singleWord = token.toLowerCase();
                if (dictionary.containsKey(singleWord)) {
                    result.append(dictionary.get(singleWord));
                } else {
                    result.append(token);
                }
                i++;
            }
        }

        return result.toString();
    }

    private String findBestMatch(String[] words, int startIndex) {
        String bestMatch = null;
        StringBuilder phrase = new StringBuilder();

        for (int i = startIndex; i < words.length; i++) {
            String word = words[i];

            if (word.matches("\\s+")) {
                continue;
            }

            if (!word.matches("\\w+")) {
                break;
            }

            if (phrase.length() > 0) {
                phrase.append(" ");
            }
            phrase.append(word);

            String currentPhrase = phrase.toString().toLowerCase();

            if (dictionary.containsKey(currentPhrase)) {
                if (bestMatch == null || currentPhrase.length() > bestMatch.length()) {
                    bestMatch = currentPhrase;
                }
            }
        }

        return bestMatch;
    }
}