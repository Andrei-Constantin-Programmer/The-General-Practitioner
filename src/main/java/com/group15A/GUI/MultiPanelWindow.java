package com.group15A.GUI;

import com.group15A.Session;
import com.group15A.Utils.PageInfo;
import com.group15A.Utils.ReceivePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The window which will be shown, consists of a card layout
 * which can switch between different JPanels (pages)
 *
 * cardLayout is needed to add pages to be switched between
 *
 * cards is the list of JPanels to be stored
 *
 * panelCards is the parent that holds all JPanels, of which
 * its layout is cardLayout
 *
 * @author Milovan Gveric
 * @author Filip Fois
 */
public class MultiPanelWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelCards;
    private Map<PageInfo, BasePanel> cards;
    private Session session;

    /**
     * Constructor for the MultiPanelWindow class
     *
     * Stores all pages,
     * sets default window size,
     * and to a certain page if the session file is still stored
     */
    public MultiPanelWindow() {
        this.session = new Session(null, false);
        createPages();

        // Run closeProgram() when window close button is clicked
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeProgram();
            }
        });

        this.setContentPane(panelCards);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(
                (int)(dimension.getWidth()*0.6), // Make window width 60% that of the screen
                (int)(dimension.getHeight()*0.8) // Make window height 80% that of the screen
        );

        // Choose the page to be displayed when starting the program
        File file = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"/LoggedUser.bin");
        PageInfo pageToShow = PageInfo.LOGIN; // log in page
        if(file.exists()) {
            pageToShow = PageInfo.HOME; // home page
        }
        showPage(pageToShow);
    }


    private void createPages() {
        this.cards = new HashMap<>();
        this.cards.put(PageInfo.LOGIN, new LogInPanel(this));
        this.cards.put(PageInfo.REGISTER, new RegisterPanel(this));
        this.cards.put(PageInfo.HOME, new HomePanel(this));
        this.cards.put(PageInfo.CHOOSE_DOCTOR, new ChooseDoctorPanel(this));

        PageInfo[] pages = PageInfo.values();

        this.cardLayout = (CardLayout) (panelCards.getLayout());
        for (PageInfo page: pages) {
            BasePanel bspanel = this.cards.get(page);
            this.panelCards.add(bspanel.getPagePanel(), bspanel.getPanelFieldName());
        }
    }

    /**
     * Switches to a given JPanel that is in the card layout
     *
     * @param page the page to switch to, contains window title and the required JPanel
     */
    public void showPage(PageInfo page, ReceivePair... pairs) {
        BasePanel nextPanel = this.cards.get(page);
        this.setTitle(nextPanel.getWindowTitle());
        this.cardLayout.show(panelCards, nextPanel.getPanelFieldName());
        for (ReceivePair pair: pairs) {
            this.cards.get(page).receiveData(pair);
        }
    }

    public Session getSession() {
        return session;
    }

    /**
     * When the close window button is clicked,
     * the user will be logged out (if they don't want to stay logged in),
     * and the program will terminate.
     */
    public void closeProgram()
    {
        // Delete session file if user doesn't want to stay logged in (i.e. log out user)
        // TODO: Delete session if user doesn't want to stay logged in.
        //---
        // TODO: Access `session.keepLoggedIn`.
//        Session session = new Session();
//        if(!session.keepLoggedIn) {
//            try {
//                File sessionFile = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/LoggedUser.bin");
//                sessionFile.delete();
//            }
//            catch (Exception e){
//                JOptionPane.showMessageDialog(
//                        panelCards,
//                        "Could not delete session file.",
//                        "ERROR: Log out failed",
//                        JOptionPane.ERROR_MESSAGE
//                );
//                System.exit(0);
//            }
//        }
        //---


        // Exit program
        System.exit(0);
    }

}
