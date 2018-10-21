import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Candidate{
    private ArrayList<String> itemSet = new ArrayList<String>();
	private int support =0;
	
	/*construct*/
	public Candidate(){
		
	}
		
	public Candidate(String item){
		itemSet.add(item);
	}
	
	public Candidate(ArrayList<String> set){
		itemSet = set;
	}
	
	/*取得所有 非空真子集*/
	public ArrayList<ArrayList<String>> getAllRealSubset(){
		Set<ArrayList<String>> result = new HashSet<ArrayList<String>>();
		int length = this.itemSet.size();
        int i;
        
        for( i=1;i<(1<<length);i++){
			Set<String> subSet = new HashSet<String>();
            for(int j=0;j<length;j++){
                
                if((i&(1<<j))!=0){
                	subSet.add(this.itemSet.get(j));                  
                }
            }
            result.add(new ArrayList (subSet));	
              
        }    
				
		return new ArrayList<ArrayList<String>>(result);
	}
	
    /*新增Item到 List*/
	public void addItem(String item){
		itemSet.add(item);
	}
	/*取得所有Item的集合*/
	public ArrayList<String> getItemSet(){
		return itemSet;
	}
	
	
	/*取得 support值*/
	public int getSupport(){
		return support;
	}
	/*設定support值*/
	public void setSupport(int s){
		support=s;
	}
	
	/*override equlas , 用於hashset判斷*/
	public boolean equals(Object obj){
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Candidate other = (Candidate) obj;
        
        if (this.itemSet.size()!=other.itemSet.size()) {
            return false;
        }
        
        for(String item : itemSet){
        	if(other.getItemSet().indexOf(item)<0)
        		return false;
        }

        return true;
	}
	
	/*定義 hashcode , 用於hashset判斷*/
    public int hashCode() {
        return Objects.hashCode(this.itemSet);
    }
    
	public String toString(){
		return itemSet.toString() + "sup=" +support;
	}
}
