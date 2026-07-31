# PuzzleGame

一个使用 Java Swing 编写的单机拼图游戏。

## 功能

- 随机打乱拼图并保证可解
- 鼠标点击交换拼图块
- 按键查看完整图片，松开后恢复拼图进度
- 计步与胜利提示

## 运行环境

- JDK 8 或更高版本

主类：`Puzzle_Game.APP`

## 从源码运行

在项目根目录执行：

```powershell
javac -encoding UTF-8 -d out src\Puzzle_Game\*.java
Copy-Item src\Puzzle_Game\Photos out\Puzzle_Game\Photos -Recurse
java -cp out Puzzle_Game.APP
```
