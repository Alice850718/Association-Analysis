import java.util.ArrayList;

public class Transaction {
	
    private ArrayList<String> itemList = new ArrayList<String>();
    public int numberOfItem = 0;
    
    public Transaction (){
    	 	
    }
    
    public boolean contains(ArrayList<String> set){	
    	
    	for(String item :set){
    		if(!itemList.contains(item))
    			return false;
    	}
    	return true;
    }
    
    public void  addItem (String item){
	 	itemList.add(item);
	 	numberOfItem ++;
    }
    
    public String  getItem (int index){
	 	return itemList.get(index);

    }
    
    public ArrayList<String>  getItemsSet (){
	 	return itemList;

    }
}
