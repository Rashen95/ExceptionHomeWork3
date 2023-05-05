import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
 * Фамилия Имя Отчество датарождения номертелефона пол
 * Форматы данных:
 * фамилия, имя, отчество - строки
 * дата_рождения - строка формата dd.mm.yyyy
 * номер_телефона - целое беззнаковое число без форматирования
 * пол - символ латиницей f или m.
 * Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки,
 * обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.
 * Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
 * Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы.
 * Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано,
 * пользователю выведено сообщение с информацией, что именно неверно.
 * Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида
 * <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
 * Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
 * Не забудьте закрыть соединение с файлом.
 * При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.
 */

public class Program {
    public static void main(String[] args) {
        new Menu().mainMenu();
    }
}

class Menu {
    /**
     * Основное меню
     */
    public void mainMenu() {
        boolean flag = false;
        Scanner s = new Scanner(System.in);
        while (!flag) {
            System.out.println(UserInterface.firstMenu());
            String change = s.nextLine().replaceAll(" ", "");
            if (change.equals("1")) {
                flag = true;
                try {
                    PeopleAdder.add();
                } catch (NoDateException | NoPhoneException e) {
                    System.out.println(e.getMessage());
                    mainMenu();
                }
            } else if (change.equals("2")) {
                flag = true;
                System.out.println(UserInterface.exitProgramMessage());
            } else {
                System.out.println(UserInterface.incorrectValueMessage());
            }
        }
    }
}

class PeopleAdder {
    /**
     * Добавление человека в базу
     */
    public static void add() throws NoDateException, NoPhoneException {
        boolean flag = true;
        int index_data;
        String data = null;
        int index_phone_number;
        long phone_number;
        int index_firstname;
        int index_lastname;
        int index_surname;
        int index_gender;
        System.out.println(UserInterface.giveMeInfo());
        String user_input = UserInput.input();
        user_input = Parser.pars(user_input);
        String[] user_input_massive = user_input.split(" ");
        try {
            Validator.amountOfDataExamination(user_input_massive);
        } catch (WrongAmountOfDataException e) {
            System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><>");
            System.out.printf("Количество данных должно быть равно 6, а вы ввели %d\n", e.getAmountOfData());
            System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><>");
            new Menu().mainMenu();
            flag = false;
        }
        if (flag) {
            index_data = Validator.dataSearch(user_input_massive);
            if (index_data == 10) {
                throw new NoDateException();
            }
            else {
                data = user_input_massive[index_data];
                user_input_massive[index_data] = "null";
            }
        }
        if (flag) {
            index_phone_number = Validator.phoneSearch(user_input_massive);
            if (index_phone_number == 10) {
                throw new NoPhoneException();
            }
            else {
                phone_number = Long.parseLong(user_input_massive[index_phone_number]);
                user_input_massive[index_phone_number] = "null";
            }
            System.out.printf("%s %d", data, phone_number);
        }
        if (flag) {
            // Тут прописываем поиск строки с F или M
        }
        if (flag) {
            // Тут прописываем последнее - запись ФИО
        }
    }
}

class Validator {
    /**
     * Проверка введенных данных на валидность
     *
     * @param s массив строк
     * @throws WrongAmountOfDataException ошибка неверного количества данных
     */
    public static void amountOfDataExamination(String[] s) throws WrongAmountOfDataException {
        if (s.length != 6) {
            if (s.length == 1 && s[0].equals("")) {
                throw new WrongAmountOfDataException(0);
            }
            throw new WrongAmountOfDataException(s.length);
        }
    }

    public static int dataSearch(String[] s) {
        String regex = "^[0-9][0-9]\\.[0-9][0-9]\\.[0-9][0-9][0-9][0-9]$";
        int index_data = 10;
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < s.length; i++) {
            Matcher matcher = pattern.matcher(s[i]);
            if (matcher.matches()) {
                index_data = i;
            }
        }
        return index_data;
    }

    public static int phoneSearch(String[] s) {
        String regex = "^8[1-9][1-9][1-9][1-9][1-9][1-9][1-9][1-9][1-9][1-9]$";
        int index_phone_number = 10;
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < s.length; i++) {
            Matcher matcher = pattern.matcher(s[i]);
            if (matcher.matches()) {
                index_phone_number = i;
            }
        }
        return index_phone_number;
    }
}

class UserInput {
    /**
     * Пользовательский ввод
     *
     * @return Строка введенная пользователем
     */
    public static String input() {
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}

class Parser {
    /**
     * Парсер (убирает из текста лишние пробелы)
     *
     * @param user_text введенный пользователем текст
     * @return Отформатированный текст
     */
    public static String pars(String user_text) {
        StringBuilder text = new StringBuilder(user_text.trim());
        String[] text_massive = text.toString().split("");
        text = new StringBuilder();
        text.append(text_massive[0]);
        for (int i = 1; i < text_massive.length; i++) {
            if (!(text_massive[i].equals(text_massive[i - 1]) && text_massive[i].equals(" "))) {
                text.append(text_massive[i]);
            }
        }
        return text.toString();
    }
}

class UserInterface {
    public static String giveMeInfo() {
        return "\nВведите Фамилию, Имя, Отчество,\nДату_рождения (в формате дд.мм.гггг),\nНомер_телефона (11-значное число через восьмерку)\nи ваш пол (f или m) через пробел:";
    }

    public static String exitProgramMessage() {
        return "Завершаю работу приложения";
    }

    public static String incorrectValueMessage() {
        return "Вы ввели неверное значение";
    }

    public static String firstMenu() {
        return """
                                
                1. Добавить нового человека в базу
                2. Завершить работу приложения
                Выберите интересующий пункт меню:""";
    }
}

abstract class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}

class WrongAmountOfDataException extends MyException {
    private final int amountOfData;

    public int getAmountOfData() {
        return amountOfData;
    }

    public WrongAmountOfDataException(int amountOfData) {
        super("Неверное количество введенных данных");
        this.amountOfData = amountOfData;
    }
}

class NoDateException extends MyException {
    public NoDateException() {
        super("В введенных данных отсутствует дата вашего рождения");
    }
}

class NoPhoneException extends MyException {
    public NoPhoneException() {
        super("В введенных данных отсутствует номер телефона");
    }
}