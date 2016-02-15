package com.wordcount.bo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public class WordCountBusiness {

   //read file and create map <String,count> 
	public static Map<String, Integer> readFileAndCreateWordMap(String fileName) {
		Map<String, Integer> wordMap = new HashMap<>();
		try (FileInputStream fis = new FileInputStream(fileName);
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						dis))) {

			Pattern pattern = Pattern.compile("\\s+");
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] words = pattern.split(line);
				for (String word : words) {
					if (wordMap.containsKey(word)) {
						wordMap.put(word, (wordMap.get(word) + 1));
					} else {
						wordMap.put(word, 1);
					}
				}
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		return wordMap;
	}
	//sorting decreasing order
	public static List<Entry<String, Integer>> sortByValueInDecreasingOrder(
			Map<String, Integer> wordMap, Integer count) {
		Set<Entry<String, Integer>> entries = wordMap.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<>(entries);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		List<Entry<String, Integer>> resultList = new ArrayList<>();
		for (int i = 0; i < count && count != 0; i++) {
			resultList.add(list.get(i));
		}
		if (count == 0) {
			resultList = list;
		}
		return resultList;
	}
	//get count of input string
	public static String getCountOfWords(Map<String, Integer> wordMap,
			String input) {
		Map<String, Integer> resultMap = new HashMap<>();
		if(null!=input){
		input = input.replace("[", "").replace("]", "").replace("\"", "");//convert json to string
		}
		String[] splitString = input.split(",");//convert String to stringArray
		for (int i = 0; i < splitString.length; i++) {
			if (null != wordMap.get(splitString[i])) {
				resultMap.put(splitString[i], wordMap.get(splitString[i]));
			} else {
				resultMap.put(splitString[i], 0);
			}

		}
		return resultMap.toString();
	}
	//convert to csv file
	 public static void generateCsvFile( List<Entry<String, Integer>> sortByValueInDecreasingOrder, String fileName)
	   {
		try
		{
		    FileWriter writer = new FileWriter(fileName);
		    for(int i=0;i<sortByValueInDecreasingOrder.size();i++){
		    	writer.append(sortByValueInDecreasingOrder.get(i).toString().split("=")[0]);
		    	 writer.append('|');
		    	writer.append(sortByValueInDecreasingOrder.get(i).toString().split("=")[1]);
		    	  writer.append('\n');
		    }
		  
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	    }

}
