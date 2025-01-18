package utils.configuration;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.FileNotFoundException;

public class ConfigLoader extends DefaultHandler {

    private String currentElement;
    private TypeEquipement currentEquipement;


   private static final Map<String, ProprieteEquipement> propertyNameToEnum = new HashMap<>();

    static {
        propertyNameToEnum.put("porteeArme", ProprieteEquipement.PORTEE);
        propertyNameToEnum.put("degatEnergieArme", ProprieteEquipement.DEGATENERGIE);
        propertyNameToEnum.put("energieAvecBouclierArme", ProprieteEquipement.ENERGIE_AVEC_BOUCLIER);
        propertyNameToEnum.put("munitionArme", ProprieteEquipement.MUNITION);
        
        propertyNameToEnum.put("delaiExplosionExplosif", ProprieteEquipement.DELAI_EXPLOSION);
        propertyNameToEnum.put("impactEnergieExplosif", ProprieteEquipement.IMPACT_ENERGIE);
        propertyNameToEnum.put("impactEnergieAvecBouclierExplosif", ProprieteEquipement.ENERGIE_AVEC_BOUCLIER);
        propertyNameToEnum.put("visibiliteExplosif", ProprieteEquipement.VISIBILITE);
    }

    /**
     * Charge le fichier XML et initialise les paramètres du jeu.
     *
     * @param filePath Chemin du fichier XML.
     * @throws SAXException En cas de problème de parsing.
     */
    public static void load() throws SAXException {
        try {
            ConfigLoader handler = new ConfigLoader();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Charger le fichier config.xml depuis le classpath
            InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("config/config.xml");
            if (inputStream == null) {
                throw new FileNotFoundException("Fichier de configuration 'config/config.xml' non trouvé dans le classpath.");
            }

            saxParser.parse(inputStream, handler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SAXException("Erreur lors du chargement du fichier de configuration.");
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    	currentElement = (localName != null && !localName.isEmpty()) ? localName : qName;

        TypeEquipement equipement = detecterTypeEquipement(currentElement);
        if (equipement != null) {
            currentEquipement = equipement;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (value.isEmpty()) {
            return;
        }

        traiterElement(value);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    	String elementName = (localName != null && !localName.isEmpty()) ? localName : qName;

        TypeEquipement equipement = detecterTypeEquipement(elementName);
        if (equipement != null && equipement == currentEquipement) {
            currentEquipement = null;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("\n Configuration finale : ");
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "\nGrille : largeur=" + Parametres.largeur + ", longueur=" + Parametres.longueur +
                ", nombreMinMurs=" + Parametres.nombreMinMurs + ", nombreMaxMurs=" + Parametres.nombreMaxMurs +
                "\nPastilles : nombreMinPastilles=" + Parametres.nombreMinPastilles + ", nombreMaxPastilles=" + Parametres.nombreMaxPastilles +
                ", energiePastille=" + Parametres.energiePastille +
                "\nCombattants : energieInitiale=" + Parametres.energieInitiale +
                "\nCoûts des actions : déplacement=" + Parametres.coutDeplacement + ", bouclier=" + Parametres.coutUtilisationBouclier +
                "\nMunitions limite=" + Parametres.munitionLimite +
                "\nÉquipements : " + Parametres.equipements+"\n";
    }

    /**
     * Détecte le type d'équipement ou d'explosif basé sur le nom de l'élément.
     *
     * @param element Le nom de l'élément XML.
     * @return Le type d'équipement ou null si aucun.
     */
    private TypeEquipement detecterTypeEquipement(String element) {
        try {
            return TypeEquipement.valueOf(element.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return null; // Aucun type d'équipement trouvé pour cet élément
        }
    }

    /**
     * Traite les éléments XML en fonction de leur type et de leur contexte.
     *
     * @param value La valeur de l'élément.
     */
    private void traiterElement(String value) {
        switch (currentElement) {
            case "largeur":
                Parametres.largeur = Integer.parseInt(value);
                break;

            case "longueur":
                Parametres.longueur = Integer.parseInt(value);
                break;

            case "nombreMinMur":
                Parametres.nombreMinMurs = Integer.parseInt(value);
                break;

            case "nombreMaxMur":
                Parametres.nombreMaxMurs = Integer.parseInt(value);
                break;

            case "nombreMinPastille":
                Parametres.nombreMinPastilles = Integer.parseInt(value);
                break;

            case "nombreMaxPastille":
                Parametres.nombreMaxPastilles = Integer.parseInt(value);
                break;

            case "energiePastille":
                Parametres.energiePastille = Integer.parseInt(value);
                break;

            case "energieInitiale":
                Parametres.energieInitiale = Integer.parseInt(value);
                break;

            case "deplacement":
                Parametres.coutDeplacement = Integer.parseInt(value);
                break;

            case "utilisationBouclier":
                Parametres.coutUtilisationBouclier = Integer.parseInt(value);
                break;

            case "nombreLimite":
                Parametres.munitionLimite = Integer.parseInt(value);
                break;

            default:
                if (propertyNameToEnum.containsKey(currentElement)) {
                    if (currentEquipement != null) {
                        ProprieteEquipement propriete = propertyNameToEnum.get(currentElement);
                        Parametres.initialiserEquipement(currentEquipement);
                        Parametres.ajouterPropriete(currentEquipement, propriete, parseValue(propriete, value));
                    } else {
                        System.err.println("Aucun équipement courant défini pour la propriété : " + currentElement);
                    }
                }
                break;
        }
    }

    /**
     * Parse une valeur en fonction de la propriété attendue.
     *
     * @param propriete La propriété à analyser.
     * @param value     La valeur sous forme de chaîne.
     * @return La valeur convertie dans le type attendu.
     */
    private Object parseValue(ProprieteEquipement propriete, String value) {
        try {
            switch (propriete) {
                case VISIBILITE:
                    return value; 
                default:
                    return Integer.parseInt(value); 
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion pour la valeur : " + value + " et la propriété : " + propriete);
            return null;
        }
    }
}
