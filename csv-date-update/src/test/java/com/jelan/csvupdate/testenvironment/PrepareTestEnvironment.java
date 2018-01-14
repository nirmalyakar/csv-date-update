package com.jelan.csvupdate.testenvironment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.collections.map.HashedMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class PrepareTestEnvironment {

	public static void main(String[] args) {
		
		String configFileLocation = "TestEnvironment//config.csv";
		Map<String, List<String>> filesToCreate = new HashedMap();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(configFileLocation), ',', '\'', 1);
			String[] line;
			while ((line = reader.readNext()) != null) {
				List<String> columns;
				if (filesToCreate.containsKey(line[0])) {
					filesToCreate.get(line[0]).add(line[1]+","+line[2]);
				} else {
					columns = new ArrayList<String>();
					columns.add(line[1]+","+line[2]);
					filesToCreate.put(line[0], columns);
				}
			}
			prepareFilesAsPerThisConfig(filesToCreate);
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	private static void prepareFilesAsPerThisConfig(Map<String, List<String>> filesToCreate) {
		for(Map.Entry<String, List<String>> entry: filesToCreate.entrySet()){

			List<String[]> csvContent = prepareCSVdata(entry.getValue());
			
			CSVWriter csvWriter=null;
			try {
				csvWriter=new CSVWriter(new FileWriter(new File("TestEnvironment//"+entry.getKey())));
				csvWriter.writeAll(csvContent);
				csvWriter.flush();
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<String[]> prepareCSVdata(List<String> list) {
		List<String[]> csvContent=new ArrayList<>();
		for(int i=0;i<50;i++){
			String[] rowContent={"asdf","asdf","asdf"};
			csvContent.add(rowContent);
		}
		return csvContent;
	}

}
