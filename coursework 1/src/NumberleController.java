import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NumberleController extends JFrame{
    private NumberleView view;

    private NumberleModel model;

    public NumberleController(NumberleView view) {
        this.view = view;
    }


    // 监听开始新游戏按钮点击事件的监听器
    public class StartNewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 在这里添加逻辑以重新设置游戏状态并开始新游戏
            resetGameAndStartNew();
        }
    }

    //鼠标点击事件监听器（添加键盘按钮功能）
    public class ImageClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel clickedLabel = (JLabel) e.getSource();
            ImageIcon correspondingIcon = view.getIconFromImageLabel(clickedLabel);

            if (model.inputIconCount % 7 == 0 && model.inputIconCount != 0) {
                // 当已输入的图标数量能被7整除且不为0时（当前一行填满时），只能点击enter.png和delete.png
                if (clickedLabel == view.imageLabelDelete) {
                    for (int j = 5; j >= 0; j--) {
                        for (int i = 6; i >= 0; i--) {
                            if (view.emptyLabels[j][i].getIcon() != view.iconEmpty) {
                                view.emptyLabels[j][i].setIcon(view.iconEmpty);
                                model.inputIconCount--;
                                return;
                            }
                        }
                    }
                } else if (clickedLabel == view.imageLabelEnter) {
                    // 检查是否包含等号
                    if (containsEqualSign()) {
                        // 检查已输入的图标数量是否能被7整除
                        if (model.inputIconCount % 7 == 0) {
                            boolean b = model.checkMathematicalEquation(view.emptyLabels);
                            System.out.println(b);
                            if (b) {
                                int[] matchStatusArray = checkMatchStatusForEquation(model.targetEquation, view.emptyLabels);
                                //更改图标颜色
                                model.updateIconColorsBasedOnMatchStatus(matchStatusArray);
                                //清空数组
                                for (int i : matchStatusArray) {
                                    matchStatusArray[i] = 0;
                                }
                                // 清空输入图标计数
                                model.inputIconCount = 0;
                                enableStartNewGameButton();
                                //正在游戏的行数加一
                                model.numberOfRowDuringGame++;
                                System.out.println(model.inputIconCount);
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
                    if (model.inputIconCount > 0) {
                        for (int j = 5; j >= 0; j--) {
                            for (int i = 6; i >= 0; i--) {
                                if (view.emptyLabels[j][i].getIcon() != view.iconEmpty) {
                                    view.emptyLabels[j][i].setIcon(view.iconEmpty);
                                    model.inputIconCount--;
                                    System.out.println(model.inputIconCount);
                                    return;
                                }
                            }
                        }
                    }
                } else if (clickedLabel == view.imageLabelEnter) {
                    // 检查是否包含等号
                    if (containsEqualSign()) {
                        // 检查已输入的图标数量是否能被7整除
                        if (model.inputIconCount % 7 == 0) {
                            boolean b = model.checkMathematicalEquation(view.emptyLabels);
                            System.out.println(b);
                            if (b) {
                                int[] matchStatusArray = checkMatchStatusForEquation(model.targetEquation, view.emptyLabels);
                                //更改图标颜色
                                model.updateIconColorsBasedOnMatchStatus(matchStatusArray);
                                //清空数组
                                for (int i : matchStatusArray) {
                                    matchStatusArray[i] = 0;
                                }
                                // 清空输入图标计数
                                model.inputIconCount = 0;
                                enableStartNewGameButton();
                                //正在游戏的行数加一
                                model.numberOfRowDuringGame++;
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
                                model.inputIconCount++;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    // 启用新游戏按钮
    public void enableStartNewGameButton() {
        view.startNewGameButton.setEnabled(true);
    }

    // 重置游戏状态并开始新游戏
    public void resetGameAndStartNew() {
        // 清空游戏状态
        model.numberOfRowDuringGame = 0;
        model.inputIconCount = 0;
        // 重新选择目标方程
        model.targetEquation = model.getRandomEquationFromList(model.equations);
        System.out.println("New Target Equation: " + model.targetEquation); // 打印新选择的方程（仅供测试）
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

    // 检查用户输入是否匹配目标方程，返回一个匹配状态数组
    public int[] checkMatchStatusForEquation(String targetEquation, JLabel[][] inputLabels) {
        // 将用户输入的图标转换为对应的字符数组
        char[] userInput = new char[inputLabels[model.numberOfRowDuringGame].length];
        for (int i = 0; i < inputLabels[model.numberOfRowDuringGame].length; i++) {
            JLabel label = inputLabels[model.numberOfRowDuringGame][i];
            userInput[i] = view.getCharacterFromImageLabel(label);
        }

        // 将目标方程转换为字符数组
        char[] targetArray = model.targetEquation.toCharArray();

        // 初始化匹配状态数组
        int[] matchArray = new int[userInput.length];

        // 检查每个图标是否与目标方程的对应字符相匹配
        for (int i = 0; i < userInput.length; i++) {
            if (i < targetArray.length && userInput[i] == targetArray[i]) {
                matchArray[i] = view.ICON_COLOR_GREEN;
            } else if (i < targetArray.length && model.targetEquation.contains(String.valueOf(userInput[i]))) {
                matchArray[i] = view.ICON_COLOR_ORANGE;
            } else {
                matchArray[i] = view.ICON_COLOR_GREY;
            }
        }
        return matchArray;
    }

    // 检查已输入的图标中是否包含等号
    public boolean containsEqualSign() {
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                if (view.emptyLabels[j][i].getIcon() == view.iconEqual) {
                    return true;
                }
            }
        }
        return false;
    }


}
