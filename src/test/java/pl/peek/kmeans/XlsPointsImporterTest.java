package pl.peek.kmeans;

import com.google.common.io.Resources;
import org.junit.Test;
import pl.peek.kmeans.impl.Point;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class XlsPointsImporterTest {

    @Test
    public void testConvertingTaskFile() throws Exception {
        File xlsFile = new File(Resources.getResource("zadanie_java_algorytm_input.xls").getFile());
        List<Point> points = XlsPointsImporter.convert(new FileInputStream(xlsFile));
        assertTrue(points.size() == 100);
    }
}