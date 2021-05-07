package excelgen;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportExcel {
    private String outputFilePath;
    private boolean dateExist;
    private int dateLineModifier;

    public ExportExcel(String filePath, boolean dateExist) {
        this.outputFilePath = filePath;
        this.dateExist = dateExist;

        this.dateLineModifier = (dateExist) ? 1 : 0;
    }

    public void export(String[][] content, String databaseMods) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("output");

        IndexedColors[] tunnelColors = {IndexedColors.GOLD, IndexedColors.CORAL, IndexedColors.LIGHT_CORNFLOWER_BLUE};

        IndexedColors[] colors = {IndexedColors.LAVENDER, IndexedColors.LIGHT_GREEN, IndexedColors.LIGHT_ORANGE,
                IndexedColors.LIGHT_TURQUOISE, IndexedColors.LIME, IndexedColors.AQUA,
                IndexedColors.SKY_BLUE, IndexedColors.ROSE, IndexedColors.TAN};


        for (int i = 0; i < content.length; i++) {
            Row row = sheet.createRow(i);

            //TUNNEL HEADER
            if ((i-dateLineModifier) % 10 == 0) {

                //Make Cell Style Border + Align Centre
                XSSFCellStyle cellStyle = wb.createCellStyle();
                addCellStyle_AlignCentre(cellStyle);
                addCellStyle_BorderAll(cellStyle);
                int tunnelColorFormula = (short) (i/10) % tunnelColors.length;
                addCellStyle_BGColor(cellStyle, tunnelColors[tunnelColorFormula].getIndex());

                for (int j = 1; j <= 6; j++) {
                    Cell cell = row.createCell(j);
                    if (j == 1) {
                        cell.setCellValue(content[i][3]);
                    }
                    cell.setCellStyle(cellStyle);
                }

                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        i, i, 1, 6);

                sheet.addMergedRegion(cellRangeAddress);

                continue;
            }

            for (int j = 0; j < content[i].length; j++) {
                Cell cell = row.createCell(j);

                //Set Cell Format as Number if it is floor
                if (j % 2 == 0 && tryParseInt(content[i][j])) {
                    cell.setCellValue(Integer.parseInt(content[i][j]));
                } else {
                    //Name type
                    cell.setCellValue(content[i][j]);

                    if (!content[i][j].startsWith("Team") && j % 2 == 1 && i != 0) { //todo: set date as compulsory
                        XSSFCellStyle cellStyle = wb.createCellStyle();
                        addCellStyle_BorderAll(cellStyle);
                        short colorFormula = (short) (((i / 10)*3 + (j / 2)) % colors.length);
                        addCellStyle_BGColor(cellStyle, colors[colorFormula].getIndex());
                        cell.setCellStyle(cellStyle);
                    } else {

                    }
                }
            }

            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(5);
        }

        //Add database mods
        //Append the database modification beside tunnel 1
        String[] split = databaseMods.split("\n");
        for (int i = 1; i <= split.length; i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            Cell cell = row.createCell(8);
            cell.setCellValue(split[i-1]);
        }

        FileOutputStream output = null;
        try {
            File file = new File(outputFilePath);
            file.getParentFile().mkdirs();
            file.createNewFile();
            output = new FileOutputStream(file);
            wb.write(output);
            output.close();
        } catch (IOException e) {
            System.err.println("Unable to write to excel file");
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException | NullPointerException e) { }
        }
    }

    private boolean tryParseInt(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void addCellStyle_BorderAll(XSSFCellStyle cellStyle) {
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
    }

    private void addCellStyle_AlignCentre(XSSFCellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    private void addCellStyle_BGColor(XSSFCellStyle cellStyle, short color) {
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(color);
    }
}
