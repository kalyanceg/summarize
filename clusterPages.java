import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.aliasi.cluster.LatentDirichletAllocation;
import com.aliasi.symbol.MapSymbolTable;
import com.aliasi.symbol.SymbolTable;
import com.aliasi.symbol.SymbolTableCompiler;
import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.ModifyTokenTokenizerFactory;
import com.aliasi.tokenizer.RegExTokenizerFactory;
import com.aliasi.tokenizer.StopTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;



public class clusterPages {
	static SymbolTable sym=new MapSymbolTable();
	static CharSequence articleText[];
	static LatentDirichletAllocation.GibbsSample LDASample(String file){
		ArrayList<CharSequence> pdfPages=pdfExtractor.fileRead(file);
		if(pdfPages!=null){
		 articleText=pdfPages.toArray(new CharSequence[pdfPages.size()]);
		
		int [][]docTokens=LatentDirichletAllocation.tokenizeDocuments(articleText,WORMBASE_TOKENIZER_FACTORY,sym,1);
		/*reporting handler is not necessary for us to display results*/
		unnecessaryReportingHandler handler= new unnecessaryReportingHandler(sym);
		short numTopics=6;
		double alpha=0.1;
		double beta=0.01;
		int numIterations=100;
		Random randomseed=new Random(35L);
		LatentDirichletAllocation.GibbsSample sample = LatentDirichletAllocation.gibbsSampler(docTokens, 
				          numTopics, alpha,  beta, 0, 1, numIterations, randomseed, handler);
		return sample;  
		}
		return null;
	}
	
	/*tokenizer copied from lingpipe source*/
	
	static final TokenizerFactory wormbaseTokenizerFactory() {
        TokenizerFactory factory = BASE_TOKENIZER_FACTORY;
        factory = new NonAlphaStopTokenizerFactory(factory);
        factory = new LowerCaseTokenizerFactory(factory);
        factory = new EnglishStopTokenizerFactory(factory);
        factory = new StopTokenizerFactory(factory,STOPWORD_SET);
        factory = new StemTokenizerFactory(factory);
        return factory;
    }


	
    static boolean validStem(String stem) {
        if (stem.length() < 2) return false;
        for (int i = 0; i < stem.length(); ++i) {
            char c = stem.charAt(i);
            for (int k = 0; k < VOWELS.length; ++k)
                if (c == VOWELS[k])
                    return true;
        }
        return false;
    }


    static final TokenizerFactory BASE_TOKENIZER_FACTORY
        = new RegExTokenizerFactory("[\\x2Da-zA-Z0-9]+"); // letter or digit or hyphen (\x2D)


    static final char[] VOWELS
        = new char[] { 'a', 'e', 'i', 'o', 'u', 'y' };


    static final String[] STOPWORD_LIST=stopWordList.stopwords;
      

    static final Set<String> STOPWORD_SET= new HashSet<String>(Arrays.asList(STOPWORD_LIST));

    static final TokenizerFactory WORMBASE_TOKENIZER_FACTORY
        = wormbaseTokenizerFactory();


    // removes tokens that have no letters
    static class NonAlphaStopTokenizerFactory extends ModifyTokenTokenizerFactory {
        static final long serialVersionUID = -3401639068551227864L;
        public NonAlphaStopTokenizerFactory(TokenizerFactory factory) {
            super(factory);
        }
        public String modifyToken(String token) {
            return stop(token) ? null : token;
        }
        public boolean stop(String token) {
            if (token.length() < 2) return true;
            for (int i = 0; i < token.length(); ++i)
                if (Character.isLetter(token.charAt(i)))
                    return false;
            return true;
        }
    }

    static class StemTokenizerFactory extends ModifyTokenTokenizerFactory {
        static final long serialVersionUID = -6045422132691926248L;
        public StemTokenizerFactory(TokenizerFactory factory) {
            super(factory);
        }
        static final String[] SUFFIXES = new String[] {
            "ss", "ies", "sses", "s" // s must be last as its weaker
        };
        public String modifyToken(String token) {
            for (String suffix : SUFFIXES) {
                if (token.endsWith(suffix)) {
                    String stem = token.substring(0,token.length()-suffix.length());
                    return validStem(stem) ? stem : token;
                }
            }
            return token;
        }
    }
	public static void main(String args[]){
		LatentDirichletAllocation.GibbsSample s=LDASample("/home/kalyan/Downloads/ror.pdf");
	}

}
