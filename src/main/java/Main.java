import com.beust.jcommander.JCommander;

import java.io.*;
import java.util.List;

public class Main {
    /*
    Программа сортировки слиянием нескольких файлов.
    Автор: Ткаченко Станислав.
    Описание алгоритма: Для хранения атрибутов входных файлов создан класс FileIn. Класс FileInService обрабатывает
    эти данные и производит чтение строк из каждого файла. В каждой итерации такого чтения пропускаются ошибочные строки,
    а также те, которые выбиваются из сортированного порядка входного файла. Затем, строка с минимальным значением
    и равные ей записываются в выходной файл, курсоры этих входных файлов передвигаются на следующие строки.
    И так до завершения обработки всех строк.
    Опция сортировки в обратном порядке: Выходной файл, полученный методом слияния, построчно копируется в предварительно
    созданный пустой "реверсный" выходной файл такого же размера. Строки записываются от конца к началу файла.
    Затем, исходный выходной файл удаляется, а "реверсный" переименовывается.

    Класс Sorter в программе не используется. Это реализация рекурсивного алгоритма сортировки массива методом слияния.
    Позже я его перевел с массивов на списки. От его использования отказался, так как посчитал излишним сортировать
    повторно уже отсортированные данные в файлах.
    */


    public static void main(String[] args) {
        //Разбираем аргументы командной строки
        StartArgs startArgs = new StartArgs();
        String[] argv = {"-a", "-i", "out.txt", "in_i1.txt", "in_i2.txt", "in_i3.txt"};
       //String[] argv = {"-a", "-s", "out.txt", "in_s1.txt", "in_s2.txt", "in_s3.txt"};
        JCommander.newBuilder().addObject(startArgs).build().parse(argv);
//        JCommander.newBuilder().addObject(startArgs).build().parse(args);
        startArgs.run();
        System.out.println("Начинаю слияние исходных файлов...");

        //Создаем выходной файл. Если имеется - перезаписываем
        File fileOut = new File(startArgs.getFileNames().get(0));
        try {
            if (fileOut.exists()) fileOut.delete();
            fileOut.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Создаём сервис для работы с входными файлами
        FileInService fileInService = new FileInService(startArgs);
        while (fileInService.isAnyFileInAvailable()) {
            fileInService.updateCurLines(); //Обходим входные файлы для обновления текущей строки
            //Записываем минимальные текущие строки в выходной файл и передвигаем курсоры
            List<FileIn> filesInWithMinLine = fileInService.getFilesInWithMinCurLine();
            for (FileIn f: filesInWithMinLine) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut, true))) {
                    bw.write(f.getCurLine());
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                f.incCursor();
            }
        }

        if (startArgs.isSortDesc()) { //Обратный порядок сортировки
            System.out.println("Реверсирую выходной файл...");

            //Создаем выходной временный файл
            File fileOutRev = new File("out_rev.txt");
            try {
                if (fileOutRev.exists()) fileOutRev.delete();
                fileOutRev.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Зная смещение, пишем в реверс файл с конца в начало
            try (BufferedReader br = new BufferedReader(new FileReader(fileOut));
                 RandomAccessFile raf = new RandomAccessFile(fileOutRev, "rw")) {
                String line = "";
                long bytesPassed = 0L;
                while(line != null) {
                    line = br.readLine();
                    if (line != null) {
                        line = line + "\r\n";
                        raf.seek(fileOut.length() - line.getBytes().length - bytesPassed);
                        raf.writeBytes(line);
                        bytesPassed += line.getBytes().length;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileOut.delete();
            fileOutRev.renameTo(fileOut);
        }
        System.out.println("Слияние завершено");
    }
}
