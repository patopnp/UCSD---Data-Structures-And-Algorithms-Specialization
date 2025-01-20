import java.util.*;

public class LCM {

  private static long lcm_improved(long a, long b) {
    long abProduct = a * b;
    long abGcd = gcd_euclidean(a, b);
    long lcm = abProduct / abGcd;
    return lcm;
  }

  private static long gcd_euclidean(long a, long b) {
    if (a == 0)
      return b;

    return gcd_euclidean(b % a, a);
  }

  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);
    int a = scanner.nextInt();
    int b = scanner.nextInt();

    System.out.println(lcm_improved(a, b));
  }
}
