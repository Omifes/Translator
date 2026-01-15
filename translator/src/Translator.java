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

class TrieNode {
    Map<String, TrieNode> children;
    String translation;

    public TrieNode() {
        this.children = new HashMap<>();
        this.translation = null;
    }

    public boolean hasTranslation() {
        return translation != null;
    }
}

public class Translator {
    private final TrieNode root = new TrieNode();
    private int dictionarySize = 0;

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
        root.children.clear();
        dictionarySize = 0;

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

                String phrase = parts[0].trim();
                String translation = parts[1].trim();

                if (phrase.isEmpty() || translation.isEmpty()) {
                    throw new InvalidFileFormatException(
                            String.format("Пустое слово или перевод в строке %d", i + 1)
                    );
                }

                String[] words = phrase.toLowerCase().split("\\s+");

                addToTrie(words, translation);
                dictionarySize++;
            }

            System.out.println("Записей в словаре: " + dictionarySize);

        } catch (NoSuchFileException e) {
            throw new FileReadException("Файл не найден: " + filename);
        } catch (AccessDeniedException e) {
            throw new FileReadException("Нет доступа к файлу: " + filename);
        } catch (IOException e) {
            throw new FileReadException("Ошибка ввода-вывода при чтении файла: " + filename);
        }
    }

    private void addToTrie(String[] words, String translation) {
        TrieNode current = root;

        for (String word : words) {

            current.children.putIfAbsent(word, new TrieNode());
            current = current.children.get(word);
        }

        if (current.translation == null ||
                current.translation.length() < translation.length()) {
            current.translation = translation;
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

        String[] tokens = text.split("(?<=\\b)|(?=\\b)");
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < tokens.length) {
            String token = tokens[i];

            if (!token.matches("\\w+")) {
                result.append(token);
                i++;
                continue;
            }

            TranslationMatch match = findLongestMatch(tokens, i);

            if (match != null) {
                result.append(match.translation);
                i = match.endIndex;
            } else {
                result.append(token);
                i++;
            }
        }

        return result.toString();
    }

    private TranslationMatch findLongestMatch(String[] tokens, int startIndex) {
        TrieNode current = root;
        TranslationMatch bestMatch = null;
        int tokenIndex = startIndex;

        while (tokenIndex < tokens.length) {
            String token = tokens[tokenIndex];

            if (token.matches("\\s+")) {
                tokenIndex++;
                continue;
            }

            if (!token.matches("\\w+")) {
                break;
            }

            String word = token.toLowerCase();
            current = current.children.get(word);

            if (current == null) {
                break;
            }

            if (current.hasTranslation()) {
                bestMatch = new TranslationMatch(
                        current.translation,
                        tokenIndex + 1
                );
            }

            tokenIndex++;
        }

        return bestMatch;
    }

    private static class TranslationMatch {
        String translation;
        int endIndex;

        TranslationMatch(String translation, int endIndex) {
            this.translation = translation;
            this.endIndex = endIndex;
        }
    }
}
