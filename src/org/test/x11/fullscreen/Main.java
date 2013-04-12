package org.test.x11.fullscreen;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Vladimir Kravets
 * E-Mail: vova.kravets@gmail.com
 * Date: 4/12/13
 * Time: 4:11 PM
 */
public class Main {

    public static void main(String[] argv) {
        TestForm test = new TestForm();
        test.pack();
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        test.setVisible(true);
    }

}
