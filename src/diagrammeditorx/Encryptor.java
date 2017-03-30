package diagrammeditorx;

/**
 * Класс для шифрования/расшифрования файлов диаграммы.
 * Шифрование происходит алгоритмом Полибия с использованием ключа, задаваемого пользователем.
 */
public class Encryptor {
    
    // Шифруемый алфавит
    private static String alphabet = "абвгдежзийклмнопрстуфхцчшщъыьэюя"
                    + "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789[].,-\";><";
    // Индексы строк и столбцов таблицы полибия
    private static String board = "0123456789.,";
    
    /**
     * Функция шифрования текста
     * @param text - шифруемый текст
     * @param key - ключ шифрования
     * @return - зашифрованный текст
     */
    public static String encrypt(String text, String key) {
	String restext = "";    // Полученный зашифрованный текст
	int pos;                // Позиция текущего символа в таблице Полибия
        int keyInd = 0;         // Индекс текущего элемента ключа

        // Для каждого символа шифруемого текста
	for (int i = 0; i < text.length(); ++i)
        {
            // Определить позицию символа в алфавите
            pos = alphabet.indexOf(text.charAt(i));
            // Если символ не найден в алфавите - не шифруем его
            if (pos == -1)
                restext += text.charAt(i);
            // Иначе
            else
            {
                // Если ключ задан, сместить позицию символа в алфавите на значение текущего символа ключа
                if (key.length() > 0) {
                    pos = (pos + key.charAt(keyInd)) % alphabet.length();
                    keyInd = (keyInd + 1) % key.length();
                }
                // Заменить полученную позицию в таблице на индексы строки и столбца
                restext += board.charAt(pos / board.length());
                restext += board.charAt(pos % board.length());
            }
        }
        return restext;
    }
    
    /**
     * Функция расшифрования текста
     * @param text - зашифрованный текст
     * @param key - ключ шифрования
     * @return - расшифрованный текст
     */
    public static String decrypt(String text, String key) {	
        String restext = "";    // Полученный расшифрованный текст
	int row, col, pos;      // Индекс строки и столбца таблицы и позиция символа в таблице соответственно
        int keyInd = 0;         // Индекс текущего элемента ключа

        // Для каждой пары символов зашифрованного текста
        for (int i = 0; i < text.length(); i += 2)
        {
            // Определить индекс строки текущего символа в таблице
            row = board.indexOf(text.charAt(i));
            // Если символ не найден в таблице, значит он не зашифрован
            if (row == -1)
            {
                restext += text.charAt(i);
                i--;
            }
            // Иначе
            else
            {
                // Определить индекс столбца таблицы
                col = board.indexOf(text.charAt(i + 1));
                // По индексам строки и столбца определить позицию символа в алфавите
                pos = row * board.length() + col;
                // Если ключ задан, сместить позицию символа в алфавите на значение текущего символа ключа
                if(key.length() > 0) {
                    pos = (pos - key.charAt(keyInd) + alphabet.length()) % alphabet.length();
                    keyInd = (keyInd + 1) % key.length();
                }
                // Добавить к результирующему тексту текущий расшифрованный символ
                restext += alphabet.charAt(pos);
            }
        }
        return restext;
    }
}
