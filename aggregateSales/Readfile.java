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


	String dairistr = ("kamaboko\nninnkku\nsyouyu\nsoisousu");
	public static void main(String[] args){

		String temporaryStr; int errorMode = 0;

		/*支店定義ファイルの呼び出し
		 *
		 */

		System.out.println("指定されたファイルを探しています・・・");
		File subshopDefine = new File(args[0]);

		String[] subshopTitle = new String[999];
		int[] subshopSales = new int[999];

		if(!subshopDefine.exists()){
			System.out.println("支店定義ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}
		try {
			FileReader subshopReader = new FileReader(subshopDefine);
			BufferedReader subshopStocker = new BufferedReader(subshopReader);
			while((temporaryStr = subshopStocker.readLine())  != null) {
				String[] contType = temporaryStr.split("\\,");
				if (contType.length > 2 || contType[0].length() != 3){
					errorMode++;
				}
				int shopNo = Integer.parseInt(contType[0]);
				subshopTitle[shopNo] = contType[1];
				subshopSales[shopNo] = 0;
				//System.out.println(ShopNo+":"+SubshopTitle[ShopNo]);
			}
			subshopStocker.close();
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

		File goodsDefine = new File(args[1]);

		String[] goodsInfoNo = new String[999];
		String[] goodsTitle = new String[999];
		int[] goodsSales = new int[999];

		if(!goodsDefine.exists()){
			System.out.println("商品定義ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}
		try {
			FileReader goodsReader = new FileReader(goodsDefine);
			BufferedReader goodsStocker = new BufferedReader(goodsReader);
			int whileCnt = 0;
			while((temporaryStr = goodsStocker.readLine())  != null) {
				String[] contType = temporaryStr.split("\\,");
				if (contType.length > 2 || contType[0].length() != 8){
					errorMode ++;
				}
				goodsInfoNo[whileCnt] = contType[0];
				goodsTitle[whileCnt] = contType[1];
				goodsSales[whileCnt] = 0;
				//System.out.println(whilecnt+":"+GoodsInfoNo[whilecnt]+","+GoodsTitle[whilecnt]);
				whileCnt++;
			}
			goodsStocker.close();
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
			//System.out.println(SalesDirList[a]);
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
				FileReader salesShopCheck = new FileReader(salesDefine);
				BufferedReader salesInfo = new BufferedReader(salesShopCheck);
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
			int nowShop = 0;
			int nowGoods = 0;
				for (int i = 0; i < salesFilesSort.size(); i++){
					/////////////////////////////////////////////////////////
						nowShop = Integer.parseInt(temporaryDisposeStr[i][0]);
						//System.out.println(NowShop);
						if (subshopTitle[nowShop] == null) {
							System.out.println(temporaryDisposeStr[i][0] +"の支店コードが不正です。\r\n"
									+ "処理を終了します。");
							return;
						}
					////////////////////////////////////////////////////////////////
						for (int b=0; b < goodsInfoNo.length; b++){
							//System.out.println(GoodsInfoNo[b] +"="+ TemporaryDisposeStr[i][1]);
							if (temporaryDisposeStr[i][1].equals(goodsInfoNo[b])){
								nowGoods = b;
								break;
							}
							if (b+1 == goodsInfoNo.length ) {
								System.out.println(temporaryDisposeStr[i][1] +"の商品コードが不正です。\r\n"
										+ "処理を終了します。");
								return;
							}
						}
					////////////////////////////////////////////////////////////////////
						subshopSales[nowShop] += Integer.parseInt(temporaryDisposeStr[i][2]);
						goodsSales[nowGoods] += Integer.parseInt(temporaryDisposeStr[i][2]);
					////////////////////////////////////////////////////////////////////////
				}
			int[] alreadyShopWrite = new int[999]; // 0 == off , 1 == on
			int[] alreadyGoodsWrite = new int[999]; // 0 == off , 1 == on
			ArrayList<Integer> salesShopRank = new ArrayList<Integer>();
			ArrayList<Integer> salesGoodsRank = new ArrayList<Integer>();
			for (int c= 0; c < subshopSales.length; c++){
				if (subshopSales[c] == 0){continue;}
				if (subshopSales[c] > 999999999){
					System.out.println("合計金額が10桁を超えました\r\n"
							+ "処理を終了します。");
					return;
				}
				subshopSales[c] *= -1;
				salesShopRank.add(subshopSales[c]);
			}
			Collections.sort(salesShopRank);
			File shopRank = new File(args[3]+"\\branch.out");
			try {
				FileWriter shopOutputStock = new FileWriter(shopRank);
				BufferedWriter shopRankOutput = new BufferedWriter(shopOutputStock);
				for (int d = 0; d < salesShopRank.size(); d++){
					for (int f = 0; f < subshopSales.length; f++){
						int temporaryInt = salesShopRank.get(d);
						if (subshopSales[f] == temporaryInt && alreadyShopWrite[f] == 0){
							temporaryStr = String.valueOf(f);
							temporaryInt *= -1;
							if (temporaryStr.length() == 2 ){	temporaryStr = "0"+temporaryStr;}
							if (temporaryStr.length() == 1 ){	temporaryStr = "00"+temporaryStr;}
							shopRankOutput.write(temporaryStr+","+subshopTitle[f]+","+temporaryInt+"\r\n");
							alreadyShopWrite[f] = 1;
						}
					}
				}
				shopRankOutput.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			for (int c= 0; c < goodsSales.length; c++){
				if (goodsSales[c] == 0){continue;}
				goodsSales[c] *= -1;
				salesGoodsRank.add(goodsSales[c]);
			}
			Collections.sort(salesGoodsRank);
			File goodsRank = new File(args[3]+"\\commodity.out");
			try {
				FileWriter goodsOutputStock = new FileWriter(goodsRank);
				BufferedWriter goodsRankOutput = new BufferedWriter(goodsOutputStock);
				for (int d = 0; d < salesGoodsRank.size(); d++){
					for (int f = 0; f < goodsSales.length; f++){
						int temporaryInt = salesGoodsRank.get(d);
						if (goodsSales[f] == temporaryInt && alreadyGoodsWrite[f] == 0){
							temporaryInt *= -1;
							goodsRankOutput.write(goodsInfoNo[f]+","+goodsTitle[f]+","+temporaryInt+"\r\n");
							alreadyGoodsWrite[f] = 1;
						}
					}
				}
				goodsRankOutput.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			System.out.println("正常にファイルを出力しました。\r\n"
					+ "処理を終了します。");
			return;
		}
	}
