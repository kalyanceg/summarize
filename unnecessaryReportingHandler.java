

import com.aliasi.cluster.LatentDirichletAllocation;
import com.aliasi.cluster.LatentDirichletAllocation.GibbsSample;

import com.aliasi.corpus.ObjectHandler;

import com.aliasi.symbol.SymbolTable;


public class unnecessaryReportingHandler implements ObjectHandler<LatentDirichletAllocation.GibbsSample>  {
	private final SymbolTable mSymbolTable;
    private final long mStartTime;

    unnecessaryReportingHandler (SymbolTable symbolTable) {
        mSymbolTable = symbolTable;
        mStartTime = System.currentTimeMillis();
    }

	
	public void handle(GibbsSample arg0) {
		
		
	}
}
