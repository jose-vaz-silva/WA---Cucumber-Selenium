package com.web.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetManager {

	private File spreadsheetFile;
	private Workbook workbook;
	private static Sheet sheet;

	public SpreadsheetManager(String spreadsheetPath) {
		this.spreadsheetFile = new File(spreadsheetPath);
		try {
			setWorkbook();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setWorkbook() throws FileNotFoundException, IOException {
		this.workbook = new XSSFWorkbook(new FileInputStream(spreadsheetFile));
	}

	public List<String> getColumnNames(String sheetName) {
		sheet = this.workbook.getSheet(sheetName);
		List<String> columnNameList = new LinkedList<String>();
		int minCollx = sheet.getRow(0).getFirstCellNum();
		int maxCollx = sheet.getRow(0).getLastCellNum();
		while (minCollx < maxCollx) {
			String cellValue = "";
			try {
				cellValue = sheet.getRow(0).getCell(minCollx).getStringCellValue();
				columnNameList.add(cellValue);
			} catch (NullPointerException e) {
				columnNameList.add("");
			}
			minCollx++;
		}
		return columnNameList;
	}

	public int getColumnIndex(String columnName, String sheetName) {
		sheet = this.workbook.getSheet(sheetName);
		List<String> columnNameList = getColumnNames(sheet.getSheetName());
		return columnNameList.indexOf(columnName);
	}

	/**
	 * Write value in spreadsheet
	 * 
	 * @param sheetName  -> tab name
	 * @param rowNumber  -> row number, start in 0
	 * @param columnName -> column name in line 0;
	 * @param value      -> value to write
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void write(String sheetName, int rowNumber, String columnName, String value)
			throws FileNotFoundException, IOException {
		sheet = this.workbook.getSheet(sheetName);
		Row row = null;
		Cell cell = null;
		try {
			row = sheet.getRow(rowNumber);
			cell = row.createCell(getColumnIndex(columnName, sheet.getSheetName()));
		} catch (Exception e) {
			row = sheet.createRow(rowNumber);
			cell = row.createCell(getColumnIndex(columnName, sheet.getSheetName()));
		}
		cell.setCellValue(value);
		workbook.write(new FileOutputStream(spreadsheetFile));
		workbook.close();
		setWorkbook();
	}

	public void writeAndCreateColumn(String sheetName, int rowNumber, String columnName, String value)
			throws FileNotFoundException, IOException {
		sheet = this.workbook.getSheet(sheetName);
		Row row = null;
		Cell cell = null;
		if (getColumnIndex(columnName, sheet.getSheetName()) < 0) {
			row = sheet.getRow(0);
			cell = row.createCell(getColumnNames(sheet.getSheetName()).size());
			cell.setCellStyle(row.createCell(getColumnNames(sheet.getSheetName()).size() - 1).getCellStyle());
			cell.setCellValue(value);
			workbook.write(new FileOutputStream(spreadsheetFile));
			workbook.close();
			setWorkbook();
		}
		try {
			row = sheet.getRow(rowNumber);
			cell = row.createCell(getColumnIndex(columnName, sheet.getSheetName()));
		} catch (Exception e) {
			row = sheet.createRow(rowNumber);
			cell = row.createCell(getColumnIndex(columnName, sheet.getSheetName()));
		}
		cell.setCellValue(value);
		workbook.write(new FileOutputStream(spreadsheetFile));
		workbook.close();
		setWorkbook();
	}

	/**
	 * Write value in spreadsheet
	 * 
	 * @param sheetName  -> tab name
	 * @param rowNumber  -> row number, start in 0
	 * @param columnName -> column index, start in 0
	 * @param value      -> value to write
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void write(String sheetName, int rowNumber, int columnIndex, String value)
			throws FileNotFoundException, IOException {
		sheet = this.workbook.getSheet(sheetName);
		Row row = null;
		Cell cell = null;
		try {
			row = sheet.getRow(rowNumber);
			cell = row.createCell(columnIndex);
		} catch (Exception e) {
			row = sheet.createRow(rowNumber);
			cell = row.createCell(columnIndex);
		}
		cell.setCellValue(value);
		workbook.write(new FileOutputStream(spreadsheetFile));
		workbook.close();
		setWorkbook();
	}

	public void write(String sheetName, int rowNumber, int columnIndex, String value, boolean save)
			throws FileNotFoundException, IOException {
		sheet = this.workbook.getSheet(sheetName);
		Row row = null;
		Cell cell = null;
		try {
			row = sheet.getRow(rowNumber);
			cell = row.createCell(columnIndex);
		} catch (Exception e) {
			row = sheet.createRow(rowNumber);
			cell = row.createCell(columnIndex);
		}
		cell.setCellValue(value);
		if (save) {
			workbook.write(new FileOutputStream(spreadsheetFile));
			workbook.close();
			setWorkbook();
		}
	}

	public String getCellValue(String sheetName, int rowNumber, String columnName) throws Exception {
		sheet = this.workbook.getSheet(sheetName);
		try {
			Row row = sheet.getRow(rowNumber);
			Cell cell = row.getCell(getColumnIndex(columnName, sheet.getSheetName()));
			String cellValue = null;
			switch (cell.getCellType()) {
			case FORMULA:
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellFormulaValue = evaluator.evaluate(cell);
				if (cellFormulaValue.getCellType() == CellType.NUMERIC) {
					if (DateUtil.isCellDateFormatted(cell)) {
						cellValue = cell.getDateCellValue().toString();
					} else {
						cellValue = String.valueOf(cellFormulaValue.getNumberValue());
					}
				} else {
					cellValue = cellFormulaValue.getStringValue();
				}
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = cell.getDateCellValue().toString();
				} else {
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			default:
				cellValue = cell.getStringCellValue();
			}
			return cellValue;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public String getCellValue(String sheetName, int rowNumber, int columnIndex) throws Exception {
		sheet = this.workbook.getSheet(sheetName);
		try {
			String cellValue = "";
			Row row = sheet.getRow(rowNumber);
			if (row != null) {
				Cell cell = row.getCell(columnIndex);
				if (cell != null) {
					switch (cell.getCellType()) {
					case FORMULA:
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						CellValue cellFormulaValue = evaluator.evaluate(cell);
						if (cellFormulaValue.getCellType() == CellType.NUMERIC) {
							if (DateUtil.isCellDateFormatted(cell)) {
								cellValue = cell.getDateCellValue().toString();
							} else {
								cellValue = String.valueOf(cellFormulaValue.getNumberValue());
							}
						} else {
							cellValue = cellFormulaValue.getStringValue();
							if (cellValue == null) {
								cellValue = cell.getCellFormula();
							}
						}
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							cellValue = cell.getDateCellValue().toString();
						} else {
							cellValue = String.valueOf(cell.getNumericCellValue());
						}
						break;
					case BOOLEAN:
						cellValue = String.valueOf(cell.getBooleanCellValue());
						break;
					default:
						cellValue = cell.getStringCellValue();
					}
				}
			}
			return cellValue;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public LinkedList<String> getAllCellsValueByColumn(String sheetName, String columnName) throws Exception {
		sheet = this.workbook.getSheet(sheetName);
		int startRowIndex = sheet.getFirstRowNum();
		int maxRow = sheet.getPhysicalNumberOfRows();
		int currentRow = startRowIndex;
		LinkedList<String> cellValues = new LinkedList<String>();
		while (currentRow < maxRow) {
			cellValues.add(getCellValue(sheetName, currentRow, getColumnIndex(columnName, sheetName)));
			currentRow++;
		}
		return cellValues;
	}

	public LinkedList<String> getAllCellsValueByColumn(String sheetName, int columnIndex) throws Exception {
		sheet = this.workbook.getSheet(sheetName);
		int startRowIndex = sheet.getFirstRowNum();
		int maxRow = sheet.getPhysicalNumberOfRows();
		int currentRow = startRowIndex;
		LinkedList<String> cellValues = new LinkedList<String>();
		while (currentRow < maxRow) {
			cellValues.add(getCellValue(sheetName, currentRow, columnIndex));
			currentRow++;
		}
		return cellValues;
	}

	public void removeSheet(String sheetName) throws FileNotFoundException, IOException {
		sheet = this.workbook.getSheet(sheetName);
		workbook.removeSheetAt(workbook.getSheetIndex(sheet));
	}

	public void removeColumn(String sheetName, String columnName) {
		sheet = workbook.getSheet(sheetName);
		int rowNumbers = sheet.getPhysicalNumberOfRows();
		int currentRow = 0;
		int columnIndex = getColumnIndex(columnName, sheetName);
		if (columnIndex != -1) {
			while (currentRow < rowNumbers) {
				Row row = sheet.getRow(currentRow);
				try {
					Cell cell = row.getCell(columnIndex);
					row.removeCell(cell);
				} catch (NullPointerException e) {
				}
				currentRow++;
			}
		}
	}

	public void removeColumns(String sheetName, List<String> columnNames) throws Exception {
		sheet = workbook.getSheet(sheetName);
		int rowNumbers = sheet.getPhysicalNumberOfRows();
		for (String columnName : columnNames) {
			int currentRow = 0;
			int columnIndex = getColumnIndex(columnName, sheetName);
			if (columnIndex != -1) {
				while (currentRow < rowNumbers) {
					if (!(sheet.getRow(currentRow) == null)) {
						Row row = sheet.getRow(currentRow);
						Cell cell = row.getCell(columnIndex);
						if (!(cell == null)) {
							row.removeCell(cell);
						}
					}
					currentRow++;
				}
			}
		}
		save();
		List<String> newColumnNames = getColumnNames(sheetName);
		int firstColumnBlankIndex = 0;
		int firstColumnFilledIndex = 0;
		int lastColumnFilledIndex = 0;
		for (String columnName : newColumnNames) {
			if (columnName.equals("")) {
				firstColumnBlankIndex = newColumnNames.indexOf(columnName);
				break;
			}
		}
		int index = firstColumnBlankIndex;
		while (index < newColumnNames.size()) {
			if (!newColumnNames.get(index).equals("")) {
				firstColumnFilledIndex = index;
				break;
			}
			index++;
		}
		index = firstColumnFilledIndex;
		while (index < newColumnNames.size()) {
			if (newColumnNames.get(index).equals("")) {
				lastColumnFilledIndex = (index - 1);
				break;
			} else if (index == (newColumnNames.size() - 1) && !(newColumnNames.get(index).equals(""))) {
				lastColumnFilledIndex = (index);
			}
			index++;
		}
		int shiftNumber = firstColumnBlankIndex - firstColumnFilledIndex;
		if (shiftNumber < 0) {
			shiftColumn(sheetName, firstColumnFilledIndex, lastColumnFilledIndex, shiftNumber);
		}
	}

	public void shiftColumn(String sheetName, int columnIndexStart, int columnIndexEnd, int shiftNumber) {
		sheet = workbook.getSheet(sheetName);
		sheet.shiftColumns(columnIndexStart, columnIndexEnd, shiftNumber);
	}

	public void shiftColumns(String sheetName, String columnNameStart, String columnNameEnd, int targetColumn) {
		int columnStartIndex = getColumnIndex(columnNameStart, sheetName);
		int shiftNumber = targetColumn - columnStartIndex;
		sheet = workbook.getSheet(sheetName);
		sheet.shiftColumns(columnStartIndex, getColumnIndex(columnNameEnd, sheetName), shiftNumber);
	}

	public void shiftColumn(String sheetName, String columnName, int targetColumn) {
		int columnNameIndex = getColumnIndex(columnName, sheetName);
		int shiftNumber = targetColumn - columnNameIndex;
		sheet = workbook.getSheet(sheetName);
		sheet.shiftColumns(columnNameIndex, columnNameIndex, shiftNumber);
	}

	public List<List<String>> getAllColumnValues(String sheetName) throws Exception {
		sheet = workbook.getSheet(sheetName);
		List<String> columnNames = getColumnNames(sheetName);
		List<List<String>> columnValues = new LinkedList<List<String>>();
		for (String columnName : columnNames) {
			if (!columnName.equals("")) {
				columnValues.add(getAllCellsValueByColumn(sheetName, columnName));
			}
		}
		return columnValues;
	}

	public void writeLists(String sheetName, List<List<String>> columnValuesLists)
			throws FileNotFoundException, IOException {
		sheet = workbook.getSheet(sheetName);
		cleanSheet(sheetName);
		StringBuilder columnValueStrBuilder = new StringBuilder();
		columnValuesLists.get(0).get(0);
		int currentListIndex = 0;
		int MaxlistSize = 0;
		for (List<String> columnValues : columnValuesLists) {
			if (MaxlistSize < columnValues.size()) {
				MaxlistSize = columnValues.size();
			}
		}
		while (currentListIndex < MaxlistSize) {
			for (List<String> columnValues : columnValuesLists) {
				if (!(currentListIndex > columnValues.size())) {
					columnValueStrBuilder.append(columnValues.get(currentListIndex) + "\t");
				} else {
					columnValueStrBuilder.append(" \t");
				}
			}
			columnValueStrBuilder.append("\n");
			currentListIndex++;
		}
		write(sheetName, 0, 0, columnValueStrBuilder.toString());
	}

	public void cleanSheet(String sheetName) {
		sheet = workbook.getSheet(sheetName);
		int rowNumbers = sheet.getPhysicalNumberOfRows();
		int rowIndex = 0;
		while (rowIndex < rowNumbers) {
			Row row = sheet.getRow(rowIndex);
			sheet.removeRow(row);
			rowIndex++;
		}
	}

	public void save() throws FileNotFoundException, IOException {
		workbook.write(new FileOutputStream(spreadsheetFile));
		workbook.close();
		setWorkbook();
	}

	public void saveToNewSpreadsheet(String path) throws FileNotFoundException, IOException {
		workbook.write(new FileOutputStream(new File(path)));
		workbook.close();
		this.workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
	}

	public String getSheetName(int index) {
		sheet = this.workbook.getSheetAt(index);
		return sheet.getSheetName();
	}

	public List<String> getAlSheetNames() {
		int sheetNumber = workbook.getNumberOfSheets();
		List<String> sheetNames = new LinkedList<String>();
		int sheetIndex = 0;
		while (sheetIndex < sheetNumber) {
			sheet = this.workbook.getSheetAt(sheetIndex);
			sheetNames.add(sheet.getSheetName());
			sheetIndex++;
		}
		return sheetNames;
	}

	public void writeExcelFormatString(String excelFormatString) {

	}

	public int getPhysicalNumberOfRows(String sheetName) {
		sheet = workbook.getSheet(sheetName);
		return sheet.getPhysicalNumberOfRows();
	}

	public int getPhysicalNumberOfColumns(String sheetName) {
		sheet = workbook.getSheet(sheetName);
		return sheet.getRow(0).getPhysicalNumberOfCells();
	}

	public int getNumbersOfSheets() {
		return workbook.getNumberOfSheets();
	}

	/*
	 * @metodo: setData
	 * 
	 * @descricao: Inserir valor da coluna x execu��o
	 * 
	 * @param: colummName ( Nome da Coluna ), value ( valor )
	 * 
	 */
	public void setData(String columnName, String value) {

		try {
			int colNum = getColumnIndex(columnName, sheet.getSheetName());
			write(sheet.getSheetName(), 0, colNum, value);
		} catch (Exception e) {
			System.out.println("Falha ao escrever na planilha!");
			System.out.println(e.getMessage());
		}
	}

	/*
	 * @metodo: getValue
	 * 
	 * @descricao: Capturar valor da coluna x execu��o
	 * 
	 * @param: colummName ( Nome da Coluna )
	 * 
	 */
	public String getData(String colummName) throws Exception {

		String value = getCellValue(sheet.getSheetName(), 0, getColumnIndex(colummName, sheet.getSheetName()));
		if (value != "") {
			return value;
		}
		return "Coluna nao localizada:" + colummName;
	}
}