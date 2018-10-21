import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Apriori {
	public static ArrayList<Transaction> dataSet = null;
	public static int totalTrasaction =0;
	public static int maxOfItemInTrasaction =0;
	
	public static void main(String[] args) throws Exception {
		
		
		
		double minSup =0.2;
		double minConfidence=0.6;
		String filePath = "DataSet";
		Apriori apriori = new Apriori();
		Converter converter = new Converter();
		dataSet = converter.ToTransactionList(filePath+".data");
		totalTrasaction = converter.totalOfTrasaction;
		converter.ToArrfFile(dataSet, 20, filePath);
		
		long start = System.currentTimeMillis();

		System.out.println("開始執行..");
		
		ArrayList<Candidate> c = apriori.getFirstCandidateSet() ;		
		ArrayList<Candidate> l=apriori.getFrequentSet(c, minSup*totalTrasaction);
		ArrayList<Candidate> largeItemSet = new ArrayList<Candidate>();
		int k=0; 
		while(true){
			k++;
			// 計算CandidateSet
			c=apriori.genCandidateSet(l);		
		
			//過濾掉 Support 小於 minSup的item,取得Frequent set
			ArrayList<Candidate> nextL=apriori.getFrequentSet(c, minSup*totalTrasaction);	

			// 新的Frequent set 為空,則結束
			if(nextL.size()==0) 
				break;
			
			l=nextL;
			// 將目前的Lk存到最後的largeItemSet
			largeItemSet.addAll(nextL);

		}
				
		//從剩下的candidate set 取得Rule		 	
		ArrayList<String> rules =apriori.getRules(largeItemSet, minConfidence);
		
		long end = System.currentTimeMillis();
		System.out.println("結束..");
		System.out.println("共花  " + (end-start) + "ms");
		System.out.println("結果:");
		System.out.println("rule number = " + rules.size());
		
		int ruleIndex =0;
		for(String r:rules){
			ruleIndex++;
    		System.out.println(ruleIndex+". " + r.toString());
    		
		}
		
	}
	
	/*產生 第一個candidate set*/
	public ArrayList<Candidate> getFirstCandidateSet(){		
		ArrayList<Candidate> c1=new ArrayList<Candidate>();
		Set<String> diffItemSet = new HashSet<String>();
		
        //System.out.println("--- Generate C1 ---");
        
		for(Transaction tr : dataSet){
			for(int index=0; index < tr.numberOfItem ;index++){
					diffItemSet.add(tr.getItem(index));
			}				
		}		
		
		for(String item : diffItemSet){
			Candidate c=new Candidate(item);
			c.setSupport(this.countSupport(c));
			c1.add(c);
			//System.out.println(c.toString());	
		}
		
		//System.out.println("\n");	
		
		return c1;
	}
	
	/*過濾掉小於minSup的Item,取得Frequents Item */
	public ArrayList<Candidate> getFrequentSet(ArrayList<Candidate> cSet,double minSup){
		ArrayList<Candidate> result = new ArrayList<Candidate>();
		
		//System.out.println("--- Generate L ---");
		for(Candidate cdt : cSet){
			if(cdt.getSupport()>=minSup){
				result.add(cdt);
				//System.out.println(cdt.toString());
			}
		}	
		//System.out.println("\n");	
		return result;
	}
		
	/*計算一個candidate的support值*/
	public int countSupport(Candidate c){
		int support =0;
		for(Transaction tr : dataSet){
			if(tr.contains(c.getItemSet()))
				support++;
		}
		return support;
	}
	
	/*計算一個candidate的support值*/
	public int countSupport(ArrayList<String> items){
		int support =0;
		
		for(Transaction tr : dataSet){
			if(tr.contains(items)){
				support++;
			}				
		}
		return support;
	}
	/*產生下一個CandidateSet*/
	public ArrayList<Candidate> genCandidateSet(ArrayList<Candidate> c){
		ArrayList<Candidate> c1 = c;
		ArrayList<Candidate> c2 = c;
		Set<Candidate> nextC = new HashSet<Candidate>();
		
		//System.out.println("--- Generate nextC ---");
		
		for(int i=0; i<c1.size() ;i++)
			for(int j=i+1;j<c2.size();j++){
				Candidate newC = new Candidate(this.union(c1.get(i).getItemSet(), c2.get(j).getItemSet()));
				if(!nextC.contains(newC)){
				newC.setSupport(this.countSupport(newC));
				nextC.add(newC);
				//System.out.println(newC.toString());	
				}
			}
				
		//System.out.println("\n");	
		return new ArrayList<Candidate>(nextC);
	}
	
	/*聯集運算*/
    public <T> ArrayList<T> union(ArrayList<T> list1, ArrayList<T> list2) {
        Set<T> set = new HashSet<T>();
        set.addAll(list1);
        set.addAll(list2);
        return new ArrayList<T>(set);
    }
    
    /*得到所有 Confidence > minConfidence 的規則
     * 做法:
     * 1.對FrequentSet裡的每個Item Set計算出所有可能的規則
     * 2.計算每條規則的conf值,if > minConf 則存到結果List中
     * 
     * */
   
    public	ArrayList<String> getRules(ArrayList<Candidate> frequentSet,double minConfidence){
    	
    	Set<String> result = new HashSet<String>();
    	DecimalFormat df=new DecimalFormat("#.##");
    	//System.out.println("--- Get Rules ---");
    	
    	for(Candidate cdt:frequentSet){			
    		int numOfItem= cdt.getItemSet().size();
    		double totalCount = Math.pow(2, numOfItem)-2; 		
    		ArrayList<Integer> allLoser = new ArrayList<Integer>();

    		//檢查是否可以Prune
    		for(int count=1; count<=totalCount ; count++){
    			boolean canPrune=false;
    			if(!allLoser.isEmpty()){
    				for(int loser :allLoser){
    					int tmp = loser & count;
    						if(tmp==loser)
    							canPrune=true;
    				}
    				
    			}
    			
    			if(!canPrune){
	    			ArrayList<String> setA = new ArrayList<String>();
	    			ArrayList<String> setB = new ArrayList<String>();
					int binaryCount = count;
		    		//轉成2進制表示,透過位元={1,0}來將Item Set分成A,B集合
		    		//產生規則 A->B,並計算conf值
	    			for(int index=0; index <numOfItem ; index++){
	    				if((binaryCount & 1)==0){
	    					setB.add(cdt.getItemSet().get(index));
	    				}	
	    				else{
	    					setA.add(cdt.getItemSet().get(index));   						
	    				}   				
	    				binaryCount = binaryCount >>1;				
	    			}
	    			
	    			//calculate the confidence of rule A->B    			
	    			int supA=this.countSupport(setA);  
	    			int supB=this.countSupport(setB);
	    			int supUnion = this.countSupport(this.union(setA, setB));  			
	    			double conf = (double) supUnion /  supA;
	    			
	    			//find a new rule
	    			if(conf > minConfidence){
						String rule =setA.toString() + " sup:"+supA + "=>" 
								+ setB.toString()+ " sup"+ supUnion+" conf="+ df.format(conf);
						result.add(rule);
						//System.out.println("Find rule:" + rule);
	    			}
	    			else{
	    				allLoser.add(count);
	    			}
    			}		
    		}
    		
    	}
    	
    	
    	System.out.println("\n");	
    	return new ArrayList<String>(result);
    }
    
    /*判斷兩個List是否有元素重複*/
    public boolean Overlapping (ArrayList<String> list1 ,ArrayList<String> list2){
    	
    	if(list1.size() > list2.size()){
    		for(String elemt2 : list2)
    		  if(list1.indexOf(elemt2)>-1)
    			  return true;
    	}
    	else{
    		for(String elemt1 : list1)
    			if(list2.indexOf(elemt1)>-1)
    				return true;
    		}
    	return false;
    
    }
}

