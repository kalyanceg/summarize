/**
 * Creates Extractive Summary
 * @author Kalyan
 * @version 0.0.1 Dec 8,2012
 */

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aliasi.cluster.LatentDirichletAllocation;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


public class extractText {
	/**
	 * find maximum probability topic in corpus
	 * @param sample will provide probability matrices
	 * @return integer denoting the topic
	 */
  static int findDocTopicProb(LatentDirichletAllocation.GibbsSample sample){
	  double maxval=0.0;
	  int maxtop=-1;
	  double []sum=new double[sample.numTopics()];
	  for(int doc=0;doc<sample.numDocuments();doc++){
		 for(int topic=0;topic<sample.numTopics();topic++)
			 sum[topic]+=sample.documentTopicProb(doc, topic);
	 }
	  for(int topic=0;topic<sample.numTopics();topic++){
		  if(maxval<sum[topic]){
			  maxval=sum[topic];
			  maxtop=topic;
		  }
	  }
	  return maxtop;
  }
  /**
   * Associate a score with each sentence
   * @param sample probability matrix
   * @return Hashmap with sentence being located by its index value and score
   */
  static HashMap<Integer,Double> sentenceProbability(int topic,LatentDirichletAllocation.GibbsSample sample){
	 HashMap<Integer,Double> maxTopicProb=new HashMap<Integer,Double>();
	  double probValue=0.0;
	  for(int sentence=0;sentence<pdfExtractor.sentences.size();sentence++){
		  String sent=pdfExtractor.sentences.get(sentence);
		  //System.out.println(sent);
		  String wordArray[]=sent.split(" .");
		  if(wordArray.length>2){
		  for(int word=0;word<wordArray.length;word++){
			  String wordSymbol=wordArray[word];
			  int symId=clusterPages.sym.symbolToID(wordSymbol);
			  //System.out.println(symId);  
			  if(symId!=-1)
			  probValue+=sample.topicWordProb(topic, symId);
		  }
		  }
		  probValue/=wordArray.length;
		  maxTopicProb.put(sentence,probValue);
		  //System.out.println(probValue);
	  }
	  return maxTopicProb;
  }
  /**
   * Sort Sentences and pick the higher ranking ones 
   * @param SentenceProb Map with scores
   * @return Map sorted by descending order of scores associated with sentences
   */
  static  List<Map.Entry<Integer, Double>> SortSentenceProb(HashMap<Integer,Double>SentenceProb){
	  List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer,Double>>(SentenceProb.entrySet());
      
	  Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {

          public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2) {
              return (m2.getValue()).compareTo(m1.getValue());
          }
      });

      return list;
  }
  /**
   * Pick high probable sentences within threshold and sort them based on their occurences for coherence and write to a pdf file
   * @param list sorted probabilty of sentence scores
   * @param  percent threshold of sentences to be extracted
   * @param outfile the output file which will contain summary
   */
   static void print(List<Map.Entry<Integer, Double>>list,int percent,String outfile){
	   Document document = new Document();
	   try{
	   PdfWriter.getInstance(document, new FileOutputStream(outfile));
	   document.open();
	   int j=0;
	   int limit=percent*pdfExtractor.sentences.size()/100;
	   int arindex[]=new int[limit];
	   for (Map.Entry<Integer, Double> entry : list) {
		   if(j<limit){
	        arindex[j]=(entry.getKey());
	        j++;
		   }
		   else
			   break;
	          //System.out.println(entry.getKey()+" "+entry.getValue());
	      }
	  Arrays.sort(arindex);
	  for(int i=0;i<arindex.length;i++)
		  document.add(new Paragraph("--->"+pdfExtractor.sentences.get(arindex[i])));
	  document.close();
	   }
	   catch(Exception e){
		   System.out.println("pdf write error");
	   }
   }
   
   
   /**
    * all methods in this class are called from here. This is the entry point
    * @param file input pdf file
    * @param percent threshold to be extracted
    * @param outfile result to be stored
    */
  static int extractiveSummarizer(String file,int percent,String outfile){
      LatentDirichletAllocation.GibbsSample sample=clusterPages.LDASample(file);
      /*select maximum topic, actually it should be multinomial*/
      if(sample!=null){
      int unoptimizedTopic=findDocTopicProb(sample);
     HashMap<Integer,Double> SentenceProb=sentenceProbability(unoptimizedTopic,sample);
     List<Map.Entry<Integer, Double>>sortSentenceProb=SortSentenceProb(SentenceProb);
     print(sortSentenceProb,percent,outfile);
     return 1;
      }
      return 0;
     
  }
 
}

