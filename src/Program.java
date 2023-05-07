import java.io.FileWriter;
import java.io.IOException;
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
                    PeopleAdder.searcher();
                } catch (NoDateException | NoPhoneException | NoGenderException | NoFIOException e) {
                    System.out.println(e.getMessage());
                }
                finally {
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
     * Поиск всех необходимых данных в строке и передача массива данных для записи в файл
     */
    public static void searcher() throws NoDateException, NoPhoneException, NoGenderException, NoFIOException {
        boolean flag = true;
        int index_data;
        String data;
        int index_phone_number;
        long phone_number;
        int index_firstname;
        String firstname;
        int index_lastname;
        String lastname;
        int index_surname;
        String surname;
        int index_gender;
        String gender;
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
            flag = false;
        }
        if (flag) {
            // Поиск даты
            index_data = Validator.dataSearch(user_input_massive);
            if (index_data == 10) {
                throw new NoDateException();
            } else {
                data = user_input_massive[index_data];
                user_input_massive[index_data] = "0";
            }

            // Поиск номера телефона
            index_phone_number = Validator.phoneSearch(user_input_massive);
            if (index_phone_number == 10) {
                throw new NoPhoneException();
            } else {
                phone_number = Long.parseLong(user_input_massive[index_phone_number]);
                user_input_massive[index_phone_number] = "0";
            }

            // Поиск пола
            index_gender = Validator.genderSearch(user_input_massive);
            if (index_gender == 10) {
                throw new NoGenderException();
            } else {
                gender = user_input_massive[index_gender];
                user_input_massive[index_gender] = "0";
            }

            // Поиск Фамилии
            index_lastname = Validator.lastNameSearch(user_input_massive);
            if (index_lastname == 10) {
                throw new NoFIOException();
            } else {
                lastname = user_input_massive[index_lastname];
                user_input_massive[index_lastname] = "0";
            }

            // Поиск Имени
            index_firstname = Validator.firstNameSearch(user_input_massive);
            if (index_firstname == 10) {
                throw new NoFIOException();
            } else {
                firstname = user_input_massive[index_firstname];
                user_input_massive[index_firstname] = "0";
            }

            // Поиск Отчества
            index_surname = Validator.surNameSearch(user_input_massive);
            if (index_surname == 10) {
                throw new NoFIOException();
            } else {
                surname = user_input_massive[index_surname];
                user_input_massive[index_surname] = "0";
            }
            String[] full_info = new String[]{lastname, firstname, surname, data, Long.toString(phone_number), gender};
            writer(full_info);
        }
    }

    /**
     * Запись полученных ВАЛИДНЫХ данных в файл
     * */
    public static void writer(String[] info) {
        try (FileWriter writer = new FileWriter(String.format("%s.txt", info[0]), true)) {
            writer.write(String.format("%s %s %s %s %s %s\n", info[0], info[1], info[2], info[3], info[4], info[5]));
            writer.flush();
            System.out.printf("%s %s %s успешно добавлен в базу\n", info[0], info[1], info[2]);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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

    /**
     * Находит в массиве дату, соответствующую необходимому формату
     * @param s переданный массив с информацией
     * @return Индекс даты в массиве
     */
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

    /**
     * Находит в массиве номер телефона, соответствующий необходимому формату
     * @param s переданный массив с информацией
     * @return Индекс номера телефона в массиве
     */
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

    /**
     * Находит в массиве пол человека
     * @param s переданный массив с информацией
     * @return Индекс пола человека в массиве
     */
    public static int genderSearch(String[] s) {
        int index_gender = 10;
        for (int i = 0; i < s.length; i++) {
            if (s[i].equalsIgnoreCase("f") || s[i].equalsIgnoreCase("m")) {
                index_gender = i;
            }
        }
        return index_gender;
    }

    /**
     * Находит в массиве Фамилию
     * @param s переданный массив с информацией
     * @return Индекс фамилии в массиве
     */
    public static int lastNameSearch(String[] s) {
        String regex = "^[A-Za-zА-Яа-я]+$";
        int index_lastname = 10;
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < s.length; i++) {
            Matcher matcher = pattern.matcher(s[i]);
            if (matcher.matches()) {
                index_lastname = i;
                return index_lastname;
            }
        }
        return index_lastname;
    }

    /**
     * Находит в массиве Имя
     * @param s переданный массив с информацией
     * @return Индекс имени в массиве
     */
    public static int firstNameSearch(String[] s) {
        String regex = "^[A-Za-zА-Яа-я]+$";
        int index_firstname = 10;
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < s.length; i++) {
            Matcher matcher = pattern.matcher(s[i]);
            if (matcher.matches()) {
                index_firstname = i;
                return index_firstname;
            }
        }
        return index_firstname;
    }

    /**
     * Находит в массиве Отчество
     * @param s переданный массив с информацией
     * @return Индекс отчества в массиве
     */
    public static int surNameSearch(String[] s) {
        String regex = "^[A-Za-zА-Яа-я]+$";
        int index_surname = 10;
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < s.length; i++) {
            Matcher matcher = pattern.matcher(s[i]);
            if (matcher.matches()) {
                index_surname = i;
                return index_surname;
            }
        }
        return index_surname;
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

class NoGenderException extends MyException {
    public NoGenderException() {
        super("В введенных данных отсутствует пол");
    }
}

class NoFIOException extends MyException {
    public NoFIOException() {
        super("Некорректно введены ФИО");
    }
}