import javax.swing.*;

public class GUIApp extends JFrame{
    public static void main(String[] args) {
        // 初始化 NumberleView 类的实例
        NumberleView view = new NumberleView();
        NumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController();

        System.out.println("Target Equation: " + controller.targetEquation);// 打印选择的方程（仅供测试）

        view.initializeJFrame();//初始化界面

        view.initializeIcons(); // 初始化图标对象

        view.initializeGameInterface();//初始化游戏界面

        view.initializeStartNewGameButton(); // 初始化开始新游戏按钮

        view.setVisible(true);//显示界面

    }
}
