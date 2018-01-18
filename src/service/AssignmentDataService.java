package service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import model.Activity;
import model.Assessment;
import model.Question;
import model.Questionnaire;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import common.BuildConfig;


public class AssignmentDataService {

	private static class AssignmentDataServicerHolder {
		private static final AssignmentDataService my = new AssignmentDataService();
	}
	
	public static AssignmentDataService getInstance() {
		return AssignmentDataServicerHolder.my;
	}
	
	private AssignmentDataService() {
	}
	
	public ArrayList<Questionnaire> loadQuestionnaireFromJsonFile(String fileName, int numberOfAssessments) {
		numberOfAssessments = numberOfAssessments + 1;
		ArrayList<Questionnaire> qns = new ArrayList<Questionnaire>();
		for (int i = 1 ; i < numberOfAssessments; i++) {
			String tFileName = String.format("%s%sdata%s%s-%s.json", System.getProperty("user.dir"), File.separator, File.separator, fileName, i);
			Gson questionnaireGson = new Gson();
			try (FileReader reader = new FileReader(tFileName)) {
				Questionnaire one = questionnaireGson.fromJson(reader, Questionnaire.class);
				qns.add(one);
			} catch (JsonSyntaxException | JsonIOException | IOException e) {
				e.printStackTrace();
			}
		}
		return qns;
	}
	
	public ArrayList<Activity> loadActivityFromJsonFile(String fileName, int numberOfActivities) {
		numberOfActivities = numberOfActivities + 1;
		ArrayList<Activity> activities = new ArrayList<Activity>();
		for (int i = 1 ; i < numberOfActivities; i++) {
			String tFileName = String.format("%s%sdata%s%s-%s.json", System.getProperty("user.dir"), File.separator, File.separator, fileName, i);
			Gson actGson = new Gson();
			try (FileReader reader = new FileReader(tFileName)) {
				Activity one = actGson.fromJson(reader, Activity.class);
				activities.add(one);
			} catch (JsonSyntaxException | JsonIOException | IOException e) {
				e.printStackTrace();
			}
		}
		return activities;
	}
	
	public <T> ArrayList<T> loadListDataFromJsonFiles(String fileName, int totalItems, Class<T> type) {
		totalItems = totalItems + 1;
		ArrayList<T> results = new ArrayList<T>();
		for (int i = 1 ; i < totalItems; i++) {
			String tFileName = String.format("%s%sdata%s%s-%s.json", System.getProperty("user.dir"), File.separator, File.separator, fileName, i);
			try {
				T one = loadDataFromJsonFile(tFileName, type);
				results.add(one);
			} catch (JsonSyntaxException | JsonIOException | IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public <T> T loadDataFromJsonFile(String fileName, Class<T> type) throws FileNotFoundException, IOException {
		try (FileReader reader = new FileReader(fileName)) {
			Gson gson = new Gson();
			T one = gson.fromJson(reader, type);
			return one;
		}
	}
	
	public ArrayList<Questionnaire> loadQuestionnaire(String fileName) {
		Workbook workbook = null;
		ArrayList<Questionnaire> qns = new ArrayList<Questionnaire>();
		
		try (FileInputStream excelFile = new FileInputStream(new File(String.format("%s%sdata%s%s", System.getProperty("user.dir"), 
				File.separator, File.separator, fileName)))) {
			workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row currentRow = rowIterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				Questionnaire qn = new Questionnaire(cellIterator.next().getStringCellValue(), cellIterator.next().getStringCellValue());
				int questionFile = (int)cellIterator.next().getNumericCellValue();
				Workbook qworkbook = null;
				try (FileInputStream qdataFile = new FileInputStream(new File(String.format("%s%sdata%s%s.xlsx", System.getProperty("user.dir"), 
						File.separator, File.separator, questionFile)))) {
					qworkbook = new XSSFWorkbook(qdataFile);
					int total = qworkbook.getNumberOfSheets();
					for (int i = 0; i < total; i++) {
						Sheet tempSheet = qworkbook.getSheetAt(i);
						Iterator<Row> trowIterator = tempSheet.iterator();
						Row t = trowIterator.next();
						Iterator<Cell> tcellIterator = t.iterator();
						String name = tcellIterator.hasNext() ? tcellIterator.next().getStringCellValue() : "";
						String description = tcellIterator.hasNext() ? tcellIterator.next().getStringCellValue() : "";
						Assessment assessment = new Assessment(name, description);
						while (trowIterator.hasNext()) {
							t = trowIterator.next();
							tcellIterator = t.iterator();
							String qcontent = tcellIterator.next().getStringCellValue().replaceAll("¡¯", "'");
							String qdescription = tcellIterator.next().getStringCellValue();
							String qtype = tcellIterator.next().getStringCellValue();
							Cell answer = tcellIterator.next();
							Cell failAnswer = tcellIterator.next();
							if (answer.getCellTypeEnum() == CellType.NUMERIC) {
								assessment.addQuestion(new Question(qcontent, qdescription, qtype, String.valueOf((int) answer.getNumericCellValue()), String.valueOf((int) failAnswer.getNumericCellValue())));
							} else {
								assessment.addQuestion(new Question(qcontent, qdescription, qtype, answer.getStringCellValue(), failAnswer.getStringCellValue()));
							}
						}
						qn.addAssessment(assessment);
					}
				} catch (FileNotFoundException ee) {
					TestLogger.error(Throwables.getStackTraceAsString(ee));
				} catch (IOException ee1) {
					TestLogger.error(Throwables.getStackTraceAsString(ee1));
				} finally {
					if (qworkbook != null) {
						qworkbook.close();
					}
				}
				
				qns.add(qn);
			}
		} catch (FileNotFoundException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} catch (IOException e1) {
			TestLogger.error(Throwables.getStackTraceAsString(e1));
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				TestLogger.error(Throwables.getStackTraceAsString(e));
			}
		}
		
		return qns;
	}

	public ArrayList<Activity> loadActivity(String fileName) {
		ArrayList<Activity> activities = new ArrayList<Activity>();
		Workbook workbook = null;
		try (FileInputStream excelFile = new FileInputStream(new File(String.format("%s%sdata%s%s", System.getProperty("user.dir"), 
				File.separator, File.separator, fileName)))) {
			workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row currentRow = rowIterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				activities.add(new Activity(cellIterator.next().getStringCellValue(),
						String.valueOf((int) cellIterator.next().getNumericCellValue()),
						String.valueOf((int) cellIterator.next().getNumericCellValue())));
			}
		}  catch (FileNotFoundException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} catch (IOException e1) {
			TestLogger.error(Throwables.getStackTraceAsString(e1));
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				TestLogger.error(Throwables.getStackTraceAsString(e));
			}
		}
		
		return activities;
	}
	
	public void buildStudentEnrolmentCSV(String[] participantIds, String fileName) {
		try (FileOutputStream outputStream = new FileOutputStream(fileName, true)) {
			outputStream.write("\n".getBytes());
			for (String participantId : participantIds) {
				String userName = String.format("selenium.%s", participantId);
				outputStream.write(String.format("%s@%s,%s,%s,fullaccess", userName, BuildConfig.testDomain, participantId, userName).getBytes());
			}
			outputStream.close();
		} catch (IOException | EncryptedDocumentException e) {
			e.printStackTrace();
		}
	}
	
}
