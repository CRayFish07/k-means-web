package pl.peek.kmeans;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import pl.peek.kmeans.impl.Point;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsPointsImporter {

    public static List<Point> convert(InputStream o) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(o);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<Point> points = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            Point point = null;
            Double xValue = null;
            Double yValue = null;

            int c = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Double dbl;
                try {
                    dbl = Double.parseDouble(cell.getStringCellValue());
                } catch (Exception e) {
                    dbl = null;
                }
                if (dbl != null) {
                    if (c == 0) {
                        xValue = dbl;
                        c++;
                    } else if (c == 1) {
                        yValue = dbl;
                        c++;
                    }
                }
                if (xValue != null && yValue != null)
                    point = new Point(xValue, yValue);
            }
            if (point != null) points.add(point);
        }
        return points;
    }

}
