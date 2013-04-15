import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinTest {

    public static boolean setFullScreenWindow(Window w, boolean fullScreen) {
        X11 x = X11.INSTANCE;
        X11.Display display = null;
        try {
            display = x.XOpenDisplay(null);
            int result = sendClientMessage(
                    display,
                    Native.getWindowID(w),
                    "_NET_WM_STATE",
                    new NativeLong[]{
                            new NativeLong(fullScreen ? _NET_WM_STATE_ADD : _NET_WM_STATE_REMOVE),
                            x.XInternAtom(display, "_NET_WM_STATE_FULLSCREEN", false),
                            new NativeLong(0L),
                            new NativeLong(0L),
                            new NativeLong(0L),
                            new NativeLong(0L)
                    }
            );
            return (result != 0);
        } finally {
            if (display != null) {
                x.XCloseDisplay(display);
            }
        }
    }

    private static int sendClientMessage(X11.Display display, long wid, String msg, NativeLong[] data) {
        assert (data.length == 5);
        X11 x = X11.INSTANCE;
        X11.XEvent event = new X11.XEvent();
        event.type = X11.ClientMessage;
        event.setType(X11.XClientMessageEvent.class);
        event.xclient.type = X11.ClientMessage;
        event.xclient.serial = new NativeLong(0L);
        event.xclient.send_event = 1;
        event.xclient.message_type = x.XInternAtom(display, msg, false);
        event.xclient.window = new X11.Window(wid);
        event.xclient.format = 32;
        event.xclient.data.setType(NativeLong[].class);
        System.arraycopy(data, 0, event.xclient.data.l, 0, 5);
        NativeLong mask = new NativeLong(X11.SubstructureRedirectMask | X11.SubstructureNotifyMask);
        int result = x.XSendEvent(display, x.XDefaultRootWindow(display), 0, mask, event);
        x.XFlush(display);
        return result;
    }
    private static final int _NET_WM_STATE_REMOVE = 0;
    private static final int _NET_WM_STATE_ADD = 1;
    private static boolean fullscreen = false;

    public static void main(String[] argv) {

        final JFrame mainFrame = new JFrame();
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        mainFrame.setContentPane(content);
        JButton button1 = new JButton("Going to fullscreen");
        JButton button2 = new JButton("Show modal dialog");
        content.add(button1);
        content.add(button2);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fullscreen = !fullscreen;
                setFullScreenWindow(mainFrame, fullscreen);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "Test", "Test message", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

    }
}
