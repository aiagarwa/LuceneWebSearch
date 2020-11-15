package Assignment1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author aishwaryaagarwal
 *
 */
public class Indexing {
	
	private static ArrayList<HashMap<String,String>> parsedCranFile;
	private static Analyzer analyzer;
	private static Similarity similarity;
	private static String indexFolderPath;

	/**
	 * @param analyzer
	 * @param similarity
	 * @param parsedCranFile
	 * @param indexFolderPath
	 */
	public Indexing(Analyzer analyzer, Similarity similarity, ArrayList<HashMap<String,String>> parsedCranFile, String indexFolderPath) {
		// TODO Auto-generated constructor stub
		this.parsedCranFile = parsedCranFile;
		this.analyzer = analyzer;
		this.similarity = similarity;
		this.indexFolderPath = indexFolderPath;
	}
	
	/**
	 * Method to Index Cran File
	 * @throws Exception
	 */
	public void indexFiles() throws Exception {
		
		//Delete existing index folder
		CommonUtils.deleteFolder(indexFolderPath);
		
		//Create Folder
		CommonUtils.checkAndCreateFolder(indexFolderPath);
		
		// To store an index on Disk
		Directory indexFolder = FSDirectory.open(Paths.get(indexFolderPath));
		
		//Set analyzer and Similarity for Indexing
		IndexWriterConfig wriConf = new IndexWriterConfig(analyzer);
		wriConf.setSimilarity(similarity);

		// Index Opening Mode
		wriConf.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(indexFolder, wriConf);
		
		//Read parsed file and Create a new document
		for (HashMap<String, String> singleDoc : parsedCranFile) {
			Document doc = new Document();
			doc.add(new StringField("ID", singleDoc.get("Id"), Field.Store.YES));
			doc.add(new TextField("Title", singleDoc.get("Title"), Field.Store.YES));
			doc.add(new TextField("Authors", singleDoc.get("Authors"), Field.Store.YES));
			doc.add(new TextField("Bibliography", singleDoc.get("Bib"), Field.Store.YES));
			doc.add(new TextField("Content", singleDoc.get("Content"), Field.Store.YES));
			
			// Save the document to the index
			writer.addDocument(doc);
		}
		
		// Commit changes and close everything
		writer.close();
		indexFolder.close();

	}
}
