package Assignment1;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.sinks.TeeSinkTokenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.StopFilter;

/**
 * @author aishwaryaagarwal
 *
 */
public class CustomAnalyzer extends Analyzer {
	
	@Override
	protected TokenStreamComponents createComponents(String arg0) {
		// TODO Auto-generated method stub
		StandardTokenizer tokenizer = new StandardTokenizer();
		
		//Add different filters to token stream
		TokenStream filter = new LowerCaseFilter(tokenizer);
		filter = new TeeSinkTokenFilter(filter);
		filter = new StopFilter(filter, EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
		filter = new PorterStemFilter(filter);
		return new TokenStreamComponents(tokenizer, filter);
	}

}
