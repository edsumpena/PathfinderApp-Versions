package App.EditParameters;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
// TODO find a better way to do this
public class ParameterScannerLocal {

    final static String absolutePath = "C:/Users/Joshua/Documents/ftc_clueless_rr/TeamCode/";
    final static String annotation = "@RobotParam";

    public static List<Parameter> scan() {
        List<Parameter> parameterList = new ArrayList<>();
        Collection<File> files = FileUtils.listFiles(new File(absolutePath), new String[]{"java"}, true);
        for (File f : files) {
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(f.getAbsolutePath()));
            }
            catch (IOException e) {
                System.out.println("Could not read " + f.getAbsolutePath());
                continue;
            }

            boolean flag = false;
            for (String line : lines) {
                if (line.contains(annotation)) {
                    flag = true;
                    continue;
                }

                if (flag) {
                    String type = line.split(" ")[0];
                    String name = line.split(" ")[1].replace(";", "");

                    parameterList.add(new Parameter(ParameterType.valueOf(type), name, "", null));
                    flag = false;
                }
            }
        }
        return parameterList;
    }
}
