package com.web.automation.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorksheetManager {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private HashMap<String, Integer> headers = new HashMap<>();
  private FileInputStream fis;
  private String worksheetPath;
  
  public WorksheetManager(String worksheetPath) throws Exception {
    this.worksheetPath = worksheetPath;
    fis = new FileInputStream(worksheetPath);
    workbook = new XSSFWorkbook(fis);
    sheet = workbook.getSheetAt(0);
    loadHeaders();
  }
  
  public WorksheetManager(String worksheetPath, String sheetName) throws Exception {
    this.worksheetPath = worksheetPath;
    fis = new FileInputStream(worksheetPath);
    workbook = new XSSFWorkbook(fis);
    sheet = workbook.getSheet(sheetName);
    loadHeaders();
  }
  
  private void loadHeaders() {
    XSSFRow firstRow = sheet.getRow(0);
    for (int i = 0; i < firstRow.getLastCellNum(); i++) {
      headers.put(String.valueOf(firstRow.getCell(i)), i);
    }
  }
  
  public String getValue(String columnName, Integer rowIndex) {
    XSSFRow row = sheet.getRow(rowIndex);
    return String.valueOf(row.getCell(headers.get(columnName)));
  }
  
  public void setValue(String columnName, Integer rowIndex, String value) {
    XSSFRow row;
    if (sheet.getRow(rowIndex) == null)
      row = sheet.createRow(rowIndex);
    else
      row = sheet.getRow(rowIndex);
    Cell cell = row.createCell(headers.get(columnName));
    cell.setCellValue(value);
  }
  
  public Integer getRowsCount() {
    return sheet.getLastRowNum() + 1;
  }
  
  public void save() throws IOException {
    fis.close();
    FileOutputStream fos = new FileOutputStream(worksheetPath);
    workbook.write(fos);
    fos.close();
  }
  
  public void save(String newPath) throws IOException {
    fis.close();
    FileOutputStream fos = new FileOutputStream(newPath);
    workbook.write(fos);
    fos.close();
  }
  
  public Boolean isRowEmpty(Integer rowIndex) {
    XSSFRow row = sheet.getRow(rowIndex);
    List<Boolean> emptyCell = new ArrayList<>();
    for (int i = 0; i < row.getLastCellNum(); i++) {
      emptyCell.add(String.valueOf(row.getCell(i)).equals(""));
    }
    return !emptyCell.contains(false);
  }
}
