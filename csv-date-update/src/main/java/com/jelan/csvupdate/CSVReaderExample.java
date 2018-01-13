package com.jelan.csvupdate;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVReaderExample {
	public CSVReaderExample(String workinglocation) {
		super();
		this.WORKINGLOCATION=workinglocation;
	}
	private final String WORKINGLOCATION;
	
	private void processCSVWithThisOrder(Map<String, List<String>> thingsToProcess) {
		for (Map.Entry<String, List<String>> entry : thingsToProcess.entrySet()) {
			String currentCsvFileToProcess=WORKINGLOCATION+"\\"+entry.getKey()+".csv";
			CSVReader csvReader;
			try {
				csvReader=new CSVReader(new FileReader(currentCsvFileToProcess));
				List<String[]> csvBody=csvReader.readAll();
				String[] heading=csvBody.get(0);//Expecting first line as heading
				Map<String, Integer> columnNameToIndexMap=createColumnNameToIndexMapFormTheseHeadings(heading);
				for(int i=1;i<csvBody.size();i++){
					for (int columnNameCounter = 0; columnNameCounter < entry.getValue().size(); columnNameCounter++) {
						//System.out.println(csvBody.get(i)[columnNameToIndexMap.get("File 2 Field4")]);
						csvBody.get(i)[columnNameToIndexMap.get(entry.getValue().get(columnNameCounter))]="changedText";
					}
				}
				csvReader.close();
				CSVWriter csvWriter=new CSVWriter(new FileWriter(currentCsvFileToProcess));
				csvWriter.writeAll(csvBody);
				csvWriter.flush();
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
    private Map<String, Integer> createColumnNameToIndexMapFormTheseHeadings(String[] heading) {
    	Map<String, Integer> columnNameToIndexMap=new HashedMap();
    	for (int i=0;i<heading.length;i++) {
			columnNameToIndexMap.put(heading[i], i);
		}
		return columnNameToIndexMap;
	}
	public static void main(String[] args) {
    	CSVReaderExample csvReaderExample=new CSVReaderExample(args[0]);
    	try{
    		csvReaderExample=new CSVReaderExample(args[0]);
    	}catch (Exception e) {
    		e.printStackTrace();
		}
        String csvFile = csvReaderExample.WORKINGLOCATION+"\\config.csv";
        Map<String, List<String>> thingsToProcess=new HashedMap();  
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile),',','\'',1);
            String[] line;
            while ((line = reader.readNext()) != null) {
            	List<String> columns;
            	if(line[4].toLowerCase().equalsIgnoreCase("yes")){
            		if(thingsToProcess.containsKey(line[0])){
            			thingsToProcess.get(line[0]).add(line[1]);
            		}else{
            			columns=new ArrayList<String>();
            			columns.add(line[1]);
            			thingsToProcess.put(line[0], columns);
            		}
            	}
            }
            csvReaderExample.processCSVWithThisOrder(thingsToProcess);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}