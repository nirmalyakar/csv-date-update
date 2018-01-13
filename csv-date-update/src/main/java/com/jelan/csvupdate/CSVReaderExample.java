package com.jelan.csvupdate;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class CSVReaderExample {

    public static void main(String[] args) {

        String csvFile = "C:\\Users\\Nirmalya\\Desktop\\DBAdminRequirement\\config.csv";
        Map<String, List<String>> thingsToProcess=new HashedMap();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile),',','\'',1 );
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
            processCSVWithThisOrder(thingsToProcess);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

	private static void processCSVWithThisOrder(Map<String, List<String>> thingsToProcess) {
		for (Map.Entry<String, List<String>> entry : thingsToProcess.entrySet()) {
			System.out.println(entry.getKey());
			for(int i=0;i<entry.getValue().size();i++){
				System.out.println("---"+entry.getValue().get(i));
			}
		}
		
	}

}