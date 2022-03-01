package com.group15A.GUI;

import com.group15A.BusinessLogic.DoctorLogic;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataModel.Doctor;
import com.group15A.Utils.PageType;
import com.group15A.Utils.ReceivePair;
import com.group15A.Utils.ReceiveType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * To allow for communication to the business layer and to take care of event handling
 *
 * chooseDoctorPanel is the actual panel that gets passed to the multiPanelWindow cardLayout
 * in order to show it in the UI
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class ChooseDoctorPanel extends BasePanel {
    private JPanel chooseDoctorPanel;
    private JPanel contentPanel;
    private JButton registerButton;
    private JPanel doctorListPanel;
    private JScrollPane doctorListScrollPane;

    private PageType returningPage;
    private DoctorLogic doctorLogic;
    private List<Doctor> doctorsList;
    private List<JButton> doctorButtons;

    /**
     * Constructor for the ChooseDoctorPanel class
     *
     * Creates action listeners for widgets
     *
     * @param panelController the instance of multiPanelWindow in order for
     *                        events from this panel to call showPage
     */
    public ChooseDoctorPanel(MultiPanelWindow panelController)
    {
        super("Choose Your New Doctor", "chooseDoctorPanel", panelController);
        createActionListeners();

        try {
            doctorLogic = new DoctorLogic();
            doctorButtons = new ArrayList<JButton>();
            doctorsList = doctorLogic.getDoctors();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(
                    chooseDoctorPanel,
                    "Please connect to the database and restart the program.",
                    "ERROR: Database not connected",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }

        addDoctorsToPanel();
    }

    /**
     * Dynamically add buttons with doctors' first and last names to the
     * scroll pane
     */
    public void addDoctorsToPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;

        for (Doctor d : doctorsList) {
            JButton doctorButton = new JButton();
            doctorButton.setText(d.getFirstName()+" "+d.getLastName());
            doctorButton.setFont(new Font("", Font.BOLD, 30));

            doctorButtons.add(doctorButton);
            doctorListPanel.add(doctorButton, gbc);
        }
    }

    /**
     * @return chooseDoctorPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.chooseDoctorPanel;
    }

    /**
     * Receives:
     *  - Which page to switch back to (as Choose Doctor Panel is shared)
     *
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair) {
        if (pair.getFirst().equals(ReceiveType.RETURN_PAGE)) {
            this.returningPage = (PageType) pair.getSecond();
        }
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        //logInButton.addActionListener( e -> panelController.showPage(Page.LOGIN));
        registerButton.addActionListener( e -> panelController.showPage(this.returningPage));
    }

}
