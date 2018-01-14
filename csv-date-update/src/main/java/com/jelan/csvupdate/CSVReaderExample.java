package com.jelan.csvupdate;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVReaderExample {
	private final String WORKINGLOCATION;
	private final int DAYSTOINCREASE;
	public CSVReaderExample(String workinglocation,int daysToIncrease) {
		super();
		this.WORKINGLOCATION = workinglocation;
		this.DAYSTOINCREASE = daysToIncrease;
	}

	private void processCSVWithThisOrder(Map<String, List<String>> thingsToProcess) throws ParseException {
		for (Map.Entry<String, List<String>> entry : thingsToProcess.entrySet()) {
			String currentCsvFileToProcess = WORKINGLOCATION + "\\" + entry.getKey() + ".csv";
			CSVReader csvReader;
			try {
				csvReader = new CSVReader(new FileReader(currentCsvFileToProcess));
				List<String[]> csvBody = csvReader.readAll();
				String[] heading = csvBody.get(0);// Expecting first line as heading
				Map<String, Integer> columnNameToIndexMap = createColumnNameToIndexMapFormTheseHeadings(heading);
				for (int i = 1; i < csvBody.size(); i++) {
					for (int columnNameCounter = 0; columnNameCounter < entry.getValue().size(); columnNameCounter++) {
						csvBody.get(i)[columnNameToIndexMap.get(entry.getValue().get(columnNameCounter))] = datetoUpdate(csvBody.get(i)[columnNameToIndexMap.get(entry.getValue().get(columnNameCounter))]);
					}
				}
				csvReader.close();
				CSVWriter csvWriter = new CSVWriter(new FileWriter(currentCsvFileToProcess));
				csvWriter.writeAll(csvBody);
				csvWriter.flush();
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private String datetoUpdate(String olddateinString) throws ParseException {
		DateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy");
		Date olddate=dateFormat.parse(olddateinString);
		Calendar c =Calendar.getInstance();
		c.setTime(olddate);
		c.add(Calendar.DATE,DAYSTOINCREASE);
		Date newDate=c.getTime();
		System.out.println(newDate);
		String newDateInString=dateFormat.format(newDate);
		return newDateInString;
	}


	private Map<String, Integer> createColumnNameToIndexMapFormTheseHeadings(String[] heading) {
		Map<String, Integer> columnNameToIndexMap = new HashedMap();
		for (int i = 0; i < heading.length; i++) {
			columnNameToIndexMap.put(heading[i], i);
		}
		return columnNameToIndexMap;
	}

	public static void main(String[] args) {
		CSVReaderExample csvReaderExample = null;
		try {
			csvReaderExample = new CSVReaderExample(args[0],Integer.parseInt(args[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String csvFile = csvReaderExample.WORKINGLOCATION + "\\config.csv";
		Map<String, List<String>> thingsToProcess = new HashedMap();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile), ',', '\'', 1);
			String[] line;
			while ((line = reader.readNext()) != null) {
				List<String> columns;
				if (line[4].toLowerCase().equalsIgnoreCase("yes")) {
					if (thingsToProcess.containsKey(line[0])) {
						thingsToProcess.get(line[0]).add(line[1]);
					} else {
						columns = new ArrayList<String>();
						columns.add(line[1]);
						thingsToProcess.put(line[0], columns);
					}
				}
			}
			csvReaderExample.processCSVWithThisOrder(thingsToProcess);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}