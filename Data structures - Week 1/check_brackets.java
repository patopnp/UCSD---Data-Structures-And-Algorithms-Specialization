import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

class Bracket {
    Bracket(char type, int position) {
        this.type = type;
        this.position = position;
    }

    boolean Match(char c) {
        if (this.type == '[' && c == ']')
            return true;
        if (this.type == '{' && c == '}')
            return true;
        if (this.type == '(' && c == ')')
            return true;
        return false;
    }

    char type;
    int position;
}

class check_brackets {
    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

        Stack<Bracket> opening_brackets_stack = new Stack<Bracket>();
        for (int position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {

                opening_brackets_stack.push(new Bracket(next, position));
            }

            if (next == ')' || next == ']' || next == '}') {
                
                if (opening_brackets_stack.empty() == true) {
                    System.out.println(position + 1);
                    return;
                }

                Bracket lastOpenBracket = opening_brackets_stack.pop();

                if (lastOpenBracket.Match(next) == false) {
                    System.out.println(position + 1);
                    return;
                } 
            }
        }

        // Printing answer, write your code here
        if (opening_brackets_stack.empty()) {
            System.out.println("Success");
            return;
        }

        Bracket lastBracketAtTheTop = opening_brackets_stack.pop();

        //this to get the first open bracket not matching - if it doesnt work test for the last that is the one at the top of the stack
        while (opening_brackets_stack.empty() == false) {
            lastBracketAtTheTop = opening_brackets_stack.pop();
        }

        System.out.println(lastBracketAtTheTop.position + 1);

    }
}
