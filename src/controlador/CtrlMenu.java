/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import conexion.ClaseDatos;
import vista.FrmMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author jjrodmar
 */
public class CtrlMenu implements ActionListener {

    FrmMenu frmmenu = null;
    private ClaseDatos _cldatos = null;

    public CtrlMenu() {
        this._cldatos = new ClaseDatos();
        this.frmmenu = new FrmMenu();
        this.frmmenu.setLocationRelativeTo(null);
        this.frmmenu.setResizable(false);
        this.frmmenu.setTitle("Mi Librería");
        RegistrarEventos();
        this.frmmenu.setVisible(true);

    }

    private void RegistrarEventos() {

        frmmenu.getBtnLibros().addActionListener(this);
        frmmenu.getBtnSalir().addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frmmenu.getBtnLibros()) {
            CtrlLibro ctrllibro = new CtrlLibro(frmmenu, _cldatos, true);
        } else if (e.getSource() == frmmenu.btnSalir) {
            salir();
        }
    }

    private void salir() {
        try {
            // Close the database
            this._cldatos.Cerrar();
        } catch (Exception e) {
            this._cldatos = null;
        }
        // Exit
        System.exit(0);
    }
}
