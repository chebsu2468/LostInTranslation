package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class GUI {

    public static void main(String[] args) {
        final String[] temp = {""};
        SwingUtilities.invokeLater(() -> {

            JSONTranslator translator = new JSONTranslator();
            String[] countryCodes = translator.getCountryCodes().toArray(new String[0]);
            CountryCodeConverter convert = new CountryCodeConverter();

            for(int i = 0; i < countryCodes.length; i++){
                countryCodes[i] = convert.fromCountryCode(countryCodes[i].toLowerCase());
            }

            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new BorderLayout());
            countryPanel.add(new JLabel("Country:"), BorderLayout.NORTH);

            JList<String> countryList = new JList<>(countryCodes);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane countryScrollPane = new JScrollPane(countryList);
            countryPanel.add(countryScrollPane, BorderLayout.CENTER);

            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int[] indices = countryList.getSelectedIndices();
                    String[] selectedCountries = new String[indices.length];
                    for (int i = 0; i < indices.length; i++) {
                        selectedCountries[i] = countryList.getModel().getElementAt(indices[i]);
                    }
                    temp[0] = selectedCountries.length > 0 ? selectedCountries[0] : "";
                }
            });

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            LanguageCodeConverter converter = new LanguageCodeConverter();

            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String countryCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(converter.fromLanguageCode(countryCode));
            }
            languagePanel.add(languageComboBox);

            languageComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String language = languageComboBox.getSelectedItem().toString();
                        // still using temp[0] for country, language handled separately
                    }
                }
            });

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String country = temp[0];
                    String language = languageComboBox.getSelectedItem().toString();

                    Translator translator1 = new JSONTranslator();

                    String result = translator1.translate(country, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);
                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(countryPanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }

}
