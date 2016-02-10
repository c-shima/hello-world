package arraytypeAggregateSales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class AtypeReadfile {
static String temporaryStr; static int errorMode = 0;
final static String[] errorMes = {"","ファイルが存在しません。","ファイルのフォーマットが不正です。",
		"ファイル名が連番になっていません。" ,"のフォーマットが不正です。","コードが不正です。",
		"合計金額が10桁を超えました。"};
static AtypeReadfile readMethod = new AtypeReadfile();

	/*
	 * コマンドライン引数
	 * 「支店定義ファイルのディレクトリ 商品定義ファイルのディレクトリ (改行はしない)
	 * 売上ファイルのディレクトリ(複数個所に入れている場合は一箇所にまとめていただく) 支店別集計ファイルの保存先
	 * (改行はしない) 商品別集計ファイルの保存先」
	 * を書いてください。
	 */

	private HashMap<String, String> readData(String readfile , Integer effectiveDigit , Integer elements){
		HashMap<String,String> readName = new HashMap<String,String>();
		File readDefine = new File(readfile);
		if(!readDefine.exists()){
			errorMode = 1;
			return null;
		}
		try {
			FileReader readReader = new FileReader(readDefine);
			BufferedReader readStocker = new BufferedReader(readReader);
			while((temporaryStr = readStocker.readLine())  != null && errorMode == 0) {
				String[] contType = temporaryStr.split("\\,");
				if (contType.length > elements || contType[0].length() != effectiveDigit){
					errorMode = 2;
				}
				readName.put(contType[0], contType[1]);
			}
			readStocker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readName;
	}

	private void outputFile(String fileName , HashMap<String,String> outputName , HashMap<String,Integer> outputSales){
		List<Map.Entry<String,Integer>> Entries = new ArrayList<Map.Entry<String,Integer>>(outputSales.entrySet());
		Collections.sort(Entries, new Comparator<Map.Entry<String,Integer>>() {
			public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
				return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
            	}
        	});
		File outputRank = new File(fileName);
		try {
			FileWriter OutputStock = new FileWriter(outputRank);
			BufferedWriter RankOutput = new BufferedWriter(OutputStock);
			for (Entry<String, Integer> s : Entries){
				if (s.getValue() != 0){
					RankOutput.write(s.getKey()+","+outputName.get(s.getKey())+","+s.getValue()+"\r\n");
				}
			}
		RankOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public static void main(String[] args){

		/*
		 * 支店定義ファイルの呼び出し
		 */
		HashMap<String,String> branchName = new HashMap<String,String>();
		HashMap<String,Integer> branchSales = new HashMap<String,Integer>();
		branchName = readMethod.readData(args[0]+File.separator+"branch.lst", 3 ,2);
			if (errorMode > 0) {
				System.out.println("支店定義"+errorMes[errorMode]);
				return;
			}
			for ( String h : branchName.keySet()){
				branchSales.put(h, 0);
			}
		/*
		 * 商品ファイルの呼び出し
		 */
			HashMap<String,String> commodityName = new HashMap<String,String>();
			HashMap<String,Integer> commoditySales = new HashMap<String,Integer>();
			commodityName = readMethod.readData(args[1]+File.separator+"commodity.lst" , 8 , 2);
			if (errorMode > 0) {
				System.out.println("商品定義"+errorMes[errorMode]);
				return;
			}
			for ( String h : commodityName.keySet()){
				commoditySales.put(h, 0);
			}
		/*
		 * 売上ファイルの呼び出し
		*/

		File salesDir = new File(args[2]);
		String[] salesDirList = salesDir.list();
		ArrayList<String> salesFilesSort = new ArrayList<String>();
		for (int i = 0; i < salesDirList.length; i++){
			if (salesDirList[i].endsWith(".rcd")){
				salesFilesSort.add(salesDirList[i]);
			}
		}
		if (salesFilesSort.size() == 0) {
			System.out.println("売上"+errorMes[1]);
			return;
		}


		Collections.sort(salesFilesSort);
		int nowNo = 0;
		for (int a = 0; a< salesFilesSort.size(); a++){
			String checkNo = salesFilesSort.get(a);
			String[] fileSortNoCheck = checkNo.split("\\.");
			if (a == 0) {
				nowNo = Integer.parseInt(fileSortNoCheck[0]) + 1;
			}
			if (a > 0) {
				if (nowNo == Integer.parseInt(fileSortNoCheck[0])){
					nowNo++;
				} else {
					System.out.println("売上"+errorMes[3]);
					return;
				}
			}
		}
		/*
		 * 支店別集計
		 */
		String [] temporaryDStr = new String[4];
		for (int i = 0; i < salesFilesSort.size(); i++){
			try {
				int whileCnt = 0;
				File salesDefine = new File(args[2]+"\\"+salesFilesSort.get(i));
				FileReader salesBranchCheck = new FileReader(salesDefine);
				BufferedReader salesInfo = new BufferedReader(salesBranchCheck);
				while((temporaryDStr[whileCnt] = salesInfo.readLine()) != null){
					if (whileCnt ==3 ) {
						System.out.println(salesDefine+""+errorMes[4]);
						return;
					}
					whileCnt++;
				}
						if (branchName.get(temporaryDStr[0]) == null){
							System.out.println( salesDefine+"の支店"+errorMes[5]);
						}
						if (commodityName.get(temporaryDStr[1]) == null) {
							System.out.println( salesDefine+"の商品"+errorMes[5]);
						}
						branchSales.put( temporaryDStr[0] , branchSales.get(temporaryDStr[0]) + Integer.parseInt(temporaryDStr[2]));
						commoditySales.put( temporaryDStr[1] , commoditySales.get(temporaryDStr[1]) + Integer.parseInt(temporaryDStr[2]));
						if (branchSales.get(temporaryDStr[0]) > 999999999 || commoditySales.get(temporaryDStr[1]) > 999999999){
							System.out.println(errorMes[6]);
						}
				if (i+1 == salesFilesSort.size() ){
					salesInfo.close();
				}
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		/*
		 * 合計金額を降順にソート
	     * branch.out に出力
	     * commodity.out に出力
	     */
		readMethod.outputFile(args[3]+"\\branch.out",branchName,branchSales);
		readMethod.outputFile(args[4]+"\\commodity.out",commodityName,commoditySales);
		return;
	}
}
