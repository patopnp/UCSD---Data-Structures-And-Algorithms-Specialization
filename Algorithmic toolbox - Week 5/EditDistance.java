import java.util.*;

class EditDistance {
  public static int EditDistance(String s, String t) {

    int d[][] = new int[s.length()+1][t.length()+1];

    for (int i = 0; i < s.length()+1; i++) {
      d[i][0] = i;
    }
    
    for (int j = 0; j < t.length()+1; j++) {
      d[0][j] = j;
    }

    for (int i = 1; i < s.length() + 1; i++) {
      for (int j = 1; j < t.length() + 1; j++) {
        int insertion = d[i][j - 1] + 1;
        int deletion = d[i - 1][j] + 1;
        int match = d[i - 1][j - 1];
        int mismatch = d[i - 1][j - 1] + 1;

        if (s.charAt(i-1) == t.charAt(j-1)) {
          d[i][j] = min(insertion, deletion, match);
        }
        else {
          d[i][j] = min(insertion, deletion, mismatch);
        }
      }
    }

    //write your code here
    return d[s.length()][t.length()];
  }

  private static int min(int a, int b, int c){

    if (a < b){
      if (c < a)
        return c;
      else
        return a;
    }
    else {
      if (a < c)
        return b;
        // b < a  a > c
      else{
        if (b < c){
          return b;

        }
        else
        {
          return c;
        }
      }
    }
  }

  public static void main(String args[]) {
    Scanner scan = new Scanner(System.in);

    String s = scan.next();
    String t = scan.next();

    System.out.println(EditDistance(s, t));
  }

}
