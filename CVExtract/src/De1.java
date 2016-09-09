import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import com.google.gson.Gson;

public class De1 {

	private List<List<String>> blockData = new ArrayList<>();
	private InfoCV info = new InfoCV();
	private static De1 function = new De1();
	
	public static void main(String[] args) {

		File input = new File("input");
		if(!input.exists()) {
			return;
		}
		listFilesForFolder(input);
	}
	
	public static void listFilesForFolder(final File folder) {
		Gson gson = new Gson();
		
		File output = new File("output");
		if(!output.exists()) {
			output.mkdirs();
		}
		
		
	    for (File file : folder.listFiles()) {
	        if (!file.isDirectory()) {
	            System.out.println(file.getName());
	            function.info = new InfoCV();
	            
	            FileInputStream fis;
	    		try {
	    			fis = new FileInputStream(file);
	    			HWPFDocument doc = new HWPFDocument(fis);
	    			WordExtractor extractor = new WordExtractor(doc);
	    			
	    			function.handlerData(extractor);
	    			String name = file.getName();
	    			if(name.indexOf(".") > 0) {
	    				name = name.substring(0, name.indexOf("."));
	    			}
	    			String pathName = output.getPath() + "/" + name +".json";

	    			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	    				    new FileOutputStream(pathName), "UTF-8"));) {
	    			    gson.toJson(function.info, writer);
	    			}
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	        }
	    }
	}
	
	
	private void handlerData(WordExtractor extractor) {
		blockData.clear();
		String[] arrayData = extractor.getParagraphText();
		List<String> temp = new ArrayList<>();
		for(String data : arrayData) {
			if(data.matches(Constant.SPLIT_PARAM)) {
				if(!temp.isEmpty()) {
					blockData.add(temp);
				}
				temp = new ArrayList<>();
			} else {
				data = data.replace("\n", " ").replace("\r", " ");
				temp.add(data.toLowerCase());
			}
		}

		int index = -1;
		for(List<String> data : blockData) {
			String removeIndex = "";
			
			for(String child : data) {
				if(child.matches(Constant.PERSONAL)) {
					index  = Constant.PERSONAL_INDEX;
					removeIndex = child;
					break;
				} else if (child.matches(Constant.EDUCATION)) {
					index  = Constant.EDUCATION_INDEX;
					removeIndex = child;
					break;
				} else if (child.matches(Constant.EMPLOYMENT)) {
					index  = Constant.EMPLOYMENT_INDEX;
					removeIndex = child;
					break;
				} 
			}
			
			if(!removeIndex.isEmpty()) {
				data.remove(removeIndex);
			}
			
			switch(index) {
				case Constant.PERSONAL_INDEX : 
					getDataPersonal(data);
					break;
				case Constant.EDUCATION_INDEX : 
					getDataEducation(data);
					break;
				case Constant.EMPLOYMENT_INDEX : 
					getDataEmployment(data);
					break;
				default:
					getDataPersonal(data);
					break;
			}
		}
		
		System.out.println("********************FINISHED************************");
	}
	
	private void getDataPersonal(List<String> data){
		Personal per = info.personal;
		
		
		for(String child : data) {
			String valueAssign = child;
			String[] value = child.split(Constant.SPLIT_PERSONAL);
			if(value.length >= 2 && value[1].trim().length() > 1) {
				valueAssign = value[1];
			} 
			
			if(child.matches(Constant.NAME)) {
				per.name = valueAssign;
			} else if(child.matches(Constant.GENDER)) {
				per.gender = valueAssign;
			} else if(child.matches(Constant.DOB)) {
				per.dob = valueAssign;
			} else if(child.matches(Constant.NATIONALITY)) {
				per.nationality = valueAssign;
			} else if(child.matches(Constant.ADDRESS)) {
				per.address = valueAssign;
			} else if(child.matches(Constant.COUNTRY)) {
				per.country = valueAssign;
			} else if(child.matches(Constant.EMAIL)) {
				child = child.replaceAll(Constant.OUT_TEXT_EMAIL, " ");
				value = child.split(" ");
				for(String email : value) {
					if(email.matches("(.*)@(.*)com")) {
						per.email = email.trim();
						continue;
					}
				}
				
			} else if(child.matches(Constant.MOBILE)) {
				per.mobile = valueAssign;
			} else if(child.matches(Constant.MATITAL_STATUS)) {
				per.maritalStatus = valueAssign;
			}    
		}
	}
	
	private void getDataEducation(List<String> data){
		Education edu = new Education();
		for(String child : data) {
			Boolean isYear = child.matches(Constant.YEAR);
			String[] array = new String[1];
			
			if(!isYear) {
				array = child.split(Constant.OUT_TEXT_PARAGRAPH);
			}else if(isYear) {
				String number = child.replaceAll(Constant.YEAR_VALUE,Constant.SPLIT_YEAR).trim();
				String [] year = number.split(Constant.SPLIT_YEAR);
				List<String> yearList = new ArrayList<>();
				for(String temp: year) {
					if(!temp.trim().isEmpty()) {
						yearList.add(temp.trim());
					}
				}
				
				if(yearList.size() > 1) {
					edu.startDate = yearList.get(0);
					edu.endDate = yearList.get(1);	
				} else if(yearList.size() == 1) {
					edu.startDate  = yearList.get(0);
					edu.endDate  = yearList.get(0);
				}
				
				int start = child.indexOf(edu.startDate);
				int end = child.indexOf(edu.endDate) + edu.endDate.length();
				child = child.replaceFirst( child.substring(start, end), "");
				array = child.split(Constant.OUT_TEXT_PARAGRAPH);
			}
			
			if(child.matches(Constant.SCHOOL) || child.matches(Constant.DEGREE)) {
				for(String paragraph : array) {
					if(paragraph.isEmpty()) { 
						continue;
					}
					
					if(paragraph.matches(Constant.SCHOOL) && edu.school.isEmpty()) {
						edu.school = paragraph.trim();
					} else if(paragraph.matches(Constant.DEGREE) && edu.degree.isEmpty()) {
						edu.degree = paragraph.trim();
					} else {
						edu.description += paragraph.trim() + " ";
					}
				}
			} else if(!isYear) {
				edu.description += " " + child.trim() + "\n";
			}
		}
		info.educations.add(edu);
	}
	
	private void getDataEmployment(List<String> data){
		Employment emp = new Employment();
		Boolean isEmp = false;
		for(String child : data) {
			Boolean isYear = child.matches(Constant.YEAR);
			String[] array = new String[1];
			if(!isYear) {
				array = child.split(Constant.OUT_TEXT_PARAGRAPH);
			} else if(isYear) {
				isEmp = true;
				String number = child.replaceAll(Constant.YEAR_VALUE,Constant.SPLIT_YEAR).trim();
				String [] year = number.split(Constant.SPLIT_YEAR);
				List<String> yearList = new ArrayList<>();
				for(String temp: year) {
					if(!temp.trim().isEmpty()) {
						yearList.add(temp.trim());
					}
				}
				
				if(yearList.size() > 1) {
					emp.startDate = yearList.get(0);
					emp.endDate = yearList.get(1);	
				} else if(yearList.size() == 1) {
					emp.startDate  = yearList.get(0);
					emp.endDate  = yearList.get(0);
				}
				
				int start = child.indexOf(emp.startDate);
				int end = child.indexOf(emp.endDate) + emp.endDate.length();
				child = child.replaceFirst(child.substring(start, end), "");
				array = child.split(Constant.OUT_TEXT_PARAGRAPH);
			}
			
			if((emp.position.isEmpty() && child.matches(Constant.POSITION))
				|| (emp.employer.isEmpty() &&child.matches(Constant.EMPLOYER))) {
				for(String paragraph : array) {
					if(paragraph.isEmpty()) { 
						continue;
					}
					
					if(emp.position.isEmpty() && paragraph.matches(Constant.POSITION)) {
						emp.position = paragraph.trim();
					} else if(emp.employer.isEmpty() && paragraph.matches(Constant.EMPLOYER)) {
						emp.employer = paragraph.trim();
					} else {
						emp.description += paragraph.trim() + " ";
					}
				}
			} else if(!isYear){
				emp.description += child.trim() + "\n";
			}
		}
		
		if(isEmp) {
			info.employments.add(emp);
		}
	}
}