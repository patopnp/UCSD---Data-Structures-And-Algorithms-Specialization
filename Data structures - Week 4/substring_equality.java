import java.util.*;
import java.io.*;

public class substring_equality {
	public class Solver {
		private int s2l;
		private long[] x2l;		
		private int s1l;
		private long[] x1l;
		private long[] h1;
		private long[] h2;
		private long x1;
		private long x2;
		private long m1;
		private long m2;

		public Solver(String s) {
			this.x2l = new long[s.length() + 1];
			this.x2l[0] = 1;			
			this.x1l = new long[s.length() + 1];
			this.x1l[0] = 1;
			this.x1 = 31;
			this.x2 = 57;
			this.m1 = 10000007L;
			this.m2 = 10000009L;
			this.h1 = precomputeHashes(s, m1, x1);
			this.h2 = precomputeHashes(s, m2, x2);

		}

		public boolean ask(int a, int b, int l) {
			return substringHash1(a, l) == substringHash1(b, l) && substringHash2(a, l) == substringHash2(b, l);
		}

		private long substringHash1(int from, int length) {

			long xl = 1;

			if (length <= s1l) {
				xl = x1l[length];
			} else {
				xl = x1l[s1l];
				for (int i = s1l + 1; i <= length; i++) {
					xl = (xl * x1) % m1;

					if (xl < 0) {
						xl += m1;
					}
					x1l[i] = xl;
				}

				s1l = length;
			}

			long value = (h1[from + length] - xl * h1[from]) % m1;

			if (value < 0) {
				value += m1;
			}

			return value;
		}

		private long substringHash2(int from, int length) {

			long xl = 1;

			if (length <= s2l) {
				xl = x2l[length];
			} else {
				xl = x2l[s2l];
				for (int i = s2l + 1; i <= length; i++) {
					xl = (xl * x2) % m2;

					if (xl < 0) {
						xl += m2;
					}
					x2l[i] = xl;
				}

				s2l = length;
			}

			long value = (h2[from + length] - xl * h2[from]) % m2;

			if (value < 0) {
				value += m2;
			}

			return value;
		}
	}

	static long[] precomputeHashes(String t, long m, long x) {
		long h[] = new long[t.length() + 1];
		h[0] = 0;

		for (int i = 1; i <= t.length(); i++) {
			h[i] = (x * h[i - 1] + (long) t.charAt(i - 1)) % m;
			if (h[i] < 0) {
				h[i] += m;
			}
		}

		return h;
	}

	private static long polyHash(String s, long m, long x) {
		long hash = 0;
		for (int i = 0; i <= s.length() - 1; i++) {

			hash = (((hash * x + s.charAt(i)) % m));
			if (hash < 0) {
				hash += m;
			}

		}
		return hash;
	}

	public void run() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		String s = in.next();
		int q = in.nextInt();
		Solver solver = new Solver(s);
		for (int i = 0; i < q; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int l = in.nextInt();
			out.println(solver.ask(a, b, l) ? "Yes" : "No");
		}
		out.close();
	}

	static public void main(String[] args) throws IOException {
		new substring_equality().run();
	}

	class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(in.readLine());
			return tok.nextToken();
		}

		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}
}
