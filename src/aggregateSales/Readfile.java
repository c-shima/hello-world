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

		String TemporaryStr; int Errormode = 0;

		/*支店定義ファイルの呼び出し
		 *
		 */

		System.out.println("指定されたファイルを探しています・・・");
		File SubshopDefine = new File(args[0]);

		String[] SubshopTitle = new String[999];
		int[] SubshopSales = new int[999];

		if(!SubshopDefine.exists()){
			System.out.println("支店定義ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}
		try {
			FileReader SubshopReader = new FileReader(SubshopDefine);
			BufferedReader SubshopStocker = new BufferedReader(SubshopReader);
			while((TemporaryStr = SubshopStocker.readLine())  != null) {
				String[] ContType = TemporaryStr.split("\\,");
				if (ContType.length > 2 || ContType[0].length() != 3){
					Errormode++;
				}
				int ShopNo = Integer.parseInt(ContType[0]);
				SubshopTitle[ShopNo] = ContType[1];
				SubshopSales[ShopNo] = 0;
				//System.out.println(ShopNo+":"+SubshopTitle[ShopNo]);
			}
			SubshopStocker.close();
			if (Errormode > 0) {
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

		File GoodsDefine = new File(args[1]);

		String[] GoodsInfoNo = new String[999];
		String[] GoodsTitle = new String[999];
		int[] GoodsSales = new int[999];

		if(!GoodsDefine.exists()){
			System.out.println("商品定義ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}
		try {
			FileReader GoodsReader = new FileReader(GoodsDefine);
			BufferedReader GoodsStocker = new BufferedReader(GoodsReader);
			int whilecnt = 0;
			while((TemporaryStr = GoodsStocker.readLine())  != null) {
				String[] ContType = TemporaryStr.split("\\,");
				if (ContType.length > 2 || ContType[0].length() != 8){
					Errormode ++;
				}
				GoodsInfoNo[whilecnt] = ContType[0];
				GoodsTitle[whilecnt] = ContType[1];
				GoodsSales[whilecnt] = 0;
				//System.out.println(whilecnt+":"+GoodsInfoNo[whilecnt]+","+GoodsTitle[whilecnt]);
				whilecnt++;
			}
			GoodsStocker.close();
			if (Errormode > 0) {
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

		File SalesDir = new File(args[2]);
		String[] SalesDirList = SalesDir.list();
		ArrayList<String> SalesFilesSort = new ArrayList<String>();
		for (int a = 0; a < SalesDirList.length; a++){
			//System.out.println(SalesDirList[a]);
			String[] FileType = SalesDirList[a].split("\\.");
			if (FileType.length > 0) {
				if (FileType[1].endsWith("rcd")){
					SalesFilesSort.add(SalesDirList[a]);
				}
			}
		}
		if (SalesFilesSort.size() == 0) {
			System.out.println("商品ファイルが存在しません。");
			System.out.println("処理を終了します。");
			return;
		}


		Collections.sort(SalesFilesSort);
		int Nowno = 0;
		for (int a = 0; a< SalesFilesSort.size(); a++){
			String CheckNo = SalesFilesSort.get(a);
			String[] FileSortNoCheck = CheckNo.split("\\.");
			if (a == 0) {
				Nowno = Integer.parseInt(FileSortNoCheck[0]) + 1;
			}
			if (a > 0) {
				if (Nowno == Integer.parseInt(FileSortNoCheck[0])){
					Nowno++;
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
			String [][] TemporaryDisposeStr = new String[SalesFilesSort.size()][3];
			for (int a = 0; a < SalesFilesSort.size(); a++){
			try {
				int whilecnt = 0;
				File SalesDefine = new File(args[2]+"\\"+SalesFilesSort.get(a));
				FileReader SalesShopCheck = new FileReader(SalesDefine);
				BufferedReader SalesInfo = new BufferedReader(SalesShopCheck);
				while((TemporaryStr = SalesInfo.readLine()) != null){
					if (whilecnt ==3 ) {
						System.out.println(SalesDefine+"のフォーマットが不正です。\r\n"
								+ "処理を終了します。");
						return;
					}
					TemporaryDisposeStr[a][whilecnt] = TemporaryStr;
					whilecnt++;
				}
				if (a+1 == SalesFilesSort.size() ){
					SalesInfo.close();
				}
			} catch (IOException e) {
				System.out.println(e);
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
			int NowShop = 0;
			int NowGoods = 0;
				for (int i = 0; i < SalesFilesSort.size(); i++){
					/////////////////////////////////////////////////////////
						NowShop = Integer.parseInt(TemporaryDisposeStr[i][0]);
						//System.out.println(NowShop);
						if (SubshopTitle[NowShop] == null) {
							System.out.println(TemporaryDisposeStr[i][0] +"の支店コードが不正です。\r\n"
									+ "処理を終了します。");
							return;
						}
					////////////////////////////////////////////////////////////////
						for (int b=0; b < GoodsInfoNo.length; b++){
							//System.out.println(GoodsInfoNo[b] +"="+ TemporaryDisposeStr[i][1]);
							if (TemporaryDisposeStr[i][1].equals(GoodsInfoNo[b])){
								NowGoods = b;
								break;
							}
							if (b+1 == GoodsInfoNo.length ) {
								System.out.println(TemporaryDisposeStr[i][1] +"の商品コードが不正です。\r\n"
										+ "処理を終了します。");
								return;
							}
						}
					////////////////////////////////////////////////////////////////////
						SubshopSales[NowShop] += Integer.parseInt(TemporaryDisposeStr[i][2]);
						GoodsSales[NowGoods] += Integer.parseInt(TemporaryDisposeStr[i][2]);
					////////////////////////////////////////////////////////////////////////
				}
			int[] AlreadyShopWrite = new int[999]; // 0 == off , 1 == on
			int[] AlreadyGoodsWrite = new int[999]; // 0 == off , 1 == on
			ArrayList<Integer> SalesShopRank = new ArrayList<Integer>();
			ArrayList<Integer> SalesGoodsRank = new ArrayList<Integer>();
			for (int c= 0; c < SubshopSales.length; c++){
				if (SubshopSales[c] == 0){continue;}
				if (SubshopSales[c] > 999999999){
					System.out.println("合計金額が10桁を超えました\r\n"
							+ "処理を終了します。");
					return;
				}
				SubshopSales[c] *= -1;
				SalesShopRank.add(SubshopSales[c]);
			}
			Collections.sort(SalesShopRank);
			File ShopRank = new File(args[3]+"\\branch.out");
			try {
				FileWriter ShopOutputStock = new FileWriter(ShopRank);
				BufferedWriter ShopRankOutput = new BufferedWriter(ShopOutputStock);
				for (int d = 0; d < SalesShopRank.size(); d++){
					for (int f = 0; f < SubshopSales.length; f++){
						int TemporaryInt = SalesShopRank.get(d);
						if (SubshopSales[f] == TemporaryInt && AlreadyShopWrite[f] == 0){
							TemporaryStr = String.valueOf(f);
							TemporaryInt *= -1;
							if (TemporaryStr.length() == 2 ){	TemporaryStr = "0"+TemporaryStr;}
							if (TemporaryStr.length() == 1 ){	TemporaryStr = "00"+TemporaryStr;}
							ShopRankOutput.write(TemporaryStr+","+SubshopTitle[f]+","+TemporaryInt+"\r\n");
							AlreadyShopWrite[f] = 1;
						}
					}
				}
				ShopRankOutput.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			for (int c= 0; c < GoodsSales.length; c++){
				if (GoodsSales[c] == 0){continue;}
				GoodsSales[c] *= -1;
				SalesGoodsRank.add(GoodsSales[c]);
			}
			Collections.sort(SalesGoodsRank);
			File GoodsRank = new File(args[3]+"\\commodity.out");
			try {
				FileWriter GoodsOutputStock = new FileWriter(GoodsRank);
				BufferedWriter GoodsRankOutput = new BufferedWriter(GoodsOutputStock);
				for (int d = 0; d < SalesGoodsRank.size(); d++){
					for (int f = 0; f < GoodsSales.length; f++){
						int TemporaryInt = SalesGoodsRank.get(d);
						if (GoodsSales[f] == TemporaryInt && AlreadyGoodsWrite[f] == 0){
							TemporaryInt *= -1;
							GoodsRankOutput.write(GoodsInfoNo[f]+","+GoodsTitle[f]+","+TemporaryInt+"\r\n");
							AlreadyGoodsWrite[f] = 1;
						}
					}
				}
				GoodsRankOutput.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			System.out.println("正常にファイルを出力しました。\r\n"
					+ "処理を終了します。");
			return;
		}
	}
