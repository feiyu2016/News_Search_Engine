package com.ajax;

import java.util.ArrayList;
import java.util.Random;

public class Select <E extends Comparable<E> > {

	void swap(ArrayList<E> a, int i, int j) {
		E tmp = a.get(i);
		a.set(i, a.get(j));
		a.set(j, tmp);
	}

	int median5(ArrayList<E> a, int offset) {
		E a0 = a.get(0 + offset);
		E a1 = a.get(1 + offset);
		E a2 = a.get(2 + offset);
		E a3 = a.get(3 + offset);
		E a4 = a.get(4 + offset);

		//System.out.println("median5 a:"+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+" "+a[4]+" "
		//                       +a[5]+" "+a[6]+" "+a[7]+" "+a[8]+" "+a[9]);
		if (a1.compareTo(a0) < 0) {
			E tmp = a0;
			a0 = a1;
			a1 = tmp;
		}
		if (a2.compareTo(a0) < 0) {
			E tmp = a0;
			a0 = a2;
			a2 = tmp;
		}
		if (a3.compareTo(a0) < 0) {
			E tmp = a0;
			a0 = a3;
			a3 = tmp;
		}
		if (a4.compareTo(a0) < 0) {
			E tmp = a0;
			a0 = a4;
			a4 = tmp;
		}
		if (a2.compareTo(a1) < 0) {
			E tmp = a1;
			a1 = a2;
			a2 = tmp;
		}

		if (a3.compareTo(a1) < 0) {
			E tmp = a1;
			a1 = a3;
			a3 = tmp;
		}

		if (a4.compareTo(a1) < 0) {
			E tmp = a1;
			a1 = a4;
			a4 = tmp;
		}

		if (a3.compareTo(a2) < 0) {
			E tmp = a3;
			a3 = a2;
			a2 = tmp;
		}

		if (a4.compareTo(a2) < 0) {
			E tmp = a4;
			a4 = a2;
			a2 = tmp;
		}

		//System.out.println("median5 a swap end:"+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+" "+a[4]+" "
		//        +a[5]+" "+a[6]+" "+a[7]+" "+a[8]+" "+a[9]);

		if (a2 == a.get(0 + offset))
			return 0;
		if (a2 == a.get(1 + offset))
			return 1;
		if (a2 == a.get(2 + offset))
			return 2;
		if (a2 == a.get(3 + offset))
			return 3;
		// else if (a2 == a[4])
		return 4;
	}

	int partition(ArrayList<E> a, int size, int pivot, int offset) {
		E pivotValue = a.get(pivot);
		//System.out.println("pivotValue:"+pivotValue);
		swap(a, pivot, size - 1 + offset);
		int storePos = offset;
		for (int loadPos = offset; loadPos < (size - 1 + offset); loadPos++) {
			if (a.get(loadPos).compareTo(pivotValue) < 0) {
				swap(a, loadPos, storePos);
				storePos++;
			}
		}
		swap(a, storePos, size - 1 + offset);
		return (storePos);
	}

	void select(ArrayList<E> a, int size, int k, int offset) {
		if (size < 5) {
			//System.out.println("size:"+size);
			for (int i = offset; i < (size + offset); i++)
				for (int j = i + 1; j < (size + offset); j++)
					if (a.get(j).compareTo(a.get(i)) < 0)
						swap(a, i, j);
			return;
		}

		int groupNum = 0;
		int group = 0;

		for (; groupNum * 5 <= size - 5; group += 5, groupNum++) {

			int med5 = median5(a, group + offset);
			//System.out.println("med5:"+med5);
			//System.out.println("i:"+(group+med5+offset)+" j:"+(groupNum+offset));
			swap(a, group + med5 + offset, groupNum + offset);
		}

		int numMedians = size / 5;
		//		 Index of median of medians
		int MOMIdx = numMedians / 2;
		//System.out.println("numMedians:"+numMedians);
		select(a, numMedians, MOMIdx + offset, offset);

		int newMOMIdx = partition(a, size, MOMIdx + offset, offset);
		if (k != newMOMIdx) {
			if (k < newMOMIdx) {
				select(a, newMOMIdx - offset, k, offset);
			} else /* if (k > newMOMIdx) */{
				//System.out.println("change offset to:"+(newMOMIdx + 1));
				select(a, size - newMOMIdx - 1 + offset, k, newMOMIdx + 1);
			}
		}
	}

	void selectRandom(ArrayList<E> a, int size, int k, int offset) {
		if (size < 5) {
			//if (size < 10000) {
			for (int i = offset; i < (size + offset); i++)
				for (int j = i + 1; j < (size + offset); j++)
					if (a.get(j).compareTo(a.get(i)) < 0)
						swap(a, i, j);
			return;
		}

		Random rand = new Random();

		//System.out.println("size:"+size+" rand:"+ (rand.nextInt(size+1) - 1) );
		int pivotIdx = partition(a, size, rand.nextInt(size) + offset, offset);
		if (k != pivotIdx) {
			if (k < pivotIdx) {
				selectRandom(a, pivotIdx - offset, k, offset);
			} else // if (k > pivotIdx) 
			{
				//System.out.println("k > pivotIdx");
				//System.arraycopy(src, srcPos, dest, destPos, length);
				selectRandom(a, size - pivotIdx - 1 + offset, k, pivotIdx + 1);
			}
		}
	}

