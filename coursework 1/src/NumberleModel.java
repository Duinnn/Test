import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class NumberleModel {
    private NumberleController controller;
    private NumberleView view;
    List<String> equations = loadEquationsFromFilePath("equations.txt");// 调用加载文件的方法
    String targetEquation = getRandomEquationFromList(equations);// 从方程列表中随机选择一个方程



    // 游戏状态跟踪
    public int numberOfRowDuringGame = 0;//正在游戏的行数
    public int inputIconCount = 0;// 追踪这一行已输入的图标数量

    // 根据匹配状态数组设置键盘图标颜色和emptyLabels对应图标颜色
    public void updateIconColorsBasedOnMatchStatus(int[] matchStatusArray) {
        boolean allGreen = true;
        for (int i = 0; i < view.emptyLabels[0].length; i++) {
            char correspondingCharacter = view.getCharacterFromImageLabel(view.emptyLabels[numberOfRowDuringGame][i]);
            switch (matchStatusArray[i]) {
                case ICON_COLOR_GREEN:
                    view.setColorForImageLabel(correspondingCharacter, "Green");
                    view.emptyLabels[numberOfRowDuringGame][i].setIcon(view.getGreenIcon(view.emptyLabels[numberOfRowDuringGame][i]));
                    break;
                case ICON_COLOR_ORANGE:
                    view.setColorForImageLabel(correspondingCharacter, "Orange");
                    view.emptyLabels[numberOfRowDuringGame][i].setIcon(view.getOrangeIcon(view.emptyLabels[numberOfRowDuringGame][i]));
                    allGreen = false; // At least one icon is not green
                    break;
                case ICON_COLOR_GREY:
                    view.setColorForImageLabel(correspondingCharacter, "Grey");
                    view.emptyLabels[numberOfRowDuringGame][i].setIcon(view.getGreyIcon(view.emptyLabels[numberOfRowDuringGame][i]));
                    allGreen = false; // At least one icon is not green
                    break;
            }
        }

        // Check if all icons are green
        if (allGreen) {
            // Display success message
            JOptionPane.showMessageDialog(GameStart.this, "Congratulations! You've completed the game successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (numberOfRowDuringGame == view.emptyLabels.length - 1) {
            // If it's the last row and not all icons are green, display failure message
            JOptionPane.showMessageDialog(GameStart.this, "Game Over! You've failed to complete the game.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 从文件加载方程列表
    public List<String> loadEquationsFromFilePath(String filePath) {
        List<String> equations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                equations.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return equations;
    }

    // 从方程列表中随机选择一个方程
    public String getRandomEquationFromList(List<String> equations) {
        Random random = new Random();
        int randomIndex = random.nextInt(equations.size());
        return equations.get(randomIndex);
    }

    //Determine whether the input conforms to mathematical equations
    public boolean checkMathematicalEquation(JLabel[][] inputLabels) {
        // 将用户输入的图标转换为对应的字符数组
        char[] userInput = new char[inputLabels[numberOfRowDuringGame].length];
        for (int i = 0; i < inputLabels[numberOfRowDuringGame].length; i++) {
            JLabel label = inputLabels[numberOfRowDuringGame][i];
            userInput[i] = view.getCharacterFromImageLabel(label);
        }

        // 将字符数组转换为字符串
        String equation = new String(userInput);

        // 计算数学表达式的结果
        String[] sides = equation.split("="); // 将等式拆分为左右两边
        if (sides.length != 2) {
            // 如果等式中不包含且仅包含一个等号，则认为是非法的等式
            return false;
        }

        // 分别计算左右两边表达式的结果
        int leftResult = evaluateMathematicalExpression(sides[0]);
        int rightResult = evaluateMathematicalExpression(sides[1]);

        // 返回左右两边结果是否相等
        return leftResult == rightResult;
    }

    //计算给定的数学表达式的结果。
    public int evaluateMathematicalExpression(String expression) {
        Stack<Integer> stack = new Stack<>();
        int currentNumber = 0;
        char operation = '+';

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                currentNumber = currentNumber * 10 + (c - '0');
            }

            if (!Character.isDigit(c) && c != ' ' || i == expression.length() - 1) {
                applyOperation(stack, currentNumber, operation);
                operation = c;
                currentNumber = 0;
            }
        }

        // 计算栈中剩余的操作
        int result = 0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }

        return result;
    }

    //执行给定操作符的操作，并将结果压入栈中。
    public void applyOperation(Stack<Integer> stack, int currentNumber, char operation) {
        if (operation == '+') {
            stack.push(currentNumber);
        } else if (operation == '-') {
            stack.push(-currentNumber);
        } else if (operation == '*') {
            stack.push(stack.pop() * currentNumber);
        } else if (operation == '/') {
            stack.push(stack.pop() / currentNumber);
        }
    }
}
