package me.jhuhn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVParser {

	public static void main(String[] args) throws IOException {
		boolean debug = false;
		String seperator = "|";
		PrintWriter writer = new PrintWriter("data_set_modified_by_jo.csv", "UTF-8");
        BufferedReader br = new BufferedReader(new FileReader(new File("data_set_original.csv")));        
        String oneLine = "";
        int k = 0;
        
        
        
        while ( (oneLine = br.readLine()) != null) {
        	if(debug)
           System.out.println(oneLine);
           
           String[] parts = oneLine.split(",");
           
           if(parts[14].trim().equals("")) {
        	   parts[14] = "NONE";
           }
           
           if(debug)
           System.out.println(parts[14]);
           
           if(parts[14].contains(seperator)) {
        	   String[] csvValues =  parts[14].split("\\" + seperator);
        	   
        	   for (int i = 0; i < csvValues.length; i++) {
        		   parts[14] = csvValues[i];
        		   
        		   String newLine = "";
        		   for (int j = 0; j < parts.length; j++) {
        			   newLine += (parts[j] + ",");
        		   }
        		   writer.write(newLine + "\n");
        	   }
        	   
        	   for (int l = 0; l < csvValues.length; l++) {
        		   if(debug)
        		   System.err.println(csvValues[l]);
        	   }
        	   
           }else {
        	   writer.write(oneLine + "\n");
           }
           
           
           if(debug)
           System.out.println("-------------------------------------------------------------------------------------------------");
           
           k++;
           
//           if(k > 1000) {
//        	   writer.close();
//        	   return;
//           }
        }
	}
}
