import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class GstCalculatorTest {
    @Test
    public void testCalculate() throws Exception{
        ArrayList<DataRow> dataRows = readRow();
        for(int i = 0; i< dataRows.size(); i++){
            DataRow dataRow = dataRows.get(i);
            try {
                Assert.assertTrue(new GstCalculator().calculate(dataRow.hsn, dataRow.amount).doubleValue() == dataRow.expectedTax.doubleValue());
            }
            catch (GstCalculator.HsnCodeNotFoundException e){
                Assert.assertTrue(e.getMessage().equals("HSN Code "+dataRow.hsn+" is not a valid HSN Code"));
            }
        }
    }

    public ArrayList<DataRow> readRow() throws IOException {
        File file = new File("data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        Iterator<Row> rowIterator = xssfSheet.rowIterator();
        ArrayList<DataRow> dataRows = new ArrayList<DataRow>();
        rowIterator.next();
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            DataRow dataRow = new DataRow();
            dataRow.id = (int)cellIterator.next().getNumericCellValue();
            dataRow.hsn = "0"+cellIterator.next().getNumericCellValue();
            dataRow.amount = cellIterator.next().getNumericCellValue();
            Cell cell = cellIterator.next();
            if (cell.getCellType() == CellType.STRING){
                dataRow.expectedTax = -1.0;
            }
            else {
                dataRow.expectedTax = cell.getNumericCellValue();
            }
            dataRows.add(dataRow);
        }
        return dataRows;
    }

    private static class DataRow{
        Integer id;
        String hsn;
        Double amount;
        Double expectedTax;
    }
}
