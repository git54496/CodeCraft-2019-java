package com.huawei;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/*
 * 读入road.txt, across.txt, car.txt
 */
public class InputData {
	public static HashMap<Integer, Cross> crossMap = new HashMap<Integer, Cross>();
	public static HashMap<Integer, Road> roadMap = new HashMap<Integer, Road>();
	public static HashMap<Integer, Car> carMap = new HashMap<Integer, Car>();


	
	public static void inputData(String[] args ) {
		String car_path = args[0];
		String road_path = args[1];
		String cross_path = args[2];
		File road_file = new File(road_path);
		File cross_file = new File(cross_path);
		File car_file = new File(car_path);
		LineIterator it1 = null, it2 = null, it3 = null;
		
		try {
			it1 = FileUtils.lineIterator(cross_file, "utf-8");
			it2 = FileUtils.lineIterator(road_file, "utf-8");
			it3 = FileUtils.lineIterator(car_file, "utf-8");
			
			System.out.println("inputData() start");
			long start = new Date().getTime();
			
			String line = "";
			
			// cross
			while(it1.hasNext()) {
				line = it1.nextLine();
				if(line.trim().charAt(0) != '#')
					break;
			}
			String[] f = line.substring(1, line.length() - 1).split(", *");
			crossMap.put(Integer.parseInt(f[0]), new Cross(f[0], f[1], f[2], f[3], f[4]));
			
			while(it1.hasNext()) {
				line = it1.nextLine();
				f = line.substring(1, line.length() - 1).split(", *");
				crossMap.put(Integer.parseInt(f[0]), new Cross(f[0], f[1], f[2], f[3], f[4]));
			}
			
			// road
			while(it2.hasNext()) {
				line = it2.nextLine();
				if(line.trim().charAt(0) != '#')
					break;
			}
			f = line.substring(1, line.length() - 1).split(", *");
			roadMap.put(Integer.parseInt(f[0]), new Road(f[0], Integer.parseInt(f[1]), Integer.parseInt(f[2]), 
					Integer.parseInt(f[3]), f[4], f[5], Integer.parseInt(f[6])));
			
			while(it2.hasNext()) {
				line = it2.nextLine();
				f = line.substring(1, line.length() - 1).split(", *");
				roadMap.put(Integer.parseInt(f[0]), new Road(f[0], Integer.parseInt(f[1]), Integer.parseInt(f[2]), 
						Integer.parseInt(f[3]), f[4], f[5], Integer.parseInt(f[6])));
			}
			
			// car
			while(it3.hasNext()) {
				line = it3.nextLine();
				if(line.trim().charAt(0) != '#')
					break;
			}
			f = line.substring(1, line.length() - 1).split(", *");
			carMap.put(Integer.parseInt(f[0]), new Car(f[0], f[1], f[2], Integer.parseInt(f[3]), Integer.parseInt(f[4])));
			
			while(it3.hasNext()) {
				line = it3.nextLine();
				f = line.substring(1, line.length() - 1).split(", *");
				carMap.put(Integer.parseInt(f[0]), new Car(f[0], f[1], f[2], Integer.parseInt(f[3]), Integer.parseInt(f[4])));
			}
	
			System.out.println(crossMap.size() + " " + roadMap.size() + " " + carMap.size());
			
			System.out.println("inputData() complete\ntime: " + (new Date().getTime() - start));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				it1.close();
				it2.close();
				it3.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}