	/*static int SEED = 352302;

	 static ArrayList<WordFreq> makeRandomArray(ArrayList<WordFreq> a, int size) {
	 //srand(SEED);
	 ArrayList<WordFreq> b = new ArrayList<WordFreq>(a.size());
	 
	 for (int i=0; i<size; i++) {
	 a.add(new WordFreq("a",(new Random().nextInt(SEED))));
	 b.add(a.get(i));
	 }
	 
	 return b;
	 }*/

	/*static void testSelect()
	 {
	 int size = 89863;//Integer.parseInt(argv[1]);
	 //int[] a = //{1865819834,817567258,960949193,1534838205,1498519326,2023857581,1841760922,1410518637,2017700260,916787095};
	 //{1865819834,817567258,960949193,1534838205,1498519326,2023857581,1841760922,1410518637,2017700260,916787095,1306509860,1345598418,545959641,1443739232,1700619141,846286449,1735819116,94674225,278872549,1875137612,1916224317,29921721,126887238,875601032,1984296294,1569316714,219165175,1112327724,1853512378,810840826,432779526,1571848564,1628408084,1393728719,959203121,979443762,1270102652,653480395,242478751,1140319264,1570267491,1548988612,338434034,2116227132,845244196,2039053175,815029934,433579664,2133727400,1093902483,161233628,1902468069,1123824204,288120867,630585454,960636850,1857437581,849750629,2072964574,1563466311,1660591455,358260453,987831227,1141515891,1751989172,1947034348,2120959654,874608177,453031095,215954757,2014927441,2023298586,1764943369,205877828,1992042071,462703917,97447355,659588357,896283581,83691108,1753490840,1057517210,1986159177,729831396,1345638077,469260983,1690468246,1055592010,1319011613,1615949173,471574673,832119420,1974209626,1459405900,1973635312,1578715150,1258956600,1947111318,305839679,1711987695};
	 //		//new int[size];
	 ArrayList<WordFreq> a = new ArrayList<WordFreq>(size);
	 
	 ArrayList<WordFreq> b = makeRandomArray(a, size);
	 //{1865819834,817567258,960949193,1534838205,1498519326,2023857581,1841760922,1410518637,2017700260,916787095};
	 //{1865819834,817567258,960949193,1534838205,1498519326,2023857581,1841760922,1410518637,2017700260,916787095,1306509860,1345598418,545959641,1443739232,1700619141,846286449,1735819116,94674225,278872549,1875137612,1916224317,29921721,126887238,875601032,1984296294,1569316714,219165175,1112327724,1853512378,810840826,432779526,1571848564,1628408084,1393728719,959203121,979443762,1270102652,653480395,242478751,1140319264,1570267491,1548988612,338434034,2116227132,845244196,2039053175,815029934,433579664,2133727400,1093902483,161233628,1902468069,1123824204,288120867,630585454,960636850,1857437581,849750629,2072964574,1563466311,1660591455,358260453,987831227,1141515891,1751989172,1947034348,2120959654,874608177,453031095,215954757,2014927441,2023298586,1764943369,205877828,1992042071,462703917,97447355,659588357,896283581,83691108,1753490840,1057517210,1986159177,729831396,1345638077,469260983,1690468246,1055592010,1319011613,1615949173,471574673,832119420,1974209626,1459405900,1973635312,1578715150,1258956600,1947111318,305839679,1711987695};//makeRandomArray(a, size);
	 //a = b;
	 
	 long selectBegin = System.currentTimeMillis();
	 select(a, size, size/2,0);

	 //System.out.println("out select a:"+a[0]+" "+a[1]+" "+a[2]+" "+a[3]+" "+a[4]+" "
	 //                       +a[5]+" "+a[6]+" "+a[7]+" "+a[8]+" "+a[9]);
	 long selectEnd = System.currentTimeMillis();
	 WordFreq selectResult = a.get(size/2);

	 a = b;
	 //makeRandomArray(a, size);
	 long selectRandomBegin = System.currentTimeMillis();
	 selectRandom(a, size, size/2,0);
	 long selectRandomEnd = System.currentTimeMillis();
	 WordFreq selectRandomResult = a.get(size/2);

	 //a = b;
	 //makeRandomArray(a, size);
	 //long sortBegin = System.currentTimeMillis();
	 //java.util.Arrays.so,0,size);
	 //long sortEnd = System.currentTimeMillis();
	 //WordFreq sortResult = b[size/2];

	 if (!(selectResult.freq == selectRandomResult.freq )) {
	 System.out.println( "Inconsistent result" );
	 //System.out.println( "sortResult:"+sortResult );
	 System.out.println( "selectResult:"+selectResult );
	 }

	 System.out.println( "Select time (ms): " + (selectEnd-selectBegin));
	 System.out.println( "SelectRandom time (ms): " + (selectRandomEnd-selectRandomBegin));
	 //System.out.println( "Sort time (ms): " + (sortEnd-sortBegin));
	 }*/

	/*static void testMedian5()
	 {
	 int[] a = {1865819834,817567258,960949193,1534838205,1498519326,2023857581,1841760922,1410518637,2017700260,916787095};
	 int med5 = median5(a,0);
	 System.out.println( "med5: " + med5);
	 }*/

	/**
	 * @param args
	 */
	/*public static void main(String[] argv) {
	 //testMedian5();
	 //testSelect();
	 }*/
}
