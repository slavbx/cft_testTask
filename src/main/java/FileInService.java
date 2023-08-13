import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileInService {
    private List<FileIn> filesIn;
    private StartArgs startArgs;

    public FileInService(StartArgs startArgs) {
        this.startArgs = startArgs;
        this.filesIn = new ArrayList<>();
        init();
    }

    public List<FileIn> getFilesIn() {
        return filesIn;
    }

    private void init() {
        // пропускаем имя out-файла, по остальным именам создаём fileIn-объекты
        for (int i = 1; i < startArgs.getFileNames().size(); i++) {
            filesIn.add(new FileIn(startArgs.getFileNames().get(i)));
        }
    }

    public List<FileIn> getFilesInAvailable() {
        List<FileIn> result = new ArrayList<>();
        for (FileIn f : filesIn) {
            if (f.isAvailable()) result.add(f);
        }
        return result;
    }

    public List<FileIn> getFilesInWithMinCurLine() {
        //Формируем лист с одним или несколькими fileIn, у которых минимальные curLine
        List<FileIn> result = new ArrayList<>();
        if (isAnyFileInAvailable()) {
            result.add(getFilesInAvailable().get(0));
            for (int i = 1; i < getFilesInAvailable().size(); i++) {
                //Поиск минимальных (и равных им) curLine (строк или чисел)
                if (compareLines(getFilesInAvailable().get(i).getCurLine(), result.get(0).getCurLine()) < 0) {
                    result.clear();
                    result.add(getFilesInAvailable().get(i));
                } else if (compareLines(getFilesInAvailable().get(i).getCurLine(), result.get(0).getCurLine()) == 0) {
                    result.add(getFilesInAvailable().get(i));
                }
            }
        }
        return result;
    }

    public boolean isAnyFileInAvailable() { //Проверка списка входных файлов на наличие доступного
        boolean result = false;
        for (FileIn f : filesIn) {
            if (f.isAvailable()) result = true;
        }
        return result;
    }

    public void updateCurLines() {
        for (FileIn f : filesIn) { //обходим входные файлы для обновления текущей строки
            if (f.isAvailable()) {
                try (BufferedReader br = new BufferedReader(new FileReader(f.getName()))) {
                    for (int i = 0; i < f.getCursor(); i++) { //холостой проход до курсора
                        br.readLine();
                    }
                    //читаем строку из каждого файла по курсору
                    //если строка ошибочная, читаем следующую, пока не закончатся данные
                    String line = br.readLine();
                    while (line != null && !isValidLine(line, f)) {
                        line = br.readLine();
                        f.incCursor();
                    }
                    if (line != null) {
                        f.setPrevLine(f.getCurLine());
                        f.setCurLine(line); //обновляем текущую строку
                    } else {
                        f.setAvailable(false); //блокируем чтение: закончились строки
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Файл" + " " + f.getName() + " не найден");
                    f.setAvailable(false); //блокируем чтение: файл не найден
                } catch (IOException e) {
                    System.out.println("Файл" + " " + f.getName() + " недоступен");
                    f.setAvailable(false); //блокируем чтение: файл недоступен
                }
            }
        }
    }

    private boolean isValidLine(String line, FileIn f) { //Проверка строки на валидность
        boolean result = true;
        if (startArgs.isTypeInt()) { //Проверка числа на его корректную запись
            if (!line.matches("^(-)?\\d+$")) {
                result = false;
            }
        } else {
            if (line.contains(" ")) { //Проверка строки на отсутствие пробелов
                result = false;
            }
        }
        if (result && f.getCursor() > 0) { //Проверка текущей строки на увеличение значения (проверка на сортировку)
            if (compareLines(f.getCurLine(), line) > 0) result = false;
        }
        if (!result) System.out.println("Файл " + f.getName() + " строка " + (f.getCursor() + 1) + " ошибочная");
        return result;
    }

    private int compareLines(String line1, String line2) { //Компаратор для любого типа (строка,  число)
        int result = 0;
        if (startArgs.isTypeInt()) {
            if (Integer.parseInt(line1) < Integer.parseInt(line2)) result = -1;
            if (Integer.parseInt(line1) == Integer.parseInt(line2)) result = 0;
            if (Integer.parseInt(line1) > Integer.parseInt(line2)) result = 1;
        } else {
            if (line1.compareTo(line2) < 0) result = -1;
            if (line1.compareTo(line2) == 0) result = 0;
            if (line1.compareTo(line2) > 0) result = 1;
        }
        return result;
    }

}
