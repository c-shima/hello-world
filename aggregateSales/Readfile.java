package aggregateSales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;



public class Readfile {

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
		File branchDefine = new File(args[0]);

		String[] branchTitle = new String[999];
		int[] branchSales = new int[999];

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
				int branchNo = Integer.parseInt(contType[0]);
				branchTitle[branchNo] = contType[1];
				branchSales[branchNo] = 0;
			}
			branchStocker.close();
			if (errorMode > 0) {
				System.out.println("支店定義ファイルのフォーマットが不正です\r\n"
					+ "処理を終了します。");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*商品ファイルの呼び出し
		 *
		 */

		File commodityDefine = new File(args[1]);

		String[] commodityInfoNo = new String[999];
		String[] commodityTitle = new String[999];
		int[] commoditySales = new int[999];

		if(!commodityDefine.exists()){
			System.out.println("商品定義ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}
		try {
			FileReader commodityReader = new FileReader(commodityDefine);
			BufferedReader commodityStocker = new BufferedReader(commodityReader);
			int whileCnt = 0;
			while((temporaryStr = commodityStocker.readLine())  != null) {
				String[] contType = temporaryStr.split("\\,");
				if (contType.length > 2 || contType[0].length() != 8){
					errorMode ++;
				}
				commodityInfoNo[whileCnt] = contType[0];
				commodityTitle[whileCnt] = contType[1];
				commoditySales[whileCnt] = 0;
				whileCnt++;
			}
			commodityStocker.close();
			if (errorMode > 0) {
				System.out.println("商品定義ファイルのフォーマットが不正です\r\n"
					+ "処理を終了します。");
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
		for (int a = 0; a < salesDirList.length; a++){
			String[] fileType = salesDirList[a].split("\\.");
			if (fileType.length > 0) {
				if (fileType[1].endsWith("rcd")){
					salesFilesSort.add(salesDirList[a]);
				}
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
					System.out.println("売上ファイル名が連番になっていません。\r\n"
							+ "処理を終了します。");
					return;
				}
			}
		}
		/*支店別集計
		 *
		 */
		System.out.println("出力中・・・");




			//System.out.println(SalesDefine);
			String [][] temporaryDisposeStr = new String[salesFilesSort.size()][3];
			for (int a = 0; a < salesFilesSort.size(); a++){
			try {
				int whileCnt = 0;
				File salesDefine = new File(args[2]+"\\"+salesFilesSort.get(a));
				FileReader salesBranchCheck = new FileReader(salesDefine);
				BufferedReader salesInfo = new BufferedReader(salesBranchCheck);
				while((temporaryStr = salesInfo.readLine()) != null){
					if (whileCnt ==3 ) {
						System.out.println(salesDefine+"のフォーマットが不正です。\r\n"
								+ "処理を終了します。");
						return;
					}
					temporaryDisposeStr[a][whileCnt] = temporaryStr;
					whileCnt++;
				}
				if (a+1 == salesFilesSort.size() ){
					salesInfo.close();
				}
			} catch (IOException e) {
				System.out.println(e);
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
			int nowBranch = 0;
			int nowCommodity = 0;
				for (int i = 0; i < salesFilesSort.size(); i++){
					/////////////////////////////////////////////////////////
						nowBranch = Integer.parseInt(temporaryDisposeStr[i][0]);
						if (branchTitle[nowBranch] == null) {
							System.out.println(temporaryDisposeStr[i][0] +"の支店コードが不正です。\r\n"
									+ "処理を終了します。");
							return;
						}
					////////////////////////////////////////////////////////////////
						for (int b=0; b < commodityInfoNo.length; b++){
							if (temporaryDisposeStr[i][1].equals(commodityInfoNo[b])){
								nowCommodity = b;
								break;
							}
							if (b+1 == commodityInfoNo.length ) {
								System.out.println(temporaryDisposeStr[i][1] +"の商品コードが不正です。\r\n"
										+ "処理を終了します。");
								return;
							}
						}
					////////////////////////////////////////////////////////////////////
						branchSales[nowBranch] += Integer.parseInt(temporaryDisposeStr[i][2]);
						commoditySales[nowCommodity] += Integer.parseInt(temporaryDisposeStr[i][2]);
					////////////////////////////////////////////////////////////////////////
				}
			int[] alreadyBranchWrite = new int[999]; // 0 == off , 1 == on
			int[] alreadyCommodityWrite = new int[999]; // 0 == off , 1 == on
			ArrayList<Integer> salesBranchRank = new ArrayList<Integer>();
			ArrayList<Integer> salesCommodityRank = new ArrayList<Integer>();
			for (int c= 0; c < branchSales.length; c++){
				if (branchSales[c] == 0){continue;}
				if (branchSales[c] > 999999999){
					System.out.println("合計金額が10桁を超えました\r\n"
							+ "処理を終了します。");
					return;
				}
				branchSales[c] *= -1;
				salesBranchRank.add(branchSales[c]);
			}
			Collections.sort(salesBranchRank);
			File branchRank = new File(args[3]+"\\branch.out");
			try {
				FileWriter branchOutputStock = new FileWriter(branchRank);
				BufferedWriter branchRankOutput = new BufferedWriter(branchOutputStock);
				for (int d = 0; d < salesBranchRank.size(); d++){
					for (int f = 0; f < branchSales.length; f++){
						int temporaryInt = salesBranchRank.get(d);
						if (branchSales[f] == temporaryInt && alreadyBranchWrite[f] == 0){
							temporaryStr = String.valueOf(f);
							temporaryInt *= -1;
							if (temporaryStr.length() == 2 ){	temporaryStr = "0"+temporaryStr;}
							if (temporaryStr.length() == 1 ){	temporaryStr = "00"+temporaryStr;}
							branchRankOutput.write(temporaryStr+","+branchTitle[f]+","+temporaryInt+"\r\n");
							alreadyBranchWrite[f] = 1;
						}
					}
				}
				branchRankOutput.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			for (int c= 0; c < commoditySales.length; c++){
				if (commoditySales[c] == 0){continue;}
				commoditySales[c] *= -1;
				salesCommodityRank.add(commoditySales[c]);
			}
			Collections.sort(salesCommodityRank);
			File commodityRank = new File(args[3]+"\\commodity.out");
			try {
				FileWriter commodityOutputStock = new FileWriter(commodityRank);
				BufferedWriter commodityRankOutput = new BufferedWriter(commodityOutputStock);
				for (int d = 0; d < salesCommodityRank.size(); d++){
					for (int f = 0; f < commoditySales.length; f++){
						int temporaryInt = salesCommodityRank.get(d);
						if (commoditySales[f] == temporaryInt && alreadyCommodityWrite[f] == 0){
							temporaryInt *= -1;
							commodityRankOutput.write(commodityInfoNo[f]+","+commodityTitle[f]+","+temporaryInt+"\r\n");
							alreadyCommodityWrite[f] = 1;
						}
					}
				}
				commodityRankOutput.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			System.out.println("正常にファイルを出力しました。\r\n"
					+ "処理を終了します。");
			return;
		}
	}
