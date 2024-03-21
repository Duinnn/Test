import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class NumberleController extends JFrame{

    // 游戏状态跟踪
    public int numberOfRowDuringGame = 0;//正在游戏的行数
    public int inputIconCount = 0;// 追踪这一行已输入的图标数量

    List<String> equations = loadEquationsFromFilePath("equations.txt");// 调用加载文件的方法
    String targetEquation = getRandomEquationFromList(equations);// 从方程列表中随机选择一个方程

    private NumberleModel model;
    private NumberleView view;


    //鼠标点击事件监听器（添加键盘按钮功能）
    public class ImageClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel clickedLabel = (JLabel) e.getSource();
            ImageIcon correspondingIcon = view.getIconFromImageLabel(clickedLabel);

            if (inputIconCount % 7 == 0 && inputIconCount != 0) {
                // 当已输入的图标数量能被7整除且不为0时（当前一行填满时），只能点击enter.png和delete.png
                if (clickedLabel == view.imageLabelDelete) {
                    for (int j = 5; j >= 0; j--) {
                        for (int i = 6; i >= 0; i--) {
                            if (view.emptyLabels[j][i].getIcon() != view.iconEmpty) {
                                view.emptyLabels[j][i].setIcon(view.iconEmpty);
                                inputIconCount--;
                                return;
                            }
                        }
                    }
                } else if (clickedLabel == view.imageLabelEnter) {
                    // 检查是否包含等号
                    if (containsEqualSign()) {
                        // 检查已输入的图标数量是否能被7整除
                        if (inputIconCount % 7 == 0) {
                            boolean b = checkMathematicalEquation(view.emptyLabels);
                            System.out.println(b);
                            if (b) {
                                int[] matchStatusArray = checkMatchStatusForEquation(targetEquation, view.emptyLabels);
                                //更改图标颜色
                                updateIconColorsBasedOnMatchStatus(matchStatusArray);
                                //清空数组
                                for (int i : matchStatusArray) {
                                    matchStatusArray[i] = 0;
                                }
                                // 清空输入图标计数
                                inputIconCount = 0;
                                enableStartNewGameButton();
                                //正在游戏的行数加一
                                numberOfRowDuringGame++;
                                System.out.println(inputIconCount);
                            } else {
                                // 弹出提示对话框
                                JOptionPane.showMessageDialog(NumberleController.this, "The left side is not equal to the right", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            // 弹出提示对话框
                            JOptionPane.showMessageDialog(NumberleController.this, "Too Short", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // 弹出提示对话框
                        JOptionPane.showMessageDialog(NumberleController.this, "No equal '=' sign", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else
            //当前行未填满时
            {
                // 点击其他图标执行相应的操作
                if (clickedLabel == view.imageLabelDelete) {
                    if (inputIconCount > 0) {
                        for (int j = 5; j >= 0; j--) {
                            for (int i = 6; i >= 0; i--) {
                                if (view.emptyLabels[j][i].getIcon() != view.iconEmpty) {
                                    view.emptyLabels[j][i].setIcon(view.iconEmpty);
                                    inputIconCount--;
                                    System.out.println(inputIconCount);
                                    return;
                                }
                            }
                        }
                    }
                } else if (clickedLabel == view.imageLabelEnter) {
                    // 检查是否包含等号
                    if (containsEqualSign()) {
                        // 检查已输入的图标数量是否能被7整除
                        if (inputIconCount % 7 == 0) {
                            boolean b = checkMathematicalEquation(view.emptyLabels);
                            System.out.println(b);
                            if (b) {
                                int[] matchStatusArray = checkMatchStatusForEquation(targetEquation, view.emptyLabels);
                                //更改图标颜色
                                updateIconColorsBasedOnMatchStatus(matchStatusArray);
                                //清空数组
                                for (int i : matchStatusArray) {
                                    matchStatusArray[i] = 0;
                                }
                                // 清空输入图标计数
                                inputIconCount = 0;
                                enableStartNewGameButton();
                                //正在游戏的行数加一
                                numberOfRowDuringGame++;
                            } else {
                                // 弹出提示对话框
                                JOptionPane.showMessageDialog(NumberleController.this, "The left side is not equal to the right", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            // 弹出提示对话框
                            JOptionPane.showMessageDialog(NumberleController.this, "Too Short", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // 弹出提示对话框
                        JOptionPane.showMessageDialog(NumberleController.this, "No equal '=' sign", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    for (int j = 0; j < 6; j++) {
                        for (int i = 0; i < 7; i++) {
                            if (view.emptyLabels[j][i].getIcon() == view.iconEmpty) {
                                view.emptyLabels[j][i].setIcon(correspondingIcon);
                                // 更新已输入的图标数量
                                inputIconCount++;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    // 监听开始新游戏按钮点击事件的监听器
    public  class StartNewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 在这里添加逻辑以重新设置游戏状态并开始新游戏
            resetGameAndStartNew();
        }
    }

    // 根据匹配状态数组设置键盘图标颜色和emptyLabels对应图标颜色
    private void updateIconColorsBasedOnMatchStatus(int[] matchStatusArray) {
        boolean allGreen = true;
        for (int i = 0; i < view.emptyLabels[0].length; i++) {
            char correspondingCharacter = view.getCharacterFromImageLabel(view.emptyLabels[numberOfRowDuringGame][i]);
            switch (matchStatusArray[i]) {
                case 1:
                    view.setColorForImageLabel(correspondingCharacter, "Green");
                    view.emptyLabels[numberOfRowDuringGame][i].setIcon(view.getGreenIcon(view.emptyLabels[numberOfRowDuringGame][i]));
                    break;
                case 2:
                    view.setColorForImageLabel(correspondingCharacter, "Orange");
                    view.emptyLabels[numberOfRowDuringGame][i].setIcon(view.getOrangeIcon(view.emptyLabels[numberOfRowDuringGame][i]));
                    allGreen = false; // At least one icon is not green
                    break;
                case 3:
                    view.setColorForImageLabel(correspondingCharacter, "Grey");
                    view.emptyLabels[numberOfRowDuringGame][i].setIcon(view.getGreyIcon(view.emptyLabels[numberOfRowDuringGame][i]));
                    allGreen = false; // At least one icon is not green
                    break;
            }
        }

        // Check if all icons are green
        if (allGreen) {
            // Display success message
            JOptionPane.showMessageDialog(NumberleController.this, "Congratulations! You've completed the game successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (numberOfRowDuringGame == view.emptyLabels.length - 1) {
            // If it's the last row and not all icons are green, display failure message
            JOptionPane.showMessageDialog(NumberleController.this, "Game Over! You've failed to complete the game.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }


    // 从文件加载方程列表
    private List<String> loadEquationsFromFilePath(String filePath) {
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
    private String getRandomEquationFromList(List<String> equations) {
        Random random = new Random();
        int randomIndex = random.nextInt(equations.size());
        return equations.get(randomIndex);
    }


    // 检查用户输入是否匹配目标方程，返回一个匹配状态数组
    private int[] checkMatchStatusForEquation(String targetEquation, JLabel[][] inputLabels) {
        // 将用户输入的图标转换为对应的字符数组
        char[] userInput = new char[inputLabels[numberOfRowDuringGame].length];
        for (int i = 0; i < inputLabels[numberOfRowDuringGame].length; i++) {
            JLabel label = inputLabels[numberOfRowDuringGame][i];
            userInput[i] = view.getCharacterFromImageLabel(label);
        }

        // 将目标方程转换为字符数组
        char[] targetArray = targetEquation.toCharArray();

        // 初始化匹配状态数组
        int[] matchArray = new int[userInput.length];

        // 检查每个图标是否与目标方程的对应字符相匹配
        for (int i = 0; i < userInput.length; i++) {
            if (i < targetArray.length && userInput[i] == targetArray[i]) {
                matchArray[i] = view.ICON_COLOR_GREEN;
            } else if (i < targetArray.length && targetEquation.contains(String.valueOf(userInput[i]))) {
                matchArray[i] = view.ICON_COLOR_ORANGE;
            } else {
                matchArray[i] = view.ICON_COLOR_GREY;
            }
        }
        return matchArray;
    }

    // 检查已输入的图标中是否包含等号
    private boolean containsEqualSign() {
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                if (view.emptyLabels[j][i].getIcon() == view.iconEqual) {
                    return true;
                }
            }
        }
        return false;
    }

    //Determine whether the input conforms to mathematical equations
    private boolean checkMathematicalEquation(JLabel[][] inputLabels) {
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

    // 启用新游戏按钮
    private void enableStartNewGameButton() {
        view.startNewGameButton.setEnabled(true);
    }

    // 重置游戏状态并开始新游戏
    private void resetGameAndStartNew() {
        // 清空游戏状态
        numberOfRowDuringGame = 0;
        inputIconCount = 0;
        // 重新选择目标方程
        targetEquation = getRandomEquationFromList(equations);
        System.out.println("New Target Equation: " + targetEquation); // 打印新选择的方程（仅供测试）
        view.startNewGameButton.setEnabled(false);
        // 移除所有现有组件
        getContentPane().removeAll();
        // 重新初始化新游戏按钮
        view.initializeStartNewGameButton();
        // 重新初始化游戏界面
        view.initializeGameInterface();
        // 更新界面
        this.repaint();
    }

    //计算给定的数学表达式的结果。
    private int evaluateMathematicalExpression(String expression) {
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
    private void applyOperation(Stack<Integer> stack, int currentNumber, char operation) {
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
