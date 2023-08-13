# cft_testTask
Тестовое задание. Программа сортировки слиянием нескольких файлов

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

Версия Java: 1.8
Система сборки: Maven 4.0.0
Сторонние библиотеки: JCommander 1.78

Зависимости для подключения бибилиотек:
<!-- https://mvnrepository.com/artifact/com.beust/jcommander -->
<dependency>
    <groupId>com.beust</groupId>
    <artifactId>jcommander</artifactId>
    <version>1.78</version>
</dependency>

Для запуска программы с другими аргументами раскомментировать соответствующую строку в методе main.