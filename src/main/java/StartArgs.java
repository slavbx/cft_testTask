import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.util.List;

public class StartArgs {
    @Parameter(names = "-a", description = "Режим сортировки: по возрастанию")
    private boolean sortAsc;

    @Parameter(names = "-d", description = "Режим сортировки: по убыванию")
    private boolean sortDesc;

    @Parameter(names = "-s", description = "Тип данных: строковый")
    private boolean typeStr;

    @Parameter(names = "-i", description = "Тип данных: целочисленный")
    private boolean typeInt;

    @Parameter(description = "Имя выходного файла, Имена входных файлов", required = true, variableArity = true)
    private List<String> fileNames;

    public boolean isSortAsc() {
        return sortAsc;
    }

    public boolean isSortDesc() {
        return sortDesc;
    }

    public boolean isTypeStr() {
        return typeStr;
    }

    public boolean isTypeInt() {
        return typeInt;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void run() {
        if (sortAsc && sortDesc) throw new ParameterException("Укажите параметр сортировки a или d, либо ничего");
        if (!typeInt && !typeStr) throw new ParameterException("Укажите параметр типа данных s или i");
        if (typeInt && typeStr) throw new ParameterException("Укажите параметр типа данных s или i, но не оба");
        if (fileNames.size() < 2) throw new ParameterException("Укажите хотя бы одно имя входного файла");
    }
}
