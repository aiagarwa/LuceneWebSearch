package Assignment1;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.similarities.Similarity;

public class RunMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//Load properties file
		Properties config = CommonUtils.loadPropertyFile(CommonConstants.VALUES_FILE);

		System.out.println("Parsing cran file.....");
		
		//Parse the cran file
		ArrayList<HashMap<String,String>> parsedCranFile = CommonUtils.parseFile(config.getProperty(CommonConstants.CRAN_DATA_FILE));
		
		//Create Index folder if does not exist 
		CommonUtils.checkAndCreateFolder(config.getProperty(CommonConstants.INDEX_FOLDER));
		
		//Set Analyzer and Similarity based on the system arguments
		Analyzer analyzer = CommonUtils.setAnalyzerType(args[0]);
		Similarity similarity = CommonUtils.setSimilarityType(args[1]);
		
		//Create indexes for the parsed Cran file
		Indexing ind = new Indexing(analyzer,similarity,parsedCranFile,config.getProperty(CommonConstants.INDEX_FOLDER)+"/indexing_"+args[0]+"_"+args[1]);
		ind.indexFiles();
		System.out.print("Indexing Completed!!...");
		
		System.out.print("Query Search Starts.....");
		//Get all the files from Index folder
		File directory = new File(config.getProperty(CommonConstants.INDEX_FOLDER) + "/indexing_"+args[0]+"_"+args[1]);
		
		//Parse the Query Index Search file
		ArrayList<HashMap<String,String>> parsedQueryFile = CommonUtils.parseFile(config.getProperty(CommonConstants.CRAN_QUERY_FILE));
		
		//Create Results folder
		CommonUtils.checkAndCreateFolder(config.getProperty(CommonConstants.SEARCH_RESULT_FOLDER));
		
		//Query Search on Indexed Files
		SearchQuery squery = new SearchQuery(analyzer,similarity,config.getProperty(CommonConstants.SEARCH_RESULT_FOLDER)+"/searching_"+args[0]+"_"+args[1],directory.toString(),parsedQueryFile);
		squery.searchQueryForIndexedFiles();
		System.out.print("Query Search Completed!!...");
	}

}
