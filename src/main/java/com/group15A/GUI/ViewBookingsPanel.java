package com.group15A.GUI;

import com.group15A.BusinessLogic.ViewBookingLogic;
import com.group15A.CustomExceptions.CustomException;
import com.group15A.CustomExceptions.DatabaseException;
import com.group15A.DataModel.Booking;
import com.group15A.DataModel.Doctor;
import com.group15A.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Filip Fois
 */
public class ViewBookingsPanel extends BasePanel {
    private JButton goHomeButton;
    private JPanel viewBookingsPanel;
    private JPanel contentPanel;
    private JPanel bookingsPanel;
    private JLabel messageLabel;
    private JButton newBookingButton;
    private JScrollPane bookingsScrollPanel;
    private JPanel bookingsDisplayPanel;
    private JLabel titleLabel;

    private ViewBookingLogic viewBookingLogic;
    private List<Booking> bookingsList;
    private List<JLabel> bookingLabelsList;

    /**
     * Constructor for ViewBookingsPanel
     */
    public ViewBookingsPanel(MultiPanelWindow panelController)
    {
        super("View Bookings", "viewBookingPanel", panelController);
       // contentPanel.setBorder(new EmptyBorder(10,10,10,10));
        createActionListeners();

        try {
            bookingLabelsList = new ArrayList<>();
            viewBookingLogic = new ViewBookingLogic();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(
                    viewBookingsPanel,
                    "Please connect to the database and restart the program.",
                    "ERROR: Database not connected",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }
    }

    /**
     * @param pair the received data from another page
     */
    @Override
    public void receiveData(ReceivePair pair)
    {
        if (pair.getFirst().equals(ReceiveType.PATIENT_ID)) {
            Integer patientID = (Integer) pair.getSecond();
            try {
                bookingsList = this.viewBookingLogic.getBookings(patientID);
                this.messageLabel.setVisible(false);
                this.titleLabel.setText("Your bookings (" + bookingsList.size() + ")");
                this.displayBookings();
            } catch (CustomException e) {
                JOptionPane.showMessageDialog(
                        viewBookingsPanel,
                        "Please connect to the database and restart the program.",
                        "ERROR: Database not connected",
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(0);
            }
        }
    }

    public void displayBookings() throws CustomException {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;

        Color color1 = new Color(144, 176, 30);
        Color color2 = new Color(30, 176, 132);

        Boolean colorFlag = true;
        for (Booking b : bookingsList) {
            Doctor doctor = this.viewBookingLogic.getDoctor(b.getDoctorID());
            //TODO: Refactor bookingLabel as its own GUI form
            JLabel bookingLabel = new JLabel();
            bookingLabel.setText(DataModification.fullDate(b.getBookingTime())+"\n with Dr "+doctor.getFullName());
            bookingLabel.setFont(new Font("", Font.BOLD, 25));
            bookingLabel.setForeground(colorFlag ? color1 : color2);
            bookingLabel.setBorder(BorderFactory.createLineBorder(colorFlag ? color1 : color2, 2));
            bookingLabel.setHorizontalAlignment(JLabel.CENTER);

            bookingLabelsList.add(bookingLabel);
            bookingsDisplayPanel.add(bookingLabel, gbc);

            colorFlag = !colorFlag;
        }
    }

    /**
     * @return viewBookingsPanel
     */
    @Override
    public JPanel getPagePanel()
    {
        return this.viewBookingsPanel;
    }

    /**
     * To create all event handlers, which will point to other methods in the class
     */
    @Override
    public void createActionListeners()
    {
        goHomeButton.addActionListener(e -> {panelController.showPage(PageType.HOME);});
        newBookingButton.addActionListener(e -> {
            panelController.showPage(
                    PageType.ADD_BOOKING,
                    new ReceivePair(ReceiveType.DOCTOR, this.panelController.getSession().getLoggedInPatientID())
            );
        });

    }

}