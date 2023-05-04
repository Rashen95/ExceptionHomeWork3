import java.util.Scanner;

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
        new Menu().MainMenu();
    }
}

class Menu {
    /**
     * Основное меню
     */
    public void MainMenu() {
        boolean flag = false;
        Scanner s = new Scanner(System.in);
        while (!flag) {
            System.out.println(UserInterface.firstMenu());
            String change = s.nextLine().replaceAll(" ", "");
            if (change.equals("1")) {
                flag = true;
                PeopleAdder.add();
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
    public static void add() {
        System.out.println(UserInterface.giveMeInfo());
        String user_input = UserInput.input();
        user_input = Parser.pars(user_input);
        String[] user_input_massive = user_input.split(" ");
        try {
            Validator.examination(user_input_massive);
        } catch (ExceptionWrongAmountOfData e) {
            System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><>");
            System.out.printf("Количество данных должно быть равно 6, а вы ввели %d\n", e.getAmountOfData());
            System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><>");
            new Menu().MainMenu();
        }
    }
}

class Validator {
    /**
     * Проверка введенных данных на валидность
     *
     * @param s массив строк
     * @throws ExceptionWrongAmountOfData ошибка неверного количества данных
     */
    public static void examination(String[] s) throws ExceptionWrongAmountOfData {
        if (s.length != 6) {
            throw new ExceptionWrongAmountOfData("Неверное количество введенных данных", s.length);
        }
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

class ExceptionWrongAmountOfData extends MyException {
    private final int amountOfData;

    public int getAmountOfData() {
        return amountOfData;
    }

    public ExceptionWrongAmountOfData(String message, int amountOfData) {
        super(message);
        this.amountOfData = amountOfData;
    }
}