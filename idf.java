import com.aliasi.cluster.LatentDirichletAllocation;


public class idf {
   
	
	
   static double[] compute_idf(LatentDirichletAllocation.GibbsSample sample, String query){
	
	   String sp[]=query.split(" ");
	   double[]idfreq=new double[sample.numDocuments()];
	   for(int doc=0;doc<sample.numDocuments();doc++)
		   
	   {
		   String docstr=(clusterPages.articleText[doc].toString());
		 for(int k=0;k<sp.length;k++){
			if(docstr.contains(sp[k])){
				idfreq[doc]+=1;
			}
		 }
	   }
	   for(int doc=0;doc<sample.numDocuments();doc++)
	   {
		   idfreq[doc]=1.0-(idfreq[doc]/(double)sp.length);
	   }
	   return idfreq;
   }
}
