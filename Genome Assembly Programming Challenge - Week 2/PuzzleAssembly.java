import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PuzzleAssembly {

	static List<String> colors = new ArrayList<String>();
	
	static PuzzlePiece ld = null;
	static PuzzlePiece ul = null;
	static PuzzlePiece rd = null;
	static PuzzlePiece ru = null;
	
	public static void main(String[] args) throws IOException {
		
		

		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		List<PuzzlePiece> pieces = new ArrayList<PuzzlePiece>();
		
		for (int j = 0; j < 25; j++) {
			String nextLine = reader.readLine();
			
			nextLine = nextLine.substring(1, nextLine.length()-1);
			
			String sides[] = nextLine.split(",");
			
			for (int m = 0; m < 4; m++) {
				if (!colors.contains(sides[m])) {
					colors.add(sides[m]);
				}
			}
			
			pieces.add(new PuzzlePiece(colors.indexOf(sides[1]), colors.indexOf(sides[2]), colors.indexOf(sides[3]), colors.indexOf(sides[0])));
			
		}
		
		PuzzlePiece[][] solution = solveFiveByFive(pieces);
		
		for(int row = 0; row < 5; row++) {
			String ps = "";
			for (int column = 0; column < 5; column++) {
				ps += "("+colors.get(solution[row][column].up) + "," + colors.get(solution[row][column].left) + "," + colors.get(solution[row][column].down) + "," + colors.get(solution[row][column].right) + ");";
			}
			
			System.out.println(ps.substring(0, ps.length()-1));
		}
		
	}

	static PuzzlePiece[][] solveFiveByFive(List<PuzzlePiece> pieces) {
		
		PuzzlePiece[][] solution = new PuzzlePiece[5][5];
		
		for (int i = 0; i < 5; i++) {
			solution[i] = new PuzzlePiece[5];
		}
		
		
		List<PuzzlePiece[]>[] allBordersUpRightDownLeft = findBordersAndCorners(pieces);
		
		
		for (PuzzlePiece[] upBorder : allBordersUpRightDownLeft[0]) {
			for (PuzzlePiece[] rightBorder : allBordersUpRightDownLeft[1]) {
				for (PuzzlePiece[] downBorder : allBordersUpRightDownLeft[2]) {
					for (PuzzlePiece[] leftBorder : allBordersUpRightDownLeft[3]) {
						
						List<PuzzlePiece> threeByThreePieces = new ArrayList<>(pieces);
						
						threeByThreePieces.remove(ru);
						threeByThreePieces.remove(rd);
						threeByThreePieces.remove(ld);						
						threeByThreePieces.remove(ul);
						
						for (int i = 0; i < 3; i++) {
							threeByThreePieces.remove(upBorder[i]);
						}
						
						for (int i = 0; i < 3; i++) {
							threeByThreePieces.remove(rightBorder[i]);							
						}
						
						for (int i = 0; i < 3; i++) {
							threeByThreePieces.remove(downBorder[i]);
						}
						
						for (int i = 0; i < 3; i++) {
							threeByThreePieces.remove(leftBorder[i]);
						}
						
						if (threeByThreePieces.size() != 9) continue;
						
						List<PuzzlePiece[][]> solutionsThreeByThree = solveThreeByThree(threeByThreePieces, upBorder, rightBorder, downBorder, leftBorder);
						
						if (!solutionsThreeByThree.isEmpty()) {
							
							PuzzlePiece[][] solutionThreeByThree = solutionsThreeByThree.get(0);
							
							solution[0][0] = ul;
							solution[4][0] = ld;
							solution[0][4] = ru;
							solution[4][4] = rd;
							
							
							for (int i = 0; i < 3; i++) {
								solution[i+1][4] = rightBorder[i];
							}
							
							for (int i = 0; i < 3; i++) {
								solution[i+1][0] = leftBorder[i];
							}
							
							for (int i = 0; i < 3; i++) {
								solution[0][i+1] = upBorder[i];
							}
							
							for (int i = 0; i < 3; i++) {
								solution[4][1+i] = downBorder[i];
							}
							
							for (int i = 0; i < 3; i++)
								for (int j = 0; j < 3; j++)
									solution[i+1][j+1] = solutionThreeByThree[i][j];
							
							return solution;
						}
						
					}
				}
			}
		}
		
		return null;
		
	}
	
	static List<PuzzlePiece[]>[] findBordersAndCorners(List<PuzzlePiece> pieces) {
		
		List<PuzzlePiece[]>[] allBordersUpRightDownLeft = new ArrayList[4];
		
		List<PuzzlePiece[]> possibleBottomBorders = new ArrayList<PuzzlePiece[]>();
		List<PuzzlePiece[]> possibleTopBorders = new ArrayList<PuzzlePiece[]>();
		List<PuzzlePiece[]> possibleRightBorders = new ArrayList<PuzzlePiece[]>();
		List<PuzzlePiece[]> possibleLeftBorders = new ArrayList<PuzzlePiece[]>();
		

		
		List<PuzzlePiece> downPiecesList = new ArrayList<>();
		List<PuzzlePiece> topPiecesList = new ArrayList<>();
		List<PuzzlePiece> rightPiecesList = new ArrayList<>();
		List<PuzzlePiece> leftPiecesList = new ArrayList<>();
		
		
		for (int i = 0; i < 25; i++) {
				if (pieces.get(i).down == colors.indexOf("black") && (pieces.get(i).left == colors.indexOf("black"))) {
					ld = pieces.get(i);
					continue;
				}
				else if ( pieces.get(i).left == colors.indexOf("black") && pieces.get(i).up == colors.indexOf("black")) {
					ul = pieces.get(i); 
					continue;
				}
				else if (pieces.get(i).right == colors.indexOf("black")&&(pieces.get(i).down == colors.indexOf("black"))) {
					rd = pieces.get(i);
					continue;
				}
				else if (pieces.get(i).up == colors.indexOf("black")&&(pieces.get(i).right == colors.indexOf("black"))) {
					ru = pieces.get(i);
					continue;
				}
				
				
				if (pieces.get(i).down == colors.indexOf("black")) {
					downPiecesList.add(pieces.get(i));
					continue;
				}
				else if ( pieces.get(i).left == colors.indexOf("black")) {
					leftPiecesList.add(pieces.get(i));
					continue;
				}
				else if ( pieces.get(i).right == colors.indexOf("black")) {
					rightPiecesList.add(pieces.get(i));
					continue;
				}
				else if ( pieces.get(i).up == colors.indexOf("black")) {
					topPiecesList.add(pieces.get(i));
					continue;
				}
		}
		
		
		int i = 0;
		int j = 1;
		int l = 2;
		if (downPiecesList.get(i).left == ld.right && downPiecesList.get(i).right == downPiecesList.get(j).left && downPiecesList.get(j).right == downPiecesList.get(l).left && downPiecesList.get(l).right == rd.left) {
			
			PuzzlePiece[] bottomBorder = new PuzzlePiece[3];
			bottomBorder[0] = downPiecesList.get(i);
			bottomBorder[1] = downPiecesList.get(j);
			bottomBorder[2] = downPiecesList.get(l);
			
			possibleBottomBorders.add(bottomBorder);
			
		}
		i = 0;
		j = 2;
		l = 1;
		if (downPiecesList.get(i).left == ld.right && downPiecesList.get(i).right == downPiecesList.get(j).left && downPiecesList.get(j).right == downPiecesList.get(l).left && downPiecesList.get(l).right == rd.left) {
			
			PuzzlePiece[] bottomBorder = new PuzzlePiece[3];
			bottomBorder[0] = downPiecesList.get(i);
			bottomBorder[1] = downPiecesList.get(j);
			bottomBorder[2] = downPiecesList.get(l);
			
			possibleBottomBorders.add(bottomBorder);
			
		}
		i = 1;
		j = 0;
		l = 2;
		if (downPiecesList.get(i).left == ld.right && downPiecesList.get(i).right == downPiecesList.get(j).left && downPiecesList.get(j).right == downPiecesList.get(l).left && downPiecesList.get(l).right == rd.left) {
			
			PuzzlePiece[] bottomBorder = new PuzzlePiece[3];
			bottomBorder[0] = downPiecesList.get(i);
			bottomBorder[1] = downPiecesList.get(j);
			bottomBorder[2] = downPiecesList.get(l);
			
			possibleBottomBorders.add(bottomBorder);
			
		}
		i = 1;
		j = 2;
		l = 0;
		if (downPiecesList.get(i).left == ld.right && downPiecesList.get(i).right == downPiecesList.get(j).left && downPiecesList.get(j).right == downPiecesList.get(l).left && downPiecesList.get(l).right == rd.left) {
			
			PuzzlePiece[] bottomBorder = new PuzzlePiece[3];
			bottomBorder[0] = downPiecesList.get(i);
			bottomBorder[1] = downPiecesList.get(j);
			bottomBorder[2] = downPiecesList.get(l);
			
			possibleBottomBorders.add(bottomBorder);
			
		}
		i = 2;
		j = 0;
		l = 1;
		if (downPiecesList.get(i).left == ld.right && downPiecesList.get(i).right == downPiecesList.get(j).left && downPiecesList.get(j).right == downPiecesList.get(l).left && downPiecesList.get(l).right == rd.left) {
			
			PuzzlePiece[] bottomBorder = new PuzzlePiece[3];
			bottomBorder[0] = downPiecesList.get(i);
			bottomBorder[1] = downPiecesList.get(j);
			bottomBorder[2] = downPiecesList.get(l);
			
			possibleBottomBorders.add(bottomBorder);
			
		}
		i = 2;
		j = 1;
		l = 0;
		if (downPiecesList.get(i).left == ld.right && downPiecesList.get(i).right == downPiecesList.get(j).left && downPiecesList.get(j).right == downPiecesList.get(l).left && downPiecesList.get(l).right == rd.left) {
			
			PuzzlePiece[] bottomBorder = new PuzzlePiece[3];
			bottomBorder[0] = downPiecesList.get(i);
			bottomBorder[1] = downPiecesList.get(j);
			bottomBorder[2] = downPiecesList.get(l);
			
			possibleBottomBorders.add(bottomBorder);
			
		}
		

		
		
		
		
		
		
		i = 0;
		j = 1;
		l = 2;
		if (leftPiecesList.get(i).up == ul.down && leftPiecesList.get(i).down == leftPiecesList.get(j).up && leftPiecesList.get(j).down == leftPiecesList.get(l).up && leftPiecesList.get(l).down == ld.up) {
			
			PuzzlePiece[] leftBorder = new PuzzlePiece[3];
			leftBorder[0] = leftPiecesList.get(i);
			leftBorder[1] = leftPiecesList.get(j);
			leftBorder[2] = leftPiecesList.get(l);
			
			possibleLeftBorders.add(leftBorder);
			
		}
		i = 0;
		j = 2;
		l = 1;
		if (leftPiecesList.get(i).up == ul.down && leftPiecesList.get(i).down == leftPiecesList.get(j).up && leftPiecesList.get(j).down == leftPiecesList.get(l).up && leftPiecesList.get(l).down == ld.up) {			
			PuzzlePiece[] leftBorder = new PuzzlePiece[3];
			leftBorder[0] = leftPiecesList.get(i);
			leftBorder[1] = leftPiecesList.get(j);
			leftBorder[2] = leftPiecesList.get(l);
			
			possibleLeftBorders.add(leftBorder);
			
		}
		i = 1;
		j = 0;
		l = 2;
		if (leftPiecesList.get(i).up == ul.down && leftPiecesList.get(i).down == leftPiecesList.get(j).up && leftPiecesList.get(j).down == leftPiecesList.get(l).up && leftPiecesList.get(l).down == ld.up) {			
			PuzzlePiece[] leftBorder = new PuzzlePiece[3];
			leftBorder[0] = leftPiecesList.get(i);
			leftBorder[1] = leftPiecesList.get(j);
			leftBorder[2] = leftPiecesList.get(l);
			
			possibleLeftBorders.add(leftBorder);
			
		}
		i = 1;
		j = 2;
		l = 0;
		if (leftPiecesList.get(i).up == ul.down && leftPiecesList.get(i).down == leftPiecesList.get(j).up && leftPiecesList.get(j).down == leftPiecesList.get(l).up && leftPiecesList.get(l).down == ld.up) {	
			PuzzlePiece[] leftBorder = new PuzzlePiece[3];
			leftBorder[0] = leftPiecesList.get(i);
			leftBorder[1] = leftPiecesList.get(j);
			leftBorder[2] = leftPiecesList.get(l);
			
			possibleLeftBorders.add(leftBorder);
			
		}
		i = 2;
		j = 0;
		l = 1;
		if (leftPiecesList.get(i).up == ul.down && leftPiecesList.get(i).down == leftPiecesList.get(j).up && leftPiecesList.get(j).down == leftPiecesList.get(l).up && leftPiecesList.get(l).down == ld.up) {	
			PuzzlePiece[] leftBorder = new PuzzlePiece[3];
			leftBorder[0] = leftPiecesList.get(i);
			leftBorder[1] = leftPiecesList.get(j);
			leftBorder[2] = leftPiecesList.get(l);
			
			possibleLeftBorders.add(leftBorder);
			
		}
		i = 2;
		j = 1;
		l = 0;
		if (leftPiecesList.get(i).up == ul.down && leftPiecesList.get(i).down == leftPiecesList.get(j).up && leftPiecesList.get(j).down == leftPiecesList.get(l).up && leftPiecesList.get(l).down == ld.up) {	
			PuzzlePiece[] leftBorder = new PuzzlePiece[3];
			leftBorder[0] = leftPiecesList.get(i);
			leftBorder[1] = leftPiecesList.get(j);
			leftBorder[2] = leftPiecesList.get(l);
			
			possibleLeftBorders.add(leftBorder);
			
		}
		
		
		
		
		
		i = 0;
		j = 1;
		l = 2;
		if (rightPiecesList.get(i).up == ru.down && rightPiecesList.get(i).down == rightPiecesList.get(j).up && rightPiecesList.get(j).down == rightPiecesList.get(l).up && rightPiecesList.get(l).down == rd.up) {
			
			PuzzlePiece[] rightBorder = new PuzzlePiece[3];
			rightBorder[0] = rightPiecesList.get(i);
			rightBorder[1] = rightPiecesList.get(j);
			rightBorder[2] = rightPiecesList.get(l);
			
			possibleRightBorders.add(rightBorder);
			
		}
		i = 0;
		j = 2;
		l = 1;
		if (rightPiecesList.get(i).up == ru.down && rightPiecesList.get(i).down == rightPiecesList.get(j).up && rightPiecesList.get(j).down == rightPiecesList.get(l).up && rightPiecesList.get(l).down == rd.up) {			
			PuzzlePiece[] rightBorder = new PuzzlePiece[3];
			rightBorder[0] = rightPiecesList.get(i);
			rightBorder[1] = rightPiecesList.get(j);
			rightBorder[2] = rightPiecesList.get(l);
			
			possibleRightBorders.add(rightBorder);
			
		}
		i = 1;
		j = 0;
		l = 2;
		if (rightPiecesList.get(i).up == ru.down && rightPiecesList.get(i).down == rightPiecesList.get(j).up && rightPiecesList.get(j).down == rightPiecesList.get(l).up && rightPiecesList.get(l).down == rd.up) {			
			PuzzlePiece[] rightBorder = new PuzzlePiece[3];
			rightBorder[0] = rightPiecesList.get(i);
			rightBorder[1] = rightPiecesList.get(j);
			rightBorder[2] = rightPiecesList.get(l);
			
			possibleRightBorders.add(rightBorder);
			
		}
		i = 1;
		j = 2;
		l = 0;
		if (rightPiecesList.get(i).up == ru.down && rightPiecesList.get(i).down == rightPiecesList.get(j).up && rightPiecesList.get(j).down == rightPiecesList.get(l).up && rightPiecesList.get(l).down == rd.up) {	
			PuzzlePiece[] rightBorder = new PuzzlePiece[3];
			rightBorder[0] = rightPiecesList.get(i);
			rightBorder[1] = rightPiecesList.get(j);
			rightBorder[2] = rightPiecesList.get(l);
			
			possibleRightBorders.add(rightBorder);
			
		}
		i = 2;
		j = 0;
		l = 1;
		if (rightPiecesList.get(i).up == ru.down && rightPiecesList.get(i).down == rightPiecesList.get(j).up && rightPiecesList.get(j).down == rightPiecesList.get(l).up && rightPiecesList.get(l).down == rd.up) {	
			PuzzlePiece[] rightBorder = new PuzzlePiece[3];
			rightBorder[0] = rightPiecesList.get(i);
			rightBorder[1] = rightPiecesList.get(j);
			rightBorder[2] = rightPiecesList.get(l);
			
			possibleRightBorders.add(rightBorder);
			
		}
		i = 2;
		j = 1;
		l = 0;
		if (rightPiecesList.get(i).up == ru.down && rightPiecesList.get(i).down == rightPiecesList.get(j).up && rightPiecesList.get(j).down == rightPiecesList.get(l).up && rightPiecesList.get(l).down == rd.up) {	
			PuzzlePiece[] rightBorder = new PuzzlePiece[3];
			rightBorder[0] = rightPiecesList.get(i);
			rightBorder[1] = rightPiecesList.get(j);
			rightBorder[2] = rightPiecesList.get(l);
			
			possibleRightBorders.add(rightBorder);
			
		}
		
		
		
		
		
		
		
		
		
		

		i = 0;
		j = 1;
		l = 2;
		if (topPiecesList.get(i).left == ul.right && topPiecesList.get(i).right == topPiecesList.get(j).left && topPiecesList.get(j).right == topPiecesList.get(l).left && topPiecesList.get(l).right == ru.left) {
			
			PuzzlePiece[] topBorder = new PuzzlePiece[3];
			topBorder[0] = topPiecesList.get(i);
			topBorder[1] = topPiecesList.get(j);
			topBorder[2] = topPiecesList.get(l);
			
			possibleTopBorders.add(topBorder);
			
		}
		i = 0;
		j = 2;
		l = 1;
		if (topPiecesList.get(i).left == ul.right && topPiecesList.get(i).right == topPiecesList.get(j).left && topPiecesList.get(j).right == topPiecesList.get(l).left && topPiecesList.get(l).right == ru.left) {
			
			PuzzlePiece[] topBorder = new PuzzlePiece[3];
			topBorder[0] = topPiecesList.get(i);
			topBorder[1] = topPiecesList.get(j);
			topBorder[2] = topPiecesList.get(l);
			
			possibleTopBorders.add(topBorder);
			
		}
		i = 1;
		j = 0;
		l = 2;
		if (topPiecesList.get(i).left == ul.right && topPiecesList.get(i).right == topPiecesList.get(j).left && topPiecesList.get(j).right == topPiecesList.get(l).left && topPiecesList.get(l).right == ru.left) {
			
			PuzzlePiece[] topBorder = new PuzzlePiece[3];
			topBorder[0] = topPiecesList.get(i);
			topBorder[1] = topPiecesList.get(j);
			topBorder[2] = topPiecesList.get(l);
			
			possibleTopBorders.add(topBorder);
			
		}
		i = 1;
		j = 2;
		l = 0;
		if (topPiecesList.get(i).left == ul.right && topPiecesList.get(i).right == topPiecesList.get(j).left && topPiecesList.get(j).right == topPiecesList.get(l).left && topPiecesList.get(l).right == ru.left) {
			
			PuzzlePiece[] topBorder = new PuzzlePiece[3];
			topBorder[0] = topPiecesList.get(i);
			topBorder[1] = topPiecesList.get(j);
			topBorder[2] = topPiecesList.get(l);
			
			possibleTopBorders.add(topBorder);
			
		}
		i = 2;
		j = 0;
		l = 1;
		if (topPiecesList.get(i).left == ul.right && topPiecesList.get(i).right == topPiecesList.get(j).left && topPiecesList.get(j).right == topPiecesList.get(l).left && topPiecesList.get(l).right == ru.left) {
			
			PuzzlePiece[] topBorder = new PuzzlePiece[3];
			topBorder[0] = topPiecesList.get(i);
			topBorder[1] = topPiecesList.get(j);
			topBorder[2] = topPiecesList.get(l);
			
			possibleTopBorders.add(topBorder);
			
		}
		i = 2;
		j = 1;
		l = 0;
		if (topPiecesList.get(i).left == ul.right && topPiecesList.get(i).right == topPiecesList.get(j).left && topPiecesList.get(j).right == topPiecesList.get(l).left && topPiecesList.get(l).right == ru.left) {
			
			PuzzlePiece[] topBorder = new PuzzlePiece[3];
			topBorder[0] = topPiecesList.get(i);
			topBorder[1] = topPiecesList.get(j);
			topBorder[2] = topPiecesList.get(l);
			
			possibleTopBorders.add(topBorder);
			
		}
		
		
		
		
		allBordersUpRightDownLeft[0] = possibleTopBorders;
		allBordersUpRightDownLeft[1] = possibleRightBorders;
		allBordersUpRightDownLeft[2] = possibleBottomBorders;
		allBordersUpRightDownLeft[3] = possibleLeftBorders;
								
		return allBordersUpRightDownLeft;
	}
	
	static List<PuzzlePiece[][]> solveThreeByThree(List<PuzzlePiece> pieces, PuzzlePiece[] topBorder, PuzzlePiece[] rightBorder, PuzzlePiece[] bottomBorder, PuzzlePiece[] leftBorder) {
		
		
		List<PuzzlePiece[][]> possibleConfigurations = new ArrayList<PuzzlePiece[][]>();
		
		List<PuzzlePiece> candidatesLU =  new ArrayList<PuzzlePiece>();
		List<PuzzlePiece> candidatesLD =  new ArrayList<PuzzlePiece>();
		List<PuzzlePiece> candidatesRD =  new ArrayList<PuzzlePiece>();
		List<PuzzlePiece> candidatesRU =  new ArrayList<PuzzlePiece>();
		
		for (int i = 0; i < pieces.size(); i++) {

			if (pieces.get(i).up == topBorder[0].down && pieces.get(i).left == leftBorder[0].right) {
				candidatesLU.add(pieces.get(i)); 
			}
			if (pieces.get(i).down == bottomBorder[0].up && pieces.get(i).left == leftBorder[2].right) {
				candidatesLD.add(pieces.get(i)); 
			}
			if (pieces.get(i).down == bottomBorder[2].up && pieces.get(i).right == rightBorder[2].left) {
				candidatesRD.add(pieces.get(i)); 
			}
			if (pieces.get(i).up == topBorder[2].down && pieces.get(i).right == rightBorder[0].left) {
				candidatesRU.add(pieces.get(i)); 
			}

		}
		
		
		for (PuzzlePiece lu : candidatesLU) {
			for (PuzzlePiece ld : candidatesLD) {
				for (PuzzlePiece rd : candidatesRD){
					for (PuzzlePiece ru : candidatesRU) {
						
						if (lu == ld || lu == rd || lu == ru || ld == rd || ld == ru || rd == ru) continue;
						
						List<PuzzlePiece> centralPiecesArrangement = new ArrayList<>();
						
						for (int o = 0; o < pieces.size(); o++) {
							centralPiecesArrangement.add(pieces.get(o));
							
						}
						
						centralPiecesArrangement.remove(ld);
						centralPiecesArrangement.remove(lu);
						centralPiecesArrangement.remove(rd);
						centralPiecesArrangement.remove(ru);
						
						
						for (int candidatePositionRight = 0; candidatePositionRight < 5; candidatePositionRight++) {
							
							for (int candidatePositionUp = 0; candidatePositionUp < 5; candidatePositionUp++) {
								if (candidatePositionRight == candidatePositionUp) continue;
								for (int candidatePosition = 0; candidatePosition < 5; candidatePosition++) {
									if (candidatePosition == candidatePositionRight || candidatePosition == candidatePositionUp) continue;
									for (int candidatePositionDown = 0; candidatePositionDown < 5; candidatePositionDown++) {
										if (candidatePositionDown == candidatePositionRight || candidatePositionDown == candidatePositionUp || candidatePositionDown == candidatePosition) continue;
										
										if ((centralPiecesArrangement.get(candidatePosition).left == leftBorder[1].right && centralPiecesArrangement.get(candidatePosition).up == lu.down && centralPiecesArrangement.get(candidatePosition).down == ld.up) 
										 && (centralPiecesArrangement.get(candidatePositionUp).up == topBorder[1].down && centralPiecesArrangement.get(candidatePositionUp).left == lu.right && centralPiecesArrangement.get(candidatePositionUp).right == ru.left)
											&& (centralPiecesArrangement.get(candidatePositionRight).right == rightBorder[1].left && centralPiecesArrangement.get(candidatePositionRight).up == ru.down && centralPiecesArrangement.get(candidatePositionRight).down == rd.up)
											&& (centralPiecesArrangement.get(candidatePositionDown).down == bottomBorder[1].up && centralPiecesArrangement.get(candidatePositionDown).left == ld.right && centralPiecesArrangement.get(candidatePositionDown).right == rd.left)
											) {
											
											for (int centralPiece = 0; centralPiece < 5; centralPiece++) {
												if (centralPiece != candidatePosition && centralPiece != candidatePositionUp && centralPiece != candidatePositionRight && centralPiece != candidatePositionDown) {
													
													if (centralPiecesArrangement.get(centralPiece).right == centralPiecesArrangement.get(candidatePositionRight).left  && centralPiecesArrangement.get(centralPiece).up == centralPiecesArrangement.get(candidatePositionUp).down && centralPiecesArrangement.get(centralPiece).left == centralPiecesArrangement.get(candidatePosition).right && centralPiecesArrangement.get(centralPiece).down == centralPiecesArrangement.get(candidatePositionDown).up) {
													
														PuzzlePiece[][] possibleConfiguration = new PuzzlePiece[3][3];
														
														possibleConfiguration[0] = new PuzzlePiece[3];
														possibleConfiguration[1] = new PuzzlePiece[3];
														possibleConfiguration[2] = new PuzzlePiece[3];
														
														
														possibleConfiguration[0][1] = centralPiecesArrangement.get(candidatePositionUp);
														possibleConfiguration[1][2] = centralPiecesArrangement.get(candidatePositionRight);
														possibleConfiguration[1][0] = centralPiecesArrangement.get(candidatePosition);
														possibleConfiguration[2][1] = centralPiecesArrangement.get(candidatePositionDown);
														possibleConfiguration[1][1] = centralPiecesArrangement.get(centralPiece);
														
														possibleConfiguration[0][0] = lu;
														possibleConfiguration[0][2] = ru;
														possibleConfiguration[2][0] = ld;
														possibleConfiguration[2][2] = rd;
														
														possibleConfigurations.add(possibleConfiguration);
													}
												}
											}
											
										}
									}
								}
							}
						}
						
						
						
						
					}
				}
			}
		}
		return possibleConfigurations;
	}
	
	static class PuzzlePiece {
		
		int left, down, right, up;
		
		PuzzlePiece(int left, int down, int right, int up) {
			this.up = up;
			this.left = left;
			this.down = down;
			this.right = right;
			
		}
		
		@Override
		public boolean equals(Object e) {
			return ((e instanceof PuzzlePiece) && ((PuzzlePiece)e).up == up  && ((PuzzlePiece)e).right == right && ((PuzzlePiece)e).down == down && ((PuzzlePiece)e).left == left);
		}
		
	}
	
	
}
