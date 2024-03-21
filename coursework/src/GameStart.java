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

public class GameStart extends JFrame {

    // 图片标签
    private JLabel imageLabel0, imageLabel1, imageLabel2, imageLabel3, imageLabel4, imageLabel5, imageLabel6, imageLabel7, imageLabel8, imageLabel9;
    private JLabel imageLabelPlus, imageLabelMinus, imageLabelMultiply, imageLabelDivision;
    private JLabel imageLabelDelete, imageLabelEqual, imageLabelEnter;

    // 图标对象
    private ImageIcon icon0, icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9;
    private ImageIcon iconPlus, iconMinus, iconMultiply, iconDivision;
    private ImageIcon iconDelete, iconEqual, iconEnter;
    private ImageIcon iconEmpty;

    private JButton startNewGameButton; // 开始新游戏的按钮对象

    private JLabel[][] emptyLabels;//这个二维数组用来存放输入的图标

    // 添加一个常量表示图标的颜色状态
    private static final int ICON_COLOR_GREEN = 1;//常量1表示绿色
    private static final int ICON_COLOR_ORANGE = 2;//常量2表示橙色
    private static final int ICON_COLOR_GREY = 3;//常量3表示灰色

    // 游戏状态跟踪
    private int numberOfRowDuringGame = 0;//正在游戏的行数
    private int inputIconCount = 0;// 追踪这一行已输入的图标数量

    List<String> equations = loadEquationsFromFilePath("equations.txt");// 调用加载文件的方法
    String targetEquation = getRandomEquationFromList(equations);// 从方程列表中随机选择一个方程

    //启动游戏
    public GameStart() {

        System.out.println("Target Equation: " + targetEquation);// 打印选择的方程（仅供测试）

        initializeJFrame();//初始化界面

        initializeIcons(); // 初始化图标对象

        initializeGameInterface();//初始化游戏界面

        this.setVisible(true);//显示界面

        initializeStartNewGameButton(); // 初始化开始新游戏按钮
    }

    //初始化JFrame界面
    public void initializeJFrame() {
        //设置界面的宽高
        this.setSize(1050, 950);
        //设置界面标题
        this.setTitle("Numberle");
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //取消默认的居中放置
        this.setLayout(null);
    }

    // 初始化图标对象
    private void initializeIcons() {
        icon0 = new ImageIcon("image/default/0.png");
        icon1 = new ImageIcon("image/default/1.png");
        icon2 = new ImageIcon("image/default/2.png");
        icon3 = new ImageIcon("image/default/3.png");
        icon4 = new ImageIcon("image/default/4.png");
        icon5 = new ImageIcon("image/default/5.png");
        icon6 = new ImageIcon("image/default/6.png");
        icon7 = new ImageIcon("image/default/7.png");
        icon8 = new ImageIcon("image/default/8.png");
        icon9 = new ImageIcon("image/default/9.png");
        iconPlus = new ImageIcon("image/default/plus.png");
        iconMinus = new ImageIcon("image/default/minus.png");
        iconMultiply = new ImageIcon("image/default/multiply.png");
        iconDivision = new ImageIcon("image/default/division.png");
        iconDelete = new ImageIcon("image/default/delete.png");
        iconEqual = new ImageIcon("image/default/equal.png");
        iconEnter = new ImageIcon("image/default/enter.png");
        iconEmpty = new ImageIcon("image/Empty.png");
    }

