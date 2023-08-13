import java.util.ArrayList;
import java.util.List;

public class Sorter {
    private List<String> lines;
    private boolean sortDesc;
    private boolean typeInt;

    public Sorter(boolean sortDesc, boolean typeInt) {
        this.lines = new ArrayList<>();
        this.sortDesc = sortDesc;
        this.typeInt = typeInt;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void process() {
        //nums = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        sort(0, lines.size() - 1, lines);
        System.out.println(lines);
    }

    private void sort(int first, int last, List<String> arr) {
        int half = 0;
        if (first < last) {
            half = (first + last) / 2; // Делим массив пополам, сортируем каждую половину
            sort(first, half, arr);
            sort(half + 1, last, arr);
            merge(first, half, last, arr); // Делаем слияние отсортированных половин
        }
    }

    private void merge(int first, int half, int last, List<String> input) {
        List<String> merged = new ArrayList<>(); // массив слияния
        for(int i = 0; i < last - first + 1; i++) { // инициализация массива
            merged.add("");
        }
        int i = first;
        int j = half + 1;
        boolean iPassed = false;
        boolean jPassed = false;

        for (int taken = 0; taken < merged.size(); taken++) { // число шагов == размер массива слияния
            // При финише j курсора, массив слияния дополняем остатками 1го массива
            if (jPassed) {
                merged.set(taken, input.get(i));
                if (i < half) i++;
            }
            // При финише i курсора, массив слияния дополняем остатками 2го массива
            if (iPassed) {
                merged.set(taken, input.get(j));
                if (j < last) j++;
            }
            // Идём курсорами i и j по каждому массиву, берём наименьшие значения на каждом шаге
            if (compareWithProperties(input.get(i), input.get(j)) && !iPassed) {
                merged.set(taken, input.get(i));
                if (i < half) {
                    i++;
                } else {
                    iPassed = true;
                }
            } else if (!compareWithProperties(input.get(i), input.get(j)) && !jPassed) {
                merged.set(taken, input.get(j));
                if (j < last) {
                    j++;
                } else {
                    jPassed = true;
                }
            }
        }
        // Помещаем массив слияния в исходный массив
        for (int k = 0; k < merged.size(); k++) {
            input.set(first + k, merged.get(k));
        }
    }

    private boolean compareWithProperties(String line1, String line2) {
        if (Integer.parseInt(line1) < Integer.parseInt(line2) && typeInt) {
            return true;
        } else if (line1.compareTo(line2) < 0 && !typeInt) {
            return true;
        } else {
            return false;
        }
    }
}
