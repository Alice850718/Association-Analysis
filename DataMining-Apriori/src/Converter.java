import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Converter {

	public int  totalOfTrasaction =0;
	
	/*將Transaction的List轉換成ARRF檔*/
	public void ToArrfFile(ArrayList<Transaction> dataSet,int numOfDiffItem,String outFileName) throws Exception{
		
			FileWriter fw = new FileWriter(outFileName+".arff");
			fw.write("@RELATION transaction \r\n");
			
			
			
			for(int i =1; i<=numOfDiffItem ; i++ )
				fw.append("@ATTRIBUTE 'Item"+ i +"'  {  t} \r\n");
				
			fw.append("@DATA\r\n");
			
			for(int i=0; i < dataSet.size() ; i++ ){
				Transaction tr = dataSet.get(i);
				
				for(int j=1; j <= numOfDiffItem ;j++){
					if(tr.getItemsSet().contains("Item"+j)) 
						fw.append("t");
					else
						fw.append("?");
					
					if(j<=numOfDiffItem-1)
						fw.append(",");
				
				}
				fw.append("\r\n");
				
			}
			
			fw.flush();
			fw.close();
		
	}
	
	/*讀取IBM data Generator 產生的資料並存成 Transaction List*/
 	public ArrayList<Transaction> ToTransactionList(String filePath) throws Exception{
        
		FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<Transaction> List = new ArrayList<Transaction>();
        Scanner scanner = null;
        String line = null;          
        
        System.out.println("---讀取 " + filePath + "---");
        
        while( (line=br.readLine()) !=null)
        {
        	scanner = new Scanner(line);
	        int  numOfTransaction = scanner.nextInt();
	        totalOfTrasaction = totalOfTrasaction + numOfTransaction;
	        
	        for(int i=1 ; i <= numOfTransaction;i++ ){
	        	Transaction transaction = new Transaction();
		        int numOfItem = scanner.nextInt();
		        for(int j =0 ; j < numOfItem ; j++ ){
		        	String item = scanner.next();
		        	transaction.addItem("Item"+item);
		        }
		        List.add(transaction);  
	        }
        }
        br.close();
        
        
        
        System.out.println("number of Transaction =" + totalOfTrasaction);
        System.out.println("\n");
        return List;
				
	}

}
