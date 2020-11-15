package Assignment1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

/**
 * 
 */

/**
 * @author aishwaryaagarwal
 *
 */

public class CommonUtils {


	/**
	 * Method to load properties from properties file
	 * @param path
	 * @return 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties loadPropertyFile(String path) throws FileNotFoundException, IOException {
		Properties _props = new Properties();
		_props.load(new FileInputStream(path));
		return _props;
	}
	
	/**
	 * Method to check if file exists
	 * @param file
	 * @return boolean value
	 * @throws FileNotFoundException
	 */
	public static boolean checkIfFileExists(File file) throws FileNotFoundException{
		if(!file.exists())
			return false;
		else 
			return true;
	}

	/**
	 * Method to check if folder exists and create if not
	 * @param path
	 */
	public static void checkAndCreateFolder(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			System.out.println("Creating directory: " + file.getName());
			file.mkdir();
		} else {
			System.out.println(file.getName() + " exists.");
		}
	}
	
	/**
	 * Method to check if folder exists and delete it
	 * @param path
	 */
	public static void deleteFolder(String path) throws IOException {
		File dir = new File(path);
		if (dir.exists()) {
			System.out.println("Deleting directory: " + dir.getName());
			FileUtils.deleteDirectory(dir);
		} else {
			System.out.println(dir.getName() + " does not exist.");
		}
	}

	/**
	 * Method to get all the sub folders and files
	 * @param path
	 * @return Path of all the sub folders and files 
	 */
	public static File[] getAllSubFolders(String parentFolderPath) {
		File directory = new File(parentFolderPath + "/");
		File[] indexPathList = directory.listFiles();
		return indexPathList;
	}

	/**
	 * Method to set the Analyzer
	 * @param Analyzer Type
	 * @return Analyzer
	 */
	public static Analyzer setAnalyzerType(String analyzerType) {
		Analyzer analyzer = null;
		System.out.println("Analyzer: " + analyzerType);
		if (analyzerType.equals("StandardAnalyzer")) {
			analyzer = new StandardAnalyzer();
			return analyzer;
		} else if (analyzerType.equals("EnglishAnalyzer")) {
			analyzer = new EnglishAnalyzer();
			return analyzer;
		}else if (analyzerType.equals("WhitespaceAnalyzer")) {
			analyzer = new WhitespaceAnalyzer();
			return analyzer;
		}else if (analyzerType.equals("CustomAnalyzer")) {
			analyzer = new CustomAnalyzer();
			return analyzer;
		}
		return analyzer;
	}

	/**
	 * Method to set the Similarity
	 * @param Similarity Type
	 * @return Similarity
	 */
	
	public static Similarity setSimilarityType(String similarityType) {
		Similarity similarity = null;
		System.out.println("Selected similarity: " + similarityType);
		if (similarityType.equals("Classic")) {
			similarity = new ClassicSimilarity();
			return similarity;
		} else if (similarityType.equals("LMDirichletSimilarity")) {
			similarity = new LMDirichletSimilarity();
			return similarity;
		} else if (similarityType.equals("BM25Similarity")) {
			similarity = new BM25Similarity();
			return similarity;
		} 
		return similarity;
	}

	/**
	 * Method to parse index and search query files
	 * @param filepath
	 * @return Parsed Content in ArrayList of HashMap
	 */

	public static ArrayList<HashMap<String, String>> parseFile(String filepath) throws IOException {
		ArrayList<HashMap<String,String>> compDoc = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> singleDoc = new HashMap<String, String>();

		File file = new File(filepath);
		if(!CommonUtils.checkIfFileExists(file))
			System.out.println("File not found at path :" + filepath);
		else {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str = br.readLine();
			while(str != null){
				if(str.startsWith(".I")) {
					compDoc.add(singleDoc);
					singleDoc = new HashMap<String, String>();
					String[] temp_str = str.split("\\s");
					singleDoc.put("Id", temp_str[1]);
					str = br.readLine();
				}
				else if(str.equals(".T")) {
					str = br.readLine();
					String temp_str = "";
					while(str != null) {
						if(str.equals(".A"))
							break;
						temp_str = temp_str+str+" ";
						str = br.readLine();
					}
					singleDoc.put("Title", temp_str.trim());
				}
				else if(str.equals(".A")) {
					str = br.readLine();
					String temp_str = "";
					while(str != null) {
						if(str.equals(".B"))
							break;
						temp_str = temp_str+str+" ";
						str = br.readLine();
					}
					singleDoc.put("Authors", temp_str.trim());
				}
				else if(str.equals(".B")) {
					str = br.readLine();
					String temp_str = "";
					while(str != null) {
						if(str.equals(".W"))
							break;
						temp_str = temp_str+str+" ";
						str = br.readLine();
					}
					singleDoc.put("Bib", temp_str.trim());
				}
				else if(str.equals(".W")) {
					str = br.readLine();
					String temp_str = "";
					while(str != null) {
						if(str.startsWith(".I "))
							break;
						temp_str = temp_str+str+" ";
						str = br.readLine();
					}
					singleDoc.put("Content", temp_str.trim());
				}

			}
			compDoc.add(singleDoc);
			compDoc.remove(0);
			System.out.println("Total Documents parsed : "+ compDoc.size());
			br.close();
		}
		return compDoc;
	}
}
