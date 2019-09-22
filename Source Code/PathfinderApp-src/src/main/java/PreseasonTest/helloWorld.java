package PreseasonTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;


public class helloWorld extends JComponent {
    static int flag = 0;
    static ArrayList<Integer> circles = new ArrayList<>();
    static ArrayList<Integer> lines = new ArrayList<>();
    private static long startTime;

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        helloWorld newContentPane = new helloWorld();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newContentPane.setOpaque(false);
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    public helloWorld() {
        circles.add(100);
        circles.add(100);
        circles.add(200);
        circles.add(200);
        circles.add(300);
        circles.add(300);
        circles.add(400);
        circles.add(400);

        lines.add(27);
        lines.add(354);
        lines.add(122);
        lines.add(85);
        lines.add(232);
        lines.add(17);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLayeredPane layeredPane = new JLayeredPane();
        //setLayout(new LayeredPaneManager(layeredPane));
        setPreferredSize(new Dimension(700, 700));
        layeredPane.setOpaque(false);
        //layeredPane.setBounds(0,0,200,200);
        //layeredPane.setPreferredSize(new Dimension(300, 310));
        //Add the ubiquitous "Hello World" label.
        JButton button = new JButton("Hello World");
        button.setBounds(150, 150, 100, 100);
        button.setBackground(Color.GREEN);
        button.setOpaque(true);
        // layeredPane.add(button,  1, 0);

        JLabel label2 = new JLabel("Hello Earth");
        label2.setBounds(130, 130, 50, 30);
        label2.setBackground(Color.RED);
        label2.setOpaque(true);
        //layeredPane.add(label2, 2, 0);

        layeredPane.setBorder(BorderFactory.createTitledBorder("World on Top"));
        layeredPane.add(button, new Integer(1), 0);
        layeredPane.add(label2, new Integer(2), 0);
        showAllCircles.loop(layeredPane);
        //init.curvedLines(layeredPane);
        add(layeredPane);
        button.addActionListener(new ActionListener() {  //Button onClickListener
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawLines.create(layeredPane);
            }
        });
    }

    public static class showAllCircles extends JPanel {
        public static JPanel paintPanel;
        static int x = 0;
        static int y = 0;
        static int numTimesRun = 0;

        public static void loop(JLayeredPane lp) {
            if (circles.size() / 3 == 0 || circles.size() / 3 == 1) {
                numTimesRun = -100;
            } else {
                numTimesRun = circles.size() / 3 - 1;
            }
            paintPanel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    x = 0;
                    y = 0;
                    //Graphics2D g2ds = (Graphics2D) g;
                    while (x < circles.size() / 2) {
                        g.setColor(Color.BLUE);
                        g.fillOval(circles.get(y), circles.get(y + 1), 15, 15);
                        x = x + 1;
                        y = y + 2;
                    }
                    try {
                        int i = 0;
                        while (i < 2) {
                            if(i == 0)
                            g.drawPolyline(extractX(circles, 2), extractY(circles, 2), circles.size() / 2);
                            else if(i == 1)
                            g.drawPolyline(extractX(lines, 2), extractY(lines, 2), lines.size() / 2);
                            i+=1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("circleDrawed");
                }
            };
            paintPanel.setOpaque(false);
            paintPanel.setBounds(0, 0, 2000, 2000);
            lp.add(paintPanel, 10, 0);
            //System.out.println(lp.getComponent(0));
        }

        private static int[] extractX(ArrayList<Integer> circles, int valsPerCircle) {
            ArrayList<Integer> xVals = new ArrayList<>();
            int i = 0;
            while (circles.size() > i) {
                xVals.add(circles.get(i));
                i += valsPerCircle;
            }

            int[] xValToArray = new int[xVals.size()];
            i = 0;
            while (xVals.size() > i) {
                xValToArray[i] = xVals.get(i);
                i += 1;
            }
            System.out.println(xValToArray.length);
            return xValToArray;
        }

        private static int[] extractY(ArrayList<Integer> circles, int valsPerCircle) {
            ArrayList<Integer> yVals = new ArrayList<>();
            int i = 0;
            while (circles.size() > i) {
                yVals.add(circles.get(i + 1));
                i += valsPerCircle;
            }

            int[] yValToArray = new int[yVals.size()];
            i = 0;
            while (yVals.size() > i) {
                yValToArray[i] = yVals.get(i);
                i += 1;
            }
            System.out.println(yValToArray.length);
            return yValToArray;
        }
    }

    public static void viewAllComponents(JLayeredPane lp) {
        int q = 0;
        while (lp.getComponentCount() > q) {
            System.out.println("searched component: " + lp.getComponent(q));
            q = q + 1;
        }
    }

    public static class drawLines extends JPanel {
        public static JPanel linePanel;

        public static void create(JLayeredPane lp) {
            linePanel = new JPanel() {  //Sets paintComponent as JPanel -> JPanel then set on layout
                @Override
                public void paintComponent(Graphics g) {  //Draws circle over JPanel
                    super.paintComponent(g);
                    linePanel.setOpaque(false);
                    linePanel.setVisible(true);
                    Graphics2D g2ds = (Graphics2D) g;
                    g2ds.setColor(Color.BLACK);
                    Line2D.Double line = new Line2D.Double(lines.get(0), lines.get(1),
                            lines.get(2), lines.get(3));
                    g2ds.fill(line);
                    g2ds.draw(line);
                }

            };
            linePanel.setOpaque(false);
            //linePanel.setBounds(0,0,2000,2000);
            if (circles.get(3) > circles.get(1)) {
                linePanel.setBounds(0, 0,
                        lines.get(2), lines.get(3));
            } else {
                linePanel.setBounds(0, 0,
                        lines.get(0), lines.get(1));
            }
            lp.add(linePanel, 20, 0);
            viewAllComponents(lp);

        }
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
