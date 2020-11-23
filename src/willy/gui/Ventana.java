/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package willy.gui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Willy
 */
public abstract class Ventana extends JFrame implements WindowConstants {

    public final JPanel p;
    private boolean haveCompBeenSet = false;

    public Ventana(final String title, final int w, final int h, final boolean resizable) {
        super(title);

        p = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(w, h);
            }
        };

        super.setContentPane(p);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setResizable(resizable);
    }

    public final void mostrar() {
        if (!super.isVisible()) {
            if (!haveCompBeenSet) {
                setComp();
                haveCompBeenSet = true;
            }
            super.pack();
            super.setLocationRelativeTo(null);
            super.setVisible(true);
        }
    }

    public final void addComp(Component cmpt) {
        p.add(cmpt);
    }

    public final void addComp(Component cmpt, Object obj) {
        p.add(cmpt, obj);
    }

    public final void addComp(Component cmpt, int i) {
        p.add(cmpt, i);
    }

    public abstract void setComp();

}