    //初始化游戏界面
    private void initializeGameInterface() {
        imageLabel1 = createLabelWithIconAndBounds(icon1, 50, 700, 100, 100);
        imageLabel2 = createLabelWithIconAndBounds(icon2, 140, 700, 100, 100);
        imageLabel3 = createLabelWithIconAndBounds(icon3, 230, 700, 100, 100);
        imageLabel4 = createLabelWithIconAndBounds(icon4, 320, 700, 100, 100);
        imageLabel5 = createLabelWithIconAndBounds(icon5, 410, 700, 100, 100);
        imageLabel6 = createLabelWithIconAndBounds(icon6, 500, 700, 100, 100);
        imageLabel7 = createLabelWithIconAndBounds(icon7, 590, 700, 100, 100);
        imageLabel8 = createLabelWithIconAndBounds(icon8, 680, 700, 100, 100);
        imageLabel9 = createLabelWithIconAndBounds(icon9, 770, 700, 100, 100);
        imageLabel0 = createLabelWithIconAndBounds(icon0, 860, 700, 100, 100);

        imageLabelPlus = createLabelWithIconAndBounds(iconPlus, 250, 800, 100, 100);
        imageLabelMinus = createLabelWithIconAndBounds(iconMinus, 360, 800, 100, 100);
        imageLabelMultiply = createLabelWithIconAndBounds(iconMultiply, 470, 800, 100, 100);
        imageLabelDivision = createLabelWithIconAndBounds(iconDivision, 580, 800, 100, 100);
        imageLabelDelete = createLabelWithIconAndBounds(iconDelete, 50, 800, 200, 100);
        imageLabelEqual = createLabelWithIconAndBounds(iconEqual, 690, 800, 100, 100);
        imageLabelEnter = createLabelWithIconAndBounds(iconEnter, 790, 800, 200, 100);

        emptyLabels = new JLabel[6][7];  // 6行7列的二维数组，表示这个游戏可以存放的图标最多是6行7列
        //创建一个Empty按钮图片对象
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                // 创建一个JLable对象
                emptyLabels[j][i] = new JLabel(iconEmpty);
                emptyLabels[j][i].setBounds(80 + i * 120, j * 110, 100, 100);
                // 把管理容器添加到界面
                this.getContentPane().add(emptyLabels[j][i]);
            }
        }
    }

    // 初始化开始新游戏按钮
    private void initializeStartNewGameButton() {
        startNewGameButton = new JButton("New Game");
        startNewGameButton.setBounds(900, 50, 120, 40);
        startNewGameButton.setEnabled(false);
        startNewGameButton.addActionListener(new StartNewGameListener());
        this.getContentPane().add(startNewGameButton);
    }

    // 启用新游戏按钮
    private void enableStartNewGameButton() {
        startNewGameButton.setEnabled(true);
    }

    // 监听开始新游戏按钮点击事件的监听器
    private class StartNewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 在这里添加逻辑以重新设置游戏状态并开始新游戏
            resetGameAndStartNew();
        }
    }

    //创建带有图标和指定边界的 JLabel 对象。
    private JLabel createLabelWithIconAndBounds(ImageIcon icon, int x, int y, int width, int height) {
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        label.addMouseListener(new ImageClickListener());
        this.getContentPane().add(label);
        return label;
    }

    // 重置游戏状态并开始新游戏
    private void resetGameAndStartNew() {
        // 清空游戏状态
        numberOfRowDuringGame = 0;
        inputIconCount = 0;
        // 重新选择目标方程
        targetEquation = getRandomEquationFromList(equations);
        System.out.println("New Target Equation: " + targetEquation); // 打印新选择的方程（仅供测试）
        startNewGameButton.setEnabled(false);
        // 移除所有现有组件
        getContentPane().removeAll();
        // 重新初始化新游戏按钮
        initializeStartNewGameButton();
        // 重新初始化游戏界面
        initializeGameInterface();
        // 更新界面
        this.repaint();
    }

    //鼠标点击事件监听器（添加键盘按钮功能）
    private class ImageClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel clickedLabel = (JLabel) e.getSource();
            ImageIcon correspondingIcon = getIconFromImageLabel(clickedLabel);

            if (inputIconCount % 7 == 0 && inputIconCount != 0) {
                // 当已输入的图标数量能被7整除且不为0时（当前一行填满时），只能点击enter.png和delete.png
                if (clickedLabel == imageLabelDelete) {
                    for (int j = 5; j >= 0; j--) {
                        for (int i = 6; i >= 0; i--) {
                            if (emptyLabels[j][i].getIcon() != iconEmpty) {
                                emptyLabels[j][i].setIcon(iconEmpty);
                                inputIconCount--;
                                return;
                            }
                        }
                    }
                } else if (clickedLabel == imageLabelEnter) {
                    // 检查是否包含等号
                    if (containsEqualSign()) {
                        // 检查已输入的图标数量是否能被7整除
                        if (inputIconCount % 7 == 0) {
                            boolean b = checkMathematicalEquation(emptyLabels);
                            if (b) {
                                int[] matchStatusArray = checkMatchStatusForEquation(targetEquation, emptyLabels);
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
                                JOptionPane.showMessageDialog(GameStart.this, "The left side is not equal to the right", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            // 弹出提示对话框
                            JOptionPane.showMessageDialog(GameStart.this, "Too Short", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // 弹出提示对话框
                        JOptionPane.showMessageDialog(GameStart.this, "No equal '=' sign", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else
            //当前行未填满时
            {
                // 点击其他图标执行相应的操作
                if (clickedLabel == imageLabelDelete) {
                    if (inputIconCount > 0) {
                        for (int j = 5; j >= 0; j--) {
                            for (int i = 6; i >= 0; i--) {
                                if (emptyLabels[j][i].getIcon() != iconEmpty) {
                                    emptyLabels[j][i].setIcon(iconEmpty);
                                    inputIconCount--;
                                    return;
                                }
                            }
                        }
                    }
                } else if (clickedLabel == imageLabelEnter) {
                    // 检查是否包含等号
                    if (containsEqualSign()) {
                        // 检查已输入的图标数量是否能被7整除
                        if (inputIconCount % 7 == 0) {
                            boolean b = checkMathematicalEquation(emptyLabels);
                            if (b) {
                                int[] matchStatusArray = checkMatchStatusForEquation(targetEquation, emptyLabels);
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
                                JOptionPane.showMessageDialog(GameStart.this, "The left side is not equal to the right", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            // 弹出提示对话框
                            JOptionPane.showMessageDialog(GameStart.this, "Too Short", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // 弹出提示对话框
                        JOptionPane.showMessageDialog(GameStart.this, "No equal '=' sign", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    for (int j = 0; j < 6; j++) {
                        for (int i = 0; i < 7; i++) {
                            if (emptyLabels[j][i].getIcon() == iconEmpty) {
                                emptyLabels[j][i].setIcon(correspondingIcon);
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


    // 根据匹配状态数组设置键盘图标颜色和emptyLabels对应图标颜色
    private void updateIconColorsBasedOnMatchStatus(int[] matchStatusArray) {
        boolean allGreen = true;
        for (int i = 0; i < emptyLabels[0].length; i++) {
            char correspondingCharacter = getCharacterFromImageLabel(emptyLabels[numberOfRowDuringGame][i]);
            switch (matchStatusArray[i]) {
                case ICON_COLOR_GREEN:
                    setColorForImageLabel(correspondingCharacter, "Green");
                    emptyLabels[numberOfRowDuringGame][i].setIcon(getGreenIcon(emptyLabels[numberOfRowDuringGame][i]));
                    break;
                case ICON_COLOR_ORANGE:
                    setColorForImageLabel(correspondingCharacter, "Orange");
                    emptyLabels[numberOfRowDuringGame][i].setIcon(getOrangeIcon(emptyLabels[numberOfRowDuringGame][i]));
                    allGreen = false; // At least one icon is not green
                    break;
                case ICON_COLOR_GREY:
                    setColorForImageLabel(correspondingCharacter, "Grey");
                    emptyLabels[numberOfRowDuringGame][i].setIcon(getGreyIcon(emptyLabels[numberOfRowDuringGame][i]));
                    allGreen = false; // At least one icon is not green
                    break;
            }
        }

        // Check if all icons are green
        if (allGreen) {
            // Display success message
            JOptionPane.showMessageDialog(GameStart.this, "Congratulations! You've completed the game successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (numberOfRowDuringGame == emptyLabels.length - 1) {
            // If it's the last row and not all icons are green, display failure message
            JOptionPane.showMessageDialog(GameStart.this, "Game Over! You've failed to complete the game.", "Failure", JOptionPane.ERROR_MESSAGE);
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
            userInput[i] = getCharacterFromImageLabel(label);
        }

        // 将目标方程转换为字符数组
        char[] targetArray = targetEquation.toCharArray();

        // 初始化匹配状态数组
        int[] matchArray = new int[userInput.length];

        // 检查每个图标是否与目标方程的对应字符相匹配
        for (int i = 0; i < userInput.length; i++) {
            if (i < targetArray.length && userInput[i] == targetArray[i]) {
                matchArray[i] = ICON_COLOR_GREEN;
            } else if (i < targetArray.length && targetEquation.contains(String.valueOf(userInput[i]))) {
                matchArray[i] = ICON_COLOR_ORANGE;
            } else {
                matchArray[i] = ICON_COLOR_GREY;
            }
        }
        return matchArray;
    }

    // 检查已输入的图标中是否包含等号
    private boolean containsEqualSign() {
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                if (emptyLabels[j][i].getIcon() == iconEqual) {
                    return true;
                }
            }
        }
        return false;
    }

    // 设置相对应的imageLabel颜色
    private void setColorForImageLabel(char correspondingCharacter, String color) {
        switch (correspondingCharacter) {
            case '1':
                imageLabel1.setIcon(getColoredIconForCharacter('1', color));
                break;
            case '2':
                imageLabel2.setIcon(getColoredIconForCharacter('2', color));
                break;
            case '3':
                imageLabel3.setIcon(getColoredIconForCharacter('3', color));
                break;
            case '4':
                imageLabel4.setIcon(getColoredIconForCharacter('4', color));
                break;
            case '5':
                imageLabel5.setIcon(getColoredIconForCharacter('5', color));
                break;
            case '6':
                imageLabel6.setIcon(getColoredIconForCharacter('6', color));
                break;
            case '7':
                imageLabel7.setIcon(getColoredIconForCharacter('7', color));
                break;
            case '8':
                imageLabel8.setIcon(getColoredIconForCharacter('8', color));
                break;
            case '9':
                imageLabel9.setIcon(getColoredIconForCharacter('9', color));
                break;
            case '0':
                imageLabel0.setIcon(getColoredIconForCharacter('0', color));
                break;
            case '+':
                imageLabelPlus.setIcon(getColoredIconForCharacter('+', color));
                break;
            case '-':
                imageLabelMinus.setIcon(getColoredIconForCharacter('-', color));
                break;
            case '*':
                imageLabelMultiply.setIcon(getColoredIconForCharacter('*', color));
                break;
            case '/':
                imageLabelDivision.setIcon(getColoredIconForCharacter('/', color));
                break;
            case '=':
                imageLabelEqual.setIcon(getColoredIconForCharacter('=', color));
                break;
        }
    }

    // 获取JLabel对应的数字或符号字符
    private char getCharacterFromImageLabel(JLabel label) {
        if (label.getIcon() == icon0) {
            return '0';
        } else if (label.getIcon() == icon1) {
            return '1';
        } else if (label.getIcon() == icon2) {
            return '2';
        } else if (label.getIcon() == icon3) {
            return '3';
        } else if (label.getIcon() == icon4) {
            return '4';
        } else if (label.getIcon() == icon5) {
            return '5';
        } else if (label.getIcon() == icon6) {
            return '6';
        } else if (label.getIcon() == icon7) {
            return '7';
        } else if (label.getIcon() == icon8) {
            return '8';
        } else if (label.getIcon() == icon9) {
            return '9';
        } else if (label.getIcon() == iconPlus) {
            return '+';
        } else if (label.getIcon() == iconMinus) {
            return '-';
        } else if (label.getIcon() == iconMultiply) {
            return '*';
        } else if (label.getIcon() == iconDivision) {
            return '/';
        } else if (label.getIcon() == iconEqual) {
            return '=';
        }
        return '\0';  // 如果没有匹配的图标，返回空字符
    }

    // 根据点击的按钮返回对应的图标
    private ImageIcon getIconFromImageLabel(JLabel sourceLabel) {
        if (sourceLabel == imageLabel1) {
            return icon1;
        } else if (sourceLabel == imageLabel2) {
            return icon2;
        } else if (sourceLabel == imageLabel3) {
            return icon3;
        } else if (sourceLabel == imageLabel4) {
            return icon4;
        } else if (sourceLabel == imageLabel5) {
            return icon5;
        } else if (sourceLabel == imageLabel6) {
            return icon6;
        } else if (sourceLabel == imageLabel7) {
            return icon7;
        } else if (sourceLabel == imageLabel8) {
            return icon8;
        } else if (sourceLabel == imageLabel9) {
            return icon9;
        } else if (sourceLabel == imageLabel0) {
            return icon0;
        } else if (sourceLabel == imageLabelPlus) {
            return iconPlus;
        } else if (sourceLabel == imageLabelMinus) {
            return iconMinus;
        } else if (sourceLabel == imageLabelMultiply) {
            return iconMultiply;
        } else if (sourceLabel == imageLabelDivision) {
            return iconDivision;
        } else if (sourceLabel == imageLabelEqual) {
            return iconEqual;
        } else if (sourceLabel == imageLabelDelete) {
            return iconDelete;
        } else if (sourceLabel == imageLabelEnter) {
            return iconEnter;
        }

        return null;  // 如果没有匹配的按钮，返回null
    }


    // 获取指定颜色的图标
    private ImageIcon getColoredIconForCharacter(char correspondingCharacter, String color) {
        String filePath = "image/" + color + "/";
        switch (correspondingCharacter) {
            case '0':
                return new ImageIcon(filePath + "0.png");
            case '1':
                return new ImageIcon(filePath + "1.png");
            case '2':
                return new ImageIcon(filePath + "2.png");
            case '3':
                return new ImageIcon(filePath + "3.png");
            case '4':
                return new ImageIcon(filePath + "4.png");
            case '5':
                return new ImageIcon(filePath + "5.png");
            case '6':
                return new ImageIcon(filePath + "6.png");
            case '7':
                return new ImageIcon(filePath + "7.png");
            case '8':
                return new ImageIcon(filePath + "8.png");
            case '9':
                return new ImageIcon(filePath + "9.png");
            case '+':
                return new ImageIcon(filePath + "plus.png");
            case '-':
                return new ImageIcon(filePath + "minus.png");
            case '*':
                return new ImageIcon(filePath + "multiply.png");
            case '/':
                return new ImageIcon(filePath + "division.png");
            case '=':
                return new ImageIcon(filePath + "equal.png");
            default:
                return null;
        }
    }

    // 获取绿色图标
    private ImageIcon getGreenIcon(JLabel sourceLabel) {
        // 根据源标签获取对应的数字或符号字符
        char correspondingCharacter = getCharacterFromImageLabel(sourceLabel);

        // 根据字符获取对应的绿色图标
        return getColoredIconForCharacter(correspondingCharacter, "Green");
    }

    // 获取灰色图标
    private ImageIcon getGreyIcon(JLabel sourceLabel) {
        // 根据源标签获取对应的数字或符号字符
        char correspondingCharacter = getCharacterFromImageLabel(sourceLabel);

        // 根据字符获取对应的灰色图标
        return getColoredIconForCharacter(correspondingCharacter, "Grey");
    }

    // 获取橙色图标
    private ImageIcon getOrangeIcon(JLabel sourceLabel) {
        // 根据源标签获取对应的数字或符号字符
        char correspondingCharacter = getCharacterFromImageLabel(sourceLabel);

        // 根据字符获取对应的橙色图标
        return getColoredIconForCharacter(correspondingCharacter, "Orange");
    }

    //Determine whether the input conforms to mathematical equations
    private boolean checkMathematicalEquation(JLabel[][] inputLabels) {
        // 将用户输入的图标转换为对应的字符数组
        char[] userInput = new char[inputLabels[numberOfRowDuringGame].length];
        for (int i = 0; i < inputLabels[numberOfRowDuringGame].length; i++) {
            JLabel label = inputLabels[numberOfRowDuringGame][i];
            userInput[i] = getCharacterFromImageLabel(label);
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
