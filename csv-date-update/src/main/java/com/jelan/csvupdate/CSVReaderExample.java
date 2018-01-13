package com.jelan.csvupdate;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.opencsv.CSVReader;

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
				String[] heading=csvReader.readNext();
				Map<String, Integer> columnNameToIndexMap=createColumnNameToIndexMapFormTheseHeadings(heading);
				String[] line;
				while((line=csvReader.readNext())!=null){
					for(int i=0;i<entry.getValue().size();i++){
						System.out.println("---"+entry.getValue().get(i));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(entry.getKey());
			for(int i=0;i<entry.getValue().size();i++){
				System.out.println("---"+entry.getValue().get(i));
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
        String csvFile = "C:\\Users\\Nirmalya\\Desktop\\DBAdminRequirement\\config.csv";
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