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

	/*コマンドライン引数
	 * 「支店定義ファイルのディレクトリ 商品定義ファイルのディレクトリ (改行はしない)
	 * 売上ファイルのディレクトリ(複数個所に入れている場合は一箇所にまとめていただく) 支店別集計ファイルの保存先
	 * (改行はしない) 商品別集計ファイルの保存先」
	 * を書いてください。
	 *
	 */


	public static void main(String[] args){

		String temporaryStr; int errorMode = 0;

		/*支店定義ファイルの呼び出し
		 *
		 */

		System.out.println("指定されたファイルを探しています・・・");
		HashMap<String,String> branchName = new HashMap<String,String>();
		HashMap<String,Integer> branchSales = new HashMap<String,Integer>();
		File branchDefine = new File(args[0]);
		if(!branchDefine.exists()){
			System.out.println("支店定義ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}
		try {
			FileReader branchReader = new FileReader(branchDefine);
			BufferedReader branchStocker = new BufferedReader(branchReader);
			while((temporaryStr = branchStocker.readLine())  != null) {
				String[] contType = temporaryStr.split("\\,");
				if (contType.length > 2 || contType[0].length() != 3){
					errorMode++;
				}
				branchName.put(contType[0], contType[1]);
				branchSales.put(contType[0],0);
			}
			branchStocker.close();
			if (errorMode > 0) {
				System.out.println("支店定義ファイルのフォーマットが不正です\r\n処理を終了します。");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*商品ファイルの呼び出し
		 *
		 */

		File commodityDefine = new File(args[1]);
		HashMap<String,String> commodityName = new HashMap<String,String>();
		HashMap<String,Integer> commoditySales = new HashMap<String,Integer>();
		if(!commodityDefine.exists()){
			System.out.println("商品定義ファイルが存在しません。\r\n処理を終了します。");
			return;
		}
		try {
			FileReader commodityReader = new FileReader(commodityDefine);
			BufferedReader commodityStocker = new BufferedReader(commodityReader);
			while((temporaryStr = commodityStocker.readLine())  != null) {
				String[] contType = temporaryStr.split("\\,");
				if (contType.length > 2 || contType[0].length() != 8){
					errorMode ++;
				}
				commodityName.put(contType[0], contType[1]);
				commoditySales.put(contType[0], 0);
			}
			commodityStocker.close();
			if (errorMode > 0) {
				System.out.println("商品定義ファイルのフォーマットが不正です\r\n処理を終了します。");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*売上ファイルの呼び出し
		 *
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
			System.out.println("商品ファイルが存在しません。");
			System.out.println("処理を終了します。");
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
					System.out.println("売上ファイル名が連番になっていません。\r\n処理を終了します。");
					return;
				}
			}
		}
		/*支店別集計
		 *
		 */


		String [] temporaryDStr = new String[3];
		for (int i = 0; i < salesFilesSort.size(); i++){
			try {
				int whileCnt = 0;
				File salesDefine = new File(args[2]+"\\"+salesFilesSort.get(i));
				FileReader salesBranchCheck = new FileReader(salesDefine);
				BufferedReader salesInfo = new BufferedReader(salesBranchCheck);
				while((temporaryStr = salesInfo.readLine()) != null){
					if (whileCnt ==3 ) {
						System.out.println(salesDefine+"のフォーマットが不正です。\r\n処理を終了します。");
						return;
					}
					switch(whileCnt){
					case 0:
						if (branchName.get(temporaryStr) == null){
							System.out.println( salesDefine+"の支店コードが不正です。\r\n処理を終了します。");
							return;}
						temporaryDStr[whileCnt] = temporaryStr;
						break;
					case 1:
						if (commodityName.get(temporaryStr) == null){
							System.out.println( salesDefine+"の商品コードが不正です。\r\n処理を終了します。");
							return;	}
						temporaryDStr[whileCnt] = temporaryStr;
						break;
					case 2:
						
						branchSales.put( temporaryDStr[0] , branchSales.get(temporaryDStr[0]) + Integer.parseInt(temporaryStr));
						commoditySales.put( temporaryDStr[1] , commoditySales.get(temporaryDStr[1]) + Integer.parseInt(temporaryStr));
						if (branchSales.get(temporaryDStr[0]) > 999999999){
							System.out.println("合計金額が10桁を超えました\r\n処理を終了します。");
							return;
						}
						if (commoditySales.get(temporaryDStr[1]) > 999999999){
							System.out.println("合計金額が10桁を超えました\r\n処理を終了します。");
							return;
						}
						break;
					}
					whileCnt++;
				}
				if (i+1 == salesFilesSort.size() ){
					salesInfo.close();
				}
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		
		System.out.println("出力中・・・");
		/*
		 * 合計金額を降順にソート
		 */
		
		List<Map.Entry<String,Integer>> branchEntries = new ArrayList<Map.Entry<String,Integer>>(branchSales.entrySet());
	    Collections.sort(branchEntries, new Comparator<Map.Entry<String,Integer>>() {
	        public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
	                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
	            }
	        });
	    List<Map.Entry<String,Integer>> commodityEntries = new ArrayList<Map.Entry<String,Integer>>(commoditySales.entrySet());
	    Collections.sort(commodityEntries, new Comparator<Map.Entry<String,Integer>>() {
	        public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
	                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
	            }
	        });
	    
	    /*
	     * branch.out に出力
	     */
	    
		File branchRank = new File(args[3]+"\\branch.out");
		try {
			FileWriter branchOutputStock = new FileWriter(branchRank);
			BufferedWriter branchRankOutput = new BufferedWriter(branchOutputStock);
			 for (Entry<String,Integer> s : branchEntries){
				 branchRankOutput.write(s.getKey()+","+branchName.get(s.getKey())+","+s.getValue()+"\r\n");
			}
			branchRankOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File commodityRank = new File(args[3]+"\\commodity.out");
		try {
			FileWriter commodityOutputStock = new FileWriter(commodityRank);
			BufferedWriter commodityRankOutput = new BufferedWriter(commodityOutputStock);
			for (Entry<String,Integer> s : commodityEntries){
				commodityRankOutput.write(s.getKey()+","+commodityName.get(s.getKey())+","+s.getValue()+"\r\n");
			}
			commodityRankOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("正常にファイルを出力しました。\r\n処理を終了します。");
		return;
	}
}

