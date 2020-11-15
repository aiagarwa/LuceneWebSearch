package Assignment1;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 */

/**
 * @author aishwaryaagarwal
 *
 */
public class SearchQuery {
	
	private static Analyzer analyzer;
	private static Similarity similarity;
	private static String indexFolderPath;
	private static String searchResultFolderPath;
	private static ArrayList<HashMap<String,String>> searchQueries;
	
	/**
	 * @param analyzer
	 * @param similarity
	 * @param searchResultFolderPath
	 * @param indexFolderPath
	 * @param searchQueries
	 */
	public SearchQuery(Analyzer analyzer, Similarity similarity,String searchResultFolderPath, String indexFolderPath, ArrayList<HashMap<String,String>> searchQueries){
		this.analyzer = analyzer;
		this.similarity = similarity;
		this.indexFolderPath = indexFolderPath;
		this.searchResultFolderPath = searchResultFolderPath;
		this.searchQueries = searchQueries;
	}
	
	/**
	 * Query and Search Indexed Files 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void searchQueryForIndexedFiles() throws IOException, ParseException {		
		ArrayList<String> searchResults = new ArrayList<String>();
				
		//Delete folder if existing
		CommonUtils.deleteFolder(searchResultFolderPath);
		
		// Open the folder that contains our search index
		Directory directory = FSDirectory.open(Paths.get(indexFolderPath));
		
		// create objects to read and search across the index
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		
		// Set similarity(Scoring Function)
		isearcher.setSimilarity(similarity);
		
		// Create the query parser
		//QueryParser parser =new QueryParser("Content", analyzer);
		QueryParser parser = new MultiFieldQueryParser(new String[] {"Title", "Authors", "Bib", "Content"}, analyzer);
		int count = 1;
		
		for(HashMap<String,String> searchQuery : searchQueries) {
			
			String QueryAfterAddingEscapeChar = QueryParser.escape(searchQuery.get("Content"));
			// parse the query with the parser
			Query query = parser.parse(QueryAfterAddingEscapeChar);

			// Get the set of results
			ScoreDoc[] hits = isearcher.search(query, CommonConstants.MAX_RESULTS).scoreDocs;

			// Print the results
			System.out.println("Documents: " + hits.length);
			
			for (int i = 0; i < hits.length; i++) {
				Document value = ireader.document(hits[i].doc);
				String[] docNo = value.getValues("ID");
				//"QueryNumber", Q0, "DocumentNo",DocumentRank", DocumentScore"
				searchResults.add(String.valueOf(count)+"\t"+0+"\t"+docNo[0]+"\t"+String.valueOf(i+1)+"\t"+String.valueOf(hits[i].score)+"\t Standard");
			}
			count++;
		}
		
		//Add all the results into the String
		StringBuilder str = new StringBuilder();
		for(String searchResult:searchResults) {
			str.append(searchResult +"\n");
		}
		
		//Create folder to save the search results
		CommonUtils.checkAndCreateFolder(searchResultFolderPath);
		
		//Write results into the file
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(searchResultFolderPath + "/SearchResult.txt")));
		writer.write(str.toString());
		
		//Close everything 
		writer.close();
	}

}
